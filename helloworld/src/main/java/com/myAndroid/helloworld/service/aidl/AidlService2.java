package com.myAndroid.helloworld.service.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

public class AidlService2 extends Service {
    public AidlService2() {
    }

    private IMySecondAidl.Stub mySecondAidl = new IMySecondAidl.Stub() {
        @Override
        public int getInt() throws RemoteException {
            return 0;
        }

        @Override
        public boolean getBoolean() throws RemoteException {
            return false;
        }

        @Override
        public String getString() throws RemoteException {
            return "AidlService2.getString() has been called!";
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return mySecondAidl;
    }
}
