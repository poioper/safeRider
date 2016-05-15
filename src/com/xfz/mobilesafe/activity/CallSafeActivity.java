package com.xfz.mobilesafe.activity;

import java.util.List;

import com.xfz.mobilesafe.R;
import com.xfz.mobilesafe.adapter.MyBaseAdapter;
import com.xfz.mobilesafe.bean.BlackNumberInfo;
import com.xfz.mobilesafe.db.dao.BlackNumberDao;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 
 * @author xfz:xfz1990@gmail.com
 * @version create time：2016-5-14
 */
public class CallSafeActivity extends Activity {
	private ListView list_View;
	private List<BlackNumberInfo> blackNumberInfos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_call_safe);
		initUI();
		initData();
	}

	private void initData() {
		BlackNumberDao dao = new BlackNumberDao(this);
		blackNumberInfos = dao.findAll();
		CallSafeAdapter adapter = new CallSafeAdapter(blackNumberInfos,this);
		list_View.setAdapter(adapter);
	}

	private void initUI() {
		list_View = (ListView) findViewById(R.id.list_view);
	}

	private class CallSafeAdapter extends MyBaseAdapter<BlackNumberInfo> {

		public CallSafeAdapter(List list, Context mContext) {
			super(list, mContext);
			
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(CallSafeActivity.this,
						R.layout.item_call_safe, null);
				holder = new ViewHolder();
				holder.tv_number = (TextView) convertView
						.findViewById(R.id.tv_number_item);
				holder.tv_mode = (TextView) convertView
						.findViewById(R.id.tv_mode);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tv_number
					.setText(list.get(position).getNumber());
			String mode = list.get(position).getMode();
			if (mode.equals("1")) {
				holder.tv_mode.setText("Phone and SMS intercepted");
			} else if (mode.equals("2")) {
				holder.tv_mode.setText("Phone intercepted");
			} else if (mode.equals("3")) {
				holder.tv_mode.setText("SMS intercepted");
			}
			return convertView;
		}
	}

	static class ViewHolder {
		TextView tv_number;
		TextView tv_mode;
	}
}
