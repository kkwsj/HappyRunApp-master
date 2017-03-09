package com.tsunami.run.happyrun.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.PolylineOptions;
import com.tsunami.run.happyrun.R;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by 2010330579 on 2016/3/27.
 */
public class RecordShowActivity extends AppCompatActivity implements AMap.OnMapLoadedListener{
    //地图
    private MapView mMapView = null;
    private AMap aMap;
    private UiSettings uiSettings = null;
    //TAG
    private static final String TAG = "RecordShow";
    //数据集
    private String averagespeed;
    private String points;
    private String distance;
    private String duration;
    private String date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showrecord);

        Toolbar toolbar = (Toolbar) findViewById(R.id.showRecordToolbar);
        setSupportActionBar(toolbar);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            // 2 添加的代码
            getSupportActionBar().setTitle("跑步轨迹");
            /////////////////////////////////
        }
        //定位 初始化
        init();

        mMapView = (MapView) findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，实现地图生命周期管理
        mMapView.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mMapView.getMap();
            //不允许显示缩放按钮
            uiSettings = aMap.getUiSettings();
            uiSettings.setZoomControlsEnabled(false);
        }
        aMap.setOnMapLoadedListener(this);// 设置amap加载成功事件监听器
    }

    private void init(){
        //获得当前item的所有数据
          averagespeed = getIntent().getStringExtra("averagespeed");
          points = getIntent().getStringExtra("points");
          distance = getIntent().getStringExtra("distance");
          duration = getIntent().getStringExtra("duration");
          date = getIntent().getStringExtra("date");
        Log.d(TAG,date);
    }
    @Override
    public void onMapLoaded() {
        List<LatLng> latLngs = new ArrayList<LatLng>(); //保存坐标
        List<String> list= Arrays.asList(points.split(";")); //先拆分成一对经纬度值
        for(int i=0;i<list.size();i++){
            String[] data = list.get(i).split(",");
            Log.d(TAG,data[1]+"xx"+data[0]);
            latLngs.add(new LatLng(Float.parseFloat(data[0]), Float.parseFloat(data[1])));
        }


        /*latLngs.add(new LatLng(31.8391010537,118.9708283016));
        latLngs.add(new LatLng(31.8500410000,119.0043180000));
        latLngs.add(new LatLng(31.8331080000,119.0082700000));
        latLngs.add(new LatLng(31.8217570000,119.0001490000));
        latLngs.add(new LatLng(31.8128580000,118.9780870000));
        latLngs.add(new LatLng(31.8129810000,119.0198400000));
        latLngs.add(new LatLng(31.8042050000,119.0358660000));*/

        int[] num = new int[]{0,0,0,0}; //获得四个经纬度分别为最大最小的点
        for(int i = 0;i<latLngs.size();i++){
            if(latLngs.get(i).latitude<latLngs.get(num[0]).latitude)
                num[0] = i;
            if(latLngs.get(i).latitude>latLngs.get(num[1]).latitude)
                num[1] = i;
            if(latLngs.get(i).longitude<latLngs.get(num[2]).longitude)
                num[2] = i;
            if(latLngs.get(i).longitude>latLngs.get(num[3]).longitude)
                num[3] = i;
        }
        // 设置所有maker显示在当前可视区域地图中
        LatLngBounds bounds = new LatLngBounds.Builder().
                include(latLngs.get(num[0])).
                include(latLngs.get(num[1])).
                include(latLngs.get(num[2])).
                include(latLngs.get(num[3])).
                build();
        aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));

        List<Integer> colorList = new ArrayList<Integer>();
        colorList.add(Color.argb(255, 255, 215, 0));
        colorList.add(Color.argb(255,255,69,0));
        colorList.add(Color.argb(255,60,179,113));
        colorList.add(Color.argb(255, 0, 206, 209));//如果第四个颜色不添加，那么最后一段将显示上一段的颜色

        aMap.addPolyline(new PolylineOptions().
                useGradient(true). //使用渐变色
                addAll(latLngs).
                colorValues(colorList).
                //  color(Color.argb(255,255,215,0)).
                        width(30));
        Log.d(TAG, "onMapLoad");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();

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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
