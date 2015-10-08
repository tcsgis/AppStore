package cn.changhong.chcare.core.webapi.bean;

import java.io.Serializable;
import java.util.Date;

public class ThirdShare implements Serializable{
	/**  
	 * @Fields serialVersionUID : TODO  
	*/
	private static final long serialVersionUID = 1L;
	private int ID;
	private String UserID;
	private int ShareType = 0;
	private Date SharedTime = new Date(System.currentTimeMillis());
	private String ContentType;
	private String SharedContent = "hello world";

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

	public int getShareType() {
		return ShareType;
	}

	public void setShareType(int shareType) {
		ShareType = shareType;
	}

	public Date getSharedTime() {
		return SharedTime;
	}

	public void setSharedTime(Date sharedTime) {
		SharedTime = sharedTime;
	}

	public String getContentType() {
		return ContentType;
	}

	public void setContentType(String contentType) {
		ContentType = contentType;
	}

	public String getSharedContent() {
		return SharedContent;
	}

	public void setSharedContent(String sharedContent) {
		SharedContent = sharedContent;
	}

}
