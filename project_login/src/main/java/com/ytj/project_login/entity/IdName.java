package com.ytj.project_login.entity;

/**
 * 字段包含id和name的实体类（简明扼要）
 * Created by Administrator on 2016/9/27.
 */
public class IdName {
    private int id;
    private String alias;

    public IdName(int id, String alias) {
        this.id = id;
        this.alias = alias;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
