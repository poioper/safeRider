package com.xfz.mobilesafe.activity;

import com.xfz.mobilesafe.R;
import com.xfz.mobilesafe.db.dao.AddressDao;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

/**
 * attribution query page
 * 
 * @author xfz:xfz1990@gmail.com
 * @version create time：2016-5-12
 */
public class AddressActivity extends Activity {
	private EditText etNumber;
	private TextView tvResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address);
		etNumber = (EditText) findViewById(R.id.et_number);
		tvResult = (TextView) findViewById(R.id.tv_result);
		etNumber.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String address = AddressDao.getAddress(s.toString());
				tvResult.setText(address);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}

	public void query(View v) {
		String number = etNumber.getText().toString().trim();

		if (!TextUtils.isEmpty(number)) {
			String address = AddressDao.getAddress(number);
			tvResult.setText(address);
		} else {
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);

			// shake.setInterpolator(new Interpolator() {
			//
			// @Override
			// public float getInterpolation(float x) {
			// //y=ax+b
			// int y=0;
			// return y;
			// }
			// });
			etNumber.startAnimation(shake);
			vibrate();
		}
	}
	private void vibrate() {
		Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		vibrator.vibrate(new long[] { 1000, 2000, 1000, 3000 }, -1);
	}
}
