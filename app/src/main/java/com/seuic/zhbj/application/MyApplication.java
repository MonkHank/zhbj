package com.seuic.zhbj.application;

import android.app.Application;
import android.os.StrictMode;

/**
 * Created by ${kevin} on 2017/7/31.
 */

public class MyApplication extends Application {
    private boolean isDevelopment = true;
    @Override
    public void onCreate() {
        super.onCreate();
        if (isDevelopment) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()   // or .detectAll() for all detectable problems
//                    .penaltyDialog() //弹出违规提示对话框
                    .penaltyLog() //在Logcat 中打印违规异常信息
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }
    }
}
