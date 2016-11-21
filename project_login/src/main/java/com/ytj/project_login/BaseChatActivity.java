package com.ytj.project_login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.ytj.project_login.adapter.ChatMsgAdapter;
import com.ytj.project_login.db.dao.DBDao;
import com.ytj.project_login.entity.LvChatMsg;
import com.ytj.project_login.entity.TelName;
import com.ytj.project_login.jsonEntity.ChatMsg;
import com.ytj.project_login.jsonEntity.ChatMsgRoot;
import com.ytj.project_login.utils.ConstantUtil;
import com.ytj.project_login.utils.MapUtil;
import com.ytj.project_login.utils.SharePreferencesUtil;
import com.ytj.project_login.utils.compressImgUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;

/**
 * 聊天界面的activity
 */
public abstract class BaseChatActivity extends Activity {

    private View isHaveMsg;
    private TextView mTextView;
    private ImageView mCheckLocation;//私聊专用
    private ListView mListView;
    private EditText mEditText;
    private Button mButton;
    private LinearLayout mImage;
    private LinearLayout mLocation;
    private LinearLayout mCamera;
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
    private String tel;//私人聊天专用
    private int mineId;
    private String mIp;
    private int ChatMsgMaxId;
    private boolean isSaveFlag = true;//数据是否保存到数据库中的标志位（默认值为true）
    private boolean isStart = true;//控制线程的运行
    private List<LvChatMsg> lvChatMsgList;
    private List<LvChatMsg> allLvChatMsgList = new ArrayList<LvChatMsg>();

    private String mCameraPath;//存放拍照后图片存储的路径
    private Date date = new Date();

    //锁定是私聊还是群聊
    protected int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        context = this;

        initView();
        initData();
        initEvent();

