package com.changhong.service;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;

import com.aaa.activity.main.MainActivity;
import com.changhong.CHApplication;
import com.changhong.activity.util.PollingUtils;
import com.changhong.service.task.AlarmOnObserver;
import com.changhong.service.task.receiver.AlarmMessageReceiver;
import com.changhong.util.CHLogger;
import com.llw.AppStore.R;

public class PollingService extends Service {

	private static final int DELAY_MILLES = 10 * 1000;
	
	private Notification mNotification;
	private NotificationManager mManager;
	private Map<Integer, AlarmOnObserver> tasks = new ConcurrentHashMap<Integer, AlarmOnObserver>();
	private ExecutorService pool;
	private HandlerThread mHandlerThread;
	private Handler mMyHandler;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		initNotifiManager();
		pool = Executors.newSingleThreadExecutor();
	}

	@Override
	public void onStart(Intent intent, int startId) {
//		if(intent != null && intent.getAction().equals(getResources().getString(R.string.AlarmAnniServiceNotify))){
//			System.out.println("Polling..." + intent.getAction());
//			try {
//				pool.execute(new PollingThread());
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}else{
//			
//			stopSelf();
//		}
		if (intent != null
				&& intent.getAction().equals(getResources().getString(
								R.string.AlarmAnniServiceNotify))) {
			initNotifiManager();
		} else {

			stopSelf();
		}
	}
	
	@Override
	public void onDestroy() {
		try {
			pool.shutdown();
			notifyObserverExit();
		} catch (Exception e) {
		}
		super.onDestroy();
	}

	//初始化通知栏配置
	private void initNotifiManager() {
		
		if(mHandlerThread == null){
			tasks.put(R.string.MessageServiceNotify, AlarmMessageReceiver.getInstance());
			
			mManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			int icon = R.drawable.icon;
			mNotification = new Notification();
			mNotification.icon = icon;
			mNotification.tickerText = "梳妆台";
			mNotification.defaults |= Notification.DEFAULT_SOUND;
			mNotification.flags = Notification.FLAG_AUTO_CANCEL;
			
			mHandlerThread = new HandlerThread("PollingService");
			mHandlerThread.start();
			mMyHandler = new Handler(mHandlerThread.getLooper());
			CHLogger.d(this, "mMyHandler.postDelayed(mTasks, 1000)");
			mMyHandler.postDelayed(mTasks, 1000);
		}
	}

	//弹出Notification
	private void showNotification(Bundle bundle) {
		mNotification.when = System.currentTimeMillis();
		//Navigator to the new activity when click the notification title
		System.out.println("showNotification==>" + bundle);
		
		if(bundle != null){
			
			int netstate = bundle.getInt("state");
			
			if(netstate == -15){
				PollingUtils.stopPollingService(this, PollingService.class, getString(R.string.AlarmAnniServiceNotify));
				return;
			}
			
			int titleRes = bundle.getInt("title");
			String text = bundle.getString("text");
			int actionRes = bundle.getInt("actionRes");
			
			Intent intent = new Intent(this, MainActivity.class);
			intent.setAction(getString(actionRes));
			PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
			mNotification.setLatestEventInfo(this,getResources().getString(titleRes), text, pendingIntent);
			mManager.notify(actionRes, mNotification);
		}
	}
	
	private void notifyObserver()
	{
		
		if(CHApplication.getApplication() == null) return;
		
		Date date = Calendar.getInstance().getTime();
		for (AlarmOnObserver observer : tasks.values())
		{
			if (observer != null)
			{
				Bundle ret = observer.onTimeOut(date);
				showNotification(ret);
			}
		}
	}
	
	
	private void notifyObserverExit()
	{
		for (AlarmOnObserver observer : tasks.values())
		{
			if (observer != null)
			{
				observer.doExit();
			}
		}
	}
	
	public void putTaskInfo(int actionRes, AlarmOnObserver observer)
	{
		if(!tasks.containsKey(actionRes)){
			tasks.put(actionRes, observer);
		}
	}
	
	private Runnable mTasks = new Runnable()
	{

		public void run()
		{
			notifyObserver();
			mMyHandler.postDelayed(mTasks, DELAY_MILLES);
		}
	};
}
