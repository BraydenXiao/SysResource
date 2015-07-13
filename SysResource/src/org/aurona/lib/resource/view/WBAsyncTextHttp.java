package org.aurona.lib.resource.view;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.os.AsyncTask;
import android.util.Log;

public class WBAsyncTextHttp extends AsyncTask<String, Void, String>{

	
	private AsyncTextHttpTaskListener listener;
	private String url;
	int mConnectionTimeout = 5000;
	int mSocketTimeout = 5000;
	
	public  WBAsyncTextHttp(String url){
		this.url = url;
	}
	
	

	
	
	public void setUrl(String url){
		this.url = url;
	}
	
	public AsyncTextHttpTaskListener getListener() {
		return listener;
	}

	public void setListener(AsyncTextHttpTaskListener listener) {
		this.listener = listener;
	}


	
	@Override
	protected String doInBackground(String... arg0) {
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(url);
			HttpParams httpParams = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, mConnectionTimeout);
			HttpConnectionParams.setSoTimeout(httpParams, mSocketTimeout);

			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String responseString = httpClient.execute(httpGet, responseHandler);
			Log.d("response", responseString);
			return responseString;
			  
		}catch (Exception e) {
//			if(listener != null){
//				this.listener.onRequestDidFailedStatus(e);
//			}
			
		}
		
		return null;
	}
	

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if(listener != null){
			if(result != null){
				this.listener.onRequestDidFinishLoad(result);
			}else{
				this.listener.onRequestDidFailedStatus(null);
			}
		}
	}
	
}
