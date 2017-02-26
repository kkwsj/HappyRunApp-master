package com.tsunami.run.happyrun.activities;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.tsunami.run.happyrun.R;
import com.tsunami.run.happyrun.dbs.MyRunDatabaseHelper;
import com.tsunami.run.happyrun.utils.MapUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GaoDeMapActivity extends AppCompatActivity implements View.OnClickListener {
    //定位
    private static final String TAG = "Map";
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = new AMapLocationClientOption();
    private List<LatLng> latLngs = new ArrayList<LatLng>(); //保存坐标
    private boolean isFirstLoc = true;
    //地图
    MapView mMapView = null;
    private AMap aMap;
    UiSettings uiSettings = null;
    //数据库
    private MyRunDatabaseHelper dbHelper;
    //资源
    private TextView text_time = null;
    private TextView text_distance = null;
    private Button btn_stopTrace = null;
    //折线颜色数据
    Integer[] colorData = new Integer[4];
    List<Integer> colorList = new ArrayList<Integer>();//用一个数组来存放颜色，渐变色，四个点需要设置四个颜色
    private int colorCount = 0;
    //日期数据
    String date = "";
    //计时器

    //kkwsj
    private boolean isRunning = false;

    private int second = 0;  //总秒数
    private final Timer timer = new Timer();
    private String duration; //格式化总秒数
    //距离
    private float distance = 0;
    //异步线程
    private final Handler myHandler = new Handler() {
        @Override
        //重写handleMessage方法,根据msg中what的值判断是否执行后续操作
        public void handleMessage(Message msg) {
            if (msg.what == 0) {

                int hour = GaoDeMapActivity.this.second / 60 / 60;
                int minute = (GaoDeMapActivity.this.second - hour * 60 * 60) / 60;
                int second = GaoDeMapActivity.this.second - hour * 60 * 60 - minute * 60;
                if (second % 59 == 0) {
                    PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
                    // 点亮屏幕
                    PowerManager.WakeLock pm_wl = pm.newWakeLock(
                            PowerManager.ACQUIRE_CAUSES_WAKEUP
                                    //            PowerManager.PARTIAL_WAKE_LOCK,
                                    | PowerManager.SCREEN_DIM_WAKE_LOCK,  //这个flag可以阻止休眠
                            ////  |PowerManager.SCREEN_BRIGHT_WAKE_LOCK ,
                            // | PowerManager.FULL_WAKE_LOCK,
                            TAG);
                    pm_wl.acquire();
                    pm_wl.release();//发出命令
                }

                duration = (hour > 9 ? hour : "0" + hour) + ":" +
                        (minute > 9 ? minute : "0" + minute) + ":" +
                        (second > 9 ? second : "0" + second);
                text_time.setText(duration);
                BigDecimal b = new BigDecimal(distance);
                float temp = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
                text_distance.setText(String.valueOf(temp));//格式化数据
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_map);


        Toolbar toolbar = (Toolbar) findViewById(R.id.secondtimertoolbar);
        setSupportActionBar(toolbar);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            // 2 添加的代码
            getSupportActionBar().setTitle("跑步");
            /////////////////////////////////
        }
        //定位 初始化
        init();
        //设置定时器信息
        setCountTimer();

        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，实现地图生命周期管理
        mMapView.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mMapView.getMap();
            //不允许显示缩放按钮
            uiSettings = aMap.getUiSettings();
            uiSettings.setZoomControlsEnabled(false);
        }

    }

    private void setCountTimer() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                myHandler.sendEmptyMessage(0);
                // kkwsj
                if(isRunning) {
                    second++;//秒数加一
                }
            }
        };
        timer.schedule(task, 0, 1000);// 1秒一次
    }

    private void init() {
        //初始化layout资源
        initLayoutResource();
        //初始化数据库文件
        initDb();
        //初始化定位
        initLocation();
        //启动服务
        startLocation();
        //初始化颜色数据
        initColor();
        //初始化日期数据
        initDate();
    }

    private void initDate() {
        //日期数据
        final Calendar calendar = Calendar.getInstance();
        int mYear = calendar.get(Calendar.YEAR); //获取当前年份
        int mMonth = calendar.get(Calendar.MONTH) + 1;//获取当前月份
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);//获取当前月份的日期号码
        int mHor = calendar.get(Calendar.HOUR_OF_DAY);//获取小时数据
        int mMinute = calendar.get(Calendar.MINUTE);//获取分钟数据
        date = String.valueOf(mYear) + "." +
                (mMonth > 9 ? String.valueOf(mMonth) : "0" + String.valueOf(mMonth)) + "." +
                (mDay > 9 ? String.valueOf(mDay) : "0" + String.valueOf(mDay)) + ".  " +
                (mHor > 9 ? String.valueOf(mHor) : "0" + String.valueOf(mHor)) + "." +
                (mMinute > 9 ? String.valueOf(mMinute) : "0" + String.valueOf(mMinute)) + "";
    }

    private void initColor() {
        colorData[0] = Color.argb(255, 255, 215, 0);
        colorData[1] = Color.argb(255, 255, 69, 0);
        colorData[2] = Color.argb(255, 60, 179, 113);
        colorData[3] = Color.argb(255, 0, 206, 209);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.stopTrace:
                if(!isRunning) {
                    btn_stopTrace.setText("暂停");
                    isRunning = true;
                } else {
                    showCompleteDialog();
                }
                //stopTrace();
                break;
        }
    }

    public void showCompleteDialog(){
        LayoutInflater factory = LayoutInflater.from(GaoDeMapActivity.this);//提示框
        final View view = factory.inflate(R.layout.dialog_edit, null);//这里必须是final的
        final EditText edit=(EditText)view.findViewById(R.id.editText);//获得输入框对象

        new AlertDialog.Builder(GaoDeMapActivity.this)
                .setTitle("是否已经完成？")//提示框标题
                //.setView(view)
                .setPositiveButton("确定",//提示框的两个按钮
                        new android.content.DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                //事件
                                stopTrace();
                                GaoDeMapActivity.this.finish();
                            }
                        }).setNegativeButton("取消",
                new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //事件
                        btn_stopTrace.setText("继续");
                        isRunning = false;
                    }
                }).create().show();

    }

    private void initLayoutResource() {
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.map);
        text_time = (TextView) findViewById(R.id.time);
        text_distance = (TextView) findViewById(R.id.distance);
        btn_stopTrace = (Button) findViewById(R.id.stopTrace);
        btn_stopTrace.setOnClickListener(this);
    }

    private void initDb() {
        dbHelper = new MyRunDatabaseHelper(this, "record.db", null, 1);//创建数据库

    }

    private void startLocation() {
        // 启动定位
        locationClient.startLocation();
    }

    private void initLocation() {
        //初始化client
        locationClient = new AMapLocationClient(this.getApplicationContext());
        //设置定位参数 这里参数应该都是默认的
        // locationClient.setLocationOption(getDefaultOption());
        locationClient.setLocationOption(locationOption);
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
    }

    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(true);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(5000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是ture
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        return mOption;
    }

    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation loc) {
            if (null != loc) {
                //解析定位结果
                String result = MapUtils.getLocationStr(loc);
                Log.d(TAG, result);

                if (loc.getErrorCode() == 0) {
                    //将定位坐标存入List中
                    latLngs.add(new LatLng(loc.getLatitude(), loc.getLongitude()));
                }
                    if (isFirstLoc) {
                        //设置缩放级别
                        aMap.moveCamera(CameraUpdateFactory.zoomTo(16));
                        //第一次快速定位后修正刷新间隔为5秒
                        locationClient.setLocationOption(getDefaultOption());
                        isFirstLoc = false;
                    } else {
                        //填充距离信息 单位公里
                        distance += AMapUtils.calculateLineDistance(latLngs.get(latLngs.size() - 2), latLngs.get(latLngs.size() - 1)) / 1000;
                    }

                    //将地图移动到定位点
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(loc.getLatitude(), loc.getLongitude())));
                    //添加覆盖物
                    addMarker(new LatLng(loc.getLatitude(), loc.getLongitude()));


                } else {
                    Log.d(TAG, "定位失败，loc is null");
                }
            }
        }

        ;

        private void addMarker(LatLng latLng) {
            //清空标记物
            aMap.clear();
            //处理线段渐变色数据
            colorCount++;
            //每10个点轮换一次颜色 避免颜色变换太密集 不过估计想好看点需要颜色取得科学些
            colorList.add(colorData[(colorCount % 40) / 10]);
            //添加折线
            aMap.addPolyline(new PolylineOptions().
                    useGradient(true). //使用渐变色
                    addAll(latLngs).
                    colorValues(colorList).
                    //  color(Color.argb(255,255,215,0)).
                            width(30));
            //添加标记点
            final Marker marker = aMap.addMarker(new MarkerOptions().
                    position(latLng));
        }

        //停止轨迹追踪
        private void stopTrace() {
            timer.cancel();   //停止计时
            createDbRecord(); //创建表数据
            // this.finish();    //销毁页面
            //以下为测试数据用
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.query("record", null, null, null, null, null, null);
            if (cursor.moveToFirst()) {

                String date = cursor.getString(cursor.getColumnIndex("date"));
                Log.d(TAG, date);
                String p = cursor.getString(cursor.getColumnIndex("points"));
                Log.d(TAG, p);
            }

        }

        private void createDbRecord() {
            //整合点数据
            String Points = "";
            for (int i = 0; i < latLngs.size(); i++) {
                Points += String.valueOf(latLngs.get(i).latitude) + ","
                        + String.valueOf(latLngs.get(i).longitude) + ";";

            }
            //平均速度数据
            int hour = GaoDeMapActivity.this.second / 60 / 60;
            int minute = (GaoDeMapActivity.this.second - hour * 60 * 60) / 60;
            float averagespeed = 0;
            if (minute != 0)
                averagespeed = distance / (float) minute;

            //格式化平均速度
            BigDecimal b = new BigDecimal(averagespeed);
            float temp_averagespeed = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
            //格式化距离数据 2位小数
            BigDecimal c = new BigDecimal(distance);
            float temp_distance = c.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();

            //装载数据
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("points", Points);
            values.put("distance", String.valueOf(temp_distance));
            values.put("duration", duration);
            values.put("averagespeed", String.valueOf(temp_averagespeed));
            values.put("date", date);
            db.insert("record", null, values);
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();
            Log.d(TAG, "onDestroy");
            //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
            mMapView.onDestroy();
            //销毁定位客户端之后，若要重新开启定位请重新New一个AMapLocationClient对象。
            locationClient.onDestroy();//销毁定位客户端。

        }

        @Override
        protected void onResume() {
            super.onResume();
            Log.d(TAG, "onResume");
            //在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
            mMapView.onResume();
        }

        @Override
        protected void onPause() {
            super.onPause();
            Log.d(TAG, "onPause");
            //在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
            mMapView.onPause();
        }

        @Override
        protected void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
            mMapView.onSaveInstanceState(outState);
        }

        @Override
        public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//            Toast.makeText(this,"已经重写", Toast.LENGTH_LONG).show();
//            return true;
//        } else
            return super.onKeyDown(keyCode, event);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            switch (id) {
                case android.R.id.home:
                    finish();
                    return true;
            }

            return super.onOptionsItemSelected(item);
        }
    }

