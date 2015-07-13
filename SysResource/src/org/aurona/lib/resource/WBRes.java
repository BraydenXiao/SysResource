package org.aurona.lib.resource;

import org.aurona.lib.bitmap.BitmapUtil;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;

public abstract class WBRes {
	public enum LocationType{RES,ASSERT,FILTERED,ONLINE,CACHE};
	
	private String name;  
	private String iconFileName; 
	private int iconID;
	private LocationType iconType;  
	protected Context context;
	private Bitmap iconBitmap;
	protected Boolean asyncIcon = false;
	private boolean isNew = false;
	
	private String managerName;

	private boolean isShowText=false;
	private String showText;
	private int textColor=0;
	
	private boolean isCircle=false;
	
	public WBRes(){
		//res = getResources();
	}
	public void setIsShowText(boolean flag)
	{
		this.isShowText=flag;
	}
	public Boolean getIsShowText()
	{
		return this.isShowText;
	}
	public void setShowText(String showText)
	{
		this.showText=showText;
	}
	public String getShowText()
	{
		return this.showText;
	}
	
	public int getTextColor() {
		return textColor;
	}
	
	public void setTextColor(int textColor) {
		this.textColor = textColor;
	}
	
	
	public boolean isCircle() {
		return isCircle;
	}
	
	public void setCircle(boolean isCircle) {
		this.isCircle = isCircle;
	}
	
	/*
	 * ��ע�⣬����ΪResource������ʽ����setContext,����ᵼ�¿�ָ���쳣
	 * */
	public void setContext(Context context){
		this.context = context;
	}
	public Resources getResources(){
		if(this.context != null){
			return this.context.getResources();
		}
		return null;
	}

	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getIconFileName() {
		return iconFileName;
	}
	
	public void setIconFileName(String icon) {
		this.iconFileName = icon;
	}
	
	public int getIconID(){
		return iconID;
	}
	
	public void setIconID(int id){
		this.iconID=id;
	}

	public LocationType getIconType() {
		return iconType;
	}

	public void setIconType(LocationType iconType) {
		this.iconType = iconType;
	}
	
	public String getType() {
		return "TRes";
	}
	
	public Boolean getAsyncIcon(){
		return asyncIcon;
	}
	
	public void getAsyncIconBitmap(WBAsyncPostIconListener listener){

	}
	
	public  Bitmap getIconBitmap(){
		if(this.iconFileName == null) return null;
		if(iconType == LocationType.RES){
			return BitmapUtil.getImageFromResourceFile(this.getResources(), iconID);
		}else if(iconType == LocationType.ASSERT){
			return BitmapUtil.getImageFromAssetsFile(this.getResources(),iconFileName);
		}
		return iconBitmap;
	}

	public boolean getIsNewValue(){
		return isNew;
	}
	
	public void setIsNewValue(boolean value){
		this.isNew=value;
	}
	
	public String getManagerName() {
		return managerName;
	}
	
	public void setManagerName(String name) {
		this.managerName = name;
	}
}
