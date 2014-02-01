package com.kju.driver;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {

	Button btnLogin;
	EditText txtUsername, txtPassword;
	SessionManager session;
	AsyncTask<String, String, String> asyncTask;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		// Session Manager
		session = new SessionManager(getApplicationContext());

		// Email, Password input text
		txtUsername = (EditText) findViewById(R.id.username);
		txtPassword = (EditText) findViewById(R.id.password);

		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// Get username, password from EditText
				String username = txtUsername.getText().toString();
				String password = txtPassword.getText().toString();

				//loginInBackground();

			     
				// Check if username, password is filled
				if (username.trim().length() > 0
						&& password.trim().length() > 0) {
					AsyncTaskLogin login = new AsyncTaskLogin();
					asyncTask=login.execute(username,password);
					
				    String asyncResultText;
					try {
						asyncResultText = asyncTask.get();						
						String json = asyncResultText.trim();
						JSONObject jsonObj = new JSONObject(json);
						String success = jsonObj.getString("success");
						if (success.equals("true")) {
							session.createLoginSession("XeNhanh",username);
							Intent i = new Intent("com.kju.driver.STARTINGPOINT");
							startActivity(i);
							finish();

						} else {
							//Login failed
							session.logoutUser();
						}		
						
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					// Login failed
				}

			}
		});
	}

//	private void loginInBackground() {
//		new AsyncTask<Void, Void, String>() {
//
//			@Override
//			protected String doInBackground(Void... params) {
//				String responseBody = "";
//				try {
//
//					HttpClient client = new DefaultHttpClient();
//					String url = "210.211.102.41:8080/FastTaxiWebservice/fast-taxi/users/validate?name=vu&password=abc";
//					HttpPost post = new HttpPost(url);
////					List<NameValuePair> pairs = new ArrayList<NameValuePair>();
////					pairs.add(new BasicNameValuePair("name", "vu"));
////					pairs.add(new BasicNameValuePair("password", "abc"));
//
//					// post.setEntity(new UrlEncodedFormEntity(pairs));
//					HttpResponse rep = client.execute(post);
//					responseBody = EntityUtils.toString(rep.getEntity());
//					Log.d("TAG-1", responseBody);
//				} catch (UnsupportedEncodingException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				} catch (ParseException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				return responseBody;
//			}
//		}.execute(null, null, null);
//
//	}

}
