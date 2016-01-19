package com.myAndroid.helloworld.activity.dataBinding;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.myAndroid.helloworld.BR;

/**
 * @author ZhouXinXing
 * @date 2016年01月08日 11:21
 * @Description 继承自BaseObservable，实现通知监听者值更改的功能。
 * 在需要属性变更的get方法中使用注解@Bindable，然后在set方法中调用notifyPropertyChanged进行通知
 * 此外DataBinding还支持集合的监听绑定，详情可以参阅官方文档Observable Collections章节
 */
public class User extends BaseObservable {
    private String firstName;
    private String lastName;
    private boolean isFriend;

    public User(String firstName, String lastName, boolean isFriend) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.isFriend = isFriend;
    }

    @Bindable
    public String getFirstName() {
        return firstName;
    }

    @Bindable
    public String getLastName() {
        return lastName;
    }

    @Bindable
    public boolean isFriend() {
        return isFriend;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        notifyPropertyChanged(BR.firstName);
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        notifyPropertyChanged(BR.lastName);
    }

    public void setIsFriend(boolean isFriend) {
        this.isFriend = isFriend;
        notifyPropertyChanged(BR.friend);
    }
}
