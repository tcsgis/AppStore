package com.aaa.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SPUtil {

	private static SharedPreferences sp = null;
	private static SharedPreferences.Editor editor = null;
	private static String SP = "sgis-llw-base";
	
	private static SharedPreferences getSP(Context context){
		if(sp == null){
			sp = context.getSharedPreferences(SP, Activity.MODE_PRIVATE);
		}
		return sp;
	}
	
	private static SharedPreferences.Editor getSPE(Context context){
		if(editor == null){
			editor = getSP(context).edit();
		}
		return editor;
	}
	
	public static boolean contains(Context context, String key){
		return getSP(context).contains(key);
	}
	
	public static int getInt(Context context, String key, int defaultValue){
		return getSP(context).getInt(key, defaultValue);
	}
	
	public static void putInt(Context context, String key, int value){
		getSPE(context).putInt(key, value).apply();
	}
	
	public static float getFloat(Context context, String key, float defaultValue){
		return getSP(context).getFloat(key, defaultValue);
	}
	
	public static void putFloat(Context context, String key, float value){
		getSPE(context).putFloat(key, value).apply();
	}
	
	public static boolean getBoolean(Context context, String key, boolean defaultValue){
		return getSP(context).getBoolean(key, defaultValue);
	}
	
	public static void putBoolean(Context context, String key, boolean value){
		getSPE(context).putBoolean(key, value).apply();
	}
	
	public static long getLong(Context context, String key, long defaultValue){
		return getSP(context).getLong(key, defaultValue);
	}
	
	public static void putLong(Context context, String key, long value){
		getSPE(context).putLong(key, value).apply();
	}
	
	public static String getString(Context context, String key, String defaultValue){
		return getSP(context).getString(key, defaultValue);
	}
	
	public static void putString(Context context, String key, String value){
		getSPE(context).putString(key, value).apply();
	}
}
