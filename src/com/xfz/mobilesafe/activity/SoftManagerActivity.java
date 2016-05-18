package com.xfz.mobilesafe.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xfz.mobilesafe.R;
import com.xfz.mobilesafe.bean.AppInfo;
import com.xfz.mobilesafe.engine.AppEngine;
import com.xfz.mobilesafe.utils.AppUtil;
import com.xfz.mobilesafe.utils.MyAsycnTaks;

/**
 * 
 * @author xfz:xfz1990@gmail.com
 * @version create time：2016-5-16
 */
public class SoftManagerActivity extends Activity implements OnClickListener {

	private ListView lv_softmanager_application;
	private ProgressBar loading;
	private List<AppInfo> list;
	private List<AppInfo> userappinfo;
	private List<AppInfo> systemappinfo;
	private TextView tv_softmanager_userorsystem;
	private AppInfo appInfo;
	private PopupWindow popupWindow;
	private Mydapter mydapter;
	private TextView tv_softmanager_rom;
	private TextView tv_softmanager_sd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//init wedgets
		setContentView(R.layout.activity_softmanager);
		lv_softmanager_application = (ListView) findViewById(R.id.lv_softmanager_application);
		loading = (ProgressBar) findViewById(R.id.loading);
		tv_softmanager_userorsystem = (TextView) findViewById(R.id.tv_softmanager_userorsystem);
		tv_softmanager_rom = (TextView) findViewById(R.id.tv_softmanager_rom);
		tv_softmanager_sd = (TextView) findViewById(R.id.tv_softmanager_sd);
		
		//obtain available storage
		long availableSD = AppUtil.getAvailableSD();
		long availableROM = AppUtil.getAvailableROM();
		
		//turn into MB
		String sdsize = Formatter.formatFileSize(getApplicationContext(), availableSD);
		String romsize = Formatter.formatFileSize(getApplicationContext(), availableROM);
		
		//show result
		tv_softmanager_sd.setText("sd card:"+sdsize);
		tv_softmanager_rom.setText("phone storage:"+romsize);
		
		//load data
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

						// 初始化控件
						LinearLayout ll_popuwindow_uninstall = (LinearLayout) contentView
								.findViewById(R.id.ll_popuwindow_uninstall);
						LinearLayout ll_popuwindow_start = (LinearLayout) contentView
								.findViewById(R.id.ll_popuwindow_start);
						LinearLayout ll_popuwindow_share = (LinearLayout) contentView
								.findViewById(R.id.ll_popuwindow_share);
						LinearLayout ll_popuwindow_detail = (LinearLayout) contentView
								.findViewById(R.id.ll_popuwindow_detail);
						// 给控件设置点击事件
						ll_popuwindow_uninstall
								.setOnClickListener(SoftManagerActivity.this);
						ll_popuwindow_start
								.setOnClickListener(SoftManagerActivity.this);
						ll_popuwindow_share
								.setOnClickListener(SoftManagerActivity.this);
						ll_popuwindow_detail
								.setOnClickListener(SoftManagerActivity.this);

						popupWindow = new PopupWindow(contentView,
								LayoutParams.WRAP_CONTENT,
								LayoutParams.WRAP_CONTENT);
						// must have backgroud to execute animation
						popupWindow.setBackgroundDrawable(new ColorDrawable(
								Color.TRANSPARENT));
						// 4.get the item's position
						int[] location = new int[2];
						view.getLocationInWindow(location);
						int x = location[0];
						int y = location[1];

						popupWindow.showAtLocation(parent, Gravity.LEFT
								| Gravity.TOP, x + 70, y - 20);

						// 6. animation
						ScaleAnimation scaleAnimation = new ScaleAnimation(0,
								1, 0, 1, Animation.RELATIVE_TO_SELF, 0,
								Animation.RELATIVE_TO_SELF, 0.5f);
						scaleAnimation.setDuration(500);

						AlphaAnimation alphaAnimation = new AlphaAnimation(
								0.4f, 1.0f);
						alphaAnimation.setDuration(500);

						AnimationSet animationSet = new AnimationSet(true);
						animationSet.addAnimation(scaleAnimation);
						animationSet.addAnimation(alphaAnimation);

						contentView.startAnimation(animationSet);
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
				if (mydapter == null) {
					mydapter = new Mydapter();
					lv_softmanager_application.setAdapter(mydapter);
				}else {
					mydapter.notifyDataSetChanged();
				}
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_popuwindow_uninstall:
			System.out.println("uninstall");
			uninstall();
			break;
		case R.id.ll_popuwindow_start:
			System.out.println("open");
			start();
			break;
		case R.id.ll_popuwindow_share:
			System.out.println("share");
			share();
			break;
		case R.id.ll_popuwindow_detail:
			System.out.println("detail");
			detail();
			break;
		}
		hidePopuwindow();
	}

	/**
	 * share
	 */
	private void share() {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.SEND");
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, "great app "+ appInfo.getName() + "");
		startActivity(intent);
	}

	/**  
	 * detail
	 */
	private void detail() {
		/**
		 * Intent { act=android.settings.APPLICATION_DETAILS_SETTINGS action
		 * dat=package:com.example.android.apis data
		 * cmp=com.android.settings/.applications.InstalledAppDetails } from pid
		 * 228
		 */
		Intent intent = new Intent();
		intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
		intent.setData(Uri.parse("package:" + appInfo.getPackageName()));
		startActivity(intent);
	}

	/**
	 * start
	 */
	private void start() {
		PackageManager pm = getPackageManager();
		// get intent
		Intent intent = pm.getLaunchIntentForPackage(appInfo.getPackageName());
		if (intent != null) {
			startActivity(intent);
		} else {
			Toast.makeText(getApplicationContext(), "core app, can't start", 0).show();
		}
	}

	/**
	 * uninstall
	 */
	private void uninstall() {
		/**
		 * <intent-filter> <action android:name="android.intent.action.VIEW" />
		 * <action android:name="android.intent.action.DELETE" /> <category
		 * android:name="android.intent.category.DEFAULT" /> <data
		 * android:scheme="package" /> </intent-filter>
		 */
		// whether a system app or not
		if (appInfo.isUser()) {
			if (!appInfo.getPackageName().equals(getPackageName())) {
				Intent intent = new Intent();
				intent.setAction("android.intent.action.DELETE");
				intent.addCategory("android.intent.category.DEFAULT");
				intent.setData(Uri.parse("package:" + appInfo.getPackageName()));// tel:110
				startActivityForResult(intent, 0);
			} else {
				Toast.makeText(getApplicationContext(),
						"kill me???", 0).show();
			}
		} else {
			Toast.makeText(getApplicationContext(),
					"system app, can't uninstall", 0).show();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		fillData();
	}
}
