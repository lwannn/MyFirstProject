package com.ytj.project_login.weixin.fragment;

import android.app.Fragment;
import android.content.Context;
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
    DetailFragmentTwo mDetailActivity;
    View view;

    public static Detail_Fragment newInstance() {
        Detail_Fragment detail_fragment = new Detail_Fragment();
        return detail_fragment;
    }

    public Detail_Fragment() {
        mDetailActivity = new DetailFragmentTwo();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            mDetailActivity.onCreate(getActivity(), inflater);
            view = mDetailActivity.getRootView();
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mDetailActivity.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mDetailActivity.onPause();

    }
}
