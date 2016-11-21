package com.ytj.project_login.weixin.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.ytj.project_login.CaseInfoActivity;
import com.ytj.project_login.R;
import com.ytj.project_login.db.DBOpenHelper;
import com.ytj.project_login.utils.ConstantUtil;
import com.ytj.project_login.weixin.LinearLayout_lai;

import java.util.List;

/**
 * Created by ddanyang on 2016/11/10.
 */

public class CaseFragment extends Fragment {

    Context context;
    DetailFragment.TitleConfiguration configuration;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        configuration = (DetailFragment.TitleConfiguration) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_caseinfo, new FrameLayout(context), true);
        configuration.setTitleText("案件");

        ((ListView) contentView.findViewById(R.id.case_info)).setAdapter(new CaseAdapter(new DBOpenHelper(getActivity()).query_case()));

        return contentView;
    }

    class CaseAdapter extends BaseAdapter {

        List<String> contentList;

        public CaseAdapter(List<String> list) {
            contentList = list;
        }

        @Override
        public int getCount() {
            return contentList.size();
        }

        @Override
        public Object getItem(int position) {
            return contentList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            String[] contentDate = contentList.get(position).split(";");
            ViewHolder viewHodler;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.fragment_caseinfo_item, new FrameLayout(context), true);
                viewHodler = new ViewHolder();
                viewHodler.caseName = (TextView) convertView.findViewById(R.id.tv_caseName);
                viewHodler.caseTime = (TextView) convertView.findViewById(R.id.tv_caseTime);
                viewHodler.caseMark = (TextView) convertView.findViewById(R.id.tv_beizhu);
                viewHodler.casePhone = (TextView) convertView.findViewById(R.id.tv_phone);
                convertView.setTag(viewHodler);

                convertView.setOnClickListener(listener);
                viewHodler.casePhone.setOnClickListener(listener);
            }else {
                viewHodler = (ViewHolder) convertView.getTag();
            }
            viewHodler.tag = contentDate[0] + ";" + contentDate[1];
            viewHodler.caseName.setText(contentDate[1]);
            viewHodler.caseMark.setText(contentDate[2]);
            viewHodler.caseMark.setText(contentDate[3]);
            viewHodler.casePhone.setText(contentDate[4]);
            viewHodler.casePhone.setTag(contentDate[4]);
            return convertView;
        }
    }

    private static final class ViewHolder{
        public String tag;
        public TextView caseName;
        public TextView caseTime;
        public TextView caseMark;
        public TextView casePhone;

    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v instanceof ViewGroup) {
                String[] values = ((ViewHolder)v.getTag()).tag.split(";");
                Intent intent = new Intent(context, CaseInfoActivity.class);
                intent.putExtra("caseid", Integer.valueOf(values[0]));
                intent.putExtra("casename", values[1]);
                startActivity(intent);

            }else {
                String tel = (String) v.getTag();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    };
}
