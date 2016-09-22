package com.ytj.project_login.jsonEntity;

/**
 * 既可以在jsonEntity中用，也可以在dbEntity中使用
 */
public class Cases {

    private String applyer;

    private String applytime;

    private String casekind;//

    private String casenum;//

    private String deleted;

    private int dept;

    private String handdate;//

    private String handover;//

    private int id;//

    private String intime;//

    private String linkman;//

    private String linktel;//

    private String name;//

    private String remark;//

    private String spies;

    private String status;

    private int userid;//

    public Cases(String casekind, String casenum, String handdate, String handover, int id, String intime, String linkman, String linktel, String name, String remark, int userid) {
        this.casekind = casekind;
        this.casenum = casenum;
        this.handdate = handdate;
        this.handover = handover;
        this.id = id;
        this.intime = intime;
        this.linkman = linkman;
        this.linktel = linktel;
        this.name = name;
        this.remark = remark;
        this.userid = userid;
    }

    public void setApplyer(String applyer) {
        this.applyer = applyer;
    }

    public String getApplyer() {
        return this.applyer;
    }

    public void setApplytime(String applytime) {
        this.applytime = applytime;
    }

    public String getApplytime() {
        return this.applytime;
    }

    public void setCasekind(String casekind) {
        this.casekind = casekind;
    }

    public String getCasekind() {
        return this.casekind;
    }

    public void setCasenum(String casenum) {
        this.casenum = casenum;
    }

    public String getCasenum() {
        return this.casenum;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getDeleted() {
        return this.deleted;
    }

    public void setDept(int dept) {
        this.dept = dept;
    }

    public int getDept() {
        return this.dept;
    }

    public void setHanddate(String handdate) {
        this.handdate = handdate;
    }

    public String getHanddate() {
        return this.handdate;
    }

    public void setHandover(String handover) {
        this.handover = handover;
    }

    public String getHandover() {
        return this.handover;
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

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    public String getLinkman() {
        return this.linkman;
    }

    public void setLinktel(String linktel) {
        this.linktel = linktel;
    }

    public String getLinktel() {
        return this.linktel;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setSpies(String spies) {
        this.spies = spies;
    }

    public String getSpies() {
        return this.spies;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getUserid() {
        return this.userid;
    }


    @Override
    public String toString() {
        return "Cases{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", intime=" + intime +
                ", remark='" + remark + '\'' +
                ", userid=" + userid +
                ", casenum='" + casenum + '\'' +
                ", casekind='" + casekind + '\'' +
                ", linktel='" + linktel + '\'' +
                ", linkman='" + linkman + '\'' +
                ", spies='" + spies + '\'' +
                ", handover='" + handover + '\'' +
                ", handdate=" + handdate +
                ", status=" + status +
                ", applyer=" + applyer +
                ", applytime=" + applytime +
                ", deleted=" + deleted +
                ", dept=" + dept +
                '}';
    }
}
