/**     
 * @Title: CacheManager.java  
 * @Package com.changhong.crazycat.cache  
 * @Description: TODO  
 * @author guoyang2011@gmail.com    
 * @date 2014-10-9 下午4:12:22  
 * @version V1.0     
*/  
package com.changhong.util.db.bean;  

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.aaa.db.AppDownloadState;
import com.aaa.db.AppUser;
import com.aaa.db.DownloadState;
import com.aaa.util.Constant;

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
	private volatile AppUser currentUser = null;
	private Map<String, ArrayList<AppDownloadState>> appDatas = new ConcurrentHashMap<String, ArrayList<AppDownloadState>>();
	private ArrayList<AppDownloadState> download = new ArrayList<AppDownloadState>();//本地数据
	
	public AppUser getCurrentUser() {
		return currentUser;
	}

	public synchronized void setCurrentUser(AppUser currentUser) {
		this.currentUser = currentUser;
	}

	public synchronized void clearAllData() {
		currentUser = null;
//		appDatas.clear();
//		download.clear();
	}
	
	public synchronized void addDownload(AppDownloadState ads){
		download.add(ads);
	}
	
	public synchronized void refreshDownloadItem(AppDownloadState ads){
		for(AppDownloadState item : download){
			if(item.getID() == ads.getID()){
				int index = download.indexOf(item);
				download.remove(item);
				download.add(index, ads);
				break;
			}
		}
	}
	
	public synchronized ArrayList<AppDownloadState> getDownload(){
		return download;
	}
	
	public synchronized void putAppData(AppDownloadState data){
		if(appDatas.containsKey(data.getTag())){
			boolean contain = false;
			for(AppDownloadState item : appDatas.get(data.getTag())){
				if(item.getID() == data.getID()){
					item.setDownloadState(data.getDownloadState());
					item.setProgress(data.getProgress());
					item.setPercent(data.getPercent());
					item.setSavePath(data.getSavePath());
					item.setSpeed(data.getSpeed());
					item.setName(data.getName());
					item.setLogoUrl(data.getLogoUrl());
					item.setTag(data.getTag());
					item.setOrder(data.getOrder());
					item.setType(data.getType());
					item.setSize(data.getSize());
					item.setDesc(data.getDesc());
					item.setPackageName(data.getPackageName());
					item.setDeveloper(data.getDeveloper());
					item.setVersion(data.getVersion());
					item.setDownloadUrl(data.getDownloadUrl());
					item.setDescUrl(data.getDescUrl());
					item.setOwner(data.getOwner());
					item.setDownCount(data.getDownCount());
					item.setUploadTime(data.getUploadTime());
					contain = true;
					break;
				}
			}
			if(! contain){
				appDatas.get(data.getTag()).add(data);
			}
		}else{
			ArrayList<AppDownloadState> list = new ArrayList<AppDownloadState>();
			list.add(data);
			appDatas.put(data.getTag(), list);
		}
		
	}
	
	public synchronized ArrayList<AppDownloadState> getAppData(String key){
		if(appDatas.containsKey(key)){
			return appDatas.get(key);
		}else{
			return new ArrayList<AppDownloadState>();
		}
	}
	
	public synchronized Map<String, ArrayList<AppDownloadState>> getAllAppData(){
		return appDatas;
	}
	
	public static final CacheManager INSTANCE=new CacheManager();
	private CacheManager(){}
	
}
