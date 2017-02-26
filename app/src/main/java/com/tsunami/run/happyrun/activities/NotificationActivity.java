package com.tsunami.run.happyrun.activities;
/**
 * Created by 2010330579 on 2016/6/29.
 */
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Button;

import com.tsunami.run.happyrun.R;

import java.io.File;

public class NotificationActivity extends Activity implements View.OnClickListener {

    private Button sendNotice;

    long[] vibrates = {0,1000,1000,1000};

    Uri soundUri = Uri.fromFile(new File("/system/360/Rihanna-Diamonds.mp3"));
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_layout);
        sendNotice = (Button) findViewById(R.id.send_notice);
        sendNotice.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.send_notice:
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                Intent intent = new Intent(this,NotificationActivity.class);
                PendingIntent pi = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
                NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setVibrate(vibrates)
                        .setContentTitle("My notification")
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setContentText("Hello World!");

//                Intent intent = new Intent(this,NotificationActivity.class);
//                PendingIntent pi = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
                mBuilder.setContentIntent(pi);
                manager.notify(1, mBuilder.build());
                break;
            default:
                break;
        }
    }


}

