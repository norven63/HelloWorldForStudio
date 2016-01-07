package com.myAndroid.helloworld.apkdownloader.ui;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.RemoteViews;

import com.myAndroid.helloworld.MyApplication;
import com.myAndroid.helloworld.R;
import com.myAndroid.helloworld.apkdownloader.model.ApkDownloadManager;
import com.myAndroid.helloworld.apkdownloader.model.ApkDownloadTask;

import java.util.concurrent.ConcurrentHashMap;

public class ApkDownloadNotificationCenter {
    private static final int MAX_PROGRESS = 100;

    public static ApkDownloadNotificationCenter instance = null;

    private NotificationManagerCompat mNotificationManagerCompat;
    private NotificationCompat.Builder mBuilder;

    private ConcurrentHashMap<ApkDownloadTask, Notification> progressNotifications;//缓存显示进度的通知

    private ApkDownloadTask.StatusChangeListener statusChangeListener;//监听下载任务单元更新
    private ApkDownloadManager.ApkDownloadTaskListChangeListener apkDownloadTaskListChangeListener;//监听下载任务队列的变化

    public static ApkDownloadNotificationCenter getInstance() {
        if (instance == null) {
            instance = new ApkDownloadNotificationCenter();
        }

        return instance;
    }

    private ApkDownloadNotificationCenter() {
        mNotificationManagerCompat = NotificationManagerCompat.from(MyApplication.getContext());
        mBuilder = new NotificationCompat.Builder(MyApplication.getContext());

        progressNotifications = new ConcurrentHashMap<ApkDownloadTask, Notification>();

        statusChangeListener = new ApkDownloadTask.StatusChangeListener() {
            @Override
            public void onChange(ApkDownloadTask apkDownloadTask) {
                if (apkDownloadTask == null) {
                    return;
                }

                switch (apkDownloadTask.getStatus()) {
                    case Succeed:
                        downloadSuccess(apkDownloadTask);

                        break;
                    case Installed:
                        downloadSuccess(apkDownloadTask);

                        break;
                    case Downloading:
                        updateProgress(apkDownloadTask);

                        break;
                    case Paused:
                        cancel(apkDownloadTask);

                        break;
                    case Failed:
                        cancel(apkDownloadTask);

                        break;
                    default:
                        break;
                }
            }
        };

        apkDownloadTaskListChangeListener = new ApkDownloadManager.ApkDownloadTaskListChangeListener() {
            @Override
            public void onAdd(ApkDownloadTask apkDownloadTask) {
                if (apkDownloadTask == null) {
                    return;
                }

                show(apkDownloadTask);
            }

            @Override
            public void onRemove(ApkDownloadTask apkDownloadTask) {
                if (apkDownloadTask == null) {
                    return;
                }

                cancel(apkDownloadTask);
            }
        };
    }

    /**
     * 注册下载任务队列监听
     */
    public void init() {
        ApkDownloadManager.getInstance.addTaskListChangeListener(apkDownloadTaskListChangeListener);
    }

    /**
     * 显示通知，下载开始调用
     * 最多只显示两条进度
     */
    public void show(ApkDownloadTask apkDownloadTask) {
        if (!progressNotifications.containsKey(apkDownloadTask) && progressNotifications.size() < 2) {
            String text = "downloading...";
            String title = TextUtils.isEmpty(apkDownloadTask.getInfo().getLabel()) ? "Unkown" : apkDownloadTask
                    .getInfo().getLabel();

            Notification notification = buildNotification(title, text);
            notification.flags = Notification.FLAG_NO_CLEAR;

            int progress = apkDownloadTask.getInfo().getProgress();
            if (progress <= 0 || progress > 100) {
                notification.contentView.setProgressBar(R.id.progress, MAX_PROGRESS, progress, true);
            } else {
                notification.contentView.setProgressBar(R.id.progress, MAX_PROGRESS, progress, false);
            }
            notification.contentView.setTextViewText(R.id.progress_des, progress + "%");

            progressNotifications.put(apkDownloadTask, notification);
            mNotificationManagerCompat.notify(apkDownloadTask.getInfo().getTaskId(), notification);
        }
    }

