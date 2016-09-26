package com.ytj.project_login.jsonEntity;

import java.util.List;

/**
 * 组成员的list集合类
 * Created by Administrator on 2016/9/25.
 */
public class TeamUsersRoot {
    private List<TeamUser> dat;
    private int ret;

    public List<TeamUser> getDat() {
        return dat;
    }

    public void setDat(List<TeamUser> dat) {
        this.dat = dat;
    }

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }
}
