package com.myAndroid.helloworld.apkdownloader.http;

public interface IFileRequestHttpResponseListener {
    /**
     * 请求文件成功
     *            服务器返回的信息
     */
    public void onSuccess();

    /**
     * 请求失败
     *
     * @param statusCode
     * @param e
     */
    public void onFailure(int statusCode, Throwable e);

    /**
     * 文件上传/下载进度
     *
     * @param bytesWritten
     * @param totalSize
     */
    public void onProgress(long bytesWritten, long totalSize);
}
