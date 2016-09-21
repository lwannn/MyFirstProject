package com.ytj.project_login.entity;

import java.util.Date;

public class Cases {

	private Integer id;

	private String name;

	private Date intime;

	private String remark;

	private Integer userid;
	
	private String casenum;

	private String casekind;//案件性质
	
	private String linktel;//联系方式
	
	private String linkman;//联系人
	
	private String spies;//侦查员 
	
	private String handover;//交办单位
	
	private Date handdate;//交办日期
	
	private Integer status;//案件状态；
	
	private Integer applyer ;//申请结案用户ID;
	
	private Date  applytime;//申请结案时间
	
	private Short deleted;//是否已删除
	
	private Integer dept;

	public Integer getDept() {
		return dept;
	}

	public void setDept(Integer dept) {
		this.dept = dept;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getIntime() {
		return intime;
	}

	public void setIntime(Date intime) {
		this.intime = intime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public String getCasenum() {
		return casenum;
	}

	public void setCasenum(String casenum) {
		this.casenum = casenum;
	}

	public String getCasekind() {
		return casekind;
	}

	public void setCasekind(String casekind) {
		this.casekind = casekind;
	}

	public String getLinktel() {
		return linktel;
	}

	public void setLinktel(String linktel) {
		this.linktel = linktel;
	}

	public String getLinkman() {
		return linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	public String getSpies() {
		return spies;
	}

	public void setSpies(String spies) {
		this.spies = spies;
	}

	public String getHandover() {
		return handover;
	}

	public void setHandover(String handover) {
		this.handover = handover;
	}

	public Date getHanddate() {
		return handdate;
	}

	public void setHanddate(Date handdate) {
		this.handdate = handdate;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getApplyer() {
		return applyer;
	}

	public void setApplyer(Integer applyer) {
		this.applyer = applyer;
	}

	public Date getApplytime() {
		return applytime;
	}

	public void setApplytime(Date applytime) {
		this.applytime = applytime;
	}

	public Short getDeleted() {
		return deleted;
	}

	public void setDeleted(Short deleted) {
		this.deleted = deleted;
	}

}
