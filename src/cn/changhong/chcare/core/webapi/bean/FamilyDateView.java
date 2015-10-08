package cn.changhong.chcare.core.webapi.bean;

import java.io.Serializable;
import java.util.Date;

import com.changhong.util.db.annotation.CHPrimaryKey;
import com.changhong.util.db.annotation.CHTransient;
import com.google.gson.annotations.SerializedName;

public class FamilyDateView implements Serializable {

	@CHTransient
	private static final long serialVersionUID = 1L;
	@CHPrimaryKey
	private int ID;
	private int FID;
	private int UID;
	@SerializedName("Date")
	private Date date;
	private Integer Offset;
	private String Name;
	private byte Type;
	
	/**
	 * 纪念日ID(更新时使用)
	 * @return
	 */
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	/**
	 * 家庭ID
	 * @return
	 */
	public int getFID() {
		return FID;
	}
	public void setFID(int fID) {
		FID = fID;
	}
	
	/**
	 * 目标用户ID(仅生日类型使用)
	 * @return
	 */
	public int getUID() {
		return UID;
	}
	public void setUID(int uID) {
		UID = uID;
	}
	
	/**
	 * 日期(不含时间)
	 * @return
	 */
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	/**
	 * 提前(+)/延后(-)多少秒提醒
	 * @return
	 */
	public Integer getOffset() {
		return Offset;
	}
	public void setOffset(Integer offset) {
		Offset = offset;
	}
	
	/**
	 * 纪念日名称(仅自定义类型)[可Null, 最长200英文/100汉字, GBK]
	 * @return
	 */
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	
	/**
	 * 纪念日类型(0:生日,1:结婚,2:自定义)[数据库类型ubyte, 不过用不到那么多就无所谓了]
	 * @return
	 */
	public byte getType() {
		return Type;
	}
	public void setType(byte type) {
		Type = type;
	}


}
