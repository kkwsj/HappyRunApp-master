package com.tsunami.run.happyrun.activities;

/**
 * Created by 2010330579 on 2016/8/21.
 */

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
//import com.imooc.baidumap.WheelView.R;

import com.tsunami.run.happyrun.R;
import com.tsunami.run.happyrun.views.WheelView;

import java.util.Arrays;

public class WheelViewActivity extends AppCompatActivity implements View.OnClickListener {
    //private static final String TAG = MainActivity.class.getSimpleName();
    private static final String[] YEARS = new String[]{"1950", "1951", "1952", "1953", "1954", "1955", "1956", "1957","1958", "1959",
            "1960", "1961", "1962", "1963", "1964", "1965", "1966", "1967","1968", "1969",
            "1970", "1971", "1972", "1973", "1974", "1975", "1976", "1977","1978", "1979",
            "1980", "1981", "1982", "1983", "1984", "1985", "1986", "1987","1988", "1989",
            "1990", "1991", "1992", "1993", "1994", "1995", "1996", "1997","1998", "1999",
            "2000", "2001", "2002", "2003", "2004", "2005", "2006", "2007","2008", "2009",
            "2010", "2011", "2012", "2013", "2014", "2015", "2016"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //findViewById(R.id.btn_me_wheel).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.btn_me_wheel:
//                View outerView = LayoutInflater.from(this).inflate(R.layout.wheel_view, null);
//                WheelView wv = (WheelView) outerView.findViewById(R.id.wheel_view_wv);
//                wv.setOffset(2);
//                wv.setItems(Arrays.asList(YEARS));
//                wv.setSeletion(3);
//                wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
//                    @Override
//                    public void onSelected(int selectedIndex, String item) {
//                        Log.d(" ", "[Dialog]selectedIndex: " + selectedIndex + ", item: " + item);
//                    }
//                });
//
//                new AlertDialog.Builder(this)
//                        .setTitle("WheelView in Dialog")
//                        .setView(outerView)
//                        .setPositiveButton("OK", null)
//                        .show();
//
//                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
}

