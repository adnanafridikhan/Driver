package com.kju.driver;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import android.os.AsyncTask;
import android.util.Log;

public class AsyncTaskLogin extends AsyncTask<String, String, String> {
	private String resp;
	@Override
	protected String doInBackground(String... params) {
		int count = params.length;
		if (count == 2) {
			try {
				HttpClient client = new DefaultHttpClient();
				String url = "http://210.211.102.41:8080/FastTaxiWebservice/fast-taxi/users/validate?name="+params[0]+"&password="+params[1];
				HttpGet get = new HttpGet(url);
				HttpResponse rep = client.execute(get);
				resp = EntityUtils.toString(rep.getEntity());
				Log.d("LOGIN", resp);

			} catch (Exception e) {
				e.printStackTrace();
				resp = e.getMessage();
			}
		} else {
			resp = "Invalid number of arguments-" + count;
		}
		return resp;
	}

}
