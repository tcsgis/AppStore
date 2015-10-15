package com.aaa.util;

import com.changhong.CHApplication;
import com.changhong.util.bitmap.CHBitmapCacheWork;
import com.changhong.util.bitmap.CHBitmapCallBackHanlder;
import com.changhong.util.bitmap.CHDownloadBitmapHandler;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MyTools {
	
	public static String float2String(float value) {
		String s = String.valueOf(value);
		String ret = s;
		int index = s.indexOf('.');
		if (index > 0 && index + 1 + Constant.DECIMAL_COUNT <= s.length()) {
			ret = s.substring(0, index + 1 + Constant.DECIMAL_COUNT);
		}
		return ret;
	}
	
	public static String getText(View v){
		String result = "";
		if(v != null){
			if(EditText.class.isInstance(v)){
				result = ((EditText)v).getText().toString().trim(); 
			}
			else if(TextView.class.isInstance(v)){
				result = ((TextView)v).getText().toString().trim();
			}
		}
		return result;
	}
	
	public static boolean validPhone(View v){
		if(getText(v).length() == 11){
			return true;
		}
		return false;
	}
	
	public synchronized static CHBitmapCacheWork getImageFetcher(Context contxt, CHApplication app, boolean isRound, int defaultImg,
			int rwidth, int rheight){
		CHBitmapCacheWork imageFetcher = new CHBitmapCacheWork(contxt);
		
		CHBitmapCallBackHanlder taBitmapCallBackHanlder = new CHBitmapCallBackHanlder();
		if(defaultImg > 0){
			taBitmapCallBackHanlder.setLoadingImage(contxt, defaultImg);
		}
		taBitmapCallBackHanlder.setCircleParams(isRound);
		
		Bitmap loading = taBitmapCallBackHanlder.getmLoadingBitmap();
		if(loading != null){
			int width = taBitmapCallBackHanlder.getmLoadingBitmap().getWidth();
			int height = taBitmapCallBackHanlder.getmLoadingBitmap().getHeight();
			CHDownloadBitmapHandler downloadBitmapFetcher = new CHDownloadBitmapHandler(
					contxt, width, height);
			
			imageFetcher.setProcessDataHandler(downloadBitmapFetcher);
		}else{
			CHDownloadBitmapHandler downloadBitmapFetcher = new CHDownloadBitmapHandler(
					contxt, rwidth, rheight);
			
			imageFetcher.setProcessDataHandler(downloadBitmapFetcher);
		}
		
		imageFetcher.setCallBackHandler(taBitmapCallBackHanlder);
		imageFetcher.setFileCache(app.getFileCache());		
		return imageFetcher;
	}
}
