package com.changhong.util.config;


/*
 * Author:quxy
 * Name:MyProperties.java
 */
public class MyProperties {
	private static MyProperties mMyProperties;
	/*
	 * 照片墙中照片保存的路径
	 */
	private String mPhotoWallDir;
	/*
	 * 家庭成员头像的保存路径
	 */
	private String mFamilyMemPhotoDir;

	private String mNewApkAddr;
	/*
	 * server ip
	 */
	private String mServerIp;
	/*
	 * 保存用户名到SharePreference中的key值
	 */
	private String mUsernameKey;
	/*
	 * 保存密码到SharePreference中的key值
	 */
	private String mPasswordKey;
	/*
	 * 保存金币数到SharePreference中的key值
	 */
	private String mCoinCountKey;
	/*
	 * 保存自动登录到SharePreference中的key值
	 */
	private String mAutoLoginKey;
	/*
	 * 保存是否隐身到SharePreference中的key值
	 */
	private String mIsVisibleKey;
	/*
	 * 保存位置上传的时间间隔到SharePreference中的key值
	 */
	private String mUploadIntervalTimeKey;
	/*
	 * 保存接收通知到SharePreference中的key值
	 */
	private String mReceiveNotifyKey;
	/*
	 * 保存声音提醒到SharePreference中的key值
	 */
	private String mSoundRemindKey;
	/*
	 * 保存我的位置到SharePreference中的key值
	 */
	private String mMyLocationAddrKey;
	/*
	 * 保存我的头像到SharePreference中的key值
	 */
	private String mMyPhotoUrlKey;
	private String mRequestMsgIntervalKey;
	
	private String mRemindUpdateKey;

	// 自定义
	private boolean isLoading;

	public static MyProperties getMyProperties() {
		if (mMyProperties == null) {
			mMyProperties = new MyProperties();
		}
		return mMyProperties;
	}

	private MyProperties() {
	}

	public void setPhotoWallDir(String photoWallDir) {
		mPhotoWallDir = photoWallDir;
	}

	public void setFamilyMemPhotoDir(String familyMemPhotoDir) {
		mFamilyMemPhotoDir = familyMemPhotoDir;
	}

	public void setServerIp(String serverip) {
		mServerIp = serverip;
	}

	public void setUsernameKey(String usernamekey) {
		mUsernameKey = usernamekey;
	}

	public void setPasswordKey(String passwkey) {
		mPasswordKey = passwkey;
	}

	public void setCoincountKey(String coincountkey) {
		mCoinCountKey = coincountkey;
	}

	public void setAutoLoginKey(String autologinkey) {
		mAutoLoginKey = autologinkey;
	}

	public void setIsvisibleKey(String isvisiblekey) {
		mIsVisibleKey = isvisiblekey;
	}

	public void setUploadIntervalTimeKey(String uploadIntervalTimekey) {
		mUploadIntervalTimeKey = uploadIntervalTimekey;
	}

	public void setReceiveNotifyKey(String receiveNotifyKey) {
		mReceiveNotifyKey = receiveNotifyKey;
	}

	public void setSoundRemingKey(String soundRemindKey) {
		mSoundRemindKey = soundRemindKey;
	}

	public void setMyLocationAddrKey(String myLocationAddrKey) {
		mMyLocationAddrKey = myLocationAddrKey;
	}

	public void setMyPhotoUrlKey(String MyPhotoUrlKey) {
		mMyPhotoUrlKey = MyPhotoUrlKey;
	}

	public void setRequestMsgIntervalKey(String requestMsgIntervalKey) {
		mRequestMsgIntervalKey = requestMsgIntervalKey;
	}

	public void setNewApkAddress(String path) {
		mNewApkAddr = path;
	}
	
	public void setRemindUpdateKey(String key) {
		mRemindUpdateKey = key;
	}

	public String getNewApkAddress() {
		return mNewApkAddr;
	}

	public String getServerIp() {
		return mServerIp;
	}

	public String getPhotoWallDir() {
		return mPhotoWallDir;
	}

	public String getFamilyMemPhotoDir() {
		return mFamilyMemPhotoDir;
	}

	public String getUsernameKey() {
		return mUsernameKey;
	}

	public String getPasswordKey() {
		return mPasswordKey;
	}

	public String getCoincountKey() {
		return mCoinCountKey;
	}

	public String getAutoLoginKey() {
		return mAutoLoginKey;
	}

	public String getIsvisibleKey() {
		return mIsVisibleKey;
	}

	public String getUploadIntervalTimeKey() {
		return mUploadIntervalTimeKey;
	}

	public String getReceiveNotifyKey() {
		return mReceiveNotifyKey;
	}

	public String getSoundRemindKey() {
		return mSoundRemindKey;
	}

	public String getMyLocationAddrKey() {
		return mMyLocationAddrKey;
	}

	public String getMyPhotoUrlKey() {
		return mMyPhotoUrlKey;
	}

	public String getRequestMsgIntervalKey() {
		return mRequestMsgIntervalKey;
	}
	
	public String getRemindUpdateKey() {
		return mRemindUpdateKey;
	}

	public boolean isLoading() {
		return isLoading;
	}

	public void setLoading(boolean isLoading) {
		this.isLoading = isLoading;
	}
}
