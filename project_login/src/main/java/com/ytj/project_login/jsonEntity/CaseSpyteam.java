package com.ytj.project_login.jsonEntity;

/**
 * 根据caseId获取case相关信息中的SpyTeam(和已经存在的SpyTeam的结构不同，不知道是神马个情况)
 * Created by Administrator on 2016/9/29.
 */
public class CaseSpyteam {
    private String children;

    private int id;

    private String manager;

    private String name;

    private String text;

    private int uid;

    private String users;

    public void setChildren(String children) {
        this.children = children;
    }

    public String getChildren() {
        return this.children;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getManager() {
        return this.manager;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getUid() {
        return this.uid;
    }

    public void setUsers(String users) {
        this.users = users;
    }

    public String getUsers() {
        return this.users;
    }

    @Override
    public String toString() {
        return "CaseSpyteam{" +
                "children='" + children + '\'' +
                ", id=" + id +
                ", manager='" + manager + '\'' +
                ", name='" + name + '\'' +
                ", text='" + text + '\'' +
                ", uid=" + uid +
                ", users='" + users + '\'' +
                '}';
    }
}
