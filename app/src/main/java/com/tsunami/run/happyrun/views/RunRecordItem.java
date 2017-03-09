package com.tsunami.run.happyrun.views;

/**
 * Created by 2010330579 on 2016/3/27.
 */
public class RunRecordItem {
    private String date;
    private String duration;
    private String distance;
    private String averagespeed;
    private String points;

    public RunRecordItem(String a, String b, String c, String d, String f){
        this.date=a;
        this.duration=b;
        this.distance=c;
        this.averagespeed = d;
        this.points = f;
    }
    public String getDate(){
        return this.date;
    }
    public String getDuration(){
        return this.duration;
    }
    public String getDistance(){return this.distance;}
    public String getAveragespeed(){return this.averagespeed;}
    public String getPoints(){return this.points;}
}
