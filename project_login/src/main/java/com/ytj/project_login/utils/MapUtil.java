package com.ytj.project_login.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * map的工具类
 * Created by Administrator on 2016/9/26.
 */
public class MapUtil {
    //用来保存id对应的组员的姓名
    public static Map<Integer,String> mNames=new HashMap<Integer, String>();

    public static void setName(int id,String name){
        mNames.put(id,name);
    }

    public static String getName(int id){
        return mNames.get(id);
    }
}
