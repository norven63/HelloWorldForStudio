package com.myAndroid.helloworld.apkdownloader.ui;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.myAndroid.helloworld.MyApplication;
import com.myAndroid.helloworld.R;
import com.myAndroid.helloworld.adapter.MyBaseAdapter;
import com.myAndroid.helloworld.apkdownloader.model.ApkDownloadStatusEnum;
import com.myAndroid.helloworld.apkdownloader.model.ApkDownloadTask;

import java.util.List;


public class ApkDownloadAdapter extends MyBaseAdapter<ApkDownloadTask> {
    public ApkDownloadAdapter(List<ApkDownloadTask> dataSource) {
        super(dataSource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.apk_download_list_item, null);
        }

        final ApkDownloadTask item = getItem(position);

        refreshView(convertView, item);

        /*
         * 注册监听：
         * 1.先判断当前convertView是否已经绑定过某个监听者，如果没有，则构建一个新的监听者对象并塞入tag中
         * 2.如果已经绑定过一个监听者，则复用该对象，并调用其注册接口
         */
        AbsrtactStatusChangeListener statusChangeListener = (AbsrtactStatusChangeListener) convertView.getTag();
        final View convertViewCP = convertView;
        if (statusChangeListener == null) {
            statusChangeListener = new AbsrtactStatusChangeListener() {
                @Override
                public void onChange(ApkDownloadTask apkDownloadTask) {
                    refreshView(convertViewCP, apkDownloadTask);
                }
            };

            convertView.setTag(statusChangeListener);
        }

        statusChangeListener.registerStatusListener(item);

        return convertView;
    }

    /**
     * @author ZhouXinXing
     * @file ApkDownloadAdapter.java
     * @date 2015年12月23日 下午8:54:31
     * @Description 当下载条目的操作按钮被点击时的回调接口
     */
    public static interface OnItemBtnClickListener {
        /**
         * 点击暂停
         */
        public void onPauseClick(ApkDownloadTask apkDownloadTask);

        /**
         * 点击开始、重新下载
         */
        public void onStartClick(ApkDownloadTask apkDownloadTask);

        /**
         * 点击安装
         */
        public void onInstallClick(ApkDownloadTask apkDownloadTask);

        /**
         * 点击删除
         */
        public void onRemoveClick(ApkDownloadTask apkDownloadTask);

        /**
         * 点击打开已安装的app
         */
        public void onOpenClick(ApkDownloadTask apkDownloadTask);
    }

    /**
     * @author ZhouXinXing
     * @file ApkDownloadAdapter.java
     * @date 2015年12月25日 上午11:34:10
     * @Description 为解决因ListView控件复用特性而导致的重复监听下载任务状态更新的问题。
     */
    private static abstract class AbsrtactStatusChangeListener implements ApkDownloadTask.StatusChangeListener {
        private ApkDownloadTask apkDownloadTask;

        /**
         * 注册监听
         * 需要先注销解绑已有的监听对象，才可以注册新的
         */
        void registerStatusListener(ApkDownloadTask apkDownloadTask) {
            unRegisterStatusListener();

            this.apkDownloadTask = apkDownloadTask;

            this.apkDownloadTask.addStatusChangeListener(this);
        }

        /**
         * 注销解绑旧的监听
         */
        void unRegisterStatusListener() {
            if (apkDownloadTask == null) {
                return;
            }

            apkDownloadTask.removeStatusChangeListener(this);
        }
    }

    private OnItemBtnClickListener onItemBtnClickListener;

    public void setOnItemBtnClickListener(OnItemBtnClickListener onItemBtnClickListener) {
        this.onItemBtnClickListener = onItemBtnClickListener;
    }

    private void refreshView(View convertView, final ApkDownloadTask apkDownloadTask) {
        TextView labelTextView = (TextView) convertView.findViewById(R.id.app_label);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.app_icon);
        Button button = (Button) convertView.findViewById(R.id.app_btn);
        TextView statusTextView = (TextView) convertView.findViewById(R.id.app_status);
        ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.app_progressBar);

        if (apkDownloadTask.getIcon() != null) {
            imageView.setImageDrawable(apkDownloadTask.getIcon());
        } else {
            imageView.setImageResource(R.drawable.ic_launcher);
        }

        if (!TextUtils.isEmpty(apkDownloadTask.getInfo().getLabel())) {
            labelTextView.setText(apkDownloadTask.getInfo().getLabel());
        } else {
            labelTextView.setText("Unknow");
        }

        if (apkDownloadTask.getStatus() == ApkDownloadStatusEnum.Downloading) {
            statusTextView.setText(apkDownloadTask.getInfo().getProgress() + "%");
        } else {
            statusTextView.setText(apkDownloadTask.getStatus().getStatusDesc());
        }

        if (apkDownloadTask.getStatus() == ApkDownloadStatusEnum.Failed || apkDownloadTask.getInfo().getProgress() <= 0) {
            progressBar.setIndeterminate(true);
        } else {
            progressBar.setIndeterminate(false);
            progressBar.setProgress(apkDownloadTask.getInfo().getProgress());
        }

        button.setText(apkDownloadTask.getStatus().getBtnDesc());
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBtnClick(apkDownloadTask);
            }
        });

        convertView.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemBtnClickListener == null) {
                    return false;
                }

                onItemBtnClickListener.onRemoveClick(apkDownloadTask);

                return false;
            }
        });
    }

    private void onBtnClick(ApkDownloadTask item) {
        if (onItemBtnClickListener == null) {
            return;
        }

        switch (item.getStatus()) {
            case Downloading:
                onItemBtnClickListener.onPauseClick(item);

                break;
            case Failed:
                onItemBtnClickListener.onStartClick(item);

                break;
            case Installed:
                onItemBtnClickListener.onOpenClick(item);

                break;
            case Paused:
                onItemBtnClickListener.onStartClick(item);

                break;
            case Succeed:
                onItemBtnClickListener.onInstallClick(item);

                break;
            default:
                break;
        }
    }
}