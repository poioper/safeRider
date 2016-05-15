package com.xfz.mobilesafe.adapter;

import java.util.List;

import android.content.Context;
import android.widget.BaseAdapter;

/** 
 * 
 * @author xfz:xfz1990@gmail.com 
 * @version create time：2016-5-15 
 */
public abstract class MyBaseAdapter<T> extends BaseAdapter {

	public List<T> list;
	public Context mContext;
	
	public MyBaseAdapter(List<T> list, Context mContext) {
		super();
		this.list = list;
		this.mContext = mContext;
	}
	

	public MyBaseAdapter() {
		super();
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
}
