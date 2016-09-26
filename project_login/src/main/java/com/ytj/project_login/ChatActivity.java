package com.ytj.project_login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ytj.project_login.adapter.ChatMsgAdapter;
import com.ytj.project_login.db.dao.DBDao;
import com.ytj.project_login.entity.LvTeamChatMsg;
import com.ytj.project_login.jsonEntity.ChatMsg;
import com.ytj.project_login.jsonEntity.ChatMsgRoot;
import com.ytj.project_login.utils.ConstantUtil;
import com.ytj.project_login.utils.MapUtil;
import com.ytj.project_login.utils.SharePreferencesUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * 聊天界面的activity
 */
public class ChatActivity extends Activity {

    private TextView mTextView;
    private ListView mListView;
    private EditText mEditText;
    private Button mButton;
    private Context context;
    private ChatMsgAdapter mAdapter;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                //获取该组成员的聊天信息
                getTeamChatMsgByDeptId(deptid);
            } else if (msg.what == 1) {
                if (mAdapter == null) {
                    mAdapter = new ChatMsgAdapter(context, lvTeamChatMsgList);
                    mListView.setAdapter(mAdapter);
                } else {
                    mAdapter.notifyDataSetChanged();
                }

            }

        }
    };

    private int deptid;
    private String deptname;
    private int mineId;
    private String mIp;
    private int teamChatMsgMaxId;
    private List<LvTeamChatMsg> lvTeamChatMsgList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        context = this;

//        //test
//        String name= MapUtil.getName(3);

        initView();
        initData();
        initEvent();
    }

    //初始化View
    private void initView() {
        mTextView = (TextView) findViewById(R.id.tv_title);
        mListView = (ListView) findViewById(R.id.lv_chat);
        mEditText = (EditText) findViewById(R.id.et_Msg);
        mButton = (Button) findViewById(R.id.btn_sendMsg);
    }

    //初始化数据
    private void initData() {
        Intent intent = getIntent();
        deptid = intent.getIntExtra("deptid", -1);//默认值为-1
        deptname=intent.getStringExtra("deptname");
        mIp = (String) SharePreferencesUtil.getParam(context, SharePreferencesUtil.IP, "1111");
        mineId = DetailActivity.MINE_ID;

        //在子线程中获取maxId
        new Thread() {
            @Override
            public void run() {
                super.run();
                DBDao dbDao = new DBDao(context);
                teamChatMsgMaxId = dbDao.getTeamChatMsgMaxId(ConstantUtil.TEAM_CHAT_TYPE, deptid + "");

                mHandler.sendEmptyMessage(0);
            }
        }.start();
    }

    //获取该组成员的聊天信息
    private void getTeamChatMsgByDeptId(int deptid) {
        String url = null;
        if (teamChatMsgMaxId == -1) {//如果为-1,说明本地没有改组的聊天信息记录（获取最新的十条信息）
            url = "http://" + mIp + "/MapLocal/android/getChat?deptid=" + deptid + "&type=" + ConstantUtil.TEAM_CHAT_TYPE;
        } else {
            url = "http://" + mIp + "/MapLocal/android/getChat?deptid=" + deptid + "&type=" + ConstantUtil.TEAM_CHAT_TYPE + "&maxid=" + teamChatMsgMaxId;
        }
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Toast.makeText(context, "网络连接错误!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String s) {
                        Gson gson = new Gson();
                        ChatMsgRoot chatMsgRoot = gson.fromJson(s, ChatMsgRoot.class);
                        if (chatMsgRoot.getRet() == 0) {//在获取到了数据的情况下
                            List<ChatMsg> chatMsgList = chatMsgRoot.getData();

                            lvTeamChatMsgList = new ArrayList<LvTeamChatMsg>();
                            for (ChatMsg chatMsg : chatMsgList
                                    ) {
                                String content = chatMsg.getContent();
                                String intime = chatMsg.getIntime().replace("T"," ");//将时间中的T给去掉
                                int teamUserId = Integer.parseInt(chatMsg.getFromnum());//发送消息的组员的id
                                String name = MapUtil.getName(teamUserId);
                                LvTeamChatMsg.Type type = null;
                                if (teamUserId == mineId) {//如果发送消息的是改用户本人
                                    type = LvTeamChatMsg.Type.OUTCOMING;
                                } else {//如果发送消息的是组内其他人
                                    type = LvTeamChatMsg.Type.INCOMING;
                                }

                                LvTeamChatMsg lvTeamChatMsg = new LvTeamChatMsg(name, content, intime, null, type);
                                lvTeamChatMsgList.add(lvTeamChatMsg);
                            }

                            mHandler.sendEmptyMessage(1);
                        }
                    }
                });
    }


    //初始化事件
    private void initEvent() {
        mTextView.setText(deptname+"聊天群");
        //为edittext添加内容监听
        mEditText.addTextChangedListener(new TextWatcher() {
            //内容变化前
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                System.out.println("-2-beforeTextChanged-->"
                        + mEditText.getText().toString() + "<--");
            }

            //内容正在变化
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("-1-onTextChanged-->"
                        + mEditText.getText().toString() + "<--");
            }

            //内容变化后
            @Override
            public void afterTextChanged(Editable s) {
                String sendMsg = mEditText.getText().toString().trim();
                if (sendMsg.equals("")) {//如果内容为空
                    mButton.setTextColor(getResources().getColor(R.color.btnTextColor));
                    mButton.setBackgroundResource(R.drawable.btn_sendmsgnull_style);
                    mButton.setClickable(false);
                } else {
                    mButton.setTextColor(Color.WHITE);
                    mButton.setBackgroundResource(R.drawable.btn_sendmsg_style);
                    mButton.setClickable(true);
                }
            }
        });

        //按钮的点击事件监听
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //取消ListView的分割线
        mListView.setDividerHeight(0);

//        //隐藏键盘，用户体验会更好（这个代码貌似没有起作用）
//        InputMethodManager inputMethodManager= (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//        inputMethodManager.hideSoftInputFromWindow(mEditText.getWindowToken(),0);
    }

}
