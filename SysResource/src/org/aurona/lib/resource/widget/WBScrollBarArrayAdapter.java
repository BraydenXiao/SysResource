package org.aurona.lib.resource.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.aurona.lib.sysresource.R;

import org.aurona.lib.database.ResRecordBean;
import org.aurona.lib.database.SQLiteHelper;
import org.aurona.lib.resource.WBAsyncPostIconListener;
import org.aurona.lib.resource.WBColorRes;
import org.aurona.lib.resource.WBImageRes;
import org.aurona.lib.resource.WBRes;
import org.aurona.lib.resource.WBRes.LocationType;
import org.aurona.lib.sysutillib.ScreenInfoUtil;
import org.aurona.lib.view.image.BorderImageView;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.PorterDuff.Mode;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.TextView;

/** An array adapter that knows how to render views when given CustomData classes */
public class WBScrollBarArrayAdapter extends ArrayAdapter<WBRes> {
	
    /** View holder for the views we need access to */
    private class Holder {
    	public  BorderImageView iconImageView;
    	public  Bitmap iconBitmap;
    	public  ProgressBar progressBar;
    	public  ImageView imgDownload;
    	public  ImageView imgBackGround;
    	public  TextView tx_text;
    	public  ImageView imageNew;
    	public  ImageView imgItemSelect;
    }
	
	private Context mContext;
	private String pkgName;
    private LayoutInflater mInflater;
    BorderImageView mCurSelectedItem;
    Holder mCurSelectedHolder;
    
    private static final int MSG_SUCCESS = 0;
    private static final int MSG_FAILURE = 1;
    
    public int selectedPos=-1;
    private int mSelectBorderColor = Color.rgb(0, 235, 232);
    

//	private static String DB_NAME = "resrecord.db";
//	private static int DB_VERSION = 1;
//	private SQLiteDatabase db;
//	private SQLiteHelper dbHelper;
    
    HashMap<Integer,View> posViewMap = new HashMap<Integer,View>();
    
    private int imageBorderWidthDp = 52;
    private int imageBorderHeightDp = 52;
    private int containHeightDp  = 60;
    
    private List<Holder> holderArray = new ArrayList<Holder>();
    
    private Bitmap filterSrc;
    private int mTextViewColor = Color.BLACK;;
    private int mTextViewBackColor = Color.TRANSPARENT;
    private int mTextViewWidth = 52;
    private int mTextViewHeight = -1;
    private int mTextViewTextSize = 11;
    private boolean isWithHalfShow = false;  //根据屏幕尺寸，控制显示半个
    private int mViewWidthDp = 0;
    private ScaleType mBorderViewScaleType =  ScaleType.FIT_CENTER;
    private boolean isSetScaleType = false;
    private boolean isFillet = false;
    private boolean isBottomSelState = false;
    
    int count = 0;
    
    public WBScrollBarArrayAdapter(Context context, WBRes[] values) {
       super(context, R.layout.res_view_widget_selectitem, values);
       if(values != null)
       count = values.length;
        mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
        pkgName = context.getApplicationInfo().packageName;  
//        try {
//    		dbHelper = new SQLiteHelper(mContext, DB_NAME, null, DB_VERSION);
//    		db = dbHelper.getWritableDatabase();
//		} catch (Exception e) {
//
//		}
    }
    
    public void setBottomSelState(boolean state)
    {
    	isBottomSelState = state;
    }
    
    public boolean getBottomSelState()
	{
		return isBottomSelState;
	}
    
	public void setFilletState(boolean state)
	{
		isFillet = state;
	}
	
	public boolean getFilletState()
	{
		return isFillet;
	}
    
    public void setIsWithHalfShow(boolean isHalfShow){
    	this.isWithHalfShow = isHalfShow;
    }
    
    public boolean isWithHalfShow(){
    	return this.isWithHalfShow;
    }
    
    public void setImageBorderViewLayout(int widthDp,int heightDp){
    	imageBorderWidthDp  = widthDp;
    	imageBorderHeightDp = heightDp;
    }
    
