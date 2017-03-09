package com.tsunami.run.happyrun.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;

import com.tsunami.run.happyrun.R;
import com.tsunami.run.happyrun.fragments.weather_fragment;
import com.tsunami.run.happyrun.fragments.me_fragment;
import com.tsunami.run.happyrun.fragments.music_fragment;
import com.tsunami.run.happyrun.fragments.run_fragment;
import com.tsunami.run.happyrun.utils.ChangeColorWithTest;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 2010330579 on 2016/3/24.
 */
public class MainActivity extends FragmentActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 4;

    private ViewPager mViewPager;

    ScreenSlidePagerAdapter mPagerAdapter;


    private List<ChangeColorWithTest> mTabIndicators =
            new ArrayList<ChangeColorWithTest>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        mViewPager.setAdapter(mPagerAdapter);

        initEvent();


    }


    @SuppressWarnings("deprecated")
    private void initEvent() {
        mViewPager.setOnPageChangeListener(this);
    }

    /**
     * 初始化主界面
     */
    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);

        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());

        ChangeColorWithTest one = (ChangeColorWithTest) findViewById(R.id.id_indicator_one);
        mTabIndicators.add(one);
        ChangeColorWithTest two = (ChangeColorWithTest) findViewById(R.id.id_indicator_two);
        mTabIndicators.add(two);
        ChangeColorWithTest three = (ChangeColorWithTest) findViewById(R.id.id_indicator_three);
        mTabIndicators.add(three);
        ChangeColorWithTest four = (ChangeColorWithTest) findViewById(R.id.id_indicator_four);
        mTabIndicators.add(four);


        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);

        one.setIconAlpha(1.0f);

    }


    /**
     * 点击跑步历史按钮
     *
     * @param view
     */
    public void runHistoryClick(View view) {
        Intent intent = new Intent(MainActivity.this, RunHistoryActivity.class);
        startActivity(intent);
    }

    /**
     * 点击分享按钮
     *
     * @param view
     */
    public void shareButtonClick(View view) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Share");
        intent.putExtra(Intent.EXTRA_TEXT, "你的想法....");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, getTitle()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;

    }


    /**
     * 设置menu,显示icon
     *
     * @param featureId
     * @param menu
     * @return
     */
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }


    @Override
    public void onClick(View v) {
        resetOtherTabs();

        switch (v.getId()) {
            case R.id.id_indicator_one:
                mTabIndicators.get(0).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(0, false);
                break;
            case R.id.id_indicator_two:
                mTabIndicators.get(1).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(1, false);
                break;
            case R.id.id_indicator_three:
                mTabIndicators.get(2).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(2, false);
                break;
            case R.id.id_indicator_four:
                mTabIndicators.get(3).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(3, false);
                break;

        }
    }


    /**
     * 重置其他的TabIndicator的颜色
     */
    private void resetOtherTabs() {
        for (int i = 0; i < mTabIndicators.size(); i++) {
            mTabIndicators.get(i).setIconAlpha(0);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (positionOffset > 0) {
            ChangeColorWithTest left = mTabIndicators.get(position);
            ChangeColorWithTest right = mTabIndicators.get(position + 1);
            left.setIconAlpha(1 - positionOffset);
            right.setIconAlpha(positionOffset);
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new run_fragment();

                case 1:
                    return new music_fragment();

                case 2:
                    return new weather_fragment();

                case 3:
                    return new me_fragment();

                default:
                    return new run_fragment();

            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

}
