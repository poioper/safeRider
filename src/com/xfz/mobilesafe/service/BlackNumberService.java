package com.xfz.mobilesafe.service;

import com.xfz.mobilesafe.db.dao.BlackNumberDao;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.SmsMessage;

/** 
 * 
 * @author xfz:xfz1990@gmail.com 
 * @version create time：2016-5-16 
 */
public class BlackNumberService extends Service {

	private SmsReceiver receiver;
	private BlackNumberDao blackNumberDao;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		blackNumberDao = new BlackNumberDao(getApplicationContext());
		receiver = new SmsReceiver();
		//2.set listener
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
		intentFilter.setPriority(1133);
		//3.register broadcast receiver
		registerReceiver(receiver, intentFilter);
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
	}
	
	private class SmsReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			Object[] objects = (Object[]) intent.getExtras().get("pdus");
			for (Object object : objects) {
				SmsMessage message = SmsMessage.createFromPdu((byte[]) object);
				String sender = message.getOriginatingAddress();
				String body = message.getMessageBody();
				System.out.println(body + sender);
				String mode = blackNumberDao.findNumber(sender);
				if (mode.equals(BlackNumberDao.ALL) || mode.equals(BlackNumberDao.SMS)) {
					//intercept sms message
					abortBroadcast();
				}
			}
		}
	}
}
