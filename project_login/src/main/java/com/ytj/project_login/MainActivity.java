package com.ytj.project_login;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ytj.project_login.utils.SharePreferencesUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;

public class MainActivity extends Activity implements View.OnClickListener {

    private CircleImageView mHeadPortrait;
    private EditText mUsername;
    private EditText mPassword;
    private Button mLogin;
    private TextView mSetIp;
    private TextView mHelp;

    private String mIp;//保存的是需要设置连接的服务器的ip地址(如果端口不是80端口，请带上端口号)
    private String username;
    private String password;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=this;

        initView();
        initData();
        initEvent();
    }

    //初始化View
    private void initView() {
        mHeadPortrait = (CircleImageView) findViewById(R.id.head_portrait);
        mUsername = (EditText) findViewById(R.id.et_username);
        mPassword = (EditText) findViewById(R.id.et_password);
        mLogin = (Button) findViewById(R.id.btn_login);

        mSetIp= (TextView) findViewById(R.id.tv_setIp);
        mHelp= (TextView) findViewById(R.id.tv_help);
    }

    //初始化数据
    private void initData() {
//        mIp = "192.168.2.118:8080";
        mIp= (String) SharePreferencesUtil.getParam(context,"ip","null");
    }

    //初始化事件
    private void initEvent() {
        mLogin.setOnClickListener(this);
        mSetIp.setOnClickListener(this);
        mHelp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login://登录按钮的点击事件
                username = mUsername.getText().toString().trim();
                password = mPassword.getText().toString().trim();
                if (username.equals("")) {
                    Toast.makeText(MainActivity.this, "用户名不能为空！", Toast.LENGTH_SHORT).show();
                } else if (password.equals("")) {
                    Toast.makeText(MainActivity.this, "密码不能为空！", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        Login();
                    }catch (Exception e){
                        Toast.makeText(context,"端口号前面的冒号必须是英文字符！",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.tv_setIp://设置服务器ip地址
                final View setIpView=View.inflate(this,R.layout.dialog_setip,null);

                AlertDialog.Builder builder=new AlertDialog.Builder(this)
                        .setTitle("设置服务器ip")
                        .setView(setIpView)
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //获取服务器ip的值
                                EditText et_ip= (EditText) setIpView.findViewById(R.id.et_setIp);
                                String ip=et_ip.getText().toString().trim();
                                //将服务器ip保存下来
                                SharePreferencesUtil.setParam(context,"ip",ip);
                                //刷新服务器ip地址
                                mIp= (String) SharePreferencesUtil.getParam(context,"ip","1111");
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog dialog=builder.create();
                dialog.show();
                break;
            case R.id.tv_help://帮助说明
                AlertDialog.Builder builder1=new AlertDialog.Builder(this)
                        .setTitle("帮助说明")
                        .setMessage("目前没有想好有什么需要帮助的！");

                AlertDialog dialog1 = builder1.create();
                dialog1.show();
                break;
        }
    }

    //用户登录方法
    private void Login() {
        String url = "http://" + mIp + "/MapLocal/android/loginForAndroid";//安卓登录接口
        OkHttpUtils
                .post()
                .url(url)
                .addParams("username", username)
                .addParams("password", password)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Toast.makeText(MainActivity.this, "网络连接错误！", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String s) {
                        String subs=s.substring(s.indexOf("\"")+1,s.lastIndexOf("\""));
                        String[] sArray=subs.split(",");
                        String flag=sArray[0];
                        String check=sArray[1];
                        if (flag.equals("true")) {
                            Toast.makeText(MainActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                        } else if(flag.equals("false")){
                            Toast.makeText(MainActivity.this, "用户名或密码错误！", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(MainActivity.this, "ip为："+mIp+",请确保无误！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
