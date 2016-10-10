package com.ytj.project_login.entity;

import java.io.Serializable;

/**
 * 实体类（包含了经度，维度和提示）
 * Created by Administrator on 2016/9/29.
 */
public class LocationInfo implements Serializable {
    private double latitude;
    private double longtitude;
    private String tips;
    private int type=TEAMUSER;//默认是参与案件人员

    public static final int TEAMUSER = 0;//参与案件人员
    public static final int OBJECT = 1;//目标人员

    public LocationInfo() {
    }

    public LocationInfo(double latitude, double longtitude, String tips) {
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.tips = tips;
    }

    public LocationInfo(double latitude, double longtitude, String tips, int type) {
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.tips = tips;
        this.type = type;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(float longtitude) {
        this.longtitude = longtitude;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
