/**     
 * @Title: CacheManager.java  
 * @Package com.changhong.crazycat.cache  
 * @Description: TODO  
 * @author guoyang2011@gmail.com    
 * @date 2014-10-9 下午4:12:22  
 * @version V1.0     
*/  
package com.changhong.util.db.bean;  

import com.aaa.db.AppUser;

/**  
 * 
 * @ClassName: CacheManager  
 * @Description: TODO  
 * @author cxp  
 * @date 2014-10-9 下午4:12:22  
 *     
 */
public class CacheManager {
	//账号相关的基本信息
	private volatile AppUser currentUser=null;
	
	
	public AppUser getCurrentUser() {
		return currentUser;
	}

	public synchronized void setCurrentUser(AppUser currentUser) {
		this.currentUser = currentUser;
	}

	public synchronized void clearAllData() {
		currentUser = null;
	}
	
	public static final CacheManager INSTANCE=new CacheManager();
	private CacheManager(){}
	
}
