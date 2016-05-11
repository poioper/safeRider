package com.xfz.mobilesafe.activity;

import com.xfz.mobilesafe.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/** 
 * Prevent From Stolen
 * @author xfz:xfz1990@gmail.com 
 * @version create time：2016-5-9 
 * specification
 */
public class LostFindActivity extends Activity {
	private SharedPreferences mPref;
	private TextView tvSafePhone;
	private ImageView ivProtect;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPref = getSharedPreferences("config", MODE_PRIVATE);
		//check weather has enter the guide page or not
		boolean configed = mPref.getBoolean("configed", false);
		if(configed){
			setContentView(R.layout.activity_lost_find);
			
			//renew safe phone number depends on sp
			tvSafePhone = (TextView) findViewById(R.id.tv_safe_phone);
			String phone = mPref.getString("safe_phone", "");
			tvSafePhone.setText(phone);
			
			//renew safe image depends on sp
			ivProtect = (ImageView) findViewById(R.id.iv_protect);
			boolean protect = mPref.getBoolean("protect", false);
			if (protect) {
				ivProtect.setImageResource(R.drawable.lock);
			}else{
				ivProtect.setImageResource(R.drawable.unlock);
			}
		}else {
			//go to guide page
			startActivity(new Intent(LostFindActivity.this, Setup1Activity.class));
			finish();
		}
	}
	/**
	 * reEnter the guide page 
	 * @param v
	 */
	public void reEnter(View v) {
		startActivity(new Intent(this, Setup1Activity.class));
		finish();
	}
}
