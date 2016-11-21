package com.ytj.project_login.weixin;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ytj.project_login.utils.ConstantUtil;
import com.ytj.project_login.weixin.fragment.CaseFragment;
import com.ytj.project_login.weixin.fragment.DetailFragment;
import com.ytj.project_login.R;
import com.ytj.project_login.weixin.fragment.Detail_Fragment;
import com.ytj.project_login.weixin.fragment.MineInfoFragment;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ddanyang on 2016/11/9.
 */

public class MainActivity_w extends Activity implements DetailFragment.TitleConfiguration {

    CircleImageView mCircleImageView;
    TextView mTextView;
    LinearLayout_lai[] bottom_items;

    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mCircleImageView = (CircleImageView) findViewById(R.id.civ);
        mTextView = (TextView) findViewById(R.id.tv_title);
        bottom_items = ((Bottom) findViewById(R.id.activity_bottom)).getBottomItems();
        fragmentManager = getFragmentManager();
        for (LinearLayout_lai l :
                bottom_items) {
            l.setOnClickListener(listener);
        }
        ((Bottom) findViewById(R.id.activity_bottom)).setSelectedItem(0);
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch ((int) v.getTag()) {
                case 0:
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.lay_fragment, new Detail_Fragment());
                    fragmentTransaction.commit();
                    break;
                case 1:
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.lay_fragment, new CaseFragment());
                    fragmentTransaction.commit();
                    break;
                case 2:
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.lay_fragment, new MineInfoFragment());
                    fragmentTransaction.commit();
                    break;
            }
        }
    };

    @Override
    public void setTitleText(String s) {
        mTextView.setText(s);
    }

    @Override
    public void setTitleImage(Bitmap bitmap) {
        mCircleImageView.setImageBitmap(bitmap);
        String path = Environment.getDataDirectory().getPath() + ConstantUtil.userHeadImagePath;
        Log.i("MainActivity_w", "setTitleImage: " + path);
        try {
            FileOutputStream out = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onBackPressed() {
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }

    long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

            if ((System.currentTimeMillis() - exitTime) > 2000)  //System.currentTimeMillis()无论何时调用，肯定大于2000
            {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
