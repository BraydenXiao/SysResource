package org.aurona.lib.border.process;



import org.aurona.lib.border.res.WBBorderRes;
import org.aurona.lib.border.res.WBBorderReturns;

import org.aurona.lib.sysresource.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;


public class WBBorderProcess {
		
	static int mapSize = 2048;


	protected static  float getBorderRate(int width,int height ,WBBorderRes borderRes){
		
		float rateW = (float)(mapSize - borderRes.getInnerPx() - borderRes.getInnerPx2()) / width;
		float rateH = (float)(mapSize - borderRes.getInnerPy() - borderRes.getInnerPy2()) / height;
		return rateW < rateH ? rateW : rateH;		
	}
	
	protected static  Bitmap processBorder(Context context, int width,int height ,WBBorderRes borderRes,int dec, boolean isLowMemoryDevice){
		//	int width  = srcBmp.getWidth();
		//	int height = srcBmp.getHeight();
			
			
			
			int max = mapSize / 2 ;
			if(isLowMemoryDevice){
				max = mapSize / 2 /dec;
			}
		
			dec *= 2;
			
			float borderRate = getBorderRate(width,height,borderRes);
			if(width > height){
				width = max;
				height = (int)((float)(borderRate * height + borderRes.getInnerPy() + borderRes.getInnerPy2()) / dec);

			}else{
				height = max;
				width = (int)((float)(borderRate * width + borderRes.getInnerPx() + borderRes.getInnerPx2()) / dec);
			}
			
			float rate = max / (float)mapSize;
			if(borderRes.getMapSize() > 0){
				rate = max / (float)borderRes.getMapSize();
			}
			
			int destWidth  = width;
			int destHeight = height;

			Bitmap newBitmap = null;  
			//newBitmap = Bitmap.createBitmap(destWidth,destHeight,Config.ARGB_8888);  

			Bitmap dst  =  BitmapFactory.decodeResource(context.getResources(), R.drawable.res_tranparent);
			newBitmap = Bitmap.createScaledBitmap(dst, destWidth, destHeight, false);
			dst.recycle();

			Canvas canvas = new Canvas(newBitmap); 
			
			Paint paint = new Paint();  
		
			try{
				//LeftCornor   左上角
				Bitmap leftTopCornorBitmap = borderRes.getLeftTopCornorBitmap();
				int left_top_cornor_width  =  calcWithRate(leftTopCornorBitmap.getWidth(),rate);
				int left_top_cornor_height =  calcWithRate(leftTopCornorBitmap.getHeight(),rate);

				
				Rect  dstRect = new Rect(0,0,left_top_cornor_width,left_top_cornor_height);
				canvas.drawBitmap(leftTopCornorBitmap, null,  dstRect, paint);      
				leftTopCornorBitmap.recycle();  
				leftTopCornorBitmap = null;	
				
				//Left Bottom Cornor
				Bitmap leftBottmoCornorBitmap = borderRes.getLeftBottomCornorBitmap();
			
				int left_bottom_cornor_width  = calcWithRate(leftBottmoCornorBitmap.getWidth(),rate);
				int left_bottom_cornor_height = calcWithRate(leftBottmoCornorBitmap.getHeight(),rate);
				int lbc_startX = 0;  int lbc_startY = destHeight - left_bottom_cornor_height;
				dstRect = new Rect(lbc_startX ,lbc_startY,lbc_startX + left_bottom_cornor_width,lbc_startY + left_bottom_cornor_height);

				canvas.drawBitmap(leftBottmoCornorBitmap, null,  dstRect, null);      
				leftBottmoCornorBitmap.recycle(); 
				leftBottmoCornorBitmap = null;

				
				
				//Right Top Cornor
				Bitmap rightTopCornorBitmap  = borderRes.getRightTopCornorBitmap();
				
				int right_top_cornor_width   =  calcWithRate(rightTopCornorBitmap.getWidth(),rate);
				int right_top_cornor_height  =  calcWithRate(rightTopCornorBitmap.getHeight(),rate);
				int rightTopCornorStarX = destWidth - right_top_cornor_width;
				int rightTopCornorStarY  = 0;
				dstRect = new Rect(rightTopCornorStarX ,rightTopCornorStarY,rightTopCornorStarX + right_top_cornor_width,rightTopCornorStarY + right_top_cornor_height);
				
				canvas.drawBitmap(rightTopCornorBitmap, null,  dstRect, null);      
				rightTopCornorBitmap.recycle(); rightTopCornorBitmap = null;

				

				//Right Bottom Cornor
				Bitmap rightBottomCornorBitmap = borderRes.getRightBottomCornorBitmap();		
				int right_bottom_cornor_width  =  calcWithRate(rightBottomCornorBitmap.getWidth(),rate);
				int right_bottom_cornor_height =  calcWithRate(rightBottomCornorBitmap.getHeight(),rate);
				
				
				int rightBottomCornorStarX  = destWidth  - right_bottom_cornor_width;
				int rightBottomCornorStarY  = destHeight - right_bottom_cornor_height;
				dstRect = new Rect(rightBottomCornorStarX ,rightBottomCornorStarY,rightBottomCornorStarX + right_bottom_cornor_width,rightBottomCornorStarY + right_bottom_cornor_height);
				canvas.drawBitmap(rightBottomCornorBitmap, null,  dstRect, null);      
				rightBottomCornorBitmap.recycle(); rightBottomCornorBitmap = null;

				//Left Line
				Bitmap leftBmp = borderRes.getLeftBitmap();  //Left Bitmap
				
				int leftWidth  = calcWithRate(leftBmp.getWidth(),rate);
				int leftHeight = calcWithRate(leftBmp.getHeight(),rate);
				
				int leftScaleHeight = destHeight - left_top_cornor_height - left_bottom_cornor_height;		
				int leftStartX  = 0;
				int leftStartY  = left_top_cornor_height;
				if (leftScaleHeight > 0) {
					//Tile Mode
					Bitmap line_newBitmap = TileBitmapVertical(leftBmp,leftScaleHeight,leftWidth,leftHeight);
					if(line_newBitmap != leftBmp){
						leftBmp.recycle();   
						leftBmp = null;
					}
					dstRect = new Rect(leftStartX ,leftStartY,leftStartX + leftWidth,leftStartY + leftScaleHeight);
					canvas.drawBitmap(line_newBitmap, null,  dstRect, null);    
					line_newBitmap.recycle();  
					line_newBitmap = null;
				}
			

				//Top Line
				Bitmap topBmp = borderRes.getTopBitmap();
				int topHeight  = calcWithRate(topBmp.getHeight(),rate);
				int topWidth   = calcWithRate(topBmp.getWidth(),rate);
				
				int topScaleWidth   = destWidth - left_top_cornor_width - right_top_cornor_width;
				int topStartX  = left_top_cornor_width;
				int topStartY  = 0;
				if (topScaleWidth > 0) {
					Bitmap line_newBitmap = TileBitmapHorizon(topBmp,topScaleWidth,topWidth,topHeight);
					if(line_newBitmap != topBmp){
						topBmp.recycle();   
						topBmp = null;
					}
					dstRect = new Rect(topStartX ,topStartY,topStartX + topScaleWidth,topStartY + topHeight);
					canvas.drawBitmap(line_newBitmap, null,  dstRect, null);    
					line_newBitmap.recycle();  
					line_newBitmap = null;
				}
				
				
				Bitmap rightBmp = borderRes.getRightBitmap();		
				int rightWidth   =  calcWithRate(rightBmp.getWidth(),rate);
				int rightHeight  =  calcWithRate(rightBmp.getHeight(),rate);
				
			
				int rightScaleHeight = destHeight - right_top_cornor_height - right_bottom_cornor_height;
				int rightStartX  = destWidth - rightWidth;
				int rightStartY  = right_top_cornor_height;
				if (rightScaleHeight > 0) {
					Bitmap line_newBitmap = TileBitmapVertical(rightBmp,rightScaleHeight,rightWidth,rightHeight);
					if(line_newBitmap != rightBmp){
						rightBmp.recycle();   rightBmp = null;
					}
					dstRect = new Rect(rightStartX ,rightStartY,rightStartX + rightWidth,rightStartY + rightScaleHeight);
					canvas.drawBitmap(line_newBitmap, null,  dstRect, null);    
					line_newBitmap.recycle();  line_newBitmap = null;
				}
				
				Bitmap bottomBmp = borderRes.getBottomBitmap();
			    
			    int bottomWidth   =  calcWithRate(bottomBmp.getWidth(),rate);
				int bottomHeight  =  calcWithRate(bottomBmp.getHeight(),rate);
				
				int bottomScaleWidth = destWidth - left_bottom_cornor_width - right_bottom_cornor_width;
				int bottomStartX  = left_bottom_cornor_width;
				int bottomStartY  = destHeight - bottomHeight;
				if (bottomScaleWidth > 0 ) {
					Bitmap line_newBitmap = TileBitmapHorizon(bottomBmp,bottomScaleWidth,bottomWidth,bottomHeight);
					if(line_newBitmap != bottomBmp){
						bottomBmp.recycle();   bottomBmp = null;
					}
					dstRect = new Rect(bottomStartX ,bottomStartY,bottomStartX + bottomScaleWidth,bottomStartY + bottomHeight);
					canvas.drawBitmap(line_newBitmap, null,  dstRect, null);    
					line_newBitmap.recycle();  line_newBitmap = null;
				}
				
				
			}catch( OutOfMemoryError ex){
				if(newBitmap != null && !newBitmap.isRecycled()){
					newBitmap.recycle();
				}
				throw ex;
			}
					
			return newBitmap;
		}
	protected static  void processBorder(Context context, int width,int height ,WBBorderRes borderRes,int dec,Canvas canvas){
		float rate = getBorderRate(width,height,borderRes);
		rate=1/rate;
		Paint paint = new Paint();  
	    paint.setAntiAlias(true);
		try{
			//LeftCornor   左上角
			Bitmap leftTopCornorBitmap = borderRes.getLeftTopCornorBitmap();
			int left_top_cornor_width  =  calcWithRate(leftTopCornorBitmap.getWidth(),rate);
			int left_top_cornor_height =  calcWithRate(leftTopCornorBitmap.getHeight(),rate);

			
			Rect  dstRect = new Rect(0,0,left_top_cornor_width,left_top_cornor_height);
			canvas.drawBitmap(leftTopCornorBitmap, null,  dstRect, paint);  
			leftTopCornorBitmap.recycle();  
			leftTopCornorBitmap = null;	
			
			//Left Bottom Cornor
			Bitmap leftBottmoCornorBitmap = borderRes.getLeftBottomCornorBitmap();
		
			int left_bottom_cornor_width  = calcWithRate(leftBottmoCornorBitmap.getWidth(),rate);
			int left_bottom_cornor_height = calcWithRate(leftBottmoCornorBitmap.getHeight(),rate);
			int lbc_startX = 0;  int lbc_startY = height - left_bottom_cornor_height;
			dstRect = new Rect(lbc_startX ,lbc_startY,lbc_startX + left_bottom_cornor_width,lbc_startY + left_bottom_cornor_height);

			canvas.drawBitmap(leftBottmoCornorBitmap, null,  dstRect, null);      
			leftBottmoCornorBitmap.recycle(); 
			leftBottmoCornorBitmap = null;

			
			
			//Right Top Cornor
			Bitmap rightTopCornorBitmap  = borderRes.getRightTopCornorBitmap();
			
			int right_top_cornor_width   =  calcWithRate(rightTopCornorBitmap.getWidth(),rate);
			int right_top_cornor_height  =  calcWithRate(rightTopCornorBitmap.getHeight(),rate);
			int rightTopCornorStarX = width - right_top_cornor_width;
			int rightTopCornorStarY  = 0;
			dstRect = new Rect(rightTopCornorStarX ,rightTopCornorStarY,rightTopCornorStarX + right_top_cornor_width,rightTopCornorStarY + right_top_cornor_height);
			
			canvas.drawBitmap(rightTopCornorBitmap, null,  dstRect, null);      
			rightTopCornorBitmap.recycle(); rightTopCornorBitmap = null;

			

			//Right Bottom Cornor
			Bitmap rightBottomCornorBitmap = borderRes.getRightBottomCornorBitmap();		
			int right_bottom_cornor_width  =  calcWithRate(rightBottomCornorBitmap.getWidth(),rate);
			int right_bottom_cornor_height =  calcWithRate(rightBottomCornorBitmap.getHeight(),rate);
			
			
			int rightBottomCornorStarX  = width  - right_bottom_cornor_width;
			int rightBottomCornorStarY  = height - right_bottom_cornor_height;
			dstRect = new Rect(rightBottomCornorStarX ,rightBottomCornorStarY,rightBottomCornorStarX + right_bottom_cornor_width,rightBottomCornorStarY + right_bottom_cornor_height);
			canvas.drawBitmap(rightBottomCornorBitmap, null,  dstRect, null);      
			rightBottomCornorBitmap.recycle(); rightBottomCornorBitmap = null;

			//Left Line
			Bitmap leftBmp = borderRes.getLeftBitmap();  //Left Bitmap
			
			int leftWidth  = calcWithRate(leftBmp.getWidth(),rate);
			int leftHeight = calcWithRate(leftBmp.getHeight(),rate);
			
			int leftScaleHeight = height - left_top_cornor_height - left_bottom_cornor_height;		
			int leftStartX  = 0;
			int leftStartY  = left_top_cornor_height;
			if (leftScaleHeight > 0) {
				//Tile Mode
				Bitmap line_newBitmap = TileBitmapVertical(leftBmp,leftScaleHeight,leftWidth,leftHeight);
				if(line_newBitmap != leftBmp){
					leftBmp.recycle();   
					leftBmp = null;
				}
				dstRect = new Rect(leftStartX ,leftStartY,leftStartX + leftWidth,leftStartY + leftScaleHeight);
				canvas.drawBitmap(line_newBitmap, null,  dstRect, null);    
				line_newBitmap.recycle();  
				line_newBitmap = null;
			}
		

			//Top Line
			Bitmap topBmp = borderRes.getTopBitmap();
			int topHeight  = calcWithRate(topBmp.getHeight(),rate);
			int topWidth   = calcWithRate(topBmp.getWidth(),rate);
			
			int topScaleWidth   = width - left_top_cornor_width - right_top_cornor_width;
			int topStartX  = left_top_cornor_width;
			int topStartY  = 0;
			if (topScaleWidth > 0) {
				Bitmap line_newBitmap = TileBitmapHorizon(topBmp,topScaleWidth,topWidth,topHeight);
				if(line_newBitmap != topBmp){
					topBmp.recycle();   
					topBmp = null;
				}
				dstRect = new Rect(topStartX ,topStartY,topStartX + topScaleWidth,topStartY + topHeight);
				canvas.drawBitmap(line_newBitmap, null,  dstRect, null);    
				line_newBitmap.recycle();  
				line_newBitmap = null;
			}
			
			
			Bitmap rightBmp = borderRes.getRightBitmap();		
			int rightWidth   =  calcWithRate(rightBmp.getWidth(),rate);
			int rightHeight  =  calcWithRate(rightBmp.getHeight(),rate);
			
		
			int rightScaleHeight = height - right_top_cornor_height - right_bottom_cornor_height;
			int rightStartX  = width - rightWidth;
			int rightStartY  = right_top_cornor_height;
			if (rightScaleHeight > 0) {
				Bitmap line_newBitmap = TileBitmapVertical(rightBmp,rightScaleHeight,rightWidth,rightHeight);
				if(line_newBitmap != rightBmp){
					rightBmp.recycle();   rightBmp = null;
				}
				dstRect = new Rect(rightStartX ,rightStartY,rightStartX + rightWidth,rightStartY + rightScaleHeight);
				canvas.drawBitmap(line_newBitmap, null,  dstRect, null);    
				line_newBitmap.recycle();  line_newBitmap = null;
			}
			
			Bitmap bottomBmp = borderRes.getBottomBitmap();
		    
		    int bottomWidth   =  calcWithRate(bottomBmp.getWidth(),rate);
			int bottomHeight  =  calcWithRate(bottomBmp.getHeight(),rate);
			
			int bottomScaleWidth = width - left_bottom_cornor_width - right_bottom_cornor_width;
			int bottomStartX  = left_bottom_cornor_width;
			int bottomStartY  = height - bottomHeight;
			if (bottomScaleWidth > 0 ) {
				Bitmap line_newBitmap = TileBitmapHorizon(bottomBmp,bottomScaleWidth,bottomWidth,bottomHeight);
				if(line_newBitmap != bottomBmp){
					bottomBmp.recycle();   bottomBmp = null;
				}
				dstRect = new Rect(bottomStartX ,bottomStartY,bottomStartX + bottomScaleWidth,bottomStartY + bottomHeight);
				canvas.drawBitmap(line_newBitmap, null,  dstRect, null);    
				line_newBitmap.recycle();  line_newBitmap = null;
			}
			
			
		}catch( OutOfMemoryError ex){
		}
	}
	
