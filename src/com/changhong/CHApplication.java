/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.changhong;

import java.lang.Thread.UncaughtExceptionHandler;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import android.app.Application;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import cn.changhong.chcare.core.webapi.util.ConnFactory;

import com.changhong.common.AndroidVersionCheckUtils;
import com.changhong.common.AsyncTask;
import com.changhong.exception.CHNoSuchCommandException;
import com.changhong.util.CHInjector;
import com.changhong.util.CHLogger;
import com.changhong.util.cache.CHFileCache;
import com.changhong.util.cache.CHFileCache.CHCacheParams;
import com.changhong.util.config.CHIConfig;
import com.changhong.util.config.CHPreferenceConfig;
import com.changhong.util.config.CHPropertiesConfig;
import com.changhong.util.config.MyProperties;
import com.changhong.util.db.SQLiteDB.TADBParams;
import com.changhong.util.db.SQLiteDBPool;
import com.changhong.util.layoutloader.ILayoutLoader;
import com.changhong.util.layoutloader.LayoutLoader;
import com.changhong.util.log.PrintToFileLogger;
import com.changhong.util.netstate.NetChangeObserver;
import com.changhong.util.netstate.NetWorkUtil.netType;
import com.changhong.util.netstate.NetworkStateReceiver;

public class CHApplication extends Application
{
	/** 配置器 为Preference */
	public final static int PREFERENCECONFIG = 0;
	/** 配置器 为PROPERTIESCONFIG */
	public final static int PROPERTIESCONFIG = 1;

	private final static String TAIDENTITYCOMMAND = "TAIdentityCommand";
	/** 配置器 */
	private CHIConfig mCurrentConfig;
	/** 获取布局文件ID加载器 */
	private ILayoutLoader mLayoutLoader;
	/** 加载类注入器 */
	private CHInjector mInjector;
	private static CHApplication application;
	private CHActivity currentActivity;
	private final HashMap<String, Class<? extends CHActivity>> registeredActivities = new HashMap<String, Class<? extends CHActivity>>();
	/** ThinkAndroid 文件缓存 */
	private CHFileCache mFileCache;
	/** ThinkAndroid数据库链接池 */
	private SQLiteDBPool mSQLiteDatabasePool;
	/** ThinkAndroid 应用程序运行Activity管理器 */
	private CHAppManager mAppManager;
	private boolean networkAvailable = false;
	private static final String SYSTEMCACHE = "chfamily";
	private NetChangeObserver taNetChangeObserver;

