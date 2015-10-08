/**     
 * @Title: FeedbackBean.java  
 * @Package cn.changhong.chcare.core.webapi.bean  
 * @Description: TODO  
 * @author guoyang2011@gmail.com    
 * @date 2014-10-20 下午6:15:09  
 * @version V1.0     
*/  
package cn.changhong.chcare.core.webapi.bean;  

import java.io.Serializable;
import java.util.Date;
  
/**  
 * @ClassName: FeedbackBean  
 * @Description: TODO  
 * @author guoyang2011@gmail.com  
 * @date 2014-10-20 下午6:15:09  
 *     
 */
public class FeedbackBean implements Serializable{
	/**  
	 * @Fields serialVersionUID : TODO  
	*/
	private static final long serialVersionUID = 1L;
	private int ID;//服务器端设置
	private int UserID;//上传用户ID
	private int APPID;//APP的ID号
	
	private int SugType;//意见类型
	private String UserSug;//意见内容
	private Date SugTime;//上传时间，服务器端设置
	private int DevVer;//开发者版本号
	private boolean isRead;//是否处理
	private int AppOS;//APP的平台类型
	
	private String OSVer;
	

	public String getOSVer() {
		return OSVer;
	}
	public void setOSVer(String oSVer) {
		OSVer = oSVer;
	}
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public int getAPPID() {
		return APPID;
	}
	public void setAPPID(int aPPID) {
		APPID = aPPID;
	}
	public int getUserID() {
		return UserID;
	}
	public void setUserID(int userID) {
		UserID = userID;
	}
	public int getSugType() {
		return SugType;
	}
	public void setSugType(int sugType) {
		SugType = sugType;
	}
	public String getUserSug() {
		return UserSug;
	}
	public void setUserSug(String userSug) {
		UserSug = userSug;
	}
	public Date getSugTime() {
		return SugTime;
	}
	public void setSugTime(Date sugTime) {
		SugTime = sugTime;
	}
	public int getDevVer() {
		return DevVer;
	}
	public void setDevVer(int devVer) {
		DevVer = devVer;
	}
	public boolean isRead() {
		return isRead;
	}
	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}
	public int getAppOS() {
		return AppOS;
	}
	public void setAppOS(int appOS) {
		AppOS = appOS;
	}
	
	
}
