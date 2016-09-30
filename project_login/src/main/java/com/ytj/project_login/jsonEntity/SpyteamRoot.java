package com.ytj.project_login.jsonEntity;

import java.util.List;

/**
 * Created by Administrator on 2016/9/30.
 */
public class SpyteamRoot {
    private List<TeamUserMore> dat ;

    private int ret;

    public void setDat(List<TeamUserMore> dat){
        this.dat = dat;
    }
    public List<TeamUserMore> getDat(){
        return this.dat;
    }
    public void setRet(int ret){
        this.ret = ret;
    }
    public int getRet(){
        return this.ret;
    }

}
