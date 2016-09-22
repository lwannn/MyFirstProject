package com.ytj.project_login.jsonEntity;

public class Users extends LoginUser {
    private Integer deptuid;
    private String path;
    private String alias;
    private String tel;
    private Integer id;
    private Integer deptid;
    private Spyteam department;
    private Role role;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getDeptuid() {
        return deptuid;
    }

    public void setDeptuid(Integer deptuid) {
        this.deptuid = deptuid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Integer getDeptid() {
        return deptid;
    }

    public void setDeptid(Integer deptid) {
        this.deptid = deptid;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Spyteam getDepartment() {
        return department;
    }

    public void setDepartment(Spyteam department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return "Users{" +
                "deptuid=" + deptuid +
                ", path='" + path + '\'' +
                ", id=" + id +
                ", alias='" + alias + '\'' +
                ", tel='" + tel + '\'' +
                ", deptid=" + deptid +
                ", department=" + department +
                ", role=" + role +
                '}';
    }
}
