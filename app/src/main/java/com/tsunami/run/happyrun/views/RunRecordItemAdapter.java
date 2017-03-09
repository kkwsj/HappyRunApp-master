package com.tsunami.run.happyrun.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.tsunami.run.happyrun.R;

import java.util.List;

/**
 * Created by 2010330579 on 2016/3/27.
 */
public class RunRecordItemAdapter extends ArrayAdapter<RunRecordItem> {
    private int resourceId;

    public RunRecordItemAdapter(Context context, int resource, List<RunRecordItem> object) {
        super(context, resource, object);
        resourceId = resource;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        RunRecordItem recordItem= getItem(position);
        ViewHolder viewHolder ;
        View view; //= LayoutInflater.from(getContext()).inflate(resourceId, null);
        if(convertView==null){
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();

            viewHolder.date = (TextView) view.findViewById(R.id.date);
            viewHolder.distance = (TextView) view.findViewById(R.id.distance);
            viewHolder.duration = (TextView) view.findViewById(R.id.duration);
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.date.setText(recordItem.getDate());
        viewHolder.distance.setText(recordItem.getDistance());
        viewHolder.duration.setText(recordItem.getDuration());
        return view;
    }
    class ViewHolder{
        TextView date;
        TextView distance;
        TextView duration;
    }
}
