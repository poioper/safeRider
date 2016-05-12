package com.xfz.mobilesafe.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.xfz.mobilesafe.R;
import com.xfz.mobilesafe.utils.StreamUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SplashActivity extends Activity {
	protected static final int CODE_UPDATE_DIALOG = 0;
	protected static final int CODE_URL_ERROR = 1;
	protected static final int CODE_NET_ERROR = 2;
	protected static final int CODE_JSON_ERROR = 3;
	protected static final int CODE_ENTER_HOME = 4;// Enter the main page

	private TextView tvVersion;
	private TextView tvProgress;// download progress
	private SharedPreferences mpref;
	private RelativeLayout rlRoot;

	// Server info
	private String mVersionName;
	private int mVersionCode;
	private String mDesc;
	private String mDownloadUrl;
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case CODE_UPDATE_DIALOG:
				showUpdateDialog();
				break;
			case CODE_URL_ERROR:
				Toast.makeText(SplashActivity.this, "Url error",
						Toast.LENGTH_SHORT).show();
				enterHome();
				break;
			case CODE_NET_ERROR:
				Toast.makeText(SplashActivity.this, "Networking error",
						Toast.LENGTH_SHORT).show();
				enterHome();
				break;
			case CODE_JSON_ERROR:
				Toast.makeText(SplashActivity.this, "Json error",
						Toast.LENGTH_SHORT).show();
				enterHome();
				break;
			case CODE_ENTER_HOME:
				enterHome();
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		tvVersion = (TextView) findViewById(R.id.tv_version);
		tvVersion.setText("Version:" + getVersionName());
		tvProgress = (TextView) findViewById(R.id.tv_progress);// visibility =
																// false
		rlRoot = (RelativeLayout) findViewById(R.id.rl_root);
		// determine weather auto-update is needed or not
		mpref = getSharedPreferences("config", MODE_PRIVATE);
		
		copyDB("address.db");
		
		boolean autoUpdate = mpref.getBoolean("auto_update", true);
		if (autoUpdate) {
			checkVersion();
		} else {
			mHandler.sendEmptyMessageDelayed(CODE_ENTER_HOME, 2000);// after 2s,
																	// send
																	// message
		}
		// flash of splash page
		AlphaAnimation anim = new AlphaAnimation(0.3f, 1f);
		anim.setDuration(2000);
		rlRoot.startAnimation(anim);
	}

	/**
	 * get the local App's Version name
	 * 
	 * @return
	 */
	private String getVersionName() {
		PackageManager manager = getPackageManager();
		try {
			PackageInfo packageInfo = manager.getPackageInfo(getPackageName(),
					0);
			String versionName = packageInfo.versionName;
			return versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * get the local App's version code
	 * 
	 * @return
	 */
	private int getVersionCode() {
		PackageManager manager = getPackageManager();
		try {
			PackageInfo packageInfo = manager.getPackageInfo(getPackageName(),
					0);
			int code = packageInfo.versionCode;
			return code;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return -1;
	}

	private void checkVersion() {
		final long startTime = System.currentTimeMillis();
		new Thread() {
			@Override
			public void run() {
				Message msg = Message.obtain();
				HttpURLConnection conn = null;
				try {
					URL url = new URL("http://10.0.2.2:8080/update.json");
					conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET");
					conn.setReadTimeout(5000);
					conn.setConnectTimeout(5000);
					conn.connect();

					int responseCode = conn.getResponseCode();
					if (responseCode == 200) {
						InputStream inputStream = conn.getInputStream();
						String result = StreamUtils.readStream(inputStream);
						JSONObject jo = new JSONObject(result);
						mVersionName = jo.getString("versionName");
						mDesc = jo.getString("description");
						mDownloadUrl = jo.getString("downloadUrl");
						mVersionCode = jo.getInt("versionCode");
						if (mVersionCode > getVersionCode()) {// update
																// available
							msg.what = CODE_UPDATE_DIALOG;
						} else {
							// no update available
							msg.what = CODE_ENTER_HOME;
						}
					}
				} catch (MalformedURLException e) {
					msg.what = CODE_URL_ERROR;
					e.printStackTrace();
				} catch (IOException e) {
					msg.what = CODE_NET_ERROR;
					e.printStackTrace();
				} catch (JSONException e) {
					msg.what = CODE_JSON_ERROR;
					e.printStackTrace();
				} finally {
					long endTime = System.currentTimeMillis();
					long timeUsed = endTime - startTime;
					// force to sleep 2s in order to show splash page.
					if (timeUsed < 2000) {
						try {
							Thread.sleep(2000 - timeUsed);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					mHandler.sendMessage(msg);
					if (conn != null) {
						conn.disconnect();
					}
				}
			}
		}.start();
	}

	/**
	 * show update dialog
	 */
	protected void showUpdateDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("latest version:" + mVersionName);
		builder.setMessage(mDesc);
		builder.setPositiveButton("update", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				download();
			}
		});
		builder.setNegativeButton("later", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				enterHome();
			}
		});
		// set the cancel listener, when user click cancel button, it will be
		// triggered
		builder.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				enterHome();
			}
		});

		builder.show();

	}

	/**
	 * download the apk file
	 */
	protected void download() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			tvProgress.setVisibility(View.VISIBLE);// show the progress
			String target = Environment.getExternalStorageDirectory()
					+ "/update.apk";
			// XUtils
			HttpUtils utils = new HttpUtils();
			utils.download(mDownloadUrl, target, new RequestCallBack<File>() {
				// the progress of downloading file
				@Override
				public void onLoading(long total, long current,
						boolean isUploading) {
					super.onLoading(total, current, isUploading);
					System.out.println("downloading progress" + current + "/"
							+ total);
					tvProgress.setText("downloading progress" + current * 100
							/ total + "%");
				}

				@Override
				public void onSuccess(ResponseInfo<File> arg0) {
					System.out.println("download succeed");
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.addCategory(Intent.CATEGORY_DEFAULT);
					intent.setDataAndType(Uri.fromFile(arg0.result),
							"application/vnd.android.package-archive");
					// startActivity(intent);
					// if user cancel installation, it will return result
					// and callback onActivityResult()
					startActivityForResult(intent, 0);
				}

				@Override
				public void onFailure(HttpException arg0, String arg1) {
					Toast.makeText(SplashActivity.this, "download fail",
							Toast.LENGTH_LONG).show();
				}
			});
		} else {
			Toast.makeText(SplashActivity.this, "SDcard not found.",
					Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		enterHome();
	}

	/**
	 * enter the home page
	 */
	private void enterHome() {
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
		finish();
	}

	/**
	 * copy database
	 */
	private void copyDB(String dbName) {
		File destFile = new File(getFilesDir(), dbName);
		if (destFile.exists()) {
			System.out.println("database "+dbName+"is already exist");
			return;
		}
		FileOutputStream out = null;
		InputStream in = null;
		try {
			in = getAssets().open(dbName);
			out = new FileOutputStream(destFile);
			int len = 0;
			byte[] buffer = new byte[1024];
			while ((len = in.read(buffer)) != -1) {
				out.write(buffer, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
