package com.myAndroid.helloworld.apkdownloader;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.myAndroid.helloworld.apkdownloader.model.ApkDownloadInfo;
import com.myAndroid.helloworld.apkdownloader.model.ApkDownloadManager;
import com.myAndroid.helloworld.apkdownloader.model.ApkDownloadStatusEnum;
import com.myAndroid.helloworld.apkdownloader.model.ApkDownloadTask;
import com.myAndroid.helloworld.apkdownloader.ui.ApkDownloadActivity;
import com.myAndroid.helloworld.apkdownloader.ui.ApkDownloadNotificationCenter;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author ZhouXinXing
 * @file ApkDownloadService.java
 * @date 2015年12月29日 上午10:25:10
 * @Description 下载任务单元的工作现场，所有下载任务的开启、暂停都在这里执行
 */
public class ApkDownloadService extends Service {
    public static final String EXTRA_TYPE = "extra_type";

    // 用以轮询检测是否还有下载任务正在执行的线程池
    private final ScheduledExecutorService checkSchedule = Executors.newScheduledThreadPool(1);
    private Future<?> checkFuture;

    private final IBinder mBinder = new MyBinder();

    public class MyBinder extends Binder {
        public ApkDownloadService getService() {
            return ApkDownloadService.this;
        }
    }

    @Override
    public IBinder onBind(Intent paramIntent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            return START_NOT_STICKY;
        }

        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            return START_NOT_STICKY;
        } else {
            int type = bundle.getInt(EXTRA_TYPE);
            if (type == 1) {
                startTask();
            }
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        ApkDownloadNotificationCenter.getInstance().init();

        if (checkFuture != null && !checkFuture.isCancelled()) {
            return;
        }

        // 开启轮询任务，60秒内没有下载任务更新数据，则自动关闭服务
        checkFuture = checkSchedule.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (ApkDownloadManager.getInstance.hasTaskDownLoading()) {
                    Log.e("apk_download", "10秒内仍然有任务进行，继续维持下载服务");
                    return;
                }

                Log.e("apk_download", "10秒内无任务进行，自动关闭下载服务");

                killSelf();
            }
        }, ApkDownloadManager.NEGATIVE_SECONDS, ApkDownloadManager.NEGATIVE_SECONDS, TimeUnit.SECONDS);
    }

    @Override
    public void onDestroy() {
        checkFuture.cancel(true);
        checkSchedule.shutdown();

        Log.e("apk_download", "下载服务->onDestroy");

        super.onDestroy();
    }

    /**
     * WIFI断开连接调用
     * 由于HttpHandler不支持pause，只能将advData先保存起来(不能remove)，resume的时候再重新startTask
     */
    public void pauseAllTasks() {
        ApkDownloadManager.getInstance.cancelAllTasks();
    }

    /**
     * WIFI连接成功调用
     */
    public void resumeAllTasks() {
        ApkDownloadManager.getInstance.resumeAllTasks();
    }

    /**
     * 暂停一个下载任务
     */
    public void pauseTask(ApkDownloadTask apkDownloadTask) {
        if (apkDownloadTask == null) {
            return;
        }

        ApkDownloadManager.getInstance.pauseTask(apkDownloadTask);
    }

    /**
     * 启动一个下载任务
     */
    private void startTask() {
        final String url = "http://gdown.baidu.com/data/wisegame/cfd3245b51838846/dazhongdianping_796.apk";
        final String url2 = "http://shouji.360tpcdn.com/150929/dea588214bcccd0b1458f7fdf536f616/com.qihoo.haosou_626.apk";
        final String url3 = "http://gdown.baidu.com/data/wisegame/28fb62cd60ab6d7d/baidushipin_1072103056.apk";
        final String url4 = "http://gdown.baidu.com/data/wisegame/7a681c9f73237b2e/jingdong_23599.apk";
        final String url5 = "http://gdown.baidu.com/data/wisegame/4a4520f827e73a3b/aiqiyi_80700.apk";
        final String url6 = "http://gdown.baidu.com/data/wisegame/29b952e102d3bbed/youku_90.apk";

        final String[] urlArray = new String[]{url, url2, url3, url4, url5, url6, url2, url6, url, url3};

        //TODO 伪造跳转
        if (ApkDownloadManager.getInstance.getApkDownloadTasks().size() > 0) {
            Intent intent = new Intent(this, ApkDownloadActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

            return;
        }

        //TODO 伪造循环
        for (int i = 0; i < urlArray.length; i++) {
            final String downUrl = urlArray[i];
            final int taskId = i;
            final String fileName = i + ".apk";
            final String filePath = getDownloadPath() + fileName;

            final ApkDownloadInfo apkDownloadInfo = new ApkDownloadInfo();
            apkDownloadInfo.setTaskId(taskId);
            apkDownloadInfo.setStatu(ApkDownloadStatusEnum.Downloading.code);
            apkDownloadInfo.setProgress(0);
            apkDownloadInfo.setTotal(0L);
            apkDownloadInfo.setLabel("label: " + fileName);
            apkDownloadInfo.setUrl(downUrl);
            apkDownloadInfo.setFileName("fileName: " + fileName);
            apkDownloadInfo.setFilePath(filePath);

            startTask(new ApkDownloadTask(apkDownloadInfo));
        }
    }

    /**
     * 启动一个下载任务
     */
    public void startTask(final ApkDownloadTask apkDownloadTask) {
        if (apkDownloadTask == null) {
            return;
        }

        ApkDownloadManager.getInstance.startApkDownloadTask(apkDownloadTask);
    }

    /**
     * 移除一个下载任务
     */
    public void removeTask(final ApkDownloadTask apkDownloadTask) {
        if (apkDownloadTask == null) {
            return;
        }

        ApkDownloadManager.getInstance.removeApkDownloadTask(apkDownloadTask);
    }

    /**
     * 获取下载路径
     */
    private String getDownloadPath() {
        File file;

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/HelloWorld/apk_downloads");
        } else {
            file = new File(getFilesDir().getAbsolutePath() + "/apk_downloads");
        }

        if (!file.exists()) {
            file.mkdirs();
        }

        return file.getAbsolutePath() + "/";
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);

        ApkDownloadManager.getInstance.cancelAllTasks();
        ApkDownloadNotificationCenter.getInstance().cancelAll();
    }

    private void killSelf() {
        Log.e("apk_download", "执行killSelf()");

        ApkDownloadManager.getInstance.cancelAllTasks();
        ApkDownloadNotificationCenter.getInstance().cancelAll();

        stopSelf();
    }
}
