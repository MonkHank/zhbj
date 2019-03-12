package com.seuic.zhbj.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by bgl on 2017/5/16.
 */

public class ToastUtils {
    private static Toast mToast;

    public static void showToast(Context context, String text) {
        if (mToast == null) {
            mToast = Toast.makeText(context,text, Toast.LENGTH_SHORT);
        }
        mToast.setText(text);
        mToast.show();
    }
}
