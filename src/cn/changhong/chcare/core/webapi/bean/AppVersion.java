/**     
 * @Title: AppVersion.java  
 * @Package cn.changhong.chcare.core.webapi.bean  
 * @Description: TODO  
 * @author guoyang2011@gmail.com    
 * @date 2014-10-24 下午1:15:27  
 * @version V1.0     
*/  
package cn.changhong.chcare.core.webapi.bean;  

import java.io.Serializable;
import java.util.Date;
  
/**  
 * @ClassName: AppVersion  
 * @Description: TODO  
 * @author guoyang2011@gmail.com  
 * @date 2014-10-24 下午1:15:27  
 *     
 */
public class AppVersion implements Serializable{
	/**  
	 * @Fields serialVersionUID : TODO  
	*/
	private static final long serialVersionUID = 1L;
	private int ID;
	private int AppID;
	private String AppName;
	private int DevVer;
	private String UserVer;
	private String VserInfo;//更新日志
	private Date ReleaseTime;//发布时间
	private int AppOS;//区别手机型号
	private String DownLoadURL;
	private int MinVer;
	
	
	/**
	 * 当前服务器最小能工作的版本号
	 * @return
	 */
	public int getMinVer() {
		return MinVer;
	}
	public void setMinVer(int minVer) {
		MinVer = minVer;
	}
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public int getAppID() {
		return AppID;
	}
	public void setAppID(int appID) {
		AppID = appID;
	}
	public String getAppName() {
		return AppName;
	}
	public void setAppName(String appName) {
		AppName = appName;
	}
	public int getDevVer() {
		return DevVer;
	}
	public void setDevVer(int devVer) {
		DevVer = devVer;
	}
	public String getUserVer() {
		return UserVer;
	}
	public void setUserVer(String userVer) {
		UserVer = userVer;
	}
	public String getVserInfo() {
		return VserInfo;
	}
	public void setVserInfo(String vserInfo) {
		VserInfo = vserInfo;
	}
	public Date getReleaseTime() {
		return ReleaseTime;
	}
	public void setReleaseTime(Date releaseTime) {
		ReleaseTime = releaseTime;
	}
	public int getAppOS() {
		return AppOS;
	}
	public void setAppOS(int appOS) {
		AppOS = appOS;
	}
	public String getDownLoadURL() {
		return DownLoadURL;
	}
	public void setDownLoadURL(String downLoadURL) {
		DownLoadURL = downLoadURL;
	}
}
