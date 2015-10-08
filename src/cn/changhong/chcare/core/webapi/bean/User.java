package cn.changhong.chcare.core.webapi.bean;

import java.io.Serializable;
import java.util.Date;

import com.changhong.util.CHLogger;
import com.changhong.util.db.annotation.CHPrimaryKey;
import com.changhong.util.db.annotation.CHTransient;

public class User implements Serializable, Cloneable{
	/**  
	 * @Fields serialVersionUID : TODO  
	 */
	
	public static final int CUR_VERSION = 1;
	
	@CHTransient
	private static final long serialVersionUID = 1L;
	@CHPrimaryKey()
	private int ID;
	private String Name;
	private boolean Gender = true;
	private Date Birthday;
	private double Height;
	private double Weight;
	private String Area;
	private String NickName;
	private String PhotoUrl;
	private String Sign;
	private int Status;
	private int Type;
	@CHTransient
	private String ChatID;
	private double Exp;

	@CHTransient
	private String RCChatID;

	//自定义

	private int FamilyId;
	@CHTransient
	private Location mMyLocationAddr; //add the location address to User

	public Date getBirthDay() {
		return Birthday;
	}

	public void setBirthDay(Date birthDay) {
		Birthday = birthDay;
	}
	public int getStatus() {
		return Status;
	}

	public void setStatus(int status) {
		Status = status;
	}

	public int getType() {
		return Type;
	}

	public void setType(int type) {
		Type = type;
	}

	private Date RegTime;
	private Date LoginTime;
	private int Level;
	private int LoginCount;

	public User() {
	}

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

	public boolean isGender() {
		return Gender;
	}

	public void setGender(boolean gender) {
		Gender = gender;
	}

	public double getHeight() {
		return Height;
	}

	public void setHeight(double height) {
		Height = height;
	}

	public double getWeight() {
		return Weight;
	}

	public void setWeight(double weight) {
		Weight = weight;
	}

	public String getArea() {
		return Area;
	}

	public void setArea(String area) {
		Area = area;
	}

	public String getNickName() {
		return NickName;
	}

	public void setNickName(String nickName) {
		NickName = nickName;
	}

	public String getPhotoUrl() {
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

	public Date getRegTime() {
		return RegTime;
	}

	public void setRegTime(Date regTime) {
		RegTime = regTime;
	}

	public Date getLoginTime() {
		return LoginTime;
	}

	public void setLoginTime(Date loginTime) {
		LoginTime = loginTime;
	}

	public int getLevel() {
		return Level;
	}

	public void setLevel(int level) {
		Level = level;
	}

	public int getLoginCount() {
		return LoginCount;
	}

	public void setLoginCount(int loginCount) {
		LoginCount = loginCount;
	}

	//为0表示没有家庭
	public int getFamilyId() {
		return FamilyId;
	}

	public void setFamilyId(int familyId) {
		FamilyId = familyId;
	}

	public void setMyLocationAddr(Location address){
		mMyLocationAddr = address;
	}

	/**
	 * 这里是高德地图的经纬度
	 * @return
	 */
	public Location getMyLocationAddr() {
		return mMyLocationAddr;
	}

	public String getChatID() {
		return ChatID;
	}

	public void setChatID(String chatID) {
		ChatID = chatID;
	}

	public String getRCChatID() {
		return RCChatID;
	}

	public void setRCChatID(String RCChatID) {
		this.RCChatID = RCChatID;
	}

	public double getExp() {
		return Exp;
	}

	public void setExp(double exp) {
		Exp = exp;
	}

	public Object clone() {
		User o = null;
		try {
			o = (User) super.clone();
		} catch (CloneNotSupportedException e) {
			CHLogger.e(this, e);
		}
		return o;
	}
}
