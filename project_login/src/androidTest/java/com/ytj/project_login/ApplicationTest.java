package com.ytj.project_login;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.ytj.project_login.db.dao.DBDao;
import com.ytj.project_login.dbEntity.User;
import com.ytj.project_login.jsonEntity.Cases;

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
        User userInfo = dbDao.getUserInfo(1);
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
}