package com.smartfren.cordova.sfgeolocationnative;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

public class SFGeolocationNative extends CordovaPlugin {
	private int MY_PERMISSIONS_REQUEST = 0;

	// flag for GPS status
	boolean isGPSEnabled = false;

	// flag for network status
	boolean isNetworkEnabled = false;
	LocationListener locationListener;
	private boolean listenerON = false;
	private PluginResult result;
	private JSONObject objPosition = new JSONObject();
	private LocationManager mLocManager;

	@Override
	public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
		if (action.equals("getCurrentLocation")) {
			mLocManager = (LocationManager) this.cordova.getActivity()
					.getSystemService(Context.LOCATION_SERVICE);
			
			if (!listenerON) {

				// Define a listener that responds to location updates
				locationListener = new LocationListener() {
					public void onLocationChanged(Location location) {

						// Called when a new location is found by the network location provider.
						updateLocation(location, callbackContext);
						

					}

					public void onStatusChanged(String provider, int status, Bundle extras) {

					}

					public void onProviderEnabled(String provider) {

					}

					public void onProviderDisabled(String provider) {

					}
				};

				// Here, thisActivity is the current activity
				if (ContextCompat.checkSelfPermission(this.cordova.getActivity(),
						android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

					// Should we show an explanation?
					if (ActivityCompat.shouldShowRequestPermissionRationale(this.cordova.getActivity(),
							android.Manifest.permission.ACCESS_FINE_LOCATION)) {

						// Show an explanation to the user *asynchronously* -- don't block
						// this thread waiting for the user's response! After the user
						// sees the explanation, try again to request the permission.

					} else {

						// No explanation needed, we can request the permission.

						ActivityCompat.requestPermissions(this.cordova.getActivity(),
								new String[] { android.Manifest.permission.ACCESS_FINE_LOCATION }, MY_PERMISSIONS_REQUEST);

						// MY_PERMISSIONS_REQUEST is an
						// app-defined int constant. The callback method gets the
						// result of the request.
					}
				}

				listenerON = true;
			}
			
			// getting GPS status
			isGPSEnabled = mLocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

			// getting network status
			isNetworkEnabled = mLocManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
			
			if (!isGPSEnabled && !isNetworkEnabled) {
				// no network provider is enabled
			} else {
				Location location = getGPSLocation();
	            if (location == null) {
	                location = getNetworkLocation();
	            }
	            updateLocation(location, callbackContext);
			}
			
			return true;
		}

		return false;
	}

	private String formatDate(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String format = formatter.format(date);

		return format;
	}
	
	private Location getGPSLocation() {
		Location loc = null;
		mLocManager.requestLocationUpdates("gps", 1000L, 0.0F, locationListener);
		LocationManager locationmanager = mLocManager;

		if (locationmanager != null) {
			loc = mLocManager.getLastKnownLocation("gps");
		}
		return loc;
	}

	private Location getNetworkLocation() {
		Location loc = null;
		mLocManager.requestLocationUpdates("network", 1000L, 0.0F, locationListener);
		LocationManager locationmanager = mLocManager;

		if (locationmanager != null) {
			loc = mLocManager.getLastKnownLocation("network");
		}
		return loc;
	}
	
	private void updateLocation(Location location, final CallbackContext callbackContext) {
		Date datePosition = new Date(location.getTime());

		String datetime = formatDate(datePosition);

		Log.e("DATA-Position", "Lat:" + location.getLatitude() + " - Long:" + location.getLongitude()
				+ " - Data e hora:" + datetime);

		try {
			objPosition.put("latitude", location.getLatitude());
			objPosition.put("longitude", location.getLongitude());
			objPosition.put("accuracy", location.getAccuracy());
			objPosition.put("time", location.getTime());
			objPosition.put("location_provider", location.getProvider());
			objPosition.put("formatTime", datetime);
			objPosition.put("extra", null);

			result = new PluginResult(PluginResult.Status.OK, objPosition);
            result.setKeepCallback(true);
            callbackContext.sendPluginResult(result);
			Log.e("GPS-LOCATION-ARRAY", objPosition.toString());

			

		} catch (JSONException e) {
			PluginResult result = new PluginResult(PluginResult.Status.JSON_EXCEPTION);
            result.setKeepCallback(true);
            callbackContext.sendPluginResult(result);
			e.printStackTrace();
			callbackContext.error(e.toString());
		}
	}
}