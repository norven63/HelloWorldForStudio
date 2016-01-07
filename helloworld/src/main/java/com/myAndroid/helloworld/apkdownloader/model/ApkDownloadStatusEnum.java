package com.myAndroid.helloworld.apkdownloader.model;


import com.myAndroid.helloworld.MyApplication;
import com.myAndroid.helloworld.R;

public enum ApkDownloadStatusEnum {
    //下载中
    Default(0, R.string.apk_download_status_default, R.string.apk_download_btn_default),
    //下载中
    Downloading(1, R.string.apk_download_status_downloading, R.string.apk_download_btn_downloading),
    //已暂停
    Paused(2, R.string.apk_download_status_paused, R.string.apk_download_btn_paused),
    //下载完成
    Succeed(3, R.string.apk_download_status_succeed, R.string.apk_download_btn_succeed),
    //下载失败
    Failed(4, R.string.apk_download_status_failed, R.string.apk_download_btn_failed),
    //正在安装
    Installing(5, R.string.apk_download_status_installing, R.string.apk_download_btn_installing),
    //已安装
    Installed(6, R.string.apk_download_status_installed, R.string.apk_download_btn_installed);

    public final int code;
    private final int statusDesc;
    private final int btnDesc;

    private ApkDownloadStatusEnum(int code, int statusDesc, int btnDesc) {
        this.code = code;
        this.statusDesc = statusDesc;
        this.btnDesc = btnDesc;
    }

    public static ApkDownloadStatusEnum getEnumByCode(int code) {
        ApkDownloadStatusEnum returnValue = Default;

        for (ApkDownloadStatusEnum item : values()) {
            if (item.code == code) {
                returnValue = item;

                break;
            }
        }

        return returnValue;
    }

    public String getStatusDesc() {
        return MyApplication.getContext().getResources().getString(statusDesc);
    }

    public String getBtnDesc() {
        return MyApplication.getContext().getResources().getString(btnDesc);
    }
}
