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

    // 声明一个SoundPool
    private SoundPool sp;
    // 定义一个整型用load（）；来设置suondIDf
    private int music;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == Nou) {
                // 每隔10s响一次
                handler.sendEmptyMessageDelayed(Nou, 5000);
                //sp.play(music, 1, 1, 0, 0, 1);
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.page_run, container, false);

        // 第一个参数为同时播放数据流的最大个数，第二数据流类型，第三为声音质量
        sp = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
        // 把你的声音素材放到res/raw里，第2个参数即为资源文件，第3个为音乐的优先级
        //music = sp.load(this, R.raw.hongbao_gq, 1);

        wv = (WhewView) rootView.findViewById(R.id.wv);
        my_photo = (RoundImageView) rootView.findViewById(R.id.my_photo);
        wv.start();
        handler.sendEmptyMessage(Nou);
        my_photo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(wv.isStarting()){
//                    //如果动画正在运行就停止，否则就继续执行
//                    wv.stop();
//                    //结束进程
//                    handler.removeMessages(Nou);
                }else{
                    // 执行动画
                    wv.start();
                    handler.sendEmptyMessage(Nou);
                }

                Intent intent = new Intent(getActivity(),GaoDeMapActivity.class);
                startActivity(intent);
            }
        });
        return rootView;
    }


}
