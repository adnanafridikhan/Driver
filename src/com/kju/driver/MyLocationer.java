package com.kju.driver;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

public class MyLocationer implements LocationListener{

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		double lat = location.getLatitude();
		double lng = location.getLongitude();		
		Log.d("Location",lat+" , "+lng);
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

}
