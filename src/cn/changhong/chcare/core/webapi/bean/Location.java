package cn.changhong.chcare.core.webapi.bean;

import java.io.Serializable;
import java.util.Date;

import com.changhong.util.db.annotation.CHPrimaryKey;
import com.changhong.util.db.annotation.CHTransient;

public class Location implements Serializable{
	/**  
	 * @Fields serialVersionUID : TODO  
	*/
	@CHTransient
	private static final long serialVersionUID = 1L;
	@CHPrimaryKey()
	private int ID;
	private String UserID;
	private int Type;
	private double Lng ;
	private double Lat ;
	private String Addr ;
	private Date GPSTime = new Date(System.currentTimeMillis());
	private Date CurTime;

	public Date getGPSTime() {
		return GPSTime;
	}

	public void setGPSTime(Date gPSTime) {
		GPSTime = gPSTime;
	}

	public Date getCurTime() {
		return CurTime;
	}

	public void setCurTime(Date curTime) {
		CurTime = curTime;
	}

	private boolean IsVisible = true;

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getUserID() {
		return UserID;
	}

	public void setUserID(String userID) {
		UserID = userID;
	}

	public int getType() {
		return Type;
	}

	public void setType(int type) {
		Type = type;
	}

	public double getLng() {
		return Lng;
	}

	public void setLng(double lng) {
		Lng = lng;
	}

	public double getLat() {
		return Lat;
	}

	public void setLat(double lat) {
		Lat = lat;
	}

	public String getAddr() {
		return Addr;
	}

	public void setAddr(String addr) {
		Addr = addr;
	}

	public boolean isIsVisible() {
		return IsVisible;
	}

	public void setIsVisible(boolean isVisible) {
		IsVisible = isVisible;
	}

}
