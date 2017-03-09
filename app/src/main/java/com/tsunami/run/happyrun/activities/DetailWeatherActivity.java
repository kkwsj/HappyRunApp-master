package com.tsunami.run.happyrun.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.tsunami.run.happyrun.R;
import com.tsunami.run.happyrun.adapters.DetailWeatherAdapter;
import com.tsunami.run.happyrun.utils.weather.WeatherDataUtil;


/**
 * Created by 2010330579 on 2016/3/27.
 */
public class DetailWeatherActivity extends AppCompatActivity{

    private SwipeRefreshLayout mSwipeRefresh;
    private RecyclerView mRecyclerView;
    private DetailWeatherAdapter mDetailAdapter;
    private Toolbar mToolBar;

    private boolean isRefresh = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setEnterTransition(new Slide());
        setContentView(R.layout.activity_detail_weather);
        initView();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.weatherDetailToolbar);
        setSupportActionBar(toolbar);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            // 2 添加的代码
            getSupportActionBar().setTitle("天气详情");
            /////////////////////////////////
        }

        mSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swiprefresh);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        if (mSwipeRefresh != null) {
            mSwipeRefresh.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);
//            mSwipeRefresh.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.YELLOW, Color.RED);
            mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {

//                    try {
//                        Thread.sleep(1000);
                        loadApi();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                }
            });
        }
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mDetailAdapter = new DetailWeatherAdapter();
        mRecyclerView.setAdapter(mDetailAdapter);
    }

    /**
     * 连接API，刷新数据
     */
    private void loadApi() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                mSwipeRefresh.setRefreshing(true);

            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    WeatherDataUtil.requestWeather();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                mDetailAdapter.notifyDataSetChanged();
                Toast.makeText(DetailWeatherActivity.this, "刷新完成！", Toast.LENGTH_SHORT).show();
                mSwipeRefresh.setRefreshing(false);
                isRefresh = true;
            }
        }.execute();
    }

    /**
     * 返回
     */
    @Override
    public void onBackPressed() {
        if (isRefresh) {
            setResult(RESULT_OK);
        }
        super.onBackPressed();
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
