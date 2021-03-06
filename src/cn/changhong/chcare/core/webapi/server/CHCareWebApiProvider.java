package cn.changhong.chcare.core.webapi.server;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import cn.changhong.chcare.core.webapi.ASAccountApi;
import cn.changhong.chcare.core.webapi.AdminApi;
import cn.changhong.chcare.core.webapi.ChCareAppManagerApi;
import cn.changhong.chcare.core.webapi.ChCareWebApiAccountnApi;
import cn.changhong.chcare.core.webapi.ChCareWebApiOfflineMessageApi;
import cn.changhong.chcare.core.webapi.FileApi;

public class CHCareWebApiProvider {

	private static final Map<WebApiServerType, IService> currentServerMap = new WeakHashMap<WebApiServerType, IService>();
	private static final Map<WebApiServerType, Class<? extends IService>> defaultServiceMap = new HashMap<WebApiServerType, Class<? extends IService>>();

	private CHCareWebApiProvider() {
	}

	public static class Self {
		private static CHCareWebApiProvider instance;

		public static CHCareWebApiProvider defaultInstance() {
			if (instance == null) {
				instance = new CHCareWebApiProvider();
			}
			return instance;
		}
	}

	static {
		defaultServiceMap.put(WebApiServerType.CHCARE_OFFLINEMESSAGE_SERVER,
				ChCareWebApiOfflineMessageApi.class);
		defaultServiceMap.put(WebApiServerType.CHCARE_CHCAREAPPMANAGERAPI_SERVER,
				ChCareAppManagerApi.class);
		defaultServiceMap.put(WebApiServerType.CHCARE_ACCOUNT_SERVER,
				ChCareWebApiAccountnApi.class);
		defaultServiceMap.put(WebApiServerType.AS_ACCOUNT_SERVER,
				ASAccountApi.class);
		defaultServiceMap.put(WebApiServerType.FILE_SERVER,
				FileApi.class);
		defaultServiceMap.put(WebApiServerType.ADMIN_SERVER,
				AdminApi.class);
	}

	public static CHCareWebApiProvider newInstance() {
		return Self.defaultInstance();
	}

	public static enum WebApiServerType {
		 CHCARE_OFFLINEMESSAGE_SERVER("offlinemessageservice"), 
		 CHCARE_ACCOUNT_SERVER("CHAccountService"), 
		 AS_ACCOUNT_SERVER("AS_ACCOUNT_SERVER"), 
		 FILE_SERVER("FILE_SERVER"), 
		 ADMIN_SERVER("ADMIN_SERVER"), 
		 CHCARE_CHCAREAPPMANAGERAPI_SERVER("appmanagerserver");
		private String value;

		private WebApiServerType(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}

	public IService getDefaultWebApiService(WebApiServerType type) {
		IService result = currentServerMap.get(type);
		if (result == null) {
			Class<?> serviceClass = defaultServiceMap.get(type);
			if (serviceClass != null) {
				try {
					result = (IService) serviceClass.getConstructors()[0]
							.newInstance((Object[]) null);
					synchronized (currentServerMap) {
						currentServerMap.put(type, result);
					}
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return result;
	}

}
