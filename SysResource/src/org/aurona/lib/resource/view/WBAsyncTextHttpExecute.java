package org.aurona.lib.resource.view;

import android.os.Build;

public class WBAsyncTextHttpExecute {
	public static void asyncHttpRequest(String url,AsyncTextHttpTaskListener listener){
		if (Build.VERSION.SDK_INT  <= 10){
			WBAsyncTextHttp23 loader = new WBAsyncTextHttp23(url);
			loader.setListener(listener);
			loader.execute();
		}else{
			WBAsyncTextHttp loader = new WBAsyncTextHttp(url);
			loader.setListener(listener);
			loader.execute();
		}
	}
	
}
