package com.seuic.zhbj.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by bgl on 2017/5/29.
 */

public class LocalCacheUtils {
    private static final String LOCAL_CACHE_PATH = Environment.getExternalStorageDirectory().getPath()
            + "/zhbj_cache";


    /**
     * 写本地缓存
     * @param url
     * @param bitmap
     */
    public void setLocalCache(String url, Bitmap bitmap) {
        File dir = new File(LOCAL_CACHE_PATH);
        if (!dir.exists() || dir.isDirectory()) {
            dir.mkdirs();
        }
        String fileName = MD5Utils.digest(url);
        File cacheFile = new File(dir, fileName);
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(cacheFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读本地缓存
     * @param url
     * @return
     */
    public Bitmap getLocalCache(String url) {
        File cacheFile = new File(LOCAL_CACHE_PATH, MD5Utils.digest(url));
        if (cacheFile.exists()) {
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(cacheFile));
                return bitmap;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
