package com.seuic.zhbj.base.impl.menu.tab;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.seuic.zhbj.NewsDetailActivity;
import com.seuic.zhbj.base.BaseMenuDetailPager;
import com.seuic.zhbj.domain.NewsMenu;
import com.seuic.zhbj.domain.NewsTabBean;
import com.seuic.zhbj.global.GlobalConstant;
import com.seuic.zhbj.utils.CacheUtils;
import com.seuic.zhbj.utils.LogUtil;
import com.seuic.zhbj.utils.PrefUtils;
import com.seuic.zhbj.utils.ToastUtils;
import com.seuic.zhbj.view.PullTorRefreshListView;
import com.viewpagerindicator.CirclePageIndicator;
import com.seuic.zhbj.R;
import java.util.ArrayList;

/**
 * Created by bgl on 2017/5/12.
 * 为了方便起见，继承BaseMenuDetailPager，继承其方法，两者没有关联
 *
 * 页签界面对象
 * 在 菜单详情页  -- 新闻 NewsMenuDetailPager 中初始化
 */

public class TabDetailPager extends BaseMenuDetailPager {

    @ViewInject(R.id.vp_top_news) ViewPager mViewPager;
    @ViewInject(R.id.tv_title) TextView tvTitle;
    @ViewInject(R.id.indicator) CirclePageIndicator mIndicator;
    @ViewInject(R.id.pageListview)PullTorRefreshListView mListView;


    private NewsMenu.NewsTabData mTabData; // 单个页签的网络数据
    private final String mUrl;
    private ArrayList<NewsTabBean.TopNews> mTopNews;
    private ArrayList<NewsTabBean.NewsData> mNewsList;
    private NewsAdapter mNewsAdapter;
    private String mMoreUrl;  // 下一页数据的连接

    private Handler mHandler;

    public TabDetailPager(Activity activity, NewsMenu.NewsTabData newsTabData) {
        super(activity);
        mTabData = newsTabData;
        mUrl = GlobalConstant.SERVER_URL + mTabData.url;
    }

