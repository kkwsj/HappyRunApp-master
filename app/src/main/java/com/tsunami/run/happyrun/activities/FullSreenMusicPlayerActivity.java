package com.tsunami.run.happyrun.activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.tsunami.run.happyrun.R;
import com.tsunami.run.happyrun.utils.music.FindSongs;
import com.tsunami.run.happyrun.utils.music.Mp3Info;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by 2010330579 on 2016/3/27.
 */
public class FullSreenMusicPlayerActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mEnd;
    private TextView mStart;
    private SeekBar mseekBar1;
    private ImageView mPlay_pause;

    private FindSongs finder;
    private List<Mp3Info> mp3Infos;

    private MediaPlayer mediaPlayer = new MediaPlayer();

    private boolean musicIsPlaying = false;
    private boolean isFirstPlay = true;


    //计时器
    private int second = 0;  //总秒数
    private final Timer timer = new Timer();
    private String duration; //格式化总秒数

    private int musicPosition;

    //异步线程
    private final Handler myHandler = new Handler() {
        @Override
        //重写handleMessage方法,根据msg中what的值判断是否执行后续操作
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                if (FullSreenMusicPlayerActivity.this.second >= ((int) mp3Infos.get(musicPosition).getDuration() / 1000)) {
                    FullSreenMusicPlayerActivity.this.second = ((int) mp3Infos.get(musicPosition).getDuration() / 1000);
                }

                int hour = FullSreenMusicPlayerActivity.this.second / 60 / 60;
                int minute = (FullSreenMusicPlayerActivity.this.second - hour * 60 * 60) / 60;
                int second = FullSreenMusicPlayerActivity.this.second - hour * 60 * 60 - minute * 60;


                duration = (minute > 9 ? minute : "0" + minute) + ":" +
                        (second > 9 ? second : "0" + second);
                mStart.setText(duration);
                mseekBar1.setProgress(FullSreenMusicPlayerActivity.this.second * 100 * 1000 / (int) mp3Infos.get(musicPosition).getDuration());

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_player);

        Toolbar toolbar = (Toolbar) findViewById(R.id.FullSreenMusicPlayerToolbar);
        setSupportActionBar(toolbar);

        finder = new FindSongs();
        mp3Infos = finder.getMp3Infos(FullSreenMusicPlayerActivity.this.getContentResolver());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            // 2 添加的代码

            Intent intent = getIntent();

            musicPosition = intent.getIntExtra("musicPosition", 0);
            getSupportActionBar().setTitle(mp3Infos.get(musicPosition).getTitle());
            getSupportActionBar().setSubtitle(mp3Infos.get(musicPosition).getArtist());


        }

        mEnd = (TextView) findViewById(R.id.endText);
        mStart = (TextView) findViewById(R.id.startText);
        mseekBar1 = (SeekBar) findViewById(R.id.seekBar1);
        mPlay_pause = (ImageView) findViewById(R.id.play_pause);

        mEnd.setText(formatTime(mp3Infos.get(musicPosition).getDuration()));

        //设置定时器信息
        setCountTimer();


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.prev:

                break;
            case R.id.play_pause:

                break;
            case R.id.next:

                break;
        }
    }

    private void setCountTimer() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                myHandler.sendEmptyMessage(0);
                if (musicIsPlaying) {
                    second++;//秒数加一
                }

            }
        };
        timer.schedule(task, 0, 1000);// 1秒一次
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                mediaPlayer.reset();
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static String formatTime(Long time) {                     //将歌曲的时间转换为分秒的制度
        String min = time / (1000 * 60) + "";
        String sec = time % (1000 * 60) + "";

        if (min.length() < 2)
            min = "0" + min;
        switch (sec.length()) {
            case 4:
                sec = "0" + sec;
                break;
            case 3:
                sec = "00" + sec;
                break;
            case 2:
                sec = "000" + sec;
                break;
            case 1:
                sec = "0000" + sec;
                break;
        }
        return min + ":" + sec.trim().substring(0, 2);
    }

    public void play_pauseClick(View view) {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            musicIsPlaying = false;
            mPlay_pause.setImageResource(R.drawable.uamp_ic_play_arrow_white_48dp);
            return;
        }
        if (!musicIsPlaying && isFirstPlay == false) {
            mediaPlayer.start();
            musicIsPlaying = true;
            mPlay_pause.setImageResource(R.drawable.uamp_ic_pause_white_48dp);
            return;
        }

        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(mp3Infos.get(musicPosition).getUrl());
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
        musicIsPlaying = true;
        isFirstPlay = false;
        mPlay_pause.setImageResource(R.drawable.uamp_ic_pause_white_48dp);


    }

    public void prevClick(View view) {
        if (musicPosition >= 1) {
            musicPosition--;
        }
        musicIsPlaying = false;
        second = 0;
        mPlay_pause.setImageResource(R.drawable.uamp_ic_play_arrow_white_48dp);

        mediaPlayer.reset();
        isFirstPlay = true;
        getSupportActionBar().setTitle(mp3Infos.get(musicPosition).getTitle());
        getSupportActionBar().setSubtitle(mp3Infos.get(musicPosition).getArtist());
        mStart.setText("00:00");
        mEnd.setText(formatTime(mp3Infos.get(musicPosition).getDuration()));

    }

    public void nextClick(View view) {
        if (musicPosition >= 1) {
            musicPosition++;
        }
        musicIsPlaying = false;
        second = 0;
        mPlay_pause.setImageResource(R.drawable.uamp_ic_play_arrow_white_48dp);

        mediaPlayer.reset();
        isFirstPlay = true;
        getSupportActionBar().setTitle(mp3Infos.get(musicPosition).getTitle());
        getSupportActionBar().setSubtitle(mp3Infos.get(musicPosition).getArtist());
        mStart.setText("00:00");
        mEnd.setText(formatTime(mp3Infos.get(musicPosition).getDuration()));
    }
}
