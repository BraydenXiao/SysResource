package org.aurona.lib.resource;




public class WBColorRes extends WBRes{
	private int colorValue;
	
	public  int getColorValue(){
		return this.colorValue;
	}

	public void setColorValue(int color) {
		this.colorValue = color;
	}
	
	public void setColorID(int id){
		this.colorValue = this.getResources().getColor(id);
	}
}
