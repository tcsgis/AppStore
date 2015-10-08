package cn.changhong.chcare.message;

public enum RouterType {
	FAMILY_PHOTOWALL_SERVICE_ROUTER("fpsr"), FAMILY_MEMBER_SERVICE_ROUTER(
			"fmsr"), FAMILY_MESSAGEBOARD_SERVICE_ROUTER("fmsr"), FAMILY_HEALTHMANAGER_SERVICE_ROUTER(
			"fhsr"), FAMILY_SYSTEM_SERVICE_ROUTER("fssr"), FAMILY_ANNI_SERVICE_ROUTER("fannisr"),FAMILY_DIARY_SERVICE_ROUTER("diarysr"),
			BBS_SERVICE_ROUTER("bbssr");
	private String value;

	private RouterType(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
}
