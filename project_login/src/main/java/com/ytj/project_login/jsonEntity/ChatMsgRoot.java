package com.ytj.project_login.jsonEntity;

import java.util.List;

/**
 * Created by Administrator on 2016/9/26.
 */
public class ChatMsgRoot {
    private int ret;

    private List<ChatMsg> data ;

    public void setRet(int ret){
        this.ret = ret;
    }
    public int getRet(){
        return this.ret;
    }

    public List<ChatMsg> getData() {
        return data;
    }

    public void setData(List<ChatMsg> data) {
        this.data = data;
    }
}
