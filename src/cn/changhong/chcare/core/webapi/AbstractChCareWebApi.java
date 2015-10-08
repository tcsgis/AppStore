package cn.changhong.chcare.core.webapi;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import cn.changhong.chcare.core.webapi.bean.CHCareFileInStream;
import cn.changhong.chcare.core.webapi.bean.ResponseBean;
import cn.changhong.chcare.core.webapi.bean.ResponseBeanWithRange;
import cn.changhong.chcare.core.webapi.server.ChCareWebApiRequestErrorType;
import cn.changhong.chcare.core.webapi.util.HttpRequestException;
import cn.changhong.chcare.core.webapi.util.HttpsConnectionManager;
import cn.changhong.chcare.core.webapi.util.IHttpRestApi;
import cn.changhong.chcare.core.webapi.util.MultipartUtility;
import cn.changhong.chcare.core.webapi.util.TokenManager;
import cn.changhong.chcare.core.webapi.util.WebApiExecutorProvider;

import com.changhong.BuildConfig;
import com.changhong.util.CHLogger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class AbstractChCareWebApi {
	protected final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	protected final static String charset = "utf-8";
	public static String BASE_URL = "http://182.92.165.152:9081/api/";
	public void setServerUrl(URL url){
		BASE_URL=url.toString();
	}
	public URL getServerUrl(){
		URL result=null;
		try {
			result= new URL(BASE_URL);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return result;
	}
	protected IHttpRestApi httpRequestHandler = HttpsConnectionManager.Self
			.defaultInstance();
	protected WebApiExecutorProvider executorProvider = WebApiExecutorProvider.Self
			.defaultInstance();

	protected ResponseBean<?> transToBean(String jsonStr) throws HttpRequestException {
		return transToBean(jsonStr, ResponseBean.class);
	}

	protected ResponseBeanWithRange<?> transToRangeBean(String jsonStr) throws HttpRequestException {
		return transToRangeBean(jsonStr, ResponseBeanWithRange.class);
	}

	protected ResponseBean<?> transToBean(String jsonStr, Type type)
			throws HttpRequestException {
		ResponseBean<?> result = (ResponseBean<?>)transToRaw(jsonStr, type);
		return result;
	}

	protected ResponseBeanWithRange<?> transToRangeBean(String jsonStr, Type type)
			throws HttpRequestException {
		ResponseBeanWithRange<?> result = (ResponseBeanWithRange<?>)transToRaw(jsonStr, type);
		return result;
	}

	protected Object transToRaw(String jsonStr, Type type)
			throws HttpRequestException {
		if (jsonStr != null) {
			try {
				return gson.fromJson(jsonStr, type);
			} catch (Exception e) {
				String msg = "Transform JsonString Value[" + safeWrapResponse(jsonStr)
						+ "] To Type [" + type.toString() + "] Error!";
				throw new HttpRequestException(
						msg,
						ChCareWebApiRequestErrorType.CHCAREWEBAPI_TRANSFORM_DATA_ERROR);
			}/* finally {
				try {
					jsonStr.close();
				} catch (IOException ignored) {

				}
			}*/
		}else{
			throw new HttpRequestException("Http Response Stream Is Null!",
					ChCareWebApiRequestErrorType.CHCAREWEBAPI_RESPONSE_ERROR);
		}

	}

	protected ResponseBean<?> postRequestUtil(String url,
			String requestBodyparams) throws HttpRequestException {
		return transToBean(basePostRequestUtil(url, requestBodyparams));
	}

	protected String basePostRequestUtil(String url, String requestBodyparams)
			throws HttpRequestException {
		String response = this.httpRequestHandler.post(url, requestBodyparams);
		if (response == null) {
			throw new HttpRequestException("Http Response Stream Is Null!",
					ChCareWebApiRequestErrorType.CHCAREWEBAPI_RESPONSE_ERROR);
		}
		safeLogResponse(response);
		return response;
	}

	protected ResponseBean<?> putRequestUtil(String url,
			String requestBodyparams) throws HttpRequestException {
		return transToBean(basePutRequestUtil(url, requestBodyparams));
	}
	protected String basePutRequestUtil(String url, String requestBodyparams)
			throws HttpRequestException {
		String response = this.httpRequestHandler.put(url, requestBodyparams);
		if (response == null) {
			throw new HttpRequestException("Http Response Stream Is Null!",
					ChCareWebApiRequestErrorType.CHCAREWEBAPI_RESPONSE_ERROR);
		}
		safeLogResponse(response);
		return response;
	}
	protected ResponseBean<?> getRequestUtil(String url)
			throws HttpRequestException {
		return transToBean(baseGetRequestUtil(url));
	}

	protected String baseGetRequestUtil(String url) throws HttpRequestException {
		String response = this.httpRequestHandler.get(url);
		if (response == null) {
			throw new HttpRequestException("Http Response Stream Is Null!",
					ChCareWebApiRequestErrorType.CHCAREWEBAPI_RESPONSE_ERROR);
		}
		safeLogResponse(response);
		return response;
	}

	/**
	 * 关于delete是否可以存在正文, http标准及各服务器的实现并不明确, 建议不传
	 * @param url
	 * @param requestBodyparams
	 * @return
	 * @throws HttpRequestException
	 */
	@Deprecated
	protected ResponseBean<?> deleteRequestUtil(String url,
			String requestBodyparams) throws HttpRequestException {
		return transToBean(baseDeleteRequestUtil(url));
	}

	protected ResponseBean<?> deleteRequestUtil(String url) throws HttpRequestException {
		return transToBean(baseDeleteRequestUtil(url));
	}

	/**
	 * 关于delete是否可以存在正文, http标准及各服务器的实现并不明确, 建议不传
	 * @param url
	 * @param requestBodyparams
	 * @return
	 * @throws HttpRequestException
	 */
	@Deprecated
	protected String baseDeleteRequestUtil(String url, String requestBodyparams)
			throws HttpRequestException {
		return baseDeleteRequestUtil(url);
	}

	protected String baseDeleteRequestUtil(String url)
			throws HttpRequestException {
		String response = this.httpRequestHandler
				.delete(url, null);
		if (response == null) {
			throw new HttpRequestException("Http Response Stream Is Null!",
					ChCareWebApiRequestErrorType.CHCAREWEBAPI_RESPONSE_ERROR);
		}
		safeLogResponse(response);
		return response;
	}

	@Deprecated
	protected String baseUsedFormUploadPhoto(String url, InputStream instream,
			String params) throws HttpRequestException {
		if (instream == null) {
			throw new HttpRequestException(
					"Read InputStream Error,InputStream Is Null!",
					ChCareWebApiRequestErrorType.CHCAREWEBAPI_REQUEST_ERROR);
		}
		System.out.println(url + "," + params);
		String response = null;
		try {
			response = this.httpRequestHandler.postFile(url, instream, params);
		} finally {
			this.httpRequestHandler.closeStream(instream);
		}
		if (response == null) {
			throw new HttpRequestException("Response Is Null!",
					ChCareWebApiRequestErrorType.CHCAREWEBAPI_REQUEST_ERROR);
		}
		safeLogResponse(response);
		return response;
	}

	public CHCareFileInStream downloadFile(String url)
			throws HttpRequestException {
		return this.httpRequestHandler.getPhotoFile(url);
	}
	public boolean downloadFile(String url,OutputStream out) throws HttpRequestException{
		String downurl = BASE_URL;
		try
		{
			downurl = new StringBuilder(BASE_URL).append("File/?id=").append(url).
					append("&key=").append(TokenManager.getFiletoken()).toString();
//			if(url.contains("?"))
//				url = (new StringBuilder(String.valueOf(url))).append("&key=").append(TokenManager.getFiletoken()).toString();
		}
		catch(Exception ignored) { 

		}
		CHLogger.d(this, "downurl " + downurl );
		return this.httpRequestHandler.getPhotoFile(downurl, out);
	}
	protected String doPostSingleFileUsedFormType(String url,
			InputStream instream, Map<String, String> formBodys, String filename)
			throws HttpRequestException {
		System.out.println(url+this.gson.toJson(formBodys));
		String response = null;
		MultipartUtility multipart = new MultipartUtility(url, charset);
		for (Map.Entry<String, String> body : formBodys.entrySet()) {
			multipart.addFormField(body.getKey(), body.getValue());
		}
		multipart.addFilePart(filename, instream);
		List<String> responses = multipart.finish();
		if (responses != null && responses.size() > 0) {
			response = responses.get(0);
		} else {
			throw new HttpRequestException("Http Response Stream Is Null!",
					ChCareWebApiRequestErrorType.CHCAREWEBAPI_RESPONSE_ERROR);
		}
		safeLogResponse(response);
		return response;
	}

	private void safeLogResponse(String response){
		if (BuildConfig.DEBUG)
			System.out.println(safeWrapResponse(response));
	}

	private String safeWrapResponse(String response){
		if (response == null)
			return "{EMPTY RESPONSE}";
		else if (response.length() > 65536)
			return response.substring(0, 1000) + "...(too long data)";
		else
			return response;
	}
}
