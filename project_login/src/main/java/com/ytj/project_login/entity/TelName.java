package com.ytj.project_login.entity;

import java.io.Serializable;

/**
 * 字段包含电话和姓名的实体类
 * Created by Administrator on 2016/9/30.
 */
public class TelName implements Serializable{
    private String tel;
    private String name;

    public TelName(String tel, String name) {
        this.tel = tel;
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TelName{" +
                "tel='" + tel + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
