package com.tsunami.run.happyrun.app;

import android.app.Application;
import android.content.Context;

/**
 * Created by 2010330579 on 2016/3/27.
 */
public class App extends Application {

    public static Context mAppContext = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext = getApplicationContext();
    }

    public static Context getmAppContext() {
        return mAppContext;
    }
}
