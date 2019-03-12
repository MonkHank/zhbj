package com.seuic.zhbj.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.seuic.zhbj.base.BasePager;

/**
 * Created by someone on 2017/4/23.
 * 设置
 */

public class SettingPager extends BasePager {
    /**
     * 子类一定要实现父类的构造方法
     * @param activity
     */
    public SettingPager(Activity activity) {
        super(activity);
    }

    @Override
   public void initData() {
        // 要给帧布局填充一个TextView对象
        TextView textView = new TextView(mActivity);
        textView.setText("设置");
        textView.setTextColor(Color.RED);
        textView.setTextSize(22);
        textView.setGravity(Gravity.CENTER);

        flContent.addView(textView);
        mTvTitle.setText("设置");
        mIbMenu.setVisibility(View.INVISIBLE);
    }
}
