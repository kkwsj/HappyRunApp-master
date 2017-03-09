package com.tsunami.run.happyrun.activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.tsunami.run.happyrun.R;
import com.tsunami.run.happyrun.views.RoundImageView;
import com.tsunami.run.happyrun.views.WhewView;

/**
 * Created by 2010330579 on 2016/3/30.
 */
public class WaterWaveCircle_Acticity extends Activity {
    private WhewView wv;
    private RoundImageView my_photo;
    private static final int Nou = 1;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == Nou) {
                // 每隔10s响一次
                handler.sendEmptyMessageDelayed(Nou, 5000);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {

        wv = (WhewView) findViewById(R.id.wv);
        my_photo = (RoundImageView) findViewById(R.id.my_photo);
        my_photo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (wv.isStarting()) {
                    //如果动画正在运行就停止，否则就继续执行
                    wv.stop();
                    //结束进程
                    handler.removeMessages(Nou);
                } else {
                    // 执行动画
                    wv.start();
                    handler.sendEmptyMessage(Nou);
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        handler.removeMessages(Nou);
    }
}
