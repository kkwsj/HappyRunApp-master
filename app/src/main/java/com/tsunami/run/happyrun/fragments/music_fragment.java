package com.tsunami.run.happyrun.fragments;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.tsunami.run.happyrun.R;
import com.tsunami.run.happyrun.activities.FullSreenMusicPlayerActivity;
import com.tsunami.run.happyrun.adapters.music.MyListViewAdapter;
import com.tsunami.run.happyrun.utils.music.CharacterParser;
import com.tsunami.run.happyrun.utils.music.FindSongs;
import com.tsunami.run.happyrun.utils.music.Mp3Info;
import com.tsunami.run.happyrun.utils.music.SideBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by 2010330579 on 2016/3/26.
 */
public class music_fragment extends Fragment {
    private ListView mListView;
    private FindSongs finder;
    private List<Mp3Info> mp3Infos;
    private MyListViewAdapter adapter;
    private CharacterParser characterParser;
    private SideBar sideBar;
    private TextView dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.activity_music, container, false);

        mListView = (ListView) rootView.findViewById(R.id.listview);
        finder = new FindSongs();
        mp3Infos = finder.getMp3Infos(getActivity().getContentResolver());
        finder.setListAdpter(getContext(), mp3Infos, mListView);

        // 点击歌曲列表响应
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int musicPosition = position;
                Intent intent = new Intent(getActivity(), FullSreenMusicPlayerActivity.class);
                intent.putExtra("musicPosition", musicPosition);
                startActivity(intent);

            }
        });

        characterParser = new CharacterParser();
        String[] songArray = new String[mp3Infos.size()];
        for (int i = 0; i < mp3Infos.size(); ++i) {
            songArray[i] = mp3Infos.get(i).getTitle();
        }
        adapter = new MyListViewAdapter(getContext(), filledData(songArray));

        sideBar = (SideBar) rootView.findViewById(R.id.sidebar);
        dialog = (TextView) rootView.findViewById(R.id.dialog);
        // 右侧的sideBar       1
        sideBar.setTextView(dialog);
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    mListView.smoothScrollToPosition(position + 1);
                }
            }
        });

        return rootView;
    }

    /**
     * 从文字中取出letter比较   1
     *
     * @param date
     * @return
     */
    private List<Mp3Info> filledData(String[] date) {
        List<Mp3Info> mSortList = new ArrayList<>();
        ArrayList<String> indexString = new ArrayList<>();

        for (int i = 0; i < date.length; i++) {
            Mp3Info sortModel = new Mp3Info();
            sortModel.setTitle(date[i]);
            String pinyin = characterParser.getSelling(date[i]);
            String sortString = pinyin.substring(0, 1).toUpperCase();
            if (sortString.matches("[A-Z]")) {

                //对重庆多音字做特殊处理
                if (pinyin.startsWith("zhongqing")) {
                    sortString = "C";
                    sortModel.setSortLetters("C");
                } else {
                    sortModel.setSortLetters(sortString.toUpperCase());
                }

                if (!indexString.contains(sortString)) {
                    indexString.add(sortString);
                }
            } else {
                sortModel.setSortLetters("#");
            }

            mSortList.add(sortModel);
        }
        Collections.sort(indexString);

        return mSortList;
    }
}
