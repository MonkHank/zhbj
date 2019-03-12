package com.seuic.zhbj.utils;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * Created by bgl on 2017/5/29.
 * 内存缓存  使用软引用(该方式已经告别)
 * 建议使用 LruCache
 */
public class MemoryCacheUtils {

    //    private HashMap<String, SoftReference<Bitmap>> mMemoryCache = new HashMap<>();
    private LruCache<String, Bitmap> mMemoryCache ;

    public MemoryCacheUtils() {
        // LruCache : least recently used 最近最少使用算法
        // 可以将最近最少使用的对象回收掉，从而保证内存不会超出范围
        long maxMemory = Runtime.getRuntime().maxMemory();
        LogUtil.v(this,"分配给app的最大内存大小："+maxMemory);
        mMemoryCache = new LruCache<String, Bitmap>((int) (maxMemory/8)){// 最大允许使用的内存多大
            // 返回每个对象的大小
            @Override
            protected int sizeOf(String key, Bitmap value) {
                int byteCount = value.getByteCount();// 计算图片的大小：每行字节数(像素点) * 高度
                return byteCount;
            }
        };

    }

    public void setMemoryCache(String url, Bitmap bitmap) {
//        mMemoryCache.put(url, bitmap);
//        SoftReference<Bitmap> softReference = new SoftReference<Bitmap>(bitmap);// 使用软引用将bitmap包装起来
//        mMemoryCache.put(url, softReference);
        mMemoryCache.put(url, bitmap);
    }

    public Bitmap getMemoryCache(String url) {
//        SoftReference<Bitmap> softReference = mMemoryCache.get(url);
//        if (softReference != null) {
//            Bitmap bitmap = softReference.get();
//            return bitmap;
//        }
        return mMemoryCache.get(url);
    }
}
