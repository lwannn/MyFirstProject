package com.ytj.project_login.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ytj.project_login.ChatBDMapActivity;
import com.ytj.project_login.R;
import com.ytj.project_login.entity.LocationInfo;
import com.ytj.project_login.entity.LvChatMsg;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/26.
 */
public class ChatMsgAdapter extends BaseAdapter {
    private List<LvChatMsg> mDatas;
    private LayoutInflater mInflater;
    private Context context;

    public ChatMsgAdapter(Context context, List<LvChatMsg> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
        mInflater = LayoutInflater.from(context);
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
        }

        return 2;
    }

    //返回布局样式的总类
    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
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

//                holder.tv_content.setTextColor(Color.BLUE);
//                holder.tv_content.setClickable(true);
//                holder.tv_content.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Toast.makeText(context, "你点击的是地图信息！！！", Toast.LENGTH_SHORT).show();
//                    }
//                });
                //TODO 如果显示图片，在点击输入框的时候，程序会down掉
                holder.iv_mapInfo = (ImageView) convertView.findViewById(R.id.iv_mapInfo);

                holder.tv_content.setVisibility(View.GONE);
                holder.iv_mapInfo.setVisibility(View.VISIBLE);
                holder.iv_mapInfo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "你点击的是地图信息！！！", Toast.LENGTH_SHORT).show();
                        //解析字符串为地图信息
                        String content = mDatas.get(position).getContent();
                        ArrayList<LocationInfo> locationInfos = analysisLocation(content);

                        Intent intent = new Intent(context, ChatBDMapActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("locationinfolist", locationInfos);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                });
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
        return convertView;
    }

    //解析字符串为地图信息
    private ArrayList<LocationInfo> analysisLocation(String content) {
        ArrayList<LocationInfo> locationInfoList = new ArrayList<LocationInfo>();

        String[] split = content.split("_");
        String uid = split[0].replace("uid=", "");
        String lonLat = split[1].replace("p=", "");
        String type = null;
        String id = null;
        if (split.length == 3) {
            type = split[2].replace("type=", "");
        } else if (split.length == 4) {
            id = split[2].replace("id=", "");
            type = split[3].replace("type=", "");
        }

        String[] uids = uid.split(",");//获取到uid的数组
        String[] lonLats = lonLat.split(",");

        for (int i = 0; i < uids.length; i++) {
            String blon = lonLats[2 * i];
            String blat = lonLats[2 * i + 1];
            double lon = Double.parseDouble(blon.substring(blon.indexOf(":") + 1));
            double lat = Double.parseDouble(blat.substring(blat.indexOf(":") + 1, blat.indexOf("}")));

            LocationInfo locatonInfo = new LocationInfo(lat, lon, "");
            locationInfoList.add(locatonInfo);
        }

        return locationInfoList;
    }

    private class ViewHolder {
        public ImageView iv_icon;
        public TextView tv_date;
        public TextView tv_name;
        public TextView tv_content;
        public ImageView iv_mapInfo;
    }
}
