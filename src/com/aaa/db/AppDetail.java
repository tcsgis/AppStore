package com.aaa.db;

import java.io.Serializable;
import java.util.ArrayList;

import com.changhong.util.db.annotation.CHPrimaryKey;

public class AppDetail  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@CHPrimaryKey
	private int ID;
	private String Name;
	private String LogoUrl;
	private byte Type;
	private float Size;//保留小数点后一位
	private String Desc;
	private String PackageName;
	private String Developer;
	private String Version;
	private String DownloadUrl;
	private ArrayList<String> DescUrl;
	
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
	
	public void setDesc(String i){
		Desc = i;
	}
	
	public String getDesc(){
		return Desc;
	}
	
	public void setVersion(String i){
		Version = i;
	}
	
	public String getVersion(){
		return Version;
	}
	
	public void setDownloadUrl(String i){
		DownloadUrl = i;
	}
	
	public String getDownloadUrl(){
		return DownloadUrl;
	}
	
	public void setDeveloper(String i){
		Developer = i;
	}
	
	public String getDeveloper(){
		return Developer;
	}
	
	public void setPackageName(String i){
		PackageName = i;
	}
	
	public String getPackageName(){
		return PackageName;
	}
	
	public void setLogoUrl(String i){
		LogoUrl = i;
	}
	
	public String getLogoUrl(){
		return LogoUrl;
	}
	
	public void setDescUrl(ArrayList<String> i){
		DescUrl = i;
	}
	
	public ArrayList<String> getDescUrl(){
		return DescUrl;
	}
	
	public void setType(byte i){
		Type = i;
	}
	
	public byte getType(){
		return Type;
	}
	
	public void setSize(float i){
		Size = i;
	}
	
	public float getSize(){
		return Size;
	}
}
