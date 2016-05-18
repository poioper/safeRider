package com.xfz.mobilesafe.engine;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Debug.MemoryInfo;

import com.xfz.mobilesafe.bean.TaskInfo;

public class TaskEngine {
	/**
	 * return all process information
	 * 
	 * @return
	 */
	public static List<TaskInfo> getTaskAllInfo(Context context) {
		List<TaskInfo> list = new ArrayList<TaskInfo>();
		// 1.activity manager
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		PackageManager pm = context.getPackageManager();
		// 2. get all running processes
		List<RunningAppProcessInfo> runningAppProcesses = activityManager
				.getRunningAppProcesses();

		for (RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
			TaskInfo taskInfo = new TaskInfo();
			// 3.get process information
			// get package name
			String packagName = runningAppProcessInfo.processName;
			taskInfo.setPackageName(packagName);
			// get process memory info
			MemoryInfo[] memoryInfo = activityManager
					.getProcessMemoryInfo(new int[] { runningAppProcessInfo.pid });
			int totalPss = memoryInfo[0].getTotalPss();
			long ramSize = totalPss * 1024;
			taskInfo.setRamSize(ramSize);
			try {
				// get application info
				ApplicationInfo applicationInfo = pm.getApplicationInfo(
						packagName, 0);
				// icon
				Drawable icon = applicationInfo.loadIcon(pm);
				taskInfo.setIcon(icon);
				// name
				String name = applicationInfo.loadLabel(pm).toString();
				taskInfo.setName(name);
				// get flag
				int flags = applicationInfo.flags;
				boolean isUser;
				if ((applicationInfo.FLAG_SYSTEM & flags) == applicationInfo.FLAG_SYSTEM) {
					isUser = false;
				} else {
					isUser = true;
				}
				taskInfo.setUser(isUser);
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
			list.add(taskInfo);
		}
		return list;
	}

}
