package com.ytj.project_login.jsonEntity;

import java.util.List;

/**
 * Created by Administrator on 2016/9/21.
 */
public class Root {
    private Dat dat;

    private int ret;

    private List<Cases> cases ;

    private Department department;

    private Role role;

    public void setDat(Dat dat){
        this.dat = dat;
    }
    public Dat getDat(){
        return this.dat;
    }
    public void setRet(int ret){
        this.ret = ret;
    }
    public int getRet(){
        return this.ret;
    }
    public void setCases(List<Cases> cases){
        this.cases = cases;
    }
    public List<Cases> getCases(){
        return this.cases;
    }
    public void setDepartment(Department department){
        this.department = department;
    }
    public Department getDepartment(){
        return this.department;
    }
    public void setRole(Role role){
        this.role = role;
    }
    public Role getRole(){
        return this.role;
    }

    @Override
    public String toString() {
        return "Root{" +
                "dat=" + dat +
                ", ret=" + ret +
                ", cases=" + cases +
                ", department=" + department +
                ", role=" + role +
                '}';
    }
}