    public void setImageBorderViewLayout(int containHeightDp,int widthDp,int heightDp){
    	this.containHeightDp     = containHeightDp;
    	this.imageBorderWidthDp  = widthDp;
    	this.imageBorderHeightDp = heightDp;
    } 
    
    public void setImageBorderViewScaleType(ScaleType scaleType){
    	isSetScaleType = true;
    	this.mBorderViewScaleType = scaleType;
    }
    
    
    public void setViewWidthDp(int viewWidthDp){
    	mViewWidthDp = viewWidthDp;
    }
    
    public void setTextViewColor(int color){
    	mTextViewColor = color;
    }
    
    public void setTextViewBackColor(int color){
    	mTextViewBackColor = color;
    }
    
    public void setTextViewWidthDp(int width)
    {
    	mTextViewWidth = width;
    }
    
    public void setTextViewHeightDp(int height)
    {
    	mTextViewHeight = height;
    }
    
    public void setTextViewTextSize(int size)
    {
    	mTextViewTextSize = size;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//    	Log.v("lbs", String.valueOf(position));
//    	
        final Holder holder;
        
        try{
        	boolean isImageResInLocal = true;
        	final WBRes posRes = getItem(position); 
        	
        	posRes.setContext(mContext);
        	if(posRes instanceof WBImageRes){
        		WBImageRes shapeRes = (WBImageRes)posRes;   
            	  if(!shapeRes.isImageResInLocal(this.getContext())){
            		  isImageResInLocal = false;
            	  }
        	}
        	
      	//boolean isNewItme = isNewItem(posRes);
        	
        if (convertView == null) {
                convertView = mInflater.inflate(R.layout.res_view_widget_selectitem, parent, false);
                BorderImageView icon = (BorderImageView) convertView.findViewById(R.id.item_icon);
                
                if(isFillet)
                {
                	icon.setFilletState(isFillet);
                }
                
                //lb added, process Scale的问题
                if(isSetScaleType){
                	icon.setScaleType(this.mBorderViewScaleType);
                }
                
                ViewGroup.LayoutParams lf_self = convertView.getLayoutParams();
                if(lf_self != null){
                	lf_self.height  = ScreenInfoUtil.dip2px(this.getContext(), containHeightDp);
                	if(ScreenInfoUtil.dip2px(this.getContext(), imageBorderWidthDp + 8) > lf_self.width){
                		lf_self.width = ScreenInfoUtil.dip2px(this.getContext(), imageBorderWidthDp + 8); 
                	}
                	
                	if(mViewWidthDp > 0){
                		lf_self.width = ScreenInfoUtil.dip2px(this.getContext(),mViewWidthDp);
                	}
                	
                	if(isWithHalfShow){
                		float width_px = (float)ScreenInfoUtil.screenWidth(this.mContext);
                		int   item_px  = lf_self.width;
                		
                		float rt = width_px / item_px;
                		rt = rt - (int)rt;
                		int idx_max  = 100;
                		int idx_loop = 0;
                		while(rt <= 0.4 || rt >= 0.6 || idx_loop > idx_max){
                			item_px += 1;
                			rt = width_px / item_px;
                    		rt = rt - (int)rt;
                		}
                		
                		lf_self.width = item_px;
                	}
                }

                FrameLayout.LayoutParams lf = (FrameLayout.LayoutParams)icon.getLayoutParams();
                if(lf != null){
                	lf.width   = ScreenInfoUtil.dip2px(this.getContext(), imageBorderWidthDp);
                	lf.height  = ScreenInfoUtil.dip2px(this.getContext(), imageBorderHeightDp);
                }
                
                FrameLayout item_layout = (FrameLayout) convertView.findViewById(R.id.item_layout);
                FrameLayout.LayoutParams il = (FrameLayout.LayoutParams)item_layout.getLayoutParams();
                if(il != null){
                	il.width   = ScreenInfoUtil.dip2px(this.getContext(), imageBorderWidthDp);
                	il.height  = ScreenInfoUtil.dip2px(this.getContext(), imageBorderHeightDp);
                }
                
                if(posRes.getIsShowText()){
                	lf.bottomMargin = ScreenInfoUtil.dip2px(this.getContext(), 6);
                	il.bottomMargin = ScreenInfoUtil.dip2px(this.getContext(), 6);
                }
                
                ProgressBar progressBar = (ProgressBar)convertView.findViewById(R.id.progressBar);
                ImageView imgDownload = (ImageView)convertView.findViewById(R.id.imageDownload);
                ImageView imageBackGround = (ImageView)convertView.findViewById(R.id.imageBackGround);
                ImageView imgItemSelect = (ImageView)convertView.findViewById(R.id.imgItemSelect);
                TextView tx_text = (TextView)convertView.findViewById(R.id.textView1);
                
                tx_text.setTextColor(mTextViewColor);
                
                if(mTextViewBackColor != Color.TRANSPARENT)
                {
                	tx_text.setBackgroundColor(mTextViewBackColor);
                }
                tx_text.setWidth(ScreenInfoUtil.dip2px(this.getContext(),mTextViewWidth));
                tx_text.setTextSize(mTextViewTextSize);
                
                if(mTextViewHeight>0)
                {
                	tx_text.setHeight(ScreenInfoUtil.dip2px(this.getContext(),mTextViewHeight));
                }
                
                if(posRes.getIsShowText()){
                	tx_text.setVisibility(View.VISIBLE);
                }else{
                	tx_text.setVisibility(View.INVISIBLE);
                }
             //    tx_text.setText("test");
//                ImageView imageNew = (ImageView)convertView.findViewById(R.id.imageNew);
//                if(isNewItme)
//                	imageNew.setVisibility(View.VISIBLE);
//                else {
//                	imageNew.setVisibility(View.INVISIBLE);
//				}
    			icon.setTag(posRes);   
    			
    		
                // Create and save off the holder in the tag so we get quick access to inner fields
                // This must be done for performance reasons
                holder = new Holder();
                holder.iconImageView   = icon;
                holder.progressBar = progressBar;
                holder.imgDownload = imgDownload;
                holder.imgBackGround = imageBackGround;
                holder.imgItemSelect = imgItemSelect;
                //holder.imageNew = imageNew;
                holder.tx_text  = tx_text;
                if(selectedPos == position){
                	mCurSelectedItem = holder.iconImageView;
                	mCurSelectedHolder = holder;
                	if(!isBottomSelState)
                	{
	                	holder.iconImageView.setBorderColor(mSelectBorderColor);
	                	holder.iconImageView.setShowBorder(true);
	                	holder.iconImageView.invalidate();
                	}
                	else {
                		holder.imgItemSelect.setVisibility(View.VISIBLE);
					}
                }
                
                if(isImageResInLocal){
                	imgDownload.setVisibility(View.INVISIBLE);
                   	holder.imgBackGround.setVisibility(View.INVISIBLE);
            		setAlphaForView(imgDownload, 0f);
        			setAlphaForView(imageBackGround, 0f);
                }else{
                	imgDownload.setVisibility(View.VISIBLE);
                   	holder.imgBackGround.setVisibility(View.VISIBLE);
                   	
                	
        			setAlphaForView(imgDownload, 0.5f);
        			setAlphaForView(imageBackGround, 0.2f);

                }
                
                convertView.setTag(holder);

                this.holderArray.add(holder);
                
            } else {
                holder = (Holder) convertView.getTag();
                holder.iconImageView.setTag(posRes);   
                if(selectedPos != position){
                	if(isBottomSelState)
                	{
                		holder.imgItemSelect.setVisibility(View.INVISIBLE);
                	}
                	else
                	{
                		holder.iconImageView.setShowBorder(false);
                	}
                	//holder.iconImageView.invalidate();
                }else{
                	mCurSelectedItem = holder.iconImageView;
                	if(isBottomSelState)
                	{
                		holder.imgItemSelect.setVisibility(View.VISIBLE);
                	}
                	else
                	{
	                	holder.iconImageView.setBorderColor(mSelectBorderColor);
	                	holder.iconImageView.setShowBorder(true);
                	}
                	//holder.iconImageView.invalidate();
                }
                
                holder.iconImageView.setImageBitmap(null);
                if(holder.iconBitmap != filterSrc && holder.iconBitmap != null && !holder.iconBitmap.isRecycled()){
                	holder.iconBitmap.recycle();
                }
            	holder.iconBitmap = null;

                ImageView imgDownload = holder.imgDownload;
                
                if(imgDownload != null){
                	if(isImageResInLocal){
                       	imgDownload.setVisibility(View.INVISIBLE);
                       	holder.imgBackGround.setVisibility(View.INVISIBLE);
                    }else{
                       	imgDownload.setVisibility(View.VISIBLE);
                       	holder.imgBackGround.setVisibility(View.VISIBLE);
                    }                      
                }
            }
            
         	if(posRes instanceof WBColorRes){
         		WBColorRes bgColorRes = (WBColorRes)posRes;
         		holder.iconImageView.setBackgroundColor(bgColorRes.getColorValue());
         		holder.iconBitmap = null;
        	}else {
        		
        		Bitmap oldBitmap = holder.iconBitmap;
	            Bitmap bmp = posRes.getIconBitmap();
	            
                if(oldBitmap != filterSrc && oldBitmap != null && !oldBitmap.isRecycled()){
    	            holder.iconImageView.setImageBitmap(null);
                	oldBitmap.recycle();
                	oldBitmap = null;
                }
                
	            if(posRes.getIsShowText()){
	            	if(posRes.getTextColor()!=0){
	            		holder.tx_text.setTextColor(posRes.getTextColor());
	            	}
				    holder.tx_text.setText(posRes.getShowText());
				}
				else{
					holder.tx_text.setText("");
				}
	            
	            if(posRes.getAsyncIcon()){
//	            	if(filterSrc != null && !filterSrc.isRecycled())
//	            	{
//		            	holder.iconImageView.setImageBitmap(null);
//	            		filterSrc.isRecycled();
//	            	}
//	            	filterSrc = null;
		            filterSrc = bmp;
	            	posRes.getAsyncIconBitmap(new WBAsyncPostIconListener() {
						
						@Override
						public void postIcon(Bitmap icon) {
							if(icon != null){
							if(posRes.isCircle()){
								Bitmap dst = getCircleBitmap(icon);
								if(dst!=null && !dst.isRecycled()){
									if(icon!=null && !icon.isRecycled())icon.recycle();
									icon = dst ;
									dst = null;
								}
							}
							holder.iconImageView.setImageBitmap(icon);
							holder.iconBitmap = icon;
							}
						}
					});
	            }else{
	            	if(posRes.isCircle()){
						Bitmap dst = getCircleBitmap(bmp);
						if(dst!=null && !dst.isRecycled()){
							if(bmp!=null && !bmp.isRecycled())bmp.recycle();
							bmp = dst ;
							dst = null;
						}
					}
		            holder.iconImageView.setImageBitmap(bmp); 
					holder.iconBitmap = bmp;
	            }
			}
               
         	
         	  if(isImageResInLocal){             
          		setAlphaForView(holder.imgDownload, 0f);
      			setAlphaForView(holder.imgBackGround, 0f);
              }else{          
      			setAlphaForView(holder.imgDownload, 0.5f);
      			setAlphaForView(holder.imgBackGround, 0.2f);
              }
         	  
            //String name = posRes.getName();

            posViewMap.put(Integer.valueOf(position), convertView);
        }catch(Exception ex){
        	ex.printStackTrace();
        }


        return convertView;
    }
    
