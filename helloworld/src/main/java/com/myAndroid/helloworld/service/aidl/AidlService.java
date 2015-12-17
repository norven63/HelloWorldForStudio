package com.myAndroid.helloworld.service.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.Toast;

/**
 * 1.注意，该Service在清单文件中定义时，将progress设置成了:myAidl。即另起一个名为myAidl的进程（是进程，不是线程）来运行该Service。
 * 2.绑定该Service时，使用的是隐式调用方式，即使用清单文件中，filter下的android:name属性（本例是helloworld.demo.adilService）
 * 3.调用前，记得将对应aidl文件拷贝至客户端程序中
 */
public class AidlService extends Service {
    public AidlService() {
    }

    private IMyAidl.Stub myAidl = new IMyAidl.Stub() {
        @Override
        public AidlBean getBean(int intA, int intB, String StringA, String StringB, int intC) throws RemoteException {
            return new AidlBean(intA, intB, StringA, StringB, intC);
        }

        @Override
        public void showMsg(String msg) throws RemoteException {
            //注意：这里不是UI线程，无法执行下面的UI代码
            Toast.makeText(AidlService.this, "来自AidlService的消息：" + msg, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return myAidl;
    }
}
