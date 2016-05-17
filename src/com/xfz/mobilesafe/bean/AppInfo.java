package com.xfz.mobilesafe.bean;

import android.graphics.drawable.Drawable;

/** 
 * App info bean
 * @author xfz:xfz1990@gmail.com 
 * @version create time：2016-5-16 
 */
public class AppInfo {
	//app name
	private String name;
	//icon
	private Drawable icon;
	//package name
	private String packageName;
	//version number
	private String versionName;
	//whether in sd card
	private boolean isSD;
	//whether is a user app
	private boolean isUser;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	public boolean isSD() {
		return isSD;
	}
	public void setSD(boolean isSD) {
		this.isSD = isSD;
	}
	public boolean isUser() {
		return isUser;
	}
	public void setUser(boolean isUser) {
		this.isUser = isUser;
	}
	public AppInfo() {
		super();
	}
	public AppInfo(String name, Drawable icon, String packageName,
			String versionName, boolean isSD, boolean isUser) {
		super();
		this.name = name;
		this.icon = icon;
		this.packageName = packageName;
		this.versionName = versionName;
		this.isSD = isSD;
		this.isUser = isUser;
	}
	@Override
	public String toString() {
		return "AppInfo [name=" + name + ", icon=" + icon + ", packageName="
				+ packageName + ", versionName=" + versionName + ", isSD="
				+ isSD + ", isUser=" + isUser + "]";
	}
	
	
}
