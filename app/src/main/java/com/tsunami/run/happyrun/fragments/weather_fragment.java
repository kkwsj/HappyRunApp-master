package com.tsunami.run.happyrun.fragments;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.tsunami.run.happyrun.R;
import com.tsunami.run.happyrun.activities.DetailWeatherActivity;
import com.tsunami.run.happyrun.utils.FileStreamUtil;
import com.tsunami.run.happyrun.utils.weather.WeatherDataUtil;
import com.tsunami.run.happyrun.utils.weather.WeatherIconUtil;

import java.io.IOException;
import java.util.List;

/**
 * Created by 2010330579 on 2016/3/26.
 */
public class weather_fragment extends Fragment {

    private static final int TO_DETAIL_WEATHER = 1;
    private static final String TAG = weather_fragment.class.getSimpleName();

    private LocationManager locationManager;
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onLocationChanged(Location location) {
            // 更新当前设备的位置信息
            connectCity(location);
        }
    };

    private RelativeLayout weatherRv;

    private TextView weatherCity;
    private TextView weatherAqi;
    private TextView aqiText;
    private TextView aqiColor;
    private TextView weatherCond;
    private TextView weatherTmpNow;
    private TextView weatherUpdate;
    private ImageView weatherIcon;

    private boolean currentNetwork = false;
    private boolean currentLocation = false;

    // 默认城市 北京
    private String mCity = "beijing";
    private NetworkChangeReceiver networkChangeReceiver;
    private LocationChangeReceiver locationChangeReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.page_find, container, false);

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        // 初始化view
        initViews(rootView);

        if (!TextUtils.isEmpty(FileStreamUtil.load(getActivity()))) {
            // 从历史数据中读取
            WeatherDataUtil.setDataAll(FileStreamUtil.load(getActivity()));
            // 显示数据
            updateWeatherUI();
        }

        // 网络变化广播监听
        //     进入网络将发生变化，进而获取位置，解析城市，获取天气
        IntentFilter networkFilter = new IntentFilter();
        networkFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        networkChangeReceiver = new NetworkChangeReceiver();
        getActivity().registerReceiver(networkChangeReceiver, networkFilter);
        // 位置服务变化广播监听
        IntentFilter locationFilter = new IntentFilter();
        locationFilter.addAction(LocationManager.PROVIDERS_CHANGED_ACTION);
        locationChangeReceiver = new LocationChangeReceiver();
        getActivity().registerReceiver(locationChangeReceiver, locationFilter);

        return rootView;
    }

    /**
     * 初始化view
     */
    private void initViews(ViewGroup rootView) {
        weatherCity = (TextView) rootView.findViewById(R.id.city_tv);
        weatherAqi = (TextView) rootView.findViewById(R.id.weather_aqi);
        aqiColor = (TextView) rootView.findViewById(R.id.aqi_color);
        aqiText = (TextView) rootView.findViewById(R.id.aqi_text);
        weatherCond = (TextView) rootView.findViewById(R.id.weather_cond);
        weatherTmpNow = (TextView) rootView.findViewById(R.id.weather_tmp_now);
        weatherUpdate = (TextView) rootView.findViewById(R.id.weather_update);
        weatherIcon = (ImageView) rootView.findViewById(R.id.weather_icon);

        weatherRv = (RelativeLayout) rootView.findViewById(R.id.weather_bg_rl);

        TextView weatherToDetail = (TextView) rootView.findViewById(R.id.weather_to_detail);
        weatherToDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toWeatherDetail();
            }
        });
    }

    /**
     * 进入获取更多的天气情况
     */
    public void toWeatherDetail() {

        // 判断是否已获取到天气数据
        if (WeatherDataUtil.getDataAll() != null) {
            Intent intent = new Intent(getActivity(), DetailWeatherActivity.class);
            startActivityForResult(intent, TO_DETAIL_WEATHER,
                    ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
        } else if (FileStreamUtil.load(getActivity()).isEmpty()) {
            if (!currentNetwork) {
                Toast.makeText(getActivity(), "请连接网络", Toast.LENGTH_SHORT).show();
            } else if (!currentLocation) {
                Toast.makeText(getActivity(), "请打开位置服务", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "请下拉刷新", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * UI中显示天气数据
     */
    private void updateWeatherUI() {
        weatherCity.setText(WeatherDataUtil.getCity());
        // 设置 空气质量
        weatherAqi.setText(WeatherDataUtil.getAqi());
        switch (WeatherDataUtil.getAqiRank()) {
            case 0:
                aqiColor.setTextColor(getResources().getColor(R.color.excellent));
                aqiColor.setBackgroundColor(getResources().getColor(R.color.excellent));
                aqiText.setText("优");   // aqi中对 优 的判断有误
                break;
            case 1:
                aqiColor.setTextColor(getResources().getColor(R.color.good));
                aqiColor.setBackgroundColor(getResources().getColor(R.color.good));
                aqiText.setText("良");
                break;
            case 2:
                aqiColor.setTextColor(getResources().getColor(R.color.slight));
                aqiColor.setBackgroundColor(getResources().getColor(R.color.slight));
                aqiText.setText("轻度污染");
                break;
            case 3:
                aqiColor.setTextColor(getResources().getColor(R.color.moderate));
                aqiColor.setBackgroundColor(getResources().getColor(R.color.moderate));
                aqiText.setText("中度污染");
                break;
            case 4:
                aqiColor.setTextColor(getResources().getColor(R.color.severe));
                aqiColor.setBackgroundColor(getResources().getColor(R.color.severe));
                aqiText.setText("重度污染");
                break;
            case 5:
                aqiColor.setTextColor(getResources().getColor(R.color.serious));
                aqiColor.setBackgroundColor(getResources().getColor(R.color.serious));
                aqiText.setText("严重污染");
                break;
        }
        // 当前天气情况
        weatherIcon.setImageResource(WeatherIconUtil.getWeatherIcon(
                WeatherDataUtil.getCondCode()));
        weatherCond.setText(WeatherDataUtil.getCond());
        weatherTmpNow.setText(WeatherDataUtil.getTmp());
        weatherUpdate.setText(WeatherDataUtil.getUpdateTime());
        weatherRv.invalidate();
    }

    /**
     * 获取位置信息
     */
    private void connectLocation() {
        // 获取network的位置提供器
        List<String> providerList = locationManager.getProviders(true);
        String provider;
        currentLocation = false;
        if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
            currentLocation = true;
            provider = LocationManager.NETWORK_PROVIDER;
        } else {
            // 位置提供器关闭时，弹出Toast提示用户
            Toast.makeText(getActivity(), "no location service", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            Location location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                // 位置信息中获取城市
                connectCity(location);
            } else {
                Log.e(TAG, "no location");
            }
            locationManager.requestLocationUpdates(provider, 1000 * 60, 200, locationListener);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取城市名
     *
     * @param location 位置信息
     */
    private void connectCity(Location location) {
        String city = "";
        try {
            Geocoder geocoder = new Geocoder(getActivity());
            List<Address> addressList = geocoder.getFromLocation(location.getLatitude(),
                    location.getLongitude(), 1);
            if (addressList != null) {
                // 获取所在市
                city += addressList.get(0).getLocality();
            }
            city = city.substring(0, city.indexOf("市"));
            if (!TextUtils.isEmpty(city)) {
                mCity = city;
            }
            WeatherDataUtil.setCity(mCity);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取城市天气
     */
    private void connectWeather() {
//        final ProgressDialog dialog = new ProgressDialog(getActivity());
//        dialog.setMessage("加载中...");
//        dialog.show();
//        new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected Void doInBackground(Void... params) {
//                try {
//              //      Thread.sleep(1000);
//                    // 获取天气
//                    WeatherDataUtil.requestWeather();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Void result) {
//                // 更新UI界面
//                updateWeatherUI();
//                dialog.dismiss();
//            }
//        }.execute();
    }

    // 监听网络变化广播
    class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // 获取当前网络状态
            ConnectivityManager connectivityManager = (ConnectivityManager)
                    getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo == null) {
                currentNetwork = false;
                // 提醒当前无网络
                Toast.makeText(getActivity(), "no network", Toast.LENGTH_LONG).show();
            } else {
                currentNetwork = true;
                connectLocation();
                if (currentLocation) {
                    // 获取天气
                    connectWeather();
                }
            }
        }
    }

    // 监听位置服务变化广播
    class LocationChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // 获取network的位置信息
            connectLocation();
            if (currentLocation) {
                if (currentNetwork) {
                    // 刷新

                    // 获取天气
                    connectWeather();
                }
            } else {
                // 位置提供器关闭时，弹出Toast提示用户
                Toast.makeText(getActivity(), "no location service", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            try {
                // 关闭程序时将监听器移除
                locationManager.removeUpdates(locationListener);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
        getActivity().unregisterReceiver(networkChangeReceiver);
        getActivity().unregisterReceiver(locationChangeReceiver);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TO_DETAIL_WEATHER:
                if (resultCode == getActivity().RESULT_OK) {
                    updateWeatherUI();
                }
                break;
            default:
                break;
        }
    }


}
