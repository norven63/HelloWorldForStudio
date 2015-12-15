// IMyAidl.aidl
package com.myAndroid.helloworld.service.aidl;

import com.myAndroid.helloworld.service.aidl.AidlBean;

// Declare any non-default types here with import statements

interface IMyAidl {
    AidlBean getBean(int intA,int intB,String StringA,String StringB,int intC);

    void showMsg(String msg);
}
