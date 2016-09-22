package com.ytj.project_login.dbEntity;

/**
 * users数据库对应的实体类
 * Created by Administrator on 2016/9/22.
 */
public class User {
    private String username;
    private String alias;//别名
    private String tel;

    public User(String username, String alias, String tel) {
        this.username = username;
        this.alias = alias;
        this.tel = tel;
    }

    public User() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", alias='" + alias + '\'' +
                ", tel='" + tel + '\'' +
                '}';
    }
}
