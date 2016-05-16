package com.xfz.mobilesafe.activity;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xfz.mobilesafe.R;
import com.xfz.mobilesafe.adapter.MyBaseAdapter;
import com.xfz.mobilesafe.bean.BlackNumberInfo;
import com.xfz.mobilesafe.db.dao.BlackNumberDao;
import com.xfz.mobilesafe.utils.ToastUtils;

/**
 * 
 * @author xfz:xfz1990@gmail.com
 * @version create time：2016-5-14
 */
public class CallSafeActivity2 extends Activity {
	private ListView list_View;
	private List<BlackNumberInfo> blackNumberInfos;
	private LinearLayout ll_pb;

	private int mCurrentPageNumber = 0;
	private int mPageSize = 20;
	private TextView tv_page_number;
	private BlackNumberDao dao;
	private int totalPage;
	private EditText et_page_number;
	private CallSafeAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_call_safe);
		initUI();
		initData();
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			ll_pb.setVisibility(View.INVISIBLE);
			adapter = new CallSafeAdapter(blackNumberInfos,
					CallSafeActivity2.this);
			tv_page_number.setText(mCurrentPageNumber+1+"/"+totalPage);
			list_View.setAdapter(adapter);
		}
	};

	private void initData() {

		new Thread() {
			@Override
			public void run() {
				super.run();
				dao = new BlackNumberDao(CallSafeActivity2.this);
				totalPage = dao.getTotalNumber() / mPageSize;
				// blackNumberInfos = dao.findAll();
				blackNumberInfos = dao.findPar(mCurrentPageNumber, mPageSize);
				handler.sendEmptyMessage(0);
			}
		}.start();

	}

	private void initUI() {
		ll_pb = (LinearLayout) findViewById(R.id.ll_pb);
		ll_pb.setVisibility(View.VISIBLE);
		list_View = (ListView) findViewById(R.id.list_view);
		tv_page_number = (TextView) findViewById(R.id.tv_page_number);
		et_page_number = (EditText) findViewById(R.id.et_page_number);
	}

	private class CallSafeAdapter extends MyBaseAdapter<BlackNumberInfo> {

		public CallSafeAdapter(List list, Context mContext) {
			super(list, mContext);

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(CallSafeActivity2.this,
						R.layout.item_call_safe, null);
				holder = new ViewHolder();
				holder.tv_number = (TextView) convertView
						.findViewById(R.id.tv_number_item);
				holder.tv_mode = (TextView) convertView
						.findViewById(R.id.tv_mode);
				holder.iv_delete = (ImageView) convertView
						.findViewById(R.id.iv_delete);
				convertView.setTag(holder);
				
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tv_number.setText(list.get(position).getNumber());
			String mode = list.get(position).getMode();
			if (mode.equals("1")) {
				holder.tv_mode.setText("Phone and SMS intercepted");
			} else if (mode.equals("2")) {
				holder.tv_mode.setText("Phone intercepted");
			} else if (mode.equals("3")) {
				holder.tv_mode.setText("SMS intercepted");
			}
			final BlackNumberInfo info = list.get(position);
			holder.iv_delete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String number = info.getNumber();
					boolean result = dao.delete(number);
					if (result) {
						ToastUtils.showToast(CallSafeActivity2.this, "delete succeed");
						list.remove(info);
						//refresh the page
						adapter.notifyDataSetChanged();
						
					}else{
						ToastUtils.showToast(CallSafeActivity2.this, "delete fail");
					}
				}
			});
			return convertView;
		}
	}

	static class ViewHolder {
		TextView tv_number;
		TextView tv_mode;
		ImageView iv_delete;
	}

	/**
	 * pre page
	 * 
	 * @param view
	 */
	public void prePage(View view) {
		if (mCurrentPageNumber <= 0) {
			ToastUtils.showToast(CallSafeActivity2.this,
					"This is the first page");
			return;
		}
		mCurrentPageNumber--;
		initData();
	}

	/**
	 * next page
	 * 
	 * @param view
	 */
	public void nextPage(View view) {
		if (mCurrentPageNumber >= (totalPage - 1)) {
			ToastUtils
					.showToast(CallSafeActivity2.this, "This is the last page");
			return;
		}
		mCurrentPageNumber++;
		initData();
	}

	/**
	 * jump to page
	 * 
	 * @param view
	 */
	public void jump(View view) {
		String str_page_number = et_page_number.getText().toString().trim();
		if (TextUtils.isEmpty(str_page_number)) {
			ToastUtils.showToast(CallSafeActivity2.this,
					"please enter correct number");
		} else {
			int number = Integer.parseInt(str_page_number);
			if (number >= 0 && number < (totalPage - 1)) {
				mCurrentPageNumber = number;
				initData();
			} else {
				ToastUtils.showToast(CallSafeActivity2.this,
						"please enter correct number");
			}
		}
	}
}
