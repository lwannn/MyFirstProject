package com.ytj.project_login.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ytj.project_login.R;
import com.ytj.project_login.utils.ConstantUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;

/**
 * ExpandableListView的适配器
 * Created by Administrator on 2016/9/20.
 */
public class MyBaseExpandableListAdapter extends BaseExpandableListAdapter{
    private Context context;
    public static JSONArray depts;
    public static Map<String,String> user_map;

    private ArrayList<String> groupType;
    private ArrayList<ArrayList<String>> items;
    private LayoutInflater inflater;

    public MyBaseExpandableListAdapter(Context context, ArrayList<String> groupType, ArrayList<ArrayList<String>> items) {

        this.context = context;
        this.depts = depts;
        this.user_map = user_map;
        this.groupType = groupType;
        this.items = items;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getGroupCount() {
        return groupType.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return items.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupType.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return items.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final ViewHolderGroup groupHolder;

        final boolean ACTION_STATE = true;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.elv_group,parent,false);
            groupHolder=new ViewHolderGroup();
            groupHolder.lay_line = (LinearLayout) convertView.findViewById(R.id.lay_line);
            groupHolder.image_group = (ImageView) convertView.findViewById(R.id.image_group);
            groupHolder.tv_group= (TextView) convertView.findViewById(R.id.tv_group);
            convertView.setTag(groupHolder);
        }else{
            groupHolder = (ViewHolderGroup) convertView.getTag();
        }
        if(isExpanded){
            groupHolder.image_group.setSelected(false);
        }else {
            groupHolder.image_group.setSelected(true);
        }
        groupHolder.tv_group.setText(groupType.get(groupPosition));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolderChild childHolder;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.elv_item,parent,false);
            childHolder=new ViewHolderChild();
            childHolder.tv_child= (TextView) convertView.findViewById(R.id.tv_item);
            childHolder.msgNum = (TextView) convertView.findViewById(R.id.msgNum);
            convertView.setTag(childHolder);
        }else {
            childHolder= (ViewHolderChild) convertView.getTag();
        }
        String data = items.get(groupPosition).get(childPosition);
        String datas[] = data.split("::");
        //根据分组信息进行不同展示
        try {
        switch (groupPosition) {
            case 0://案件组
                childHolder.tv_child.setText(datas[0]);
                if (depts != null && depts.length() != 0) {
                    childHolder.msgNum.setVisibility(View.VISIBLE);
                    childHolder.msgNum.setText(depts.getJSONObject(0).getString("msgNum"));
                } else {
                    childHolder.msgNum.setVisibility(View.INVISIBLE);
                }
                break;
            case 1://案件
                childHolder.tv_child.setText(datas[0]);
                childHolder.msgNum.setVisibility(View.INVISIBLE);
                break;
            case 2://组员
                childHolder.tv_child.setText(datas[0]);
                if (user_map != null) {
                    if (user_map.get(datas[1]) == null) {
                        childHolder.msgNum.setVisibility(View.INVISIBLE);
                    }else {
                        childHolder.msgNum.setVisibility(View.VISIBLE);
                        childHolder.msgNum.setText(user_map.get(datas[1]));
                    }
                }
                break;
        }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private class ViewHolderGroup{
        public LinearLayout lay_line;
        public ImageView image_group;
        public TextView tv_group;
    }

    private class ViewHolderChild{
        public TextView tv_child;
        public TextView msgNum;
    }
}
