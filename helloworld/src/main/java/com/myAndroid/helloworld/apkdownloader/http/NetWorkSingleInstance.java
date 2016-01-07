package com.myAndroid.helloworld.apkdownloader.http;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.File;


public enum NetWorkSingleInstance {
    getInstance;
    private final HttpUtils http = new HttpUtils();

    /**
     * 用XUtils请求下载
     */
    public HttpHandler downLoadApk(String url, String filePath,
            final IFileRequestHttpResponseListener httpResponseListener) {
        return new XUtilHandler(http.download(url, filePath, true, true, new RequestCallBack<File>() {
            @Override
            public void onSuccess(ResponseInfo<File> arg0) {
                httpResponseListener.onSuccess();
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                httpResponseListener.onProgress(current, total);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                httpResponseListener.onFailure(error.getExceptionCode(),
                        new Throwable(error == null ? "未知" : error.getLocalizedMessage()));
            }
        }));
    }

    //    private final AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
    //    {
    //        asyncHttpClient.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
    //        asyncHttpClient.setUserAgent(getUserAgent());
    //
    //        PersistentCookieStore cookieStore = new PersistentCookieStore(SecurityApplication.getAppContext());
    //        asyncHttpClient.setCookieStore(cookieStore);
    //        AsyncHttpClient.allowRetryExceptionClass(IOException.class);
    //        AsyncHttpClient.allowRetryExceptionClass(SocketTimeoutException.class);
    //        AsyncHttpClient.allowRetryExceptionClass(ConnectTimeoutException.class);
    //        AsyncHttpClient.blockRetryExceptionClass(UnknownHostException.class);
    //        AsyncHttpClient.blockRetryExceptionClass(ConnectionPoolTimeoutException.class);
    //    }
    //
    //    // 获取当前设备的UA信息
    //    public static synchronized String getUserAgent() {
    //        // app名称 : Hanmi
    //        String bundleName = "KalendsCheBao";
    //        // app当前版本号 : 1.1.2
    //        String version = "1.1.2";
    //        // 当前设备型号
    //        String platFormHardware = Build.MODEL + Build.VERSION.RELEASE;
    //        String platFormOSversion = "Android" + Build.VERSION.RELEASE;
    //        // HanmiBook_1.1.0-SNAPSHOT_MI 2S4.1.1_Android4.1.1
    //        String userAgent = bundleName + "_" + version + "_" + platFormHardware + "_" + platFormOSversion;
    //        return userAgent;
    //    }
    //
    //    /**
    //     * 用AsyncHttpClient请求下载
    //     */
    //    public HttpHandler downLoadApk(final String url, final File downLoadFile,
    //            final IFileRequestHttpResponseListener httpResponseListener) {
    //        // effective for java 38 检查参数有效性, 对于共有的方法, 要使用异常机制来通知调用方发生了入参错误.
    //        if (TextUtils.isEmpty(url)) {
    //            throw new NullPointerException("入参为空.");
    //        }
    //        RequestHandle requestHandle = null;
    //
    //        long seekPos = 0;
    //        asyncHttpClient.removeHeader("Range");
    //        if (downLoadFile.exists()) {
    //            // 需要断点续传
    //            if (downLoadFile.exists()) {
    //                asyncHttpClient.addHeader("Range", "bytes=" + downLoadFile.length() + "-");
    //                seekPos = downLoadFile.length();
    //            }
    //        } else {
    //            // 不需要断点续传时, 要删除之前的临时文件, 好重头进行下载
    //            seekPos = 0;
    //        }
    //
    //        try {
    //            final RandomAccessFileAsyncHttpResponseHandler fileAsyncHttpResponseHandler = new RandomAccessFileAsyncHttpResponseHandler(
    //                    downLoadFile, seekPos) {
    //
    //                @Override
    //                public void onSuccess(int statusCode, Header[] headers, File file) {
    //                    httpResponseListener.onSuccess();
    //                }
    //
    //                @Override
    //                public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
    //                    httpResponseListener.onFailure(statusCode, throwable);
    //                }
    //
    //                @Override
    //                public void onProgress(int bytesWritten, int totalSize) {
    //                    httpResponseListener.onProgress(bytesWritten, totalSize);
    //                }
    //            };
    //
    //            requestHandle = asyncHttpClient.get(url, new RequestParams(), fileAsyncHttpResponseHandler);
    //        } catch (Exception e) {
    //            e.printStackTrace();
    //        }
    //
    //        return new AysnchHttpClientHandler(requestHandle);
    //    }
}
