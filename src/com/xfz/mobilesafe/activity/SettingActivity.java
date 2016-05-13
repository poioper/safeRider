package com.xfz.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.xfz.mobilesafe.R;
import com.xfz.mobilesafe.service.AddressService;
import com.xfz.mobilesafe.utils.ServiceStatusUtils;
import com.xfz.mobilesafe.view.SettingClickView;
import com.xfz.mobilesafe.view.SettingItemView;

public class SettingActivity extends Activity {
	private SettingItemView sivUpdate;// update setting
	private SettingItemView sivAddress;// attribution setting
	private SettingClickView scvAddressStyle;// attribution setting
	private SettingClickView scvAddressLocation;// attribution toast location
	private SharedPreferences mPref;
	final String[] items = new String[] { "transparant", "orange", "blue",
			"gray", "green" };
	private int style;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		mPref = getSharedPreferences("config", MODE_PRIVATE);
		initUpdateView();
		initAddressView();
		initAddressStyle();
		initAddressLocation();
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
		} else {
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

	/**
	 * modify the style of the attribution toast
	 */
	private void initAddressStyle() {
		scvAddressStyle = (SettingClickView) findViewById(R.id.scv_address_style);
		scvAddressStyle.setTitle("Attribution Toast Style");
		style = mPref.getInt("address_style", 0);
		scvAddressStyle.setDesc(items[style]);
		scvAddressStyle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showSingleChooseDialog();
			}
		});
	}

	protected void showSingleChooseDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("toast style");
		style = mPref.getInt("address_style", 0);
		builder.setSingleChoiceItems(items, style,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						mPref.edit().putInt("address_style", which).commit();
						dialog.dismiss();
						scvAddressStyle.setDesc(items[which]);
					}
				});
		builder.setNegativeButton("cancel", null);
		builder.show();
	}

	/**
	 * modify attribution toast's place on screen
	 */
	private void initAddressLocation() {
		scvAddressLocation = (SettingClickView) findViewById(R.id.scv_address_location);
		scvAddressLocation.setTitle("attribution toast position");
		scvAddressLocation.setDesc("set the position of the attribution toast");
		scvAddressLocation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(SettingActivity.this,
						DragViewActivity.class));
			}
		});
	}
}
