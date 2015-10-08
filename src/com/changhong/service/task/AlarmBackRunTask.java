package com.changhong.service.task;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.os.Bundle;

import com.changhong.CHApplication;
import com.changhong.util.CHLogger;
import com.changhong.util.db.SQLiteDB;

public abstract class AlarmBackRunTask implements AlarmOnObserver{

	private AtomicLong lastTime = new AtomicLong(0);
	private SQLiteDB sqlitdb;

	public Bundle onTimeOut(Date date){
		Bundle ret = null;
		try {
			
			boolean isForegroud = isAppForegroud();
			Long thisTime = System.currentTimeMillis();
			if(thisTime - lastTime.get() >= getRunInterval(isForegroud)){

				lastTime = new AtomicLong(thisTime);
				ret = doTask(date);//前台运行时不用notify

				releaseSqlitdb();
			}
		} catch (Exception e) {
			// TODO: handle exception
			CHLogger.e(this, e.getMessage());
		} finally{
			releaseSqlitdb();
		}

		return ret;
	}

	/**
	 * 是否前台运行
	 * @return
	 */
	protected boolean isAppForegroud() {

		try {
			if(CHApplication.getApplication() != null) {
				String appName = CHApplication.getApplication().getPackageName();
				ActivityManager activityManager = (ActivityManager) CHApplication.getApplication().getSystemService(Context.ACTIVITY_SERVICE);
				List<RunningAppProcessInfo> appProcesses = activityManager
						.getRunningAppProcesses();
				if(appProcesses != null){
					for (RunningAppProcessInfo appProcess : appProcesses) {
						if (appProcess!= null && appName.equals(appProcess.processName)) {
							if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
								return true;
							} else {
								return false;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			CHLogger.e(this, e.getMessage());
		}
		return false;
	}

	protected abstract Bundle doTask(Date date);

	protected abstract int getRunInterval(boolean isAppForegroud);

	protected SQLiteDB getSqlitdb() {

		if(sqlitdb == null){
			sqlitdb = CHApplication.getApplication().getSQLiteDatabasePool().getSQLiteDatabase();
		}
		return sqlitdb;
	}

	protected void releaseSqlitdb() {
		CHApplication.getApplication().getSQLiteDatabasePool().releaseSQLiteDatabase(sqlitdb);
		sqlitdb = null;
	}
}
