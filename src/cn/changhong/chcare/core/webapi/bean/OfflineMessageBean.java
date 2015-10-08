package cn.changhong.chcare.core.webapi.bean;

import java.io.Serializable;

import com.changhong.util.db.annotation.CHPrimaryKey;
import com.changhong.util.db.annotation.CHTransient;

public class OfflineMessageBean<T> implements Serializable{
	/**  
	 * @Fields serialVersionUID : TODO  
	*/
	private static final long serialVersionUID = 1L;
	@CHPrimaryKey
	private int ID;
	private int Type;
	@CHTransient
	private T Val;
	private String valString;
	private Long TimeStamp;
	private int SUID;
	private int RID;
	private int RType;
	
	private int HavePage;
	private int op;
	private String routerType;//消息粗略类型
	
	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public int getType() {
		return Type;
	}

	public void setType(int type) {
		Type = type;
	}

	public T getVal() {
		return Val;
	}

	public void setVal(T val) {
		Val = val;
	}

	public int getSUID() {
		return SUID;
	}

	public void setSUID(int sUID) {
		SUID = sUID;// 13730123457 123
	}

	public int getRID() {
		return RID;
	}

	public void setRID(int rID) {
		RID = rID;
	}

	public int getRType() {
		return RType;
	}

	public void setRType(int rType) {
		RType = rType;
	}

	public Long getTimeStamp() {
		return TimeStamp;
	}

	public void setTimeStamp(Long timeStamp) {
		TimeStamp = timeStamp;
	}

	public String getValString() {
		return valString;
	}

	/**
	 * 仅用于数据库存储
	 * @param valString
	 */
	public void setValString(String valString) {
		this.valString = valString;
	}

	public int getHavePage() {
		return HavePage;
	}

	public void setHavePage(int havePage) {
		HavePage = havePage;
	}

	public int getOp() {
		return op;
	}

	public void setOp(int op) {
		this.op = op;
	}

	public String getRouterType() {
		return routerType;
	}

	public void setRouterType(String routerType) {
		this.routerType = routerType;
	}

}
