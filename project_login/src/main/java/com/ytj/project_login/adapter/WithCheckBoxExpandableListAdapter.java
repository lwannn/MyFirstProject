package com.ytj.project_login.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.ytj.project_login.R;
import com.ytj.project_login.entity.TelName;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/9/30.
 */
public class WithCheckBoxExpandableListAdapter extends BaseExpandableListAdapter {
    private ArrayList<String> groupType;
    private ArrayList<ArrayList<TelName>> items;
    private LayoutInflater mInflater;
    public static ArrayList<HashMap<Integer, Boolean>> isSelected;//用来保存checkBox的选择状态

    public WithCheckBoxExpandableListAdapter(Context context, ArrayList<String> groupType, ArrayList<ArrayList<TelName>> items) {
        this.groupType = groupType;
        this.items = items;
        mInflater = LayoutInflater.from(context);

        isSelected = new ArrayList<HashMap<Integer, Boolean>>();
        initData();
    }

    //用来初始化数据(先都设置为全都不选中的状态)
    private void initData() {
        for (int i = 0; i < groupType.size(); i++) {
            HashMap<Integer, Boolean> childIsSelected = new HashMap<Integer, Boolean>();
            for (int j = 0; j < items.get(i).size(); j++) {
                childIsSelected.put(j, false);
            }
            isSelected.add(i, childIsSelected);
        }
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
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ParentViewHolder parentHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.check_group_layout, parent, false);
            parentHolder = new ParentViewHolder();
            parentHolder.mGroupType = (TextView) convertView.findViewById(R.id.tv_group);
            parentHolder.mParentIsSelected = (CheckBox) convertView.findViewById(R.id.cb_parentIsSelected);

            convertView.setTag(parentHolder);
        } else {
            parentHolder = (ParentViewHolder) convertView.getTag();
        }
        parentHolder.mGroupType.setText(groupType.get(groupPosition));
        parentHolder.mParentIsSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                HashMap<Integer, Boolean> childIsSelected = isSelected.get(groupPosition);
                if (isChecked) {
                    for (int i = 0; i < childIsSelected.size(); i++) {
                        childIsSelected.put(i, true);
                    }
                } else {
                    for (int i = 0; i < childIsSelected.size(); i++) {
                        childIsSelected.put(i, false);
                    }
                }

                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.check_item_layout, parent, false);
            childHolder = new ChildViewHolder();
            childHolder.mChildIsSelected = (CheckBox) convertView.findViewById(R.id.cb_isSelected);
            childHolder.mChild = (TextView) convertView.findViewById(R.id.tv_child);

            convertView.setTag(childHolder);
        } else {
            childHolder = (ChildViewHolder) convertView.getTag();
        }

        final HashMap<Integer, Boolean> childIsSelected = isSelected.get(groupPosition);
        childHolder.mChild.setText(items.get(groupPosition).get(childPosition).getName());
        childHolder.mChildIsSelected.setOnClickListener(new View.OnClickListener() {// 监听checkBox并根据原来的状态来设置新的状态
            @Override
            public void onClick(View v) {
                if (childIsSelected.get(childPosition)) {
                    childIsSelected.put(childPosition, false);
                } else {
                    childIsSelected.put(childPosition, true);
                }
                notifyDataSetChanged();
            }
        });
            childHolder.mChildIsSelected.setChecked(childIsSelected.get(childPosition));
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private class ParentViewHolder {
        public CheckBox mParentIsSelected;
        public TextView mGroupType;
    }

    private class ChildViewHolder {
        public CheckBox mChildIsSelected;
        public TextView mChild;
    }
}
