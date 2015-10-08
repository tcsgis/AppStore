/**     
 * @Title: ChCareWepApiServiceType.java  
 * @Package cn.changhong.chcare.core.webapi.server  
 * @Description: TODO  
 * @author guoyang2011@gmail.com    
 * @date 2014-9-27 下午2:07:16  
 * @version V1.0     
*/  
package cn.changhong.chcare.core.webapi.server;  
  
/**  
 * @ClassName: ChCareWepApiServiceType  
 * @Description: TODO  
 * @author guoyang2011@gmail.com  
 * @date 2014-9-27 下午2:07:16  
 *     
 */
public enum ChCareWepApiServiceType {
	
	WebApi_OfflineMsg_getUserOfflineMessage_Service("getUserOfflineMessage"),
	WebApi_OfflineMsg_pollingMessage_Service("pollingMessage"),
	WebApi_OfflineMsg_markMessage_Service("markMessage"),
	
	WebApi_Notify_getCommentsMentionedMe("getCommentsMentionedMe"),
	
	WebApi_AppManager_putFeedback_Service("AppManager_putFeedback"),
	WebApi_getAppLastVersionInfo_putFeedback_Service("getAppLastVersionInfo_putFeedback");
	
	
	 private String value;
	 private ChCareWepApiServiceType(String value){
		 this.value=value;
	 }
	 public String getValue(){
		 return value;
	 }
	
	
}
