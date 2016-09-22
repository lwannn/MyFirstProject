package com.ytj.project_login.jsonEntity;

public class Role {

	private Integer id;
	
	private String rolename;
	
	private String acids;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRolename() {
		return rolename;
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

	public String getAcids() {
		return acids;
	}

	public void setAcids(String acids) {
		this.acids = acids;
	}

	@Override
	public String toString() {
		return "Role{" +
				"id=" + id +
				", rolename='" + rolename + '\'' +
				", acids='" + acids + '\'' +
				'}';
	}
}
