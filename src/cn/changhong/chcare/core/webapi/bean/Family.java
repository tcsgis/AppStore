package cn.changhong.chcare.core.webapi.bean;

import java.io.Serializable;
import java.util.Date;

import com.changhong.util.db.annotation.CHPrimaryKey;
import com.changhong.util.db.annotation.CHTransient;

public class Family implements Serializable{
	/**  
	 * @Fields serialVersionUID : TODO  
	*/
	@CHTransient
	private static final long serialVersionUID = 1L;
	@CHPrimaryKey()
	private int ID;
	private String Name;
	private int CreatorID;
	private Date CreateTime;
	private int MemberCount;
	private String status;
	private String Type;
	private int Level;
	private String PhotoUrl;
	private String Sign;//家庭描述
	private double Exp;
	private String ShareType;
	private String ChatID;
	
	@CHTransient
	private Location HouseAddr;
	
	@CHTransient
	private final static String FileUrl = "Images/family/";

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public int getCreatorID() {
		return CreatorID;
	}

	public void setCreatorID(int creatorID) {
		CreatorID = creatorID;
	}

	public Date getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(Date createTime) {
		CreateTime = createTime;
	}

	public int getMemberCount() {
		return MemberCount;
	}

	public void setMemberCount(int memberCount) {
		MemberCount = memberCount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public int getLevel() {
		return Level;
	}

	public void setLevel(int level) {
		Level = level;
	}

	public String getPhotoUrl() {
		if(PhotoUrl == null)
			return null;
		else
			return FileUrl + PhotoUrl;
	}
	
	public String getSimplePhotoUrl() {
		return PhotoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		PhotoUrl = photoUrl;
	}

	public String getSign() {
		return Sign;
	}

	public void setSign(String sign) {
		Sign = sign;
	}

	public double getBalance() {
		return Exp;
	}

	public void setBalance(double balance) {
		Exp = balance;
	}

	public String getShareType() {
		return ShareType;
	}

	public void setShareType(String shareType) {
		ShareType = shareType;
	}

	public String getChatID() {
		return ChatID;
	}

	public void setChatID(String chatID) {
		ChatID = chatID;
	}

	public Location getHouseAddr() {
		return HouseAddr;
	}

	public void setHouseAddr(Location houseAddr) {
		HouseAddr = houseAddr;
	}

}
