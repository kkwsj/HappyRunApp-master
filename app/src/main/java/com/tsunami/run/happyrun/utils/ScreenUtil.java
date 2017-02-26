package com.tsunami.run.happyrun.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by SHEN XIAOMING on 2016/8/16.
 */
public class ScreenUtil {

    /**
     * 获取屏幕密度
     */
    public static float getDeviceDensity(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(
                Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        return metrics.density;
    }
}
