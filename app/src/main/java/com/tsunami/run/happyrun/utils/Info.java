package com.tsunami.run.happyrun.utils;

import com.tsunami.run.happyrun.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 2010330579 on 2016/1/8.
 */
public class Info implements Serializable {

    private static final long serialVersionUID = -1010711775;
    private double latitude;
    private double longitude;
    private int imageId;
    private String name;
    private String distance;
    private int zan;

    public static List<Info> infos = new ArrayList<Info>();

    static {
        infos.add(new Info(34.242652,108.971171, R.drawable.a01,"英伦贵族小旅馆",
        "距离200米",1456));
    }
    public Info(double latitude, double longitude,
                int imageId, String name, String diatance, int zan) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.imageId = imageId;
        this.name = name;
        this.distance = distance;
        this.zan =zan;
    }
    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getImageId() {
        return imageId;
    }

    public int getZan() {
        return zan;
    }

    public String getName() {
        return name;
    }

    public String getDistance() {
        return distance;
    }
}
