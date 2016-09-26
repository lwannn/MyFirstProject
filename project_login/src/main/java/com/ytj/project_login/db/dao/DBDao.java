package com.ytj.project_login.db.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ytj.project_login.db.DBOpenHelper;
import com.ytj.project_login.dbEntity.User;
import com.ytj.project_login.jsonEntity.Cases;
import com.ytj.project_login.jsonEntity.ChatMsg;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库的工具类
 * Created by Administrator on 2016/9/22.
 */
public class DBDao {
    private DBOpenHelper helper;

    public DBDao(Context context) {
        this.helper = new DBOpenHelper(context);
    }

    //添加user(如果存在就更新，否则就插入)
    public void addOrUpdateUser(Integer id, String username, String alias, String tel, String path) {
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor c = db.rawQuery("select * from users where id=?", new String[]{id + ""});
        if (c.moveToNext()) {
            db.execSQL("update users set username=?,alias=?,tel=?,path=? where id=?", new String[]{username, alias, tel, path, id + ""});
        } else {
            db.execSQL("insert into users values(?,?,?,?,?)", new String[]{id + "", username, alias, tel, path});
        }
        c.close();
        db.close();
    }

    //根据username获取user的基本信息
    public User getUserInfo(int userid) {
        User user = null;
        SQLiteDatabase db = helper.getReadableDatabase();
        //应该只有一行
        Cursor c = db.rawQuery("select username,alias,tel from users where id=?", new String[]{userid + ""});
        while (c.moveToNext()) {
            String username = c.getString(c.getColumnIndex("username"));
            String alias = c.getString(c.getColumnIndex("alias"));
            String tel = c.getString(c.getColumnIndex("tel"));
            user = new User(username, alias, tel);
        }
        c.close();
        db.close();

        return user;
    }

    //添加案件
    public void addOrUpdateCase(Cases casee) {
        //初始化数据
        int id = casee.getId();
        String name = casee.getName();
        String intime = casee.getIntime();
        int userid = casee.getUserid();
        String remark = casee.getRemark();
        String linktel = casee.getLinktel();
        String linkman = casee.getLinkman();
        String casenum = casee.getCasenum();
        String casekind = casee.getCasekind();
        String handover = casee.getHandover();
        String handdate = casee.getHanddate();

        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor c = db.rawQuery("select * from cases where id=?", new String[]{id + ""});
        if (c.moveToNext()) {//说明数据库中存在这个案件(只需要进行更新)
            db.execSQL("update cases set name=?,intime=?,userid=?,remark=?,linktel=?,linkman=?,casenum=?,casekind=?,handover=?,handdate=? where id=?", new String[]{name, intime, userid + "", remark, linktel, linkman, casenum, casekind, handover, handdate, id + ""});
        } else {
            db.execSQL("insert into cases values(?,?,?,?,?,?,?,?,?,?,?)", new String[]{id + "", name, intime, userid + "", remark, linktel, linkman, casenum, casekind, handover, handdate});
        }

        c.close();
        db.close();
    }

    //根据id获取案件信息
    public Cases getCaseInfoById(int id) {
        Cases cases = null;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("select * from cases where id=?", new String[]{id + ""});
        while (c.moveToNext()) {
            String name = c.getString(c.getColumnIndex("name"));
            String intime = c.getString(c.getColumnIndex("intime"));
            int userid = c.getInt(c.getColumnIndex("userid"));
            String remark = c.getString(c.getColumnIndex("remark"));
            String linktel = c.getString(c.getColumnIndex("linktel"));
            String linkman = c.getString(c.getColumnIndex("linkman"));
            String casenum = c.getString(c.getColumnIndex("casenum"));
            String casekind = c.getString(c.getColumnIndex("casekind"));
            String handover = c.getString(c.getColumnIndex("handover"));
            String handdate = c.getString(c.getColumnIndex("handdate"));

            cases = new Cases(casekind, casenum, handdate, handover, id, intime, linkman, linktel, name, remark, userid);
        }

        return cases;
    }

    //添加聊天信息
    public void addChatMsg(ChatMsg chatMsg) {
        SQLiteDatabase db = helper.getWritableDatabase();

        int id = chatMsg.getId();
        String fromnum = chatMsg.getFromnum();
        String content = chatMsg.getContent();
        String tonum = chatMsg.getTonum();
        int type = chatMsg.getType();
        int ctype = chatMsg.getCtype();
        String intime = chatMsg.getIntime();

        db.execSQL("insert into chatmsg values(?,?,?,?,?,?,?)", new String[]{id + "", fromnum, content, tonum, type + "", ctype + "", intime});
        db.close();
    }

    /**
     * 分级查询组的聊天信息
     *
     * @param type   判断是群聊消息还是私聊消息（1群0私）
     * @param tonum  群消息就是组的id,私聊信息就是组员的id
     * @param limit  查询的最小条数
     * @param offset 查询的起始条数位置
     */
    public List<ChatMsg> getTeamChatMsg(int type, String tonum, int limit, int offset) {
        SQLiteDatabase db = helper.getReadableDatabase();
        List<ChatMsg> chatMsgList = new ArrayList<ChatMsg>();
        Cursor c = db.rawQuery("select * from chatmsg where type=? and tonum=? limit ? offset ?", new String[]{type + "", tonum, limit + "", offset + ""});
        while (c.moveToNext()) {
            int id = c.getInt(c.getColumnIndex("id"));
            String fromnum = c.getString(c.getColumnIndex("fromnum"));
            String content = c.getString(c.getColumnIndex("content"));
            int ctype = c.getInt(c.getColumnIndex("ctype"));
            String intime = c.getString(c.getColumnIndex("intime"));

            ChatMsg chatMsg = new ChatMsg(content, ctype, fromnum, id, intime, null, tonum, type);
            chatMsgList.add(chatMsg);
        }

        c.close();
        db.close();

        return chatMsgList;
    }

    /**
     * 获取组聊天信息的最大id
     *
     * @param type
     * @param tonum
     * @return
     */
    public int getTeamChatMsgMaxId(int type, String tonum) {
        int teamMaxId = -1;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("select id from chatmsg where type=? and tonum=? order by id desc", new String[]{type + "", tonum});
        if (c.moveToNext()) {
            teamMaxId = c.getInt(c.getColumnIndex("id"));
        }
        c.close();
        db.close();
        return teamMaxId;
    }
}
