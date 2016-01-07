package com.myAndroid.helloworld.apkdownloader.model;

import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;

import com.myAndroid.helloworld.MyApplication;
import com.myAndroid.helloworld.apkdownloader.http.HttpHandler;
import com.myAndroid.helloworld.apkdownloader.http.IFileRequestHttpResponseListener;
import com.myAndroid.helloworld.apkdownloader.http.NetWorkSingleInstance;
import com.myAndroid.helloworld.apkdownloader.model.utils.PackageUtil;
import com.myAndroid.helloworld.apkdownloader.model.utils.SysUtil;
import com.myAndroid.helloworld.apkdownloader.model.utils.ThreadPoolUtils;
import com.myAndroid.helloworld.apkdownloader.ui.ApkDownloadNotificationCenter;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author ZhouXinXing
 * @file ApkDownloadTask.java
 * @date 2015年12月23日 下午4:49:20
 * @Description 下载任务单元抽象
 */
public class ApkDownloadTask {
    private final String TAG = getClass().getSimpleName();

    /**
     * @author ZhouXinXing
     * @file ApkDownloadTask.java
     * @date 2015年12月29日 下午6:53:12
     * @Description 下载任务队列变化监听
     */
    public interface StatusChangeListener {
        public void onChange(ApkDownloadTask apkDownloadTask);
    }

    ApkDownloadStatusEnum statu;//任务状态

    private Drawable icon;//app图标

    private final ApkDownloadInfo apkDownloadInfo;//下载任务明细

    private long lastUpdateTime = new Date().getTime();//任务最近的更新时间

    private List<StatusChangeListener> statusChangeListeners = new ArrayList<StatusChangeListener>();//监听者列表

    private HttpHandler handler;//网络任务句柄

    public ApkDownloadTask(ApkDownloadInfo apkDownloadInfo) {
        this.apkDownloadInfo = apkDownloadInfo;

        statu = ApkDownloadStatusEnum.getEnumByCode(this.apkDownloadInfo.getStatu());

        if (new File(this.apkDownloadInfo.filePath).exists()
                && (statu == ApkDownloadStatusEnum.Succeed || statu == ApkDownloadStatusEnum.Installed)) {

            icon = PackageUtil.getApkIcon(MyApplication.getContext(), this.apkDownloadInfo.filePath);
        }

        //添加通知栏监听，在状态更新时进行回调
        addStatusChangeListener(ApkDownloadNotificationCenter.getInstance().getStatusChangeListener());
    }

    /**
     * 启动下载
     */
    void start() {
        if (apkDownloadInfo == null) {
            log("----- 启动Apk下载失败，下载信息为空 -----");

            return;
        }

        //先将上一次下载任务取消
        if (handler != null) {
            handler.cancel();
        }

        handler = NetWorkSingleInstance.getInstance.downLoadApk(apkDownloadInfo.url, apkDownloadInfo.filePath,
                new IFileRequestHttpResponseListener() {
                    @Override
                    public void onProgress(long curren, long total) {
                        lastUpdateTime = new Date().getTime();

                        apkDownloadInfo.progress = (int) (curren * 100 / total);

                        notifyChange();

                        if (apkDownloadInfo.total <= 0) {
                            apkDownloadInfo.total = total;//第一次网络对接时，缓存住total值
                            updateDB();
                        }

                        log("下载进度: " + apkDownloadInfo.label + " --> " + apkDownloadInfo.progress + "%");
                    }

                    @Override
                    public void onSuccess() {
                        //TODO deal with root
                        boolean isRoot = false;
                        if (isRoot) {

                        } else {
                            statu = ApkDownloadStatusEnum.Succeed;
                            apkDownloadInfo.statu = statu.code;
                        }

                        parseApkFile(new File(apkDownloadInfo.filePath));

                        log("下载成功: " + apkDownloadInfo.label + "\nfilePath：" + apkDownloadInfo.filePath);
                    }

                    @Override
                    public void onFailure(int statusCode, Throwable e) {
                        if (statusCode == 416) {//当文件已下载完成时，返回416
                            statu = ApkDownloadStatusEnum.Succeed;
                            apkDownloadInfo.statu = statu.code;
                            apkDownloadInfo.progress = 100;

                            parseApkFile(new File(apkDownloadInfo.filePath));

                            log("apk文件已存在: " + apkDownloadInfo.label + "\nfilePath：" + apkDownloadInfo.filePath);
                        } else {
                            updateStatu(ApkDownloadStatusEnum.Failed);

                            log("下载失败：" + apkDownloadInfo.label);
                            log("原因：" + e.getLocalizedMessage());
                        }
                    }
                });
    }

