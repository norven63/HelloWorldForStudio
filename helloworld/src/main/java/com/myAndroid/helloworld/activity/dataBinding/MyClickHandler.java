package com.myAndroid.helloworld.activity.dataBinding;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.myAndroid.helloworld.BR;
import com.myAndroid.helloworld.MyApplication;

/**
 * @author ZhouXinXing
 * @date 2016年01月08日 11:25
 * @Description
 */
public class MyClickHandler {
    private final User user;

    public MyClickHandler(User user) {
        this.user = user;
    }

    public void onClickFriend(View view) {
        user.setFirstName("change-Friend-fisrtNmae");
        user.setLastName("change-Friend-lastName");
        user.setIsFriend(false);
    }

    public void onClickEnemy(View view) {
        user.setFirstName("change-Enemy-fisrtNmae");
        user.setLastName("change-Enemy-lastName");
        user.setIsFriend(true);
    }
}
