package com.ytj.project_login.db;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/22.
 */
public class DBOpenHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "maplocal.db";
    private static final int VERSION = 1;

    private static final String query_case = "select id,name,intime,remark,linktel " +
            "from cases";

    public DBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //建表
        db.execSQL("create table users (id Integer primary key,username String not null,alias String not null,tel String not null,path String)");
        db.execSQL("create table cases (id Integer primary key,name String not null,intime String not null,userid Integer,remark String,linktel String,linkman String,casenum String not null,casekind String,handover String not null,handdate String)");
        db.execSQL("create table chatmsg (id Integer primary key,fromnum String not null,content String not null,tonum String not null,type Integer not null,ctype Integer not null,intime String not null)");
        db.execSQL("create table objects(id Integer primary key,name String not null,remark String,intime String,caseid int not null,userid int not null,tel String not null,sex int not null,opath String,alias String)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public List<String> query_case() {
        SQLiteDatabase dbUtils = getWritableDatabase();
        Cursor cursor = dbUtils.rawQuery(query_case, null);
        List<String> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            int size = cursor.getColumnCount();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < size; i++) {
                sb.append(cursor.getString(i) + ";");
            }
            list.add(sb.toString());
        }
        cursor.close();
        return list;
    }

}
