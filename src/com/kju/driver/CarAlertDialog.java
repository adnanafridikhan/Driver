package com.kju.driver;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;

public class CarAlertDialog extends Activity  {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		
		String message = intent.getStringExtra("message");
		
		AlertDialog ad = new AlertDialog.Builder(this)
		.setMessage(message)
		.setIcon(R.drawable.ic_launcher).setTitle("Có khách hàng")
		.setPositiveButton("OK", new OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				finish();
			}
		})
		.setNeutralButton("Cancel", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();			
			}
		}).setCancelable(false).create();
ad.show();
	}
}