	public static WBBorderReturns processNinePathBorderOuter(Context context,int width,int height,WBBorderRes borderRes,boolean isLowMemoryDevice){
		
		Bitmap bmpBorder = null; 
		try{
			bmpBorder = processBorder(context,width,height,borderRes,1,isLowMemoryDevice);
		}catch( OutOfMemoryError ex){
			try{
				if(bmpBorder != null && !bmpBorder.isRecycled()){
					bmpBorder.recycle(); bmpBorder = null;
				}
				bmpBorder = processBorder(context,width,height,borderRes,2,isLowMemoryDevice);
			}catch(OutOfMemoryError ex2){
				try{
					if(bmpBorder != null && !bmpBorder.isRecycled()){
						bmpBorder.recycle(); bmpBorder = null;
					}
					bmpBorder = processBorder(context,width,height,borderRes,4,isLowMemoryDevice);
				}catch(OutOfMemoryError ex3){
					if(bmpBorder != null && !bmpBorder.isRecycled()){
						bmpBorder.recycle(); bmpBorder = null;
					}
					bmpBorder = null;
				}
			}
		}
		
	//	return bmpBorder;
		
		//int width  = srcBmp.getWidth();
		//int height = srcBmp.getHeight();
		
		float rate = getBorderRate(width,height,borderRes); 

		int left = (int)( borderRes.getInnerPx() / rate);
		int top = (int)(borderRes.getInnerPy() / rate);
		int right = (int)(borderRes.getInnerPx2()  / rate);
		int bottom = (int)(borderRes.getInnerPy2() / rate);
        borderRes=null;
		WBBorderReturns br = new WBBorderReturns(bmpBorder,left,top,right,bottom);
		return br;
//		int destWidth  = width  + dx + dx_right;
//		int destHeight = height + dy + dy_bottom;
		
	
		
		/*

		Bitmap newBitmap = null;  
		try{		
			newBitmap = Bitmap.createBitmap(destWidth,destHeight,TBitmapUtility.getBestBitmapConfig());  
		}catch( OutOfMemoryError ex){
			try{
				if(bmpBorder != null && !bmpBorder.isRecycled()){
					bmpBorder.recycle(); bmpBorder = null;
				}
				if(newBitmap != null && !newBitmap.isRecycled()){
					newBitmap.recycle();
					newBitmap = null;
				}
				
				bmpBorder = processBorder(context,width,height,borderRes,2);
				newBitmap = Bitmap.createBitmap(destWidth,destHeight,TBitmapUtility.getBestBitmapConfig());  
			}catch( OutOfMemoryError e2){
				try{
					if(bmpBorder != null && !bmpBorder.isRecycled()){
						bmpBorder.recycle(); bmpBorder = null;
					}
					if(newBitmap != null && !newBitmap.isRecycled()){
						newBitmap.recycle();
						newBitmap = null;
					}
					bmpBorder = processBorder(context,width,height,borderRes,4);
					newBitmap = Bitmap.createBitmap(destWidth,destHeight,TBitmapUtility.getBestBitmapConfig());  
				}catch(OutOfMemoryError e3){
					if(bmpBorder != null && !bmpBorder.isRecycled()){
						bmpBorder.recycle(); bmpBorder = null;
					}
					if(newBitmap != null && !newBitmap.isRecycled()){
						newBitmap.recycle();
						newBitmap = null;
					}
					bmpBorder = processBorder(context,width,height,borderRes,8);
					newBitmap = Bitmap.createBitmap(destWidth,destHeight,TBitmapUtility.getBestBitmapConfig());  
				}
	
			}
		}
		
		Canvas canvas = new Canvas(newBitmap); 
		Paint paint  = new Paint();  
		paint.setFilterBitmap(true);
	//	Rect srcRect = new Rect(0,0,width,height);
	//	Rect dstRect = new Rect(dx,dy,width + dx ,height + dy);
	//	canvas.drawBitmap(srcBmp, srcRect,  dstRect, paint);         //先把原来的图片绘制上去
		
	//	int borderW = bmpBorder.getWidth();
	//	int borderH = bmpBorder.getHeight();
		Rect dstRect = new Rect(0,0,destWidth ,destHeight);
		canvas.drawBitmap(bmpBorder, null,  dstRect, paint);  
		bmpBorder.recycle(); bmpBorder = null;
		

		return newBitmap;*/
	}
	
	
	public static Bitmap processNinePathBorderOuter(Context context,int width,int height,WBBorderRes borderRes,Canvas canvas){
		
		
		try{
			Bitmap bmp = Bitmap.createBitmap(width, height, Config.ARGB_8888);
			Canvas cas = new Canvas(bmp);
			processBorder(context,width,height,borderRes,1,cas);
			return bmp;
		}catch( OutOfMemoryError ex){
		}
		
		return null;
	}
	
