package com.seuic.zhbj.base.impl.menu;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.seuic.zhbj.MainActivity;
import com.seuic.zhbj.R;
import com.seuic.zhbj.base.BaseMenuDetailPager;
import com.seuic.zhbj.base.impl.menu.tab.TabDetailPager;
import com.seuic.zhbj.domain.NewsMenu;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;

/**
 * Created by bgl on 2017/4/26.
 */

/**
 * 菜单详情页  -- 新闻
 * 在 NewsCenterPager 中初始化
 * 布局结构---
 *            ViewPagerIndicator
 *            ViewPager
 *
 *
 * ViewPagerIndicator的使用步骤：
 *  1、引入库
 *  2、拷贝布局文件
 *  3、拷贝相关代码(指示器和viewpager的绑定,重写getPageTitle方法返回标题)
 *  4、清单文件中添加样式
 *  5、自己的layout修改背景为白色
 */
public class NewsMenuDetailPager extends BaseMenuDetailPager implements ViewPager.OnPageChangeListener{

    @ViewInject(R.id.vp_news_menu_detail)ViewPager mViewPager;
    @ViewInject(R.id.indicator)TabPageIndicator mIndicator;

    ArrayList<NewsMenu.NewsTabData> mTabData; // 页签网络数据
    ArrayList<TabDetailPager> mPagers; // 页签页面集合

    public NewsMenuDetailPager(Activity activity, ArrayList<NewsMenu.NewsTabData> children) {
        super(activity);
        mTabData = children;
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.pager_news_menu_detail, null);//TabPageIndicator+ViewPager
        ViewUtils.inject(this,view);
        return view;
    }

    @Override
    public void initData() {
        mPagers = new ArrayList<TabDetailPager>();
        for (int i = 0; i<mTabData.size();i++) {
            TabDetailPager pager = new TabDetailPager(mActivity,mTabData.get(i));
            mPagers.add(pager);
        }
        mViewPager.setAdapter(new NewsMenuDetailAdapter());
        mIndicator.setViewPager(mViewPager); // 将ViewPager和指示器绑定在一起,注意必须在viewpager设置数据之后绑定

        // 设置页面滑动监听
//        mViewPager.setOnPageChangeListener(this);
        // 此处必须让指示器设置监听，不能向上面那样设置，否则滑动过程中指示器不会滑动
        mIndicator.setOnPageChangeListener(this);
        mPagers.get(0).initData();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
//        LogUtil.e(this,"位置："+position);
        mPagers.get(position).initData();
        if (position == 0)
            setSldingMenuEnable(true); // 开启侧边栏
        else
            setSldingMenuEnable(false); // 关闭侧边栏
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class NewsMenuDetailAdapter extends PagerAdapter{

        // 指定指示器的标题
        @Override
        public CharSequence getPageTitle(int position) {
            NewsMenu.NewsTabData data = mTabData.get(position);
            return data.title;
        }

        @Override
        public int getCount() {
            return mPagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TabDetailPager pager = mPagers.get(position);
            View view =pager.mRootView;
            container.addView(view);
//            pager.initData();
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

    /**
     * 开启或关闭侧边栏
     * @param b
     */
    private void setSldingMenuEnable(boolean b) {
        // 获取侧边栏对象
        MainActivity mainActivity = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainActivity.getSlidingMenu();
        if (b) {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        }else {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }

    @OnClick(R.id.btn_next)
    public void nextPage(View view) {
        // 跳转到下一个页面
        int currentItem = mViewPager.getCurrentItem();
        currentItem ++;
        mViewPager.setCurrentItem(currentItem);
    }
}
