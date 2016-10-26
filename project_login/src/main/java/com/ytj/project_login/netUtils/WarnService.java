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
import android.util.Log;
import android.widget.Toast;

import com.ytj.project_login.BaseBDMapActivity;
import com.ytj.project_login.DetailActivity;
import com.ytj.project_login.R;
import com.ytj.project_login.WarnMapActivity;
import com.ytj.project_login.utils.ConstantUtil;
import com.ytj.project_login.utils.SharePreferencesUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

/**
 * Created by ddanyang on 2016/10/17.
 * 任何情况下后台轮训，检查预警消息
 * 在DetailActivity.getInfo(),当获取到用户信息之后，userID
 */

public class WarnService extends Service {

    JSONObject jsonObject;
    NotificationManager manager;

    public WarnService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        warnTaskInBackground();
        return super.onStartCommand(intent, flags, startId);
    }

    //请求预警信息
    void warnTaskInBackground() {
        OkHttpUtils
                .post()
                .url(ConstantUtil.IP + "/MapLocal/android/checkNewLog")
                .addParams("id", (String) SharePreferencesUtil.getParam(this, "userId", ""))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Toast.makeText(WarnService.this, "预警请求有误", Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onResponse(String s) {
                        Log.i("WarnService", "onResponse: yes i am map" + s);
                        Toast.makeText(WarnService.this, s, Toast.LENGTH_LONG);
                        try {
                            jsonObject = new JSONObject(s);
                            if (jsonObject.length() != 0) {


                                Intent intent = new Intent(WarnService.this, WarnMapActivity.class);
                                intent.putExtra("lat", jsonObject.getString("lat"));
                                intent.putExtra("lon", jsonObject.getString("lon"));
                                intent.putExtra("wid", jsonObject.getString("wid"));
                                intent.putExtra("tid", jsonObject.getString("tid"));

                                PendingIntent pendingIntent = PendingIntent.getActivity(WarnService.this,
                                        0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

                                Notification.Builder builder = new Notification.Builder(WarnService.this)
                                        .setSmallIcon(R.mipmap.ic_launcher)
                                        .setContentTitle("聊天导航")
                                        .setContentText("预警信息，请点击查看");
                                builder.setTicker("预警");
                                builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher, null));
                                builder.setAutoCancel(true);
                                builder.setContentIntent(pendingIntent);
                                if (Build.VERSION.SDK_INT > 15) {
                                    manager.notify(122, builder.build());
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }
}