	@Override
	public void onCreate()
	{
		// TODO Auto-generated method stub
            onPreCreateApplication();
		super.onCreate();
		ConnFactory.Init(this.getApplicationContext());
			doOncreate();
			onAfterCreateApplication();
			getAppManager();
            //初始化聊天服务
		AsyncTask.init();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

	private void doOncreate()
	{
		// TODO Auto-generated method stub
		application = this;
		// 注册App异常崩溃处理器
		taNetChangeObserver = new NetChangeObserver()
		{
			@Override
			public void onConnect(netType type)
			{
				// TODO Auto-generated method stub
				super.onConnect(type);
				CHApplication.this.onConnect(type);
			}

			@Override
			public void onDisConnect()
			{
				// TODO Auto-generated method stub
				super.onDisConnect();
				CHApplication.this.onDisConnect();

			}
		};
		NetworkStateReceiver.registerObserver(taNetChangeObserver);
		// 注册activity启动控制控制器
		registerAllActivity2();
	}

	/**
	 * 当前没有网络连接
	 */
	public void onDisConnect()
	{
		networkAvailable = false;
		if (currentActivity != null)
		{
			currentActivity.onDisConnect();
		}
	}

	/**
	 * 网络连接连接时调用
	 */
	protected void onConnect(netType type)
	{
		// TODO Auto-generated method stub
		networkAvailable = true;
		if (currentActivity != null)
		{
			currentActivity.onConnect(type);
		}
	}

	/**
	 * 获取Application
	 * 
	 * @return
	 */
	public static CHApplication getApplication()
	{
		return application;
	}

	public CHActivity getCurrentActivity()
	{
		return currentActivity;
	}
	
	protected void onAfterCreateApplication()
	{
		getPropertiesConfig();
	}

	protected void onPreCreateApplication()
	{
		CHLogger.e("onPreCreateApplication", "======onPreCreateApplication=====");
	}

	public CHIConfig getPreferenceConfig()
	{
		return getConfig(PREFERENCECONFIG);
	}

	//cxp test
	private CHIConfig getPropertiesConfig()
	{
		return getConfig(PROPERTIESCONFIG);
	}

	public CHIConfig getConfig(int confingType)
	{
		if (confingType == PREFERENCECONFIG)
		{
			mCurrentConfig = CHPreferenceConfig.getPreferenceConfig(this);

		} else if (confingType == PROPERTIESCONFIG)
		{
			mCurrentConfig = CHPropertiesConfig.getPropertiesConfig(this);
		} else
		{
			mCurrentConfig = CHPreferenceConfig.getPreferenceConfig(this);
			//cxp test			mCurrentConfig = CHPropertiesConfig.getPropertiesConfig(this);
		}
		if (!mCurrentConfig.isLoadConfig())
		{
			mCurrentConfig.loadConfig();
		}
		return mCurrentConfig;
	}

	public CHIConfig getCurrentConfig()
	{
		if (mCurrentConfig == null)
		{
			getPreferenceConfig();
		}
		return mCurrentConfig;
	}

	public ILayoutLoader getLayoutLoader()
	{
		if (mLayoutLoader == null)
		{
			mLayoutLoader = LayoutLoader.getInstance(this);
		}
		return mLayoutLoader;
	}

	public void setLayoutLoader(ILayoutLoader layoutLoader)
	{
		this.mLayoutLoader = layoutLoader;
	}


	public CHInjector getInjector()
	{
		if (mInjector == null)
		{
			mInjector = CHInjector.getInstance();
		}
		return mInjector;
	}

	public void setInjector(CHInjector injector)
	{
		this.mInjector = injector;
	}

	public void onActivityCreating(CHActivity activity)
	{
	}

	public void onActivityDestroyed(CHActivity activity)
	{
		activity.closeDb();
	}


	public void registerActivity(int resID, Class<? extends CHActivity> clz)
	{
		String activityKey = getString(resID);
		registerActivity(activityKey, clz);
	}

	public void registerActivity(String activityKey,
			Class<? extends CHActivity> clz)
	{
		if (registeredActivities.containsKey(activityKey))
			CHLogger.w(this, "Already an activity with the same key " + activityKey + ": " + registeredActivities.get(activityKey) + "->" + clz);
		registeredActivities.put(activityKey, clz);
	}

	public void unregisterActivity(int resID)
	{
		String activityKey = getString(resID);
		unregisterActivity(activityKey);
	}

	public void unregisterActivity(String activityKey)
	{
		registeredActivities.remove(activityKey);
	}

	public CHFileCache getFileCache()
	{
		if (mFileCache == null)
		{
			CHCacheParams cacheParams = new CHCacheParams(this, SYSTEMCACHE);
			CHFileCache fileCache = new CHFileCache(cacheParams);
			application.setFileCache(fileCache);

		}
		return mFileCache;
	}

	public void setFileCache(CHFileCache fileCache)
	{
		this.mFileCache = fileCache;
	}


	public SQLiteDBPool getSQLiteDatabasePool()
	{
		if (mSQLiteDatabasePool == null)
		{
			//4版本以上的要处理字段
			//5~6消息更新
			//7 User增加Exp字段, 家庭纪念日
			//8 家庭日记
			//9 历史遗留的UID为0的数据
			TADBParams params = new TADBParams();
			params.setDbVersion(9);

			mSQLiteDatabasePool = SQLiteDBPool.getInstance(this, params, false);
			mSQLiteDatabasePool.setOnDbUpdateListener(null);
			mSQLiteDatabasePool.createPool();
		}
		return mSQLiteDatabasePool;
	}

	public void setSQLiteDatabasePool(SQLiteDBPool sqliteDatabasePool)
	{
		this.mSQLiteDatabasePool = sqliteDatabasePool;
	}

	public CHAppManager getAppManager()
	{
		if (mAppManager == null)
		{
			mAppManager = CHAppManager.getAppManager();
		}
		return mAppManager;
	}

	public void registerAllActivity2(){

//		Intent intent = new Intent(Intent.ACTION_MAIN, null);
//		intent.setPackage(this.getPackageName());

		PackageManager pm = getPackageManager();
		try {
//			final Resources nonRes = new Resources(getResources().getAssets(), null,null);
			PackageInfo pi = pm.getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES);
			for (ActivityInfo info : pi.activities) {
//				Log.w("XXX", info.nonLocalizedLabel + "/" + nonRes.getString(info.labelRes));
				
				String label = info.name;
				final int index = label.lastIndexOf('.');
				if (index != -1) {
					label = label.substring(index + 1);
				}

				try {
					Class<?> thisClzz = Class.forName(info.name);
					if(thisClzz !=null && CHActivity.class.isAssignableFrom(thisClzz)){
						registerActivity(label, (Class<CHActivity>)thisClzz);
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		} catch (PackageManager.NameNotFoundException e) {
		}

	}
	/**
	 * 注册APP中所有的activity
	 */
	public void registerAllActivity(){

		Intent intent = new Intent(Intent.ACTION_MAIN, null);
		intent.setPackage(this.getPackageName());
		PackageManager pm = getPackageManager();
		List<ResolveInfo> list = pm.queryIntentActivities(intent, 0);

		for (ResolveInfo info : list) {

			String label = info.activityInfo.name;
			final int index = label.lastIndexOf('.');
			//TODO:第一次没有设置到返回值上, 是个bug吗
			if (index != -1) {
				label = label.substring(index + 1);
			}
			try {
				Class<?> thisClzz = Class.forName(info.activityInfo.name);
				if(thisClzz !=null && CHActivity.class.isAssignableFrom(thisClzz)){
					registerActivity(label, (Class<CHActivity>)thisClzz);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	/**
	 * 退出应用程序
	 * 
	 * @param isBackground
	 *            是否开开启后台运行,如果为true则为后台运行
	 */
	public void exitApp(boolean isBackground)
	{
		if(!isBackground){
		}
		mAppManager.AppExit(this, isBackground);
	}

	/**
	 * 退出所有activity并显示登录界面并显示2:main
	 * 
	 */
	public void toMainActivity()
	{
		mAppManager.toMainActivity(this);
	}

	/**
	 * 获取当前网络状态，true为网络连接成功，否则网络连接失败
	 * 
	 * @return
	 */
	public boolean isNetworkAvailable()
	{
		return networkAvailable;
	}
	
	public void closeSQLiteDatabase()
	{
		if (mSQLiteDatabasePool != null)
		{
			this.mSQLiteDatabasePool.closeSQLiteDatabase();
			this.mSQLiteDatabasePool = null;
		}
	}
}
