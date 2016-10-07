package com.ytj.project_login.jsonEntity;

/**
 * 经纬度信息的实体类
 * Created by Administrator on 2016/10/6.
 */
public class Location {
    private String addrinfo;

    private int id;

    private String intime;

    private String lat;

    private String lon;

    private String mark;

    private String name;

    private String tel;

    private String url;

    public void setAddrinfo(String addrinfo) {
        this.addrinfo = addrinfo;
    }

    public String getAddrinfo() {
        return this.addrinfo;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setIntime(String intime) {
        this.intime = intime;
    }

    public String getIntime() {
        return this.intime;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLat() {
        return this.lat;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLon() {
        return this.lon;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getMark() {
        return this.mark;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getTel() {
        return this.tel;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return this.url;
    }
}
