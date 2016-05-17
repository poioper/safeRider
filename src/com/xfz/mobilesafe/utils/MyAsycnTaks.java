package com.xfz.mobilesafe.utils;

import android.os.Handler;

public abstract class MyAsycnTaks {
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			postTask();
		};
	};

	public abstract void preTask();

	public abstract void doinBack();

	public abstract void postTask();

	public void execute() {
		preTask();
		new Thread() {
			public void run() {
				doinBack();
				handler.sendEmptyMessage(0);
			};
		}.start();
	}
}
