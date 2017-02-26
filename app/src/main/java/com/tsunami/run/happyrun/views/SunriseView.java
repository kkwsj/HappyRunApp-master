package com.tsunami.run.happyrun.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;


import com.tsunami.run.happyrun.R;
import com.tsunami.run.happyrun.utils.weather.WeatherDataUtil;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * Created by SHEN XIAOMING on 2016/7/24.
 */
public class SunriseView extends View {

    private static final String TAG = "SunriseView";

    // 图片
    private Bitmap bitmap;
    // 画笔
    private Paint mPaint1, mPaint2;
    // View宽高
    private int mWidth;
    private int mHeight;

    // 圆的半径
    private int radius;
    // 圆心坐标
    private Point circlePoint;
    // bitmap中心
    private Point center;

    private String sunriseStr;
    private String sunsetStr;

    private int sunriseTime;
    private int sunsetTime;
    private int currentTime;

    private float dayRate;

    private ValueAnimator animator;

    private int ratio = 0;

    private int sun_state;

    public SunriseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public SunriseView(Context context) {
        super(context);
        initView();
    }

    public SunriseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
//        mDrawable = ContextCompat.getDrawable(getContext(), R.drawable.sun);
//        mDrawable = getResources().getDrawable(R.drawable.sun, null);
        // 加载图片
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sun);
        // bitmap缩放
        bitmap = Bitmap.createScaledBitmap(bitmap, 30, 30, true);
        // 画笔设置
        mPaint1 = new Paint();
        mPaint1.setStyle(Paint.Style.FILL); // 实线
        mPaint1.setStrokeWidth(2);
        mPaint2 = new Paint();
        mPaint2.setStyle(Paint.Style.STROKE);
        mPaint2.setStrokeWidth(2);
        mPaint2.setAntiAlias(true);
        mPaint2.setPathEffect(new DashPathEffect(   // 虚线效果
                new float[]{5, 5}, 0));

        // 获取日出日落时间
        sunriseStr = WeatherDataUtil.getSunrise();
        sunsetStr = WeatherDataUtil.getSunset();
        // 日出日落时间化为分钟
        String hour = sunriseStr.substring(0, 2);
        String minute = sunriseStr.substring(3, 5);
        sunriseTime = Integer.parseInt(hour) * 60 + Integer.parseInt(minute);
        hour = sunsetStr.substring(0, 2);
        minute = sunsetStr.substring(3, 5);
        sunsetTime = Integer.parseInt(hour) * 60 + Integer.parseInt(minute);
        // 获取当前时间
        long time = System.currentTimeMillis();
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        String currentTimeStr = format.format(date);
        hour = currentTimeStr.substring(0, 2);
        minute = currentTimeStr.substring(3, 5);
        currentTime = Integer.parseInt(hour) * 60 + Integer.parseInt(minute);
        // 目前已到白天的时间比重
        dayRate = 1.0f * (currentTime - sunriseTime) / (sunsetTime - sunriseTime);
        Log.e(TAG, "" + dayRate);

        if (currentTime <= sunriseTime) {
            sun_state = 0;
        } else if (currentTime >= sunsetTime) {
            sun_state = 2;
        } else {
            sun_state = 1;
        }

        center = new Point();

        animator = ValueAnimator.ofInt(0, (int)(100 * dayRate));
        if (dayRate < 0.5f) {
            if (dayRate < 0.25f) {
                animator.setDuration(500);
            } else {
                animator.setDuration(1000);
            }
        } else {
            if (dayRate < 0.75f) {
                animator.setDuration(1500);
            } else {
                animator.setDuration(2000);
            }
        }
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawLine((int) (mWidth * 0.1f), mHeight - radius * 0.1f,
                (int) (mWidth * 0.9f), mHeight - radius * 0.1f, mPaint1);
        canvas.drawArc(new RectF(mWidth / 2 - radius, mHeight - radius * 1.1f,
                mWidth / 2 + radius, mHeight + radius * 0.9f), 0, -180, true, mPaint2);
//        canvas.drawArc(mWidth / 2 - radius, mHeight - radius * 1.1f,
//                mWidth / 2 + radius, mHeight + radius * 0.9f,
//                0, -180, true, mPaint2);

        switch (sun_state) {
            case 0: // 当前时间早于日出
                center.x = circlePoint.x - radius;
                center.y = circlePoint.y;
                break;
            case 1: // 当前时间日出日落之间
                if (ratio < 100 * dayRate) {
                    center.x = circlePoint.x - (int)(radius * Math.cos(Math.PI * ratio / 100));
                    center.y = circlePoint.y - (int) (radius * Math.sin(Math.PI * ratio / 100));
                    canvas.drawBitmap(bitmap, center.x - bitmap.getWidth() / 2, center.y - bitmap.getHeight() / 2, mPaint1);
                    ratio++;
                    invalidate();
                }
                break;
            case 2: // 当前时间晚于日落
                if (ratio <= 100) {
                    center.x = circlePoint.x - (int)(radius * Math.cos(Math.PI * ratio / 100));
                    center.y = circlePoint.y - (int) (radius * Math.sin(Math.PI * ratio / 100));
                    canvas.drawBitmap(bitmap, center.x - bitmap.getWidth() / 2, center.y - bitmap.getHeight() / 2, mPaint1);
                    ratio++;
                    invalidate();
                }
                break;
        }
        canvas.drawBitmap(bitmap, center.x - bitmap.getWidth() / 2, center.y - bitmap.getHeight() / 2, mPaint1);

        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = super.getMeasuredWidth();
        mHeight = super.getMeasuredHeight();

        radius = (int) (mWidth / 2 * 0.7f);
        circlePoint = new Point(mWidth / 2, mHeight - (int)(radius * 0.1f));
    }
}
