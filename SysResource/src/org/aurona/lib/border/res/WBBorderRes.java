package org.aurona.lib.border.res;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.aurona.lib.resource.WBImageRes;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;


public class WBBorderRes  extends WBImageRes{
	
	public enum BorderType{IMAGE,NINE};   //Scale Image, Nine
	public enum BackgroundType{NORMAL,GRADIENT,BGIMAGE};   //Scale Image, Nine
	
	
	private int defaultColor;
	
	private List<Integer> gradientColorArray;
	
	private BorderType borderType;
	
	private String leftUri;
	private String rightUri;
	private String topUri;
	private String bottomUri;
	
	private String leftTopCornorUri;
	private String leftBottomCornorUri;
	private String rightTopCornorUri;
	private String rightBottomCornorUri;
	private Orientation orientation=Orientation.LEFT_RIGHT;
	private BackgroundType backgroundType = BackgroundType.NORMAL;
	
	
	private int  innerPx;  //InnerPx
	private int  innerPy; 
	
	private int  innerPx2;
	private int  innerPy2;
	private int mapSize;

	private String bgImageResName;
	
	public Orientation getGradientOrientation() {
		return orientation;
	}

	public void setOrientation(Orientation orientation) {
		this.orientation = orientation;
	}
	public int getDefaultColor() {
		return defaultColor;
	}

	public void setDefaultColor(int defaultColor) {
		this.defaultColor = defaultColor;
	}

	public String getLeftUri() {
		return leftUri;
	}

	public void setLeftUri(String leftUri) {
		this.leftUri = leftUri;
	}

	public String getRightUri() {
		return rightUri;
	}

	public void setRightUri(String rightUri) {
		this.rightUri = rightUri;
	}

	public String getTopUri() {
		return topUri;
	}

	public void setTopUri(String topUri) {
		this.topUri = topUri;
	}

	public String getBottomUri() {
		return bottomUri;
	}

	public void setBottomUri(String bottomUri) {
		this.bottomUri = bottomUri;
	}

	public String getLeftTopCornorUri() {
		return leftTopCornorUri;
	}

	public void setLeftTopCornorUri(String leftTopCornorUri) {
		this.leftTopCornorUri = leftTopCornorUri;
	}

	public String getLeftBottomCornorUri() {
		return leftBottomCornorUri;
	}

	public void setLeftBottomCornorUri(String leftBottomCornorUri) {
		this.leftBottomCornorUri = leftBottomCornorUri;
	}

	public String getRightTopCornorUri() {
		return rightTopCornorUri;
	}

	public void setRightTopCornorUri(String rightTopCornorUri) {
		this.rightTopCornorUri = rightTopCornorUri;
	}

	public String getRightBottomCornorUri() {
		return rightBottomCornorUri;
	}

	public void setRightBottomCornorUri(String rightBottomCornorUri) {
		this.rightBottomCornorUri = rightBottomCornorUri;
	}

	public BorderType getBorderType() {
		return borderType;
	}

	public void setBorderType(BorderType borderType) {
		this.borderType = borderType;
	}

	public BackgroundType getBackgroundType() {
		return backgroundType;
	}

	public void setBackgroundType(BackgroundType backgroundType) {
		this.backgroundType = backgroundType;
	}

	public List<Integer> getGradientColorArray() {
		return gradientColorArray;
	}

	public void setGradientColorArray(List<Integer> gradientColorArray) {
		this.gradientColorArray = gradientColorArray;
	}
	
	
	public Drawable getGradientDrawable(){
		GradientDrawable gradientDrawable = null;
		if(this.backgroundType == BackgroundType.NORMAL){
		//	 Orientation orientation = Orientation.LEFT_RIGHT;
			 int[] colors = new int[2];
			 colors[0] = defaultColor;
			 colors[1] = defaultColor;
			 gradientDrawable = new GradientDrawable(orientation, colors);
			 gradientDrawable.setGradientType(0);

		}else if(this.backgroundType == BackgroundType.GRADIENT){
		//	 Orientation orientation = Orientation.LEFT_RIGHT;
			 int[] colors = new int[2];
			 colors[0] = gradientColorArray.get(0);
			 colors[1] = gradientColorArray.get(1);

			 gradientDrawable = new GradientDrawable(orientation, colors);
			 gradientDrawable.setGradientType(0);

		}
		return gradientDrawable;
	}


	public String getBgImageResName() {
		return bgImageResName;
	}

	public void setBgAssetPath(String resName) {
		this.bgImageResName = resName;
	}
	
	public int getInnerPx() {
		return innerPx;
	}

	public void setInnerPx(int innerPx) {
		this.innerPx = innerPx;
	}
	
	public int getInnerPy() {
		return innerPy;
	}

	public void setInnerPy(int innerPy) {
		this.innerPy = innerPy;
	}
	
	
	public int getInnerPx2() {
		return innerPx2;
	}

	public void setInnerPx2(int innerPx2) {
		this.innerPx2 = innerPx2;
	}
	
	public int getInnerPy2() {
		return innerPy2;
	}

	public void setInnerPy2(int innerPy2) {
		this.innerPy2 = innerPy2;
	}
	
	public int getMapSize() {
		return mapSize;
	}

	public void setMapSize(int mapSize) {
		this.mapSize = mapSize;
	}
	
	
	public Bitmap getLeftTopCornorBitmap(){
		Bitmap bmp = null;
		if(leftTopCornorUri != null){
			bmp = this.readFromAssert(context, this.leftTopCornorUri);
		}
		return bmp;
	}
	
	public Bitmap getLeftBottomCornorBitmap(){		
		Bitmap bmp = null;
		if(leftBottomCornorUri != null){
			bmp = this.readFromAssert(context, this.leftBottomCornorUri);
		}
		return bmp;
	}
	
	public Bitmap getRightTopCornorBitmap(){
		Bitmap bmp = null;
		if(rightTopCornorUri != null){
			bmp = this.readFromAssert(context, this.rightTopCornorUri);
		}
		return bmp;
	}
	
	
	public Bitmap getRightBottomCornorBitmap(){
		Bitmap bmp = null;
		if(rightBottomCornorUri != null){
			bmp = this.readFromAssert(context, this.rightBottomCornorUri);
		}
		return bmp;
	}
	
	public Bitmap getLeftBitmap(){
		Bitmap bmp = null;
		if(leftUri != null){
			bmp = this.readFromAssert(context, this.leftUri);
		}
		return bmp;
	}
	
	public Bitmap getTopBitmap(){
		Bitmap bmp = null;
		if(topUri != null){
			bmp = this.readFromAssert(context, this.topUri);
		}
		return bmp;
	}
	
	public Bitmap getRightBitmap(){
		Bitmap bmp = null;
		if(rightUri != null){
			bmp = this.readFromAssert(context, this.rightUri);
		}
		return bmp;
	}
	
	public Bitmap getBottomBitmap(){
		Bitmap bmp = null;
		if(bottomUri != null){
			bmp = this.readFromAssert(context, this.bottomUri);
		}
		return bmp;
	}
	
	
	protected Bitmap readFromAssert(Context context,String uri){
		Bitmap bmp = null;
		try {
			AssetManager assetManager = context.getAssets();
			InputStream stream = assetManager.open(uri);
			bmp = BitmapFactory.decodeStream(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bmp;
	}
	
}
