package com.ytj.project_login.jsonEntity;

/**
 * Created by Administrator on 2016/9/21.
 */
public class Department {
    private int id;

    private String manager;

    private String name;

    private int uid;

    private String users;

    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return this.id;
    }
    public void setManager(String manager){
        this.manager = manager;
    }
    public String getManager(){
        return this.manager;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setUid(int uid){
        this.uid = uid;
    }
    public int getUid(){
        return this.uid;
    }
    public void setUsers(String users){
        this.users = users;
    }
    public String getUsers(){
        return this.users;
    }

    @Override
    public String toString() {
        return "Department{" +
                "id=" + id +
                ", manager='" + manager + '\'' +
                ", name='" + name + '\'' +
                ", uid=" + uid +
                ", users='" + users + '\'' +
                '}';
    }
}
