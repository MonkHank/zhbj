package com.seuic.zhbj.base.impl;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.seuic.zhbj.MainActivity;
import com.seuic.zhbj.base.BaseMenuDetailPager;
import com.seuic.zhbj.base.BasePager;
import com.seuic.zhbj.base.impl.menu.InteractMenuDetaiPager;
import com.seuic.zhbj.base.impl.menu.NewsMenuDetailPager;
import com.seuic.zhbj.base.impl.menu.PhotosMenuDetaiPager;
import com.seuic.zhbj.base.impl.menu.TopicMenuDetaiPager;
import com.seuic.zhbj.domain.NewsMenu;
import com.seuic.zhbj.fragment.LeftMenuFragment;
import com.seuic.zhbj.global.GlobalConstant;
import com.seuic.zhbj.utils.CacheUtils;
import com.seuic.zhbj.utils.LogUtil;

import java.util.ArrayList;

/**
 * Created by someone on 2017/4/23.
 * 新闻中心
 *          包括：
 *          新闻、专题、组图、互动
 */

public class NewsCenterPager extends BasePager {
    public final Object TAG = NewsCenterPager.this;

   // 菜单详情页的集合
    private ArrayList<BaseMenuDetailPager> mMenuDetailPagers;
    private NewsMenu mNewsData; // 分类信息网络数据

    /**
     * 子类一定要实现父类的构造方法
     * @param activity
     */
    public NewsCenterPager(Activity activity) {
        super(activity);
    }

    // ContentFragment 的 initData()方法中调用
    @Override
   public void initData() {
        mTvTitle.setText("新闻中心");
        mIbMenu.setVisibility(View.VISIBLE);

        // 先判断有没有缓存，如果有的话，就先加载缓存
        String cache = CacheUtils.getCache(GlobalConstant.CATEGORY_URL, mActivity);
        if (!TextUtils.isEmpty(cache)) {
            LogUtil.d(TAG,"有缓存了");
            processData(cache);
        }
//        else {
//            // 请求服务器，获取数据，使用xUtils请求服务器
//            getDataFromServer();
//        }
        getDataFromServer();//如果服务器有数据更新了，那么按照上面那么弄，就一直从缓存中取；缓存的意义就是先睹为快
    }

    private void getDataFromServer() {
        HttpUtils utils = new HttpUtils();
        utils.send(HttpMethod.GET, GlobalConstant.CATEGORY_URL, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                processData(result);
                CacheUtils.setCache(GlobalConstant.CATEGORY_URL,result,mActivity);
                LogUtil.v(TAG, result);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                LogUtil.v(TAG, msg);
            }
        });
    }
    /**
     * 使用Gson来解析json
     */
    private void processData(String json) {
        Gson gson = new Gson();
        mNewsData = gson.fromJson(json, NewsMenu.class);
        LogUtil.e(TAG, mNewsData.toString());

        // 获取侧边栏对象
        MainActivity mainUi = (MainActivity) mActivity;
        LeftMenuFragment leftMenuFragment = mainUi.getLeftMenuFragment();
        // 给侧边栏设置数据
        leftMenuFragment.setMenuData(mNewsData.data);

        // 初始化菜单详情页
        mMenuDetailPagers = new ArrayList<BaseMenuDetailPager>();
        mMenuDetailPagers.add(new NewsMenuDetailPager(mActivity,mNewsData.data.get(0).children));
        mMenuDetailPagers.add(new TopicMenuDetaiPager(mActivity));
        mMenuDetailPagers.add(new PhotosMenuDetaiPager(mActivity,ibPhoto));
        mMenuDetailPagers.add(new InteractMenuDetaiPager(mActivity));

        // 将新闻菜单详情页设置成默认页面
        setCurrentDetailPager(0);
    }

    /**
     * 设置菜单详情页
     * @param position
     */
    public void setCurrentDetailPager(int position) {
        // 重新给framelayout添加内容
        // 获取当前应该显示的页面
        BaseMenuDetailPager pager = mMenuDetailPagers.get(position);
        // 当前页面的布局
        View view = pager.mRootView;
        // 清除旧的布局
        flContent.removeAllViews();
        // 给帧布局添加布局
        flContent.addView(view);
        // 初始化数据
        pager.initData();
        // 更新标题
        mTvTitle.setText(mNewsData.data.get(position).title);

        // 如果是组图界面，显示切换按钮
        if (pager instanceof PhotosMenuDetaiPager) {// 如果是组图界面
            ibPhoto.setVisibility(View.VISIBLE);
        } else { // 隐藏切换按钮
            ibPhoto.setVisibility(View.GONE);
        }
    }
}
