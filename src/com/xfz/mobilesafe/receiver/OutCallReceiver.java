package com.xfz.mobilesafe.receiver;

import com.xfz.mobilesafe.db.dao.AddressDao;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/** 
 * 
 * @author xfz:xfz1990@gmail.com 
 * @version create time：2016-5-12 
 */
public class OutCallReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String number = getResultData();
		String address = AddressDao.getAddress(number);
		Toast.makeText(context, address, Toast.LENGTH_LONG).show();
	}

}
