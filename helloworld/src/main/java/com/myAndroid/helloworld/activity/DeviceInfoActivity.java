package com.myAndroid.helloworld.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.myAndroid.helloworld.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;

public class DeviceInfoActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.device_info_activity);

        String model = android.os.Build.MODEL; // 手机型号
        String release = android.os.Build.VERSION.RELEASE; // 系统版本号

        ((TextView) findViewById(R.id.device_type_textView_right)).setText(model);
        ((TextView) findViewById(R.id.os_release_textView_right)).setText(release);

        showUUID();
        showAndroidId();
        showDeviceId();
        showNetConnectionInfo();
    }

    /**
     * ===获取网络连接信息===
     * <p/>
     * 1. 需要打开wifi,否则ip会返回0.0.0.0
     * 2. 需要权限<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
     */
    private void showNetConnectionInfo() {
        TextView ipTextView = (TextView) findViewById(R.id.ip_textView_right);
        TextView macTextView = (TextView) findViewById(R.id.mac_address_textView_right);

        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);

        if (!wifiManager.isWifiEnabled()) {
            ipTextView.setText("请开启wifi");
            macTextView.setText("请开启wifi");

            return;
        }

        WifiInfo wifiInfo = wifiManager.getConnectionInfo();

        int ipAddress = wifiInfo.getIpAddress();
        String ip = intToIp(ipAddress);
        ipTextView.setText(ip);

        macTextView.setText(wifiInfo.getMacAddress());

        // 获取当前连接的wifi信息
        TextView wifiSSIDTextView = (TextView) findViewById(R.id.wifi_SSID_textView_right);
        wifiSSIDTextView.setText(wifiInfo.getSSID());

        TextView wifiMacTextView = (TextView) findViewById(R.id.wifi_mac_textView_right);
        wifiMacTextView.setText(wifiInfo.getBSSID());

        // 获取所有附近的wifi信息
        final ListView listView = (ListView) findViewById(R.id.wifi_listView);
        final List<ScanResult> wifiList = wifiManager.getScanResults();
        listView.setAdapter(new BaseAdapter() {

            @SuppressLint("InflateParams")
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view;
                ViewHolder viewHolder;

                if (convertView != null) {
                    view = convertView;
                    viewHolder = (ViewHolder) view.getTag();
                } else {
                    view = getLayoutInflater().inflate(R.layout.device_info_item_layout, null);
                    viewHolder = new ViewHolder();
                    viewHolder.textView = (TextView) view.findViewById(R.id.wifi_info_item_SSID);
                    viewHolder.editText = (EditText) view.findViewById(R.id.wifi_info_item_mac);

                    view.setTag(viewHolder);
                }

                if (getItem(position) != null) {
                    viewHolder.textView.setText(getItem(position).SSID);
                    viewHolder.editText.setText(getItem(position).BSSID);
                }

                return view;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public ScanResult getItem(int position) {
                return wifiList == null ? null : wifiList.get(position);
            }

            @Override
            public int getCount() {
                return wifiList == null ? 0 : wifiList.size();
            }

            class ViewHolder {
                TextView textView;
                EditText editText;
            }
        });
    }

    private String intToIp(int ipAddress) {
        return (ipAddress & 0xFF) + "." + ((ipAddress >> 8) & 0xFF) + "." + ((ipAddress >> 16) & 0xFF) + "."
                + ((ipAddress >> 24) & 0xFF);
    }

    /**
     * ===显示Installtion ID===
     * 通过在程序安装后第一次运行后生成一个ID实现的，>>> 但是在删除应用后，该码会随之变动 <<<。
     * 但该方式跟设备唯一标识不一样，它会因为不同的应用程序而产生不同的ID，而不是设备唯一ID。
     * 可以用来标识在某个应用中的唯一ID（即Installtion ID），或者跟踪应用的安装数量
     * <p/>
     * 1. 不同app获取不同
     * 2. 删除app会重置
     */
    private synchronized void showUUID() {
        String uuid = null;

        File installation = new File(getFilesDir(), "INSTALLATION");
        try {
            if (!installation.exists()) {
                writeInstallationFile(installation);
            }

            uuid = readInstallationFile(installation);
        } catch (Exception e) {
            e.printStackTrace();
        }

        TextView textView = (TextView) findViewById(R.id.installtionID_textView_right);
        textView.setText(uuid);
    }

    private void writeInstallationFile(File installation) throws IOException {
        FileOutputStream out = new FileOutputStream(installation);
        String id = UUID.randomUUID().toString();
        out.write(id.getBytes());
        out.close();
    }

    private String readInstallationFile(File installation) throws IOException {
        RandomAccessFile f = new RandomAccessFile(installation, "r");
        byte[] bytes = new byte[(int) f.length()];
        f.readFully(bytes);
        f.close();
        return new String(bytes);
    }

    /**
     * ===显示AndroidId===
     * ANDROID_ID是设备第一次启动时产生和存储的64bit的一个数，当设备被wipe（刷机）后该数重置。
     * 在Android <=2.1 or Android >=2.3的版本是可靠、稳定的，但在2.2的版本并不是100%可靠的。
     * 在主流厂商生产的设备上，有一个很经常的bug，就是每个设备都会产生相同的ANDROID_ID：9774d56d682e549c
     * <p/>
     * 1. 不同app获取相同
     * 2. 小心9774d56d682e549c
     */
    private void showAndroidId() {
        try {
            String androidId = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
            UUID uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8"));

            TextView textView = (TextView) findViewById(R.id.AID_textView_right);
            textView.setText(uuid.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * ===显示Device id===
     * 根据不同的手机设备返回IMEI、MEID或者ESN码
     * 1. 非手机设备： 如果设备没有通话的硬件功能的话，就没有这个DEVICE_ID
     * 2. 权限： 获取DEVICE_ID需要<uses-permission
     * android:name="android.permission.READ_PHONE_STATE"/>权限（目前Google已经开始禁止获取该权限）
     * 3. bug：在少数的一些手机设备上，该实现有漏洞，会返回垃圾，如:zeros或者asterisks的产品
     */
    private void showDeviceId() {
        try {
            String deviceId = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
            UUID uuid = deviceId != null ? UUID.nameUUIDFromBytes(deviceId.getBytes("utf8")) : UUID.randomUUID();

            TextView textView = (TextView) findViewById(R.id.device_id_textView_right);
            textView.setText(uuid.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
