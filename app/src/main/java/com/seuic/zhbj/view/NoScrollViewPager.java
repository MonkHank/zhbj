package com.seuic.zhbj.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by someone on 2017/4/23.
 * 不允许滑动的viewpager
 */

public class NoScrollViewPager extends ViewPager {

    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    // 事件拦截
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false; // 不拦截子控件的事件；
    }

    /**
     * 重写此方法，禁用ViewPager的滑动
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return true;
    }
}