	public static Bitmap processNinePathBitmap(Context context,Bitmap srcBmp, WBBorderRes borderRes,boolean isLowMemDevice){
		int width=srcBmp.getWidth();
		int height=srcBmp.getHeight();
		Bitmap bmpBorder = null; 
		try{
			bmpBorder = processBorder(context,width,height,borderRes,1,isLowMemDevice);
		}catch( OutOfMemoryError ex){
			try{
				if(bmpBorder != null && !bmpBorder.isRecycled()){
					bmpBorder.recycle(); bmpBorder = null;
				}
				bmpBorder = processBorder(context,width,height,borderRes,2,isLowMemDevice);
			}catch(OutOfMemoryError ex2){
				try{
					if(bmpBorder != null && !bmpBorder.isRecycled()){
						bmpBorder.recycle(); bmpBorder = null;
					}
					bmpBorder = processBorder(context,width,height,borderRes,4,isLowMemDevice);
				}catch(OutOfMemoryError ex3){
					if(bmpBorder != null && !bmpBorder.isRecycled()){
						bmpBorder.recycle(); bmpBorder = null;
					}
					bmpBorder = null;
				}
			}
		}
		
		
		//int width  = srcBmp.getWidth();
		//int height = srcBmp.getHeight();
		
		float rate = getBorderRate(width,height,borderRes); 

		int dx = (int)( borderRes.getInnerPx() / rate);
		int dy = (int)(borderRes.getInnerPy() / rate);
		int dx_right = (int)(borderRes.getInnerPx2()  / rate);
		int dy_bottom = (int)(borderRes.getInnerPy2() / rate);

		int destWidth  = width  + dx + dx_right;
		int destHeight = height + dy + dy_bottom;
		
	
		
		

		Bitmap newBitmap = null;  
		try{		
			newBitmap = Bitmap.createBitmap(destWidth,destHeight,Config.ARGB_8888);  
		}catch( OutOfMemoryError ex){
			try{
				if(bmpBorder != null && !bmpBorder.isRecycled()){
					bmpBorder.recycle(); bmpBorder = null;
				}
				if(newBitmap != null && !newBitmap.isRecycled()){
					newBitmap.recycle();
					newBitmap = null;
				}
				
				bmpBorder = processBorder(context,width,height,borderRes,2,isLowMemDevice);
				newBitmap = Bitmap.createBitmap(destWidth,destHeight,Config.ARGB_8888);  
			}catch( OutOfMemoryError e2){
				try{
					if(bmpBorder != null && !bmpBorder.isRecycled()){
						bmpBorder.recycle(); bmpBorder = null;
					}
					if(newBitmap != null && !newBitmap.isRecycled()){
						newBitmap.recycle();
						newBitmap = null;
					}
					bmpBorder = processBorder(context,width,height,borderRes,4,isLowMemDevice);
					newBitmap = Bitmap.createBitmap(destWidth,destHeight,Config.ARGB_8888);  
				}catch(OutOfMemoryError e3){
					if(bmpBorder != null && !bmpBorder.isRecycled()){
						bmpBorder.recycle(); bmpBorder = null;
					}
					if(newBitmap != null && !newBitmap.isRecycled()){
						newBitmap.recycle();
						newBitmap = null;
					}
					bmpBorder = processBorder(context,width,height,borderRes,8,isLowMemDevice);
					newBitmap = Bitmap.createBitmap(destWidth,destHeight,Config.ARGB_8888);  
				}
	
			}
		}
		
		Canvas canvas = new Canvas(newBitmap); 
		Paint paint  = new Paint();  
		paint.setFilterBitmap(true);
		Rect srcRect = new Rect(0,0,width,height);
		Rect dstRect = new Rect(dx,dy,width + dx ,height + dy);
		canvas.drawBitmap(srcBmp, srcRect,  dstRect, paint);         //先把原来的图片绘制上去
		
		int borderW = bmpBorder.getWidth();
		int borderH = bmpBorder.getHeight();
	    dstRect = new Rect(0,0,destWidth ,destHeight);
		canvas.drawBitmap(bmpBorder, null,  dstRect, paint);  
		bmpBorder.recycle(); bmpBorder = null;
		

		return newBitmap;

	}
	
	
	
