package cn.changhong.chcare.core.webapi.util;

import java.io.Closeable;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import cn.changhong.chcare.core.webapi.bean.CHCareFileInStream;

public interface IHttpRestApi {
	public String get(String url) throws HttpRequestException;

	public String post(String url, String params) throws HttpRequestException;

	public String put(String url, String params) throws HttpRequestException;

	public String delete(String url, String params) throws HttpRequestException;
	@Deprecated
	public String postFile(String url, InputStream filestream, String params)
			throws HttpRequestException;

	public void closeStream(Closeable stream) throws HttpRequestException;

	public CHCareFileInStream getPhotoFile(String url)
			throws HttpRequestException;
	public boolean getPhotoFile(String url, OutputStream out) throws HttpRequestException;
}
