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
	private String Name;//资源名字
	private String LogoUrl;//logo
	private String Tag;//分类//小标签
	private int Order;//排列的顺序;
	private byte Type;//apk（分为游戏、应用====）、网页、书籍，====//能否下载
	private float Size;//保留小数点后一位，单位M
	private String Desc;//详细描述
	private String PackageName;//apk的包名
	private String Developer;//开发者
	private String Version;
	private String DownloadUrl;//下载地址、或者网页链接
	private ArrayList<String> DescUrl;//描述图片，最多5张，最少3张
	private int Owner;//(内部)上传者Id
	private int DownCount;
	private String UploadTime;
	
	public void setID(int i){
		ID = i;
	}
	
	public int getID(){
		return ID;
	}
	
	public void setOrder(int i){
		Order = i;
	}
	
	public int getOrder(){
		return Order;
	}
	
	public void setUploadTime(String i){
		UploadTime = i;
	}
	
	public String getUploadTime(){
		return UploadTime;
	}
	
	public void setDownCount(int i){
		DownCount = i;
	}
	
	public int getDownCount(){
		return DownCount;
	}
	
	public void setOwner(int i){
		Owner = i;
	}
	
	public int getOwner(){
		return Owner;
	}
	
	public void setName(String i){
		Name = i;
	}
	
	public String getName(){
		return Name;
	}
	
	public void setTag(String i){
		Tag = i;
	}
	
	public String getTag(){
		return Tag;
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