	public static Bitmap fromBmp(Bitmap srcBmp){
		int width  = srcBmp.getWidth();
		int height = srcBmp.getHeight();
		
		Bitmap newBitmap = null;  
		
	
		//newBitmap = Bitmap.createBitmap(destWidth,destHeight,Config.ARGB_8888);  
		newBitmap = Bitmap.createBitmap(width,height,Config.ARGB_8888);  
			
		
		
		Canvas canvas = new Canvas(newBitmap);   
		Paint paint = new Paint();  
		//canvas.drawBitmap(srcBmp, 0,  0, paint);
		Rect rect = new Rect();
		rect.left = 0;
		rect.right = width;
		rect.top = 0;
		rect.bottom = height;
		canvas.drawBitmap(srcBmp, rect, rect, paint);

		//canvas.drawBitmap(srcBmp, rect, rect, paint);
		//canvas.save(Canvas.ALL_SAVE_FLAG);  
		
				// 存储新合成的图片  
				//canvas.restore();  
		return newBitmap;
	} 

	private static Bitmap TileBitmapVertical(Bitmap bitmap,int totalHeight, int width, int height){
		int count_1      =  totalHeight  / height;
		int remainer_1   =  totalHeight  % height;
		int detect_count = count_1;
		if(remainer_1 != 0){
			int s1 = totalHeight - height * count_1;
			int s2 = height * (count_1 + 1) - totalHeight;
			
			if(s2 < s1){
				detect_count = count_1 + 1;
			}
		}
		
		int detailHeight = detect_count * height;
		if(detailHeight <= 0){
			detailHeight = totalHeight;
		}
		Bitmap line_newBitmap = null;  
		line_newBitmap = Bitmap.createBitmap(width,detailHeight,Config.ARGB_8888);  
		Canvas canvas_left = new Canvas(line_newBitmap); 
		Rect dstLineRect = new Rect(0,0,width,height);
		Rect dstLineRect0 = new Rect(0, 0, width, detailHeight);
//		BitmapShader bs = new BitmapShader(bitmap, TileMode.REPEAT, TileMode.REPEAT);
//		Paint p = new Paint();
//		p.setShader(bs);
//		Rect dstRect = new Rect(0,0,width,detailHeight);
////		Matrix m = new Matrix();
////		m.postTranslate(dstRect.left, dstRect.right);
////		p.getShader().setLocalMatrix(m);
//		canvas_left.drawRect(dstRect, p);
		if (detect_count == 0) {
			canvas_left.drawBitmap(bitmap, null, dstLineRect0, null);
		}else {
			for(int i =0 ; i < detect_count; i++){
				canvas_left.drawBitmap(bitmap, null, dstLineRect, null);
				dstLineRect.top += height;
				dstLineRect.bottom += height;
			}
		}
		
		return line_newBitmap;
	}
	
