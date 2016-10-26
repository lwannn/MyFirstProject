package com.ytj.project_login.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ytj.project_login.ChatBDMapActivity;
import com.ytj.project_login.ChatTrueTimeBDMapActivity;
import com.ytj.project_login.R;
import com.ytj.project_login.entity.LocationInfo;
import com.ytj.project_login.entity.LvChatMsg;
import com.ytj.project_login.entity.TelName;
import com.ytj.project_login.jsonEntity.ObjectData;
import com.ytj.project_login.jsonEntity.ObjectRoot;
import com.ytj.project_login.jsonEntity.TeamUserData;
import com.ytj.project_login.jsonEntity.TeamUserRoot;
import com.ytj.project_login.utils.DensityUtil;
import com.ytj.project_login.utils.SharePreferencesUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/9/26.
 */
public class ChatMsgAdapter extends BaseAdapter {
    private List<LvChatMsg> mDatas;
    private LayoutInflater mInflater;
    private Context context;
    private String mIp;
    private ArrayList<LocationInfo> locationInfoList;
    private ArrayList<TelName> telNameArrayList;

    public ChatMsgAdapter(Context context, List<LvChatMsg> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
        mInflater = LayoutInflater.from(context);
        mIp = (String) SharePreferencesUtil.getParam(context, SharePreferencesUtil.IP, "1111");
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //判断是哪一种布局样式
    @Override
    public int getItemViewType(int position) {
        LvChatMsg.Type type = mDatas.get(position).getType();
        if (type == LvChatMsg.Type.INCOMING) {
            return 0;
        } else if (type == LvChatMsg.Type.OUTCOMING) {
            return 1;
        } else if (type == LvChatMsg.Type.INCOMINGMAP) {
            return 2;
        } else if (type == LvChatMsg.Type.OUTCOMINGMAP) {
            return 3;
        } else if (type == LvChatMsg.Type.INCOMINGIMAGE) {
            return 4;
        } else if (type == LvChatMsg.Type.OUTCOMINGIMAGE) {
            return 5;
        }
        return 100;
    }

    //返回布局样式的总类
    @Override
    public int getViewTypeCount() {
        return 6;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Handler handler = new Handler();
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            if (getItemViewType(position) == 0) {
                convertView = mInflater.inflate(R.layout.item_from_msg, parent, false);
                holder.tv_content = (TextView) convertView.findViewById(R.id.tv_fromMsg);
            } else if (getItemViewType(position) == 1) {
                convertView = mInflater.inflate(R.layout.item_to_msg, parent, false);
                holder.tv_content = (TextView) convertView.findViewById(R.id.tv_toMsg);
            } else if (getItemViewType(position) == 2) {
                convertView = mInflater.inflate(R.layout.item_from_msg, parent, false);
                holder.tv_content = (TextView) convertView.findViewById(R.id.tv_fromMsg);
                //TODO 如果显示图片，在点击输入框的时候，程序会down掉(getViewTypeCount()要一致)
                holder.iv_mapInfo = (ImageView) convertView.findViewById(R.id.iv_mapInfo);

                holder.tv_content.setVisibility(View.GONE);
                holder.iv_mapInfo.setVisibility(View.VISIBLE);
            } else if (getItemViewType(position) == 3) {
                convertView = mInflater.inflate(R.layout.item_to_msg, parent, false);
                holder.tv_content = (TextView) convertView.findViewById(R.id.tv_toMsg);
                holder.iv_mapInfo = (ImageView) convertView.findViewById(R.id.iv_mapInfo);

                holder.tv_content.setVisibility(View.GONE);
                holder.iv_mapInfo.setVisibility(View.VISIBLE);
            } else if (getItemViewType(position) == 4) {//发送过来的图片
                convertView = mInflater.inflate(R.layout.item_from_msg, parent, false);
                holder.tv_content = (TextView) convertView.findViewById(R.id.tv_fromMsg);
                holder.iv_mapInfo = (ImageView) convertView.findViewById(R.id.iv_mapInfo);

                holder.tv_content.setVisibility(View.GONE);
                holder.iv_mapInfo.setVisibility(View.VISIBLE);
                holder.iv_mapInfo.setImageResource(R.mipmap.ic_launcher);//设置一个默认的图片
            } else if (getItemViewType(position) == 5) {//发送出去的图片
                convertView = mInflater.inflate(R.layout.item_to_msg, parent, false);
                holder.tv_content = (TextView) convertView.findViewById(R.id.tv_toMsg);
                holder.iv_mapInfo = (ImageView) convertView.findViewById(R.id.iv_mapInfo);

                holder.tv_content.setVisibility(View.GONE);
                holder.iv_mapInfo.setVisibility(View.VISIBLE);
                holder.iv_mapInfo.setImageResource(R.mipmap.ic_launcher);//设置一个默认的图片
            }
            holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        LvChatMsg lvChatMsg = mDatas.get(position);
        holder.tv_date.setText(lvChatMsg.getIntime());
        holder.tv_name.setText(lvChatMsg.getName());
        holder.tv_content.setText(lvChatMsg.getContent());

        if (getItemViewType(position) == 2 || getItemViewType(position) == 3) {
            /**
             * 点击事件不能放在convertView==null的情况下的方法体
             */
            holder.iv_mapInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "你点击的是地图信息！！！" + position, Toast.LENGTH_SHORT).show();
                    //解析字符串为地图信息
                    String content = mDatas.get(position).getContent();
                    analysisLocation(content);
                }
            });
        }

        //获取图片，并且进行适当的缩放
        if (getItemViewType(position) == 5 || getItemViewType(position) == 4) {
            String fileName = lvChatMsg.getContent();//图片的文件名
            String url = "http://" + mIp + "/MapLocal/upload/" + fileName;
            final ViewHolder finalHolder = holder;
            OkHttpUtils
                    .get()
                    .url(url)
                    .build()
                    .execute(new BitmapCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            Toast.makeText(context, "网络连接错误！", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(final Bitmap bitmap) {
                            //耗时操作，要在子线程中完成
                            new Thread() {
                                @Override
                                public void run() {
                                    super.run();

                                    final Bitmap baseBitmap = bitmap;
                                    //压缩质量
//                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                            bitmap.compress(Bitmap.CompressFormat.JPEG,0, baos);//PNG无法压缩
//                            Log.e("System.out", baos.toByteArray().length+"");
//                            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

                                    if (baseBitmap != null) {


                                    //缩小大小
                                    //获取100dp对应的px
                                    int px = DensityUtil.dip2px(context, 100);
                                    int px2 = DensityUtil.dip2px(context, 200);
                                    Log.e("System.out", px + "," + px2);
                                    //获取这个图片的宽和高
                                    float width = baseBitmap.getWidth();
                                    float height = baseBitmap.getHeight();
                                    //创建操作图片用的Matrix对象
                                    Matrix matrix = new Matrix();
                                    float scale = (float) 1.0;//默认不缩放
                                    if (width > px) {
                                        //设置缩放率
                                        scale = (float) px / width;
                                    }
                                    //缩放图片的动作
                                    matrix.postScale(scale, scale);


                                    final Bitmap compressBitmap = Bitmap.createBitmap(baseBitmap, 0, 0, (int) width, (int) height, matrix, true);
                                    Bitmap enlargeBitmap = baseBitmap;
                                    if (width > 2 * px) {
                                        scale = 2 * px / width;
                                        matrix = new Matrix();
                                        matrix.postScale(scale, scale);
                                        enlargeBitmap = Bitmap.createBitmap(baseBitmap, 0, 0, (int) width, (int) height, matrix, true);
                                    }
                                    Log.e("System.out", width + "");
//                            Bitmap compressBitmap = BitmapFactory.decodeStream(bais);
                                    final Bitmap finalEnlargeBitmap = enlargeBitmap;
                                    handler.post(
                                            new Runnable() {
                                                @Override
                                                public void run() {
                                                    final boolean[] flag = {true};
                                                    finalHolder.iv_mapInfo.setImageBitmap(compressBitmap);
                                                    finalHolder.iv_mapInfo.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            if (flag[0]) {
                                                                flag[0] = false;
                                                                finalHolder.iv_mapInfo.setImageBitmap(finalEnlargeBitmap);
                                                            } else {
                                                                flag[0] = true;
                                                                finalHolder.iv_mapInfo.setImageBitmap(compressBitmap);
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                    );

                                    //TODO 保存图片
                                }}
                            }.start();
                        }
                    });
        }
        return convertView;
    }

    //解析字符串为地图信息
    private ArrayList<LocationInfo> analysisLocation(String content) {
        locationInfoList = new ArrayList<LocationInfo>();

        String[] split = content.split("_");
        String uid = split[0].replace("uid=", "");
        String lonLat = split[1].replace("p=", "");
        String id = split[2].replace("id=", "");
        String type = split[3].replace("type=", "");
        int itype = Integer.parseInt(type);

        String[] uids = uid.split(",");//获取到uid的数组
        String[] lonLats = lonLat.split(",");

        if (itype == 0) {//位置信息
            for (int i = 0; i < (lonLats.length / 2); i++) {
                String blon = lonLats[2 * i];
                String blat = lonLats[2 * i + 1];
                double lon = Double.parseDouble(blon.substring(blon.indexOf(":") + 1));
                double lat = Double.parseDouble(blat.substring(blat.indexOf(":") + 1, blat.indexOf("}")));

                LocationInfo locatonInfo = new LocationInfo(lat, lon, "");
                locationInfoList.add(locatonInfo);
            }

            tiaozhuan();
        } else if (itype == 1) {//地图目标/组员
            for (int i = 0; i < uids.length; i++) {
                String blon = lonLats[2 * i];
                String blat = lonLats[2 * i + 1];
                double lon = Double.parseDouble(blon.substring(blon.indexOf(":") + 1));
                double lat = Double.parseDouble(blat.substring(blat.indexOf(":") + 1, blat.indexOf("}")));

                LocationInfo locatonInfo = new LocationInfo(lat, lon, "");
                locationInfoList.add(locatonInfo);
            }

            if (!id.equals("null")) {//如果有目标
                getObjectsInfo(id);
            }
        } else if (itype == 2) {//TODO 实时地图
            telNameArrayList = new ArrayList<TelName>();
            if (!uid.equals("null")) {
                getTeamUserInfo(uid, id);
            }
        }


        return locationInfoList;
    }

    //获取组员的信息
    private void getTeamUserInfo(String uid, final String id) {
        String url = "http://" + mIp + "/MapLocal/android/getGpsByuid";
        OkHttpUtils
                .post()
                .url(url)
                .addParams("uid", uid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Toast.makeText(context, "网络连接错误！", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String s) {
                        Gson gson = new Gson();
                        TeamUserRoot teamUserRoot = gson.fromJson(s, TeamUserRoot.class);
                        if (teamUserRoot.getRet() == 0) {
                            List<TeamUserData> data = teamUserRoot.getData();
                            for (TeamUserData teamUserData : data
                                    ) {
                                String tel = teamUserData.getTel();
                                String name = teamUserData.getName();

                                TelName telName = new TelName(tel, name);
                                telNameArrayList.add(telName);
                            }
                        }

                        getObjectsInfo2(id);
                    }
                });
    }

    private void getObjectsInfo2(String id) {
        String url = "http://" + mIp + "/MapLocal/android/getGpsByid";
        OkHttpUtils
                .post()
                .url(url)
                .addParams("id", id)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Toast.makeText(context, "网络连接错误！", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String s) {
                        Gson gson = new Gson();
                        ObjectRoot objectRoot = gson.fromJson(s, ObjectRoot.class);
                        if (objectRoot.getRet() == 0) {
                            List<ObjectData> data = objectRoot.getData();
                            for (ObjectData objectData : data
                                    ) {
                                String name = objectData.getName();
                                String tel = objectData.getTel();

                                TelName telName = new TelName(tel, name);
                                telNameArrayList.add(telName);
                            }
                        }
                        Intent intent = new Intent(context, ChatTrueTimeBDMapActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("telName", telNameArrayList);
                        intent.putExtras(bundle);
                        context.startActivity(intent);

                    }
                });
    }

    //获取目标人的信息
    private void getObjectsInfo(String id) {
        String url = "http://" + mIp + "/MapLocal/android/getGpsByid";
        OkHttpUtils
                .post()
                .url(url)
                .addParams("id", id)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Toast.makeText(context, "网络连接错误！", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String s) {
                        Gson gson = new Gson();
                        ObjectRoot objectRoot = gson.fromJson(s, ObjectRoot.class);
                        if (objectRoot.getRet() == 0) {
                            List<ObjectData> data = objectRoot.getData();
                            for (ObjectData objectData : data
                                    ) {
                                Double lat = Double.valueOf(objectData.getLat());
                                Double lon = Double.valueOf(objectData.getLon());
                                String tips = objectData.getName();
                                int type = LocationInfo.OBJECT;

                                LocationInfo locationInfo = new LocationInfo(lat, lon, tips, type);
                                Log.e("System.out", "之前" + locationInfoList.size() + "");
                                locationInfoList.add(locationInfo);

                                Log.e("System.out", "之后" + locationInfoList.size() + "");

                                tiaozhuan();
                            }
                        }
                    }
                });
    }

    private void tiaozhuan() {
        //获取完数据之后再跳转
        Intent intent = new Intent(context, ChatBDMapActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("locationinfolist", locationInfoList);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    private class ViewHolder {
        public ImageView iv_icon;
        public TextView tv_date;
        public TextView tv_name;
        public TextView tv_content;
        public ImageView iv_mapInfo;
    }
}
