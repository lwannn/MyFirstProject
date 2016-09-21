package com.ytj.project_login;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.ytj.project_login.adapter.MyBaseExpandableListAdapter;
import com.ytj.project_login.utils.SharePreferencesUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.Call;

/**
 * 显示user的详细信息（1.所在组 2.所参与的案件）
 */
public class DetailActivity extends Activity {
    private ArrayList<String> groupType;
    private ArrayList<ArrayList<String>> items;
    private ArrayList<String> itemTeam;
    private ArrayList<String> itemCase;

    private ExpandableListView mExpandableListView;
    private MyBaseExpandableListAdapter mAdapter;
    private Context context;

    private String mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        context = this;

        initData();
        initView();
        initEvent();
    }

    //初始化数据
    private void initData() {
        mUsername = getIntent().getStringExtra("username");

        groupType = new ArrayList<String>();
        groupType.add(0, "所在组");
        groupType.add(1, "所参与案件");

        items = new ArrayList<ArrayList<String>>();
        itemTeam = new ArrayList<String>();
        itemTeam.add(0, "重案组");
        items.add(0, itemTeam);
        getTeamInfo();
        itemCase = new ArrayList<String>();
        itemCase.add(0, "杀人案");
        itemCase.add(1, "放火案");
        items.add(1, itemCase);

    }

    //初始化view
    private void initView() {
        mExpandableListView = (ExpandableListView) findViewById(R.id.elv);
    }

    //初始化事件
    private void initEvent() {
        mAdapter = new MyBaseExpandableListAdapter(context, groupType, items);
        mExpandableListView.setAdapter(mAdapter);

        //给子列表添加点击事件
        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Toast.makeText(context, "你点击了" + items.get(groupPosition).get(childPosition), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        //将mExpandableListView展开
        mExpandableListView.expandGroup(0);
        mExpandableListView.expandGroup(1);
    }

    //获取所在组的信息
    private void getTeamInfo() {
        //获取服务器的地址
        String ip = (String) SharePreferencesUtil.getParam(context, "ip", "null");
        //获取checkId
        String checkId = (String) SharePreferencesUtil.getParam(context, "checkId", "0");

        String url = "http://" + ip+ "/MapLocal/android/queryUserByUsername";

        OkHttpUtils
                .post()
                .url(url)
                .addParams("username", mUsername)
                .addParams("check", checkId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Toast.makeText(context, "网络连接错误！", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String s) {
//                        itemTeam.remove(0);
//                        itemTeam.add(0, "侦查组");
//                        mAdapter.notifyDataSetChanged();//更新mExpandableListView

                        try {
                            Map result=analysisJson(s);
                            String ret= (String) result.get("ret");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

    //解析json数据
    private Map analysisJson(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);

        Map result = new HashMap();
        Iterator iterator = jsonObject.keys();
        String key = null;
        String value = null;

        while (iterator.hasNext()) {

            key = (String) iterator.next();
            value = jsonObject.getString(key);
            result.put(key, value);

        }
        return result;
    }
}
