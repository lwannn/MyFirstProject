package com.ytj.project_login;


import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ytj.project_login.adapter.DetailListAdapter;
import com.ytj.project_login.db.dao.DBDao;
import com.ytj.project_login.entity.IdCaseName;
import com.ytj.project_login.entity.ItemTeam;
import com.ytj.project_login.jsonEntity.Cases;
import com.ytj.project_login.jsonEntity.Department;
import com.ytj.project_login.jsonEntity.TeamUser;
import com.ytj.project_login.jsonEntity.TeamUsersRoot;
import com.ytj.project_login.jsonEntity.UserRoot;
import com.ytj.project_login.jsonEntity.headPortrait;
import com.ytj.project_login.netUtils.NetService;
import com.ytj.project_login.netUtils.WarnService;
import com.ytj.project_login.utils.ConstantUtil;
import com.ytj.project_login.utils.MapUtil;
import com.ytj.project_login.utils.SharePreferencesUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;

/**
 * 显示user的详细信息（1.所在组 2.所参与的案件）
 */
public class DetailActivity extends Activity {
//    private ArrayList<String> groupType;
//    private ArrayList<ArrayList<String>> items;
//    private ArrayList<String> itemTeam;
//    private ArrayList<String> itemCase;
//    private ArrayList<String> itemWorkMate;

    //    private ExpandableListView mExpandableListView;
    private ListView mListView;
    private CircleImageView mCircleImageView;
    private TextView mTextView;
    private Context context;
    private UserRoot userRoot;
    //    private List<IdName> teamUserNameList = new ArrayList<IdName>();
    private List<IdCaseName> caseNameList = new ArrayList<IdCaseName>();
    private List<ItemTeam> itemTeamList = new ArrayList<ItemTeam>();
    private ItemTeam itemTeam;
    private DetailListAdapter mAdapter;
//    private IdName idName;

