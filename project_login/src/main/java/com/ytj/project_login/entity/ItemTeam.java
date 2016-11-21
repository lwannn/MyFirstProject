package com.ytj.project_login.entity;

/**
 * 消息tab 的item的数据的实体类
 * Created by Administrator on 2016/11/15.
 */

public class ItemTeam {
    private int id;//组员id
    private String alias;//组员别名
    private String picPath;//头像的路径
    private String tel;//电话号码
    private Type type;//item的类型

    public ItemTeam(int id, String alias, String picPath, String tel, Type type) {
        this.id = id;
        this.alias = alias;
        this.picPath = picPath;
        this.tel = tel;
        this.type = type;
    }

    public enum Type {
        GROUP, MEMBER
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

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