    public void setSelectBorderColor(int selColor){
    	mSelectBorderColor = selColor;
    }
    
    public int getSelectBorderColor(){
    	return mSelectBorderColor;
    }
    
//    public Bitmap getCircleBitmap(Bitmap bmp,int width){
//    	if(bmp==null || bmp.isRecycled()){
//    		return null;
//    	}
//    	Bitmap dst = bmp.copy(Config.ARGB_8888, true);
//    	Paint paint = new Paint();
//    	paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
//    	Canvas canvas = new Canvas(bmp);
//    	float wd = ((float)width)/2.0f;
//    	canvas.drawCircle(wd, wd, wd, paint);
//    	return dst;
//    }
    
    private Bitmap getCircleBitmap(Bitmap bitmap) {  
    	if(bitmap==null || bitmap.isRecycled()){
    		return null;
    	}
    	Bitmap output = null;
    	try{
    		output = Bitmap.createBitmap(bitmap.getWidth(),  
            bitmap.getHeight(), Config.ARGB_8888);
	        Canvas canvas = null;
	        try{
	        	canvas = new Canvas(output);
	        }catch(Exception e){
	        	e.printStackTrace();
	        	if(output!=null && !output.isRecycled())output.recycle();
	        	output = bitmap.copy(Config.ARGB_8888, true);
	        	canvas = new Canvas(output);
	        }
	        final int color = 0xff424242; 
	        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());  
	        Paint paint = new Paint();
	        paint.setAntiAlias(true);  
	        canvas.drawARGB(0, 255, 255, 255);  
	        paint.setColor(color); 
	        int x = bitmap.getWidth(); 
	        
	        RectF rectf = new RectF(0+10, 0+10, bitmap.getWidth()-10, bitmap.getHeight()-10);
	        float radius = ((float)bitmap.getWidth())/2.0f - 20;
//	        canvas.drawRoundRect(rectf,radius,radius,paint);
	        canvas.drawArc(rectf, 0, 360, true, paint);
	        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));  
	        canvas.drawBitmap(bitmap, rect, rect, paint);
