package com.xfz.mobilesafe.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xfz.mobilesafe.R;

/**
 * modify the position of the attribution toast
 * 
 * @author xfz:xfz1990@gmail.com
 * @version create time：2016-5-13
 */
public class DragViewActivity extends Activity {

	private TextView tvTop;
	private TextView tvBottom;
	private ImageView ivDrag;
	private int startX;
	private int startY;
	private SharedPreferences mPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drag_view);
		tvTop = (TextView) findViewById(R.id.tv_top);
		tvBottom = (TextView) findViewById(R.id.tv_bottom);
		ivDrag = (ImageView) findViewById(R.id.iv_drag);
		mPref = getSharedPreferences("config", MODE_PRIVATE);
		final long[] mHits = new long[2];

		int lastX = mPref.getInt("lastX", 0);
		int lastY = mPref.getInt("lastY", 0);

		// onMeasure->onLayout->onDraw
		// ivDrag.layout(lastX, lastY, lastX + ivDrag.getWidth(),
		// lastY + ivDrag.getHeight());

		final int winWidth = getWindowManager().getDefaultDisplay().getWidth();
		final int winHeight = getWindowManager().getDefaultDisplay()
				.getHeight();

		if (lastY > winHeight / 2) {
			tvTop.setVisibility(View.VISIBLE);
			tvBottom.setVisibility(View.INVISIBLE);
		} else {
			tvTop.setVisibility(View.INVISIBLE);
			tvBottom.setVisibility(View.VISIBLE);
		}

		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivDrag
				.getLayoutParams();
		layoutParams.leftMargin = lastX;
		layoutParams.topMargin = lastY;

		ivDrag.setLayoutParams(layoutParams);

		ivDrag.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
				mHits[mHits.length - 1] = SystemClock.uptimeMillis();
				if (mHits[0] >= (SystemClock.uptimeMillis() - 500)) {
					// put the drag view to the center of the window
					ivDrag.layout(winWidth / 2 - ivDrag.getWidth() / 2,
							ivDrag.getTop(), winWidth / 2 + ivDrag.getWidth()
									/ 2, ivDrag.getBottom());
				}
			}
		});

		ivDrag.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					int endX = (int) event.getRawX();
					int endY = (int) event.getRawY();

					int dx = endX - startX;
					int dy = endY - startY;

					int l = ivDrag.getLeft() + dx;
					int r = ivDrag.getRight() + dx;

					int t = ivDrag.getTop() + dy;
					int b = ivDrag.getBottom() + dy;

					if (t > winHeight / 2) {
						tvTop.setVisibility(View.VISIBLE);
						tvBottom.setVisibility(View.INVISIBLE);
					} else {
						tvTop.setVisibility(View.INVISIBLE);
						tvBottom.setVisibility(View.VISIBLE);
					}

					// refresh the screen
					ivDrag.layout(l, t, r, b);

					// initiate start positions
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_UP:
					Editor editor = mPref.edit();
					editor.putInt("lastX", ivDrag.getLeft());
					editor.putInt("lastY", ivDrag.getTop());
					editor.commit();
					break;

				default:
					break;
				}

				return false;
			}
		});
	}
}