    private String mUsername;
    private String mIp;
    private String mCheckId;
    public static int MINE_ID;
    public static String MINE_NAME;
    public static String MINE_TEL;
    Intent netServiceIntent;
    private boolean isStart;
    Timer timer;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    getHeadPortraitBitmap();
                    break;
                case 1:
                    if (mAdapter == null) {
                        mAdapter = new DetailListAdapter(context, itemTeamList);
                        mListView.setAdapter(mAdapter);
                    }
//                    for (int i = 0; i < teamUserNameList.size(); i++) {
//                        itemWorkMate.add(i, teamUserNameList.get(i).getAlias() + "::" + teamUserNameList.get(i).getId());
//                    }
                    break;
                case 2:

                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        context = this;
        netServiceIntent = new Intent(this, NetService.class);
        initData();
        initView();
        initEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isStart = true;
        stopService(netServiceIntent);
    }

    //初始化数据
    private void initData() {
        mUsername = getIntent().getStringExtra("username");
        if (mUsername == null || mUsername.equals("")) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        mIp = (String) SharePreferencesUtil.getParam(context, SharePreferencesUtil.IP, "1111");
        mCheckId = (String) SharePreferencesUtil.getParam(context, SharePreferencesUtil.CHECK_ID, "0");

//        groupType = new ArrayList<String>();
//        groupType.add(0, "所在组");
//        groupType.add(1, "所参与案件");
//        groupType.add(2, "组员");
//
//        items = new ArrayList<ArrayList<String>>();
//        itemTeam = new ArrayList<String>();
//        itemTeam.add(0, "侦查组");
//        items.add(0, itemTeam);
//        itemCase = new ArrayList<String>();
//        itemCase.add(0, "杀人案");
//        itemCase.add(1, "放火案");
//        items.add(1, itemCase);
//        itemWorkMate = new ArrayList<String>();
//        items.add(2, itemWorkMate);

        getHeadPortraitUrl();
        getInfo();
    }

    //获取头像的相对路径，并保存
    private void getHeadPortraitUrl() {
        //获取图像路径
        String imageUrl = "http://" + mIp + "/MapLocal/android/getImage";
        Log.i("DeDEDEDE", imageUrl);
        OkHttpUtils
                .post()
                .url(imageUrl)
                .addParams("username", mUsername)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Toast.makeText(context, "网络连接错误！", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String s) {
                        Gson gson = new Gson();
                        headPortrait headPortrait = gson.fromJson(s, headPortrait.class);
                        String test = headPortrait.getDat();
                        if (headPortrait.getRet() == 1) {
                            Toast.makeText(context, "获取不到资源！", Toast.LENGTH_SHORT).show();
                        } else {
                            //将头像的相对路径存储到sp
                            SharePreferencesUtil.setParam(context, SharePreferencesUtil.HEAD_PORTRAIT_URL, headPortrait.getDat());
                            mHandler.sendEmptyMessage(0);
                        }
                    }

                });
    }

    //获取头像的bitmap并且设置(在图片路径存好的前提下)
    private void getHeadPortraitBitmap() {
        String url = "http://" + mIp + SharePreferencesUtil.getParam(context, SharePreferencesUtil.HEAD_PORTRAIT_URL, "/1111");
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new BitmapCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                    }

                    @Override
                    public void onResponse(Bitmap bitmap) {
                        if (bitmap != null)//在获取的图片不为空的情况下
                            mCircleImageView.setImageBitmap(bitmap);

                        //TODO 将图片保存到本地
                    }
                });
    }

    //初始化view
    private void initView() {
//        mExpandableListView = (ExpandableListView) findViewById(R.id.elv);
        mListView = (ListView) findViewById(R.id.listView);
        mCircleImageView = (CircleImageView) findViewById(R.id.civ);
        mTextView = (TextView) findViewById(R.id.tv_title);
//        mExpandableListView.setGroupIndicator(null);
    }

    //初始化事件
    private void initEvent() {
//        mAdapter = new DetailListAdapter(context, groupType, items);
//        mExpandableListView.setAdapter(mAdapter);
//        //给子列表添加点击事件
//        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//            @Override
//            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//                if (groupPosition == 0) {//如果点击的是所在组，就跳转到聊天窗口
//                    Intent intent = new Intent(context, TeamChatActivity.class);
//                    intent.putExtra("id", userRoot.getDat().getDeptid());
//                    intent.putExtra("Chatname", userRoot.getDepartment().getName());
//                    startActivity(intent);
//                } else if (groupPosition == 1) {//案件详情
//                    Intent intent = new Intent(context, CaseInfoActivity.class);
//                    intent.putExtra("caseid", caseNameList.get(childPosition).getId());
//                    intent.putExtra("casename", caseNameList.get(childPosition).getCaseName());
//                    startActivity(intent);
//                } else if (groupPosition == 2) {//私聊
//                    Intent intent = new Intent(context, PersonalChatActivity.class);
//                    intent.putExtra("id", teamUserNameList.get(childPosition).getId());
//                    intent.putExtra("Chatname", teamUserNameList.get(childPosition).getAlias());
//                    intent.putExtra("tel", teamUserNameList.get(childPosition).getTel());
//                    startActivity(intent);
//                }
//                return true;
//            }
//        });
//        mExpandableListView.expandGroup(2);
        //定义一个循环事件来处理消息
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                while (true) {
                    if (isStart)
                        refershUI();
                }

            }
        }, 5000, 1000);
    }

    //获取所在组和所参与案件的信息
    private void getInfo() {
        String url = "http://" + mIp + "/MapLocal/android/queryUserByUsername";

        OkHttpUtils
                .post()
                .url(url)
                .addParams("username", mUsername)
                .addParams("check", mCheckId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Toast.makeText(context, "网络连接错误！", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String s) {
//                        //将原始数据remove掉
//                        itemTeam.removeAll(itemTeam);
//                        itemCase.removeAll(itemCase);

                        userRoot = analysisJson(s);
                        Department department = userRoot.getDepartment();
                        itemTeam = new ItemTeam(department.getId(), department.getName(), null, null, ItemTeam.Type.GROUP);
                        itemTeamList.add(itemTeam);
//                        itemTeam.add(0, department.getName());
                        List<Cases> cases = userRoot.getCases();
                        for (int i = 0; i < cases.size(); i++) {
//                            itemCase.add(i, cases.get(i).getName());
                            //将caseid和casename放到一个集合中
                            IdCaseName idCaseName = new IdCaseName(cases.get(i).getId(), cases.get(i).getName());
                            caseNameList.add(i, idCaseName);
                        }
                        //将案件id和案件都存到sharepreferences
                        StringBuilder sb_id = new StringBuilder();
                        StringBuilder sb_name = new StringBuilder();
                        for (IdCaseName idCaseName : caseNameList) {
                            sb_id.append(idCaseName.getId() + "::");
                            sb_name.append(idCaseName.getCaseName() + "::");
                        }
                        SharePreferencesUtil.setParam(DetailActivity.this, "caseId", sb_id.toString());
                        SharePreferencesUtil.setParam(DetailActivity.this, "caseName", sb_name.toString());
                        //通过组的id获取组的成员的详细信息（并且保存到数据库中）
                        int deptid = userRoot.getDat().getDeptid();
                        getUsersInfo(deptid);

                        //将该用户的id存到变量中
                        MINE_ID = userRoot.getDat().getId();
                        SharePreferencesUtil.setParam(DetailActivity.this, "userId", Integer.toString(MINE_ID));
                        Intent warnServiceIntent = new Intent(DetailActivity.this, WarnService.class);
                        startService(warnServiceIntent);
                        //将该用户的name存到static变量中
                        MINE_NAME = userRoot.getDat().getAlias();
                        //将该用户的tel存到static变量中
                        MINE_TEL = userRoot.getDat().getTel();

                        //设置title
                        mTextView.setText(userRoot.getDat().getAlias());

                        new Thread() {
                            @Override
                            public void run() {
                                super.run();
                                DBDao dbDao = new DBDao(context);
                                //将案件信息都保存起来
                                List<Cases> cases = userRoot.getCases();
                                for (int i = 0; i < cases.size(); i++) {
                                    dbDao.addOrUpdateCase(cases.get(i));
                                }
                            }
                        }.start();
                    }
                });
    }

    //通过组的id获取组的成员的详细信息（并且保存到数据库中）
    private void getUsersInfo(int deptid) {
        String url = "http://" + mIp + "/MapLocal/android/getDeptList";
        OkHttpUtils
                .post()
                .url(url)
                .addParams("deptid", deptid + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Toast.makeText(context, "网络连接错误！", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(final String s) {
                        new Thread() {
                            @Override
                            public void run() {
                                super.run();
                                //获取组员的相关信息，并将数据添加到数据库中
                                DBDao dbDao = new DBDao(context);
                                Gson gson = new Gson();
                                TeamUsersRoot teamUsersRoot = gson.fromJson(s, TeamUsersRoot.class);
                                List<TeamUser> teamUserList = teamUsersRoot.getDat();
                                for (int i = 0; i < teamUserList.size(); i++) {
                                    TeamUser teamUser = teamUserList.get(i);
                                    int id = teamUser.getId();
                                    String username = teamUser.getUsername();
                                    String alias = teamUser.getAlias();
                                    String tel = teamUser.getTel();
                                    String path = teamUser.getPath();

                                    dbDao.addOrUpdateUser(id, username, alias, tel, path);
                                    //将id对应的名字保存到静态map中
                                    MapUtil.setName(id, alias);

//                                    idName = new IdName(id, alias, tel);
                                    itemTeam = new ItemTeam(id, alias, path, tel, ItemTeam.Type.MEMBER);
                                    if (itemTeam.getId() != MINE_ID) {//不要将自己的名字添加到组员
                                        //将组的name添加到list中
//                                        teamUserNameList.add(idName);
                                        itemTeamList.add(itemTeam);
                                    }
                                }
                                mHandler.sendEmptyMessage(1);
                            }
                        }.start();
                    }
                });
    }

    //解析json数据,转换成root对象
    private UserRoot analysisJson(String jsonString) {
        Gson gson = new Gson();
        UserRoot userRoot = gson.fromJson(jsonString, UserRoot.class);
        return userRoot;
    }


    //定义网络请求事件，更新UI展示有几条数据
    void refershUI() {
        Log.i("isRun", "refershUI: run");
        String url = "http://" + mIp + "/MapLocal/android/readList?id=" + MINE_ID;
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Toast.makeText(context, "网络连接错误：" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(String s) {
                        if (!s.equals(DetailListAdapter.jsonString)) {
                            if (s.contains(ConstantUtil.NetService)) {//数据请求失败
                                //Toast.makeText(context, s, Toast.LENGTH_LONG).show();
                            } else {//数据请求成功
                                try {
                                    DetailListAdapter.jsonString = s;
                                    JSONObject jsonObject = new JSONObject(s);
                                    JSONArray user = jsonObject.getJSONArray("users");
                                    DetailListAdapter.user_map = new HashMap<>();
                                    for (int i = 0; i < user.length(); i++) {
                                        DetailListAdapter.user_map.put(user.getJSONObject(i).getString("fromnum"), user.getJSONObject(i).getString("msgNum"));
                                    }
                                    DetailListAdapter.depts = jsonObject.getJSONArray("depts");
                                    if (mAdapter != null)
                                        mAdapter.notifyDataSetChanged();
                                    if (user.length() == 0 && DetailListAdapter.depts.length() == 0) {
                                        ConstantUtil.IS_HaveOrNO = false;
                                        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                        manager.cancel(121);
                                    } else {
                                        ConstantUtil.IS_HaveOrNO = true;
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
        isStart = false;
        startService(netServiceIntent);
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        timer = null;
//    }

    //    String getCurProcessName(Context context) {
//        int pid = android.os.Process.myPid();
//        ActivityManager mActivityManager = (ActivityManager) context
//                .getSystemService(Context.ACTIVITY_SERVICE);
//        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
//                .getRunningAppProcesses()) {
//            if (appProcess.pid == pid) {
//
//                return appProcess.processName;
//            }
//        }
//        return null;
//    }

}
