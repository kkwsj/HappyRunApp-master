package com.tsunami.run.happyrun.dbs;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 2010330579 on 2016/3/27.
 */
public class MyRunDatabaseHelper extends SQLiteOpenHelper {

    private static final String RECORD_CREATE =
            "create table if not exists record("
                    + "id integer primary key,"
                    + "points STRING,"
                    + "distance STRING,"
                    + "duration STRING,"
                    + "averagespeed STRING,"
                    + "date STRING"
                    + ");";
    private Context mContext;

    public MyRunDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(RECORD_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

}
