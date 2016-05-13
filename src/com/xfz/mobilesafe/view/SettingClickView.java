package com.xfz.mobilesafe.view;

import com.xfz.mobilesafe.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * setting page's view
 * @author xfz
 *
 */
public class SettingClickView extends RelativeLayout {

	private TextView tvTitle;
	private TextView tvDesc;

	public SettingClickView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	public SettingClickView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public SettingClickView(Context context) {
		super(context);
		initView();
	}

	/**
	 * initiate View
	 */
	private void initView() {
		View.inflate(getContext(), R.layout.view_setting_click, this);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		tvDesc = (TextView) findViewById(R.id.tv_desc);

	}

	public void setTitle(String title) {
		tvTitle.setText(title);
	}

	public void setDesc(String desc) {
		tvDesc.setText(desc);
	}
}
