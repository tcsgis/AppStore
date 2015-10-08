package cn.changhong.chcare.core.webapi.server;

public enum ChCareWebApiRequestErrorType {
	CHCAREWEBAPI_TRANSFORM_DATA_ERROR(-100), CHCAREWEBAPI_REQUEST_ERROR(-200), CHCAREWEBAPI_RESPONSE_ERROR(
			-300), CHCAREWEBAPI_OTHER_ERROR(-500);
	private int value;

	private ChCareWebApiRequestErrorType(int value) {
		this.value = value;
	}

	public int getValue() {
		return this.value;
	}
}
