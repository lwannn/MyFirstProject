package com.ytj.project_login.entity;

public class Users extends LoginUser{
	private Integer deptuid;
	
	private String path;
	
	
	private Integer id;
	
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

	private String alias;
	
	
	private String tel;
	
	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}


	private Integer deptid;
	
	private Spyteam department;
	
	private Role role;

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
	
	
}
