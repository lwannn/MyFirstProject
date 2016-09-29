package com.ytj.project_login.entity;

import java.io.Serializable;

/**
 * 字段包含id和teamName的实体类
 * Created by Administrator on 2016/9/29.
 */
public class IdTeamName implements Serializable{
    private int id;
    private String teamName;

    public IdTeamName(int id, String teamName) {
        this.id = id;
        this.teamName = teamName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
}
