package com.ytj.project_login.manager;

import android.media.MediaRecorder;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by Administrator on 2016/11/9.
 */

public class AudioManager {
    private MediaRecorder mMediaRecorder;
    private String mDir;
    private String mCurFilePath;
    private boolean isPrepare;

    //使用单例模式
    private static AudioManager mInstance;

    public AudioManager(String dir) {
        mDir = dir;
    }

    public static AudioManager getInstance(String dir) {
        if (mInstance == null) {
            synchronized (AudioManager.class) {
                mInstance = new AudioManager(dir);
            }
        }

        return mInstance;
    }

    public String getCurFilePath() {
        return mCurFilePath;
    }

    public interface OnAudioPrePareListener {
        public void wellPrepare();
    }

    public OnAudioPrePareListener mListener;

    public void setOnPrepareListener(OnAudioPrePareListener listener) {
        mListener = listener;
    }

    public void prepareAudio() {
        try {
            isPrepare = false;
            File dir = new File(mDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String fileName = generateFileName();
            File file = new File(mDir, fileName);
            mCurFilePath = file.getAbsolutePath();

            mMediaRecorder = new MediaRecorder();
            mMediaRecorder.setOutputFile(file.getAbsolutePath());
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mMediaRecorder.prepare();
            mMediaRecorder.start();
            isPrepare = true;

            if (mListener != null)
                mListener.wellPrepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //随机产生文件名
    private String generateFileName() {
        return UUID.randomUUID().toString() + ".amr";
    }

    public int getVoiceLevel(int maxLevel) {
        if (isPrepare) {
            try {
                return maxLevel * mMediaRecorder.getMaxAmplitude() / 32768 + 1;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 1;
    }

    public void release() {
        mMediaRecorder.stop();
        mMediaRecorder.release();
        mMediaRecorder = null;
    }

    public void cancel() {
        release();
        if (mCurFilePath != null) {
            File file = new File(mCurFilePath);
            if (file.exists()) {
                file.delete();
            }
            mCurFilePath = null;
        }
    }
}
