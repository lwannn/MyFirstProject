package com.ytj.project_login.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.ytj.project_login.entity.IdCaseName;

import java.util.Set;

/**
 * sharepreference工具类
 * ps:这个工具类的默认值一定要是相对应类型的值
 * Created by Administrator on 2016/9/19.
 */
public class SharePreferencesUtil {
    /**
     * 保存在手机里面的文件名
     */
    private static final String FILE_NAME = "share_data";
    private static SharedPreferences sp;

    //需要存储数据的key
    public static final String IP="ip";
    public static final String CHECK_ID="checkid";
    public static final String HEAD_PORTRAIT_URL="headPortraitUrl";
    //单例模式
    private static SharedPreferences getSharedPreferences(Context context) {
        if (sp == null) {
            synchronized (SharedPreferences.class) {
                if (sp == null) {
                    sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
                }
            }
        }
        return sp;
    }

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param context
     * @param key
     * @param object
     */
    public static void setParam(Context context, String key, Object object) {

        String type = object.getClass().getSimpleName();
        sp = getSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();

        if ("String".equals(type)) {
            editor.putString(key, (String) object);
        } else if ("Integer".equals(type)) {
            editor.putInt(key, (Integer) object);
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(key, (Boolean) object);
        } else if ("Float".equals(type)) {
            editor.putFloat(key, (Float) object);
        } else if ("Long".equals(type)) {
            editor.putLong(key, (Long) object);
        }
        editor.commit();
    }

    public static void setParam(Context context, String key, Set<String> object) {
        sp = getSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putStringSet(key, object);
        editor.commit();
    }

    public static Set<String> getParam(Context context, String key) {
        sp = getSharedPreferences(context);
        return sp.getStringSet(key, null);
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param context
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object getParam(Context context, String key, Object defaultObject) {
        String type = defaultObject.getClass().getSimpleName();
        sp = getSharedPreferences(context);

        if ("String".equals(type)) {
            return sp.getString(key, (String) defaultObject);
        } else if ("Integer".equals(type)) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if ("Boolean".equals(type)) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if ("Float".equals(type)) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if ("Long".equals(type)) {
            return sp.getLong(key, (Long) defaultObject);
        }

        return null;
    }
}
