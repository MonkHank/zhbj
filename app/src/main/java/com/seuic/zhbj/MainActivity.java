package com.seuic.zhbj;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import android.view.WindowManager;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.seuic.zhbj.fragment.ContentFragment;
import com.seuic.zhbj.fragment.LeftMenuFragment;
import com.seuic.zhbj.utils.LogUtil;

/**
 * Created by someone on 2017/4/23.
 */

public class MainActivity extends SlidingFragmentActivity {
    private final static String TAG_LEFT_MENU = "TAG_LEFT_MENU";
    private final static String TAG_CONTENT = "TAG_CONTENT";
    @Override
    public void onCreate( Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // 一个空的framelayout

        setBehindContentView(R.layout.left_menu);// 一个空的framelayout

        SlidingMenu slidingMenu = getSlidingMenu();

        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN); // 全屏触摸
//        slidingMenu.setBehindOffset(300); // 屏幕预留300像素宽度

        // 在屏幕宽度 480像素上，预留的大小比例就是 300 / 480
        // 换成其他屏幕宽度时，需要比例一致，就 x 其他屏幕宽度
        WindowManager wm=getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        LogUtil.i(this,"屏幕宽度："+width+"，高度："+wm.getDefaultDisplay().getHeight());
        slidingMenu.setBehindOffset(width * 300 / 480);
        initFragment();
    }
    /**
     * 初始化两个fragment
     */
    private void initFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fl_main_content, new ContentFragment(), TAG_CONTENT);
        transaction.replace(R.id.fl_left_menu, new LeftMenuFragment(), TAG_LEFT_MENU);
        transaction.commit();
//        Fragment fragmentByTag = fm.findFragmentByTag(TAG_CONTENT); // 根据标记找到对应fragment
    }

    /**
     * 获取侧边栏对象
     * @return
     */
    public LeftMenuFragment getLeftMenuFragment() {
        FragmentManager fm = getSupportFragmentManager();
        LeftMenuFragment fragment = (LeftMenuFragment) fm.findFragmentByTag(TAG_LEFT_MENU);
        return fragment;
    }

    /**
     * 获取ContentFragment主页对象
     * @return
     */
    public ContentFragment getContentFragment() {
        FragmentManager fm = getSupportFragmentManager();
        ContentFragment fragment = (ContentFragment) fm.findFragmentByTag(TAG_CONTENT);
        return fragment;
    }
}
