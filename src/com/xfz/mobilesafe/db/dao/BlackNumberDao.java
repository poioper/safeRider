package com.xfz.mobilesafe.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;

import com.xfz.mobilesafe.bean.BlackNumberInfo;

/**
 * 
 * @author xfz:xfz1990@gmail.com
 * @version create time：2016-5-14
 */
public class BlackNumberDao {
	
	public static final String CALL="3";
	public static final String SMS="1";
	public static final String ALL="2";
	
	private BlackNumberOpenHelper helper;

	public BlackNumberDao(Context context) {
		helper = new BlackNumberOpenHelper(context);
	}

	/**
	 * 
	 * @param number
	 * @param mode
	 */
	public boolean add(String number, String mode) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put("number", number);
		contentValues.put("mode", mode);
		long rowid = db.insert("blacknumber", null, contentValues);
		db.close();
		if (rowid == -1) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 
	 * @param number
	 */
	public boolean delete(String number) {
		SQLiteDatabase db = helper.getWritableDatabase();
		int rowNumber = db.delete("blacknumber", "number = ?",
				new String[] { number });
		db.close();
		if (rowNumber == 0) {
			return false;

		} else {
			return true;
		}
	}

	/**
	 * 
	 * @param number
	 */
	public boolean changeNumberMode(String number, String mode) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put("mode", mode);
		int rowNumber = db.update("blacknumber", contentValues, "number = ?",
				new String[] { number });
		db.close();
		if (rowNumber == 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * find the mode for a specific number
	 * @return
	 */
	public String findNumber(String number) {
		String mode = "";
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query("blacknumber", new String[] { "mode" },
				"number=?", new String[] { number }, null, null, null);
		if (cursor.moveToNext()) {
			mode = cursor.getString(0);
		}
		cursor.close();
		db.close();
		return mode;
	}

	/**
	 * find all number in the list
	 * 
	 * @return
	 */
	public List<BlackNumberInfo> findAll() {
		SQLiteDatabase db = helper.getReadableDatabase();
		List<BlackNumberInfo> blackNumberInfos = new ArrayList<BlackNumberInfo>();
		Cursor cursor = db
				.query("blacknumber", new String[] { "number", "mode" }, null,
						null, null, null, null);
		while (cursor.moveToNext()) {
			BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
			blackNumberInfo.setNumber(cursor.getString(0));
			blackNumberInfo.setMode(cursor.getString(1));
			blackNumberInfos.add(blackNumberInfo);
		}
		cursor.close();
		db.close();
		SystemClock.sleep(3000);
		return blackNumberInfos;
	}

	/**
	 * load data per page
	 * 
	 * @param pageNumber
	 *            current page
	 * @param pageSize
	 *            how many data per page
	 * @return
	 */
	public List<BlackNumberInfo> findPar(int pageNumber, int pageSize) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"select number,mode from blacknumber limit ? offset ?",
				new String[] { String.valueOf(pageSize),
						String.valueOf(pageSize * pageNumber) });
		ArrayList<BlackNumberInfo> blackNumberInfos = new ArrayList<BlackNumberInfo>();
		while (cursor.moveToNext()) {
			BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
			blackNumberInfo.setMode(cursor.getString(1));
			blackNumberInfo.setNumber(cursor.getString(0));
			blackNumberInfos.add(blackNumberInfo);
		}
		cursor.close();
		db.close();
		return blackNumberInfos;
	}
	
	/**
	 * load data per batch
	 * @param startIndex the start index of the page
	 * @param maxCount max items per page
	 * @return
	 */
	public List<BlackNumberInfo> findPar2(int startIndex, int maxCount) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"select number,mode from blacknumber limit ? offset ?",
				new String[] { String.valueOf(maxCount),
						String.valueOf(startIndex) });
		ArrayList<BlackNumberInfo> blackNumberInfos = new ArrayList<BlackNumberInfo>();
		while (cursor.moveToNext()) {
			BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
			blackNumberInfo.setMode(cursor.getString(1));
			blackNumberInfo.setNumber(cursor.getString(0));
			blackNumberInfos.add(blackNumberInfo);
		}
		cursor.close();
		db.close();
		return blackNumberInfos;
	}
	
	public int getTotalNumber(){
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select count(*) from blacknumber", null);
		cursor.moveToNext();
		int res = cursor.getInt(0);
		cursor.close();
		db.close();
		return res;
	}
}