//	        canvas.save();
//	        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
	        Paint paint2 = new Paint();
	        paint2.setAntiAlias(true);  
			paint2.setStrokeWidth(3.0f);
	        paint2.setStyle(Paint.Style.STROKE);
			paint2.setColor(0xffffffff);
			radius = ((float) bitmap.getWidth()) / 2.0f;
			canvas.drawCircle(radius, radius, radius - 5, paint2);
    	}catch(Exception e){
    		e.printStackTrace();
    		output = null;
    	}
        return output;  
    }  
    
    
    public void setSelectPosition(int pos){
    	selectedPos = pos;
    	View sel_View = posViewMap.get(Integer.valueOf(pos));
    	if(sel_View != null)
    	{
	    	Holder hd = (Holder)sel_View.getTag();
	    	BorderImageView img_v = hd.iconImageView;
	    	if(img_v != mCurSelectedItem){
	    		if(mCurSelectedItem != null){
	    			if(isBottomSelState)
                	{
	    				if(mCurSelectedHolder != null)
	    					mCurSelectedHolder.imgItemSelect.setVisibility(View.INVISIBLE);
                	}
                	else
                	{
			    		mCurSelectedItem.setShowBorder(false);
			    		mCurSelectedItem.invalidate();
                	}
	    		}
	        	mCurSelectedItem = img_v;
	        	mCurSelectedHolder = hd;
	    	}
	    	if(mCurSelectedItem != null){
	    		mCurSelectedItem.setBorderColor(mSelectBorderColor);
	    		if(isBottomSelState)
            	{
	    			if(mCurSelectedHolder != null)
	    				mCurSelectedHolder.imgItemSelect.setVisibility(View.VISIBLE);
            	}
            	else
            	{
		    		mCurSelectedItem.setShowBorder(true);
		    		mCurSelectedItem.invalidate();
            	}
	    	}
    	}
    }    
    
    public void setViewInDownloading(int pos){
    	View v = posViewMap.get(pos);
    	if(v != null){
        	Holder h = (Holder)v.getTag();
        	ProgressBar bar = h.progressBar;
        	bar.setVisibility(View.VISIBLE);
        	
        	ImageView imgDownload = h.imgDownload;
        	if(imgDownload != null){
        		imgDownload.setVisibility(View.INVISIBLE);
        		h.imgBackGround.setVisibility(View.VISIBLE);
        		
          		setAlphaForView(imgDownload, 0f);
      			setAlphaForView(h.imgBackGround, 0f);
        	}
    	}
    }
    
    public void setViewInDownloadOk(int pos){
    	View v = posViewMap.get(pos);
    	if(v != null){
        	Holder h = (Holder)v.getTag();
        	ProgressBar bar = h.progressBar;
        	bar.setVisibility(View.INVISIBLE);
    		h.imgBackGround.setVisibility(View.INVISIBLE);
    	}
    }
    

	public void setViewInDownloadFail(int pos){
    	mHandler.obtainMessage(MSG_FAILURE,pos).sendToTarget();
    }
    
    private  Handler mHandler = new Handler() {
        public void handleMessage (Message msg) {
        	if(msg.what == MSG_SUCCESS){ 		
        		return;
        	}else if(msg.what == MSG_FAILURE){
            	int pos = Integer.parseInt(msg.obj.toString());
            	View v = posViewMap.get(pos);
            	if(v != null){
                	Holder h = (Holder)v.getTag();
                	ProgressBar bar = h.progressBar;
                	bar.setVisibility(View.INVISIBLE);
                   	h.imgBackGround.setVisibility(View.VISIBLE);
                   	h.imgDownload.setVisibility(View.VISIBLE);
    				Toast.makeText(mContext, "Download failed!", Toast.LENGTH_SHORT).show();
            	}
        		
        	}
        }
	};
    
