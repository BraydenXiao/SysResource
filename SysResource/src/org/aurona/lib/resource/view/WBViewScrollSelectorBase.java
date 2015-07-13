package org.aurona.lib.resource.view;


import org.aurona.lib.database.SQLiteHelper;
import org.aurona.lib.resource.WBImageRes;
import org.aurona.lib.resource.WBRes;
import org.aurona.lib.resource.WBImageRes.OnResImageDownLoadListener;
import org.aurona.lib.resource.WBRes.LocationType;
import org.aurona.lib.resource.manager.WBManager;
import org.aurona.lib.resource.widget.WBHorizontalListView;
import org.aurona.lib.resource.widget.WBScrollBarArrayAdapter;



import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.AdapterView.OnItemClickListener;

public class WBViewScrollSelectorBase extends FrameLayout implements OnItemClickListener{

	
	protected WBHorizontalListView scrollView;
	protected WBScrollBarArrayAdapter scrollDataAdapter;
	protected   WBManager mManager;
	protected   WBOnResourceChangedListener mResListener;
	protected   WBMaterialUrlInterface mMaterialUrlBase;
	
	public WBViewScrollSelectorBase(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/*
	 * 
	 * */
	public void setDataAdapter(WBManager manager){
		mManager = manager;
		int count = mManager.getCount();
		WBRes[] resArray = new WBRes[count];
		for(int i =0 ; i < count; i++){
			 resArray[i] = mManager.getRes(i);
		}
		scrollDataAdapter = new WBScrollBarArrayAdapter(this.getContext(),resArray);
		scrollView.setAdapter(scrollDataAdapter);
		scrollView.setOnItemClickListener(this);
	}
	
	
	public void setWBOnResourceChangedListener(WBOnResourceChangedListener listener){
		mResListener = listener;
	}
	
	public void setWBMaterialUrlInterface(WBMaterialUrlInterface materialBase){
		mMaterialUrlBase = materialBase;
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
		final WBRes res = this.mManager.getRes(pos);
		
		//final int myPos = pos;		
		String url = null;
		if(mMaterialUrlBase != null){
			url = mMaterialUrlBase.getUrlBase() + "&name=" + res.getName();
		}
		
		if(res instanceof WBImageRes){
			WBImageRes imageRes = (WBImageRes)res;
			onImageResProcess(url,imageRes,pos);
		}else{
			mResListener.resourceChanged(res, "..",this.mManager.getCount(),pos);
		}
		
		//�������Item
		//scrollDataAdapter.addResRecordItem(res,pos);
	
	}
	
	
	protected void onImageResProcess(String url,final WBImageRes imgRes,final int pos){
		
		if(imgRes.getImageType() == LocationType.ONLINE){		
			boolean isResLocal = imgRes.isImageResInLocal(this.getContext());
			
			if(isResLocal){
				mResListener.resourceChanged(imgRes, "..",this.mManager.getCount(),pos);		
			}else{
				scrollDataAdapter.setViewInDownloading(pos);
				final int position = pos;
				WBAsyncTextHttpExecute.asyncHttpRequest(url, new AsyncTextHttpTaskListener(){
					@Override
					public void onRequestDidFinishLoad(String result) {
						String img_url = resultToUrl(result);
						imgRes.setImageFileName(img_url);
				
						imgRes.downloadImageOnlineRes(getContext(),new OnResImageDownLoadListener(){
							@Override
							public void onImageDownLoadFinish(String filename) {
								mResListener.resourceChanged(imgRes, "..",mManager.getCount(),position);		
								scrollDataAdapter.setViewInDownloadOk(pos);
							}
							
							@Override
							public void onImageDownLoadFaile() {
								scrollDataAdapter.setViewInDownloadOk(pos);
								
							}						
						});						
					}
					
					@Override
					public void onRequestDidFailedStatus(Exception e) {
						scrollDataAdapter.setViewInDownloadFail(pos);
					}
				});				
			}


		}else{
			mResListener.resourceChanged(imgRes, "..",this.mManager.getCount(),pos);
		}
	}
	
	
	protected  String resultToUrl(String result){
		return result;
		
	}

}
