package com.ytj.project_login.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ytj.project_login.R;
import com.ytj.project_login.entity.LvChatMsg;

import java.util.List;

/**
 * Created by Administrator on 2016/9/26.
 */
public class ChatMsgAdapter extends BaseAdapter{
    private List<LvChatMsg> mDatas;
    private LayoutInflater mInflater;

    public ChatMsgAdapter(Context context,List<LvChatMsg> mDatas) {
        this.mDatas = mDatas;
        mInflater=LayoutInflater.from(context);
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
        if(mDatas.get(position).getType()== LvChatMsg.Type.INCOMING){
            return 0;
        }
        return 1;
    }

    //返回布局样式的总类
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            holder=new ViewHolder();
            if(getItemViewType(position)==0){
                convertView=mInflater.inflate(R.layout.item_from_msg,parent,false);
                holder.tv_content= (TextView) convertView.findViewById(R.id.tv_fromMsg);
            }else{
                convertView=mInflater.inflate(R.layout.item_to_msg,parent,false);
                holder.tv_content= (TextView) convertView.findViewById(R.id.tv_toMsg);
            }
            holder.tv_date= (TextView) convertView.findViewById(R.id.tv_date);
            holder.tv_name= (TextView) convertView.findViewById(R.id.tv_name);
            holder.iv_icon= (ImageView) convertView.findViewById(R.id.iv_icon);

            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }

        LvChatMsg lvChatMsg =mDatas.get(position);
        holder.tv_date.setText(lvChatMsg.getIntime());
        holder.tv_name.setText(lvChatMsg.getName());
        holder.tv_content.setText(lvChatMsg.getContent());
        return convertView;
    }

    private class ViewHolder{
        public ImageView iv_icon;
        public TextView tv_date;
        public TextView tv_name;
        public TextView tv_content;
    }
}
