package com.tsunami.run.happyrun.fragments;

import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tsunami.run.happyrun.R;
import com.tsunami.run.happyrun.activities.GaoDeMapActivity;
import com.tsunami.run.happyrun.views.RoundImageView;
import com.tsunami.run.happyrun.views.WhewView;

/**
 * Created by 2010330579 on 2016/3/26.
 */
public class run_fragment extends Fragment {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.page_run, container, false);

        wv = (WhewView) rootView.findViewById(R.id.wv);
        my_photo = (RoundImageView) rootView.findViewById(R.id.my_photo);
        wv.start();
        handler.sendEmptyMessage(Nou);
        my_photo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (wv.isStarting()) {

                } else {
                    // 执行动画
                    wv.start();
                    handler.sendEmptyMessage(Nou);
                }

                Intent intent = new Intent(getActivity(), GaoDeMapActivity.class);
                startActivity(intent);
            }
        });
        return rootView;
    }


}
