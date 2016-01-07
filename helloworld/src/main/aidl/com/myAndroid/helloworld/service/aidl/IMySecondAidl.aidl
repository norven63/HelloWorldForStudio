// IMySecondAidl.aidl
package com.myAndroid.helloworld.service.aidl;

// Declare any non-default types here with import statements
// 注意，只要重新编译项目，即可在Java源码中引入IMySecondAidl，因为编译器自动生成了IMySecondAidl的Java源文件
interface IMySecondAidl {
    int getInt();

    boolean getBoolean();

    String getString();
}
