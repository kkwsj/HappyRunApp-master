package com.tsunami.run.happyrun.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tsunami.run.happyrun.R;
import com.tsunami.run.happyrun.dbs.MyRunDatabaseHelper;
import com.tsunami.run.happyrun.views.RunRecordItem;
import com.tsunami.run.happyrun.views.RunRecordItemAdapter;

import java.util.ArrayList;
import java.util.List;

public class RunHistoryActivity extends AppCompatActivity {
    //数据库
    private MyRunDatabaseHelper dbHelper;
    private List<RunRecordItem> recordItemList = new ArrayList<RunRecordItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);

        Toolbar toolbar = (Toolbar) findViewById(R.id.runHistorytToolbar);
        setSupportActionBar(toolbar);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            // 2 添加的代码
            getSupportActionBar().setTitle("跑步记录");
        }

        //初始化record资源
        init();
        RunRecordItemAdapter adapter = new RunRecordItemAdapter(RunHistoryActivity.this.getApplicationContext(), R.layout.map_list_item, recordItemList);
        ListView listView = (ListView) findViewById(R.id.record_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                RunRecordItem recordItem = recordItemList.get(i);
                Intent intent = new Intent(RunHistoryActivity.this, RecordShowActivity.class);
                intent.putExtra("date", recordItem.getDate());
                intent.putExtra("duration", recordItem.getDuration());
                intent.putExtra("distance", recordItem.getDistance());
                intent.putExtra("averagespeed", recordItem.getAveragespeed());
                intent.putExtra("points", recordItem.getPoints());
                startActivity(intent);
            }
        });
    }

    private void init() {
        dbHelper = new MyRunDatabaseHelper(this, "record.db", null, 1);//创建数据库
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("record", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                //遍历cursor对象
                String date = cursor.getString(cursor.getColumnIndex("date"));
                String duration = cursor.getString(cursor.getColumnIndex("duration"));
                String distance = cursor.getString(cursor.getColumnIndex("distance")) + "里";
                //装载到List中
                recordItemList.add(new RunRecordItem(date,
                        duration,
                        distance,
                        cursor.getString(cursor.getColumnIndex("averagespeed")),
                        cursor.getString(cursor.getColumnIndex("points"))));
                Log.d("Map", date);
            } while (cursor.moveToNext());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
