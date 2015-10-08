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

import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.changhong.activity.util.Tools;
import com.changhong.util.CHLogger;
import com.changhong.util.config.CHIConfig;
import com.changhong.util.db.SQLiteDB;
import com.changhong.util.netstate.NetWorkUtil.netType;

public abstract class CHActivity extends Activity
{
	/** 模块的名字 */
	private String moduleName = "";
	/** 布局文件的名字 */
	private String layouName = "";
	private boolean thDoFinish = false;
	private SQLiteDB sqlitdb;
	
	private Dialog mDialog;
	private boolean isWaiting = false;
	
	private static final String TAIDENTITYCOMMAND = "chidentitycommand";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		notifiyApplicationActivityCreating();

		try {
			onPreOnCreate(savedInstanceState);
			super.onCreate(savedInstanceState);

			getCHApplication().getAppManager().addActivity(this);
			initActivity();
			onAfterOnCreate(savedInstanceState);
			notifiyApplicationActivityCreated();
		} catch (Exception e) {
			// TODO: handle exception
			CHLogger.e(this, e);
			super.finish();
			Tools.reLogin(false);
		}
	}

	protected void onPreOnCreate(Bundle savedInstanceState){
		
	}
	
	public CHApplication getCHApplication()
	{
		return (CHApplication) getApplication();
	}

	private void notifiyApplicationActivityCreating()
	{
//		getCHApplication().onActivityCreating(this);
	}

	private void notifiyApplicationActivityCreated()
	{
//		getCHApplication().onActivityCreated(this);
	}

	@Override
	protected void onDestroy() {
		hideWaitDialog();
		super.onDestroy();
		notifiyApplicationActivityDestroyed();
	}

	private void notifiyApplicationActivityDestroyed()
	{
		getCHApplication().onActivityDestroyed(this);
	}


	/**
	 * 指示该页面是否需要登录信息
	 * @return
	 */
	protected boolean needLogin() {
		return true;
	}

	private void initActivity()
	{
		// 初始化模块名
		getModuleName();
		// 初始化布局名
		getLayouName();
		// 加载类注入器
		initInjector();
		// 自动加载默认布局
		loadDefautLayout();
	}

	protected void onAfterOnCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
	}

	/**
	 * 初始化注入器
	 */
	private void initInjector()
	{
		// TODO Auto-generated method stub
		getCHApplication().getInjector().injectResource(this);
		getCHApplication().getInjector().inject(this);
	}

	/**
	 * 自动加载默认布局
	 */
	private void loadDefautLayout()
	{
		try
		{
			int layoutResID = getCHApplication().getLayoutLoader().getLayoutID(
					layouName);
			setContentView(layoutResID);
		} catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	@Override
	public void setContentView(int layoutResID)
	{
		// TODO Auto-generated method stub
		super.setContentView(layoutResID);
		// 由于view必须在视图记载之后添加注入
		getCHApplication().getInjector().injectView(this);
		onAfterSetContentView();
	}

	public void setContentView(View view, LayoutParams params)
	{
		super.setContentView(view, params);
		// 由于view必须在视图记载之后添加注入
		getCHApplication().getInjector().injectView(this);
		onAfterSetContentView();
	}

	public void setContentView(View view)
	{
		super.setContentView(view);
		// 由于view必须在视图记载之后添加注入
		getCHApplication().getInjector().injectView(this);
		onAfterSetContentView();
	}

	protected void onAfterSetContentView()
	{
		// TODO Auto-generated method stub

	}

	/**
	 * 获取模块的名字
	 */
	public String getModuleName()
	{
		String moduleName = this.moduleName;
		if (moduleName == null || moduleName.length() == 0)//moduleName.equalsIgnoreCase("")
		{
			final String className = getClass().getName();
			//moduleName = className.substring(0, className.length());//和className完全一样??
			moduleName = className;
//			String arrays[] = moduleName.split("\\.");

//			this.moduleName = changeXName(arrays[arrays.length - 1]);
			final int index = moduleName.lastIndexOf('.');
			//TODO:第一次没有设置到返回值上, 是个bug吗
			String tmp = moduleName;
			if (index != -1) {
				tmp = tmp.substring(index + 1);
			}
			this.moduleName = changeXName(tmp);
		}
		return moduleName;
	}

	/**
	 * 把LoginActivity这样的字段改成login_activity
	 */
	private String changeXName(String name){
		StringBuffer sb = new StringBuffer();
		char[] c = name.toCharArray();
		for(int i=0;i<c.length;i++){
			if(c[i]<97){
				sb.append("_");
				sb.append((""+c[i]).toLowerCase());
			}else{
				sb.append(c[i]);
			}
		}
		return sb.substring(1);
	}

	/**
	 * 设置模块的名字
	 */
	public void setModuleName(String moduleName)
	{
		this.moduleName = moduleName;
	}

	/**
	 * 获取布局文件名
	 * 
	 * @return布局文件名
	 */
	public String getLayouName()
	{
		String layouName = this.layouName;
		if (layouName == null || layouName.equalsIgnoreCase(""))
		{
			this.layouName = this.moduleName;
		}
		return layouName;
	}

	/**
	 * 设置布局文件名
	 */
	protected void setLayouName(String layouName)
	{
		this.layouName = layouName;
	}

//	/**
//	 * 此方法调用时,界面还未被初始化
//	 * @param response activity之间传递的参数data中为bundle对象
//	 */
//	public void preProcessData(CHResponse response)
//	{
//
//	}
//
//	/**
//	 * 此方法调用时,界面已经被初始化
//	 * @param response activity之间传递的参数data中为bundle对象
//	 */
//	public void processData(CHResponse response)
//	{
//
//	}
//
//	public final void doCommand(int resId, CHRequest request)
//	{
//		doCommand(resId, request, null, false);
//	}
//
//	public final void doCommand(String commandKey, CHRequest request)
//	{
//		doCommand(commandKey, request, null, false);
//	}
//	//cxp页面跳转时不要等待条
//	private final void doActivity(String commandKey, CHRequest request)
//	{
//		this.thDoFinish = false;
//		doCommand(commandKey, request, null, false);
//	}
//
//	public final void doCommand(int resId, CHRequest request,
//			CHIResponseListener listener)
//	{
//		doCommand(resId, request, listener, false);
//	}
//
//	public final void doCommand(String commandKey, CHRequest request,
//			CHIResponseListener listener)
//	{
//		doCommand(commandKey, request, listener, false);
//	}
//
//	public final void doCommand(int resId, CHRequest request,
//			CHIResponseListener listener, boolean showProgress)
//	{
//		doCommand(resId, request, listener, showProgress, true);
//	}
//
//	public final void doCommand(String commandKey, CHRequest request,
//			CHIResponseListener listener, boolean showProgress)
//	{
//		doCommand(commandKey, request, listener, showProgress, true);
//	}
//
//	public final void doCommand(int resId, CHRequest request,
//			CHIResponseListener listener, boolean showProgress, boolean record)
//	{
//		doCommand(resId, request, listener, showProgress, record, false);
//	}
//
//	public final void doCommand(String commandKey, CHRequest request,
//			CHIResponseListener listener, boolean showProgress, boolean record)
//	{
//		doCommand(commandKey, request, listener, showProgress, record, false);
//	}
//
//	public final void doCommand(int resId, CHRequest request,
//			CHIResponseListener listener, boolean showProgress, boolean record,
//			boolean resetStack)
//	{
//		String commandKey = String.valueOf(resId);
//		doCommand(commandKey, request, listener, showProgress, record,
//				resetStack);
//	}
//
//	public final void doCommand(String commandKey, CHRequest request,
//			CHIResponseListener listener, boolean showProgress, boolean record,
//			boolean resetStack)
//	{
//		CHLogger.i(CHActivity.this, "go with cmdid=" + commandKey
//				+ ", record: " + record + ", request: " + request
//				+ ", listener: " + listener + ", showProgress: " + showProgress
//				+ ", resetStack: " + resetStack);
//		getCHApplication().doCommand(commandKey, request, listener, record,
//				resetStack);
//	}

	/**
	 * 返回
	 */
	public final void back()
	{
//		getCHApplication().back(this);
	}

	/**
	 * 网络连接连接时调用
	 */
	public void onConnect(netType type)
	{

	}

	/**
	 * 当前没有网络连接
	 */
	public void onDisConnect()
	{

	}

	@Override
	public void finish()
	{
		// TODO Auto-generated method stub
		hideWaitDialog();
		getCHApplication().getAppManager().removeActivity(this);
		super.finish();
	}

	/**
	 * 退出应用程序
	 * 
	 * @param isBackground
	 *            是否开开启后台运行,如果为true则为后台运行
	 */
	public void exitApp(boolean isBackground)
	{
		moveTaskToBack(false);
		getCHApplication().exitApp(isBackground);
	}

	/**
	 * 退出应用程序，且在后台运行
	 * 
	 */
	public void exitAppToBackground()
	{
		exitApp(true);
	}

	/**
	 * 运行activity
	 * 
	 * @param activityResID 对应 AndroidManifest.xml中的activity的android:label属性值
	 */
//	public final void doActivity(int activityResID)
//	{
//		doActivity(activityResID, null);
//	}

	/**
	 * 
	 * @param activityResID 对应 AndroidManifest.xml中的activity的android:label属性值
	 * @param record 表示是否可以返回,如果有false表示不可返回,如果有true表示可返回 .默认为true.
	 */
//	public final void doActivity(int activityResID, boolean record)
//	{
//		this.thDoFinish = record;
//		CHRequest request = getActivityRequest(activityResID, null);
//		doCommand(TAIDENTITYCOMMAND, request, null, false, record);
//	}
//
//	/**
//	 * 
//	 * @param activityResID 对应 AndroidManifest.xml中的activity的android:label属性值
//	 * @param bundle 参数传递
//	 */
//	public final void doActivity(int activityResID, Bundle bundle)
//	{
//		CHRequest request = getActivityRequest(activityResID, bundle);
//		// 启动activity
//		doActivity(TAIDENTITYCOMMAND, request);
//	}
//
//	private CHRequest getActivityRequest(int activityResID, Bundle bundle){
//		CHRequest request = new CHRequest();
//		request.setData(bundle);
//		request.setActivityKeyResID(activityResID);
//		return request;
//	}
//
//	public final void doActivityTransObj(int activityResID, Object obj)
//	{
//		CHRequest request = new CHRequest();
//		request.setData(obj);
//		request.setActivityKeyResID(activityResID);
//		// 启动activity
//		doActivity(TAIDENTITYCOMMAND, request);
//	}


	/**
	 * 通过startActivityForResult方法运行activity 
	 * 
	 */
	public final void doActivityForResult(Intent intent, int requestCode)
	{
		startActivityForResult(intent, requestCode);
	}

	@Override
	public void onBackPressed()
	{
		back();
		super.onBackPressed();
	}

	/**
	 * 
	 * @return 是否有activity返回值，如果有跳转前的activity就不会被finish
	 */
	public boolean isThDoFinish() {
		return thDoFinish;
	}

	public void setThDoFinish(boolean thDoFinish) {
		this.thDoFinish = thDoFinish;
	}
	
	public SQLiteDB getSqlitdb() {
		if(sqlitdb == null) {
			synchronized (this) {
				if (sqlitdb == null) {
					//CHLogger.w(this, "getSqlitdb");
					sqlitdb = getCHApplication().getSQLiteDatabasePool().getSQLiteDatabase();
				}
			}
		}
		return sqlitdb;
	}

	public void closeDb() {
		if (sqlitdb != null) {
			synchronized (this) {
				if (sqlitdb != null) {
					//CHLogger.w(this, "closeDb");
					getCHApplication().getSQLiteDatabasePool().releaseSQLiteDatabase(sqlitdb);
					sqlitdb = null;
				}
			}
		}
	}

	/**
	 * 保存页面信息
	 */
	protected void onSaveInstanceState(Bundle state) {
		super.onSaveInstanceState(state);
	}

	public void setmDialog(Dialog mDialog, boolean isCanDimiss) {
		
		hideWaitDialog();
		this.mDialog = mDialog;
		this.isWaiting = isCanDimiss;
	}
	
	public void hideWaitDialog() {
		if(isWaiting){
			hideAllDialog();
		}
	}
	
	public void hideAllDialog(){
		if(mDialog!=null && mDialog.isShowing()){
			mDialog.dismiss();
		}
		mDialog = null;
	}
}
