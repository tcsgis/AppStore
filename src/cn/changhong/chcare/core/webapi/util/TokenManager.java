package cn.changhong.chcare.core.webapi.util;


public class TokenManager {
	
	private static String token;
	private static String filetoken;
	
	private static boolean isConnectNet = true;
	
	public static String getToken() {
		return token;
	}

	public static void setToken(String newToken) {
		token = newToken;
	}

	public static String getFiletoken() {
		return filetoken;
	}

	public static void setFiletoken(String filetoken) {
		TokenManager.filetoken = filetoken;
	}

	public static boolean isConnectNet() {
		return isConnectNet;
	}

	public static void setConnectNet(boolean isConnectNet) {
		TokenManager.isConnectNet = isConnectNet;
	}
	
	
}
