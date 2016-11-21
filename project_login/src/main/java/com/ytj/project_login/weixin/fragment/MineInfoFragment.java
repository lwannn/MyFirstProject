package com.ytj.project_login.weixin.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ytj.project_login.R;
import com.ytj.project_login.utils.ConstantUtil;
import com.ytj.project_login.weixin.MineInfoSet;
import com.ytj.project_login.weixin.loadimage.SelectPhotoActivity;

/**
 * Created by ddanyang on 2016/11/14.
 */

public class MineInfoFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FrameLayout contentView = (FrameLayout) inflater.inflate(R.layout.fragment_mine, new FrameLayout(getActivity()), true);

        ImageView imageHead = (ImageView) contentView.findViewById(R.id.img_head);
        String path = Environment.getDataDirectory().getPath() + ConstantUtil.userHeadImagePath;
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        imageHead.setImageBitmap(bitmap);

        TextView tv_name = (TextView) contentView.findViewById(R.id.tv_name);
        tv_name.setText(ConstantUtil.userName);

        View lay_set = contentView.findViewById(R.id.lay_set);
        lay_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        View _view = ((LinearLayout)contentView.getChildAt(0)).getChildAt(0);
        _view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MineInfoSet.class);
                startActivity(intent);
            }
        });
        _view = ((LinearLayout)contentView.getChildAt(0)).getChildAt(1);
        _view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SelectPhotoActivity.class);
                startActivity(intent);
            }
        });

        return contentView;
    }
}
