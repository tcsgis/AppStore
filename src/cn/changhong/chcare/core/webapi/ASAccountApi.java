package cn.changhong.chcare.core.webapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aaa.db.AppDetail;
import com.aaa.db.AppUser;
import com.aaa.util.Role;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import cn.changhong.chcare.core.webapi.bean.ResponseBean;
import cn.changhong.chcare.core.webapi.bean.ResponseBeanWithRange;
import cn.changhong.chcare.core.webapi.server.IASAccountService;
import cn.changhong.chcare.core.webapi.util.HttpRequestException;
import cn.changhong.chcare.core.webapi.util.TokenManager;

public class ASAccountApi extends IASAccountService{

	@Override
	public ResponseBean<?> getVetifyCode(String phoneNumber, int type, byte role)
			throws HttpRequestException {
		
		String url = BASE_URL + "Account/Vfy?username=" + phoneNumber + "&type=" + type + "&role=" + role;
		return this.getRequestUtil(url);
	}

	@Override
	public ResponseBean<?> verifyCode(String username, String code, int type)
			throws HttpRequestException {
		
		String url = BASE_URL + "Account/Vfy";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("UserName", username);
		params.put("VerifyCode", code);
		params.put("Type", type);
		return this.postRequestUtil(url, this.gson.toJson(params));
	}

	@Override
	public ResponseBean<?> login(String username, String verifyCode)
			throws HttpRequestException {
		String url = BASE_URL + "Token?v=2&misc=UserInfo";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Name", username);
		if(verifyCode != null){
//			params.put("CurPwd", verifyCode);
			params.put("VerifyCode", verifyCode);
			params.put("ClientId", 1);
			params.put("PwdMode", 2);
			/*
			 * Role != 0 => 用你给的Role登录/注册
		   Role == 0 => 用任何可能的身份登录/用普通用户身份注册
			 */
			params.put("Role", Role.UNDIFINED);
		}
		
		String response = this.basePostRequestUtil(url, this.gson.toJson(params));
		ResponseBean<?> result = this.transToBean(response);
		
		if (result != null && result.getState() >= 0) {

			ResponseBean<AppUser> return_resultBean = new ResponseBean<AppUser>();
			
			JsonParser parser = new JsonParser();
			JsonElement jsonEl = parser.parse(response);

			JsonObject jsonObj = jsonEl.getAsJsonObject().getAsJsonObject("Data");// 转换成Json对象

			TokenManager.setToken(jsonObj.getAsJsonPrimitive("Token").getAsString());
			TokenManager.setFiletoken(jsonObj.getAsJsonPrimitive("FileToken").getAsString());
			jsonObj = jsonObj.getAsJsonObject("Extra");// 转换成Json对象
			JsonObject userJs = jsonObj.getAsJsonObject("UserInfo");
			
			AppUser user = new AppUser();
			user = transToAppUser(userJs);
			
			return_resultBean.setData(user);
			return_resultBean.setState(result.getState());
			return_resultBean.setDesc(result.getDesc());
			
			result = return_resultBean;
		}
		return result;
	}

	@Override
	public ResponseBean<?> logout() throws HttpRequestException {
		String url = BASE_URL + "Token";
		return this.deleteRequestUtil(url);
	}

	@Override
	public ResponseBean updateSelfMg(AppUser user) throws HttpRequestException {
		String url = BASE_URL + "User";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Photo", user.getPhoto());
		
		return this.postRequestUtil(url, this.gson.toJson(params));
	}

	@Override
	public ResponseBean getApps(String tag) throws HttpRequestException {
		String url;
		if(tag == null){
			url = BASE_URL + "app";
		}else{
			url = BASE_URL + "app?tag=" + tag;
		}
		
		String response = this.baseGetRequestUtil(url);
		ResponseBeanWithRange<?> result = transToRangeBean(response);
		
		try {
			if (result!=null && result.getState() >= 0) {
				ResponseBeanWithRange<List<AppDetail>> return_resultBean = new ResponseBeanWithRange<List<AppDetail>>();
				ArrayList<AppDetail> users = transToAppDetailList(response);
				
				return_resultBean.setData(users);
				return_resultBean.setState(result.getState());
				return_resultBean.setDesc(result.getDesc());
				
				result = return_resultBean;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	@Override
	public ResponseBean uploadUserOperation(int[] ids)
			throws HttpRequestException {
		
		String url = BASE_URL + "app/stat";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("AppIds", ids);
		return this.postRequestUtil(url, this.gson.toJson(params));
	}

}
