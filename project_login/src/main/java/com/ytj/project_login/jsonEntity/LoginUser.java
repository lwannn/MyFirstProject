package com.ytj.project_login.jsonEntity;

public class LoginUser {

	private String username;
	
	private String password;
	
	private Integer roleid;
	
	private Integer id;
	
	private boolean  leader;

	public boolean getLeader() {
		return leader;
	}

	public void setLeader(boolean leader) {
		this.leader = leader;
	}

	public Integer getRoleid() {
		return roleid;
	}

	public void setRoleid(Integer roleid) {
		this.roleid = roleid;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "LoginUser{" +
				"username='" + username + '\'' +
				", password='" + password + '\'' +
				", roleid=" + roleid +
				", id=" + id +
				", leader=" + leader +
				'}';
	}
}
