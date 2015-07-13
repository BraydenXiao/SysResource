package org.aurona.lib.resource;

import java.io.File;

import org.aurona.lib.bitmap.BitmapUtil;
import org.aurona.lib.onlineImage.AsyncImageLoader;
import org.aurona.lib.onlineImage.AsyncImageLoader.OnLineImageToFileCallback;




import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class WBImageRes extends WBRes{

	public interface OnResImageLoadListener{
		public void onImageLoadFinish(Bitmap bitmap);
		public void onImageLoadFaile();
		
	}
	
	public interface OnResImageDownLoadListener{
		public void onImageDownLoadFinish(String filename);
		public void onImageDownLoadFaile();
	}
	
	public enum FitType{TITLE,SCALE};

	private FitType fitType;
	protected String imageFileName;
	private int imageID;
	protected LocationType imageType; 
	

	public String getImageFileName() {
		return imageFileName;
	}

	public void setImageFileName(String image) {
		this.imageFileName = image;
	}

	public LocationType getImageType() {
		return imageType;
	}

	public void setImageType(LocationType imageType) {
		this.imageType = imageType;
	}
	
	
	/*
	 * Resource�Ƿ��Ѿ��ڱ���
	 * */
	public boolean isImageResInLocal(Context context){
		if(imageType == LocationType.RES)    return true;
		if(imageType == LocationType.ASSERT) return true;
		if(imageType==null) return true;
		if(imageType == LocationType.CACHE) return true;
		if(imageType == LocationType.ONLINE){
			if(onlineImageResLocalFile(context) != null) return true;
		}
		return false;
	}
	
	public void getImageBitmap(Context context,OnResImageLoadListener listener){
		if(this.imageType == null){
			if(listener != null){
				listener.onImageLoadFaile();
			}
		}
		
		if(imageType == LocationType.RES){
			if(listener != null){
				Bitmap bmp = BitmapUtil.getImageFromAssetsFile(this.getResources(),imageFileName);
				listener.onImageLoadFinish(bmp);
			}
			//return BitmapDbUtil.getImageFromResourceFile(res, imageID);
		}else if(imageType == LocationType.ASSERT){
			
			if(listener != null){
				Bitmap bmp = BitmapUtil.getImageFromAssetsFile(this.getResources(),imageFileName);
				listener.onImageLoadFinish(bmp);
			}
			//return bmp;
		}else if(imageType == LocationType.ONLINE){
			String filename = onlineImageResLocalFile(context);
			Bitmap bmp = BitmapFactory.decodeFile(filename);
			if(listener != null){
				listener.onImageLoadFinish(bmp);
			}			
		}
	}
	
	/*������LocalImageBitmap*/
	public Bitmap getLocalImageBitmap(){
		if(this.imageType == null) return null;
		
		if(imageType == LocationType.RES){
			return BitmapUtil.getImageFromResourceFile(this.getResources(), imageID);
		}else if(imageType == LocationType.ASSERT){
			Bitmap bmp = BitmapUtil.getImageFromAssetsFile(this.getResources(),imageFileName);
			return bmp;
		}
		return null;
	}
	
	/*����*/
	public void downloadImageOnlineRes(Context context,final OnResImageDownLoadListener listener){
		if(context == null) {
			listener.onImageDownLoadFaile();
			return;
		}
		File f = context.getFilesDir();
		String path   = f.getAbsolutePath();
		String m_path = path + "/" + "material";
		File  dir = new File(m_path);
		if (!dir.exists()) {
			dir.mkdir();
		}
		
		String resImagePath = path + "/" + "material" + "/" + this.getName();
		File  res_dir = new File(resImagePath);
		if(!res_dir.exists()){
			res_dir.mkdir();
		
		}
		
		String resImageFileName = path + "/" + "material" + "/" + this.getName() + "/" + this.getName();
		//new AsyncImageLoader().l
		AsyncImageLoader loader = new AsyncImageLoader();
		loader.loadImageToFile(context, this.getImageFileName(), resImageFileName, new OnLineImageToFileCallback(){
			@Override
			public void imageDownload(String filename) {
				if(listener != null){
					listener.onImageDownLoadFinish(filename);
				}
			}

			@Override
			public void imageDownloadFaile(Exception e) {
				listener.onImageDownLoadFaile();
			}
			
		});
	}
	
	public FitType getFitType() {
		return fitType;
	}

	public void setScaleType(FitType fitType) {
		this.fitType = fitType;
	}
	
	private String onlineImageResLocalFile(Context context){
		
		File f = context.getFilesDir();
		String path   = f.getAbsolutePath();
		String m_path = path + "/" + "material";
		File  dir = new File(m_path);
		if (!dir.exists()) {
			dir.mkdir();
		}
		
		String resImagePath = path + "/" + "material" + "/" + this.getName();
		File  res_dir = new File(resImagePath);
		if(res_dir.exists()){
			String resImageFileName = path + "/" + "material" + "/" + this.getName() + "/" + this.getName();
			File resImageFile = new File(resImageFileName);
			if(resImageFile.exists()) return resImageFileName;
		}
		
		return null;
	}
	

	
}
