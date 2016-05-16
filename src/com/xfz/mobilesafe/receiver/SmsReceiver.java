package com.xfz.mobilesafe.receiver;

import com.xfz.mobilesafe.R;
import com.xfz.mobilesafe.service.LocationService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationListener;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;

/**
 * sms interceptor
 * 
 * @author xfz:xfz1990@gmail.com
 * @version create time：2016-5-11
 */
public class SmsReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		System.out.println("qingdan注册广播接受者接受短信");
		Object[] objects = (Object[]) intent.getExtras().get("pdus");
		for (Object object : objects) {
			SmsMessage message = SmsMessage.createFromPdu((byte[]) object);
			String originatingAddress = message.getOriginatingAddress();// incoming
																		// number
			String messageBody = message.getMessageBody();// message body
			if ("#*alarm*#".equals(messageBody)) {
				// play alarm music
				MediaPlayer player = MediaPlayer.create(context, R.raw.alarm);
				player.setVolume(1f, 1f);
				player.setLooping(true);
				player.start();
				abortBroadcast();
			} else if ("#*location*#".equals(messageBody)) {
				context.startService(new Intent(context, LocationService.class));
				SharedPreferences sp = context.getSharedPreferences("config",
						Context.MODE_PRIVATE);
				String location = sp.getString("location", "");
				System.out.println("location:" + location);
				abortBroadcast();
			} else if ("#*wipedata*#".equals(messageBody)) {

			} else if ("#*lockscreen*#".equals(messageBody)) {

			}
		}
	}

}
