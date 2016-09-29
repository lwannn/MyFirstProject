package com.ytj.project_login.jsonEntity;

/**
 * 目标任务的实体类
 */
public class Objects {
    private String alias;

    private int caseid;

    private int deleted;

    private String embed;

    private int id;

    private String intime;

    private String name;

    private String opath;

    private String remark;

    private int sex;

    private String tel;

    private int userid;

    public Objects(String alias, int caseid, int id, String intime, String name, String opath, String remark, int sex, String tel, int userid) {
        this.alias = alias;
        this.caseid = caseid;
        this.id = id;
        this.intime = intime;
        this.name = name;
        this.opath = opath;
        this.remark = remark;
        this.sex = sex;
        this.tel = tel;
        this.userid = userid;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getAlias() {
        return this.alias;
    }

    public void setCaseid(int caseid) {
        this.caseid = caseid;
    }

    public int getCaseid() {
        return this.caseid;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public int getDeleted() {
        return this.deleted;
    }

    public void setEmbed(String embed) {
        this.embed = embed;
    }

    public String getEmbed() {
        return this.embed;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setIntime(String intime) {
        this.intime = intime;
    }

    public String getIntime() {
        return this.intime;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setOpath(String opath) {
        this.opath = opath;
    }

    public String getOpath() {
        return this.opath;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getSex() {
        return this.sex;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getTel() {
        return this.tel;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getUserid() {
        return this.userid;
    }

    @Override
    public String toString() {
        return "Objects{" +
                "alias='" + alias + '\'' +
                ", caseid=" + caseid +
                ", deleted=" + deleted +
                ", embed='" + embed + '\'' +
                ", id=" + id +
                ", intime='" + intime + '\'' +
                ", name='" + name + '\'' +
                ", opath='" + opath + '\'' +
                ", remark='" + remark + '\'' +
                ", sex=" + sex +
                ", tel='" + tel + '\'' +
                ", userid=" + userid +
                '}';
    }
}
