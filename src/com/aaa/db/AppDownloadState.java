package com.aaa.db;

import com.changhong.util.CHLogger;
import com.changhong.util.download.DownloadManager;

public class AppDownloadState extends AppDetail implements Cloneable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private byte DownloadState;// DownloadState
	private long Progress;// 进度
	private float Percent;//进度百分比
	private String SavePath;
	private int Speed;//KB/s

	public AppDownloadState(){
		
	}
	
	public AppDownloadState(AppDetail app) {
		setID(app.getID());
		setName(app.getName());
		setLogoUrl(app.getLogoUrl());
		setTag(app.getTag());
		setOrder(app.getOrder());
		setType(app.getType());
		setSize(app.getSize());
		setDesc(app.getDesc());
		setPackageName(app.getPackageName());
		setDeveloper(app.getDeveloper());
		setVersion(app.getVersion());
		setDownloadUrl(app.getDownloadUrl());
		setDescUrl(app.getDescUrl());
		setSavePath(DownloadManager.getDownloadManager().getSavePath(app.getDownloadUrl()));
	}

	public void setProgress(long b) {
		Progress = b;
	}

	public long getProgress() {
		return Progress;
	}

	public void setDownloadState(byte b) {
		DownloadState = b;
	}

	public byte getDownloadState() {
		return DownloadState;
	}

	public void setSavePath(String b) {
		SavePath = b;
	}

	public String getSavePath() {
		return SavePath;
	}

	public void setSpeed(int b) {
		Speed = b;
	}

	public int getSpeed() {
		return Speed;
	}
	
	public void setPercent(float b) {
		Percent = b;
	}
	
	public float getPercent() {
		return Percent;
	}
	
	public AppDownloadState clone() {
		AppDownloadState o = null;
		try {
			o = (AppDownloadState) super.clone();
		} catch (CloneNotSupportedException e) {
			CHLogger.e(this, e);
		}
		return o;
	}
}
