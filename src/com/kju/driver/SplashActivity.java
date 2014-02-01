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
		startService(new Intent(this, LocationService.class));
		Thread timer = new Thread(){
			public void run(){
				try{
					sleep(4000);
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					SessionManager session = new SessionManager(getApplicationContext());
					///session.logoutUser(); 
					if(!session.isLoggedIn()){
						Intent login = new Intent("com.kju.driver.LOGIN");
						startActivity(login);
					}else{
						Intent startingPoint = new Intent("com.kju.driver.STARTINGPOINT");
						startActivity(startingPoint);
					}
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
