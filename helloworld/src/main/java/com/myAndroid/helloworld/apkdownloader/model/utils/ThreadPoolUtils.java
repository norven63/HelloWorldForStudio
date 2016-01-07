package com.myAndroid.helloworld.apkdownloader.model.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @file ThreadPoolUtils.java
 *
 * @author liule-ms
 *
 * @date 2015年3月13日 上午10:33:38
 *
 * @description
 */
public class ThreadPoolUtils {

    private static ExecutorService instance = null;

    private ThreadPoolUtils() {
    }

    public static ExecutorService getInstance() {
        if (instance == null) {
            instance = Executors.newCachedThreadPool();
        }
        return instance;
    }
}
