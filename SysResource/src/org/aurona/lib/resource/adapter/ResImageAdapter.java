package org.aurona.lib.resource.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.aurona.lib.sysresource.R;
import org.aurona.lib.sysutillib.ScreenInfoUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class ResImageAdapter extends BaseAdapter {
	private List<Map<String,Object>> list;
	private Context context;
	private int width = 68, height = 28;
	private int textColor = Color.BLACK;
	
	public ResImageAdapter(Context context){
		this.context=context;
		this.list=new ArrayList<Map<String,Object>>();
		width = ScreenInfoUtil.dip2px(context,width);
		height = ScreenInfoUtil.dip2px(context,height);
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Map<String,Object> getItem(int location) {
		return list.get(location);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	public void addObject(Map<String,Object> map){
		list.add(map);
		notifyDataSetChanged();
	}
	
	int imageWidth=32;int imageHeight=32;
	public void setImageItemSize(int width,int height){
		imageWidth = width;
		imageHeight =height;
//		this.width = width;
//		this.height =height;
//		this.width = ScreenInfoUtil.dip2px(context,width);
//		this.height = ScreenInfoUtil.dip2px(context,height);
	}
	
	public void SetTextColor(int color)
	{
		textColor = color;
	}
	
	@Override
	public View getView(int location, View arg1, ViewGroup arg2) {
		View view = LayoutInflater.from(context).inflate(R.layout.res_view_image_item,null);
		ImageView imageView = (ImageView)view.findViewById(R.id.item_image);
		Map<String,Object> map = getItem(location);
		imageView.setImageBitmap((Bitmap)map.get("image"));

//		Drawable drawable = new BitmapDrawable((Bitmap)map.get("image"));  
//		imageView.setBackground(drawable);selecter_test_imageview
//		imageView.setBackgroundResource(R.drawable.selecter_test_imageview);
//		image.getLayoutParams().height = height;
//		image.getLayoutParams().width = width;
//		imageView.invalidate();
		
		TextView text = (TextView)view.findViewById(R.id.item_text);
		if(map.get("text") != null){    
			if(map.get("textColor") != null){
				text.setTextColor(((Integer)map.get("textColor")).intValue());
			}
			String str = map.get("text").toString();
			int intindex = str.indexOf("_") + 1;
			str = str.substring(intindex);
			text.setText(str);
			text.setVisibility(View.VISIBLE);
		}else{
			text.setVisibility(View.INVISIBLE);
			FrameLayout.LayoutParams lf = (FrameLayout.LayoutParams)imageView.getLayoutParams();
			lf.topMargin = 0;
			lf.height = ScreenInfoUtil.dip2px(context,imageWidth);
			lf.width = ScreenInfoUtil.dip2px(context,imageHeight);
			lf.gravity = Gravity.CENTER;
		}
		if(textColor != Color.BLACK)
		{
			text.setTextColor(textColor);
		}
		
		return view;
	}
	
	public void dispose(){
		if(list != null){
			for(int i=0;i<list.size();i++)
			{
				Map<String,Object> map = list.get(i);
				if(map != null && map.get("image")!=null)
				{
					Bitmap bmp = (Bitmap)map.get("image");
					if(!bmp.isRecycled())
					{
						bmp.recycle();
						bmp = null;
					}
				}
			}
			list.clear();
			list = null;
		}
	}
}
