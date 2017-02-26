package com.tsunami.run.happyrun.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by 2010330579 on 2016/3/24.
 */
public class TabFragment extends Fragment {
    private String mTitle = "Default";
    public static final String TITLE = "title";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(getArguments() != null) {
            mTitle = getArguments().getString(TITLE);
        }

        TextView tv = new TextView(getActivity());
        tv.setTextSize(20);
        tv.setBackgroundColor(Color.parseColor("#87CEFA"));
        tv.setText(mTitle);
        tv.setGravity(Gravity.CENTER);
        return  tv;
    }
}
