package com.ytj.project_login;

import android.app.Activity;
import android.os.Bundle;

import java.util.ArrayList;

/**
 * 显示user的详细信息（1.所在组 2.所参与的案件）
 */
public class DetailActivity extends Activity {
    private ArrayList<String> groupType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
    }
}
