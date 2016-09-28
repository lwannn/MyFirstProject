package com.ytj.project_login.entity;

/**
 * 字段包含id和caseName的实体类
 * Created by Administrator on 2016/9/28.
 */
public class IdCaseName {
    private int id;
    private String caseName;

    public IdCaseName(int id, String caseName) {
        this.id = id;
        this.caseName = caseName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }
}
