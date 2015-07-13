package org.aurona.lib.resource.view;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.aurona.lib.resource.adapter.ResImageAdapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.aurona.lib.sysresource.R;

public class ResImageLayout extends LinearLayout {

	private ResImageAdapter adapter;
	private Context mContext;
	
	private int mSelLocation = -1;
	private Drawable mSelViewBackImage = null;
	private View mSelView = null;
	
	public ResImageLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
	}

	public void setSelectImageLocation(int pos){
		mSelLocation = pos;
	}
	
	public void setSelectViewBackImage(Drawable bgimage){
		mSelViewBackImage = bgimage;
	}
	
	public void clearSelectViewBackImage(){
		if(mSelView != null){
			ImageView imageView = (ImageView)mSelView.findViewById(R.id.item_image);
			Map<String,Object> selmap = ResImageLayout.this.adapter.getItem(mSelLocation);
			imageView.setImageBitmap((Bitmap)selmap.get("image"));
			viewSetBackground(mSelView,null);
			mSelLocation = -1;
			mSelView = null;
		}
	}
	
	public OnItemClickListener mitemListener;
	
	public void setAdapter(ResImageAdapter adapter) {
		this.adapter = adapter;
		this.removeAllViews();
		
		if(adapter == null) return;
		
		for(int i = 0; i < adapter.getCount(); i++){	
			final Map<String,Object> map = adapter.getItem(i);
			
			View view = adapter.getView(i, null, null);
			if(i== mSelLocation)
			{
				mSelView = view;
				ImageView imageView = (ImageView)view.findViewById(R.id.item_image);
				if(map.get("imageSelAssetFile") != null)
				{
					Bitmap assetbmp = getImageFromAssetsFile(mContext.getResources(),
							String.valueOf(map.get("imageSelAssetFile")));
					imageView.setImageBitmap(assetbmp);
				}
			}
			
			view.setTag(i);
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(mitemListener != null){
						int id = 0;
						if(v.getTag() != null){
							id = Integer.parseInt(String.valueOf(v.getTag()));

							ImageView imageView = (ImageView)v.findViewById(R.id.item_image);
							
							if(map.get("imageSelAssetFile") != null){
								String strValueString = String.valueOf(map.get("imageSelAssetFile"));
								Bitmap assetbmp = getImageFromAssetsFile(mContext.getResources(),
										strValueString);
								imageView.setImageBitmap(assetbmp);
							}
							
							if(mSelView != null && mSelLocation != -1 && id != mSelLocation){
								imageView = (ImageView)mSelView.findViewById(R.id.item_image);
								Map<String,Object> selmap = ResImageLayout.this.adapter.getItem(mSelLocation);
								imageView.setImageBitmap((Bitmap)selmap.get("image"));
								
								
								viewSetBackground(mSelView,null);
								//mSelView.setBackground(null);
							}

							if(mSelViewBackImage != null){
								viewSetBackground(v,mSelViewBackImage);

								//v.setBackground(mSelViewBackImage);
							}
							
							mSelView = v;
							mSelLocation = id;
						}
						if(map.get("id") != null){
							id = (Integer)map.get("id");
						}
						String tx = "";
						if(map.get("text") != null){
							tx = map.get("text").toString();
						}
						mitemListener.ItemClick(v,id,tx);
					}
				}
			});
//			this.setOrientation(HORIZONTAL);
			this.addView(view,new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		}
	}
	

	public void setOnItemClickListener(OnItemClickListener itemListener){
		mitemListener = itemListener;
	}
	
	
    public interface OnItemClickListener {
        void ItemClick(View vw,int id,String strMessage);
    }

	public Bitmap getImageFromAssetsFile(Resources res,String fileName){
        Bitmap image = null;
        try{
            InputStream is = res.getAssets().open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return image;
    }
	
	protected void viewSetBackground(View v,Drawable dr){
		int sdk = android.os.Build.VERSION.SDK_INT;
		if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
		    v.setBackgroundDrawable(dr);
		} else {
			viewSetBackgroundJellyBean(v,dr);
		}
	}
	
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	protected void viewSetBackgroundJellyBean(View v,Drawable dr){
		  v.setBackground(dr);
	}
	
}
