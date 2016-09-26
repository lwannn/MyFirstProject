package com.ytj.project_login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ytj.project_login.adapter.MyBaseExpandableListAdapter;
import com.ytj.project_login.db.dao.DBDao;
import com.ytj.project_login.jsonEntity.Cases;
import com.ytj.project_login.jsonEntity.Department;
import com.ytj.project_login.jsonEntity.TeamUser;
import com.ytj.project_login.jsonEntity.TeamUsersRoot;
import com.ytj.project_login.jsonEntity.UserRoot;
import com.ytj.project_login.jsonEntity.headPortrait;
import com.ytj.project_login.utils.MapUtil;
import com.ytj.project_login.utils.SharePreferencesUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
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
    private CircleImageView mCircleImageView;
    private MyBaseExpandableListAdapter mAdapter;
    private Context context;
    private UserRoot userRoot;
    private TeamUsersRoot teamUsersRoot;

    private String mUsername;
    private String mIp;
    private String mCheckId;
    public static int MINE_ID;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            getHeadPortraitBitmap();
        }
    };

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
        mIp = (String) SharePreferencesUtil.getParam(context, SharePreferencesUtil.IP, "1111");
        mCheckId = (String) SharePreferencesUtil.getParam(context, SharePreferencesUtil.CHECK_ID, "0");

        groupType = new ArrayList<String>();
        groupType.add(0, "所在组");
        groupType.add(1, "所参与案件");

        items = new ArrayList<ArrayList<String>>();
        itemTeam = new ArrayList<String>();
        itemTeam.add(0, "侦查组");
        items.add(0, itemTeam);
        itemCase = new ArrayList<String>();
        itemCase.add(0, "杀人案");
        itemCase.add(1, "放火案");
        items.add(1, itemCase);

        getHeadPortraitUrl();
        getInfo();
    }

    //获取头像的相对路径，并保存
    private void getHeadPortraitUrl() {
        //获取图像路径
        String imageUrl = "http://" + mIp + "/MapLocal/android/getImage";
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
                        if(bitmap!=null)//在获取的图片不为空的情况下
                        mCircleImageView.setImageBitmap(bitmap);
                        //TODO 将图片保存到本地
                    }
                });
    }

    //初始化view
    private void initView() {
        mExpandableListView = (ExpandableListView) findViewById(R.id.elv);
        mCircleImageView = (CircleImageView) findViewById(R.id.civ);
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
                String itemName = items.get(groupPosition).get(childPosition);//点击item的名称
                if (groupPosition == 0) {//如果点击的是所在组，就跳转到聊天窗口
                    Intent intent=new Intent(context,ChatActivity.class);
                    intent.putExtra("deptid",userRoot.getDat().getDeptid());//将组的id传过去
                    intent.putExtra("deptname",userRoot.getDepartment().getName());//将组的名称也传过去
                    startActivity(intent);
                } else if (groupPosition == 1) {
                }
                return true;
            }
        });

        //将mExpandableListView展开
        mExpandableListView.expandGroup(0);
        mExpandableListView.expandGroup(1);
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
                        //test
//                        itemTeam.remove(0);
//                        itemTeam.add(0, "侦查组");
//                        mAdapter.notifyDataSetChanged();//更新mExpandableListView
                        itemTeam.remove(0);
                        //要倒序remove，先1后0
                        itemCase.remove(1);
                        itemCase.remove(0);
                        userRoot = analysisJson(s);
                        Department department = userRoot.getDepartment();
                        itemTeam.add(0, department.getName());
                        List<Cases> cases = userRoot.getCases();
                        for (int i = 0; i < cases.size(); i++) {
                            itemCase.add(i, cases.get(i).getName());
                        }
                        mAdapter.notifyDataSetChanged();

                        //通过组的id获取组的成员的详细信息（并且保存到数据库中）
                        int deptid = userRoot.getDat().getDeptid();
                        getUsersInfo(deptid);

                        //将该用户的id存到static变量中
                        MINE_ID=userRoot.getDat().getId();

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
                                    MapUtil.setName(id,alias);
                                }
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


}
