package com.myAndroid.helloworld;

import java.io.File;

import android.app.Application;
import android.content.Context;

import com.myAndroid.helloworld.apkdownloader.model.ApkDownloadManager;

public class MyApplication extends Application {
    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        context = this;

        File file = getFilesDir();// 路径为/data/data/com.myAndroid.helloworld/files

        ApkDownloadManager.getInstance.init(this);
        // startService(new Intent(this, LockScreenService.class));// 启动锁屏的Service
    }
}
