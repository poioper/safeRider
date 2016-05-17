package com.xfz.mobilesafe.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xfz.mobilesafe.R;
import com.xfz.mobilesafe.bean.AppInfo;
import com.xfz.mobilesafe.engine.AppEngine;
import com.xfz.mobilesafe.utils.MyAsycnTaks;

/**
 * 
 * @author xfz:xfz1990@gmail.com
 * @version create time：2016-5-16
 */
public class SoftManagerActivity extends Activity {

	private ListView lv_softmanager_application;
	private ProgressBar loading;
	private List<AppInfo> list;
	private List<AppInfo> userappinfo;
	private List<AppInfo> systemappinfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_softmanager);
		lv_softmanager_application = (ListView) findViewById(R.id.lv_softmanager_application);
		loading = (ProgressBar) findViewById(R.id.loading);
		fillData();
	}

	private void fillData() {
		new MyAsycnTaks() {

			@Override
			public void preTask() {
				loading.setVisibility(View.VISIBLE);
			}

			@Override
			public void postTask() {
				lv_softmanager_application.setAdapter(new Mydapter());
				loading.setVisibility(View.INVISIBLE);
			}

			@Override
			public void doinBack() {
				list = AppEngine.getAppInfos(getApplicationContext());
				userappinfo = new ArrayList<AppInfo>();
				systemappinfo = new ArrayList<AppInfo>();
				for (AppInfo appinfo : list) {
					if (appinfo.isUser()) {
						userappinfo.add(appinfo);
					} else {
						systemappinfo.add(appinfo);
					}
				}
			}
		}.execute();
	}

	private class Mydapter extends BaseAdapter {

		@Override
		public int getCount() {
			return userappinfo.size() + systemappinfo.size() + 2;
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (position == 0) {
				// add user app count textview
				TextView textView = new TextView(getApplicationContext());
				textView.setBackgroundColor(Color.GRAY);
				textView.setTextColor(Color.WHITE);
				textView.setText("user apps(" + userappinfo.size() + ")");
				return textView;
			} else if (position == userappinfo.size() + 1) {
				// add sys app counttextview
				TextView textView = new TextView(getApplicationContext());
				textView.setBackgroundColor(Color.GRAY);
				textView.setTextColor(Color.WHITE);
				textView.setText("system apps(" + systemappinfo.size() + ")");
				return textView;
			}

			View view;
			ViewHolder viewHolder;
			if (convertView != null && convertView instanceof RelativeLayout) {
				view = convertView;
				viewHolder = (ViewHolder) view.getTag();
			} else {
				view = View.inflate(getApplicationContext(),
						R.layout.item_softmanager, null);
				viewHolder = new ViewHolder();
				viewHolder.iv_itemsoftmanage_icon = (ImageView) view
						.findViewById(R.id.iv_itemsoftmanage_icon);
				viewHolder.tv_softmanager_name = (TextView) view
						.findViewById(R.id.tv_softmanager_name);
				viewHolder.tv_softmanager_issd = (TextView) view
						.findViewById(R.id.tv_softmanager_issd);
				viewHolder.tv_softmanager_version = (TextView) view
						.findViewById(R.id.tv_softmanager_version);
				view.setTag(viewHolder);
			}
			AppInfo appInfo = null;
			// load data from userappinfo and systemappinfo
			if (position <= userappinfo.size()) {
				// user app
				appInfo = userappinfo.get(position - 1);
			} else {
				// system app
				appInfo = systemappinfo.get(position - userappinfo.size() - 2);
			}

			viewHolder.iv_itemsoftmanage_icon.setImageDrawable(appInfo
					.getIcon());
			viewHolder.tv_softmanager_name.setText(appInfo.getName());
			boolean sd = appInfo.isSD();
			if (sd) {
				viewHolder.tv_softmanager_issd.setText("SD card");
			} else {
				viewHolder.tv_softmanager_issd.setText("phone storage");
			}
			viewHolder.tv_softmanager_version.setText(appInfo.getVersionName());
			return view;
		}
	}

	static class ViewHolder {
		ImageView iv_itemsoftmanage_icon;
		TextView tv_softmanager_name, tv_softmanager_issd,
				tv_softmanager_version;
	}
}
