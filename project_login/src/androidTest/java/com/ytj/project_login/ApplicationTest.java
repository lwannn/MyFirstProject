package com.ytj.project_login;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.ytj.project_login.db.dao.DBDao;
import com.ytj.project_login.dbEntity.User;
import com.ytj.project_login.jsonEntity.Cases;
import com.ytj.project_login.jsonEntity.ChatMsg;
import com.ytj.project_login.utils.MapUtil;

import java.util.List;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void test() {
        Log.e("System.out", "可以进行测试哦，boy!");
    }

    public void testGetUserInfo() {
        DBDao dbDao = new DBDao(getContext());
        User userInfo = dbDao.getUserInfo(3);
        Log.e("System.out", userInfo == null ? "没有数据哦！亲！" : userInfo.toString());
    }

    public void testAddUser() {
        DBDao dbDao = new DBDao(getContext());
        dbDao.addOrUpdateUser(1, "旺仔", "罗帅", "13545906534", "/image.jpg");
    }

    public void testGetCaseInfoByID() {
        DBDao dbDao = new DBDao(getContext());
        Cases caseInfoById = dbDao.getCaseInfoById(3);
        Log.e("System.out", caseInfoById == null ? "没有数据哦！亲！" : caseInfoById.toString());
    }

    //在程序完整运行时可以获取到数据（单元测试获取不到数据，我也感到很诧异0.0）
    public void testgetMapName() {
        String name = MapUtil.getName(3);
        Log.e("System.out", name);
    }

    public void testAddChatmsg() {
        ChatMsg chatMsg = new ChatMsg("罗旺听说你很6啊", 0, "3", 2, "13:39", null, "8", 1);
        DBDao dbDao = new DBDao(getContext());
        dbDao.addChatMsg(chatMsg);
    }

    public void testGetChatMsg() {
        DBDao dbDao = new DBDao(getContext());
        List<ChatMsg> teamChatMsg = dbDao.getTeamChatMsg(1, "8", 2, 0);
        Log.e("System.out", teamChatMsg.size() + "");
    }

    public void testGetTeamChatMsgMaxId() {
        DBDao dbDao = new DBDao(getContext());
        int teamChatMsgMaxId = dbDao.getTeamChatMsgMaxId(1, "8");
        Log.e("System.out", teamChatMsgMaxId + "");
    }
}