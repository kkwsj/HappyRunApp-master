package com.tsunami.run.happyrun.adapters.music;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;
import com.tsunami.run.happyrun.R;
import com.tsunami.run.happyrun.utils.music.Mp3Info;

import java.util.List;

public class MyListViewAdapter extends BaseAdapter
                                implements SectionIndexer {

    private Context context;        //上下文对象引用
    private List<Mp3Info> mp3Infos;   //存放Mp3Info引用的集合
    private Mp3Info mp3Info;        //Mp3Info对象引用
    private int pos = -1;           //列表位置
    private ViewContainer vc;


    public MyListViewAdapter(Context context, List<Mp3Info> mp3Infos) {
        this.context = context;
        this.mp3Infos = mp3Infos;
    }


    @Override
    public Object[] getSections() {
        return null;
    }
    @Override
    public int getPositionForSection(int sectionIndex) {
        if (sectionIndex == '#') {
            return 0;
        }
        for (int i = 0; i < mp3Infos.size(); i++) {
            String sortStr = mp3Infos.get(i).getSortLetters();
            if (TextUtils.isEmpty(sortStr)) {
                ;
            } else {
                char firstChar = sortStr.toUpperCase().charAt(0);
                if (firstChar == sectionIndex) {
                    return i;
                }
            }
        }
        return -1;
    }
    @Override
    public int getSectionForPosition(int position) {
        return mp3Infos.get(position).getSortLetters().charAt(0);
    }


    @Override
    public int getCount() {
        return mp3Infos.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        vc = null;
        if (convertView == null) {
            vc = new ViewContainer();
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.music_list_item_layout, null);
            vc.music_title = (TextView) convertView.findViewById(R.id.music_title);
            vc.music_artist = (TextView) convertView.findViewById(R.id.music_artist);
            vc.music_duration = (TextView) convertView.findViewById(R.id.music_duration);
            convertView.setTag(vc);
        } else {
            vc = (ViewContainer) convertView.getTag();
        }
        mp3Info = mp3Infos.get(position);

        vc.music_title.setText(mp3Info.getTitle());         //显示标题
        vc.music_artist.setText(mp3Info.getArtist());       //显示艺术家
        vc.music_duration.setText(String.valueOf(formatTime(mp3Info.getDuration()))); //显示长度
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return position;
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
}
