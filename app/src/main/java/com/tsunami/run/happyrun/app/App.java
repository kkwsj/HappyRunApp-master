package com.tsunami.run.happyrun.app;

import android.app.Application;
import android.content.Context;

import com.baidu.apistore.sdk.ApiStoreSDK;

/**
 * Created by 2010330579 on 2016/3/27.
 */
public class App extends Application {

    public static Context mAppContext = null;

    @Override
    public void onCreate() {
       // ApiStoreSDK.init(this, "c9fede3ca8b6f60bf44c5d0c4f24ec2c");
        super.onCreate();
        mAppContext = getApplicationContext();
    }

    public static Context getmAppContext() {
        return mAppContext;
    }
}
