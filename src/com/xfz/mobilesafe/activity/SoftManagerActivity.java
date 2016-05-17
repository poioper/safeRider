package com.xfz.mobilesafe.activity;

import java.util.List;

import com.xfz.mobilesafe.R;
import com.xfz.mobilesafe.bean.AppInfo;
import com.xfz.mobilesafe.engine.AppEngine;
import com.xfz.mobilesafe.utils.MyAsycnTaks;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 
 * @author xfz:xfz1990@gmail.com
 * @version create time：2016-5-16
 */
public class SoftManagerActivity extends Activity {

	private ListView lv_softmanager_application;
	private ProgressBar loading;
	private List<AppInfo> list;

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
			}
		}.execute();
	}

	private class Mydapter extends BaseAdapter {

		@Override
		public int getCount() {
			return list.size();
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
			View view;
			ViewHolder viewHolder;
			if (convertView != null) {
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
			AppInfo appInfo = list.get(position);
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
