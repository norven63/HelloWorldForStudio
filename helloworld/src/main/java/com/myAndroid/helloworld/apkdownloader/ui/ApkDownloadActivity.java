package com.myAndroid.helloworld.apkdownloader.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.myAndroid.helloworld.MyApplication;
import com.myAndroid.helloworld.R;
import com.myAndroid.helloworld.apkdownloader.ApkDownloadService;
import com.myAndroid.helloworld.apkdownloader.model.ApkDownloadManager;
import com.myAndroid.helloworld.apkdownloader.model.ApkDownloadTask;

import java.io.File;

/**
 * @author ZhouXinXing
 * @file ApkDownloadActivity.java
 * @date 2015年12月29日 下午6:40:09
 * @Description 下载任务列表界面
 */
public class ApkDownloadActivity extends Activity {
    private final int PERMISSION_CODE_WRITE_EXTERNAL_STORAGE = 123;//申请读写SD卡权限的请求码（读、写两个权限，申请任意其一可得其二，二者属于同一权限组）

    private ApkDownloadAdapter mAdapter;
    private ListView mListView;
    private View mEmptyView;

    private ApkDownloadService mService;
    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = ((ApkDownloadService.MyBinder) service).getService();
            try {
                service.linkToDeath(new IBinder.DeathRecipient() {
                    @Override
                    public void binderDied() {
                        Log.e("apk_download", "下载服务已死亡");
                    }
                }, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }
    };

    //监听任务下载队列的变化
    private ApkDownloadManager.ApkDownloadTaskListChangeListener taskListChangeListener = new ApkDownloadManager.ApkDownloadTaskListChangeListener() {
        @Override
        public void onAdd(ApkDownloadTask apkDownloadTask) {
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onRemove(ApkDownloadTask apkDownloadTask) {
            mAdapter.notifyDataSetChanged();
        }
    };

    //新app安装的广播监听
    private BroadcastReceiver addNewAppReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
                String packageName = intent.getData().getSchemeSpecificPart();

                ApkDownloadManager.getInstance.installNewApp(packageName);
            }
        }
    };

    //网络状况广播监听
    private BroadcastReceiver netWorkReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                if (mService == null) {
                    return;
                }

//                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//                NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//                NetworkInfo wifiNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//
//                if (wifiNetInfo.isConnected()) {
//                    Log.e("apk_download", "网络：wifi连接");
//                    mService.resumeAllTasks();
//                } else {
//                    Log.e("apk_download", "网络：wifi断开");
//                    mService.pauseAllTasks();
//                }

                boolean noNetWork = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
                if (noNetWork) {
                    Log.e("apk_download", "当前无网络连接");
                    mService.pauseAllTasks();

                    return;
                }

                int networkType = intent.getIntExtra(ConnectivityManager.EXTRA_NETWORK_TYPE, ConnectivityManager.TYPE_DUMMY);
                switch (networkType) {
                    case ConnectivityManager.TYPE_MOBILE:
                        Log.e("apk_download", "网络：蜂窝 " + checkfor3GorLte());
                        mService.pauseAllTasks();

                        break;
                    case ConnectivityManager.TYPE_WIFI:
                        Log.e("apk_download", "网络：wifi");
                        mService.resumeAllTasks();

                        break;
                    default:
                        break;
                }
            }
        }
    };

    /**
     * 检查网络类型
     */
    private String checkfor3GorLte() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        switch (telephonyManager.getNetworkType()) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return "2G";

            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return "3G";

            case TelephonyManager.NETWORK_TYPE_LTE:
                return "4G";

            default:
                return "未知";
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bindService(new Intent(this, ApkDownloadService.class), mServiceConnection,
                Context.BIND_AUTO_CREATE);

        //申请WRITE_EXTERNAL_STORAGE权限，Android6.0新增的权限机制
        if (Build.VERSION.SDK_INT >= 23 && !ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_CODE_WRITE_EXTERNAL_STORAGE);
        }

        //注册app安装广播
        IntentFilter packageFilter = new IntentFilter();
        packageFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        packageFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        packageFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        packageFilter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY - 1);
        packageFilter.addDataScheme("package");
        registerReceiver(addNewAppReceiver, packageFilter);

        //注册网络状态变化广播
        IntentFilter netWorkFilter = new IntentFilter();
        netWorkFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netWorkReceiver, netWorkFilter);

        initView();
    }

    private void initView() {
        setContentView(R.layout.apk_download_manager_layout);

        findViewById(R.id.empty_view_title).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ApkDownloadActivity.this, ApkDownloadService.class);
                intent.putExtra(ApkDownloadService.EXTRA_TYPE, 1);
                startService(intent);
            }
        });

        mListView = (ListView) findViewById(R.id.list_view);
        mEmptyView = findViewById(R.id.empty_view);
        mListView.setEmptyView(mEmptyView);

        mAdapter = new ApkDownloadAdapter(ApkDownloadManager.getInstance.getApkDownloadTasks());
        mListView.setAdapter(mAdapter);
        mAdapter.setOnItemBtnClickListener(new ApkDownloadAdapter.OnItemBtnClickListener() {
            @Override
            public void onStartClick(ApkDownloadTask apkDownloadTask) {
                mService.startTask(apkDownloadTask);
            }

            @SuppressLint("InflateParams")
            @Override
            public void onRemoveClick(final ApkDownloadTask apkDownloadTask) {
                final AlertDialog dialog = new AlertDialog.Builder(ApkDownloadActivity.this).create();
                Window window = dialog.getWindow();
                window.setGravity(Gravity.BOTTOM);
                dialog.show();

                View view = LayoutInflater.from(ApkDownloadActivity.this)
                        .inflate(R.layout.dialog_confirm_del_apk, null);

                ((TextView) view.findViewById(R.id.btnDelApkTitle)).setText("确定删除"
                        + apkDownloadTask.getInfo().getLabel() + "?");

                view.findViewById(R.id.btnDelApk).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mService.removeTask(apkDownloadTask);

                        dialog.dismiss();
                    }
                });

                dialog.setContentView(view);
            }

            @Override
            public void onPauseClick(ApkDownloadTask apkDownloadTask) {
                mService.pauseTask(apkDownloadTask);
            }

            @Override
            public void onInstallClick(ApkDownloadTask apkDownloadTask) {
                tryInstall(ApkDownloadActivity.this, apkDownloadTask.getInfo().getFilePath());
            }

            @Override
            public void onOpenClick(ApkDownloadTask apkDownloadTask) {
                try {
                    Intent intent = getPackageManager().getLaunchIntentForPackage(
                            apkDownloadTask.getInfo().getPkgName());
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(ApkDownloadActivity.this, apkDownloadTask.getInfo().getLabel() + "启动失败",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void tryInstall(Context context, String file) {
        File tar = new File(file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(tar), "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        ApkDownloadManager.getInstance.addTaskListChangeListener(taskListChangeListener);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(addNewAppReceiver);
        unregisterReceiver(netWorkReceiver);
        unbindService(mServiceConnection);
        mService = null;
        ApkDownloadManager.getInstance.removeTaskListChangeListener(taskListChangeListener);

        super.onDestroy();
    }
}