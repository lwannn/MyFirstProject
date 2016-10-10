package com.ytj.project_login.jsonEntity;

import java.util.List;

/**
 * Created by Administrator on 2016/10/10.
 */
public class TeamUserRoot {
    private int ret;

    private List<TeamUserData> data;

    public void setRet(int ret) {
        this.ret = ret;
    }

    public int getRet() {
        return this.ret;
    }

    public void setData(List<TeamUserData> data) {
        this.data = data;
    }

    public List<TeamUserData> getData() {
        return this.data;
    }
}
