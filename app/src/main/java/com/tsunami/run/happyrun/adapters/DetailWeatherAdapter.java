package com.tsunami.run.happyrun.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.tsunami.run.happyrun.R;
import com.tsunami.run.happyrun.utils.ScreenUtil;
import com.tsunami.run.happyrun.utils.weather.WeatherDataUtil;
import com.tsunami.run.happyrun.utils.weather.WeatherIconUtil;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by SHEN XIAOMING on 2016/9/26.
 */
public class DetailWeatherAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static String TAG = DetailWeatherAdapter.class.getSimpleName();

    private static final int TYPE_TODAY = 0;
    private static final int TYPE_WEEK = 1;
    private static final int TYPE_LIFE = 2;

    private Context mContext;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        switch (viewType) {
            case TYPE_TODAY:
                return new TodayViewHolder(
                        LayoutInflater.from(mContext).inflate(R.layout.detail_today, parent, false));
            case TYPE_WEEK:
                return new WeekViewHolder(
                        LayoutInflater.from(mContext).inflate(R.layout.detail_week, parent, false));
            case TYPE_LIFE:
                return new LifeViewHolder(
                        LayoutInflater.from(mContext).inflate(R.layout.detail_life, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemType = getItemViewType(position);
        switch (itemType) {
            case TYPE_TODAY:
                ((TodayViewHolder) holder).loadData();
                break;
            case TYPE_WEEK:
                ((WeekViewHolder) holder).loadData();
                break;
            case TYPE_LIFE:
                ((LifeViewHolder) holder).loadData();
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case TYPE_TODAY:
                return DetailWeatherAdapter.TYPE_TODAY;
            case TYPE_WEEK:
                return DetailWeatherAdapter.TYPE_WEEK;
            case TYPE_LIFE:
                return DetailWeatherAdapter.TYPE_LIFE;
        }
        return super.getItemViewType(position);
    }


    /**
     * 今日详情
     */
    class TodayViewHolder extends RecyclerView.ViewHolder {
        
        private final int TODAY_SIZE = 4;
        
        private LinearLayout today_ll;
        private TextView sunrise_tv;
        private TextView sunset_tv;
        private ImageView[] icon_iv = new ImageView[TODAY_SIZE];
        private TextView[] item_tv = new TextView[TODAY_SIZE];
        private TextView[] value_tv = new TextView[TODAY_SIZE];

        public TodayViewHolder(View view) {
            super(view);
            today_ll = (LinearLayout) view.findViewById(R.id.today_ll);
            sunrise_tv = (TextView) view.findViewById(R.id.today_sunrise);
            sunset_tv = (TextView) view.findViewById(R.id.today_sunset);
            for (int i = 0; i < TODAY_SIZE; i++) {
                View itemView = View.inflate(mContext, R.layout.today_ll, null);
                icon_iv[i] = (ImageView) itemView.findViewById(R.id.today_icon);
                item_tv[i] = (TextView) itemView.findViewById(R.id.today_item);
                value_tv[i] = (TextView) itemView.findViewById(R.id.today_value);
                today_ll.addView(itemView);
            }
            // 加载数据
            loadData();
        }

        public void loadData() {
            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.today_fl);
            icon_iv[0].setImageBitmap(bitmap);
            item_tv[0].setText("体感");
            bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.today_hum);
            icon_iv[1].setImageBitmap(bitmap);
            item_tv[1].setText("湿度");
            bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.today_pres);
            icon_iv[2].setImageBitmap(bitmap);
            item_tv[2].setText("气压");
            bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.today_vis);
            icon_iv[3].setImageBitmap(bitmap);
            item_tv[3].setText("能见度");
            try {
                String now_str = WeatherDataUtil.getNow();
                JSONObject now = new JSONObject(now_str);
                value_tv[0].setText(now.getString("fl") + "℃");
                value_tv[1].setText(now.getString("hum") + "%");
                value_tv[2].setText(now.getString("pres") + "hpa");
                value_tv[3].setText(now.getString("vis") + "km");

                String daily_forecast_str = WeatherDataUtil.getDailyForecast();
                JSONArray daily_forecast = new JSONArray(daily_forecast_str);
                JSONObject today= daily_forecast.getJSONObject(0);
                String sun_str = today.getString("astro");
                JSONObject sun = new JSONObject(sun_str);
                sunrise_tv.setText(sun.getString("sr"));
                sunset_tv.setText(sun.getString("ss"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 未来一周天气
     */
    class WeekViewHolder extends RecyclerView.ViewHolder {

        private final int WEEK_SIZE = 7;
        private LinearLayout week_ll;
        private TextView[] day_tv = new TextView[WEEK_SIZE];
        private ImageView[] icon_iv = new ImageView[WEEK_SIZE];
        private TextView[] cond_tv = new TextView[WEEK_SIZE];
        private TextView[] temp_tv = new TextView[WEEK_SIZE];

        public WeekViewHolder(View view) {
            super(view);
            week_ll = (LinearLayout) view.findViewById(R.id.week_ll);
            for (int i = 0; i < WEEK_SIZE; i++) {
                View itemView = View.inflate(mContext, R.layout.week_ll, null);
                day_tv[i] = (TextView) itemView.findViewById(R.id.week_day);
                icon_iv[i] = (ImageView) itemView.findViewById(R.id.week_icon);
                cond_tv[i] = (TextView) itemView.findViewById(R.id.week_cond);
                temp_tv[i] = (TextView) itemView.findViewById(R.id.week_temp);
                week_ll.addView(itemView);
            }
            // 加载数据
            loadData();
        }

        public void loadData() {
            try {
                for (int i = 0 ; i < WEEK_SIZE; i++) {
                    JSONArray daily_forecast = new JSONArray(WeatherDataUtil.getDailyForecast());
                    JSONObject days = daily_forecast.getJSONObject(i);
                    // 日期
                    String date = days.getString("date");
                    date = date.substring(5, 10);
                    day_tv[i].setText(date);
                    // 天气情况和图标
                    String cond_str = days.getString("cond");
                    JSONObject cond = new JSONObject(cond_str);
                    String txt_d = cond.getString("txt_d");
                    String txt_n = cond.getString("txt_n");
                    if (txt_d.equals(txt_n)) {
                        cond_tv[i].setText(txt_d);
                    } else {
                        cond_tv[i].setText(txt_d + "转" + txt_n);
                    }
                    String code = cond.getString("code_d");
                    Bitmap icon = BitmapFactory.decodeResource(mContext.getResources(),
                            WeatherIconUtil.getWeatherIcon(code));
                    icon_iv[i].setImageBitmap(icon);
                    // 温度
                    String tmp_str = days.getString("tmp");
                    JSONObject tmp = new JSONObject(tmp_str);
                    String min = tmp.getString("min");
                    String max = tmp.getString("max");
                    String temp = min + "~" + max + "℃";
                    temp_tv[i].setText(temp);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 生活指数
     */
    class LifeViewHolder extends RecyclerView.ViewHolder {

        private final int LIFE_SIZE = 7;
        private ImageView[] icon_iv = new ImageView[LIFE_SIZE];
        private TextView[] item_tv = new TextView[LIFE_SIZE];
        private TextView[] cond_tv = new TextView[LIFE_SIZE];
        private TextView[] more_tv = new TextView[LIFE_SIZE];
        private ImageView[] up_down_iv = new ImageView[LIFE_SIZE];

        private LinearLayout[] detail_item_cond_ll = new LinearLayout[LIFE_SIZE];

        private boolean flag_down = false;

        private LinearLayout life_ll;

        public LifeViewHolder(View view) {
            super(view);
            life_ll = (LinearLayout) view.findViewById(R.id.life_ll);
            for (int i = 0; i < LIFE_SIZE; i++) {
                View itemView = View.inflate(mContext, R.layout.life_ll, null);
                icon_iv[i] = (ImageView) itemView.findViewById(R.id.life_icon);
                item_tv[i] = (TextView) itemView.findViewById(R.id.life_item);
                cond_tv[i] = (TextView) itemView.findViewById(R.id.life_condition);
                more_tv[i] = (TextView) itemView.findViewById(R.id.life_more);
                up_down_iv[i] = (ImageView) itemView.findViewById(R.id.life_up_down);
                detail_item_cond_ll[i] = (LinearLayout) itemView.findViewById(R.id.detail_item_cond);
                life_ll.addView(itemView);
                final int pos = i;
                up_down_iv[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setMoreTvVisibility(pos);
                    }
                });
            }
            // 加载数据
            loadData();
        }

        private void setMoreTvVisibility(int k){
            flag_down = !flag_down;
            if (flag_down) {
                Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.life_item_up);
                up_down_iv[k].setImageBitmap(bmp);
                more_tv[k].setVisibility(View.VISIBLE);
                detail_item_cond_ll[k].setPadding(0, 10 * (int) ScreenUtil.getDeviceDensity(mContext), 0, 0);
            } else {
                Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.life_item_down);
                up_down_iv[k].setImageBitmap(bmp);
                more_tv[k].setVisibility(View.GONE);
                detail_item_cond_ll[k].setPadding(0, 18 * (int)ScreenUtil.getDeviceDensity(mContext), 0, 0);
            }
        }

        public void loadData() {
            Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.life_cmf);
            icon_iv[0].setImageBitmap(bmp);
            bmp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.life_drsg);
            icon_iv[1].setImageBitmap(bmp);
            bmp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.life_flu);
            icon_iv[2].setImageBitmap(bmp);
            bmp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.life_sport);
            icon_iv[3].setImageBitmap(bmp);
            bmp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.life_trav);
            icon_iv[4].setImageBitmap(bmp);
            bmp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.life_uv);
            icon_iv[5].setImageBitmap(bmp);
            bmp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.life_cw);
            icon_iv[6].setImageBitmap(bmp);
            bmp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.life_item_down);
            for (int i = 0; i < LIFE_SIZE;i++) {
                up_down_iv[i].setImageBitmap(bmp);
            }
            item_tv[0].setText(mContext.getString(R.string.life_cmf));
            item_tv[1].setText(mContext.getString(R.string.life_drsg));
            item_tv[2].setText(mContext.getString(R.string.life_flu));
            item_tv[3].setText(mContext.getString(R.string.life_sport));
            item_tv[4].setText(mContext.getString(R.string.life_trav));
            item_tv[5].setText(mContext.getString(R.string.life_uv));
            item_tv[6].setText(mContext.getString(R.string.life_cw));
            String suggestion_str = WeatherDataUtil.getSuggestion();
            try {
                JSONObject suggestion = new JSONObject(suggestion_str);
                String comf_str = suggestion.getString("comf");
                cond_tv[0].setText(new JSONObject(comf_str).getString("brf"));
                more_tv[0].setText(new JSONObject(comf_str).getString("txt"));
                String drsg_str = suggestion.getString("drsg");
                cond_tv[1].setText(new JSONObject(drsg_str).getString("brf"));
                more_tv[1].setText(new JSONObject(drsg_str).getString("txt"));
                String flu_str = suggestion.getString("flu");
                cond_tv[2].setText(new JSONObject(flu_str).getString("brf"));
                more_tv[2].setText(new JSONObject(flu_str).getString("txt"));
                String sport_str = suggestion.getString("sport");
                cond_tv[3].setText(new JSONObject(sport_str).getString("brf"));
                more_tv[3].setText(new JSONObject(sport_str).getString("txt"));
                String trav_str = suggestion.getString("trav");
                cond_tv[4].setText(new JSONObject(trav_str).getString("brf"));
                more_tv[4].setText(new JSONObject(trav_str).getString("txt"));
                String uv_str = suggestion.getString("uv");
                cond_tv[5].setText(new JSONObject(uv_str).getString("brf"));
                more_tv[5].setText(new JSONObject(uv_str).getString("txt"));
                String cw_str = suggestion.getString("cw");
                cond_tv[6].setText(new JSONObject(cw_str).getString("brf"));
                more_tv[6].setText(new JSONObject(cw_str).getString("txt"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
