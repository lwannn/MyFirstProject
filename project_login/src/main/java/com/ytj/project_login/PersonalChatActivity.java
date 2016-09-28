package com.ytj.project_login;

import com.ytj.project_login.db.dao.DBDao;
import com.ytj.project_login.utils.ConstantUtil;

public class PersonalChatActivity extends BaseChatActivity {
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
        return dbDao.getPersonalChatMsgMaxId(toId + "", fromId + "", ConstantUtil.PERSONAL_CHAT_TYPE);
    }
}
