package org.aurona.lib.border.view;



import org.aurona.lib.bitmap.BitmapUtil;
import org.aurona.lib.border.process.WBBorderProcess;
import org.aurona.lib.border.res.WBBorderRes;



import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;


public class WBBorderViewProcess  extends View{

	public     int width;
	public     int height;
	private  Context mContext;
	
	private Bitmap bitmap = null;
	private  WBBorderRes res;
	private boolean isResChange = false;
	Paint p = new Paint();
	Matrix m = new Matrix();
	Rect dstRect;// = new Rect(0,0,width ,height);
	public WBBorderViewProcess(Context context) {
		super(context);
		this.mContext=context;
	}
	
	public WBBorderViewProcess(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext=context;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		this.width   = this.getWidth();
		this.height  = this.getHeight();
		if(bitmap!=null && !bitmap.isRecycled()){
			if(dstRect == null){
				dstRect = new Rect(0,0,width ,height);
			}
			dstRect.left  = 0;
			dstRect.right = width;
			dstRect.top   = 0;
			dstRect.bottom = height;
			
			canvas.drawBitmap(bitmap, null,  dstRect, null); 
		}		
	}

	public void changeRes(WBBorderRes borderRes,boolean isLowMemDevice){
		res = borderRes;
		if(bitmap != null && !bitmap.isRecycled()){
			bitmap.recycle();
		}
		bitmap = null;
		if(res == null){
			this.invalidate();
			return;
		} 
		
		if(borderRes.getBorderType() != null && borderRes.getBorderType() == WBBorderRes.BorderType.IMAGE){
			Bitmap bmp = borderRes.getLocalImageBitmap();
			
			bitmap = BitmapUtil.sampeZoomFromBitmap(bmp, width);
			if(bmp != bitmap && !bmp.isRecycled()){
				bmp.recycle();
			}
			bmp = null;
		}else{
			bitmap = WBBorderProcess.processNinePathBorderOuter(mContext, width,height, borderRes,null);
		}
		isResChange = true;
		this.invalidate();
	}
	
	public WBBorderRes getCurrentRes(){
		return res;
	}
	
	public Bitmap getBitmap(){
		return this.bitmap;
	}
	
	
	public void dispose(){
		if(bitmap != null && !bitmap.isRecycled()){
			bitmap.recycle();
		}
		bitmap = null;
		res = null;
	}


}
