package cn.changhong.chcare.core.webapi.util;

import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

import cn.changhong.chcare.core.webapi.bean.CHCareFileInStream;
import cn.changhong.chcare.core.webapi.server.ChCareWebApiRequestErrorType;

public class HttpsConnectionManager implements IHttpRestApi {
	String DEFAULT_BOUNDRY = "******";
	String END_FLAG = "\r\n";
	String TWO_HYPHENS = "--";
	int timeout = -1;

	public static class Self {
		private static HttpsConnectionManager instance;

		public static HttpsConnectionManager defaultInstance() {
			if (instance == null) {
				instance = new HttpsConnectionManager();
			}
			return instance;
		}
	}

	//TODO: use RAW string for special Type(not work for HttpConnection
	private enum HttpRequestType {
		GET("GET"), PUT("PUT"), DELETE("DELETE"), POST("POST"), OPTIONS(
				"OPTIONS"), HEAD("HEAD"), TRACE("TRACE"), PATCH("PATCH");
		private String value;

		HttpRequestType(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}

	private enum HttpContentType {
		JSON("application/json"), STREAM("application/octet-stream");
		private String value;

		HttpContentType(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}

	private String requestHandler(HttpRequestType type, String url,
			String jsonObject) throws HttpRequestException {
		String result = null;
		System.out.println(type + " " + url + (jsonObject != null ? ("<-" + jsonObject) : ""));
		HttpURLConnection conn = this.createHttpConn(type, url);
		if (conn != null) {
			boolean success = false;

			InputStream in = null;
			try {
				int outLength = 0;
				if (type == HttpRequestType.POST ||
						type == HttpRequestType.PUT ||
						type == HttpRequestType.PATCH) {
					//TODO: 空字符串应该发送么??
					if (!TextUtils.isEmpty(jsonObject)) {
						conn.setDoOutput(true);
						OutputStream out = null;
						try {
							byte[] buffer = jsonObject.getBytes("UTF-8");
							outLength = buffer.length;
							//NOTE: Content-Length是自动生成的, 不要自己去写
//							conn.setRequestProperty("Content-Length", outLength + "");
							out = conn.getOutputStream();
							out.write(buffer);
						} catch (IOException e) {
							throw new HttpRequestException(
									"Create Http OutputStream Error!Full Msg[" + e.getMessage() + "]",
									ChCareWebApiRequestErrorType.CHCAREWEBAPI_REQUEST_ERROR);
						} finally {
							this.closeStream(out);
						}
					}
				}
				int responseCode;
				try {
					responseCode = conn.getResponseCode();
				} catch (IOException e) {
					throw new HttpRequestException(
							"Get Response Code Failed!Full Msg["+e.getMessage()+"]",
							ChCareWebApiRequestErrorType.CHCAREWEBAPI_RESPONSE_ERROR);
				}
				System.out.println(responseCode);

//				if (responseCode == 200) {
					System.out.println(
							conn.getRequestMethod()+"(" + responseCode + ") " + conn.getURL());
							//NOTE: 开启压缩后无法读取到ContentLength数据, 故移除
							//"->" + outLength + "/" + conn.getContentLength();
					try {
						if (responseCode >= 400)
							in = conn.getErrorStream();
						else
							in = conn.getInputStream();
						result = HttpReaderUtils.readToEnd(in);
					} catch (IOException e) {
						throw new HttpRequestException(
								"Read Response Stream Failed!Full Msg["+e.getMessage()+"], Response Code=" + responseCode,
								ChCareWebApiRequestErrorType.CHCAREWEBAPI_RESPONSE_ERROR);
					}
//				} else {
//					throw new HttpRequestException(
//							"Http Response Error,Error Code[" + responseCode
//									+ "]",
//							ChCareWebApiRequestErrorType.CHCAREWEBAPI_RESPONSE_ERROR);
//				}
				success = true;
			} finally {
				this.closeStream(in);
				if (!success)
					conn.disconnect();
			}
		}
		return result;
	}

	private HttpURLConnection createHttpConn(HttpRequestType type, String url)
			throws HttpRequestException {
		return this.createHttpConn(type, url, HttpContentType.JSON);
	}

	private HttpURLConnection createHttpConn(HttpRequestType type, String url,
			HttpContentType contentType) throws HttpRequestException {
		return createHttpConn(type.getValue(), url, contentType);
	}

