package com.changhong.service.task.receiver;

import java.util.Date;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;
import cn.changhong.chcare.core.webapi.bean.OfflineMessageBean;
import cn.changhong.chcare.core.webapi.bean.ResponseBeanWithRange;
import cn.changhong.chcare.core.webapi.server.CHCareWebApiProvider;
import cn.changhong.chcare.core.webapi.server.CHCareWebApiProvider.WebApiServerType;
import cn.changhong.chcare.core.webapi.server.IOfflineMessageService;
import com.changhong.CHApplication;
import com.changhong.service.task.AlarmBackRunTask;
import com.changhong.util.CHLogger;
import com.changhong.util.config.MyProperties;
import com.changhong.util.db.bean.CacheManager;
import com.llw.AppStore.R;

public class AlarmMessageReceiver extends AlarmBackRunTask{

	private static final int GET_NEW_CUSTOM_BID = 10001;//有新的发型师参与竞标

	private static final int mBackRunInterval = 10 * 1000;//后台运行时触发事件间隔
	private static final int mForegroudInterval = 10 * 1000;//前台运行时触发事件间隔
	
	private static long mStartIndex = -1;
	private static int userId = -1;
	private static boolean haveNotification = true;


	private Handler mMainHandler;
	private MyHandler mMyHandler;
	private int mPreviousTime = 0;//最大为6

	private SharedPreferences mSP;

	private CHApplication application = CHApplication.getApplication();

	private Bundle retNotifyInfo;

	private IOfflineMessageService mMessageService = 
			(IOfflineMessageService) CHCareWebApiProvider.Self
			.defaultInstance().getDefaultWebApiService(
					WebApiServerType.CHCARE_OFFLINEMESSAGE_SERVER);

	private static AlarmMessageReceiver instance;
	private AlarmMessageReceiver(){

	}

	public static AlarmMessageReceiver getInstance(){

		if(instance == null){
			instance = new AlarmMessageReceiver();
			instance.initStart();
		}
		return instance;
	};

	public void setmMainHandler(Handler mMainHandler) {
		this.mMainHandler = mMainHandler;
	}

	public void setHaveNotification(boolean haveNotification) {
		AlarmMessageReceiver.haveNotification = haveNotification;
	}
	
	@Override
	protected Bundle doTask(Date date) {
		Bundle ret = null;
		try {
			requireNotify();
			if(retNotifyInfo != null && !isAppForegroud() && haveNotification )ret = retNotifyInfo;//如果是前台运行就不用通知提醒
			retNotifyInfo = null;
		} catch (Exception e) {
			CHLogger.e(this, "=doTask="+e.getLocalizedMessage());
		}
		return ret;
	}

	@Override
	protected int getRunInterval(boolean isAppForegroud) {
		//在前台并且没有异常情况时频率为10秒
		
//		if(isAppForegroud && mPreviousTime < 1){
			return mForegroudInterval;
//		}else{
//			return (mPreviousTime + 1) * mBackRunInterval;
//		}

	}
	
	@SuppressLint("HandlerLeak")
	private class MyHandler extends Handler {


		@Override
		public void handleMessage(Message msg) {
			
		}
	}

	private void initStart() {
		
		try {
			if(mMyHandler == null){
				mMyHandler = new MyHandler();
			}	

			if(application == null){
				application = CHApplication.getApplication();
			}
			
			String receiveCenterKey = MyProperties.getMyProperties().getReceiveNotifyKey();
			haveNotification = CHApplication.getApplication().getPreferenceConfig().getBoolean(receiveCenterKey, true);
		} catch (Exception e) {
			CHLogger.e(this, e);
		}
		
	}

