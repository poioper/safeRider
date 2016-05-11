package com.xfz.mobilesafe.receiver;

import com.xfz.mobilesafe.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;

/** 
 * sms interceptor
 * @author xfz:xfz1990@gmail.com 
 * @version create time：2016-5-11 
 */
public class SmsReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Object[] objects = (Object[]) intent.getExtras().get("pdus");
		for (Object object : objects) {
			SmsMessage message = SmsMessage.createFromPdu((byte[])object);
			String originatingAddress = message.getOriginatingAddress();//incoming number
			String messageBody = message.getMessageBody();//message body
			if("#*alarm*#".equals(messageBody)){
				//play alarm music
				MediaPlayer player = MediaPlayer.create(context, R.raw.alarm);
				player.setVolume(1f, 1f);
				player.setLooping(true);
				player.start();
				abortBroadcast();
			}
		}
	}

}
