package com.xfz.mobilesafe.activity;

import com.xfz.mobilesafe.R;
import com.xfz.mobilesafe.utils.MD5Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * main page
 * 
 * @author 99167
 * 
 */
public class HomeActivity extends Activity {
	private GridView gvHome;
	private String[] mItems = new String[] { "home safe", "call safe",
			"software manager", "task manager", "Virus Killer",
			"Flow Statistics", "Cache Cleaner", "Advanced Tools", "Setting" };
	private int[] mPics = new int[] { R.drawable.home_safe,
			R.drawable.home_callmsgsafe, R.drawable.home_apps,
			R.drawable.home_taskmanager, R.drawable.home_trojan,
			R.drawable.home_netmanager, R.drawable.home_sysoptimize,
			R.drawable.home_tools, R.drawable.home_settings };
	private SharedPreferences mPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		mPref = getSharedPreferences("config", MODE_PRIVATE);
		gvHome = (GridView) findViewById(R.id.gv_home);
		gvHome.setAdapter(new HomeAdapter());
		// set listener
		gvHome.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					showPasswordDialog();
					break;
				case 7:
					startActivity(new Intent(HomeActivity.this,
							AToolsActivity.class));
					break;
				case 8:
					// go to setting page
					startActivity(new Intent(HomeActivity.this,
							SettingActivity.class));
					break;

				default:
					break;
				}
			}
		});
	}

	/**
	 * show password dialog
	 */
	protected void showPasswordDialog() {
		// determine has password or not
		String savedPassword = mPref.getString("password", null);
		if (!TextUtils.isEmpty(savedPassword)) {
			showPasswordInputDialog();
		} else {
			// if not, prumpt a password dialog
			showPasswordSetDialog();
		}
	}

	/**
	 * enter password dialog
	 */
	private void showPasswordInputDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder.create();
		View view = View.inflate(this, R.layout.dailog_input_password, null);
		dialog.setView(view, 0, 0, 0, 0);
		Button btnOK = (Button) view.findViewById(R.id.btn_ok);
		Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
		final EditText etPassword = (EditText) view
				.findViewById(R.id.et_password);
		btnOK.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String password = etPassword.getText().toString();
				if (TextUtils.isEmpty(password)) {
					Toast.makeText(HomeActivity.this,
							"content can't be empty!", Toast.LENGTH_SHORT)
							.show();
				} else {
					String safedPassword = mPref.getString("password", null);
					if (MD5Utils.encode(password).equals(safedPassword)) {
						// Toast.makeText(HomeActivity.this, "success!",
						// Toast.LENGTH_SHORT).show();
						dialog.dismiss();
						// go to theft proof page
						startActivity(new Intent(HomeActivity.this,
								LostFindActivity.class));
					} else {
						Toast.makeText(HomeActivity.this, "wrong password!",
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	/**
	 * show password set dialog
	 */
	private void showPasswordSetDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder.create();
		View view = View.inflate(this, R.layout.dailog_set_password, null);
		dialog.setView(view, 0, 0, 0, 0);
		Button btnOK = (Button) view.findViewById(R.id.btn_ok);
		Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
		final EditText etPassword = (EditText) view
				.findViewById(R.id.et_password);
		final EditText etPasswordConfirm = (EditText) view
				.findViewById(R.id.et_password_confirm);
		btnOK.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String password = etPassword.getText().toString();
				String passwordConfirm = etPasswordConfirm.getText().toString();
				if (!TextUtils.isEmpty(password)
						&& !TextUtils.isEmpty(passwordConfirm)) {
					if (password.equals(passwordConfirm)) {
						// Toast.makeText(HomeActivity.this, "success!",
						// Toast.LENGTH_SHORT).show();
						dialog.dismiss();
						mPref.edit()
								.putString("password",
										MD5Utils.encode(password)).commit();
						// go to theft proof page
						startActivity(new Intent(HomeActivity.this,
								LostFindActivity.class));
					} else {
						Toast.makeText(HomeActivity.this,
								"passwords don't match!", Toast.LENGTH_SHORT)
								.show();
					}
				} else {
					Toast.makeText(HomeActivity.this,
							"content can't be empty!", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	class HomeAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mItems.length;
		}

		@Override
		public Object getItem(int position) {
			return mItems[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View converView, ViewGroup parent) {
			View view = View.inflate(HomeActivity.this,
					R.layout.home_list_item, null);
			ImageView ivItem = (ImageView) view.findViewById(R.id.iv_item);
			TextView tvItem = (TextView) view.findViewById(R.id.tv_item);
			tvItem.setText(mItems[position]);
			ivItem.setImageResource(mPics[position]);
			return view;
		}

	}
}
