package com.kju.driver;

import static com.kju.driver.CommonUtilities.SENDER_ID;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;

/**
 * @author monkey
 * 
 */
public class StartingPoint extends Activity {

	public static final String EXTRA_MESSAGE = "message";
	public static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";
	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

	/**
	 * Substitute you own sender ID here. This is the project number you got
	 * from the API Console, as described in "Getting Started."
	 */
	String SENDER_ID = "279534329436";

	/**
	 * Tag used on log messages.
	 */
	static final String TAG = "GCM Demo";

	// TextView mDisplay;
	GoogleCloudMessaging gcm;
	AtomicInteger msgId = new AtomicInteger();
	Context context;
	SessionManager session;
	String regid;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    
		//startService(new Intent(this, LocationService.class)); 
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		WindowManager wm = getWindowManager();
		Display display = wm.getDefaultDisplay();
		Point screenSize = new Point();
		display.getSize(screenSize);
		if (screenSize.x > screenSize.y) {
			// horizontal
			HorizontalFragment hori_fg = new HorizontalFragment();
			fragmentTransaction.replace(android.R.id.content, hori_fg);
		} else {
			// vertical
			VerticalFragment vert_fg = new VerticalFragment();
			fragmentTransaction.replace(android.R.id.content, vert_fg);
		}
		fragmentTransaction.commit();

		context = getApplicationContext();
		 
		// session.logoutUser();
		// Check device for Play Services APK. If check succeeds, proceed with
		// GCM registration.
		if (checkPlayServices()) {
			gcm = GoogleCloudMessaging.getInstance(this);
			
			
			regid = getRegistrationId(context);
			
			//unregisterInBackground();
			Log.i(TAG,regid);
			if (regid.isEmpty()) {
				registerInBackground();
			}
		} else {
			Log.i(TAG, "No valid Google Play Services APK found.");
			
		}

		// Code for GCM here

	}

	@Override
	protected void onResume() {
		super.onResume();
		// Check device for Play Services APK.
		checkPlayServices();
	}

	 @Override
	    public boolean onCreateOptionsMenu(Menu menu)
	    {
	        MenuInflater menuInflater = getMenuInflater();
	        menuInflater.inflate(R.menu.main, menu);
	        return true;
	    }
	
	
	    @Override
	    public boolean onOptionsItemSelected(MenuItem item)
	    {
	         
	        switch (item.getItemId())
	        {
	        case R.id.menu_login:
	        	session = new SessionManager(getApplicationContext());
	        	session.logoutUser();
	            return true;
	 
	        default:
	            return super.onOptionsItemSelected(item);
	        }
	    }    
	 
	 
	/**
	 * Check the device to make sure it has the Google Play Services APK. If it
	 * doesn't, display a dialog that allows users to download the APK from the
	 * Google Play Store or enable it in the device's system settings.
	 */
	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, this,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Log.i(TAG, "This device is not supported.");
				finish();
			}
			return false;
		}
		return true;
	}

	/**
	 * Stores the registration ID and the app versionCode in the application's
	 * {@code SharedPreferences}.
	 * 
	 * @param context
	 *            application's context.
	 * @param regId
	 *            registration ID
	 */
	private void storeRegistrationId(Context context, String regId) {
		final SharedPreferences prefs = getGcmPreferences(context);
		int appVersion = getAppVersion(context);
		Log.i(TAG, "Saving regId on app version " + appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(PROPERTY_REG_ID, regId);
		editor.putInt(PROPERTY_APP_VERSION, appVersion);
		Log.d("ID: ", regId);
		editor.commit();
	}

	/**
	 * Gets the current registration ID for application on GCM service, if there
	 * is one.
	 * <p>
	 * If result is empty, the app needs to register.
	 * 
	 * @return registration ID, or empty string if there is no existing
	 *         registration ID.
	 */
	private String getRegistrationId(Context context) {
		final SharedPreferences prefs = getGcmPreferences(context);
		String registrationId = prefs.getString(PROPERTY_REG_ID, "");
		if (registrationId.isEmpty()) {
			Log.i(TAG, "Registration not found.");
			return "";
		}
		// Check if app was updated; if so, it must clear the registration ID
		// since the existing regID is not guaranteed to work with the new
		// app version.
		int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION,
				Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			Log.i(TAG, "App version changed.");
			return "";
		}
		return registrationId;
	}

	private void unregisterInBackground() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(context);
					}
					gcm.unregister();
		
				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
				}
				return msg;
			}
		}.execute(null, null, null);
	}
	
	private void registerInBackground() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(context);
					}
					regid = gcm.register(SENDER_ID);
					msg = "Device registered, registration ID=" + regid;
					Log.d("ABC", regid);
					// You should send the registration ID to your server over
					// HTTP, so it
					// can use GCM/HTTP or CCS to send messages to your app.
					sendRegistrationIdToBackend(regid);

					// For this demo: we don't need to send it because the
					// device will send
					// upstream messages to a server that echo back the message
					// using the
					// 'from' address in the message.

					// Persist the regID - no need to register again.
					storeRegistrationId(context, regid);
				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
					// If there is an error, don't just keep trying to register.
					// Require the user to click a button again, or perform
					// exponential back-off.
				}
				return msg;
			}
		}.execute(null, null, null);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/**
	 * @return Application's version code from the {@code PackageManager}.
	 */
	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	/**
	 * @return Application's {@code SharedPreferences}.
	 */
	private SharedPreferences getGcmPreferences(Context context) {
		// This sample app persists the registration ID in shared preferences,
		// but
		// how you store the regID in your app is up to you.
		return getSharedPreferences(StartingPoint.class.getSimpleName(),
				Context.MODE_PRIVATE);
	}

	/**
	 * Sends the registration ID to your server over HTTP, so it can use
	 * GCM/HTTP or CCS to send messages to your app. Not needed for this demo
	 * since the device sends upstream messages to a server that echoes back the
	 * message using the 'from' address in the message.
	 */
	private void sendRegistrationIdToBackend(String regid) {
		// send id  + username to Server
	}

}
