package com.example.test_recorder;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2016/11/9.
 */

public class ListAdapter extends BaseAdapter {
    private List<RecorderInfo> mDatas;
    private Context context;
    private LayoutInflater mInflater;
    private int mMinItemWidth;
    private int mMaxItemWidth;

    public ListAdapter(List<RecorderInfo> mDatas, Context context) {
        this.mDatas = mDatas;
        this.context = context;
        mInflater = LayoutInflater.from(context);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        mMaxItemWidth = (int) (displayMetrics.widthPixels * 0.7f);
        mMinItemWidth = (int) (displayMetrics.widthPixels * 0.15f);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public interface OnImageClickListener {
        public void OnItemClick(View view, int position);
    }

    private OnImageClickListener mListener;

    public void setOnImageClickListener(OnImageClickListener listener) {
        mListener = listener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.audio_item, parent, false);
            holder = new ViewHolder();
            holder.seconds = (TextView) convertView.findViewById(R.id.tv_seconds);
            holder.length = convertView.findViewById(R.id.fl_voiceLength);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.seconds.setText(Math.round(mDatas.get(position).getTime()) + "\"");
        ViewGroup.LayoutParams lp = holder.length.getLayoutParams();
        lp.width = (int) (mMinItemWidth + mMaxItemWidth / 60f * mDatas.get(position).getTime());
        final View finalConvertView = convertView;
        holder.length.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.OnItemClick(finalConvertView, position);
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView seconds;
        View length;
    }
}
