package com.example.test_recorder;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Context mContext;
    private RecorderButton mRecorderButton;
    private ListView mListView;
    private ListAdapter mAdapter;
    private List<RecorderInfo> mDatas = new ArrayList<RecorderInfo>();
    private ImageView animationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        mRecorderButton = (RecorderButton) findViewById(R.id.btn_recorder);
        mListView = (ListView) findViewById(R.id.listView);
        mAdapter = new ListAdapter(mDatas, mContext);
        mListView.setAdapter(mAdapter);
        mRecorderButton.setOnAudioFinishRecorderListener(new RecorderButton.OnAudioFinishRecorderListener() {
            @Override
            public void Finish(float time, String filePath) {
                RecorderInfo info = new RecorderInfo(time, filePath);
                mDatas.add(info);
                mAdapter.notifyDataSetChanged();
                mListView.setSelection(mDatas.size() - 1);
            }
        });
//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (animationView != null) {
//                    animationView.setBackgroundResource(R.drawable.adj);
//                    animationView = null;
//                }
//
//                //动画
//                animationView = (ImageView) view.findViewById(R.id.iv_voiceIcon);
//                animationView.setBackgroundResource(R.drawable.animation_voice);
//                AnimationDrawable ad = (AnimationDrawable) animationView.getBackground();
//                ad.start();
//
//                //播放语音
//                MediaManager.playRound(mDatas.get(position).getFilePath(), new MediaPlayer.OnCompletionListener() {
//                    @Override
//                    public void onCompletion(MediaPlayer mp) {
//                        animationView.setBackgroundResource(R.drawable.adj);
//                    }
//                });
//            }
//        });

        mAdapter.setOnImageClickListener(new ListAdapter.OnImageClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                if (animationView != null) {
                    animationView.setBackgroundResource(R.drawable.adj);
                    animationView = null;
                }

                //动画
                animationView = (ImageView) view.findViewById(R.id.iv_voiceIcon);
                animationView.setBackgroundResource(R.drawable.animation_voice);
                AnimationDrawable ad = (AnimationDrawable) animationView.getBackground();
                ad.start();

                //播放语音
                MediaManager.playRound(mDatas.get(position).getFilePath(), new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        animationView.setBackgroundResource(R.drawable.adj);
                    }
                });
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        MediaManager.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MediaManager.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaManager.release();
    }
}
