package com.myAndroid.helloworld.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.myAndroid.helloworld.activity.SendOrderReceiverActivity;

public class ThiredReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.e("abc", "三进入!");
		String string = intent.getStringExtra(SendOrderReceiverActivity.DATA);

		Bundle bundle = getResultExtras(false);
		String string2 = "";
		if (null != bundle) {
			string2 = bundle.getString(FirstReceiver.DATA_FROM_FIRST);// 此处依旧获得的是第一个接收器传入的数据
		}
		Toast.makeText(context, "3: " + string + " : " + string2, Toast.LENGTH_SHORT).show();

		Log.i("ORDER_RECEIVER", "thired");
	}
}
