package com.example.test_expandablelistview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.test_expandablelistview.R;

/**
 * ExpandableListView的适配器
 * Created by Administrator on 2016/9/20.
 */
public class MyBaseExpandableListAdapter extends BaseExpandableListAdapter{
    int[] logos;
    private String[] armTypes;
    private String[][] arms;

    private Context context;
    private LayoutInflater inflater;
    public MyBaseExpandableListAdapter(Context context,int[] logos,String[] armTypes,String[][] arms) {
        this.context=context;
        this.logos=logos;
        this.armTypes=armTypes;
        this.arms=arms;

        this.inflater=LayoutInflater.from(context);
    }

    @Override
    public int getGroupCount() {
        return armTypes.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return arms[groupPosition].length;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return armTypes[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return arms[groupPosition][childPosition];
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    //表示id是唯一的（使getCheckedItemIds方法能够正常获取用户选中的选项的id）
    @Override
    public boolean hasStableIds() {
        return true;
    }

    //取得用于显示给定分组的视图，这个方法仅返回分组的视图对象
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolderGroup holderGroup;
        if (convertView==null){
            convertView= inflater.inflate(R.layout.group_layout,parent,false);
            holderGroup=new ViewHolderGroup();
            holderGroup.iv_icon= (ImageView) convertView.findViewById(R.id.iv_icon);
            holderGroup.tv_group= (TextView) convertView.findViewById(R.id.tv_group);
            convertView.setTag(holderGroup);
        }else{
            holderGroup= (ViewHolderGroup) convertView.getTag();
        }
        holderGroup.iv_icon.setImageResource(logos[groupPosition]);
        holderGroup.tv_group.setText(armTypes[groupPosition]);
        return convertView;
    }

    //取得显示给定分组给定子位置的数据用的视图
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolderItem holderItem;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.item_layout,parent,false);
            holderItem=new ViewHolderItem();
            holderItem.tv_child= (TextView) convertView.findViewById(R.id.tv_child);
            convertView.setTag(holderItem);
        }else{
            holderItem= (ViewHolderItem) convertView.getTag();
        }

        holderItem.tv_child.setText(arms[groupPosition][childPosition]);
        return convertView;
    }

    //设置子列表是否可选中
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private class ViewHolderGroup{
        public ImageView iv_icon;
        public TextView tv_group;
    }

    private class ViewHolderItem{
        public TextView tv_child;
    }
}
