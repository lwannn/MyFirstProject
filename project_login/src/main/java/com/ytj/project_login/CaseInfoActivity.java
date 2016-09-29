package com.ytj.project_login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ytj.project_login.adapter.MyBaseExpandableListAdapter;
import com.ytj.project_login.db.dao.DBDao;
import com.ytj.project_login.entity.IdTeamName;
import com.ytj.project_login.jsonEntity.CaseRoot;
import com.ytj.project_login.jsonEntity.CaseSpyteam;
import com.ytj.project_login.jsonEntity.Cases;
import com.ytj.project_login.jsonEntity.Objects;
import com.ytj.project_login.utils.SharePreferencesUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * 显示案件详情的activity
 */
public class CaseInfoActivity extends Activity {

    private TextView mTitle;
    private ExpandableListView mExpandableListView;
    private TextView mCaseName;
    private TextView mInTime;
    private TextView mRemark;
    private TextView mLinkMan;
    private TextView mLinkTel;
    private TextView mCaseNum;
    private TextView mCaseKind;

    private MyBaseExpandableListAdapter mAdapter;
    private Context context;
    private ArrayList<String> groupType;
    private ArrayList<ArrayList<String>> item;
    private ArrayList<String> itemTeam;
    private ArrayList<String> itemObject;
    private ArrayList<IdTeamName> idTeamNameList = new ArrayList<IdTeamName>();

    private int caseId;
    private String caseName;
    private Cases caseInfo;
    private String mIp;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mCaseName.setText(caseInfo.getName());
            mInTime.setText(caseInfo.getIntime().replace("T", " "));
            mRemark.setText(caseInfo.getRemark());
            mLinkMan.setText(caseInfo.getLinkman());
            mLinkTel.setText(caseInfo.getLinktel());
            mCaseNum.setText(caseInfo.getCasenum());
            mCaseKind.setText(caseInfo.getCasekind());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_info);
        context = this;

        initView();
        initData();
        initEvent();
    }

    //初始化View
    private void initView() {
        mTitle = (TextView) findViewById(R.id.tv_title);
        mExpandableListView = (ExpandableListView) findViewById(R.id.elv);
        mCaseName = (TextView) findViewById(R.id.tv_caseName);
        mInTime = (TextView) findViewById(R.id.tv_intime);
        mRemark = (TextView) findViewById(R.id.tv_remark);
        mLinkMan = (TextView) findViewById(R.id.tv_linkman);
        mLinkTel = (TextView) findViewById(R.id.tv_linktel);
        mCaseNum = (TextView) findViewById(R.id.tv_caseNum);
        mCaseKind = (TextView) findViewById(R.id.tv_caseKind);
    }

    //初始化数据
    private void initData() {
        mIp = (String) SharePreferencesUtil.getParam(context, SharePreferencesUtil.IP, "111");
        Intent intent = getIntent();
        caseId = intent.getIntExtra("caseid", -1);
        caseName = intent.getStringExtra("casename");

        groupType = new ArrayList<String>();
        groupType.add(0, "参与组");
        groupType.add(1, "目标人");

        item = new ArrayList<ArrayList<String>>();

        itemTeam = new ArrayList<String>();
        itemTeam.add(0, "重案组");
        itemTeam.add(1, "测试组");
        item.add(0, itemTeam);

        itemObject = new ArrayList<String>();
        itemObject.add(0, "刘盛奎");
        itemObject.add(1, "卢志威");
        item.add(1, itemObject);

        //获取案件的基本信息
        new Thread() {
            @Override
            public void run() {
                super.run();
                DBDao dbDao = new DBDao(context);
                caseInfo = dbDao.getCaseInfoById(caseId);

                mHandler.sendEmptyMessage(0);
            }
        }.start();

        getCaseInfo();
    }

    //获取案件相关信息
    private void getCaseInfo() {
        String url = "http://" + mIp + "/MapLocal/android/getGroup";
        OkHttpUtils
                .post()
                .url(url)
                .addParams("caseid", caseId + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Toast.makeText(context, "网络连接错误！", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String s) {
                        Gson gson = new Gson();
                        CaseRoot caseRoot = gson.fromJson(s, CaseRoot.class);

                        itemTeam.remove(1);
                        itemTeam.remove(0);
                        itemObject.remove(1);
                        itemObject.remove(0);

                        final List<Objects> objectsList = caseRoot.getObjects();
                        for (int i = 0; i < objectsList.size(); i++) {
                            itemObject.add(i, objectsList.get(i).getName());
                        }
                        List<CaseSpyteam> spyteams = caseRoot.getSpyteam();
                        for (int i = 0; i < spyteams.size(); i++) {
                            CaseSpyteam caseSpyteam = spyteams.get(i);
                            int id = caseSpyteam.getId();
                            String name = caseSpyteam.getName();

                            IdTeamName idTeamName = new IdTeamName(id, name);
                            idTeamNameList.add(idTeamName);
                            itemTeam.add(i, caseSpyteam.getName());
                        }

                        mAdapter.notifyDataSetChanged();

                        //将目标人物保存到数据库中
                        new Thread() {
                            @Override
                            public void run() {
                                super.run();
                                DBDao dbDao = new DBDao(context);
                                for (Objects objects : objectsList
                                        ) {
                                    dbDao.addOrUpadateObjects(objects);
                                }
                            }
                        }.start();
                    }
                });
    }

    //初始化事件
    private void initEvent() {
        mAdapter = new MyBaseExpandableListAdapter(context, groupType, item);
        mExpandableListView.setAdapter(mAdapter);
        mTitle.setText(caseName);
    }

    public void checkLocation(View view) {
//        Toast.makeText(context, "醉了醉了！！！", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, selectLocationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("caseid", caseId);
        bundle.putString("casename",caseName);
        bundle.putSerializable("idcasename", idTeamNameList);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
