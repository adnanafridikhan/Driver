package com.kju.driver;

import com.kju.driver.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		Thread timer = new Thread(){
			public void run(){
				try{
					sleep(6000);
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					Intent openStartingPoint = new Intent("com.kju.driver.STARTINGPOINT");
					startActivity(openStartingPoint);
				}
			}
		};
		timer.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
