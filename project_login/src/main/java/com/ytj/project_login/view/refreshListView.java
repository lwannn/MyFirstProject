package com.ytj.project_login.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.ytj.project_login.R;

/**
 * Created by Administrator on 2016/10/27.
 */

public class refreshListView extends ListView implements AbsListView.OnScrollListener {
    private OnRefershListener mListener;
    private View mHeaderView;
    private boolean isRefresh;//标志位，表示是否正在刷新
    private Context mContext;

    public refreshListView(Context context) {
        this(context, null);
    }

    public refreshListView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.listViewStyle);
    }

    public refreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(context);
    }

    private void init(Context context) {
//        View view = LayoutInflater.from(context).inflate(R.layout.listview_refresh,this,false);
        View view = View.inflate(context, R.layout.listview_refresh, null);
        mHeaderView = view.findViewById(R.id.progressBar);
        mHeaderView.setVisibility(View.GONE);//progressbar一开始是隐藏的
        this.addHeaderView(view);

        //为listView自己添加滚动监听
        this.setOnScrollListener(this);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        int firdtItemPosition = view.getFirstVisiblePosition();

        if (firdtItemPosition == 0 && scrollState == SCROLL_STATE_IDLE && scroll_down) {
            if (!isRefresh) {
                //开始刷新
                isRefresh = true;
                mHeaderView.setVisibility(View.VISIBLE);
                if (mListener != null)
                    mListener.OnRefresh();
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    }

    private boolean scroll_down = false;//是否向下滑的标志位
    private float down_y = 1;
    private float up_y = 0;

    //重写onTouchEvent
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                down_y = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                up_y = ev.getY();
                if (up_y > down_y) {//向下滑
                    scroll_down = true;
                } else {//向上滑
                    scroll_down = false;
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    //listView下拉刷新的回调接口
    public interface OnRefershListener {
        public void OnRefresh();
    }

    //暴露一个方法设置回调监听接口
    public void setOnRefreshListener(OnRefershListener listener) {
        this.mListener = listener;
    }

    //刷新完成
    public void refreshComplete() {
        isRefresh = false;
        mHeaderView.setVisibility(View.GONE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        Toast.makeText(mContext,MeasureSpec.getSize(heightMeasureSpec)+"",Toast.LENGTH_SHORT).show();
    }
}
