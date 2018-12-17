package com.smartfren.cordova.sfgeolocationnative;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class SFGeolocationNative extends CordovaPlugin implements LocationListener {
	private LocationManager mLocManager;
	private Location mLoc;
	private boolean isGPSenabled;
	private boolean isGPSActive;
	private boolean isNetworkEnabled;
	private boolean isNetworkActive;
	private double longitude, latitude, accuracy;
	private String provider;
	private CallbackContext mCallbackCtx;

	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		mCallbackCtx = callbackContext;
		try {
			if (action.equals("getCurrentLocation")) {
				mLocManager = (LocationManager) this.cordova.getActivity().getSystemService(Context.LOCATION_SERVICE);

				latitude = 0.0;
				longitude = 0.0;
				accuracy = 0.0;
//				provider = "";

				mLocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000L, 500.0f, locationListener);

				mLoc = mLocManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

				mLoc = getGPSLocation();
				if (mLoc == null) {
					mLoc = getNetworkLocation();
				}

//				jsonObject.put("provider", provider);

				if (latitude == 0.0) {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("latitude", latitude);
					jsonObject.put("longitude", longitude);
					jsonObject.put("accuracy", accuracy);
					callbackContext.success(jsonObject);
				} else {
					mLocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000L, 500.0f,
							locationListener);

					mLoc = mLocManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

					mLoc = getGPSLocation();
					if (mLoc == null) {
						mLoc = getNetworkLocation();
					}
				}

				return true;
			}
			return false;
		} catch (Exception e) {
			callbackContext.success("---------" + e.getMessage());
			return false;
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		mLoc = location;
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

	private final LocationListener locationListener = new LocationListener() {
		public void onLocationChanged(Location location) {
			try {
			updateWithNewLocation(location);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void onProviderDisabled(String provider) {
			try {
				updateWithNewLocation(null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};

	private void updateWithNewLocation(Location location) throws JSONException {
		if (location != null) {
			latitude = location.getLatitude();
			longitude = location.getLongitude();
			accuracy = location.getAccuracy();
//			provider = location.getProvider();

//			JSONObject jsonObject = new JSONObject();
//			jsonObject.put("latitude", latitude);
//			jsonObject.put("longitude", longitude);
//			jsonObject.put("accuracy", accuracy);
//			jsonObject.put("provider", provider);
//			mCallbackCtx.success(jsonObject);
		}
	}

	private Location getGPSLocation() {
		isGPSenabled = mLocManager.isProviderEnabled("gps");
		Location loc = null;
		if (isGPSenabled) {
			if (isGPSActive) {
				mLocManager.requestLocationUpdates("gps", 1000L, 0.0F, this);
				LocationManager locationmanager = mLocManager;

				if (locationmanager != null) {
					loc = mLocManager.getLastKnownLocation("gps");
				}
			}
		}

		return loc;
	}

	private Location getNetworkLocation() {
		isNetworkEnabled = mLocManager.isProviderEnabled("network");
		Location loc = null;
		if (isNetworkEnabled) {
			if (isNetworkActive) {
				mLocManager.requestLocationUpdates("network", 1000L, 0.0F, this);
				LocationManager locationmanager = mLocManager;

				if (locationmanager != null) {
					loc = mLocManager.getLastKnownLocation("network");
				}
			}
		}
		return loc;
	}
}
