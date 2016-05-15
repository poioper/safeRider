package com.xfz.mobilesafe.activity;

import java.util.List;

import com.xfz.mobilesafe.R;
import com.xfz.mobilesafe.bean.BlackNumberInfo;
import com.xfz.mobilesafe.db.dao.BlackNumberDao;

import android.app.Activity;
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
		CallSafeAdapter adapter = new CallSafeAdapter();
		list_View.setAdapter(adapter);
		
	}

	private void initUI() {
		list_View = (ListView) findViewById(R.id.list_view);
	}

	private class CallSafeAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return blackNumberInfos.size();
		}

		@Override
		public Object getItem(int position) {
			return blackNumberInfos.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(CallSafeActivity.this, R.layout.item_call_safe, null);
			TextView tv_number = (TextView) view.findViewById(R.id.tv_number_item);
			TextView tv_mode = (TextView) view.findViewById(R.id.tv_mode);
			tv_number.setText(blackNumberInfos.get(position).getNumber());
			tv_mode.setText(blackNumberInfos.get(position).getMode());
			return view;
		}

	}
}
