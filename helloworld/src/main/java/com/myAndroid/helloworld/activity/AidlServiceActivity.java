package com.myAndroid.helloworld.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.Button;
import android.widget.TextView;

import com.myAndroid.helloworld.R;
import com.myAndroid.helloworld.service.aidl.AidlBean;
import com.myAndroid.helloworld.service.aidl.IMyAidl;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AidlServiceActivity extends Activity {
    private IMyAidl myAidl;

    @Bind(R.id.getBean)
    Button getBean;
    @Bind(R.id.showMsg)
    Button showMsg;
    @Bind(R.id.content_textView)
    TextView contentTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_aidl_service);
        ButterKnife.bind(this);

        Intent intent = new Intent("helloworld.demo.adilService");

        /**
         * 注意此处的getPackageName()返回的值，如果被其他的app绑定Service时，Intent在setPackage需要传入这个值。
         * 此处示例的值为your.application.myDemoA.id而非清单文件中配置的com.myAndroid.helloworld,是因为在gradle配置中申明了productFlavors函数（可以打出不同的包名的apk文件）
         */
        intent.setPackage(getPackageName());

        bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                myAidl = IMyAidl.Stub.asInterface(service);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                myAidl = null;
            }
        }, BIND_AUTO_CREATE);
    }

    @OnClick(R.id.getBean)
    public void getBean() {
        if (myAidl != null) {
            try {
                AidlBean aidlBean = myAidl.getBean(1, 2, "A", "B", 3);
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("intA: " + aidlBean.getIntA());
                stringBuffer.append("\nintB: " + aidlBean.getIntB());
                stringBuffer.append("\nintC: " + aidlBean.getIntC());
                stringBuffer.append("\nStringA: " + aidlBean.getStringA());
                stringBuffer.append("\nStringB: " + aidlBean.getStringB());
                contentTextView.setText(stringBuffer.toString());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick(R.id.showMsg)
    public void showMsg() {
        if (myAidl != null) {
            try {
                myAidl.showMsg("Test msg.");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
