package org.aurona.lib.border.res;

import android.graphics.Bitmap;

public class WBBorderReturns {

	public Bitmap frameBitmap;
	public int left;
	public int top;
	public int right;
	public int bottom;
	public WBBorderReturns(Bitmap frameBitmap,int left,int top,int right,int bottom)
	{
		this.frameBitmap=frameBitmap;
		this.left=left;
		this.top=top;
		this.right=right;
		this.bottom=bottom;
	}
	public Bitmap getFrameBitmap()
	{
		return this.frameBitmap;
	}
	public void setFrameBitmap(Bitmap bitmap)
	{
		 this.frameBitmap=bitmap;
	}
	public int getLeft()
	{
		return this.left;
	}
	public int getTop()
	{
		return this.top;
	}
	public int getRight()
	{
		return this.right;
	}
	public int getBottom()
	{
		return this.bottom;
	}
	public void recycleFrameBitmap()
	{
		if(this.frameBitmap!=null)
		{
			if(!this.frameBitmap.isRecycled())
			{
				this.frameBitmap.recycle();
			}
			this.frameBitmap=null;
		}
	}
}
