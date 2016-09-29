package com.ytj.project_login.jsonEntity;

import java.util.List;

/**
 * Created by Administrator on 2016/9/29.
 */
public class CaseRoot {
    private int ret;

    private List<Objects> Objects;

    private List<CaseSpyteam> Spyteam;

    public void setRet(int ret) {
        this.ret = ret;
    }

    public int getRet() {
        return this.ret;
    }

    public void setObjects(List<Objects> Objects) {
        this.Objects = Objects;
    }

    public List<Objects> getObjects() {
        return this.Objects;
    }

    public void setSpyteam(List<CaseSpyteam> Spyteam) {
        this.Spyteam = Spyteam;
    }

    public List<CaseSpyteam> getSpyteam() {
        return this.Spyteam;
    }

}