	private static Bitmap TileBitmapHorizon(Bitmap bitmap,int totalWidth, int width, int height){
		int count_1      =  totalWidth  / width;
		int remainer_1   =  totalWidth  % width;
		int detect_count = count_1;
		if(remainer_1 != 0){
			int s1 = totalWidth - width * count_1;
			int s2 = width * (count_1 + 1) - totalWidth;
			
			if(s2 < s1){
				detect_count = count_1 + 1;
			}
		}
		
		int detailWidth = detect_count * width;
		if(detailWidth <= 0){
//			detailWidth = 1;
			detailWidth = totalWidth;
		}
		Bitmap line_newBitmap = null;  
		line_newBitmap = Bitmap.createBitmap(detailWidth,height,Config.ARGB_8888);  
		Canvas canvas_left = new Canvas(line_newBitmap); 
//		BitmapShader bs = new BitmapShader(bitmap, TileMode.REPEAT, TileMode.REPEAT);
//		Paint p = new Paint();
//		p.setShader(bs);
//		Rect dstRect = new Rect(0,0,detailWidth,height);
////		Matrix m = new Matrix();
////		m.postTranslate(dstRect.left, dstRect.right);
////		p.getShader().setLocalMatrix(m);
//		canvas_left.drawRect(dstRect, p);
		
		Rect dstLineRect = new Rect(0,0,width,height);
		Rect dstLineRect0 = new Rect(0,0,detailWidth,height);
		if (detect_count == 0) {
			canvas_left.drawBitmap(bitmap, null, dstLineRect0, null);
		}else {
			for(int i =0 ; i < detect_count; i++){
				canvas_left.drawBitmap(bitmap, null, dstLineRect, null);
				dstLineRect.left  += width;
				dstLineRect.right += width;
			}
		}
		
		return line_newBitmap;
	}

	private static int calcWithRate(int value, float rate){
		int value_rate  = (int)(value  * rate);
		float value_rate_f  = value  * rate;
		
		float diff = value_rate_f - value_rate;
		if(Math.abs(diff) >= 0.5f){
			value_rate += 1;
		}
		
		return value_rate;
	}
}
