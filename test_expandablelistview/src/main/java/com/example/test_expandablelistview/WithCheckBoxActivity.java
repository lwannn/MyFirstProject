package com.example.test_expandablelistview;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ExpandableListView;

import com.example.test_expandablelistview.adapter.WithCheckBoxExpandableListAdapter;

import java.util.ArrayList;

public class WithCheckBoxActivity extends AppCompatActivity {

    private ExpandableListView mExpandableListView;
    private WithCheckBoxExpandableListAdapter mAdapter;

    private ArrayList<String> groupType;
    private ArrayList<ArrayList<String>> items;
    private ArrayList<String> itemTeam;
    private ArrayList<String> itemObject;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_check_box);
        context = this;

        initView();
        initData();
        initEvent();
    }

    //初始化视图
    private void initView() {
        mExpandableListView = (ExpandableListView) findViewById(R.id.elv);
    }

    //初始化数据
    private void initData() {
        groupType = new ArrayList<String>();
        groupType.add("重案组");
        groupType.add("目标人");

        items = new ArrayList<ArrayList<String>>();
        itemTeam = new ArrayList<String>();
        itemTeam.add("罗旺");
        itemTeam.add("陈涛");
        items.add(itemTeam);
        itemObject = new ArrayList<String>();
        itemObject.add("刘盛奎");
        itemObject.add("卢志威");
        items.add(itemObject);
    }

    //初始化事件
    private void initEvent() {
        mAdapter = new WithCheckBoxExpandableListAdapter(context, groupType, items);
        mExpandableListView.setAdapter(mAdapter);
    }
}
