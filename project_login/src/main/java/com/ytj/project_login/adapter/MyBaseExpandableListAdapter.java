package com.ytj.project_login.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.ytj.project_login.R;

import java.util.ArrayList;

/**
 * ExpandableListView的适配器
 * Created by Administrator on 2016/9/20.
 */
public class MyBaseExpandableListAdapter extends BaseExpandableListAdapter{
    private ArrayList<String> groupType;
    private ArrayList<ArrayList<String>> items;
    private LayoutInflater inflater;
    public MyBaseExpandableListAdapter(Context context, ArrayList<String> groupType, ArrayList<ArrayList<String>> items) {
        this.groupType=groupType;
        this.items=items;

        inflater=LayoutInflater.from(context);
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
        ViewHolderGroup groupHolder;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.elv_group,parent,false);
            groupHolder=new ViewHolderGroup();
            groupHolder.tv_group= (TextView) convertView.findViewById(R.id.tv_group);
            convertView.setTag(groupHolder);
        }else{
            groupHolder = (ViewHolderGroup) convertView.getTag();
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
            convertView.setTag(childHolder);
        }else {
            childHolder= (ViewHolderChild) convertView.getTag();
        }

        childHolder.tv_child.setText(items.get(groupPosition).get(childPosition));
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private class ViewHolderGroup{
        public TextView tv_group;
    }

    private class ViewHolderChild{
        public TextView tv_child;
    }
}
