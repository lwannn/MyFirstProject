package com.ytj.project_login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ytj.project_login.db.dao.DBDao;
import com.ytj.project_login.entity.TelName;
import com.ytj.project_login.jsonEntity.ChatMsg;
import com.ytj.project_login.utils.ConstantUtil;

import java.util.List;

public class PersonalChatActivity extends BaseChatActivity {

    {
        type = 0;
    }

    @Override
    public int getChatType() {
        return ConstantUtil.PERSONAL_CHAT_TYPE;
    }

    @Override
    public String getTitleEnd() {
        return "";
    }

    @Override
    public String getNewInfoUrl(String mIp, int fromId, int toId) {
        return "http://" + mIp + "/MapLocal/android/getChat?fromnum=" + toId + "&tonum=" + fromId + "&type=" + ConstantUtil.PERSONAL_CHAT_TYPE;
    }

    @Override
    public String getUrl(String mIp, int fromId, int toId, int chatMsgMaxId) {
        return "http://" + mIp + "/MapLocal/android/getChat?fromnum=" + toId + "&tonum=" + fromId + "&type=" + ConstantUtil.PERSONAL_CHAT_TYPE + "&maxid=" + chatMsgMaxId;
    }

    @Override
    public int getChatMsgMaxId(DBDao dbDao, int fromId, int toId) {
        int idft = dbDao.getPersonalChatMsgMaxId(toId + "", fromId + "", ConstantUtil.PERSONAL_CHAT_TYPE);
        int idtf = dbDao.getPersonalChatMsgMaxId(fromId + "", toId + "", ConstantUtil.PERSONAL_CHAT_TYPE);
        return idft > idtf ? idft : idtf;
    }

    @Override
    public void checkLocation(final Context context, final String chatname, final String tel, View mCheckLocation) {
        mCheckLocation.setVisibility(View.VISIBLE);//显示textView
        final TelName telName = new TelName(tel, chatname);
        //添加点击事件
        mCheckLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), chatname + "," + tel, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, PersonalBDMapActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("telName", telName);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public List<ChatMsg> getRefreshChatMsgList(DBDao dbDao, String fromnum, String tonum, int limit, int offset) {
        List<ChatMsg> chatMsgs = null;
        chatMsgs = dbDao.getPersonalChatMsg(fromnum, tonum, ConstantUtil.PERSONAL_CHAT_TYPE, limit, offset);
        return chatMsgs;
    }
}
