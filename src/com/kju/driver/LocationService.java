package com.kju.driver;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;

public class LocationService extends IntentService{
	LocationManager locationManager;
	public LocationService() {
		super("LocationService");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
			while(true){
			try {
				Thread.sleep(5000);
				Log.d("Location","test");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	

}
