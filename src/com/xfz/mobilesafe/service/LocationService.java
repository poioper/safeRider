package com.xfz.mobilesafe.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

/**
 * 
 * @author xfz:xfz1990@gmail.com
 * @version create time：2016-5-11
 */
public class LocationService extends Service {
	private LocationManager lm;
	private MyLocationListener listener;
	private SharedPreferences mPref;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		mPref = getSharedPreferences("config", MODE_PRIVATE);

		lm = (LocationManager) getSystemService(LOCATION_SERVICE);
		// List<String> allProviders = lm.getAllProviders();

		Criteria criteria = new Criteria();
		criteria.setCostAllowed(true);// like 3G 4G
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		String bestProvider = lm.getBestProvider(criteria, true);

		listener = new MyLocationListener();
		lm.requestLocationUpdates(bestProvider, 0, 0, listener);
	}

	class MyLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			String longitude = "" + location.getLongitude();
			String latitude = "" + location.getLatitude();
			mPref.edit()
					.putString(
							"location",
							"j:" + location.getLongitude() + "; w:"
									+ location.getLatitude()).commit();
			stopSelf();
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {

		}

		@Override
		public void onProviderEnabled(String provider) {

		}

		@Override
		public void onProviderDisabled(String provider) {

		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		lm.removeUpdates(listener);
	}
}
