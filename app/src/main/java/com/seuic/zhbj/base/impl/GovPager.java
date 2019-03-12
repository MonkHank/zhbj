package com.seuic.zhbj.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.seuic.zhbj.base.BasePager;
import com.seuic.zhbj.utils.LogUtil;

/**
 * Created by someone on 2017/4/23.
 * 首页
 */

public class GovPager extends BasePager {
    /**
     * 子类一定要实现父类的构造方法
     * @param activity
     */
    public GovPager(Activity activity) {
        super(activity);
    }

    @Override
   public void initData() {
        LogUtil.i(GovPager.this,"政务初始化");
        // 要给帧布局填充一个TextView对象
        TextView textView = new TextView(mActivity);
        textView.setText("政务");
        textView.setTextColor(Color.RED);
        textView.setTextSize(22);
        textView.setGravity(Gravity.CENTER);

        flContent.addView(textView);
        mTvTitle.setText("政务中心");
        mIbMenu.setVisibility(View.VISIBLE);
    }
}
