package com.tsunami.run.happyrun.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by bee on 2016/9/16.
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION=1;
    private static final String RECORD_TABLE="record";
    private static final String RECORD_CREATE=
            "create table if not exists record("
                    +"id integer primary key,"
                    +"points STRING,"
                    +"distance STRING,"
                    +"duration STRING,"
                    +"averagespeed STRING,"
                    +"date STRING"
                    +");";
    private Context mContext;

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(RECORD_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
   /* public static final String KEY_ROWID="id";
    public static final String KEY_DISTANCE="distance";
    public static final String KEY_DURATION="duration";
    public static final String KEY_SPEED="averagespeed";
    public static final String KEY_LINE="pathline";
    public static final String KEY_STRAT="stratpoint";
    public static final String KEY_END="endpoint";
    public static final String KEY_DATE="date";

    public long createrecord(String distance,String duration, String averagespeed,String latLngs, String date)
    {
        ContentValues args = new ContentValues();
        args.put("distance", distance);
        args.put("duration", duration);
        args.put("averagespeed", averagespeed);
        args.put("latLngs", latLngs);
        args.put("date", date);
        return db.insert(RECORD_TABLE, null, args);
    }

   *//* public Cursor getallrecord()
    {
       // return db.query(RECORD_TABLE, new String []{KEY_DISTANCE,KEY_DURATION,KEY_SPEED,KEY_LINE,KEY_STRAT,KEY_END,KEY_DATE}, null, null, null, null, null);
    }*/
}
