/**     
 * @Title: User.java  
 * @Package com.changhong.crazycat.db.sqlite.entity  
 * @Description: TODO  
 * @author guoyang2011@gmail.com    
 * @date 2014-9-25 上午9:23:15  
 * @version V1.0     
*/  
package cn.changhong.chcare.core.webapi.bean;  

import java.io.Serializable;
import java.util.List;

import cn.changhong.chcare.core.webapi.photowalll.bean.PhotoView;

import com.changhong.util.db.annotation.CHPrimaryKey;
import com.changhong.util.db.annotation.CHTransient;
  
/**  
 * 每个用户的APP配置信息实体类
 * @ClassName: UserAppConfig  
 * @Description: TODO  
 * @author guoyang2011@gmail.com  
 * @date 2014-9-25 上午9:23:15  
 *     
 */
public class UserAppConfig implements Serializable{

	/**  
	 * @Fields serialVersionUID : TODO  
	*/
	@CHTransient
	private static final long serialVersionUID = 1L;
	@CHPrimaryKey()
	private long userID;//用户ID
	private int locationUpdateTimer = 300000;//定时上传位置的时间间隔,单位毫秒
	private int offlineMsgDownloadTimer;
	private int offlineMsgLastDownloadIndex;//消息更新的最后一条ID
	private long offlineMsgLastUpdateTimestamp;//最近一次获取消息的时间戳
	private boolean isvisible = true;//隐身是否可见
	
	

	public boolean getIsvisible() {
		return isvisible;
	}
	public void setIsvisible(boolean isvisible) {
		this.isvisible = isvisible;
	}
	public long getUserID() {
		return userID;
	}
	public void setUserID(long userID) {
		this.userID = userID;
	}
	public int getLocationUpdateTime() {
		return locationUpdateTimer;
	}
	public void setLocationUpdateTime(int locationUpdateTime) {
		this.locationUpdateTimer = locationUpdateTime;
	}
	public int getOfflineMsgDownloadTime() {
		return offlineMsgDownloadTimer;
	}
	public void setOfflineMsgDownloadTime(int offlineMsgDownloadTime) {
		this.offlineMsgDownloadTimer = offlineMsgDownloadTime;
	}
	public int getOfflineMsgLastDownloadIndex() {
		return offlineMsgLastDownloadIndex;
	}
	public void setOfflineMsgLastDownloadIndex(int offlineMsgLastDownloadIndex) {
		this.offlineMsgLastDownloadIndex = offlineMsgLastDownloadIndex;
	}
	public long getOfflineMsgLastUpdateTimestamp() {
		return offlineMsgLastUpdateTimestamp;
	}
	public void setOfflineMsgLastTimestamp(long offlineMsgLastTimestamp) {
		this.offlineMsgLastUpdateTimestamp = offlineMsgLastTimestamp;
	}

	// 自定义
	@CHTransient
	private List<PhotoView> mPhotoViewList;

	public List<PhotoView> getPhotoViewList() {
		return mPhotoViewList;
	}

	public void setPhotoViewList(List<PhotoView> list) {
		mPhotoViewList = list;
	}

}
