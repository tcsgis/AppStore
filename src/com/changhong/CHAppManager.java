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

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;

import com.aaa.activity.main.MainActivity;
import com.changhong.util.CHLogger;

public class CHAppManager
{
	private static Stack<Activity> activityStack;
	private static CHAppManager instance;

	private CHAppManager()
	{

	}

	/**
	 * 单一实例
	 */
	public static CHAppManager getAppManager()
	{
		if (instance == null)
		{
			instance = new CHAppManager();
		}
		return instance;
	}

	/**
	 * 添加Activity到堆栈
	 */
	public void addActivity(Activity activity)
	{
		if (activityStack == null)
		{
			activityStack = new Stack<Activity>();
		}
		activityStack.add(activity);
	}

	/**
	 * 获取当前Activity（堆栈中最后一个压入的）
	 */
	public Activity currentActivity()
	{
		Activity activity = activityStack.lastElement();
		return activity;
	}

	/**
	 * 结束当前Activity（堆栈中最后一个压入的）
	 */
	public void finishActivity()
	{
		Activity activity = activityStack.lastElement();
		finishActivity(activity);
	}

	/**
	 * 结束指定的Activity
	 */
	public void finishActivity(Activity activity)
	{
		if (activity != null)
		{
			activityStack.remove(activity);
			activity.finish();
			activity = null;
		}
	}

	/**
	 * 移除指定的Activity
	 */
	public void removeActivity(Activity activity)
	{
		if (activity != null)
		{
			activityStack.remove(activity);
			activity = null;
		}
	}

	/**
	 * 结束指定类名的Activity
	 */
	public void finishActivity(final Class<?> cls)
	{
		if (activityStack != null && !activityStack.empty()) {
			//避免迭代过程中remove集合改变造成出错
			for (int i = activityStack.size() - 1; i >= 0; --i)
			{
				Activity activity = activityStack.get(i);
				if (activity.getClass().equals(cls))
				{
					finishActivity(activity);
				}
			}
		}
	}

	/**
	 * 结束所有Activity
	 */
	public void finishAllActivity()
	{
		if (activityStack != null){
			int size = activityStack.size() - 1;
			for (int i = size; i >= 0; i--)
			{
				Activity activity = activityStack.get(i);
				if (null != activity)
				{
					activity.finish();
				}
			}
			activityStack.clear();
		}
	}
	
	/**
	 * 结束所有Activity,2:main
	 */
	public void toMainActivity(Context context)
	{
		toActivity(context, MainActivity.class, null);
	}

	/**
	 * 关闭所有在指定类型Activity之上的Activity
	 * 未找到时则结束所有Activity
	 * @param context 上下文
	 *
	 * @param activityClass 指定Activity类型(为null则全部结束)
	 * @return 是否找到满足条件的Activity
	 */
	public void toActivity(Context context, final Class<? extends Activity> activityClass, final Bundle extras) {
		boolean res = false;
		if (activityStack == null) return;

		List<Activity> toFinish = new ArrayList<Activity>();
		try {
			int size = activityStack.size() - 1;
			for (int i = size; i >= 0; i--)
			{
				Activity activity = activityStack.get(i);
				if (activity != null)
				{
					if (activityClass != null && activityClass.isInstance(activity)) {
						//此处赋值的原因是如context不是Act, start时无法获取TSK
						//需加FLAG_ACTIVITY_NEW_TASK, 但后者又会新启一个
						context = activity;
						res = true;
						break;
					}
					toFinish.add(activity);
				} else {
					//why?
					activityStack.remove(i);
				}
			}
			//强制start一次, 在部分手机上连续退出多个Activity时回退栈会出错??
			final boolean needStart = (activityClass != null) && ((!res) || (extras != null));
			if (needStart) {
				final Intent intent = new Intent(context, activityClass);
				if (extras != null)
					intent.putExtras(extras);
				if (res) {
					//Activity还在, 直接恢复即可
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
							Intent.FLAG_ACTIVITY_SINGLE_TOP);
//					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					//toFinish = null;//singleInstance模式下无效
				} else {
					//Activity已被杀, 尝试重建(此时应该已对所有Act执行finish)
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
						intent.addFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME |
								Intent.FLAG_ACTIVITY_CLEAR_TASK);
				}
				intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				context.startActivity(intent);

				if (toFinish != null) {
					for (Activity act : toFinish) {
						act.finish();
						act.overridePendingTransition(0,0);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 退出应用程序
	 * 
	 * @param context
	 *            上下文
	 * @param isBackground
	 *            是否开开启后台运行
	 */
	public void AppExit(final Context context, final boolean isBackground)
	{
		try {
			finishAllActivity();
			new Thread(){
				@Override
				public void run() {
					try{
						try {
							Thread.sleep(700);
						} catch (InterruptedException e) {
						}
						ActivityManager activityMgr = (ActivityManager) context
								.getSystemService(Context.ACTIVITY_SERVICE);
						activityMgr.killBackgroundProcesses(context.getPackageName());
					} catch (Exception e)
					{
//						e.printStackTrace();
					} finally
					{
						// 注意，如果您有后台程序运行，请不要支持此句子
						if (!isBackground)
						{
							System.exit(0);
//							Process.killProcess(Process.myPid());
						}
					}
				}
			}.start();
			/*ActivityManager activityMgr = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);
			activityMgr.killBackgroundProcesses(context.getPackageName());*/
		} catch (Exception e)
		{
			System.exit(0);
//			e.printStackTrace();
		}/* finally
		{
			// 注意，如果您有后台程序运行，请不要支持此句子
			if (!isBackground)
			{
				System.exit(0);
				Process.killProcess(Process.myPid());
			}
		}*/
	}

	/**
	 * 仅在栈中还存在其它Activity时结束当前Activity
	 * @param activity
	 * @return 是否结束当前Activity
	 */
	public boolean moveToPrev(final Activity activity) {
		if (activityStack.size() > 1) {
			activityStack.remove(activity);
			toActivity(activity, activity.getClass(), activity.getIntent().getExtras());
			activity.finish();
			return true;
		}
		return false;
	}
}