package com.tsunami.run.happyrun.app;

import android.app.Application;
import android.content.Context;

import com.baidu.apistore.sdk.ApiStoreSDK;

/**
 * Created by SHEN XIAOMING on 2016/9/25.
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
