package com.seuic.zhbj.base.impl.menu;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.seuic.zhbj.base.BaseMenuDetailPager;

/**
 * Created by bgl on 2017/4/26.
 */

/**
 * 菜单详情页  -- 专题
 */
public class TopicMenuDetaiPager extends BaseMenuDetailPager {

    public TopicMenuDetaiPager(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {
        TextView textView = new TextView(mActivity);
        textView.setText("菜单详情页  -- 专题");
        textView.setTextColor(Color.RED);
        textView.setTextSize(22);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }
}
