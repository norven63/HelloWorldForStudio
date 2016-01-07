package com.myAndroid.helloworld.apkdownloader.model.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

public class PackageUtil {
    // 截断处理，防止恶意APP的超长label攻击
    private static final int APP_LABEL_MAX_LENGTH = 64;

    public static String getApkLabel(Context context, String apkFilePath) {
        String label = "";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pkgInfo = pm.getPackageArchiveInfo(apkFilePath, PackageManager.GET_ACTIVITIES);
            ApplicationInfo appInfo = pkgInfo.applicationInfo;
            appInfo.sourceDir = apkFilePath;
            appInfo.publicSourceDir = apkFilePath;
            label = pm.getApplicationLabel(appInfo).toString();
            if (TextUtils.isEmpty(label)) {
                label = appInfo.packageName;
            }
            if (!TextUtils.isEmpty(label) && label.length() > APP_LABEL_MAX_LENGTH) {
                label = label.substring(0, APP_LABEL_MAX_LENGTH);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return label;
    }

    public static Drawable getApkIcon(Context context, String apkFilePath) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pkgInfo = pm.getPackageArchiveInfo(apkFilePath, PackageManager.GET_ACTIVITIES);
            ApplicationInfo appInfo = pkgInfo.applicationInfo;
            appInfo.sourceDir = apkFilePath;
            appInfo.publicSourceDir = apkFilePath;
            return appInfo.loadIcon(pm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static PackageInfo getApkPackageInfo(Context context, String apkFilePath) {
        PackageInfo pkgInfo = null;
        try {
            PackageManager pm = context.getPackageManager();
            pkgInfo = pm.getPackageArchiveInfo(apkFilePath, PackageManager.GET_ACTIVITIES);
            ApplicationInfo appInfo = pkgInfo.applicationInfo;
            appInfo.sourceDir = apkFilePath;
            appInfo.publicSourceDir = apkFilePath;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pkgInfo;
    }
}
