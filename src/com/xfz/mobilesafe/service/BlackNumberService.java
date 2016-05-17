package com.xfz.mobilesafe.service;

import java.lang.reflect.Method;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

import com.android.internal.telephony.ITelephony;
import com.xfz.mobilesafe.db.dao.BlackNumberDao;

/**
 * 
 * @author xfz:xfz1990@gmail.com
 * @version create time：2016-5-16
 */
public class BlackNumberService extends Service {

	private SmsReceiver receiver;
	private BlackNumberDao blackNumberDao;
	private TelephonyManager telephonyManager;
	private MyPhoneStateListener myPhoneStateListener;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		blackNumberDao = new BlackNumberDao(getApplicationContext());
		receiver = new SmsReceiver();
		// 2.set listener
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
		intentFilter.setPriority(1133);
		// 3.register broadcast receiver
		registerReceiver(receiver, intentFilter);
		myPhoneStateListener = new MyPhoneStateListener();

		telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

		telephonyManager.listen(myPhoneStateListener,
				PhoneStateListener.LISTEN_CALL_STATE);
	}

	private class MyPhoneStateListener extends PhoneStateListener {
		@Override
		public void onCallStateChanged(int state, final String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);
			if (state == TelephonyManager.CALL_STATE_RINGING) {
				String mode = blackNumberDao.findNumber(incomingNumber);
				if (mode.equals(BlackNumberDao.CALL)
						|| mode.equals(BlackNumberDao.ALL)) {
					endCall();
					// delete the records
					final ContentResolver resolver = getContentResolver();
					final Uri uri = Uri.parse("content://call_log/calls");
					resolver.registerContentObserver(uri, true,
							new ContentObserver(new Handler()) {
								@Override
								public void onChange(boolean selfChange) {
									super.onChange(selfChange);
									resolver.delete(uri, "number=?",
											new String[] { incomingNumber });
									resolver.unregisterContentObserver(this);
								}
							});
				}
			}
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
		telephonyManager.listen(myPhoneStateListener,
				PhoneStateListener.LISTEN_NONE);
	}

	public void endCall() {
		try {
			// use reflection to realize servicemanager since it is hiden
			Class<?> loadClass;

			loadClass = Class.forName("android.os.ServiceManager");

			Method method = loadClass.getDeclaredMethod("getService",
					String.class);

			IBinder invoke = (IBinder) method.invoke(null,
					Context.TELEPHONY_SERVICE);

			ITelephony iTelephony = ITelephony.Stub.asInterface(invoke);

			iTelephony.endCall();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class SmsReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Object[] objects = (Object[]) intent.getExtras().get("pdus");
			for (Object object : objects) {
				SmsMessage message = SmsMessage.createFromPdu((byte[]) object);
				String sender = message.getOriginatingAddress();
				String body = message.getMessageBody();
				System.out.println(body + sender);
				String mode = blackNumberDao.findNumber(sender);
				if (mode.equals(BlackNumberDao.ALL)
						|| mode.equals(BlackNumberDao.SMS)) {
					// intercept sms message
					abortBroadcast();
				}
			}
		}
	}
}
