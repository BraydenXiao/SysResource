package org.aurona.lib.resource.view;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.os.Handler;

public class WBAsyncTextHttp23 {
	private AsyncTextHttpTaskListener listener;
	private String url;
	int mConnectionTimeout = 5000;
	int mSocketTimeout = 5000;
	private final Handler handler = new Handler();
	
	public  WBAsyncTextHttp23(String url){
		this.url = url;
	}
	
	
	public static void asyncHttpRequest(String url,AsyncTextHttpTaskListener listener){
		WBAsyncTextHttp loader = new WBAsyncTextHttp(url);
		loader.setListener(listener);
		loader.execute();
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

	public void execute(){
		new Thread(new Runnable() {  
            @Override  
            public void run() { 
            	String responseString = null;
            	try {
        			DefaultHttpClient httpClient = new DefaultHttpClient();
        			HttpGet httpGet = new HttpGet(url);
        			HttpParams httpParams = httpClient.getParams();
        			HttpConnectionParams.setConnectionTimeout(httpParams, mConnectionTimeout);
        			HttpConnectionParams.setSoTimeout(httpParams, mSocketTimeout);

        			ResponseHandler<String> responseHandler = new BasicResponseHandler();
        			responseString = httpClient.execute(httpGet, responseHandler);
        			  
        		}catch (Exception e) {

        		}
            	final String result = responseString;
            	
                handler.post(new Runnable() {  
                	@Override  
                    public void run() {  
                		if(listener != null){
                			if(result != null){
                				listener.onRequestDidFinishLoad(result);
                			}else{
                				listener.onRequestDidFailedStatus(null);
                			}
                		}
                    }  
                });  
            }  
        }).start();  
	}
	

	
}