	private HttpURLConnection createHttpConn(String type, String url,
			HttpContentType contentType) throws HttpRequestException {
		HttpURLConnection httpConn = null;
		try {
			URL u = new URL(url);
			httpConn = (HttpURLConnection)ConnFactory.getInstance().open(u);
			//httpConn = (HttpURLConnection) u.openConnection();
			httpConn.setDoInput(true);
			httpConn.setRequestMethod(type);

			int timeout = this.timeout;
			if (timeout > 0) {
				httpConn.setConnectTimeout(timeout);
				httpConn.setReadTimeout(timeout);
			} else {
				httpConn.setConnectTimeout(3000);
				httpConn.setReadTimeout(5000);
			}

			httpConn.setRequestProperty("Content-Type", contentType.getValue()
					+ "; charset=utf-8");
			if (TokenManager.getToken() != null) {
				System.out.println(TokenManager.getToken());
				httpConn.setRequestProperty("Authorization", "HAuth "
						+ TokenManager.getToken());
			}
		} catch (IOException e) {
			if(httpConn != null){
				httpConn.disconnect();
			}
			throw new HttpRequestException(
					"Create Http Request Connection Failed!Full Msg["+e.getMessage()+"]",
					ChCareWebApiRequestErrorType.CHCAREWEBAPI_REQUEST_ERROR);
		}
		return httpConn;
	}

	@Override
	public String get(String url) throws HttpRequestException {
		return requestHandler(HttpRequestType.GET, url, null);
	}

	@Override
	public String post(String url, String params) throws HttpRequestException {
		return this.requestHandler(HttpRequestType.POST, url, params);
	}

