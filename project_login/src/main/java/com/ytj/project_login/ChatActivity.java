package com.ytj.project_login;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 聊天界面的activity
 */
public class ChatActivity extends Activity {

    private TextView mTextView;
    private ListView mListView;
    private EditText mEditText;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

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
    }

    //初始化事件
    private void initEvent() {
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
    }

}
