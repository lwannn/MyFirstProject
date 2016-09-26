package com.ytj.project_login.jsonEntity;

/**
 * Created by Administrator on 2016/9/25.
 */
public class TeamUser {
    private String alias;

    private String department;

    private int deptid;

    private int deptuid;

    private int id;

    private boolean leader;

    private String password;

    private String path;

    private String role;

    private int roleid;

    private String tel;

    private String username;

    public void setAlias(String alias){
        this.alias = alias;
    }
    public String getAlias(){
        return this.alias;
    }
    public void setDepartment(String department){
        this.department = department;
    }
    public String getDepartment(){
        return this.department;
    }
    public void setDeptid(int deptid){
        this.deptid = deptid;
    }
    public int getDeptid(){
        return this.deptid;
    }
    public void setDeptuid(int deptuid){
        this.deptuid = deptuid;
    }
    public int getDeptuid(){
        return this.deptuid;
    }
    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return this.id;
    }
    public void setLeader(boolean leader){
        this.leader = leader;
    }
    public boolean getLeader(){
        return this.leader;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public String getPassword(){
        return this.password;
    }
    public void setPath(String path){
        this.path = path;
    }
    public String getPath(){
        return this.path;
    }
    public void setRole(String role){
        this.role = role;
    }
    public String getRole(){
        return this.role;
    }
    public void setRoleid(int roleid){
        this.roleid = roleid;
    }
    public int getRoleid(){
        return this.roleid;
    }
    public void setTel(String tel){
        this.tel = tel;
    }
    public String getTel(){
        return this.tel;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public String getUsername(){
        return this.username;
    }

    @Override
    public String toString() {
        return "TeamUser{" +
                "alias='" + alias + '\'' +
                ", department='" + department + '\'' +
                ", deptid=" + deptid +
                ", deptuid=" + deptuid +
                ", id=" + id +
                ", leader=" + leader +
                ", password='" + password + '\'' +
                ", path='" + path + '\'' +
                ", role='" + role + '\'' +
                ", roleid=" + roleid +
                ", tel='" + tel + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
