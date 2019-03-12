package com.seuic.zhbj.utils;

import android.content.Context;

/**
 * Created by bgl on 2017/4/25.
 * 网络缓存的工具类
 */

public class CacheUtils {
    /**
     * 以url为key,以json 为value，保存到本地
     * @param url
     * @param json
     */
    public static void setCache(String url, String json, Context ctx) {
        // 也可以用文件缓存，以(MD5)url为文件名，以json为文件内容；
        // 谷歌市场中会对这部分做一个讲解
        PrefUtils.setString(ctx,url,json);

    }

    /**
     * 获取缓存
     * @param url
     * @param ctx
     * @return
     */
    public static String getCache(String url,Context ctx) {
        // 查找有没有一个文件名叫(MD5)url的，有的话就取出来
        String result = PrefUtils.getString(ctx, url, null);
        return result;
    }
}
