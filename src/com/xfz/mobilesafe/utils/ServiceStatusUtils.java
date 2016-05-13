package com.xfz.mobilesafe.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

/** 
 * service status utils
 * @author xfz:xfz1990@gmail.com 
 * @version create time：2016-5-12 
 */
public class ServiceStatusUtils {
	public static boolean isServiceRunning(Context ctx,String serviceName) {
		ActivityManager am = (ActivityManager) ctx
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> runningService = am.getRunningServices(100);
		for (RunningServiceInfo runningServiceInfo : runningService) {
			String className = runningServiceInfo.service.getClassName();
			System.out.println(className);
			if (className.equals(serviceName)) {
				return true;
			}
		}
		return false;
	}
}
