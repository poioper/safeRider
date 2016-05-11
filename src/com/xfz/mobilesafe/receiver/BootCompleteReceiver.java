package com.xfz.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/** 
 * receiver of boot complete broadcast
 * @author xfz:xfz1990@gmail.com 
 * @version create time：2016-5-10 
 */
public class BootCompleteReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		
		boolean protect = sp.getBoolean("protect", false);
		
		if(protect){
			String sim = sp.getString("sim", null);
			if (!TextUtils.isEmpty(sim)) {
				//get the current sim card
				TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
				String currentSim = tm.getSimSerialNumber();
				if (sim.equals(currentSim)) {
					System.out.println("phone is safe");
				}else{
					System.out.println("sim card changed");
				}
			}
		}
	}

}
