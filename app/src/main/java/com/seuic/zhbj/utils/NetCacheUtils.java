package com.seuic.zhbj.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by bgl on 2017/5/29.
 * 网络缓存工具类
 */

public class NetCacheUtils {
    private LocalCacheUtils mLocalCacheUtils;
    private MemoryCacheUtils mMemoryCacheUtils;

    public NetCacheUtils(LocalCacheUtils mLocalCacheUtils, MemoryCacheUtils mMemoryCacheUtils) {
        this.mLocalCacheUtils = mLocalCacheUtils;
        this.mMemoryCacheUtils = mMemoryCacheUtils;
    }

    public void getBitmapFromNet(ImageView imageView, String url) {
        //AsyncTask 异步封装工具类，可以实现异步请求以及主界面更新(线程池 + handler 的封装)
        new BitmapTask().execute(imageView, url);

    }

    /**
     * 第一个泛型：doInBackground 参数类型
     * 第二个泛型：onProgressUpdate 参数类型
     * 第三个泛型：doInBackground 返回类型 和 onPostExecute 参数类型
     */
    class BitmapTask extends AsyncTask<Object, Integer, Bitmap> {

        private ImageView imageViews;
        private String url;

        // 预加载 (热身)
        @Override
        protected void onPreExecute() {// 主线程
            super.onPreExecute();
        }

        // 正在加载
        @Override
        protected Bitmap doInBackground(Object... params) {// 子线程
            imageViews = (ImageView) params[0];
            url = (String) params[1];
            // 开始下载图片
            Bitmap bitmap = downLoad(url);

//            publishProgress(values); // 调用此方法实现进度更新（会回调onProgressUpdate）
            return bitmap;
        }

        // 更新进度方法
        @Override
        protected void onProgressUpdate(Integer... values) {// 主线程
            // 更新进度条
            super.onProgressUpdate(values);
        }

        // 加载结束后的方法
        @Override
        protected void onPostExecute(Bitmap result) {// 主线程
            imageViews.setTag(url); // 将当前ImageView和url绑定在一起
            super.onPostExecute(result);
            if (result != null) {
                // 由于listview的重用机制导致imageview对象可能被多个item共用，从而导致错误的图片设置给了imageview对象
                //所以需要在此校验，判断是否是正确图片
                String url = (String) imageViews.getTag();
                if (url.equals(this.url)) {// 判断图片绑定的url是否是当前bitmap的url，如果是，说明图片正确
                    imageViews.setImageBitmap(result);
                    // 写本地缓存
                    mLocalCacheUtils.setLocalCache(url,result);
                    // 写内存缓存
                    mMemoryCacheUtils.setMemoryCache(url,result);
                }
            }
        }
    }

    // 下载图片
    private Bitmap downLoad(String url) {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);// 超过5 s没连上
            conn.setReadTimeout(5000); // 连上 ，超过5s没数据
            conn.connect();
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                InputStream inputStream = conn.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return null;
    }
}
