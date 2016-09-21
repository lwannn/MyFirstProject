package com.ytj.project_login.entity;

import java.util.List;

public class Spyteam {

	private Integer id;
	
	private String name;
	
	private Integer uid;
	
	private List<Users> users;
	private Users manager;

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

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public List<Users> getUsers() {
		return users;
	}

	public void setUsers(List<Users> users) {
		this.users = users;
	}

	public Users getManager() {
		return manager;
	}

	public void setManager(Users manager) {
		this.manager = manager;
	}
	
}
