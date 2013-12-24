package com.kju.driver;


import static com.kju.driver.CommonUtilities.SENDER_ID;

import com.google.android.gcm.GCMRegistrar;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;

import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

/**
 * @author monkey
 * 
 */
public class StartingPoint extends Activity {


	static final String TAG = "GCMDemo";
	String regid;
	Context context;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
		
		
		checkNotNull(SENDER_ID, "SENDER_ID");

		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);

		 String regId = GCMRegistrar.getRegistrationId(this);

		if (regId.equals("")) {
			GCMRegistrar.register(this, SENDER_ID);
			regId = GCMRegistrar.getRegistrationId(this);
			Log.d(TAG, "Already registered: "+ regId);
			//send regid to server
		} else {
			regId = GCMRegistrar.getRegistrationId(this);
			Log.d(TAG, "Already registered: "+ regId);
		}

	}
	
	private void checkNotNull(Object reference, String name) {
		if (reference == null) {
			throw new NullPointerException(getString(R.string.error_config,
					name));
		}
	}

}
