package com.ytj.project_login.entity;

/**
 * listView 的群聊消息的实体类
 * Created by Administrator on 2016/9/26.
 */
public class LvChatMsg {
    public String name;
    public String content;//聊天的内容
    public String intime;//聊天的时间
    public String headIcon;//头像的图片路径
    public Type type;//聊天内容的类型

    public enum Type {
        INCOMING, OUTCOMING
    }

    public LvChatMsg(String name, String content, String intime, String headIcon, Type type) {
        this.name = name;
        this.content = content;
        this.intime = intime;
        this.headIcon = headIcon;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getHeadIcon() {
        return headIcon;
    }

    public void setHeadIcon(String headIcon) {
        this.headIcon = headIcon;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
