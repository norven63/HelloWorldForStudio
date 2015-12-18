package com.myAndroid.helloworld.activity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.myAndroid.helloworld.R;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;


public class GlideActivity extends Activity {
    private final int PERMISSION_CODE_WRITE_EXTERNAL_STORAGE = 123;//申请读写SD卡权限的请求码（读、写两个权限，申请任意其一可得其二，二者属于同一权限组）

    @Bind(R.id.imageView)
    ImageView imageView;
    @Bind(R.id.imageView_2)
    ImageView imageView2;
    @Bind(R.id.imageView_3)
    ImageView imageView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_glide);
        ButterKnife.bind(this);

        Glide.with(this).load("http://img3.imgtn.bdimg.com/it/u=533786160,1852532877&fm=21&gp=0.jpg").centerCrop().placeholder(R.drawable.ic_launcher).crossFade().into(imageView);
        Glide.with(this).load("http://a.hiphotos.baidu.com/image/w%3D310/sign=be191b5ef4246b607b0eb475dbf81a35/b3b7d0a20cf431adcbff06344936acaf2edd9889.jpg").centerCrop().animate(R.anim.test).placeholder(R.drawable.ic_launcher).into(imageView2);

        //申请WRITE_EXTERNAL_STORAGE权限，Android6.0新增的权限机制
        if (Build.VERSION.SDK_INT >= 23 && !ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_CODE_WRITE_EXTERNAL_STORAGE);
        } else {
            //还可以加载物理地址图片
            Glide.with(this).load(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/gilder_test.jpg").placeholder(R.drawable.ic_launcher).into(imageView3);
        }
    }


    /**
     * Android6.0新增的权限申请回调函数
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //判断权限请求码是否匹配，以及是否获取了所申请的权限
        if (requestCode == PERMISSION_CODE_WRITE_EXTERNAL_STORAGE && grantResults[0] == 0) {
            Glide.with(this).load(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/gilder_test.jpg").placeholder(R.drawable.ic_launcher).into(imageView3);
        }
    }
}
