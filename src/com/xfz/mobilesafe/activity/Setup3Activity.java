package com.xfz.mobilesafe.activity;

import com.xfz.mobilesafe.R;
import com.xfz.mobilesafe.utils.ToastUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author xfz:xfz1990@gmail.com
 * @version create time：2016-5-9 specification third guide page
 */
public class Setup3Activity extends BaseSetupActivity {
	private EditText etPhone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_setup3);
		etPhone = (EditText) findViewById(R.id.et_phone);
		String phone = mPref.getString("safe_phone", "");
		etPhone.setText(phone);
	}

	@Override
	public void showNextPage() {
		String phone = etPhone.getText().toString().trim();
		if (TextUtils.isEmpty(phone)) {
			ToastUtils.showToast(this, "safe number can't be empty");
			return;
		}
		mPref.edit().putString("safe_phone", phone).commit();
		startActivity(new Intent(this, Setup4Activity.class));
		finish();
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
	}

	@Override
	public void showPreviousPage() {
		startActivity(new Intent(this, Setup2Activity.class));
		overridePendingTransition(R.anim.tran_previous_in,
				R.anim.tran_previous_out);
		finish();
	}

	/**
	 * select contact
	 * 
	 * @param v
	 */
	public void selectContact(View v) {
		Intent intent = new Intent(this, ContactActivity.class);
		startActivityForResult(intent, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			String phone = data.getStringExtra("phone");
			etPhone.setText(phone);
		}
	}
}