	@SuppressWarnings("unchecked")
	private void requireNotify() {
		if(CacheManager.INSTANCE.getCurrentUser() == null) return;

		int reState = 0;
		try {

			int currentUserId = CacheManager.INSTANCE.getCurrentUser().getID();
			initSharedPrefrences(currentUserId);
			CHLogger.e(this, "requireNotify, startIndex = " + mStartIndex);

			ResponseBeanWithRange<?> response = mMessageService.pollingMessage(mStartIndex, -1);

			if (response != null && response.getState() >= 0) {
				refreshStartIndex(response.getEndIndex());
				
				List<OfflineMessageBean<?>> msgList = (List<OfflineMessageBean<?>>) response.getData();
				if (msgList != null && msgList.size() > 0) {
					handleNotify(msgList);
				}
			} else if(response.getState() == -15){//cxp temp
				retNotifyInfo = new Bundle();
				retNotifyInfo.putInt("state", response.getState());
			} 
			
			reState = response.getState();
		} catch (Exception e) {
			CHLogger.e(this, e.getLocalizedMessage() + e.getMessage());
			reState = -400;
		}
		
		if(reState == -1 || reState == -400){
			if(mPreviousTime < 7)++mPreviousTime;
		}else{
			mPreviousTime = 0;
		}
	}

	private void handleNotify(List<OfflineMessageBean<?>> list) {
		int notifyCount = list.size();
		boolean needSound = false;
		
		if(notifyCount > 0){
			needSound = application.getPreferenceConfig().getBoolean(MyProperties.getMyProperties().getSoundRemindKey(), true);
			boolean needNotify = application.getPreferenceConfig().getBoolean(MyProperties.getMyProperties().getReceiveNotifyKey(), true);

			if(needNotify){
				retNotifyInfo = notifySystem(notifyCount);
			}
		}
	
		if(needSound)
			soundRemind();
	}
	
	private void initSharedPrefrences(int currentUserId){

		try {

			if(userId != currentUserId) {

				mSP = application.getSharedPreferences(String.valueOf(currentUserId), Activity.MODE_PRIVATE);
				if(mSP.contains("lastMsgId")){
					mStartIndex = mSP.getLong("lastMsgId", 0);
				}else{

					SharedPreferences.Editor mSPEditor = mSP.edit();
					mSPEditor.putLong("lastMsgId", 0);
					mSPEditor.apply();
					mStartIndex = 0;
				}
			}

		} catch (Exception e) {
			CHLogger.e(this, e.getMessage());
			mStartIndex = 0;
		}finally{
			userId = currentUserId;
		}
	}

	private void refreshStartIndex(int maxIndex) {

		if (maxIndex > mStartIndex) {
			try {

				SharedPreferences.Editor mSPEditor = mSP.edit();
				mSPEditor.putLong("lastMsgId", maxIndex).apply();
			} catch (Exception e) {
				CHLogger.e(this, e.getMessage());
			}finally{
				mStartIndex = maxIndex;
			}
		}
	}

	private void soundRemind() {
		Uri uri = RingtoneManager.getActualDefaultRingtoneUri(application, RingtoneManager.TYPE_NOTIFICATION);
		Ringtone r = RingtoneManager.getRingtone(application, uri);

		if(r != null){
			r.play();
		}
	}

	private Bundle notifySystem(int msgCount) {
		Bundle ret = new Bundle();
		ret.putInt("title", R.string.app_name);
		ret.putString("text", application.getResources().getString(R.string.there_is_new_msg, msgCount));
		ret.putInt("actionRes", R.string.MessageServiceNotify);
		
		return ret;
	}

	private void sendMainMessage(int action, int count){
		if(mMainHandler != null)
			mMainHandler.sendMessage(mMainHandler.obtainMessage(action, count));
	}
	
	private void sendMainEmptyMessage(int action){
		if(mMainHandler != null)
			mMainHandler.sendEmptyMessage(action);
	}

	private void saveOrUpateObj(Object obj , String where){
		try {
			List<?> objs= getSqlitdb().query(obj.getClass(), true, where, null, null, null, "1");

			if(objs == null || objs.isEmpty()){
				getSqlitdb().insert(obj);
			}else{
				getSqlitdb().update(obj);
			}
		} catch (Exception e) {
			CHLogger.e(this, e.getMessage());
		}

	}
	
	@Override
	public void doExit() {
		mMainHandler = null;
		mMyHandler = null;
//		mOtherHandler = null;
		mSP = null;
		instance = null;
	}
	
	private void showToast(int res){
		try {
			if(isAppForegroud()){
				Toast.makeText(CHApplication.getApplication(), res, Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
