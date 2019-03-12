package com.seuic.zhbj.base;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.seuic.zhbj.MainActivity;
import com.seuic.zhbj.R;

/**
 * Created by someone on 2017/4/23.
 * 5个标签页的基类
 */

public class BasePager {
    public Activity mActivity;
    public FrameLayout flContent;
    public TextView mTvTitle; // 智慧北京、新闻、生活、政务中心...
    public ImageButton mIbMenu;
    public final View mRootView; // 当前页面的布局对象
    public ImageButton ibPhoto;

    public BasePager(Activity activity) {
        mActivity = activity;
        mRootView = initView();
    }

    public View initView() {
        View view = View.inflate(mActivity, R.layout.base_pager, null);
        flContent = (FrameLayout) view.findViewById(R.id.fl_content);
        mTvTitle = (TextView) view.findViewById(R.id.tv_title);
        mIbMenu = (ImageButton) view.findViewById(R.id.ib_menu);
        ibPhoto = (ImageButton) view.findViewById(R.id.ib_display);

        mIbMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });
        return view;
    }

    /**
     * 打开或者关闭侧边栏
     */
    private void toggle() {
        MainActivity mainUi = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUi.getSlidingMenu();
        slidingMenu.toggle();// 如果当前状态是开，调用后就关，反之亦然；
    }
    public void initData() {

    }

    /**
     * 给侧边栏设置数据
     */
    public void setMenuData() {

    }
}
