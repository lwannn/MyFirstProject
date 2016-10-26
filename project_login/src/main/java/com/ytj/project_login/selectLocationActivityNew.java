package com.ytj.project_login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ytj.project_login.adapter.WithCheckBoxExpandableListAdapterNew;
import com.ytj.project_login.db.dao.DBDao;
import com.ytj.project_login.entity.IdTeamName;
import com.ytj.project_login.entity.TelName;
import com.ytj.project_login.jsonEntity.Objects;
import com.ytj.project_login.jsonEntity.SpyteamRoot;
import com.ytj.project_login.jsonEntity.TeamUserMore;
import com.ytj.project_login.utils.SharePreferencesUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * 选择要显示地图信息的人
 */
public class selectLocationActivityNew {

    private View view;

//    private TextView mTitle;
    private ExpandableListView mExpandableListView;
    private WithCheckBoxExpandableListAdapterNew mAdapter;
    private Context context;

    private int caseId;
    private String caseName;
    private List<IdTeamName> idTeamNameList;

    private ArrayList<String> groupType = new ArrayList<String>();
    private ArrayList<ArrayList<TelName>> items = new ArrayList<ArrayList<TelName>>();
    private ArrayList<TelName> itemObject;
    private String mIp;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mAdapter == null) {
                mAdapter = new WithCheckBoxExpandableListAdapterNew(context, groupType, items);
                mAdapter.setUpTeamBDMap((WithCheckBoxExpandableListAdapterNew.UpTeamBDMap) context);
                mExpandableListView.setAdapter(mAdapter);
            } else {
                mAdapter.notifyDataSetChanged();
            }
        }
    };




    public selectLocationActivityNew(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.activity_select_location, null, false);
        this.context = context;
        initView();
    }

    private void initView() {
//        mTitle = (TextView) view.findViewById(R.id.tv_title);
        mExpandableListView = (ExpandableListView) view.findViewById(R.id.elv);
    }

    //对caseInfo数据进行操作,需要在initData之前调用
    void setCaseInfo(int caseid, String casename,List<IdTeamName> idTeamNameList) {
        caseId = caseid;
        caseName = casename;
        this.idTeamNameList = idTeamNameList;
        initData();
    }

    //初始化数据
    private void initData() {
        mIp = (String) SharePreferencesUtil.getParam(context, SharePreferencesUtil.IP, "1111");
        //获取从caseInfo获取的数据

        TelName telName = new TelName("13333333333", "www");
        for (int i = 0; i < idTeamNameList.size(); i++) {
            groupType.add(i, idTeamNameList.get(i).getTeamName());
            ArrayList<TelName> itemTeam = new ArrayList<TelName>();
            itemTeam.add(telName);
            items.add(itemTeam);
            getTeamInfo(idTeamNameList.get(i).getId(), itemTeam);
        }

        groupType.add("目标人物");

        itemObject = new ArrayList<TelName>();
        itemObject.add(telName);
        items.add(itemObject);

        //获取目标人的相关信息
        getObjectInfo();
    }

    //根据组id获取组的人员的相关信息
    private void getTeamInfo(int id, final ArrayList<TelName> itemTeam) {
        String url = "http://" + mIp + "/MapLocal/android/getDeptList";
        OkHttpUtils
                .post()
                .url(url)
                .addParams("deptid", id + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Toast.makeText(context, "网络连接错误！", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(final String s) {
                        itemTeam.clear();
                        Gson gson = new Gson();
                        SpyteamRoot spyteamRoot = gson.fromJson(s, SpyteamRoot.class);
                        List<TeamUserMore> TeamUserList = spyteamRoot.getDat();
                        for (TeamUserMore teamUserMore : TeamUserList) {
                            String tel = teamUserMore.getTel();
                            String name = teamUserMore.getAlias();
                            TelName telname = new TelName(tel, name);
                            itemTeam.add(telname);
                        }
                        mHandler.sendEmptyMessage(0);
                    }
                });
    }

    //获取目标人的相关信息
    private void getObjectInfo() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                DBDao dbDao = new DBDao(context);
                List<Objects> objects = dbDao.getObjectsByCaseId(caseId);
                itemObject.clear();
                for (Objects object : objects) {
                    String tel = object.getTel();
                    String name = object.getName();
                    TelName telname = new TelName(tel, name);
                    itemObject.add(telname);
                }
            }
        }.start();
    }


    public ArrayList<ArrayList<TelName>> getItems(){
        return items;
    }

    View getView() {
        return view;
    }

}
