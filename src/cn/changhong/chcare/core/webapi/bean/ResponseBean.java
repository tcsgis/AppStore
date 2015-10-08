package cn.changhong.chcare.core.webapi.bean;

import java.io.Serializable;
import java.util.List;

public class ResponseBean<T> implements Serializable{
	/**  
	 * @Fields serialVersionUID : TODO  
	*/
	private static final long serialVersionUID = 1L;
	private int State;
	private boolean isLocData;//cxp 用于判断是否为离线数据
	
	private String Desc;
	private T Data;
	private List<Integer> DelIds;

	public T getData() {
		return Data;
	}

	public void setData(T data) {
		Data = data;
	}

	public List<Integer> getDelIds() {
		return DelIds;
	}

	public void setDelIds(List<Integer> delIds) {
		DelIds = delIds;
	}

	public int getState() {
		return State;
	}

	public void setState(int state) {
		State = state;
	}

	public String getDesc() {
		return Desc;
	}

	public void setDesc(String desc) {
		Desc = desc;
	}
	/**
	 * 是否为离线数据
	 * @return
	 */
	public boolean isLocData() {
		return isLocData;
	}

	public void setLocData(boolean isLocData) {
		this.isLocData = isLocData;
	}

}
