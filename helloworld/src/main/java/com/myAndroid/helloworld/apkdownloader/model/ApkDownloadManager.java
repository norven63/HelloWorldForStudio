package com.myAndroid.helloworld.apkdownloader.model;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.myAndroid.helloworld.apkdownloader.model.db.ApkDownloadInfoDao;
import com.myAndroid.helloworld.apkdownloader.model.db.DaoMaster;
import com.myAndroid.helloworld.apkdownloader.model.db.DaoSession;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * @author ZhouXinXing
 * @file ApkDownloadModelManager.java
 * @date 2015年12月23日 下午2:30:49
 * @Description 下载单元管理类，单例. 封装了下载单元的操作接口，并持有一个全局的任务列表
 */
public enum ApkDownloadManager {
    getInstance;

    public final static long NEGATIVE_SECONDS = 10;//判断是否为长期未更新任务的阈值，单位：秒

    final Handler mainHandler = new Handler(Looper.getMainLooper());

    ApkDownloadInfoDao apkDownloadInfoDao;

    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private List<ApkDownloadTask> apkDownloadTasks = new ArrayList<ApkDownloadTask>();
    private List<ApkDownloadTaskListChangeListener> apkDownloadTaskListChangeListeners = new ArrayList<ApkDownloadTaskListChangeListener>();

    public interface ApkDownloadTaskListChangeListener {
        public void onAdd(ApkDownloadTask apkDownloadTask);

        public void onRemove(ApkDownloadTask apkDownloadTask);
    }

    /**
     * 读取本地数据进行初始化工作
     */
    public void init(Context context) {
        if (!apkDownloadTasks.isEmpty()) {
            return;
        }

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "apkDownloadInfo-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();

        apkDownloadInfoDao = daoSession.getApkDownloadInfoDao();

        QueryBuilder<ApkDownloadInfo> qb = apkDownloadInfoDao.queryBuilder();
        List<ApkDownloadInfo> apkDownloadInfoDbDatas = qb.list();

        for (ApkDownloadInfo item : apkDownloadInfoDbDatas) {
            File apkFile = new File(item.filePath);

            do {
                if (!apkFile.exists()) {
                    //下载文件不存
                    item.progress = 0;
                    item.statu = ApkDownloadStatusEnum.Paused.code;

                    break;
                }

                if (item.total == null && item.total == 0) {
                    //文件的总大小未获取到

                    break;
                }

                if (apkFile.length() >= item.total) {
                    //文件已下载完成
                    item.progress = 100;
                    item.statu = ApkDownloadStatusEnum.Succeed.code;

                    break;
                }

                if (apkFile.length() < item.total) {
                    //根据现有文件大小，设置下载进度值
                    item.progress = (int) (apkFile.length() * 100 / item.total);
                    item.statu = ApkDownloadStatusEnum.Paused.code;

                    break;
                }
            } while (false);

            apkDownloadTasks.add(new ApkDownloadTask(item));
        }
    }

    public List<ApkDownloadTask> getApkDownloadTasks() {
        ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();
        try {
            readLock.lock();

            return apkDownloadTasks;
        } finally {
            readLock.unlock();
        }
    }

