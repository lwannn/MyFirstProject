package com.ytj.project_login.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ytj.project_login.DetailActivity;
import com.ytj.project_login.PersonalChatActivity;
import com.ytj.project_login.R;
import com.ytj.project_login.TeamChatActivity;
import com.ytj.project_login.entity.ItemTeam;
import com.ytj.project_login.jsonEntity.RecentMsg;
import com.ytj.project_login.utils.SharePreferencesUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/11/15.
 */

public class DetailListAdapter extends BaseAdapter {
    private Context mContext;
    private List<ItemTeam> mDatas;
    private LayoutInflater mInflater;
    private String mIp;

    public static JSONArray depts;
    public static Map<String, String> user_map;
    public static String jsonString = "";

    public DetailListAdapter(Context mContext, List<ItemTeam> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        mInflater = LayoutInflater.from(mContext);
        mIp = (String) SharePreferencesUtil.getParam(mContext, SharePreferencesUtil.IP, "111");
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

    @Override
    public int getItemViewType(int position) {
        ItemTeam.Type type = mDatas.get(position).getType();
        if (type == ItemTeam.Type.GROUP) {
            return 0;
        }
        return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            if (getItemViewType(position) == 0) {
                convertView = mInflater.inflate(R.layout.elv_group, new FrameLayout(mContext), true);
            } else if (getItemViewType(position) == 1) {
                convertView = mInflater.inflate(R.layout.elv_item, new FrameLayout(mContext), true);
                holder.mDetails = (TextView) convertView.findViewById(R.id.tv_detail);
            }
            holder.mIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
            holder.mTitle = (TextView) convertView.findViewById(R.id.tv_item);
            holder.mDate = (TextView) convertView.findViewById(R.id.tv_date);
            holder.msgNum = (TextView) convertView.findViewById(R.id.msgNum);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ItemTeam itemTeam = mDatas.get(position);
        String tel = itemTeam.getTel();
        //用Glide来显示图片
        String imgUrl = "http://" + mIp + "/MapLocal/upload/" + itemTeam.getPicPath();
        if (getItemViewType(position) == 1)
            Glide.with(mContext)
                    .load("http://pic55.nipic.com/file/20141208/19462408_171130083000_2.jpg")
                    .placeholder(R.drawable.icon)
                    .error(R.drawable.icon)
                    .centerCrop()
                    .into(holder.mIcon);
        holder.mTitle.setText(itemTeam.getAlias());
        //TODO　用接口获取最新的信息（来显示detail和date信息）
        if (holder.mDetails != null)
            holder.mDetails.setText(itemTeam.getTel());
//        updateMsgDate(holder.mDetails, holder.mDate, position);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getItemViewType(position) == 0) {
                    Intent intent = new Intent(mContext, TeamChatActivity.class);
                    intent.putExtra("id", itemTeam.getId());
                    intent.putExtra("Chatname", itemTeam.getAlias());
                    mContext.startActivity(intent);
                } else if (getItemViewType(position) == 1) {
                    Intent intent = new Intent(mContext, PersonalChatActivity.class);
                    intent.putExtra("id", itemTeam.getId());
                    intent.putExtra("Chatname", itemTeam.getAlias());
                    intent.putExtra("tel", itemTeam.getTel());
                    mContext.startActivity(intent);
                }
            }
        });

        if (getItemViewType(position) == 0) {
            if (depts != null && depts.length() != 0) {
                holder.msgNum.setVisibility(View.VISIBLE);
                try {
                    holder.msgNum.setText(depts.getJSONObject(0).getString("msgNum"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                holder.msgNum.setVisibility(View.INVISIBLE);
            }
        } else if (getItemViewType(position) == 1) {
            if (user_map != null) {
                if (user_map.get(itemTeam.getId() + "") == null) {
                    holder.msgNum.setVisibility(View.INVISIBLE);
                } else {
                    holder.msgNum.setVisibility(View.VISIBLE);
                    holder.msgNum.setText(user_map.get(itemTeam.getId() + ""));
                }
            }
        }
        return convertView;
    }

    //更新日期和最新的消息
    //TODO 不知道是不是因为线程太多了，导致很卡
    private void updateMsgDate(final TextView mDetails, final TextView mDate, int position) {
        String url = "http://" + mIp + "/MapLocal/android/getChatByNum";
        OkHttpUtils
                .post()
                .url(url)
                .addParams("fromnum", DetailActivity.MINE_ID + "")
                .addParams("tonum", mDatas.get(position).getId() + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Toast.makeText(mContext, "网络连接错误！", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(String s) {
                        JsonParser parser = new JsonParser();
                        JsonObject jsonObj = parser.parse(s).getAsJsonObject();
                        int ret = jsonObj.get("ret").getAsInt();
                        if (ret == 0) {//等于0，说明获取到了数据
                            JsonObject json = jsonObj.get("data").getAsJsonObject();
                            Gson gson = new Gson();
                            RecentMsg recentMsg = gson.fromJson(json, RecentMsg.class);
                            String detail = recentMsg.getContent();
                            String intime = recentMsg.getIntime().replace("T", " ");
                            String formatTime = "";
                            try {
                                Date parse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(intime);
                                Date date = new Date();
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy MM dd");
                                String parseTime = dateFormat.format(parse);
                                String todayTime = dateFormat.format(date);
                                if (parseTime.equals(todayTime)) {//如果是今天发送的消息
                                    formatTime = new SimpleDateFormat("ahh:mm").format(parse);
                                } else {//之前发送的消息
                                    formatTime = new SimpleDateFormat("MM月dd日").format(parse);
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if (mDetails != null && detail != null) {
                                mDetails.setText(detail);
                            }
                            if (mDate != null && formatTime != null) {
                                mDate.setText(formatTime);
                            }
                        }
                    }
                });
    }

    private class ViewHolder {
        public ImageView mIcon;//图标
        public TextView mTitle;//组员人名
        public TextView mDetails;//最新的聊天信息
        public TextView mDate;//日期
        public TextView msgNum;//未读消息的条数
    }
}