    /**
     * 解析apk文件，获取相应信息并判断是否已经安装了app
     * 内部封装了自动通知监听者、更新数据库的逻辑
     */
    private void parseApkFile(final File file) {
        ThreadPoolUtils.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                String filePath = file.getAbsolutePath();
                String label = PackageUtil.getApkLabel(MyApplication.getContext(), filePath);
                Drawable drawable = PackageUtil.getApkIcon(MyApplication.getContext(), filePath);

                PackageInfo apkPackageInfo = PackageUtil.getApkPackageInfo(MyApplication.getContext(),
                        filePath);
                String pkgName = "";
                boolean isInstalled = false;
                if (apkPackageInfo != null) {
                    pkgName = apkPackageInfo.packageName;

                    if (!TextUtils.isEmpty(pkgName)) {
                        isInstalled = SysUtil.isPkgInstalled(MyApplication.getContext(), pkgName);
                    }
                }

                icon = drawable;
                apkDownloadInfo.label = label;
                apkDownloadInfo.pkgName = pkgName;

                if (isInstalled) {
                    updateStatu(ApkDownloadStatusEnum.Installed);
                } else {
                    notifyChange();
                    updateDB();
                }
            }
        });
    }

    /**
     * 取消下载
     */
    void pause() {
        if (handler == null) {
            return;
        }

        handler.cancel();

        updateStatu(ApkDownloadStatusEnum.Paused);
    }

    /**
     * 安装成功
     */
    void installSuccessful() {
        updateStatu(ApkDownloadStatusEnum.Installed);
    }

    /**
     * 更新状态
     * 内部封装了自动通知监听者、更新数据库的逻辑
     */
    void updateStatu(ApkDownloadStatusEnum statu) {
        this.statu = statu;
        apkDownloadInfo.statu = statu.code;

        notifyChange();
        updateDB();
    }

    /**
     * 回收一个下载任务对象，释放所有资源和对象关联，并移除数据库对应的记录
     */
    void recycle() {
        statusChangeListeners.clear();

        pause();

        File apkFile = new File(apkDownloadInfo.getFilePath());
        if (apkFile.exists()) {
            apkFile.delete();
        }

        deleteDB();
    }

    /**
     * 新增数据库条目
     */
    void insertDB() {
        ApkDownloadManager.getInstance.apkDownloadInfoDao.insert(apkDownloadInfo);
    }

    /**
     * 更新数据库条目
     */
    void updateDB() {
        ApkDownloadManager.getInstance.apkDownloadInfoDao.update(apkDownloadInfo);
    }

    /**
     * 删除数据库条目
     */
    void deleteDB() {
        ApkDownloadManager.getInstance.apkDownloadInfoDao.delete(apkDownloadInfo);
    }

    /**
     * 通知所有监听者
     */
    private void notifyChange() {
        if (statusChangeListeners.isEmpty()) {
            return;
        }

        for (final StatusChangeListener item : statusChangeListeners) {
            if (item == null) {
                return;
            }

            ApkDownloadManager.getInstance.mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    item.onChange(ApkDownloadTask.this);
                }
            });
        }
    }

    public void addStatusChangeListener(StatusChangeListener statusChangeListener) {
        if (statusChangeListeners.contains(statusChangeListener)) {
            return;
        }

        statusChangeListeners.add(statusChangeListener);
    }

    public void removeStatusChangeListener(StatusChangeListener statusChangeListener) {
        statusChangeListeners.remove(statusChangeListener);
    }

    public ApkDownloadInfo getInfo() {
        return apkDownloadInfo;
    }

    public Drawable getIcon() {
        return icon;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public ApkDownloadStatusEnum getStatus() {
        return statu;
    }

    private void log(String logMsg) {
        Log.e(TAG, logMsg);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((apkDownloadInfo == null) ? 0 : apkDownloadInfo.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ApkDownloadTask other = (ApkDownloadTask) obj;
        if (apkDownloadInfo == null) {
            if (other.apkDownloadInfo != null) {
                return false;
            }
        } else if (!apkDownloadInfo.equals(other.apkDownloadInfo)) {
            return false;
        }
        return true;
    }
}
