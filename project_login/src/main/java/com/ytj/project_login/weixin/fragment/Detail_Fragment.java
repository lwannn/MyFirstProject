package com.ytj.project_login.weixin.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ytj.project_login.weixin.fragment.DetailFragment;

/**
 * Created by ddanyang on 2016/11/9.
 */

public class Detail_Fragment extends Fragment{
    DetailFragment mDetailActivity;

    public Detail_Fragment() {
        mDetailActivity = new DetailFragment();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mDetailActivity.onCreate(getActivity(), inflater);
        return mDetailActivity.getRootView();
    }
}
