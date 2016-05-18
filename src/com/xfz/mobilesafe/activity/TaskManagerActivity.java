package com.xfz.mobilesafe.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xfz.mobilesafe.R;
import com.xfz.mobilesafe.bean.TaskInfo;
import com.xfz.mobilesafe.engine.TaskEngine;
import com.xfz.mobilesafe.utils.MyAsycnTaks;

public class TaskManagerActivity extends Activity {

	private ListView lv_taskmanager_processes;
	private ProgressBar loading;
	private List<TaskInfo> list;
	private List<TaskInfo> userappinfo;
	private List<TaskInfo> systemappinfo;
	private Myadapter myadapter;
	private TaskInfo taskInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_taskmanager);
		lv_taskmanager_processes = (ListView) findViewById(R.id.lv_taskmanager_processes);
		loading = (ProgressBar) findViewById(R.id.loading);

		// load data
		fillData();
		listviewItemClick();
	}

	/**
	 * listview条目点击事件
	 */
	private void listviewItemClick() {
		lv_taskmanager_processes
				.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						if (position == 0 || position == userappinfo.size() + 1) {
							return;
						}
						if (position <= userappinfo.size()) {
							taskInfo = userappinfo.get(position - 1);
						} else {
							taskInfo = systemappinfo.get(position
									- userappinfo.size() - 2);
						}
						if (taskInfo.isChecked()) {
							taskInfo.setChecked(false);
						} else {
							if (!taskInfo.getPackageName().equals(
									getPackageName())) {
								taskInfo.setChecked(true);
							}
						}
						ViewHolder viewHolder = (ViewHolder) view.getTag();
						viewHolder.cb_itemtaskmanager_ischecked
								.setChecked(taskInfo.isChecked());
					}
				});
	}

	/**
	 * load data
	 */
	private void fillData() {
		new MyAsycnTaks() {

			@Override
			public void preTask() {
				loading.setVisibility(View.VISIBLE);
			}

			@Override
			public void postTask() {
				if (myadapter == null) {
					myadapter = new Myadapter();
					lv_taskmanager_processes.setAdapter(myadapter);
				} else {
					myadapter.notifyDataSetChanged();
				}
				loading.setVisibility(View.INVISIBLE);
			}

			@Override
			public void doinBack() {
				list = TaskEngine.getTaskAllInfo(getApplicationContext());
				userappinfo = new ArrayList<TaskInfo>();
				systemappinfo = new ArrayList<TaskInfo>();
				for (TaskInfo taskinfo : list) {
					if (taskinfo.isUser()) {
						userappinfo.add(taskinfo);
					} else {
						systemappinfo.add(taskinfo);
					}
				}
			}
		}.execute();
	}

	private class Myadapter extends BaseAdapter {

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
				TextView textView = new TextView(getApplicationContext());
				textView.setBackgroundColor(Color.GRAY);
				textView.setTextColor(Color.WHITE);
				textView.setText("user process(" + userappinfo.size() + ")");
				return textView;
			} else if (position == userappinfo.size() + 1) {
				TextView textView = new TextView(getApplicationContext());
				textView.setBackgroundColor(Color.GRAY);
				textView.setTextColor(Color.WHITE);
				textView.setText("system process(" + systemappinfo.size() + ")");
				return textView;
			}

			View view;
			ViewHolder viewHolder;
			if (convertView != null && convertView instanceof RelativeLayout) {
				view = convertView;
				viewHolder = (ViewHolder) view.getTag();
			} else {
				view = View.inflate(getApplicationContext(),
						R.layout.item_taskmanager, null);
				viewHolder = new ViewHolder();
				viewHolder.iv_itemtaskmanager_icon = (ImageView) view
						.findViewById(R.id.iv_itemtaskmanager_icon);
				viewHolder.tv_itemtaskmanager_name = (TextView) view
						.findViewById(R.id.tv_itemtaskmanager_name);
				viewHolder.tv_itemtaskmanager_ram = (TextView) view
						.findViewById(R.id.tv_itemtaskmanager_ram);
				viewHolder.cb_itemtaskmanager_ischecked = (CheckBox) view
						.findViewById(R.id.cb_itemtaskmanager_ischecked);
				view.setTag(viewHolder);
			}
			TaskInfo taskinfo = null;
			if (position <= userappinfo.size()) {
				taskinfo = userappinfo.get(position - 1);
			} else {
				taskinfo = systemappinfo.get(position - userappinfo.size() - 2);
			}
			
			//icon is empty
			if (taskinfo.getIcon() == null) {
				viewHolder.iv_itemtaskmanager_icon
						.setImageResource(R.drawable.ic_default);
			} else {
				viewHolder.iv_itemtaskmanager_icon.setImageDrawable(taskinfo
						.getIcon());
			}
			//name is empty
			if (TextUtils.isEmpty(taskinfo.getName())) {
				viewHolder.tv_itemtaskmanager_name.setText(taskinfo
						.getPackageName());
			} else {
				viewHolder.tv_itemtaskmanager_name.setText(taskinfo.getName());
			}

			long ramSize = taskinfo.getRamSize();
			String formatFileSize = Formatter.formatFileSize(
					getApplicationContext(), ramSize);
			viewHolder.tv_itemtaskmanager_ram.setText("RAM occupation:" + formatFileSize);

			if (taskinfo.isChecked()) {
				viewHolder.cb_itemtaskmanager_ischecked.setChecked(true);
			} else {
				viewHolder.cb_itemtaskmanager_ischecked.setChecked(false);
			}
			if (taskinfo.getPackageName().equals(getPackageName())) {
				viewHolder.cb_itemtaskmanager_ischecked
						.setVisibility(View.INVISIBLE);
			} else {
				viewHolder.cb_itemtaskmanager_ischecked
						.setVisibility(View.VISIBLE);
			}
			return view;
		}

	}

	static class ViewHolder {
		ImageView iv_itemtaskmanager_icon;
		TextView tv_itemtaskmanager_name, tv_itemtaskmanager_ram;
		CheckBox cb_itemtaskmanager_ischecked;
	}

	/**
	 * select all
	 * 
	 * @param v
	 */
	public void all(View v) {
		// user process
		for (int i = 0; i < userappinfo.size(); i++) {
			if (!userappinfo.get(i).getPackageName().equals(getPackageName())) {
				userappinfo.get(i).setChecked(true);
			}
		}
		// system process
		for (int i = 0; i < systemappinfo.size(); i++) {
			systemappinfo.get(i).setChecked(true);
		}
		// update page
		myadapter.notifyDataSetChanged();
	}

	/**
	 * cancel
	 * 
	 * @param v
	 */
	public void cancel(View v) {
		// user process
		for (int i = 0; i < userappinfo.size(); i++) {
			userappinfo.get(i).setChecked(false);
		}
		// system process
		for (int i = 0; i < systemappinfo.size(); i++) {
			systemappinfo.get(i).setChecked(false);
		}
		// update
		myadapter.notifyDataSetChanged();
	}

	/**
	 * clean
	 * 
	 * @param v
	 */
	public void clear(View v) {
		ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		List<TaskInfo> deleteTaskInfos = new ArrayList<TaskInfo>();

		for (int i = 0; i < userappinfo.size(); i++) {
			if (userappinfo.get(i).isChecked()) {
				am.killBackgroundProcesses(userappinfo.get(i).getPackageName());
				deleteTaskInfos.add(userappinfo.get(i));
			}
		}
		for (int i = 0; i < systemappinfo.size(); i++) {
			if (systemappinfo.get(i).isChecked()) {
				am.killBackgroundProcesses(systemappinfo.get(i)
						.getPackageName());
				deleteTaskInfos.add(systemappinfo.get(i));
			}
		}
		long memory = 0;
		for (TaskInfo taskInfo : deleteTaskInfos) {
			if (taskInfo.isUser()) {
				userappinfo.remove(taskInfo);
			} else {
				systemappinfo.remove(taskInfo);
			}
			memory += taskInfo.getRamSize();
		}
		String deletesize = Formatter.formatFileSize(getApplicationContext(),
				memory);
		Toast.makeText(
				getApplicationContext(),
				"cleaned " + deleteTaskInfos.size() + " processes, relased " + deletesize + " memory",
				0).show();

		deleteTaskInfos.clear();
		deleteTaskInfos = null;
		myadapter.notifyDataSetChanged();
	}

	/**
	 * setting
	 * 
	 * @param v
	 */
	public void setting(View v) {

	}

}
