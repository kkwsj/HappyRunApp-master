package com.tsunami.run.happyrun.activities;

/**
 * Created by 2010330579 on 2016/6/29.
 */
import android.app.Activity;
import android.app.NotificationManager;
import android.os.Bundle;

import com.tsunami.run.happyrun.R;

/**
 * Created by 2010330579 on 2016/3/27.
 */
public class NewNotificationActivity extends Activity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_layout);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(1);
    }
}

