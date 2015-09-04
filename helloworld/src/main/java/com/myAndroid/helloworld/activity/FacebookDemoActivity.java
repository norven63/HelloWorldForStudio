package com.myAndroid.helloworld.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.Arrays;

/**
 * Created by Norven on 2015/5/6 0006.
 */
public class FacebookDemoActivity extends Activity {
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                loginResult.getRecentlyDeniedPermissions();//获取被拒绝的权限
                loginResult.getRecentlyGrantedPermissions();//获取被授予的权限
                loginResult.getAccessToken().getUserId();
                Toast.makeText(FacebookDemoActivity.this, "onSuccess", Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onCancel() {
                Toast.makeText(FacebookDemoActivity.this, "onCancel", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(FacebookDemoActivity.this, "onError", Toast.LENGTH_SHORT).show();
            }
        });

        LoginManager.getInstance().logInWithPublishPermissions(this, Arrays.asList("publish_actions"));
//        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends","publish_actions"));
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
