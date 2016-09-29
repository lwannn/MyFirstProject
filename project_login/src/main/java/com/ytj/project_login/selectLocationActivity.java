package com.ytj.project_login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.ytj.project_login.entity.IdTeamName;

import java.util.List;

/**
 * 选择要显示地图信息的人
 */
public class selectLocationActivity extends Activity {

    private TextView mTitle;
    private ExpandableListView mExpandableListView;

    private int caseId;
    private String caseName;
    private List<IdTeamName> idTeamNameList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);
        initView();
        initData();
        initEvent();
    }

    //初始化View
    private void initView() {
        mTitle= (TextView) findViewById(R.id.tv_title);
        mExpandableListView= (ExpandableListView) findViewById(R.id.elv);
    }

    //初始化数据
    private void initData() {
        Intent intent=getIntent();
        Bundle bundle = intent.getExtras();
        caseId=bundle.getInt("caseid");
        caseName=bundle.getString("casename");
        idTeamNameList= (List<IdTeamName>) bundle.getSerializable("idcasename");
    }

    //初始化事件
    private void initEvent() {
        mTitle.setText(caseName+"案件相关人员");
    }

}
