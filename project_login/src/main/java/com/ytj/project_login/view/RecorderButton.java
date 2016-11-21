package com.ytj.project_login.view;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.ytj.project_login.R;
import com.ytj.project_login.manager.AudioManager;
import com.ytj.project_login.manager.DialogManager;

/**
 * Created by Administrator on 2016/11/8.
 */

public class RecorderButton extends Button implements AudioManager.OnAudioPrePareListener {
    private static final int DISTANCE_Y_CANCEL = 50;
    private static final int STATE_NORMAL = 1;//正常状态
    private static final int STATE_RECORDING = 2;//正在录音的状态
    private static final int STATE_WANT_TO_CANCEL = 3;//想去取消录音的状态

    private int mCurState = STATE_NORMAL;//现在的状态
    private DialogManager mDialogManager;
    private AudioManager mAudioManager;
    private float mTime;
    private boolean isRecording;//是否正在录音的标志位
    private boolean isStrike;//是否触发长按事件的标志位

    public RecorderButton(Context context) {
        this(context, null);
    }

    public RecorderButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        String dir = Environment.getExternalStorageDirectory() + "/lwan_audio_recorder";
        mDialogManager = new DialogManager(getContext());
        mAudioManager = AudioManager.getInstance(dir);//单例模式
        mAudioManager.setOnPrepareListener(this);

        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                isStrike = true;
                mAudioManager.prepareAudio();
                return false;
            }
        });
    }

    private Runnable mGetVoiceLevelRunnable = new Runnable() {
        @Override
        public void run() {
            while (isRecording) {
                try {
                    Thread.sleep(100);
                    mHandler.sendEmptyMessage(MSG_UPATE_VOICE);
                    mTime += 0.1f;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private static final int MSG_AUDIO_PREPARE = 0X110;
    private static final int MSG_UPATE_VOICE = 0X111;
    private static final int MSG_DIALOG_DISMISS = 0X112;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_AUDIO_PREPARE:
                    isRecording = true;
                    //当recorder prepaer之后
                    mDialogManager.showRecorderDialog();
                    //开启一个线程来获取声音的等级
                    new Thread(mGetVoiceLevelRunnable).start();
                    break;
                case MSG_UPATE_VOICE:
                    mDialogManager.updateVoiceLevel(mAudioManager.getVoiceLevel(7));
                    break;
                case MSG_DIALOG_DISMISS:
                    mDialogManager.dismissDialog();
                    break;
            }
        }
    };

    @Override
    public void wellPrepare() {
        mHandler.sendEmptyMessage(MSG_AUDIO_PREPARE);
    }

    public interface OnAudioFinishRecorderListener {
        public void Finish(float time, String filePath);
    }

    private OnAudioFinishRecorderListener mListener;

    public void setOnAudioFinishRecorderListener(OnAudioFinishRecorderListener listener) {
        mListener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                changeState(STATE_RECORDING);
                break;
            case MotionEvent.ACTION_MOVE:
                if (wantToCancel(x, y)) {
                    changeState(STATE_WANT_TO_CANCEL);
                } else {
                    changeState(STATE_RECORDING);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!isStrike) {
                    reset();
                    return super.onTouchEvent(event);
                }

                if (!isRecording || mTime < 0.6f) {
                    mDialogManager.tooShort();
                    mAudioManager.cancel();
                    mHandler.sendEmptyMessageDelayed(MSG_DIALOG_DISMISS, 1300);
                } else if (mCurState == STATE_RECORDING) {//正常录制结束
                    mDialogManager.dismissDialog();
                    mAudioManager.release();

                    if (mListener != null) {
                        mListener.Finish(mTime, mAudioManager.getCurFilePath());
                    }
                } else if (mCurState == STATE_WANT_TO_CANCEL) {
                    mDialogManager.dismissDialog();
                    mAudioManager.cancel();
                }
                reset();
                break;
        }

        return super.onTouchEvent(event);

    }

    //重置一些标志位
    private void reset() {
        changeState(STATE_NORMAL);
        mTime = 0;
        isRecording = false;
        isStrike = false;
    }

    private boolean wantToCancel(float x, float y) {
        if (x < 0 || x > getWidth()) {
            return true;
        }

        if (y < -DISTANCE_Y_CANCEL || y > DISTANCE_Y_CANCEL + getHeight()) {
            return true;
        }
        return false;
    }

    private void changeState(int state) {
        if (mCurState != state) {
            mCurState = state;
            switch (state) {
                case STATE_NORMAL:
                    setBackgroundResource(R.drawable.btn_state_normal);
                    setText(R.string.click_recorder);
                    break;
                case STATE_RECORDING:
                    setBackgroundResource(R.drawable.btn_state_pressed);
                    setText(R.string.recording);
                    mDialogManager.recording();
                    break;
                case STATE_WANT_TO_CANCEL:
                    setBackgroundResource(R.drawable.btn_state_pressed);
                    setText(R.string.up_cancel);
                    mDialogManager.cancelRecorder();
                    break;
            }
        }
    }
}
