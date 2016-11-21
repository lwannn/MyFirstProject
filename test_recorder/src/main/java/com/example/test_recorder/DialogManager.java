package com.example.test_recorder;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/11/8.
 */

public class DialogManager {
    private Dialog mDialog;
    private Context mContext;

    private ImageView mIcon;
    private ImageView mVoice;
    private TextView mTip;
    private LayoutInflater mInflater;

    public DialogManager(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public void showRecorderDialog() {
        mDialog = new Dialog(mContext, R.style.Theme_AudioDialog);
        View view = mInflater.inflate(R.layout.dialog, null);
        mDialog.setContentView(view);

        mIcon = (ImageView) mDialog.findViewById(R.id.iv_recorder_icon);
        mVoice = (ImageView) mDialog.findViewById(R.id.iv_recorder_voice);
        mTip = (TextView) mDialog.findViewById(R.id.tv_lable);

        mDialog.show();
    }

    public void recording() {
        if (mDialog != null) {
            mIcon.setVisibility(View.VISIBLE);
            mVoice.setVisibility(View.VISIBLE);
            mTip.setVisibility(View.VISIBLE);

            mIcon.setBackgroundResource(R.drawable.recorder);
            mTip.setText("手指上滑，取消录音！");
        }
    }

    public void cancelRecorder() {
        if (mDialog != null) {
            mIcon.setVisibility(View.VISIBLE);
            mVoice.setVisibility(View.GONE);
            mTip.setVisibility(View.VISIBLE);

            mIcon.setBackgroundResource(R.drawable.cancel);
            mTip.setText("松开手指，取消录音！");
        }
    }

    public void tooShort() {
        if (mDialog != null) {
            mIcon.setVisibility(View.VISIBLE);
            mVoice.setVisibility(View.GONE);
            mTip.setVisibility(View.VISIBLE);

            mIcon.setBackgroundResource(R.drawable.voice_to_short);
            mTip.setText("录音时间过短！");
        }
    }

    public void updateVoiceLevel(int level) {
        if (mDialog != null) {

            //根据资源名称获取资源id
            int resId = mContext.getResources().getIdentifier("v" + level, "drawable", mContext.getPackageName());
            mVoice.setBackgroundResource(resId);
        }
    }

    public void dismissDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }
}