    /**
     * 启动一个下载任务
     */
    public void startApkDownloadTask(ApkDownloadTask apkDownloadTask) {
        if (!canTaskStart(apkDownloadTask)) {
            return;
        }

        ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();
        try {
            writeLock.lock();

            //防止重复添加
            if (apkDownloadTasks.contains(apkDownloadTask)) {
                apkDownloadTask = apkDownloadTasks.get(apkDownloadTasks.indexOf(apkDownloadTask));

                apkDownloadTask.updateStatu(ApkDownloadStatusEnum.Downloading);
            } else {
                apkDownloadTasks.add(apkDownloadTask);

                apkDownloadTask.insertDB();
            }

            apkDownloadTask.start();

            notifyAdd(apkDownloadTask);
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * 移除一个下载任务
     */
    public void removeApkDownloadTask(ApkDownloadTask apkDownloadTask) {
        ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();
        try {
            writeLock.lock();

            apkDownloadTask.recycle();

            apkDownloadTasks.remove(apkDownloadTask);

            notifyRemove(apkDownloadTask);
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * 暂停一个下载任务
     */
    public void pauseTask(ApkDownloadTask apkDownloadTask) {
        apkDownloadTask.pause();
    }

    /**
     * 暂停所有任务(仅限正在下载的任务)
     */
    public void cancelAllTasks() {
        ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();
        try {
            readLock.lock();

            if (apkDownloadTasks.isEmpty()) {
                return;
            }

            for (ApkDownloadTask element : apkDownloadTasks) {
                if (element == null) {
                    continue;
                }

                if (element.getStatus() == ApkDownloadStatusEnum.Downloading) {
                    element.pause();
                }
            }
        } finally {
            readLock.unlock();
        }
    }

    /**
     * 重新开始所有任务(仅限暂停、下载失败的任务)
     */
    public void resumeAllTasks() {
        ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();
        try {
            readLock.lock();

            if (apkDownloadTasks.isEmpty()) {
                return;
            }

            for (ApkDownloadTask element : apkDownloadTasks) {
                if (element == null) {
                    continue;
                }

                if (element.getStatus() == ApkDownloadStatusEnum.Paused
                        || element.getStatus() == ApkDownloadStatusEnum.Failed) {

                    element.updateStatu(ApkDownloadStatusEnum.Downloading);

                    element.start();

                    //此处事件通知的主要意义，其实是给Notifaction添加一条新通知之用
                    notifyAdd(element);
                }
            }
        } finally {
            readLock.unlock();
        }
    }

    private void notifyAdd(final ApkDownloadTask apkDownloadTask) {
        if (apkDownloadTaskListChangeListeners.isEmpty()) {
            return;
        }

        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                for (ApkDownloadTaskListChangeListener element : apkDownloadTaskListChangeListeners) {
                    if (element == null) {
                        continue;
                    }

                    element.onAdd(apkDownloadTask);
                }
            }
        });
    }

    private void notifyRemove(final ApkDownloadTask apkDownloadTask) {
        if (apkDownloadTaskListChangeListeners.isEmpty()) {
            return;
        }

        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                for (ApkDownloadTaskListChangeListener element : apkDownloadTaskListChangeListeners) {
                    if (element == null) {
                        continue;
                    }

                    element.onRemove(apkDownloadTask);
                }
            }
        });
    }

    public void addTaskListChangeListener(ApkDownloadTaskListChangeListener dataChangeListener) {
        if (apkDownloadTaskListChangeListeners.contains(dataChangeListener)) {
            return;
        }

        apkDownloadTaskListChangeListeners.add(dataChangeListener);
    }

    public void removeTaskListChangeListener(ApkDownloadTaskListChangeListener dataChangeListener) {
        apkDownloadTaskListChangeListeners.remove(dataChangeListener);
    }

    /**
     * 判断某个下载任务是否允许执行
     */
    public boolean canTaskStart(ApkDownloadTask apkDownloadTask) {
        if (apkDownloadTask == null) {
            return false;
        }

        ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();
        try {
            readLock.lock();

            if (!apkDownloadTasks.contains(apkDownloadTask)) {
                return true;
            }

            apkDownloadTask = apkDownloadTasks.get(apkDownloadTasks.indexOf(apkDownloadTask));//复用当前列表中的已有对象

            if (apkDownloadTask.getStatus() == ApkDownloadStatusEnum.Paused
                    || apkDownloadTask.getStatus() == ApkDownloadStatusEnum.Failed) {

                return true;
            }
        } finally {
            readLock.unlock();
        }

        return false;
    }

    /**
     * 判断是否还有任务在下载
     * 状态为下载中但最新的更新时间超过60秒的任务，记为长时间没有更新的任务，不能被算作下载中的任务
     */
    public boolean hasTaskDownLoading() {
        if (apkDownloadTasks.isEmpty()) {
            return false;
        }

        boolean hasTaskDownLoading = false;
        ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();
        try {
            readLock.lock();

            long now = new Date().getTime();
            for (ApkDownloadTask element : apkDownloadTasks) {
                if (element == null) {
                    continue;
                }

                if (element.getStatus() == ApkDownloadStatusEnum.Downloading
                        && now - element.getLastUpdateTime() < NEGATIVE_SECONDS * 1000) {

                    hasTaskDownLoading = true;

                    break;
                }
            }
        } finally {
            readLock.unlock();
        }

        return hasTaskDownLoading;
    }

    /**
     * 监听app安装成功
     * 将匹配的任务状态设置为已安装
     */
    public void installNewApp(String pkgName) {
        if (TextUtils.isEmpty(pkgName)) {
            return;
        }

        ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();
        try {
            readLock.lock();

            for (ApkDownloadTask element : apkDownloadTasks) {
                if (element == null) {
                    continue;
                }

                if (element.statu != ApkDownloadStatusEnum.Succeed) {
                    //非下载完成的任务，不需要修改状态
                    continue;
                }

                String apkPkgName = element.getInfo().pkgName;

                if (apkPkgName != null && apkPkgName.equals(pkgName)) {
                    element.installSuccessful();
                }
            }
        } finally {
            readLock.unlock();
        }
    }

    /**
     * 返回一个正在下载的任务给通知栏
     */
    public ApkDownloadTask getOneRunningTaskForNotification(Collection<ApkDownloadTask> remainProgressTasks) {
        ApkDownloadTask task = null;

        ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();
        try {
            readLock.lock();

            if (apkDownloadTasks.isEmpty()) {
                return task;
            }

            for (ApkDownloadTask element : apkDownloadTasks) {
                if (element == null) {
                    continue;
                }

                if (remainProgressTasks.contains(element)) {
                    //如果该任务已经在通知栏中显示，则不取
                    continue;
                }

                if (element.statu == ApkDownloadStatusEnum.Downloading) {
                    task = element;

                    break;
                }
            }
        } finally {
            readLock.unlock();
        }

        return task;
    }
}
