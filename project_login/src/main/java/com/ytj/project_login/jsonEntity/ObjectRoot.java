package com.ytj.project_login.jsonEntity;

import java.util.List;

/**
 * Created by Administrator on 2016/10/10.
 */
public class ObjectRoot {
    private int ret;

    private List<ObjectData> data;

    public void setRet(int ret) {
        this.ret = ret;
    }

    public int getRet() {
        return this.ret;
    }

    public void setData(List<ObjectData> data) {
        this.data = data;
    }

    public List<ObjectData> getData() {
        return this.data;
    }
}
