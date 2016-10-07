package com.ytj.project_login.entity;

/**
 * 字段包含id和name的实体类（简明扼要）
 * Created by Administrator on 2016/9/27.
 */
public class IdName {
    private int id;
    private String alias;
    private String tel;//电话号码

    public IdName(int id, String alias, String tel) {
        this.id = id;
        this.alias = alias;
        this.tel = tel;
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

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
