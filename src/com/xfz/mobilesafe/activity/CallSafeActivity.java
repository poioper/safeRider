package com.xfz.mobilesafe.activity;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
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
public class CallSafeActivity extends Activity {
	private ListView list_View;
	private List<BlackNumberInfo> blackNumberInfos;
	private LinearLayout ll_pb;

	private BlackNumberDao dao;
	private CallSafeAdapter adapter;

	/**
	 * the start index of the page
	 */
	private int mStartIndex = 0;

	/**
	 * batch size
	 */
	private int maxCount = 20;

	/**
	 * total items in db
	 */
	private int totalNumber;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_call_safe2);
		initUI();
		initData();
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			ll_pb.setVisibility(View.INVISIBLE);
			if (adapter == null) {
				adapter = new CallSafeAdapter(blackNumberInfos,
						CallSafeActivity.this);
				list_View.setAdapter(adapter);
			}else {
				adapter.notifyDataSetChanged();
			}
			
		}
	};

	private void initData() {
		dao = new BlackNumberDao(CallSafeActivity.this);
		totalNumber = dao.getTotalNumber();
		new Thread() {
			@Override
			public void run() {
				super.run();
				if (blackNumberInfos == null) {
					// load data per batch
					blackNumberInfos = dao.findPar2(mStartIndex, maxCount);
				} else {
					// append coming data to the tail of blackNumberInfos in
					// order to avoid the coming data cover the previous data
					blackNumberInfos
							.addAll(dao.findPar2(mStartIndex, maxCount));
				}
				handler.sendEmptyMessage(0);
			}
		}.start();

	}

	private void initUI() {
		ll_pb = (LinearLayout) findViewById(R.id.ll_pb);
		ll_pb.setVisibility(View.VISIBLE);
		list_View = (ListView) findViewById(R.id.list_view);

		// set list_view's Scroll listener
		list_View.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
					int lastVisiblePosition = list_View
							.getLastVisiblePosition();
					if (lastVisiblePosition == (blackNumberInfos.size()-1)) {
						mStartIndex += maxCount;
						if (lastVisiblePosition >= totalNumber) {
							ToastUtils.showToast(CallSafeActivity.this,
									"no more data");
							return;
						}
						initData();
					}
					
					break;
					
				default:
					break;
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

			}
		});
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
				holder.iv_delete = (ImageView) convertView
						.findViewById(R.id.iv_delete);
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tv_number.setText(list.get(position).getNumber());
			String mode = list.get(position).getMode();
			if (mode.equals(BlackNumberDao.ALL)) {
				holder.tv_mode.setText("Phone and SMS intercepted");
			} else if (mode.equals(BlackNumberDao.CALL)) {
				holder.tv_mode.setText("Phone intercepted");
			} else if (mode.equals(BlackNumberDao.SMS)) {
				holder.tv_mode.setText("SMS intercepted");
			}
			final BlackNumberInfo info = list.get(position);
			holder.iv_delete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String number = info.getNumber();
					boolean result = dao.delete(number);
					if (result) {
						ToastUtils.showToast(CallSafeActivity.this,
								"delete succeed");
						list.remove(info);
						// refresh the page
						adapter.notifyDataSetChanged();

					} else {
						ToastUtils.showToast(CallSafeActivity.this,
								"delete fail");
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
}
