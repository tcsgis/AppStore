package cn.changhong.chcare.core.webapi;

import java.util.HashMap;
import java.util.Map;

import com.aaa.db.AppDetail;
import com.aaa.db.AppUser;
import com.aaa.util.MyTools;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import cn.changhong.chcare.core.webapi.bean.ResponseBean;
import cn.changhong.chcare.core.webapi.server.IAdminService;
import cn.changhong.chcare.core.webapi.util.HttpRequestException;
import cn.changhong.chcare.core.webapi.util.TokenManager;

public class AdminApi extends IAdminService{

	@Override
	public ResponseBean uploadAppDetail(AppDetail app)
			throws HttpRequestException {
		
		String url;
		if(app.getID() != 0){
			url = BASE_URL + "app?id=" + app.getID();
		}else{
			url = BASE_URL + "app";
		}
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("Name", app.getName());
		params.put("LogoUrl", app.getLogoUrl());
		params.put("Tag", app.getTag());
		params.put("Order", app.getOrder());
		params.put("Type", app.getType());
		params.put("Size", app.getSize());
		params.put("Desc", app.getDesc());
		params.put("PackageName", app.getPackageName());
		params.put("Developer", app.getDeveloper());
		params.put("Version", app.getVersion());
		params.put("DownloadUrl", app.getDownloadUrl());
		params.put("DescUrl", MyTools.composePhotos(app.getDescUrl()));
		
//		return this.postRequestUtil(url, this.gson.toJson(params));
		
		String response = this.basePostRequestUtil(url, this.gson.toJson(params));
		ResponseBean<?> result = this.transToBean(response);
		
		if (result != null && result.getState() >= 0) {

			ResponseBean<Integer> return_resultBean = new ResponseBean<Integer>();
			
			JsonParser parser = new JsonParser();
			JsonElement jsonEl = parser.parse(response);

			JsonObject jsonObj = jsonEl.getAsJsonObject().getAsJsonObject("Data");// 转换成Json对象
			Integer id = jsonObj.get("Id").getAsInt();
			
			return_resultBean.setData(id);
			return_resultBean.setState(result.getState());
			return_resultBean.setDesc(result.getDesc());
			
			result = return_resultBean;
		}
		return result;
	}

	@Override
	public ResponseBean deleteApp(int id) throws HttpRequestException {
		
		String url = BASE_URL + "app?id=" + id;
		return this.deleteRequestUtil(url);
	}

}
