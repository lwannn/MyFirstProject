package com.ytj.project_login.jsonEntity;

/**
 * Created by Administrator on 2016/9/26.
 */
public class ChatMsg {
    private String content;

    private int ctype;

    private String fromnum;

    private int id;

    private String intime;

    private String tel;

    private String tonum;

    private int type;

    public ChatMsg(String content, int ctype, String fromnum, int id, String intime, String tel, String tonum, int type) {
        this.content = content;
        this.ctype = ctype;
        this.fromnum = fromnum;
        this.id = id;
        this.intime = intime;
        this.tel = tel;
        this.tonum = tonum;
        this.type = type;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return this.content;
    }

    public void setCtype(int ctype) {
        this.ctype = ctype;
    }

    public int getCtype() {
        return this.ctype;
    }

    public void setFromnum(String fromnum) {
        this.fromnum = fromnum;
    }

    public String getFromnum() {
        return this.fromnum;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setIntime(String intime) {
        this.intime = intime;
    }

    public String getIntime() {
        return this.intime;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getTel() {
        return this.tel;
    }

    public void setTonum(String tonum) {
        this.tonum = tonum;
    }

    public String getTonum() {
        return this.tonum;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return "ChatMsg{" +
                "content='" + content + '\'' +
                ", ctype=" + ctype +
                ", fromnum='" + fromnum + '\'' +
                ", id=" + id +
                ", intime='" + intime + '\'' +
                ", tel='" + tel + '\'' +
                ", tonum='" + tonum + '\'' +
                ", type=" + type +
                '}';
    }
}
