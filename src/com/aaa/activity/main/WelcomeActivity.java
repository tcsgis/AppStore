package com.aaa.activity.main;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import com.changhong.activity.BaseActivity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import com.aaa.activity.main.MainActivity;
import com.aaa.db.AppDownloadState;
import com.aaa.db.DownloadState;
import com.aaa.util.Constant;
import com.changhong.util.config.CHIConfig;
import com.changhong.util.db.bean.CacheManager;
import com.changhong.util.download.DownloadManager;

public class WelcomeActivity extends BaseActivity{

//	private ISalonAccountService accountService = (ISalonAccountService) CHCareWebApiProvider.Self
//			.defaultInstance().getDefaultWebApiService(
//					WebApiServerType.SALON_ACCOUNT_SERVER);
	
	@Override
	protected void onAfterOnCreate(Bundle savedInstanceState) {
		super.onAfterOnCreate(savedInstanceState);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
		doGetLocalDatas();
		
		CHIConfig config = this.getCHApplication().getPreferenceConfig();
		String phone = config.getString(Constant.USERNAME, "");
		
		if(phone.length() == 11){
			toLogin(phone);
		}else{
			toMainActivity();
		}
	}
	
	private void doGetLocalDatas() {
		File saveDir = new File(DownloadManager.getDownloadManager().getRootPath());
		try {
			File[] files = saveDir.listFiles();
			File file;
			String name;
			FileInputStream fis = null; 
			int size;
			String savePath;
			String suffix;
			
			for(int i = 0; i < files.length; i++){
				file = files[i];
				name = file.getName();
				suffix = name.substring(name.lastIndexOf('.'));
				
				if(Constant.TEMP_SUFFIX.equals(suffix)){
					//未下载完
					savePath = saveDir.getPath() + File.separator + name.substring(0, name.lastIndexOf('.'));
					fis = new FileInputStream(file); 
					size = fis.available(); 
					AppDownloadState ads = new AppDownloadState();
					ads.setSavePath(savePath);
					ads.setProgress(size);
					ads.setDownloadState(DownloadState.PAUSE);
					CacheManager.INSTANCE.getDownload().add(ads);
				}else{
					//已下载完
					savePath = saveDir.getPath() + File.separator + name;
					AppDownloadState ads = new AppDownloadState();
					ads.setSavePath(savePath);
					ads.setDownloadState(hasInstall(ads) ? DownloadState.INSTALLED | DownloadState.DOWNLOADED : DownloadState.DOWNLOADED);
					CacheManager.INSTANCE.getDownload().add(ads);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private boolean hasInstall(AppDownloadState ads){
		PackageManager pm = this.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(ads.getSavePath(), PackageManager.GET_ACTIVITIES);
        ApplicationInfo appInfo = null;
        if (info != null) {
            appInfo = info.applicationInfo;
            String packageName = appInfo.packageName;
            // 获取所有已安装程序的包信息
            List<PackageInfo> pinfo = pm.getInstalledPackages(0);
            for ( int i = 0; i < pinfo.size(); i++ ) {
            	if(pinfo.get(i).packageName.equalsIgnoreCase(packageName))
            		return true;
            }
        }
        return false;
	}

	private void doLogin(String phone) {
//		accountService.login(phone, mima, new AsyncResponseCompletedHandler<String>() {
//			
//			@Override
//			public String doCompleted(ResponseBean<?> response,
//					ChCareWepApiServiceType servieType) {
//				if(response.getState() >= 0){
//					SalonUser user = (SalonUser) response.getData();
//					CacheManager.INSTANCE.setCurrentUser(user);
//				}
//				
//				Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
//				startActivity(intent);
//				return null;
//			}
//		});
	}
	
	private Handler handler = new Handler( );
	
	private void toLogin(final String phone){
		Runnable runnable = new Runnable()
		{
			public void run()
			{
				doLogin(phone);
			}
		};
		
		handler.postDelayed(runnable, 1000);
	}
	
	private void toMainActivity(){
		Runnable runnable = new Runnable()
		{
			public void run()
			{
				Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		};
		
		handler.postDelayed(runnable, 2000);
	}
}

