package cn.changhong.chcare.core.webapi.bean;

import java.io.Serializable;
import java.util.Date;

public class RequestHealthParams implements Serializable{
	/**  
	 * @Fields serialVersionUID : TODO  
	*/
	private static final long serialVersionUID = 1L;
	private String type;
	private Date st;
	private Date et;
	private String to_uid;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getSt() {
		return st;
	}

	public void setSt(Date st) {
		this.st = st;
	}

	public Date getEt() {
		return et;
	}

	public void setEt(Date et) {
		this.et = et;
	}

	public String getTo_uid() {
		return to_uid;
	}

	public void setTo_uid(String to_uid) {
		this.to_uid = to_uid;
	}
}
