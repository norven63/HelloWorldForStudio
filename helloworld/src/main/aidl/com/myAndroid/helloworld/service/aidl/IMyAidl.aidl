// IMyAidl.aidl
package com.myAndroid.helloworld.service.aidl;

import com.myAndroid.helloworld.service.aidl.AidlBean;

// Declare any non-default types here with import statements
// 注意，只要重新编译项目，即可在Java源码中引入IMyAidl，因为编译器自动生成了IMyAidl的Java源文件
interface IMyAidl {
    AidlBean getBean(int intA,int intB,String StringA,String StringB,int intC);

    void showMsg(String msg);
}
