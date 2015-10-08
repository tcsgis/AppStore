/**     
 * @Title: DefaultCacheProvider.java  
 * @Package cn.changhong.chcare.cache  
 * @Description: TODO  
 * @author guoyang2011@gmail.com    
 * @date 2014-9-18 下午3:39:17  
 * @version V1.0     
 */
package cn.changhong.chcare.core.webapi.util;

/**
 * @ClassName: DefaultCacheProvider
 * @Description: TODO
 * @author guoyang2011@gmail.com
 * @date 2014-9-18 下午3:39:17
 * 
 */
public class DefaultCacheProvider {
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
