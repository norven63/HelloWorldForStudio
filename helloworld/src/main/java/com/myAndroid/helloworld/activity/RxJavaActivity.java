package com.myAndroid.helloworld.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.myAndroid.helloworld.R;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

public class RxJavaActivity extends Activity {
    public class LiftAllTransformer implements Observable.Transformer<View, String> {
        @Override
        public Observable<String> call(Observable<View> observable) {
            return observable.lift(new Observable.Operator<String, View>() {//Operator的泛型类型顺序，和Transformer以及map的泛型类型顺序正好相反
                @Override
                public Subscriber<? super View> call(Subscriber<? super String> subscriber) {
                    return null;
                }
            });
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java);

        Observable<String> observable = Observable.just("a","b");
        observable.map(new Func1<String, Object>() {
            @Override
            public Object call(String s) {
                return null;
            }
        });
    }
}
