package com.myAndroid.helloworld.apkdownloader.http;

public class XUtilHandler implements HttpHandler {
    private final com.lidroid.xutils.http.HttpHandler<?> httpHanlder;

    public XUtilHandler(com.lidroid.xutils.http.HttpHandler<?> httpHanlder) {
        this.httpHanlder = httpHanlder;
    }

    @Override
    public void cancel() {
        httpHanlder.cancel(true);
    }

    @Override
    public boolean isCanceled() {
        return httpHanlder.isCancelled();
    }
}
