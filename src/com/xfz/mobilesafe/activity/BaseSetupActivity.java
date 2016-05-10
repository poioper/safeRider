package com.xfz.mobilesafe.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.widget.Toast;

/**
 * @author xfz:xfz1990@gmail.com
 * @version create time：2016-5-10 super class of guide pages.
 */
public abstract class BaseSetupActivity extends Activity {
	private GestureDetector mDetector;
	public SharedPreferences mPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPref = getSharedPreferences("config", MODE_PRIVATE);
		mDetector = new GestureDetector(this, new SimpleOnGestureListener() {
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {
				// determine weather vertical movement is too larger.
				if (Math.abs(e2.getRawY() - e1.getRawY()) > 100) {
					Toast.makeText(BaseSetupActivity.this,
							"can not move like this", Toast.LENGTH_SHORT)
							.show();
					return true;
				}
				if (Math.abs(velocityX) < 100) {
					Toast.makeText(BaseSetupActivity.this, "move too slow",
							Toast.LENGTH_SHORT).show();
					return true;
				}
				// fling to right,want to go to previous
				if (e2.getRawX() - e1.getRawX() > 200) {
					showPreviousPage();
					return true;
				}
				if (e1.getRawX() - e2.getRawX() > 200) {
					showNextPage();
					return true;
				}
				return super.onFling(e1, e2, velocityX, velocityY);
			}
		});
	}

	/**
	 * show next page
	 */
	public abstract void showNextPage();

	/**
	 * show previous page
	 */
	public abstract void showPreviousPage();

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mDetector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}

	// go to next page
	public void next(View v) {
		showNextPage();
	}

	// go to previous page
	public void previous(View v) {
		showPreviousPage();
	}
}
