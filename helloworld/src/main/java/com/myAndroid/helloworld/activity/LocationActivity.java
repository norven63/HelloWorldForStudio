package com.myAndroid.helloworld.activity;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.myAndroid.helloworld.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Norven on 2015/6/17 0017.
 */
public class LocationActivity extends Activity {
    private TextView logTextView;
    private TextView latTextView;
    private TextView longTextView;
    private TextView timeTextView;

    private ToggleButton providerToggleButton;

    private StringBuilder logSb = new StringBuilder("日志信息:");
    private StringBuilder latSb = new StringBuilder("维度:");
    private StringBuilder longSb = new StringBuilder("经度:");
    private StringBuilder timeSb = new StringBuilder("更新时间:");

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            latSb.append("\n" + location.getLatitude());
            latTextView.setText(latSb.toString());

            longSb.append("\n" + location.getLongitude());
            longTextView.setText(longSb.toString());

            timeSb.append("\n" + sdf.format(new Date()));
            timeTextView.setText(timeSb.toString());
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            logSb.append("\nonStatusChanged" + "\ns: " + s + "\ni " + i + "\nBundle " + bundle + "\n");
            logTextView.setText(logSb.toString());
        }

        @Override
        public void onProviderEnabled(String s) {
            logSb.append("\nonProviderEnabled" + "\ns: " + s + "\n");
            logTextView.setText(logSb.toString());
        }

        @Override
        public void onProviderDisabled(String s) {
            logSb.append("\nonProviderDisabled" + "\ns: " + s + "\n");
            logTextView.setText(logSb.toString());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_location);

        latTextView = (TextView) findViewById(R.id.latTextView);
        latTextView.setText(latSb.toString());

        longTextView = (TextView) findViewById(R.id.longTextView);
        longTextView.setText(longSb.toString());

        timeTextView = (TextView) findViewById(R.id.timeTextView);
        timeTextView.setText(timeSb.toString());

        final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        logSb.append("\n可用定位器 >>> " + locationManager.getProviders(true));

        //判断是否有地理位置权限
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        boolean hasPermission = true;
        if (location == null) {
            hasPermission = false;
        }
        logSb.append("\n是否有权限 >>> " + hasPermission);

        logTextView = (TextView) findViewById(R.id.logTextView);
        logTextView.setText(logSb.toString());


//        // 查询条件
//        Criteria criteria = new Criteria();
//        criteria.setAccuracy(Criteria.ACCURACY_FINE); // 定位的精准度
//        criteria.setAltitudeRequired(false);          // 海拔信息是否关注
//        criteria.setBearingRequired(false); // 对周围的事物是否关心
//        criteria.setCostAllowed(true);  // 是否支持收费查询
//        criteria.setPowerRequirement(Criteria.POWER_LOW); // 是否耗电
//        criteria.setSpeedRequired(false); // 对速度是否关注
//
//        // 获取最好的定位方式
//        String provider = locationManager.getBestProvider(criteria, true); // true 代表从打开的设备中查找
//
//        logSb.append("\n当前定位器 >>> " + provider);

//        locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, locationListener, null);//只请求一次位置信息

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 3, locationListener);

        providerToggleButton = (ToggleButton) findViewById(R.id.providerToggleButton);
        providerToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                String provider;

                if (b) {
                    provider = LocationManager.NETWORK_PROVIDER;
                } else {
                    provider = LocationManager.GPS_PROVIDER;
                }

                logSb.append("\n当前定位器已切换 >>> " + provider);
                logTextView.setText(logSb.toString());

                locationManager.removeUpdates(locationListener);
                locationManager.requestLocationUpdates(provider, 3000, 3, locationListener);
            }
        });
    }
}
