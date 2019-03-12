package com.seuic.zhbj.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.seuic.zhbj.R;
import com.seuic.zhbj.utils.LogUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by bgl on 2017/5/23.
 * 下拉刷新 ListView
 */

public class PullTorRefreshListView extends ListView implements AbsListView.OnScrollListener{

    private static final int STATE_PULL_TO_REFRESH = 1;
    private static final int STATE_RELEASE_TO_REFRESH = 2;
    private static final int STATE_REFRESHING = 3;

    private int mCurrentState =  STATE_PULL_TO_REFRESH; // 当前刷新状态;

    private View mHeaderView;
    private int mHeaderViewHeight;
    private int startY;
    private TextView tvTitle;
    private TextView tvTime;
    private ImageView ivArrow;
    private RotateAnimation animUp;
    private RotateAnimation animDown;
    private ProgressBar pbLoading;
    private View mFooterView;
    private int mFooterViewHeight;

    public PullTorRefreshListView(Context context) {
        super(context);
        initHeaderView();
        initFooterView();
    }

    public PullTorRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeaderView();
        initFooterView();
    }

    public PullTorRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView();
        initFooterView();
    }

    // 初始化头布局
    private void initHeaderView() {
        mHeaderView = View.inflate(getContext(), R.layout.pull_to_refresh_header, null);
        this.addHeaderView(mHeaderView);
        tvTitle = (TextView) mHeaderView.findViewById(R.id.tv_title);
        tvTime = (TextView) mHeaderView.findViewById(R.id.tv_time);
        ivArrow = (ImageView) mHeaderView.findViewById(R.id.iv_arrow);
        pbLoading = (ProgressBar) mHeaderView.findViewById(R.id.pb_loading);
        // 隐藏头布局
        mHeaderView.measure(0, 0);
        mHeaderViewHeight = mHeaderView.getMeasuredHeight();
        mHeaderView.setPadding(0,-mHeaderViewHeight,0,0);
        LogUtil.d(this,"测量高度："+mHeaderViewHeight);

        initAnim();
        setCurrentTime();
    }

    // 初始化脚布局
    private void initFooterView() {
        mFooterView = View.inflate(getContext(), R.layout.pull_to_refresh_footer, null);
        addFooterView(mFooterView);
        mFooterView.measure(0, 0);

        mFooterViewHeight = mFooterView.getMeasuredHeight();
        mFooterView.setPadding(0,-mFooterViewHeight,0,0);
        setOnScrollListener(this);
    }

    private void setCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        String time = format.format(new Date());
        tvTime.setText(time);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = (int) ev.getY();

                break;
            case MotionEvent.ACTION_MOVE:
                // 当用户按住头条新闻的ViewPager进行下拉时，ActionDown事件会被ViewPager消费，导致startY没有赋值，此处需要重新获取与一下；
                if (startY == -1) {
                    startY = (int) ev.getY();
                }

                if (mCurrentState == STATE_REFRESHING) {
                    // 如果正在刷新，跳出循环
                    break;
                }

                int endY = (int) ev.getY();
                int dy = endY - startY;
                // 必须下拉，并且当前显示的是第一个item ,才下拉刷新
                if (dy>0 && getFirstVisiblePosition() == 0) {
                    // 计算当前控件的padding值
                    int padding = dy-mHeaderViewHeight  ;
//                    int padding = mHeaderViewHeight - dy  ;
                    mHeaderView.setPadding(0,padding,0,0);
                    if (padding >0 && mCurrentState != STATE_RELEASE_TO_REFRESH) {
                        // 改为松开刷新
                        mCurrentState = STATE_RELEASE_TO_REFRESH;
                        refreshState();
                    } else if (padding<0 && mCurrentState != STATE_PULL_TO_REFRESH) {
                        // 改为下拉刷新
                        mCurrentState = STATE_PULL_TO_REFRESH;
                    }
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                startY = -1;
                if (mCurrentState == STATE_RELEASE_TO_REFRESH) {
                    mCurrentState = STATE_REFRESHING;
                    refreshState();
                    // 完整展示头布局
                    mHeaderView.setPadding(0,0,0,0);
                    // 4.进行回调
                    if (mListener != null) {
                        mListener.onRefresh();
                    }
                }else if (mCurrentState == STATE_PULL_TO_REFRESH){
                    // 隐藏头布局
                    mHeaderView.setPadding(0,-mHeaderViewHeight,0,0);
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void initAnim() {
        animUp = new RotateAnimation(0,-180, Animation.RELATIVE_TO_SELF,-0.5f
                ,Animation.RELATIVE_TO_SELF,-0.5f);
        animUp.setDuration(200);
        animUp.setFillAfter(true);

        animDown = new RotateAnimation(-180,0, Animation.RELATIVE_TO_SELF,-0.5f
                ,Animation.RELATIVE_TO_SELF,-0.5f);
        animDown.setDuration(200);
        animDown.setFillAfter(true);

    }

    // 根据当前状态来刷新界面
    private void refreshState() {
        switch (mCurrentState) {
            case STATE_PULL_TO_REFRESH:
                tvTitle.setText("下拉刷新");
                pbLoading.setVisibility(View.INVISIBLE);
                ivArrow.setVisibility(View.VISIBLE);

                ivArrow.startAnimation(animDown);
                break;

            case STATE_RELEASE_TO_REFRESH:
                tvTitle.setText("松开刷新");
                pbLoading.setVisibility(View.INVISIBLE);
                ivArrow.setVisibility(View.VISIBLE);

                ivArrow.startAnimation(animUp);
                break;

            case STATE_REFRESHING:
                tvTitle.setText("正在刷新");
                // 清除箭头动画，否则无法隐藏
                ivArrow.clearAnimation();
                pbLoading.setVisibility(View.VISIBLE);
                ivArrow.setVisibility(View.INVISIBLE);
                break;
        }
    }

    // 刷新结束，收起控件
    public void onRefreshComplited(boolean success) {
        if (!isLoadMore) {
            mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);
            mCurrentState = STATE_PULL_TO_REFRESH;
            tvTitle.setText("下拉刷新");
            pbLoading.setVisibility(View.INVISIBLE);
            ivArrow.setVisibility(View.VISIBLE);
            // 只要刷新成功，才更新时间
            if (success) {
                setCurrentTime();
            }
        } else { // 加载更多
            mFooterView.setPadding(0,-mFooterViewHeight,0,0);
            isLoadMore = false;
        }
    }

    /**
     * 3.定义成员变量，监听接口
     */
    private OnRefreshListener mListener;
    /**
     * 2. 暴露接口，设置监听
     */
    public void setOnrefreshListener(OnRefreshListener mListener) {
        this.mListener = mListener;
    }

    /**
     * 1.下拉刷新的回调接口
     */
    public interface  OnRefreshListener{
        void onRefresh();

        // 下拉加载更多
        void onLoadMore();
    }


    private boolean isLoadMore;// 标记是否正在加载更多
    // 滑动状态发生变化
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE) { // 空闲状态
            int lastVisiblePosition = getLastVisiblePosition();
            if (lastVisiblePosition == getCount() -1 && !isLoadMore) {// 最后一个条目,并且没有显示正在加载更多
                // 到底了  加载更多
                isLoadMore = true;
                mFooterView.setPadding(0,0,0,0); // 显示加载更多
                setSelection(lastVisiblePosition);// 将listview的位置显示在最后一个item上，从而加载更多直接展示出来，
                                                    // 无需手动滑动
                // 通知主页面加载更多
                if (mListener != null) {
                    mListener.onLoadMore();
                }
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}
