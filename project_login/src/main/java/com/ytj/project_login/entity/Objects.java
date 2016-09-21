package com.ytj.project_login.entity;

import java.util.Date;

public class Objects {

	private Integer id;

	private String name;

	private String remark;

	private Date intime;

	private Integer caseid;
	
	private Integer embed;
	
	private Integer userid;
	
	private Short deleted;
	
	private String tel;
	
	private Integer sex;
	
	private  String alias;
	
	private String opath;

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}


	public String getOpath() {
		return opath;
	}

	public void setOpath(String opath) {
		this.opath = opath;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getIntime() {
		return intime;
	}

	public void setIntime(Date intime) {
		this.intime = intime;
	}

	public Integer getCaseid() {
		return caseid;
	}

	public void setCaseid(Integer caseid) {
		this.caseid = caseid;
	}

	public Integer getEmbed() {
		return embed;
	}

	public void setEmbed(Integer embed) {
		this.embed = embed;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public Short getDeleted() {
		return deleted;
	}

	public void setDeleted(Short deleted) {
		this.deleted = deleted;
	}

}
