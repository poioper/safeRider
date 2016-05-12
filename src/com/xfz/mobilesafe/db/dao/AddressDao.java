package com.xfz.mobilesafe.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Attribution query tool
 * 
 * @author xfz:xfz1990@gmail.com
 * @version create time：2016-5-12
 */
public class AddressDao {
	private static final String PATH = "data/data/com.xfz.mobilesafe/files/address.db";

	public static String getAddress(String number) {
		String address = "unknown number";

		SQLiteDatabase database = SQLiteDatabase.openDatabase(PATH, null,
				SQLiteDatabase.OPEN_READONLY);
		if (number.matches("^1[3-8]\\d{9}$")) {
			Cursor cursor = database
					.rawQuery(
							"select location from data2 where id=(select outkey from data1 where id=?)",
							new String[] { number.substring(0, 7) });
			if (cursor.moveToNext()) {
				address = cursor.getString(0);
			}
			cursor.close();
		}
		database.close();
		return address;
	}
}
