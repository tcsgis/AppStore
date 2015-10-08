package cn.changhong.chcare.core.webapi.util;

import cn.changhong.chcare.core.webapi.server.ChCareWebApiRequestErrorType;

public class HttpRequestException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ChCareWebApiRequestErrorType errorType;

	public ChCareWebApiRequestErrorType getErrorType() {
		return errorType;
	}

	public void setErrorType(ChCareWebApiRequestErrorType errorType) {
		this.errorType = errorType;
	}

	public HttpRequestException(String msg,
			ChCareWebApiRequestErrorType errorType) {
		super(msg);
		this.errorType = errorType;
	}

	public HttpRequestException(String msg) {
	}
}
