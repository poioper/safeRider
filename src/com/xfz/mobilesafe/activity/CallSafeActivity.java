package com.xfz.mobilesafe.activity;

import com.xfz.mobilesafe.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

/** 
 * 
 * @author xfz:xfz1990@gmail.com 
 * @version create time：2016-5-14 
 */
public class CallSafeActivity extends Activity{
	private ListView list_View;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		list_View = (ListView) findViewById(R.id.list_view);
	}
}
