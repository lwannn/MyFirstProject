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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ytj.project_login.adapter.ChatMsgAdapter;
import com.ytj.project_login.db.dao.DBDao;
import com.ytj.project_login.entity.LvChatMsg;
import com.ytj.project_login.jsonEntity.ChatMsg;
import com.ytj.project_login.jsonEntity.ChatMsgRoot;
import com.ytj.project_login.utils.ConstantUtil;
import com.ytj.project_login.utils.MapUtil;
import com.ytj.project_login.utils.SharePreferencesUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;

/**
 * 聊天界面的activity
 */
public abstract class BaseChatActivity extends Activity {

    private TextView mTextView;
    private ListView mListView;
    private EditText mEditText;
    private Button mButton;
    private Context context;
    private Thread getMsgThread;
    private ChatMsgAdapter mAdapter;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                //获取该组成员的聊天信息
                getChatMsgByid(id);
            } else if (msg.what == 1) {
                if (mAdapter == null) {
                    mAdapter = new ChatMsgAdapter(context, allLvChatMsgList);
                    mListView.setAdapter(mAdapter);
                } else {
                    mAdapter.notifyDataSetChanged();
                }
                //直接跳转到ListView的最后一个item,这样用户体验会更好
                mListView.setSelection(allLvChatMsgList.size());
            }

        }
    };

    private int id;
    private String Chatname;
    private int mineId;
    private String mIp;
    private int ChatMsgMaxId;
    private boolean isSaveFlag = true;//数据是否保存到数据库中的标志位（默认值为true）
    private boolean isStart = true;//控制线程的运行
    private List<LvChatMsg> lvChatMsgList;
    private List<LvChatMsg> allLvChatMsgList = new ArrayList<LvChatMsg>();

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
        id = intent.getIntExtra("id", -1);//默认值为-1
        Chatname = intent.getStringExtra("Chatname");
        mIp = (String) SharePreferencesUtil.getParam(context, SharePreferencesUtil.IP, "1111");
        mineId = DetailActivity.MINE_ID;

        //在子线程中获取maxId
        getMsgThread = new Thread() {
            @Override
            public void run() {
                super.run();
                DBDao dbDao = new DBDao(context);
                while (isStart & isSaveFlag) {
//                    ChatMsgMaxId = dbDao.getTeamChatMsgMaxId(getChatType(), id + "");
                    ChatMsgMaxId = getChatMsgMaxId(dbDao, mineId, id);
                    mHandler.sendEmptyMessage(0);
                    isSaveFlag = false;
                    try {
                        //TODO 如果轮询的设置时间过短，就容易崩溃(目前还没有找到原因)
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        getMsgThread.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isStart = true;
        if(!getMsgThread.isAlive()){//如果线程停了，就开启
            getMsgThread.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isStart = false;
        Log.e("System.out", getMsgThread.isAlive() + "");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //获取该组成员的聊天信息
    private void getChatMsgByid(int id) {
        String url = null;
        if (ChatMsgMaxId == -1) {//如果为-1,说明本地没有改组的聊天信息记录（获取最新的十条信息）
//            url = "http://" + mIp + "/MapLocal/android/getChat?deptid=" + id + "&type=" + ConstantUtil.TEAM_CHAT_TYPE;
            url = getNewInfoUrl(mIp, mineId, id);
        } else {
//            url = "http://" + mIp + "/MapLocal/android/getChat?deptid=" + id + "&type=" + ConstantUtil.TEAM_CHAT_TYPE + "&maxid=" + ChatMsgMaxId;
            url = getUrl(mIp, mineId, id, ChatMsgMaxId);
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
                        int ret = -1;
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            ret = jsonObject.getInt("ret");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (ret == 0) {//在获取到了数据的情况下
                            Gson gson = new Gson();
                            ChatMsgRoot chatMsgRoot = gson.fromJson(s, ChatMsgRoot.class);
                            List<ChatMsg> chatMsgList = null;
                            LvChatMsg lvChatMsg = null;

                            chatMsgList = chatMsgRoot.getData();

                            lvChatMsgList = new ArrayList<LvChatMsg>();
                            for (int i = chatMsgList.size() - 1; i >= 0; i--) {//接口给我的数据是倒序的，所以从后到前遍历
                                ChatMsg chatMsg = chatMsgList.get(i);
                                String content = chatMsg.getContent();
                                String intime = chatMsg.getIntime().replace("T", " ");//将时间中的T给去掉
                                int teamUserId = Integer.parseInt(chatMsg.getFromnum());//发送消息的组员的id
                                String name = MapUtil.getName(teamUserId);
                                LvChatMsg.Type type = null;
                                if (teamUserId == mineId) {//如果发送消息的是改用户本人
                                    type = LvChatMsg.Type.OUTCOMING;
                                } else {//如果发送消息的是组内其他人
                                    type = LvChatMsg.Type.INCOMING;
                                }

                                lvChatMsg = new LvChatMsg(name, content, intime, null, type);
                                if (lvChatMsg.type == LvChatMsg.Type.INCOMING) {//如果是来的消息就添加到聊天集合中
                                    lvChatMsgList.add(lvChatMsg);
                                }
                            }
                            if (lvChatMsgList.size() > 0) {
                                allLvChatMsgList.addAll(lvChatMsgList);
                                mHandler.sendEmptyMessage(1);
                            }

                            SaveChatMsg(chatMsgList);
                        } else if (ret == 1) {
                            isSaveFlag = true;
                        }
                    }
                });
    }

    //保存新的聊天记录到数据库
    private void SaveChatMsg(final List<ChatMsg> chatMsgList) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                DBDao dbDao = new DBDao(context);
                for (ChatMsg chatMsg : chatMsgList
                        ) {
                    dbDao.addChatMsg(chatMsg);
                }

                isSaveFlag = true;
            }
        }.start();
    }

    //初始化事件
    private void initEvent() {
        mTextView.setText(Chatname + getTitleEnd());
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
                String name = MapUtil.getName(mineId);
                String content = mEditText.getText().toString().trim();
                String intime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                LvChatMsg.Type type = LvChatMsg.Type.OUTCOMING;
                LvChatMsg lvChatMsg = new LvChatMsg(name, content, intime, null, type);
                allLvChatMsgList.add(lvChatMsg);
                mHandler.sendEmptyMessage(1);

                mEditText.setText("");//将消息框设置为空
                //将发送的消息传给web端
                sendMsg(content);
            }
        });

        //取消ListView的分割线
        mListView.setDividerHeight(0);

//        //隐藏键盘，用户体验会更好（这个代码貌似没有起作用）
//        InputMethodManager inputMethodManager= (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//        inputMethodManager.hideSoftInputFromWindow(mEditText.getWindowToken(),0);
    }

    private void sendMsg(final String content) {
        String url = "http://" + mIp + "/MapLocal/android/addChat";
        OkHttpUtils
                .post()
                .url(url)
                .addParams("fromnum", mineId + "")
                .addParams("content", content)
                .addParams("tonum", id + "")
                .addParams("type", getChatType() + "")
                .addParams("ctype", ConstantUtil.CHAT_WRITING_TYPE + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Toast.makeText(context, "网络连接错误！", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String s) {
                        if (!s.equals("true")) {
                            Toast.makeText(context, "服务器错误，消息无法发出！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public abstract int getChatType();

    public abstract String getTitleEnd();

    public abstract String getNewInfoUrl(String mIp, int fromId, int toId);

    public abstract String getUrl(String mIp, int fromId, int toId, int chatMsgMaxId);

    public abstract int getChatMsgMaxId(DBDao dbDao, int fromId, int toId);
}
