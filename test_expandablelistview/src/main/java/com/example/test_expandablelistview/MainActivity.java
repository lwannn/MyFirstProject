package com.example.test_expandablelistview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListView;

import com.example.test_expandablelistview.adapter.MyBaseExpandableListAdapter;

public class MainActivity extends AppCompatActivity {

    private ExpandableListView mExpandableListView;
    int[] logos = new int[]{
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher
    };

    private String[] armTypes = new String[]{
            "WORD", "EXCEL", "EMAIL", "PPT"
    };

    private String[][] arms = new String[][]{
            {"文档编辑", "文档排版", "文档处理", "文档打印"},
            {"表格编辑", "表格排版", "表格处理", "表格打印"},
            {"收发邮件", "管理邮箱", "登录登出", "注册绑定"},
            {"演示编辑", "演示排版", "演示处理", "演示打印"},
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mExpandableListView = (ExpandableListView) findViewById(R.id.elv);
        MyBaseExpandableListAdapter myAdapter = new MyBaseExpandableListAdapter(this,logos,armTypes,arms);

        mExpandableListView.setAdapter(myAdapter);
    }
}
