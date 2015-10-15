package com.aaa.db;

public class AppDownloadState extends AppDetail{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private byte DownloadState;//DownloadState
	private float Progress;//进度
	private String SavePath;
	private int Speed;
	
	public void setProgress(float b){
		Progress = b;
	}
	
	public float getProgress(){
		return Progress;
	}
	
	public void setDownloadState(byte b){
		DownloadState = b;
	}
	
	public byte getDownloadState(){
		return DownloadState;
	}
	
	public void setSavePath(String b){
		SavePath = b;
	}
	
	public String getSavePath(){
		return SavePath;
	}
	
	public void setSpeed(int b){
		Speed = b;
	}
	
	public int getSpeed(){
		return Speed;
	}
}
