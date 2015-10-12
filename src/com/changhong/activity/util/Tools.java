/**     
 * @Title: tools.java  
 * @Package com.changhong.family.util  
 * @Description: TODO  
 * @author quxy    
 * @date 2014年9月28日 下午4:14:06  
 * @version V1.0     
 */
package com.changhong.activity.util;

import java.io.File;
import java.security.MessageDigest;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import android.content.Context;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Toast;

import cn.changhong.chcare.core.webapi.util.TokenManager;

import com.llw.AppStore.R;
import com.aaa.activity.main.MainActivity;
import com.changhong.CHApplication;
import com.changhong.common.AndroidVersionCheckUtils;
import com.changhong.util.cache.CHExternalOverFroyoUtils;
import com.changhong.util.cache.CHExternalUnderFroyoUtils;
import com.changhong.util.db.bean.CacheManager;

/**
 * @ClassName: tools
 * @Description: TODO
 * @author quxy
 * @date 2014年9月28日 下午4:14:06
 * 
 */
public class Tools {
	
	// for toast
	private static String oldMsg;
	protected static Toast toast = null;
	private static long oneTime = 0;
	private static long twoTime = 0;

	/**
	 * 返回目录的路径
	 * 
	 * @param context
	 * @param dirName
	 *            生成的目录名
	 * @return　如果失败路径为null
	 */
	public static String getAppDiskCacheDir(Context context, String dirName) {
		try {
			File dir = null;
			if (AndroidVersionCheckUtils.hasGingerbread()) {
				dir = CHExternalOverFroyoUtils
						.getDiskCacheDir(context, dirName);
			} else {
				dir = CHExternalUnderFroyoUtils.getDiskCacheDir(context,
						dirName);
			}
			if (!dir.exists()) {
				dir.mkdirs();
			}
			return dir.getPath();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * 判断输入的字符是否是数字和字母
	 */
	public static boolean isPasswdChar(char ch) {
		if (ch == 'A' || ch == 'B' || ch == 'C' || ch == 'D' || ch == 'E'
				|| ch == 'F' || ch == 'G' || ch == 'H' || ch == 'I'
				|| ch == 'J' || ch == 'K' || ch == 'L' || ch == 'M'
				|| ch == 'N' || ch == 'O' || ch == 'P' || ch == 'Q'
				|| ch == 'R' || ch == 'S' || ch == 'T' || ch == 'U'
				|| ch == 'V' || ch == 'W' || ch == 'X' || ch == 'Y'
				|| ch == 'Z' || ch == 'a' || ch == 'b' || ch == 'c'
				|| ch == 'd' || ch == 'e' || ch == 'f' || ch == 'g'
				|| ch == 'h' || ch == 'i' || ch == 'j' || ch == 'k'
				|| ch == 'l' || ch == 'm' || ch == 'n' || ch == 'o'
				|| ch == 'p' || ch == 'q' || ch == 'r' || ch == 's'
				|| ch == 't' || ch == 'u' || ch == 'v' || ch == 'w'
				|| ch == 'x' || ch == 'y' || ch == 'z' || ch == '1'
				|| ch == '2' || ch == '3' || ch == '4' || ch == '5'
				|| ch == '6' || ch == '7' || ch == '8' || ch == '9'
				|| ch == '0') {
			return true;
		}
		return false;
	}
	
	
	// for toast 暂时放这里
	public static void showToast(Context context, String s) {
		if (toast == null) {
			toast = Toast.makeText(context, s, Toast.LENGTH_SHORT);
			toast.show();
			oneTime = System.currentTimeMillis();
		} else {
			twoTime = System.currentTimeMillis();
			if (s.equals(oldMsg)) {
				if (twoTime - oneTime > Toast.LENGTH_SHORT) {
					toast.show();
				}
			} else {
				oldMsg = s;
				toast.setText(s);
				toast.show();
			}
		}
		oneTime = twoTime;
	}

	// for toast 暂时放这里
	public static void showToast(Context context, int resId) {
		showToast(context, context.getString(resId));
	}
	
	/**
	 * 返回到主界面
	 */
	public static void reMain(){
		
		CHApplication app = CHApplication.getApplication();
		app.getAppManager().toActivity(app, MainActivity.class, null);	
	}
	
	/**
     * Description 
     * @param data
     * @param key  
     * @return
     * @throws Exception
     */
	  private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
	        DESKeySpec dks = new DESKeySpec(key);
	        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
	        SecretKey securekey = keyFactory.generateSecret(dks);
	        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
	        IvParameterSpec iv = new IvParameterSpec(key);
	        cipher.init(Cipher.ENCRYPT_MODE, securekey, iv);
	 
	        return cipher.doFinal(data);
	    }

	/**
	 * 字节数组转字符串
	 * 
	 * @param byteArray 字节数组
	 */
	private static  String byteArrayToHex(byte[] byteArray) {
		char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'A', 'B', 'C', 'D', 'E', 'F' };
		char[] resultCharArray = new char[byteArray.length * 2];
		int index = 0;
		for (byte b : byteArray) {

			resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];

			resultCharArray[index++] = hexDigits[b & 0xf];

		}
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < 8; i++) {
			builder.append(resultCharArray[i]);
		}

		return builder.toString();
	}
	
	public static String des_Encypt(String name,String Password) {
		byte[] des_key  = convert_MD5(name);
		if (Password == null)
			return null;
		byte[] result;
		try {
			result = encrypt(Password.getBytes(), des_key);
			return Base64.encodeToString(result, Base64.NO_WRAP);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static int bytesLengthUTF2GBK(String s){
		if(s == null)
			return 0;
		int length = s.length();
		int bytesLength = s.getBytes().length;
		int result = (bytesLength - length) / 2;//得到汉字的个数
		return length + result;//汉字和拼音的个数
	}
	
	private static  byte[] convert_MD5(String s) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			byte[] inputByteArray = s.getBytes("UTF-8");
			messageDigest.update(inputByteArray);
			byte[] resultByteArray = messageDigest.digest();
			return Arrays.copyOf(resultByteArray, 8);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	
	/**
	 * 退出程序
	 * @param isAppExit 是否正常退出
	 */
	public static void reLogin(boolean isAppExit)
	{
		
		CacheManager.INSTANCE.clearAllData();
		TokenManager.setFiletoken(null);
		TokenManager.setToken(null);
		
		CHApplication app = CHApplication.getApplication();
		final Bundle bundle = new Bundle();
		bundle.putBoolean("AppExit", isAppExit);
		app.getAppManager().toActivity(app, MainActivity.class, bundle);
		
		try {
			CHApplication.getApplication().getCurrentActivity().closeDb();
			CHApplication.getApplication().closeSQLiteDatabase();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
