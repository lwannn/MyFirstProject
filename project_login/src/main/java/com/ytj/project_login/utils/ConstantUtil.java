package com.ytj.project_login.utils;

/**
 * 保存一些常用常量的工具类
 * Created by Administrator on 2016/9/26.
 */
public class ConstantUtil {

    public static final int TEAM_CHAT_TYPE = 1;//群聊的type类型
    public static final int PERSONAL_CHAT_TYPE = 0;//私聊的type类型

    public static final int CHAT_WRITING_TYPE = 0;//聊天类型为文字
    public static final int CHAT_IMAGE_TYPE = 1;//聊天类型为图片
    public static final int CHAT_MAP_TYPE = 4;//聊天类型为地理信息

    //静态变量，用于可以在任何地方知道是否有未读消息
    public static boolean IS_HaveOrNO = false;
    //class名字Log时定位用
    public static final String NetService = "NetService";
    public static String IP = "http://192.168.2.50:8080";
    public static String userName = "";
    public static String userPassword;
}
