package com.seuic.zhbj.fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.seuic.zhbj.MainActivity;
import com.seuic.zhbj.R;
import com.seuic.zhbj.base.BasePager;
import com.seuic.zhbj.base.impl.GovPager;
import com.seuic.zhbj.base.impl.HomePager;
import com.seuic.zhbj.base.impl.NewsCenterPager;
import com.seuic.zhbj.base.impl.SettingPager;
import com.seuic.zhbj.base.impl.SmartServicePager;
import com.seuic.zhbj.utils.LogUtil;

import java.util.ArrayList;

/**
 * Created by someone on 2017/4/23.
 */

public class ContentFragment extends BaseFragment {

    private ViewPager mViewPager; // NoScrollViewPager
    private ArrayList<BasePager> mPagers = new ArrayList<BasePager>();// 5个标签页的集合
    private RadioGroup rgGroup;

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_content, null);
        mViewPager = (ViewPager) view.findViewById(R.id.vp_content);
        rgGroup = (RadioGroup) view.findViewById(R.id.rg_tab);
        return view;
    }

    @Override
    public void initData() {
        mPagers.add(new HomePager(mActivity));
        mPagers.add(new NewsCenterPager(mActivity));
        mPagers.add(new SmartServicePager(mActivity));
        mPagers.add(new GovPager(mActivity));
        mPagers.add(new SettingPager(mActivity));

        mViewPager.setAdapter(new ContentAdapter());
        // 底栏标签切换监听
        rgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_tab_home: // 首页
                        mViewPager.setCurrentItem(0,false); // 取消viewpager动画滑动效果
//                        mPagers.get(0).initData();// 太麻烦，下面4个都要写
                        break;

                    case R.id.rb_tab_news:// 新闻中心
                        mViewPager.setCurrentItem(1,false);
                        break;

                    case R.id.rb_tab_smart:// 智慧服务
                        mViewPager.setCurrentItem(2,false);
                        break;

                    case R.id.rb_tab_gov: // 政务
                        mViewPager.setCurrentItem(3,false);
                        break;

                    case R.id.rb_tab_setting: // 设置
                        mViewPager.setCurrentItem(4,false);
                        break;
                }
            }
        });

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            /**
             * 当某个页面被选中
             * @param position
             */
            @Override
            public void onPageSelected(int position) {
                if (position == 0 || position == mPagers.size() -1){ // 首页和设置页禁用，其他开启侧边栏
                    setSldingMenuEnable(false);
                }else {
                    setSldingMenuEnable(true);
                }
                BasePager pager = mPagers.get(position);
                pager.initData();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mPagers.get(0).initData(); // 进入后先手动加载第一页
        setSldingMenuEnable(false); // 首次进入要调用一下
    }

    /**
     * 设置侧边栏开启或关闭
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

    class ContentAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mPagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        /**
         * 这个方法默认在ViewPager加载后执行两次
         * @param container
         * @param position
         * @return
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            LogUtil.d(ContentFragment.this,"执行了...");
            BasePager basePager = mPagers.get(position);
            View view = basePager.mRootView; // 获取当前页面对象的布局 initView执行
//            basePager.initData(); // 初始化数据   为了节省流量和性能，不要在此调用初始化数据方法
            container.addView(view);// 把当前页面添加至viewpager中
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    /**
     * 获取新闻中心页面
     */
    public NewsCenterPager getNewsCenterPager() {
        NewsCenterPager pager = (NewsCenterPager) mPagers.get(1);
        return pager;
    }
}
