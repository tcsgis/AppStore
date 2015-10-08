/**     
 * @Title: ChCareFeedbackApi.java  
 * @Package cn.changhong.chcare.core.webapi  
 * @Description: TODO  
 * @author guoyang2011@gmail.com    
 * @date 2014-10-21 上午9:23:25  
 * @version V1.0     
*/  
package cn.changhong.chcare.core.webapi;  

import java.lang.reflect.Type;

import cn.changhong.chcare.core.webapi.bean.AppVersion;
import cn.changhong.chcare.core.webapi.bean.FeedbackBean;
import cn.changhong.chcare.core.webapi.bean.ResponseBean;
import cn.changhong.chcare.core.webapi.server.AAppManagerService;
import cn.changhong.chcare.core.webapi.util.HttpRequestException;

import com.google.gson.reflect.TypeToken;
  
/**  
 * @ClassName: ChCareFeedbackApi  
 * @Description: TODO  
 * @author guoyang2011@gmail.com  
 * @date 2014-10-21 上午9:23:25  
 *     
 */
public class ChCareAppManagerApi extends AAppManagerService{
	/**  
	 *   
	 *   
	 * @param feedback
	 * @return
	 * @throws HttpRequestException  
	 * @see cn.changhong.chcare.core.webapi.server.AFeedbackService#putFeedback(cn.changhong.chcare.core.webapi.bean.FeedbackBean)  
	*/  
	
	@Override
	public ResponseBean<?> putFeedback(FeedbackBean feedback)
			throws HttpRequestException {
		String url=BASE_URL+"Suggestion";
		return  this.postRequestUtil(url, this.gson.toJson(feedback));
	}

	/**  
	 *   
	 *   
	 * @param AppID
	 * @param Dev_Ver
	 * @param App_OS
	 * @return
	 * @throws HttpRequestException  
	 * @see cn.changhong.chcare.core.webapi.server.AAppManagerService#getAppLastVersionInfo(int, int, int)  
	*/  
	
	@Override
	public ResponseBean<?> getAppLastVersionInfo(int AppID, int Dev_Ver,
			int App_OS) throws HttpRequestException {
		String url=BASE_URL+"App?AppID="+AppID+"&Dev_Ver="+Dev_Ver+"&App_OS="+App_OS;
		String response = this.baseGetRequestUtil(url);
		ResponseBean<?> result = this.transToBean(response);
		if (result!=null&&result.getState() >= 0) {
			Type type = new TypeToken<ResponseBean<AppVersion>>() {
			}.getType();
			result = this.transToBean(response, type);
		}
		return result;
	}
}