        //判断手机根目录是否有“图片”目录
        fileIsExist();
    }

    private void fileIsExist() {
        File file = new File(Environment.getExternalStorageDirectory() + "/图片");
        if (!file.exists()) {
            file.mkdir();
        }
    }

    //初始化View
    private void initView() {
        isHaveMsg = findViewById(R.id.is_have_msg);
        mCheckLocation = (ImageView) findViewById(R.id.ima_title_lacal);
        mTextView = (TextView) findViewById(R.id.tv_title);
        mListView = (ListView) findViewById(R.id.lv_chat);
        mEditText = (EditText) findViewById(R.id.et_Msg);
        mButton = (Button) findViewById(R.id.btn_sendMsg);

        mImage = (LinearLayout) findViewById(R.id.ll_image);
        mLocation = (LinearLayout) findViewById(R.id.ll_location);
        mCamera = (LinearLayout) findViewById(R.id.ll_camera);
    }

    //初始化数据
    private void initData() {
        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);//默认值为-1
        Chatname = intent.getStringExtra("Chatname");
        tel = intent.getStringExtra("tel");
        mIp = (String) SharePreferencesUtil.getParam(context, SharePreferencesUtil.IP, "1111");
        mineId = DetailActivity.MINE_ID;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isStart = true;
        //在子线程中获取maxId
        getMsgThread = new Thread() {
            @Override
            public void run() {
                super.run();
                //设置线程的优先级
                Process.setThreadPriority(Process.SYSTEM_UID);
                DBDao dbDao = new DBDao(context);
                while (isStart & isSaveFlag) {
                    ChatMsgMaxId = getChatMsgMaxId(dbDao, mineId, id);
                    mHandler.sendEmptyMessage(0);
                    isSaveFlag = false;
                    Log.e("System.out", "我还好好滴！！！");
                    try {
                        //TODO 如果轮询的设置时间过短，就容易崩溃(目前还没有找到原因)
                        if (ConstantUtil.IS_HaveOrNO) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    isHaveMsg.setVisibility(View.VISIBLE);
                                }
                            });
                        }else{
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    isHaveMsg.setVisibility(View.INVISIBLE);
                                }
                            });
                        }
                        Thread.sleep(1000);
                        if (ConstantUtil.IS_HaveOrNO) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    isHaveMsg.setVisibility(View.VISIBLE);
                                }
                            });
                        }else{
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    isHaveMsg.setVisibility(View.INVISIBLE);
                                }
                            });
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        getMsgThread.start();

        boolean b = getMsgThread.isAlive();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isStart = false;
        Log.e("System.out", getMsgThread.isAlive() + "");
    }

    //获取该组成员的聊天信息
    private void getChatMsgByid(int id) {
        String url = null;
        if (ChatMsgMaxId == -1) {//如果为-1,说明本地没有改组的聊天信息记录（获取最新的十条信息）
            url = getNewInfoUrl(mIp, mineId, id);
        } else {
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
                                int ctype = chatMsg.getCtype();
                                if (teamUserId == mineId) {//如果发送消息的是改用户本人
                                    if (ctype == ConstantUtil.CHAT_WRITING_TYPE) {
                                        type = LvChatMsg.Type.OUTCOMING;
                                    } else if (ctype == ConstantUtil.CHAT_IMAGE_TYPE) {
                                        type = LvChatMsg.Type.OUTCOMINGIMAGE;
                                    } else if (ctype == ConstantUtil.CHAT_MAP_TYPE) {
                                        type = LvChatMsg.Type.OUTCOMINGMAP;
                                    }
                                } else {//如果发送消息的是组内其他人
                                    if (ctype == ConstantUtil.CHAT_WRITING_TYPE) {
                                        type = LvChatMsg.Type.INCOMING;
                                    } else if (ctype == ConstantUtil.CHAT_IMAGE_TYPE) {
                                        type = LvChatMsg.Type.INCOMINGIMAGE;
                                    } else if (ctype == ConstantUtil.CHAT_MAP_TYPE) {
                                        //表明是地图信息
                                        type = LvChatMsg.Type.INCOMINGMAP;
                                    }

                                }


                                lvChatMsg = new LvChatMsg(name, content, intime, null, type);
                                lvChatMsgList.add(lvChatMsg);

                            }
                            if (lvChatMsgList.size() > 0) {
                                allLvChatMsgList.addAll(lvChatMsgList);
                                mHandler.sendEmptyMessage(1);
                            }

                            SaveChatMsg(chatMsgList);
                        } else if (ret == 1) {
                            isSaveFlag = true;
                        }
                        //当读取到用户消息则更新消息状态
                        upMsgState();
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
                for (ChatMsg chatMsg : chatMsgList) {
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
                String content = mEditText.getText().toString().trim();
                mEditText.setText("");//将消息框设置为空
                //将发送的消息传给web端
                sendMsg(content, ConstantUtil.CHAT_WRITING_TYPE);
            }
        });

        //取消ListView的分割线
        mListView.setDividerHeight(0);

        checkLocation(context, Chatname, tel, mCheckLocation);

        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//给选取图片设置点击事件
                //从本地相册选取图片
                Intent intent = new Intent();
                //设置文件类型
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(intent, 0);
            }
        });

        mLocation.setOnClickListener(new View.OnClickListener() {//给选取地图设置点击事件
            @Override
            public void onClick(View v) {
                TelName telName = new TelName(DetailActivity.MINE_TEL, DetailActivity.MINE_NAME);

                Intent intent = new Intent(context, PersonalBDMapActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("telName", telName);
                intent.putExtras(bundle);
                startActivityForResult(intent, 2);

            }
        });

        mCamera.setOnClickListener(new View.OnClickListener() {//给照相设置点击事件
            @Override
            public void onClick(View v) {
                String fileName = date.getTime() + ".jpg";
                mCameraPath = Environment.getExternalStorageDirectory() + "/图片/" + fileName;
                Uri photoUri = Uri.fromFile(new File(mCameraPath));
                //打开相机，将拍照后图像存储的路径进行更改
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {//取消选择图片
            Toast.makeText(getApplicationContext(), "取消", Toast.LENGTH_SHORT).show();
            return;
        }
        if (requestCode == 0) {//打开图库的请求码
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    Bitmap bm = null;
                    Uri imageUri = data.getData();//获取图片的uri

                    //获取图片的路径
                    String[] proj = {MediaStore.Images.Media.DATA};
                    //好像是android多媒体数据库的封装接口，具体的看android文档
                    Cursor cursor = getContentResolver().query(imageUri, proj, null, null, null);
                    String ipath = null;
                    if (cursor != null) {
                        //按我个人理解 这个是获得用户选择的图片的索引值
                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        //将光标移至开头，这个很重要，不小心很容易引起越界(并不这样认为)
                        cursor.moveToFirst();
                        //最后根据索引值获取图片路径
                        ipath = cursor.getString(column_index);
                    } else {
                        ipath = imageUri.getPath();
                    }
                    Log.e("System.out", ipath + "");


                    if (ipath != null) {
                        //将图片发送到服务端
                        sendImage(ipath);
                    } else {
                        Toast.makeText(context, "获取不了图片路径！！！", Toast.LENGTH_SHORT).show();
                    }
                }
            }.start();
        }

        if (requestCode == 1) {//打开相机的请求码
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    File file = new File(mCameraPath);
                    if (file.exists()) {
                        sendImage(mCameraPath);
                    }
                }
            }.start();
        }

        if (requestCode == 2 & resultCode == 1) {//打开地图的请求码和返回码
            LatLng latLng = data.getParcelableExtra("latLng");
            //组装成地图信息
            String mapInfo = "uid=null_p={lon:" + latLng.longitude + ",lat:" + latLng.latitude + "}_id=null_type=0";
            sendMsg(mapInfo, ConstantUtil.CHAT_MAP_TYPE);
        }
    }

    private void sendImage(String ipath) {
        String path = compressImgUtil.getimage(ipath, context);//压缩图片的质量和大小
        File file = new File(path);
        String url = "http://" + mIp + "/MapLocal/chatMsgAction/add";
        if (file.exists()) {//如果图片文件存在，就上传文件
            OkHttpUtils
                    .post()
                    .addFile("file", ".jpg", file)
                    .url(url)
                    .addParams("fromnum", mineId + "")
                    .addParams("content", "")
                    .addParams("tonum", id + "")
                    .addParams("type", getChatType() + "")
                    .addParams("ctype", ConstantUtil.CHAT_IMAGE_TYPE + "")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            Toast.makeText(context, "服务器连接错误，图片发送不出去！", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(String s) {
                        }
                    });
        }
    }

    private void sendMsg(final String content, int ctype) {
        String url = "http://" + mIp + "/MapLocal/chatMsgAction/add";
        Log.i("url:", url);
        OkHttpUtils
                .post()
                .url(url)
                .addParams("fromnum", mineId + "")
                .addParams("content", content)
                .addParams("tonum", id + "")
                .addParams("type", getChatType() + "")
                .addParams("ctype", ctype + "")
                .addParams("readed",getReaded())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Toast.makeText(context, "网络连接错误！", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String s) {
                        Log.i("results:", s);
                    }
                });
    }

    //更新消息状态
    int upMsgState_number = 0;
    void upMsgState() {
        OkHttpUtils.post()
                .url(ConstantUtil.IP + "/MapLocal/chatMsgAction/updRead")
                .addParams("fromnum", id + "")
                .addParams("id", mineId + "")
                .addParams("type", type+"")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        if (upMsgState_number < 5) {
                            upMsgState();
                        }else {
                            Toast.makeText(BaseChatActivity.this, "网络连接问题，或者服务器问题", Toast.LENGTH_LONG).show();
                            upMsgState_number = 0;
                        }
                    }

                    @Override
                    public void onResponse(String s) {
                        int i = 0;
                        String g = s;
                    }
                });
    }

    String getReaded() {
        int readed = 0;
        switch (getChatType()) {
            case ConstantUtil.PERSONAL_CHAT_TYPE://是组员消息的时候
                break;
            case ConstantUtil.TEAM_CHAT_TYPE: //是群消息的时候
                readed = mineId;
                break;
        }
        return readed + "";
    }

    public abstract int getChatType();

    public abstract String getTitleEnd();

    public abstract String getNewInfoUrl(String mIp, int fromId, int toId);

    public abstract String getUrl(String mIp, int fromId, int toId, int chatMsgMaxId);

    public abstract int getChatMsgMaxId(DBDao dbDao, int fromId, int toId);

    public abstract void checkLocation(Context context, String chatname, String tel, View mCheckLocation);//私聊的会用到

    public void back(View view) {
        finish();
    }
}
