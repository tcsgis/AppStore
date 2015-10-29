package com.aaa.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cn.changhong.chcare.core.webapi.util.TokenManager;

import com.aaa.activity.login.LoginActivity;
import com.aaa.db.AppDetail;
import com.aaa.db.AppDownloadState;
import com.aaa.db.DownloadState;
import com.changhong.CHActivity;
import com.changhong.CHApplication;
import com.changhong.util.bitmap.CHBitmapCacheWork;
import com.changhong.util.bitmap.CHBitmapCallBackHanlder;
import com.changhong.util.bitmap.CHDownloadBitmapHandler;
import com.changhong.util.config.CHIConfig;
import com.changhong.util.db.bean.CacheManager;
import com.changhong.util.download.DownloadManager;
import com.google.gson.JsonElement;
import com.llw.AppStore.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MyTools {
	
	private final static String TAG = "MyTools";
	private static final String PHOTO_DIVIDER = "\\|";
	private static final String PHOTO_SPLIT = "|";
	
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
	
	public static boolean toLogin(Activity activity, AppDownloadState ads){
		if(activity == null) return false;
		
		if(CacheManager.INSTANCE.getCurrentUser() == null || CacheManager.INSTANCE.getCurrentUser().getID() == 0){
			Intent i = new Intent(activity, LoginActivity.class);
			Bundle b = new Bundle();
			b.putSerializable(Constant.APP_DOWNLOAD_STATE, ads);
			i.putExtras(b);
			Toast.makeText(activity, R.string.login_string7, Toast.LENGTH_SHORT).show();
			activity.startActivityForResult(i, Constant.REQUEST_LOGIN);
			return true;
		}
		return false;
	}

	public static boolean startDownload(AppDownloadState ads) {
		if(ads != null && ads.getDownloadUrl() != null){
			//开始下载
			DownloadManager.getDownloadManager().addHandler(ads.getDownloadUrl());
			
			AppDownloadState ads2 = ads.clone();
			ads2.setDownloadState(DownloadState.DOWNLOADING);
			//更新下载界面列表数据
			CacheManager.INSTANCE.getDownload().add(ads2);
			//更新主页显示列表数据
			try {
				CacheManager.INSTANCE.putAppData(ads2);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}
	
	public static void pauseDownload(AppDownloadState ads){
		if(ads != null && ads.getDownloadUrl() != null){
			DownloadManager.getDownloadManager().pauseHandler(ads.getDownloadUrl());
			
			AppDownloadState ads2 = ads.clone();
			ads2.setDownloadState(DownloadState.PAUSE);
			//更新下载界面列表数据
			for(AppDownloadState item : CacheManager.INSTANCE.getDownload()){
				if(item.getDownloadUrl().equals(ads.getDownloadUrl())){
					item.setDownloadState(DownloadState.PAUSE);
					break;
				}
			}
			//更新主页显示列表数据
			try {
				CacheManager.INSTANCE.putAppData(ads2);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void cancelDownload(AppDownloadState ads){
		if(ads != null && ads.getDownloadUrl() != null){
			DownloadManager.getDownloadManager().deleteHandler(ads.getDownloadUrl());
			
			AppDownloadState ads2 = ads.clone();
			ads2.setDownloadState(DownloadState.NONE);
			//更新下载界面列表数据
			for(AppDownloadState item : CacheManager.INSTANCE.getDownload()){
				if(item.getDownloadUrl().equals(ads.getDownloadUrl())){
					CacheManager.INSTANCE.getDownload().remove(item);
					break;
				}
			}
			//更新主页显示列表数据
			try {
				CacheManager.INSTANCE.putAppData(ads2);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	

	public static void continueDownload(AppDownloadState ads) {
		if(ads != null && ads.getDownloadUrl() != null){
			DownloadManager.getDownloadManager().continueHandler(ads.getDownloadUrl());
			
			AppDownloadState ads2 = ads.clone();
			ads2.setDownloadState(DownloadState.DOWNLOADING);
			//更新下载界面列表数据
			for(AppDownloadState item : CacheManager.INSTANCE.getDownload()){
				if(item.getDownloadUrl().equals(ads.getDownloadUrl())){
					item.setDownloadState(DownloadState.DOWNLOADING);
					break;
				}
			}
			//更新主页显示列表数据
			try {
				CacheManager.INSTANCE.putAppData(ads2);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static AppDownloadState initAppDownloadState(Context c, AppDownloadState ads) {
		//先初始化本地数据库保存的
		AppDownloadState item;
		AppDownloadState app = ads.clone();
		app.setDownloadState(DownloadState.NONE);
		
		boolean hasApkFile = false;
		for(int i = 0; i < CacheManager.INSTANCE.getDownload().size(); i++){
			item = CacheManager.INSTANCE.getDownload().get(i);
			
			if(item.getSavePath().equals(ads.getSavePath())){
				float percent = item.getProgress() * 100 / 1024 / 1024 / ads.getSize();
				//主页面item
				app.setDownloadState(item.getDownloadState());
				app.setProgress(item.getProgress());
				app.setPercent(percent);
				//下载页面item
				CacheManager.INSTANCE.getDownload().remove(i);
				CacheManager.INSTANCE.getDownload().add(i, app.clone());
				hasApkFile = true;
				break;
			}
		}
		
		//是否已安装，但删除了安装包
		if(! hasApkFile){
			PackageManager pm = c.getPackageManager();
			List<PackageInfo> pinfo = pm.getInstalledPackages(0);
			
			for ( int j = 0; j < pinfo.size(); j++ ) {
				if(pinfo.get(j).packageName.equalsIgnoreCase(ads.getPackageName())){
					app.setDownloadState(DownloadState.INSTALLED);
					break;
				}
			}
		}
		
		return app;
	}

	public static void refreshLoadingState(String url, long totalSize, long currentSize, long speed) {
		String key = null;
		float percent = (float)currentSize * 100 / totalSize;
		for(AppDownloadState item : CacheManager.INSTANCE.getDownload()){
			if(url.equals(item.getDownloadUrl())){
				item.setSpeed((int)speed);
				item.setPercent(percent);
				item.setProgress(currentSize);
				key = item.getTag();
				break;
			}
		}
		
		for(AppDownloadState item : CacheManager.INSTANCE.getAppData(key)){
			if(url.equals(item.getDownloadUrl())){
				item.setSpeed((int)speed);
				item.setPercent(percent);
				item.setProgress(currentSize);
				break;
			}
		}
	}
	
	public static void refreshLoadedState(String url){
		String key = null;
		for(AppDownloadState item : CacheManager.INSTANCE.getDownload()){
			if(url.equals(item.getDownloadUrl())){
				item.setSpeed(0);
				item.setPercent(100);
				item.setProgress(0);
				item.setDownloadState(DownloadState.DOWNLOADED);
				key = item.getTag();
				break;
			}
		}
		
		for(AppDownloadState item : CacheManager.INSTANCE.getAppData(key)){
			if(url.equals(item.getDownloadUrl())){
				item.setSpeed(0);
				item.setPercent(100);
				item.setProgress(0);
				item.setDownloadState(DownloadState.DOWNLOADED);
				break;
			}
		}
	}

	public static String getTag(int type) {
		try {
			return Constant.MAIN_VIEW_SLIDE_TITLES[type];
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void refreshFailureState(String url) {
		String key = null;
		AppDownloadState item;
		for(int i = 0; i < CacheManager.INSTANCE.getDownload().size(); i++){
			item = CacheManager.INSTANCE.getDownload().get(i);
			if(url.equals(item.getDownloadUrl())){
				key = item.getTag();
				CacheManager.INSTANCE.getDownload().remove(i);
				break;
			}
		}
		
		for(int i = 0; i < CacheManager.INSTANCE.getAppData(key).size(); i++){
			item = CacheManager.INSTANCE.getAppData(key).get(i);
			if(url.equals(item.getDownloadUrl())){
				item.setSpeed(0);
				item.setPercent(0);
				item.setProgress(0);
				item.setDownloadState(DownloadState.NONE);
				
				try {
					File file = new File(item.getSavePath() + Constant.TEMP_SUFFIX);
					if(file != null && file.isFile() && file.exists()){
						file.delete();
					}else{
						file = new File(item.getSavePath());
						if(file != null && file.isFile() && file.exists()){
							file.delete();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
		}
		
	}

	public static void toastLoadFailure(Context c) {
		Toast.makeText(c, R.string.toast1, Toast.LENGTH_SHORT).show();
	}
	
	public static void installApk(Context c, AppDownloadState data){
		try {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			// 执行的数据类型Uri.fromFile(new File(mUrl)
			intent.setDataAndType(Uri.fromFile(new File(data.getSavePath())), "application/vnd.android.package-archive");
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//防止安装中退出
			c.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void refreshInstallState(String packageName){
		byte newState = (byte) (DownloadState.INSTALLED | DownloadState.DOWNLOADED);
		
		Collection<ArrayList<AppDownloadState>> values = CacheManager.INSTANCE.getAllAppData().values();
		for(ArrayList<AppDownloadState> list : values){
			boolean find = false;
			for(int i = 0; i < list.size(); i++){
				if(packageName.equals(list.get(i).getPackageName())){
					list.get(i).setDownloadState(newState);
					find = true;
					break;
				}
			}
			if(find)
				break;
		}
		
		AppDownloadState item;
		for(int i = 0; i < CacheManager.INSTANCE.getDownload().size(); i++){
			item = CacheManager.INSTANCE.getDownload().get(i);
			if(packageName.equals(item.getPackageName())){
				item.setDownloadState(newState);
				break;
			}
		}
	}
	
	public static void openAnotherApp(Context c, AppDownloadState data){
		try{
			Intent intent = c.getPackageManager().getLaunchIntentForPackage(data.getPackageName());
			c.startActivity(intent);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static boolean hasInstall(Context c, String packageName){
		PackageManager pm = c.getPackageManager();
        // 获取所有已安装程序的包信息
		List<PackageInfo> pinfo = pm.getInstalledPackages(0);
		for ( int i = 0; i < pinfo.size(); i++ ) {
			if(pinfo.get(i).packageName.equalsIgnoreCase(packageName))
				return true;
		}
        return false;
	}

	public static void removeDownloadedFile(AppDownloadState data) {
		try {
			File file = new File(data.getSavePath());
			if(file != null && file.isFile() && file.exists()){
				file.delete();
			}else{
				file = new File(data.getSavePath() + Constant.TEMP_SUFFIX);
				if(file != null && file.isFile() && file.exists()){
					file.delete();
				}
			}
			
			byte newState = DownloadState.NONE;
			byte oldState = data.getDownloadState();
			if(oldState == DownloadState.DOWNLOADED){
				newState = DownloadState.NONE;
			}
			else if(oldState == (DownloadState.DOWNLOADED | DownloadState.INSTALLED)){
				newState = DownloadState.INSTALLED;
			}
			
			Collection<ArrayList<AppDownloadState>> values = CacheManager.INSTANCE.getAllAppData().values();
			for(ArrayList<AppDownloadState> list : values){
				boolean find = false;
				for(int i = 0; i < list.size(); i++){
					if(data.getID() == list.get(i).getID()){
						list.get(i).setDownloadState(newState);
						find = true;
						break;
					}
				}
				if(find)
					break;
			}
			
			AppDownloadState item;
			for(int i = 0; i < CacheManager.INSTANCE.getDownload().size(); i++){
				item = CacheManager.INSTANCE.getDownload().get(i);
				if(data.getID() == item.getID()){
					CacheManager.INSTANCE.getDownload().remove(i);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void removeAppDetail(AppDetail app){
		AppDownloadState data = new AppDownloadState(app);
		for(AppDownloadState item : CacheManager.INSTANCE.getAppData(data.getTag())){
			if(item.getID() == data.getID()){
				CacheManager.INSTANCE.getAppData(data.getTag()).remove(item);
				break;
			}
		}
	}
	
	public static ArrayList<String> splitPhoto(JsonElement je){
		ArrayList<String> photos = new ArrayList<String>();
		if(je != null){
			String[] ss = je.getAsString().split(PHOTO_DIVIDER);
			for(int i = 0; i < ss.length; i++){
				photos.add(ss[i]);
			}
		}
		return photos;
	}
	
	public static ArrayList<String> splitPhoto(String s){
		ArrayList<String> photos = new ArrayList<String>();
		if(s != null){
			String[] ss = s.split(PHOTO_DIVIDER);
			for(int i = 0; i < ss.length; i++){
				photos.add(ss[i]);
			}
		}
		return photos;
	}
	
	public static String composePhotos(ArrayList<String> photos){
		String s = "";
		if(photos != null){
			for(int i = 0; i < photos.size(); i++){
				if(i == 0){
					s += photos.get(i);
				}else{
					s += PHOTO_SPLIT + photos.get(i);
				}
			}
		}
		return s;
	}

	public static void setToken(CHActivity c) {
		CHIConfig config = c.getCHApplication().getPreferenceConfig();
		String token = config.getString(Constant.TOKEN, "");
		String fileToken = config.getString(Constant.FILE_TOKEN, "");
		TokenManager.setToken(token);
		TokenManager.setFiletoken(fileToken);
	}
	
	public static void saveToken(CHActivity c){
		CHIConfig config = c.getCHApplication().getPreferenceConfig();
		config.setString(Constant.TOKEN, TokenManager.getToken());
		config.setString(Constant.FILE_TOKEN, TokenManager.getFiletoken());
	}
	
	public static void clearToken(CHActivity c){
		CHIConfig config = c.getCHApplication().getPreferenceConfig();
		config.setString(Constant.TOKEN, "");
		config.setString(Constant.FILE_TOKEN, "");
	}
	
	public static boolean editNotNull(EditText edit){
		if(edit.getText() != null && edit.getText().toString() != null
				&& ! edit.getText().toString().trim().equals("")){
			return true;
		}
		return false;
	}

	public static AppDetail toAppDetail(AppDownloadState data) {
		AppDetail app = new AppDetail();
		app.setID(data.getID());
		app.setName(data.getName());
		app.setLogoUrl(data.getLogoUrl());
		app.setTag(data.getRealTag());
		app.setOrder(data.getOrder());
		app.setType(data.getType());
		app.setSize(data.getSize());
		app.setDesc(data.getDesc());
		app.setPackageName(data.getPackageName());
		app.setDeveloper(data.getDeveloper());
		app.setVersion(data.getVersion());
		app.setDownloadUrl(data.getDownloadUrl());
		app.setDescUrl(data.getDescUrl());
		app.setOwner(data.getOwner());
		app.setDownCount(data.getDownCount());
		app.setUploadTime(data.getUploadTime());
		return app;
	}

	public static void openWeb(Activity c, String downloadUrl) {
		try {
			final Uri uri = Uri.parse(downloadUrl);          
			final Intent it = new Intent(Intent.ACTION_VIEW, uri);          
			c.startActivity(it);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
