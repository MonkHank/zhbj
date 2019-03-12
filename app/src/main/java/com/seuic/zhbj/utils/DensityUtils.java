package com.seuic.zhbj.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by Kevin on 2017/7/3.
 */

public class DensityUtils {
    public static int dip2px(int dp, Context ctx) {
        DisplayMetrics metrics = ctx.getResources().getDisplayMetrics();
        float density = metrics.density;
        int px = (int) (dp * density + 0.5f);  // px = dp * 设备密度
        return px;
    }
}
