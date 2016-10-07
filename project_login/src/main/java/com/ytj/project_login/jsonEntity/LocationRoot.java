package com.ytj.project_login.jsonEntity;

import java.util.List;

/**
 * Created by Administrator on 2016/10/6.
 */
public class LocationRoot {
    private int ret;

    private List<List<Location>> data;

    public void setRet(int ret) {
        this.ret = ret;
    }

    public int getRet() {
        return this.ret;
    }

    public List<List<Location>> getData() {
        return data;
    }

    public void setData(List<List<Location>> data) {
        this.data = data;
    }
}
