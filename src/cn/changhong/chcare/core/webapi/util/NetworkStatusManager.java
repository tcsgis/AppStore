/**     
 * @Title: NetworkStatusManager.java  
 * @Package cn.changhong.chcare.core.webapi  
 * @Description: TODO  
 * @author guoyang2011@gmail.com    
 * @date 2014-9-18 下午4:11:18  
 * @version V1.0     
 */
package cn.changhong.chcare.core.webapi.util;

/**
 * @ClassName: NetworkStatusManager
 * @Description: TODO
 * @author guoyang2011@gmail.com
 * @date 2014-9-18 下午4:11:18
 * 
 */
public class NetworkStatusManager {
	private ENetworkStatus networkStatus;
	private ENetworkType networkType;

	public synchronized ENetworkStatus getNetworkStatus() {
		return networkStatus;
	}

	public synchronized void setNetworkStatus(ENetworkStatus networkStatus) {
		this.networkStatus = networkStatus;
	}

	public synchronized ENetworkType getNetworkType() {
		return networkType;
	}

	public synchronized void setNetworkType(ENetworkType networkType) {
		this.networkType = networkType;
	}
}
