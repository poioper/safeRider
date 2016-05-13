package com.xfz.mobilesafe.activity;

import com.xfz.mobilesafe.R;
import com.xfz.mobilesafe.service.AddressService;
import com.xfz.mobilesafe.utils.ServiceStatusUtils;
import com.xfz.mobilesafe.view.SettingItemView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class SettingActivity extends Activity {
	private SettingItemView sivUpdate;// update setting
	private SettingItemView sivAddress;// attribution setting
	private SharedPreferences mPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		mPref = getSharedPreferences("config", MODE_PRIVATE);
		initUpdateView();
		initAddressView();
	}

	/**
	 * init update function
	 */
	private void initUpdateView() {
		sivUpdate = (SettingItemView) findViewById(R.id.siv_update);
		// sivUpdate.setTitle("Auto Update");
		boolean autoUpdate = mPref.getBoolean("auto_update", true);
		if (autoUpdate) {
			// sivUpdate.setDesc("auto update is on");
			sivUpdate.setChecked(true);
		} else {
			// sivUpdate.setDesc("auto update is off");
			sivUpdate.setChecked(false);
		}
		sivUpdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// determine the check box is checked or not
				if (sivUpdate.isChecked()) {
					// set to unchecked
					sivUpdate.setChecked(false);
					// sivUpdate.setDesc("auto update is off");
					mPref.edit().putBoolean("auto_update", false).commit();
				} else {
					sivUpdate.setChecked(true);
					// sivUpdate.setDesc("auto update is on");
					mPref.edit().putBoolean("auto_update", true).commit();
				}
			}
		});
	}

	/**
	 * init attribution function
	 */
	private void initAddressView() {
		sivAddress = (SettingItemView) findViewById(R.id.siv_address);
		boolean serviceRunning = ServiceStatusUtils.isServiceRunning(this,
				"com.xfz.mobilesafe.service.AddressService");
		if (serviceRunning) {
			sivAddress.setChecked(true);
		}else {
			sivAddress.setChecked(false);
		}
		
		sivAddress.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (sivAddress.isChecked()) {
					sivAddress.setChecked(false);
					stopService(new Intent(SettingActivity.this,
							AddressService.class));
				} else {
					sivAddress.setChecked(true);
					startService(new Intent(SettingActivity.this,
							AddressService.class));
				}
			}
		});

	}
}
