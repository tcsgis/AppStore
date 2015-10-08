package com.aaa.util;

import android.app.Activity;
import android.util.DisplayMetrics;

public class DMUtil {
	
	//发廊、发型师照片
	private static final int FACE_PHOTO_W = 360;//都是dp
	private static final int FACE_PHOTO_H = 190;
	//顾客发布竞价图片
	private static final int BID_PHOTO_W = 230;
	private static final int BID_PHOTO_H = 320;
	//证书、作品、健康证照片
	private static final int ELSE_PHOTO_W = 100;
	private static final int ELSE_PHOTO_H = 120;
	//广告照片
	private static final int AD_PHOTO_W = 360;
	private static final int AD_PHOTO_H = 120;
	
	private static final int BASE = 720;
	
	private static int mWidthPixels = -1;
	private static int mHeightPixels = -1;
	private static int photo_width = -1;
	private static int photo_height = -1;
	private static int bid_photo_width = -1;
	private static int bid_photo_height = -1;
	private static int ad_photo_width = -1;
	private static int ad_photo_height = -1;
	private static int else_photo_width = -1;
	private static int else_photo_height = -1;
	private static float density = -1;
	
	public static int adjust(Activity activity, int length) {
		return getWindowWidth(activity) * length / BASE;
	}
	
	public static int getWindowWidth(Activity activity){
		if(mWidthPixels < 0){
			DisplayMetrics dm = new DisplayMetrics();
			activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
			mWidthPixels = dm.widthPixels;
			mHeightPixels = dm.heightPixels;
		}
		return mWidthPixels;
	}
	
	public static int getWindowHeight(Activity activity){
		if(mHeightPixels < 0){
			DisplayMetrics dm = new DisplayMetrics();
			activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
			mWidthPixels = dm.widthPixels;
			mHeightPixels = dm.heightPixels;
		}
		return mHeightPixels;
	}
	
	public static int getFacePhotoWidth(Activity activity){
		return photo_width < 0 ? (int) (FACE_PHOTO_W * getDensity(activity)) : photo_width;
	}
	
	public static int getFacePhotoHeight(Activity activity){
		return photo_height < 0 ? (int) (FACE_PHOTO_H * getDensity(activity)) : photo_height;
	}
	
	public static int getBidPhotoWidth(Activity activity){
		return bid_photo_width < 0 ? (int) (BID_PHOTO_W * getDensity(activity)) : bid_photo_width;
	}
	
	public static int getBidPhotoHeight(Activity activity){
		return bid_photo_height < 0 ? (int) (BID_PHOTO_H * getDensity(activity)) : bid_photo_height;
	}
	
	public static int getAdPhotoWidth(Activity activity){
		return ad_photo_width < 0 ? (int) (AD_PHOTO_W * getDensity(activity)) : ad_photo_width;
	}
	
	public static int getAdPhotoHeight(Activity activity){
		return ad_photo_height < 0 ? (int) (AD_PHOTO_H * getDensity(activity)) : ad_photo_height;
	}
	
	public static int getElsePhotoWidth(Activity activity){
		return else_photo_width < 0 ? (int) (ELSE_PHOTO_W * getDensity(activity)) : else_photo_width;
	}
	
	public static int getElsePhotoHeight(Activity activity){
		return else_photo_height < 0 ? (int) (ELSE_PHOTO_H * getDensity(activity)) : else_photo_height;
	}
	
	public static int getHeight(Activity activity, int dp){
		return (int) (dp * getDensity(activity));
	}
	
	private static float getDensity(Activity activity){
		if(density < 0){
			DisplayMetrics dm = new DisplayMetrics();
			activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
			density = dm.density;
		}
		return density;
	}
}