    /**
     * 更新下载进度
     */
    public void updateProgress(ApkDownloadTask apkDownloadTask) {
        if (progressNotifications.containsKey(apkDownloadTask)) {
            String title = "Download: "
                    + (TextUtils.isEmpty(apkDownloadTask.getInfo().getLabel()) ? "Unkown" : apkDownloadTask.getInfo()
                    .getLabel());

            Notification notification = progressNotifications.get(apkDownloadTask);
            notification.flags = Notification.FLAG_NO_CLEAR;

            notification.contentView.setViewVisibility(R.id.progress, View.VISIBLE);
            notification.contentView.setViewVisibility(R.id.progress_des, View.VISIBLE);
            notification.contentView.setViewVisibility(R.id.des, View.GONE);
            notification.contentView.setTextViewText(R.id.title, title);

            int progress = apkDownloadTask.getInfo().getProgress();
            if (progress <= 0 || progress > 100) {
                notification.contentView.setProgressBar(R.id.progress, MAX_PROGRESS, progress, true);
            } else {
                notification.contentView.setProgressBar(R.id.progress, MAX_PROGRESS, progress, false);
            }
            notification.contentView.setTextViewText(R.id.progress_des, progress + "%");

            mNotificationManagerCompat.notify(apkDownloadTask.getInfo().getTaskId(), notification);
        }
    }

    /**
     * 下载成功
     */
    private void downloadSuccess(ApkDownloadTask apkDownloadTask) {
        String text = "Click to install";
        String title = "Download: "
                + (TextUtils.isEmpty(apkDownloadTask.getInfo().getLabel()) ? "Unkown" : apkDownloadTask.getInfo()
                .getLabel());

        Notification notification;
        if (progressNotifications.containsKey(apkDownloadTask)) {
            //如果是当前显示进度的通知，则复用该对象，然后重新显示一条正在下载中的任务
            notification = progressNotifications.get(apkDownloadTask);

            progressNotifications.remove(apkDownloadTask);

            ApkDownloadTask oneTask = ApkDownloadManager.getInstance
                    .getOneRunningTaskForNotification(progressNotifications.keySet());
            if (oneTask != null) {
                show(oneTask);
            }
        } else {
            notification = buildNotification(title, text);
        }

        notification.flags = Notification.FLAG_AUTO_CANCEL;

        notification.contentView.setViewVisibility(R.id.progress, View.GONE);
        notification.contentView.setViewVisibility(R.id.progress_des, View.GONE);
        notification.contentView.setViewVisibility(R.id.des, View.VISIBLE);
        notification.contentView.setTextViewText(R.id.title, title);
        notification.contentView.setTextViewText(R.id.des, text);

        if (apkDownloadTask.getIcon() != null) {
            notification.contentView.setBitmap(R.id.app_icon, "setImageBitmap",
                    getBitmapFrowDrawable(apkDownloadTask.getIcon()));
        }

        mNotificationManagerCompat.notify(apkDownloadTask.getInfo().getTaskId(), notification);
    }

    private Bitmap getBitmapFrowDrawable(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof NinePatchDrawable) {
            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                    drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            return bitmap;
        } else {
            return null;
        }
    }

    /**
     * 在取消某个下载任务时调用，将对应的通知关闭
     */
    private void cancel(ApkDownloadTask apkDownloadTask) {
        if (progressNotifications.containsKey(apkDownloadTask)) {
            progressNotifications.remove(apkDownloadTask);
            mNotificationManagerCompat.cancel(apkDownloadTask.getInfo().getTaskId());

            ApkDownloadTask oneTask = ApkDownloadManager.getInstance
                    .getOneRunningTaskForNotification(progressNotifications.keySet());
            if (oneTask != null) {
                show(oneTask);
            }
        }
    }

    /**
     * 关闭所有通知
     */
    public void cancelAll() {
        progressNotifications.clear();
        mNotificationManagerCompat.cancelAll();
    }

    private RemoteViews getRemoteViews(CharSequence title, CharSequence des) {
        RemoteViews contentView = new RemoteViews(MyApplication.getContext().getPackageName(),
                R.layout.apk_download_notification_layout);
        contentView.setTextViewText(R.id.title, title);
        contentView.setTextViewText(R.id.des, des);

        return contentView;
    }

    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(MyApplication.getContext(), ApkDownloadActivity.class);
        return PendingIntent.getActivity(MyApplication.getContext(), 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private Notification buildNotification(String title, String text) {
        mBuilder.setSmallIcon(R.drawable.ic_notification);

        if (Build.VERSION.SDK_INT >= 20) {
            mBuilder.setPriority(NotificationCompat.PRIORITY_MAX);
        } else {
            mBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }

        if (Build.VERSION.SDK_INT >= 14) {
            mBuilder.setProgress(0, 0, true);
        }

        mBuilder.setWhen(System.currentTimeMillis());
        mBuilder.setContentIntent(getPendingIntent());

        Notification notification = mBuilder.build();
        notification.contentView = getRemoteViews(title, text);

        return notification;
    }

    public ApkDownloadTask.StatusChangeListener getStatusChangeListener() {
        return statusChangeListener;
    }
}
