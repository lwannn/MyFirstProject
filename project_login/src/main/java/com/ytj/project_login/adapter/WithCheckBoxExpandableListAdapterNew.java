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
public class WithCheckBoxExpandableListAdapterNew extends BaseExpandableListAdapter {
    private ArrayList<String> groupType;
    private ArrayList<ArrayList<TelName>> items;
    private LayoutInflater mInflater;

    UpTeamBDMap upTeamBDMap;
    public interface UpTeamBDMap {
        void addItemAndNotifyMap(int parent, int child_id, boolean is_all);
        void removeItemAndNotifyMsp(int parent, int child_id, boolean is_all);
    }
    public void setUpTeamBDMap(UpTeamBDMap upTeamBDMap) {
        this.upTeamBDMap = upTeamBDMap;
    }

    public WithCheckBoxExpandableListAdapterNew(Context context, ArrayList<String> groupType, ArrayList<ArrayList<TelName>> items) {
        this.groupType = groupType;
        this.items = items;
        mInflater = LayoutInflater.from(context);

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
                if (isChecked) {
                    upTeamBDMap.addItemAndNotifyMap(groupPosition, 0, true);
                } else {
                    upTeamBDMap.removeItemAndNotifyMsp(groupPosition, 0, true);
                }
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
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

        childHolder.mChild.setText(items.get(groupPosition).get(childPosition).getName());
        childHolder.mChildIsSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    upTeamBDMap.addItemAndNotifyMap(groupPosition, childPosition, false);
                } else {
                    upTeamBDMap.removeItemAndNotifyMsp(groupPosition, childPosition, false);
                }
            }
        });
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
