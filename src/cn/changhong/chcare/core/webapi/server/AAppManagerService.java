/**     
 * @Title: AAppManagerService.java  
 * @Package cn.changhong.chcare.core.webapi.server  
 * @Description: TODO  
 * @author guoyang2011@gmail.com    
 * @date 2014-10-20 下午6:11:40  
 * @version V1.0     
*/  
package cn.changhong.chcare.core.webapi.server;  

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import cn.changhong.chcare.core.webapi.AbstractChCareWebApi;
import cn.changhong.chcare.core.webapi.bean.FeedbackBean;
import cn.changhong.chcare.core.webapi.bean.ResponseBean;
import cn.changhong.chcare.core.webapi.handler.AsyncResponseCompletedHandler;
import cn.changhong.chcare.core.webapi.util.HttpRequestException;
  
/**  
 * @ClassName: AFeedbackService  
 * @Description: TODO  
 * @author guoyang2011@gmail.com  
 * @date 2014-10-20 下午6:11:40  
 *     
 */
public abstract class AAppManagerService  extends AbstractChCareWebApi implements
IService{
	/**
	 * 上传用户反馈信息
	 * @Title: putFeedback  
	 * @Description: TODO  
	 * @param @param feedback 反馈意见的bean
	 * @param @return
	 * @param @throws HttpRequestException      
	 * @return {@code ResponseBean<AppVersion>} 
	 * @throws
	 */
	public abstract ResponseBean<?> putFeedback(FeedbackBean feedback) throws HttpRequestException;
	/**
	 * 
	 * @Title: putFeedback  
	 * @Description: TODO  
	 * @param @param feedback 反馈意见的bean
	 * @param @param handler 
	 * @param @return      
	 * @return {@code Future<ResponseBean<AppVersion>>}
	 * @throws
	 */
	public <T> Future<ResponseBean<?>> putFeedback(final FeedbackBean feedback,final AsyncResponseCompletedHandler<T> handler){
		Future<ResponseBean<?>> future = executorProvider
				.doTask(new Callable<ResponseBean<?>>() {

					@Override
					public ResponseBean<?> call() {
						ResponseBean<?> bean = null;
						try {
							bean = putFeedback(feedback);
							handler.onCompleted(bean,ChCareWepApiServiceType.WebApi_AppManager_putFeedback_Service);
						} catch (HttpRequestException e) {
							handler.onThrowable(e,ChCareWepApiServiceType.WebApi_AppManager_putFeedback_Service);
						}
						return bean;
					}
				});
		return future;
	}
	/**
	 * 获取App最新版本
	 * @Title: getAppLastVersionInfo  
	 * @Description: TODO  
	 * @param @param AppID AppId
	 * @param @param Dev_Ver 开发者版本
	 * @param @param App_OS 平台：1代表android,2代表ISO,3代表WPhone
	 * @param @return
	 * @param @throws HttpRequestException      
	 * @return {@code ResponseBean<?> }
	 * @throws
	 */
	public abstract ResponseBean<?> getAppLastVersionInfo(int AppID,int Dev_Ver,int App_OS) throws HttpRequestException;
	/**
	 * 
	 * @Title: getAppLastVersionInfo  
	 * @Description: TODO  
	 * @param @param AppID
	 * @param @param Dev_Ver
	 * @param @param App_OS
	 * @param @param handler
	 * @param @return      
	 * @return {@code Future<ResponseBean<?>>} 
	 * @throws
	 */
	public <T> Future<ResponseBean<?>>  getAppLastVersionInfo(final int AppID,final int Dev_Ver,final int App_OS,final AsyncResponseCompletedHandler<T> handler){
		Future<ResponseBean<?>> future = executorProvider
				.doTask(new Callable<ResponseBean<?>>() {

					@Override
					public ResponseBean<?> call() {
						ResponseBean<?> bean = null;
						try {
							bean = getAppLastVersionInfo(AppID,Dev_Ver,App_OS);
							handler.onCompleted(bean,ChCareWepApiServiceType.WebApi_getAppLastVersionInfo_putFeedback_Service);
						} catch (HttpRequestException e) {
							handler.onThrowable(e,ChCareWepApiServiceType.WebApi_getAppLastVersionInfo_putFeedback_Service);
						}
						return bean;
					}
				});
		return future;
	}
}
