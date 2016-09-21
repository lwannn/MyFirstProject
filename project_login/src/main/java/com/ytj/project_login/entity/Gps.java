package com.ytj.project_login.entity;

import java.util.Date;

public class Gps {
	private int id;
	private String lon;
	private String lat;
	private String tel;
	private Date intime ;
	private String addrinfo;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLon() {
		return lon;
	}
	public void setLon(String lon) {
		this.lon = lon;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public Date getIntime() {
		return intime;
	}
	public void setIntime(Date intime) {
		this.intime = intime;
	}
	public String getAddrinfo() {
		return addrinfo;
	}
	public void setAddrinfo(String addrinfo) {
		this.addrinfo = addrinfo;
	}
}
