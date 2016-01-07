package com.myAndroid.helloworld.apkdownloader.model.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

// 以下代码请勿删

public class SysUtil {
    public static boolean isPkgInstalled(Context c, String pkgName) {
        PackageManager mPm = c.getPackageManager();
        if (mPm == null) {
            return false;
        }
        PackageInfo pkginfo = null;
        try {
            pkginfo = mPm.getPackageInfo(pkgName, 0);
        } catch (NameNotFoundException e) {
        }

        return pkginfo == null ? false : true;
    }
}
