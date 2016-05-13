package com.xfz.mobilesafe.service;

import com.xfz.mobilesafe.R;
import com.xfz.mobilesafe.db.dao.AddressDao;

import android.R.integer;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

/**
 * show incoming call service
 * 
 * @author xfz:xfz1990@gmail.com
 * @version create time：2016-5-12
 */
public class AddressService extends Service {

	private TelephonyManager tm;
	private MyListener listener;
	private OutCallReceiver receiver;
	private WindowManager mWM;
	private View view;
	private SharedPreferences mPref;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		listener = new MyListener();
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		receiver = new OutCallReceiver();
		IntentFilter filter = new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL);
		registerReceiver(receiver, filter);
		mPref = getSharedPreferences("config", MODE_PRIVATE);
	}

	class MyListener extends PhoneStateListener {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				System.out.println("ringling!!!!!!!");
				String address = AddressDao.getAddress(incomingNumber);
				// Toast.makeText(AddressService.this, address,
				// Toast.LENGTH_LONG)
				// .show();
				showToast(address);
				break;
			case TelephonyManager.CALL_STATE_IDLE:
				if (mWM != null && view != null) {
					mWM.removeView(view);
					view = null;
				}
			default:
				break;
			}
		}
	}

	class OutCallReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String number = getResultData();
			String address = AddressDao.getAddress(number);
			// Toast.makeText(context, address, Toast.LENGTH_LONG).show();
			showToast(address);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);
		unregisterReceiver(receiver);
	}

	private void showToast(String text) {
		mWM = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
		;
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		params.format = PixelFormat.TRANSLUCENT;
		params.type = WindowManager.LayoutParams.TYPE_TOAST;
		params.gravity = Gravity.LEFT + Gravity.TOP;
		params.setTitle("Toast");
		
		int lastX = mPref.getInt("lastX", 0);
		int lastY = mPref.getInt("lastY", 0);
		
		params.x = lastX;
		params.y = lastY;
		
		int style = mPref.getInt("address_style", 0);
		int[] bgs = new int[] { R.drawable.call_locate_white,
				R.drawable.call_locate_orange, R.drawable.call_locate_blue,
				R.drawable.call_locate_gray, R.drawable.call_locate_green };
		// view = new TextView(this);
		view = View.inflate(this, R.layout.toast_address, null);
		view.setBackgroundResource(bgs[style]);
		TextView tvText = (TextView) view.findViewById(R.id.tv_number);
		tvText.setText(text);
		mWM.addView(view, params);
	}
}