    @Override
    public View initView() {
      /*  textView = new TextView(mActivity);
//        textView.setText("页签");
//        textView.setText(mTabData.title); // 在这里会有空指针
        textView.setTextColor(Color.RED);
        textView.setTextSize(22);
        textView.setGravity(Gravity.CENTER);*/
        View view = View.inflate(mActivity, R.layout.pager_tab_detail, null);// PullTorRefreshListView
        ViewUtils.inject(this, view);
        // 给listView添加头布局
        View mHeaderView = View.inflate(mActivity, R.layout.list_item_header, null);// 轮播图
        // 注意：此处必须将头布局注入
        ViewUtils.inject(this, mHeaderView);
        mListView.addHeaderView(mHeaderView);

        // 5.前端界面设置回调
        mListView.setOnrefreshListener(new PullTorRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 刷新操作
                getDataFromServer();
            }

            @Override
            public void onLoadMore() {
                // 判断手否有下一页数据
                if (mMoreUrl != null) {
                    getMoreDataFromServer();
                } else {
                    ToastUtils.showToast(mActivity,"没有下一页了");
                    mListView.onRefreshComplited(true);// 此处 true or false没有效果
                }
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 获取头布局数量
                int headerViewsCount = mListView.getHeaderViewsCount();
                position = position - headerViewsCount;
                NewsTabBean.NewsData news = mNewsList.get(position);

                // read_ids：1101,1102,1103
                String readIds = PrefUtils.getString(mActivity, "res_id", "");
                if (!readIds.contains(""+news.id)) { // 只有不包括当前id才追加，避免重复追加同一个id；
                    readIds = readIds + news.id + ",";//1101,
                    PrefUtils.setString(mActivity,"res_id",readIds);
                }
                // 要将被点击的item的文字改成灰色，局部刷新，view对象就是当前被点击的对象
                TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
                tvTitle.setTextColor(Color.GRAY);
                Intent intent = new Intent(mActivity, NewsDetailActivity.class);
                intent.putExtra("url", news.url);
                mActivity.startActivity(intent);

//                mNewsAdapter.notifyDataSetChanged(); // 和上面两行代码效果一样；但是浪费性能
            }
        });

        return view;
    }

    @Override
    public void initData() {
//        textView.setText(mTabData.title);
        String cache = CacheUtils.getCache(mUrl, mActivity);
        if (!TextUtils.isEmpty(cache))
            processedData(cache,false);

        getDataFromServer();
    }

    public void getDataFromServer() {
        HttpUtils utils = new HttpUtils();
        utils.send(HttpMethod.GET, mUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                processedData(result,false);
                CacheUtils.setCache(mUrl,result,mActivity);
                // 收起下拉刷新
                mListView.onRefreshComplited(true);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                e.printStackTrace();
                ToastUtils.showToast(mActivity, s);
                // 收起下拉刷新
                mListView.onRefreshComplited(false);
            }
        });
    }

    // 加载下一页数据
    private void getMoreDataFromServer() {
        HttpUtils utils = new HttpUtils();
        utils.send(HttpMethod.GET, mMoreUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                processedData(result,true);
                // 收起下拉刷新
                mListView.onRefreshComplited(true);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                e.printStackTrace();
                ToastUtils.showToast(mActivity, s);
                // 收起下拉刷新
                mListView.onRefreshComplited(false);
            }
        });
    }

    private void processedData(String result,boolean isMore) {
        Gson gson = new Gson();
        NewsTabBean newsTabBean = gson.fromJson(result, NewsTabBean.class);
//        LogUtil.i(this,newsTabBean.toString());
        String moreUrl = newsTabBean.data.more;
        if (!TextUtils.isEmpty(moreUrl)) {
            // 下一页的连接
            mMoreUrl = GlobalConstant.SERVER_URL + moreUrl;
            LogUtil.i(this,"下一页连接:"+moreUrl);
        } else {
            mMoreUrl = null;
        }
        if (!isMore) {
            // 头条新闻填充数据
            mTopNews = newsTabBean.data.topnews;
            if (mTopNews != null) {
                mViewPager.setAdapter(new TopNewsAdapter());
                mIndicator.setViewPager(mViewPager);
                mIndicator.setSnap(true); // 快照方式显示

                // 事件要设置Indicator
                mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        // 更新头条新闻标题
                        NewsTabBean.TopNews topNews = mTopNews.get(position);
                        tvTitle.setText(topNews.title);
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
                // 更新头条新闻的第一个标题
                tvTitle.setText(mTopNews.get(0).title);
                // 默认让第一个选中，解决页面销毁后重新初始化时，indicator仍然保留上次圆点位置的bug
                mIndicator.onPageSelected(0);
            }
            // 更新列表新闻
            mNewsList = newsTabBean.data.news;
            if (mNewsList != null) {
                mNewsAdapter = new NewsAdapter();
                mListView.setAdapter(mNewsAdapter);
            }
            // 保证自动循环只执行一次；
            if (mHandler == null) {
                mHandler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        int currentItem = mViewPager.getCurrentItem();
                        currentItem++;
                        if (currentItem > mTopNews.size() - 1) {
                            currentItem = 0;// 如果已经到了最后一个页面，跳到第一个页面
                        }
                        mViewPager.setCurrentItem(currentItem);
                        mHandler.sendEmptyMessageDelayed(0, 3000);// 3秒后走handleMessage方法，形成内循环
                    }
                };
                mHandler.sendEmptyMessageDelayed(0, 3000);// 3秒后走handleMessage方法

                mViewPager.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN: // 停止自动停播
                                // 删除handler的所有消息
                                mHandler.removeCallbacksAndMessages(null);
//                                mHandler.post(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        // 在主线程执行
//                                    }
//                                });
                                break;

                            case MotionEvent.ACTION_CANCEL:// 取消事件,当触摸按下viewpage后，直接滑动listview导致抬起事件无法响应，但会走此事件
                                // 启动广告
                                mHandler.sendEmptyMessageDelayed(0, 3000);// 3秒后走handleMessage方法
                                break;

                            case MotionEvent.ACTION_UP:
                                // 启动广告
                                mHandler.sendEmptyMessageDelayed(0, 3000);// 3秒后走handleMessage方法
                                break;
                        }
                        return false;
                    }
                });
            }

        } else { // 加载更多数据
            ArrayList<NewsTabBean.NewsData> moreNews = newsTabBean.data.news;
            mNewsList.addAll(moreNews);// 将数据追加在原来的集合中
            // 刷新listview
            mNewsAdapter.notifyDataSetChanged();
        }


    }

    // 头条新闻数据适配
    class TopNewsAdapter extends PagerAdapter {

        private final BitmapUtils mBitmapUtils;

        public TopNewsAdapter() {
            mBitmapUtils = new BitmapUtils(mActivity);
            //  Set the default image
            mBitmapUtils.configDefaultLoadingImage(R.mipmap.topnews_item_default);
        }

        @Override
        public int getCount() {
            return mTopNews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = new ImageView(mActivity);
            view.setImageResource(R.mipmap.topnews_item_default);
            // 设置图片缩放方式，宽高填充父控件
            view.setScaleType(ImageView.ScaleType.FIT_XY);
            // 下载图片-将图片设置给ImageView，避免内存溢出，加缓存
            // BitmapUtils
            String imageUrl = mTopNews.get(position).topimage;
            mBitmapUtils.display(view,imageUrl);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    class NewsAdapter extends BaseAdapter{

        private  BitmapUtils mBitmapUtils;

        public NewsAdapter() {
            mBitmapUtils = new BitmapUtils(mActivity);
            mBitmapUtils.configDefaultLoadingImage(R.mipmap.news_pic_default);
        }


        @Override
        public int getCount() {
            return mNewsList.size();
        }

        @Override
        public Object getItem(int position) {
            return mNewsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mActivity, R.layout.list_item_news, null);
                holder = new ViewHolder();
                holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
                holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
                holder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            NewsTabBean.NewsData news = (NewsTabBean.NewsData) getItem(position);
            holder.tvTitle.setText(news.title);
            holder.tvDate.setText(news.pubdate);

            // 根据本地记录来标记已读和未读
            String readIds = PrefUtils.getString(mActivity, "res_id", "");
            if (readIds.contains("" + news.id)) {
                holder.tvTitle.setTextColor(Color.GRAY);
            } else {
                holder.tvTitle.setTextColor(Color.BLACK);
            }

            mBitmapUtils.display(holder.ivIcon,news.listimage);
            return convertView;
        }
    }
    static class ViewHolder {
        public ImageView ivIcon;
        public TextView tvTitle;
        public TextView tvDate;
    }
}
