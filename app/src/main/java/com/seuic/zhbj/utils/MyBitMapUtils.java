package com.seuic.zhbj.utils;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.seuic.zhbj.R;

/**
 * Created by bgl on 2017/5/29.
 *
 * 自定义3级缓存展示图片
 */

public class MyBitMapUtils {

    private MemoryCacheUtils mMemoryCacheUtils;
    private NetCacheUtils mNetCacheUtils;
    private LocalCacheUtils mLocalCacheUtils;

    public MyBitMapUtils() {
        mMemoryCacheUtils = new MemoryCacheUtils();
        mLocalCacheUtils = new LocalCacheUtils();
        mNetCacheUtils = new NetCacheUtils(mLocalCacheUtils,mMemoryCacheUtils);
    }


    /**
     * @param imageView
     * @param url
     * 优先从内存中加载图片，速度最快，不浪费流量；
     * 其次从本地加载图片，速度快，不浪费流量
     * 最后从网络加载图片，速度慢，消耗流量
     */
    public void display(ImageView imageView, String url) {
        imageView.setImageResource(R.mipmap.news_pic_default);// 设置默认图片
        Bitmap bitmap = mMemoryCacheUtils.getMemoryCache(url);
        if (bitmap != null) { // 从内存缓存加载
            imageView.setImageBitmap(bitmap);
            return ;
        }
        bitmap = mLocalCacheUtils.getLocalCache(url);
        if (bitmap != null) { // 说明有本地缓存
            imageView.setImageBitmap(bitmap);
            // 写内存缓存
            mMemoryCacheUtils.setMemoryCache(url,bitmap);
            return;
        }
        mNetCacheUtils.getBitmapFromNet(imageView, url);
    }
}
