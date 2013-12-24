package com.kju.driver;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kju.driver.R;

import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class HorizontalFragment extends Fragment {
	static final LatLng Center = new LatLng(21.021087, 105.829625);
	private GoogleMap googleMap;	
	private Button accept;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_horizontal, container,false);
		accept = (Button) view.findViewById(R.id.accept);
		
		try {
			if (googleMap == null) {
				googleMap = ((MapFragment) getFragmentManager()
						.findFragmentById(R.id.map)).getMap();
			}
			googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(
					Center, 14);
			googleMap.animateCamera(yourLocation);
			//googleMap.setMyLocationEnabled(true);
			Marker TP = googleMap.addMarker(new MarkerOptions().
					 position(Center).title("Current"));

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		accept.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.d("onclick", "send to server");
			}
		});
		
		return view;
	}

}
