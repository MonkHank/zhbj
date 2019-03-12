package com.seuic.zhbj.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Dell on 2017/4/16.
 */

public class PrefUtils {
    public static boolean getBoolean(Context ctx, String key,boolean defaultValue) {
        SharedPreferences sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        return sp.getBoolean(key, defaultValue);
    }

    public static void setBoolean(Context ctx, String key,boolean value) {
        SharedPreferences sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        sp.edit().putBoolean(key,value).commit();
    }
    public static void setString(Context ctx, String key,String value) {
        SharedPreferences sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        sp.edit().putString(key,value).commit();
    }
    public static String getString(Context ctx, String key,String value) {
        SharedPreferences sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        return sp.getString(key,value);
    }

    public static void setInt(Context ctx, String key,int value) {
        SharedPreferences sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        sp.edit().putInt(key,value).commit();
    }

    public static int getInt(Context ctx, String key,int value) {
        SharedPreferences sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        return sp.getInt(key,value);
    }
}