	@Override
	public void closeStream(Closeable stream) throws HttpRequestException {
		if (stream != null)
			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	@Override
	public String put(String url, String params) throws HttpRequestException {
		return this.requestHandler(HttpRequestType.PUT, url, params);
	}

	@Override
	public String delete(String url, String params) throws HttpRequestException {
		return this.requestHandler(HttpRequestType.DELETE, url, params);
	}

	@Deprecated
	@Override
	public String postFile(String url, InputStream inStream, String params)
			throws HttpRequestException {

		String result = null;
		DataOutputStream outStream = null;
		InputStream responseStream = null;
		HttpURLConnection conn = null;
		if (inStream != null) {
			conn = this.createHttpConn(HttpRequestType.POST, url,
					HttpContentType.STREAM);
			try {
				conn.setRequestMethod(HttpRequestType.POST.getValue());
				conn.setUseCaches(false);
				conn.setDoOutput(true);
				conn.setDoInput(true);
				conn.setConnectTimeout(5000);
				conn.setReadTimeout(5000);
				conn.setRequestProperty("connection", "Keep-Alive");

				conn.setRequestProperty("Content-Type",
						"multipart/form-data;boundary=" + DEFAULT_BOUNDRY);

				outStream = new DataOutputStream(new BufferedOutputStream(conn.getOutputStream()));
				outStream.writeBytes(this.TWO_HYPHENS + this.DEFAULT_BOUNDRY
						+ this.END_FLAG);
				outStream.writeBytes("Content-Disposition: form-data;"+params + this.END_FLAG);
				outStream.writeBytes("Content-Type:application/octet-stream"
						+ this.END_FLAG + this.END_FLAG);
				byte[] buffer = new byte[65536];
				int bytesRead = -1;
				int size = 0;
				while ((bytesRead = inStream.read(buffer)) != -1) {
					outStream.write(buffer, 0, bytesRead);
//					System.out.println(bytesRead);
					size += bytesRead;
				}
				this.closeStream(inStream);
				inStream = null;//避免二次close
				System.out.println(size);
				outStream.writeBytes(this.END_FLAG);
				outStream.writeBytes(this.TWO_HYPHENS + this.DEFAULT_BOUNDRY+this.TWO_HYPHENS
						+ this.END_FLAG);
				outStream.flush();
				this.closeStream(outStream);
				outStream = null;//避免二次close
				int status = conn.getResponseCode();
				System.out.println(status);
				//if (status == 200) {
					responseStream = conn.getInputStream();
				result = HttpReaderUtils.readToEnd(responseStream);
				//}
			} catch (ProtocolException e) {

				e.printStackTrace();

			} catch (IOException e) {

				e.printStackTrace();
			} finally {
				this.closeStream(outStream);
				this.closeStream(inStream);
				this.closeStream(responseStream);
				conn.disconnect();
			}

		}
		return result;
	}

	/**
	 * 
	 * 
	 * @param url
	 * @return
	 * @throws HttpRequestException
	 * @see cn.changhong.chcare.core.webapi.util.IHttpRestApi#getPhotoFile(java.lang.String)
	 */

	@Override
	public CHCareFileInStream getPhotoFile(String url)
			throws HttpRequestException {

		//NOTE: 此函数需要fileType, 暂时不和另一个同名函数逻辑合并
		CHCareFileInStream result = null;
		HttpURLConnection conn = this.createHttpConn(HttpRequestType.GET, url);
		conn.setReadTimeout(5000);
		conn.setConnectTimeout(5000);
		if (conn != null) {
			InputStream in = null;
			int responseCode = -1;
			boolean success = false;
			try {
				try {
					responseCode = conn.getResponseCode();
				} catch (IOException e) {
					throw new HttpRequestException(
							"Get Response Code Failed!Full Msg["+e.getMessage()+"]",
							ChCareWebApiRequestErrorType.CHCAREWEBAPI_RESPONSE_ERROR);
				}
				if (responseCode == 200) {

					System.out.println("\n\n"
							+ conn.getHeaderField("Content-Length") + "\n\n");
					String fileType = null;
					try {
						fileType = conn.getHeaderField("Content-Type").split("/")[1];
					} catch (Exception e) {
						throw new HttpRequestException(
								"GET Response Photo Stream Type Error!Full Msg["+e.getMessage()+"]",
								ChCareWebApiRequestErrorType.CHCAREWEBAPI_RESPONSE_ERROR);
					}

					ByteArrayOutputStream outStram = new ByteArrayOutputStream();
					try {
						int size = 0;
						in = new BufferedInputStream(conn.getInputStream());
						byte[] buffer = new byte[65536];
						for (int n=-1; (n = in.read(buffer)) != -1;) {
							outStram.write(buffer, 0, n);
							size+=n;
						}
						System.out.println(size);
					} catch (IOException e) {
						throw new HttpRequestException(
								"GET Response File Stream Error!Full Msg["+e.getMessage()+"]",
								ChCareWebApiRequestErrorType.CHCAREWEBAPI_RESPONSE_ERROR);
					}
					result = new CHCareFileInStream(outStram, fileType);
				} else {
					throw new HttpRequestException(
							"Http Response Error,Error Code[" + responseCode
									+ "]",
							ChCareWebApiRequestErrorType.CHCAREWEBAPI_RESPONSE_ERROR);
				}
				success = true;
			} finally {
				this.closeStream(in);
				if (!success)
					conn.disconnect();
			}
		}
		return result;
	}

	/**  
	 *   
	 *   
	 * @param url
	 * @param out
	 * @return
	 * @throws HttpRequestException  
	 * @see cn.changhong.chcare.core.webapi.util.IHttpRestApi#getPhotoFile(java.lang.String, java.io.OutputStream)  
	*/  
	
	@Override
	public boolean getPhotoFile(String url, OutputStream out)
			throws HttpRequestException {
		if(out == null || url == null ){
			throw new HttpRequestException("IllegalArgumentException:Url Or Stream Is Null", ChCareWebApiRequestErrorType.CHCAREWEBAPI_REQUEST_ERROR);
		}
		boolean result = false;
		HttpURLConnection conn = this.createHttpConn(HttpRequestType.GET, url);
		conn.setReadTimeout(5000);
		conn.setConnectTimeout(5000);
		if (conn != null) {
			InputStream in = null;
			boolean success = false;
			int responseCode = -1;
			try {
				try {
					responseCode = conn.getResponseCode();
				} catch (IOException e) {
					throw new HttpRequestException(
							"Get Response Code Failed!Full Msg["+e.getMessage()+"]",
							ChCareWebApiRequestErrorType.CHCAREWEBAPI_RESPONSE_ERROR);
				}
				if (responseCode == 200) {
					try {
						int size = 0;
						in = new BufferedInputStream(conn.getInputStream());
						byte[] buffer = new byte[65536];
						for (int n=-1; (n = in.read(buffer)) != -1;) {
							out.write(buffer, 0, n);
							size+=n;
						}
						System.out.println(size);
					} catch (IOException e) {
						throw new HttpRequestException(
								"GET Response File Stream Error!Full Msg["+e.getMessage()+"]",
								ChCareWebApiRequestErrorType.CHCAREWEBAPI_RESPONSE_ERROR);
					}
					result = true;
				} else {
					throw new HttpRequestException(
							"Http Response Error,Error Code[" + responseCode
									+ "]",
							ChCareWebApiRequestErrorType.CHCAREWEBAPI_RESPONSE_ERROR);
				}
				success = true;
			} finally {
				this.closeStream(out);
				this.closeStream(in);
				if (!success)
					conn.disconnect();
			}
		}
		return result;
	}


}
