package cn.changhong.chcare.core.webapi.bean;

import java.io.Serializable;
import java.util.Date;

public class BBSView implements Serializable{
	/**  
	 * @Fields serialVersionUID : TODO  
	*/
	private static final long serialVersionUID = 1L;
	private int ID;
	private int UID;
	private int UName;
	private int CID;
	private String Title;
	private String Content;
	private double Lng;
	private double lat;
	private String Pics;
	private int CommentCnt;
	private boolean IsAnonymous;
	private Date Time;
	private long Timestamp;
	private boolean IsFavor;
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public int getUID() {
		return UID;
	}
	public void setUID(int uID) {
		UID = uID;
	}
	public int getUName() {
		return UName;
	}
	public void setUName(int uName) {
		UName = uName;
	}
	public int getCID() {
		return CID;
	}
	public void setCID(int cID) {
		CID = cID;
	}
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}
	public double getLng() {
		return Lng;
	}
	public void setLng(double lng) {
		Lng = lng;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public String getPics() {
		return Pics;
	}
	public void setPics(String pics) {
		Pics = pics;
	}
	public int getCommentCnt() {
		return CommentCnt;
	}
	public void setCommentCnt(int commentCnt) {
		CommentCnt = commentCnt;
	}
	public boolean isIsAnonymous() {
		return IsAnonymous;
	}
	public void setIsAnonymous(boolean isAnonymous) {
		IsAnonymous = isAnonymous;
	}
	public Date getTime() {
		return Time;
	}
	public void setTime(Date time) {
		Time = time;
	}
	public long getTimestamp() {
		return Timestamp;
	}
	public void setTimestamp(long timestamp) {
		Timestamp = timestamp;
	}
	public boolean isIsFavor() {
		return IsFavor;
	}
	public void setIsFavor(boolean isFavor) {
		IsFavor = isFavor;
	}
	
}
