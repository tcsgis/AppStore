/**     
 * @Title: NetworkStatusType.java  
 * @Package cn.changhong.chcare.cache  
 * @Description: TODO  
 * @author guoyang2011@gmail.com    
 * @date 2014-9-18 下午3:47:24  
 * @version V1.0     
 */
package cn.changhong.chcare.core.webapi.util;

/**
 * @ClassName: NetworkStatusType
 * @Description: TODO
 * @author guoyang2011@gmail.com
 * @date 2014-9-18 下午3:47:24
 * 
 */
public enum ENetworkType {
	GPRS_TYPE("GENERAL_PACKET_RADIO_SERVICE"), WIFI_TYPE("WIFI"), NO_CONNECTION_TYPE(
			"no_connection_type");
	private String value;

	private ENetworkType(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
}
