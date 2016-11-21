package com.ytj.project_login.jsonEntity;

/**
 * 最新消息的实体类
 * Created by Administrator on 2016/11/17.
 */

public class RecentMsg{
    private String content;
    private String intime;

    public RecentMsg(String content, String intime) {
        this.content = content;
        this.intime = intime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIntime() {
        return intime;
    }

    public void setIntime(String intime) {
        this.intime = intime;
    }

    @Override
    public String toString() {
        return "RecentMsg{" +
                "content='" + content + '\'' +
                ", intime='" + intime + '\'' +
                '}';
    }
}