//    private boolean isNewItem(WBRes posRes){
//    	if(!posRes.getIsNewValue()) return false;
//    	if(posRes.getManagerName() == null) return false;
//    	if(pkgName != null)
//    	{
//    		String ItemID = pkgName + posRes.getManagerName() + posRes.getName();
//    		return !checkExistById(ItemID);
//    	}
//    	return false;
//    }
    


//    public void add(ResRecordBean p) {  
//    	ContentValues values = new ContentValues();  
//        values.put(ResRecordBean.ID, p.getId());  
//        values.put(ResRecordBean.PACKAGENAME, p.getPackageName());  
//        values.put(ResRecordBean.MANAGERNAME, p.getManagerName());  
//        values.put(ResRecordBean.CLICKITEMNAME, p.getClickItemnName());
//        if(db != null)
//        	db.insert(SQLiteHelper.TB_NAME, null, values);  
//    }  
//    
//
//    public boolean checkExistById(String id) {
//    	if(db == null) return true; 
//        Cursor cursor = db.query(SQLiteHelper.TB_NAME, null, ResRecordBean.ID + "=?", new String[]{id}, null, null, null);  
//        if(cursor != null && cursor.moveToFirst()){  
//        	return true;
//        }  
//        return false;  
//    }  
//    
//    public void addResRecordItem(WBRes posRes,int pos)
//    {
//    	View v = posViewMap.get(pos);
//    	
//    	if(!isNewItem(posRes)) return;
//    	String ItemID = pkgName + posRes.getManagerName() + posRes.getName();
//    	ResRecordBean bean = new ResRecordBean();
//    	bean.setId(ItemID);
//    	bean.setPackageName(pkgName);
//    	bean.setManagerName(posRes.getManagerName());
//    	bean.setClickItemnName(posRes.getName());
//    	
//    	add(bean);
//    	if(v != null){
//        	Holder h = (Holder)v.getTag();
//           	h.imageNew.setVisibility(View.INVISIBLE);
//    	}
//    }
    
    
    public void dispose(){
		if (filterSrc != null && !filterSrc.isRecycled()) {
			filterSrc.recycle();
		}
		
		for (int i = 0; i < this.holderArray.size(); i++) {
			Holder holder = holderArray.get(i);
			holder.iconImageView.setImageBitmap(null);
			if (holder.iconBitmap != null && !holder.iconBitmap.isRecycled()) {
				holder.iconBitmap.recycle();
			}
			holder.iconBitmap = null;
		}
		
		//mHandler.
    	
    }
    
    
    
    private void setAlphaForView(View v, float alpha) {
    	AlphaAnimation animation = new AlphaAnimation(alpha, alpha);
    	animation.setDuration(0);
    	animation.setFillAfter(true);
    	v.startAnimation(animation);
    }
}
