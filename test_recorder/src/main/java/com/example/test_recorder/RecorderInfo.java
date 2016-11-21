package com.example.test_recorder;

/**
 * 录音音频的粗略信息
 * Created by Administrator on 2016/11/9.
 */

public class RecorderInfo {
    private float time;
    private String filePath;

    public RecorderInfo(float time, String filePath) {
        this.time = time;
        this.filePath = filePath;
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
