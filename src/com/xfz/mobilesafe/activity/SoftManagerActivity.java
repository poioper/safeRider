package com.xfz.mobilesafe.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

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
	private TextView tv_softmanager_userorsystem;
	private AppInfo appInfo;
	private PopupWindow popupWindow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_softmanager);
		lv_softmanager_application = (ListView) findViewById(R.id.lv_softmanager_application);
		loading = (ProgressBar) findViewById(R.id.loading);
		tv_softmanager_userorsystem = (TextView) findViewById(R.id.tv_softmanager_userorsystem);
		fillData();
		listviewOnscroll();
		listviewItemClick();
	}

	/**
	 * item click event
	 */
	private void listviewItemClick() {
		lv_softmanager_application
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// 1.skip textview
						if (position == 0 || position == userappinfo.size() + 1) {
							return;
						}
						// 2.determine user or sys app
						if (position <= userappinfo.size()) {
							appInfo = userappinfo.get(position - 1);
						} else {
							appInfo = systemappinfo.get(position
									- userappinfo.size() - 2);
						}
						// 5.delete old one
						hidePopuwindow();
						// 3.pop up bubble
						View contentView = View.inflate(
								getApplicationContext(), R.layout.pop_window,
								null);
						popupWindow = new PopupWindow(contentView,
								LayoutParams.WRAP_CONTENT,
								LayoutParams.WRAP_CONTENT);
						// 4.get the item's position
						int[] location = new int[2];
						view.getLocationInWindow(location);
						int x = location[0];
						int y = location[1];

						popupWindow.showAtLocation(parent, Gravity.LEFT
								| Gravity.TOP, x + 70, y - 20);
					}
				});
	}

	private void hidePopuwindow() {
		if (popupWindow != null) {
			popupWindow.dismiss();
			popupWindow = null;
		}
	}

	/**
	 * listview scroll listener
	 */
	private void listviewOnscroll() {
		lv_softmanager_application.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				hidePopuwindow();
				if (userappinfo != null && systemappinfo != null) {
					if (firstVisibleItem >= userappinfo.size() + 1) {
						tv_softmanager_userorsystem.setText("system apps("
								+ systemappinfo.size() + ")");
					} else {
						tv_softmanager_userorsystem.setText("user apps("
								+ userappinfo.size() + ")");
					}
				}
			}
		});
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

	protected void onDestroy() {
		super.onDestroy();
		hidePopuwindow();
	}
}
