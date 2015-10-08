/**     
 * @Title: ENetworkStatus.java  
 * @Package cn.changhong.chcare.cache  
 * @Description: TODO  
 * @author guoyang2011@gmail.com    
 * @date 2014-9-18 下午3:55:58  
 * @version V1.0     
 */
package cn.changhong.chcare.core.webapi.util;

/**
 * @ClassName: ENetworkStatus
 * @Description: TODO
 * @author guoyang2011@gmail.com
 * @date 2014-9-18 下午3:55:58
 * 
 */
public enum ENetworkStatus {
	GOOD("good"), NORMAL("normal"), BAD("bad");
	private String value;

	private ENetworkStatus(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
}
