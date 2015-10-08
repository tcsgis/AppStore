/**     
 * @Title: ss.java  
 * @Package cn.changhong.chcare.core.webapi.util  
 * @Description: TODO  
 * @author guoyang2011@gmail.com    
 * @date 2014-9-19 下午5:51:36  
 * @version V1.0     
 */
package cn.changhong.chcare.core.webapi.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.changhong.chcare.core.webapi.server.ChCareWebApiRequestErrorType;

/**
 * @ClassName: MultipartUtility
 * @Description: TODO
 * @author guoyang2011@gmail.com
 * @date 2014-9-19 下午5:51:36
 * 
 */
public class MultipartUtility {
	private final String boundary;
	private static final String LINE_FEED = "\r\n";
	private HttpURLConnection httpConn;
	private String charset;
	private OutputStream outputStream;
	private PrintWriter writer;
	
	private volatile boolean isCancel=false;

	public boolean isCancel() {
		return isCancel;
	}

	public synchronized void setCancel(boolean isCancel) {
		this.isCancel = isCancel;
	}

	/**
	 * This constructor initializes a new HTTP POST request with content type is
	 * set to multipart/form-data
	 * 
	 * @param requestURL 请求URL
	 * @param charset 编码字符集
	 * @throws HttpRequestException
	 */
	public MultipartUtility(String requestURL, String charset)
			throws HttpRequestException {
//		System.out.println(requestURL);
		try {
			this.charset = charset;

			// creates a unique boundary based on time stamp

			boundary = "---" + System.currentTimeMillis() + "---";

			URL url = new URL(requestURL);
			httpConn = (HttpURLConnection) ConnFactory.getInstance().open(url);
			httpConn.setUseCaches(false);
			httpConn.setDoOutput(true); // indicates POST method
			httpConn.setDoInput(true);
			httpConn.setRequestProperty("Content-Type",
					"multipart/form-data; boundary=" + boundary);
			// httpConn.setRequestProperty("User-Agent", "CodeJava Agent");
			//httpConn.setRequestProperty("Test", "Bonjour");
			httpConn.setRequestProperty("Authorization", "HAuth "
					+ TokenManager.getToken());
			outputStream = httpConn.getOutputStream();
			writer = new PrintWriter(new OutputStreamWriter(outputStream,
					charset), true);
		} catch (IOException ex) {
			doClean();

			throw new HttpRequestException(
					"Create Http Request Connection Failed!Full Msg["
							+ ex.getMessage() + "]",
					ChCareWebApiRequestErrorType.CHCAREWEBAPI_REQUEST_ERROR);
		}
	}

	/**
	 * Adds a form field to the request
	 * 
	 * @param name
	 *            field name
	 * @param value
	 *            field value
	 */
	public void addFormField(String name, String value) {
		boolean success = false;
		try {
			writer.append("--").append(boundary).append(LINE_FEED)
					.append("Content-Disposition: form-data; name=\"").append(name).append("\"")
					.append(LINE_FEED)
					.append("Content-Type: text/plain; charset=").append(charset)
					.append(LINE_FEED)
					.append(LINE_FEED)
					.append(value)
					.append(LINE_FEED)
					.flush();
			success = true;
		} finally {
			if (!success)
				doClean();
		}
	}

	/**
	 * Adds a upload file section to the request
	 * 
	 * @param fileName
	 *            name attribute in <input type="file" name="..." />
	 * @param inputStream
	 *            a File to be uploaded
	 * @throws HttpRequestException
	 */
	public void addFilePart(String fileName, InputStream inputStream)
			throws HttpRequestException {
		boolean success = false;
		try {
			if (fileName == null || fileName.trim().length() == 0) {
				throw new HttpRequestException("Illegal Args!FileName Is Null!",
						ChCareWebApiRequestErrorType.CHCAREWEBAPI_REQUEST_ERROR);
			}
			if (fileName.contains("\"")) {
				throw new HttpRequestException("Illegal Args!FileName Is Null!",
						ChCareWebApiRequestErrorType.CHCAREWEBAPI_REQUEST_ERROR);
			}
			try {
				String fieldName = fileName.substring(0, fileName.lastIndexOf('.'));
				writer.append("--").append(boundary)
						.append(LINE_FEED)
						.append("Content-Disposition: form-data; name=\"").append(fieldName)
						.append("\"; filename=\"").append(fileName).append("\"")
						.append(LINE_FEED)
						.append("Content-Type: ")
						.append(URLConnection.guessContentTypeFromName(URLEncoder.encode(fileName, "UTF-8")))
						.append(LINE_FEED)
						.append("Content-Transfer-Encoding: binary")
						.append(LINE_FEED)
						.append(LINE_FEED)
						.flush();

				byte[] buffer = new byte[4096];
				int bytesRead;
				while ((bytesRead = inputStream.read(buffer)) != -1) {
					if (isCancel())
						throw new IOException("Cancel upload file!");
					outputStream.write(buffer, 0, bytesRead);
				}
				outputStream.flush();

				writer.append(LINE_FEED)
						.flush();

			} catch (IOException e) {
				throw new HttpRequestException(
						"Create Http Request Stream Failed!Full Msg["
								+ e.getMessage() + "]",
						ChCareWebApiRequestErrorType.CHCAREWEBAPI_REQUEST_ERROR);
			}
			success = true;
		} finally {
			if (!success)
				doClean();
			closeStream(inputStream);
		}
	}

	/**
	 * Adds a header field to the request.
	 * 
	 * @param name
	 *            - name of the header field
	 * @param value
	 *            - value of the header field
	 */
	public void addHeaderField(String name, String value) {
		boolean success = false;
		try {
			writer.append(name).append(": ").append(value)
					.append(LINE_FEED)
					.flush();
			success = true;
		} finally {
			if (!success)
				doClean();
		}
	}

	/**
	 * Completes the request and receives response from the server.
	 * 
	 * @return a list of Strings as response in case the server returned status
	 *         OK, otherwise an exception is thrown.
	 * @throws HttpRequestException
	 */
	public List<String> finish() throws HttpRequestException {
		boolean success = false;
		try {

//		writer.append(LINE_FEED).flush();
			writer.append("--").append(boundary).append("--")
					.append(LINE_FEED)
					.close();

			closeStream(outputStream);
			// checks server's status code first
			int status;
			try {
				status = httpConn.getResponseCode();
			} catch (IOException e) {
				throw new HttpRequestException("Get Response Code Failed!Full Msg["
						+ e.getMessage() + "]",
						ChCareWebApiRequestErrorType.CHCAREWEBAPI_RESPONSE_ERROR);
			}
			if (status == HttpURLConnection.HTTP_OK) {
				InputStream in = null;
				try {
					in = httpConn.getInputStream();
					String response = HttpReaderUtils.readToEnd(in);
					success = true;
					return response != null ? Collections.singletonList(response) : null;
				} catch (IOException ex) {
					throw new HttpRequestException(
							"Get Response File Stream Failed!Full Msg["
									+ ex.getMessage() + "]",
							ChCareWebApiRequestErrorType.CHCAREWEBAPI_RESPONSE_ERROR);
				}
			} else {
				throw new HttpRequestException("Http Response Error,Error Code["
						+ status + "]",
						ChCareWebApiRequestErrorType.CHCAREWEBAPI_RESPONSE_ERROR);
			}

		} finally {
			if (!success)
				doClean();
		}
	}

	private void doClean() {
		if (httpConn != null)
			httpConn.disconnect();
		closeStream(writer);
		closeStream(outputStream);
	}
	private void closeStream(Closeable stream) {
		if (stream != null)
			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
}