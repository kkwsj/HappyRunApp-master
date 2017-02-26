package com.tsunami.run.happyrun.activities;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.tsunami.run.happyrun.R;
import com.tsunami.run.happyrun.utils.PeriscopeLayout;

public class HeartDanceActivity extends Activity {
    private static final String TAG = "HeartDanceActivity";
    private Button btn_start;
    // 心型气泡
    private PeriscopeLayout periscopeLayout;

    private int heartDanceDepth = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.heart_dance);
        // 初始化
        periscopeLayout = (PeriscopeLayout) findViewById(R.id.periscope);

        btn_start = (Button) findViewById(R.id.btn_start);
//        btn_start.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                //调用添加泡泡的方法
//                periscopeLayout.addHeart();
//
//            }
//        });



        btn_start.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //调用添加泡泡的方法
                if(heartDanceDepth < 5) {
                    new Thread(new ThreadShow()).start();
                    heartDanceDepth++;
                    Log.d(TAG,"danceDepth++");
                }
            }
        });
    }

    //handler类接收数据
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                periscopeLayout.addHeart();
            }
        };
    };

    // 线程类
    class ThreadShow implements Runnable {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            while (true) {
                try {
                    Thread.sleep(200);
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
//                    System.out.println("send...");
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
//                    System.out.println("thread error...");
                }
            }
        }
    }
}
