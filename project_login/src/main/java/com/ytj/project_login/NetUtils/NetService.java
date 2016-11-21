package com.ytj.project_login.netUtils;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ytj.project_login.DetailActivity;
import com.ytj.project_login.R;
import com.ytj.project_login.utils.ConstantUtil;
import com.ytj.project_login.utils.SharePreferencesUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;

/**
 * Created by ddanyang on 2016/10/17.
 * 程序退出后，后台轮训，检查未读消息
 * 在DetailActivity中，onCreate方法stop服务，onKeyDown方法开启
 */

public class NetService extends Service {
    NotificationManager manager;
    String data;
    Timer timer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (isProcessRunning(NetService.this)) {
                    netTaskInBackground();
                }
            }
        }, 1, 5000);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }

    void netTaskInBackground() {
        Log.i("isRun", "refershUI: run");
        OkHttpUtils
                .get()
                .url(getConnectionUrl(DetailActivity.MINE_ID))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(String s) {
                        if (s.contains(ConstantUtil.NetService)) {//数据请求失败
                            //Toast.makeText(context, s, Toast.LENGTH_LONG).show();
                        } else {//数据请求成功
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                JSONArray user = jsonObject.getJSONArray("users");
                                JSONArray depts = jsonObject.getJSONArray("depts");
                                if (user.length() != 0 || depts.length() != 0) {
                                    if (user.length() == 1) {
                                        JSONObject u = user.getJSONObject(0);
                                        if (u.getString("fromnum").equals((String) SharePreferencesUtil.getParam(NetService.this, "userId", ""))) {
                                            return;
                                        }
                                    }
                                    Log.i("hhhhh", "onResponse: " + user.length());
                                    Log.i("hhhhh", "onResponse: " + depts.length());

                                    Intent intent = new Intent(NetService.this, DetailActivity.class);
                                    intent.putExtra("username", ConstantUtil.userName);
                                    PendingIntent pendingIntent = PendingIntent.getActivity(NetService.this,
                                            0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

                                    Notification.Builder builder = new Notification.Builder(NetService.this)
                                            .setSmallIcon(R.mipmap.ic_launcher)
                                            .setContentTitle("聊天导航")
                                            .setContentText("您有新消息未读取，请点击查看");
                                    builder.setTicker("新消息");
                                    builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher, null));
                                    builder.setAutoCancel(true);
                                    builder.setContentIntent(pendingIntent);
                                    if (Build.VERSION.SDK_INT > 15) {
                                        manager.notify(121, builder.build());
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });
    }

    String getConnectionUrl(int id) {
        String url = ConstantUtil.IP + "/MapLocal/android/readList?id=" + id;
        return url;
    }

    public boolean isProcessRunning(Context context) {
        Context mContext = context.getApplicationContext();
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = mContext.getPackageName();
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) return false;
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }
}
