package com.xfz.mobilesafe.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.xfz.mobilesafe.R;

/**
 * 
 * @author xfz:xfz1990@gmail.com
 * @version create time：2016-5-11
 */
public class ContactActivity extends Activity {
	private ListView lvlList;
	private ArrayList<Map<String, String>> readContact;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);
		lvlList = (ListView) findViewById(R.id.lv_list);
		readContact = readContact();
		lvlList.setAdapter(new SimpleAdapter(this, readContact,
				R.layout.contact_list_item, new String[] { "name", "phone" },
				new int[] { R.id.tv_name, R.id.tv_phone }));
		lvlList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String phone = readContact.get(position).get("phone");
				Intent intent = new Intent();
				intent.putExtra("phone", phone);
				setResult(Activity.RESULT_OK, intent);
				finish();
			}
		});
	}

	private ArrayList<Map<String, String>> readContact() {
		// load contact's id from raw_contacts
		Uri rawContactsUri = Uri
				.parse("content://com.android.contacts/raw_contacts");
		Uri dataUri = Uri.parse("content://com.android.contacts/data");
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Cursor rawContactsCursor = getContentResolver().query(rawContactsUri,
				new String[] { "contact_id" }, null, null, null);
		if (rawContactsCursor != null) {
			while (rawContactsCursor.moveToNext()) {
				String contactId = rawContactsCursor.getString(0);
				// load number and contact's name from data depends on contact's
				// id
				Cursor dataCursor = getContentResolver().query(dataUri,
						new String[] { "data1", "mimetype" }, "contact_id=?",
						new String[] { contactId }, null);
				if (dataCursor != null) {
					HashMap<String, String> map = new HashMap<String, String>();
					while (dataCursor.moveToNext()) {
						String data1 = dataCursor.getString(0);
						String mimetype = dataCursor.getString(1);
						// determine which is number and which is name depends
						// on mimetype
						if ("vnd.android.cursor.item/phone_v2".equals(mimetype)) {
							map.put("phone", data1);
						} else if ("vnd.android.cursor.item/name"
								.equals(mimetype)) {
							map.put("name", data1);
						}
					}
					list.add(map);
					dataCursor.close();
				}
			}
			rawContactsCursor.close();
		}
		return list;
	}
}
