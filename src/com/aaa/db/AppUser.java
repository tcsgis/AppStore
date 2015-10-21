package com.aaa.db;

import java.io.Serializable;
import java.util.ArrayList;

import com.changhong.util.db.annotation.CHPrimaryKey;

public class AppUser implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@CHPrimaryKey
	private int ID;
	private String Name;
	private ArrayList<AppDetail> DownloadHistory;//下载、浏览历史
	private String Photo;//头像
	private byte Role;
	
	public void setID(int i){
		ID = i;
	}
	
	public int getID(){
		return ID;
	}
	
	public void setName(String i){
		Name = i;
	}
	
	public String getName(){
		return Name;
	}
	
	public void setPhoto(String i){
		Photo = i;
	}
	
	public String getPhoto(){
		return Photo;
	}
	
	public void setRole(byte i){
		Role = i;
	}
	
	public byte getRole(){
		return Role;
	}
	
	public void setDownloadHistory(ArrayList<AppDetail> i){
		DownloadHistory = i;
	}
	
	public ArrayList<AppDetail> getDownloadHistory(){
		return DownloadHistory;
	}
}
