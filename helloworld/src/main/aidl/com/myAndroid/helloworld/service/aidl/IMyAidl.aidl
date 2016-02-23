// IMyAidl.aidl
package com.myAndroid.helloworld.service.aidl;

import com.myAndroid.helloworld.service.aidl.AidlBean;

// 1.只要重新编译项目，即可在Java源码中引入IMySecondAidl，因为编译器自动生成了IMySecondAidl的Java源文件到gen目录下
// 2.如果想把.aidl文件变成.java文件提供给调用者，可以使用library项目来打包，在bin目录下生成一个jar包，里面会自动将.adil文件转成.java文件并放入到对应包目录下
interface IMyAidl {
    AidlBean getBean(int intA,int intB,String StringA,String StringB,int intC);

    void showMsg(String msg);
}
