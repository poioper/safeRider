package com.xfz.mobilesafe.engine;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.xfz.mobilesafe.bean.AppInfo;

/**
 * App management engine
 * 
 * @author xfz:xfz1990@gmail.com
 * @version create time：2016-5-16
 */
public class AppEngine {

	/**
	 * get all app infos in the phone
	 */
	public static List<AppInfo> getAppInfos(Context context) {
		List<AppInfo> list = new ArrayList<AppInfo>();
		// get app info
		PackageManager pm = context.getPackageManager();
		// all infos of app that installed in the phone
		List<PackageInfo> installedPackages = pm.getInstalledPackages(0);
		for (PackageInfo packageInfo : installedPackages) {
			// get package name
			String packageName = packageInfo.packageName;
			// get version number
			String versionName = packageInfo.versionName;
			// get application
			ApplicationInfo applicationInfo = packageInfo.applicationInfo;
			// get app icon
			Drawable icon = applicationInfo.loadIcon(pm);
			// get app name
			String name = applicationInfo.loadLabel(pm).toString();
			// whether or not is an user app
			int flags = applicationInfo.flags;// flag contains app relative
												// infos
			boolean isUser;
			if ((applicationInfo.FLAG_SYSTEM & flags) == applicationInfo.FLAG_SYSTEM) {
				isUser = false;
			} else {
				isUser = true;
			}
			boolean isSD;
			// in sd card or not
			if ((applicationInfo.FLAG_EXTERNAL_STORAGE & flags) == applicationInfo.FLAG_EXTERNAL_STORAGE) {
				isSD = true;
			} else {
				isSD = false;
			}

			// add to bean
			AppInfo appInfo = new AppInfo(name, icon, packageName, versionName,
					isSD, isUser);

			// add to list
			list.add(appInfo);
		}
		return list;
	}
}
