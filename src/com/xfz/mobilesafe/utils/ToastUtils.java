package com.xfz.mobilesafe.utils;

import android.content.Context;
import android.widget.Toast;

/** 
 * 
 * @author xfz:xfz1990@gmail.com 
 * @version create time：2016-5-11 
 */
public class ToastUtils {
	public static void showToast(Context context,String text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}
}
