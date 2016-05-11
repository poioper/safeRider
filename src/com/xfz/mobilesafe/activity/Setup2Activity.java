package com.xfz.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.xfz.mobilesafe.R;
import com.xfz.mobilesafe.utils.ToastUtils;
import com.xfz.mobilesafe.view.SettingItemView;

/**
 * @author xfz:xfz1990@gmail.com
 * @version create time：2016-5-9 
 * second guide page
 */
public class Setup2Activity extends BaseSetupActivity {

	private SettingItemView sivSim;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup2);
		sivSim = (SettingItemView) findViewById(R.id.siv_sim);
		
		String sim = mPref.getString("sim", null);
		if (TextUtils.isEmpty(sim)) {
			sivSim.setChecked(false);
		}else{
			sivSim.setChecked(true);
		}
		
		sivSim.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (sivSim.isChecked()) {
					sivSim.setChecked(false);
					//remove the saved sim card from sp
					mPref.edit().remove("sim").commit();
				}else{
					sivSim.setChecked(true);
					//save SIM card info
					TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
					String simSerialNumber = tm.getSimSerialNumber();
					System.out.println("serial num = "+simSerialNumber);
					//save the sim card to sp
					mPref.edit().putString("sim", simSerialNumber).commit();
				}
			}
		});
	}

	/**
	 * show next page
	 */
	public void showNextPage() {
		//if sim card not bound, not allowed to go to next step
		String sim = mPref.getString("sim", null);
		if (TextUtils.isEmpty(sim)) {
			ToastUtils.showToast(this, "has to bind sim card!");
			return;
		}
		startActivity(new Intent(this, Setup3Activity.class));
		finish();
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
	}

	/**
	 * show previous page
	 */
	public void showPreviousPage() {
		startActivity(new Intent(this, Setup1Activity.class));
		finish();
		overridePendingTransition(R.anim.tran_previous_in,
				R.anim.tran_previous_out);
	}
}
