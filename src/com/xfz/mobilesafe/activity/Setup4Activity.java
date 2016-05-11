package com.xfz.mobilesafe.activity;

import com.xfz.mobilesafe.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * @author xfz:xfz1990@gmail.com
 * @version create time：2016-5-9 specification forth guide page
 */
public class Setup4Activity extends BaseSetupActivity {
	private CheckBox cbProtect;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_setup4);
		cbProtect = (CheckBox) findViewById(R.id.cb_protect);
		
		boolean protect = mPref.getBoolean("protect", false);
		if (protect) {
			cbProtect.setText("Proof is on");
			cbProtect.setChecked(true);
		}else{
			cbProtect.setText("Proof is off");
			cbProtect.setChecked(false);
		}
		cbProtect.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					cbProtect.setText("Proof is on");
					mPref.edit().putBoolean("protect", true).commit();
				}else{
					cbProtect.setText("Proof is off");
					mPref.edit().putBoolean("protect", false).commit();
				}
			}
		});
	}

	@Override
	public void showNextPage() {
		startActivity(new Intent(this, LostFindActivity.class));
		finish();
		mPref.edit().putBoolean("configed", true).commit();
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
	}

	@Override
	public void showPreviousPage() {
		startActivity(new Intent(this, Setup3Activity.class));
		overridePendingTransition(R.anim.tran_previous_in,
				R.anim.tran_previous_out);
		finish();
	}
}
