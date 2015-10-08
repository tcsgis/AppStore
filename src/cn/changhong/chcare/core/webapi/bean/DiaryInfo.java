package cn.changhong.chcare.core.webapi.bean;

import java.io.Serializable;
import java.util.Date;

import com.changhong.util.db.annotation.CHPrimaryKey;
import com.changhong.util.db.annotation.CHTransient;

public class DiaryInfo implements Serializable{
	
	/**
	 * 
	 */
	@CHTransient
	private static final long serialVersionUID = 1L;
	@CHPrimaryKey
	private Integer ID;           //日记ID，发表日记时为空
	private Integer UID;          //用户ID，发表日记时为空
	private Integer FID;          //家庭ID，未加入家庭时，为空
	private Integer Priv;         //日记的查看权限，(1:家庭可见，0:自己可见)，获取日记时为空  
	private String Text;      //日记内容[最长500英文/250汉字, GBK]
	private Double Lng;       //经度  
	private Double Lat;       //纬度  
    private Date Time;        //日记发表的时间，发表日记时为空
    private String Address;   //日记中添加的地址  
    private String Pics;      //图片地址（分隔符模式，以“|”为分隔符），发表日记时为空
    private Integer CommentCnt;    //日记评论数，发表日记时为空
    
    
    //自定义
    private Long LastCheckTime = 0l;
    private int HavePage = 0;//<=0：表示没有分页; >0:表示有分页
    
	
	
	public Integer getID() {
		return ID;
	}
	public void setID(Integer iD) {
		ID = iD;
	}
	public Integer getUID() {
		return UID;
	}
	public void setUID(Integer uID) {
		UID = uID;
	}
	public Integer getFID() {
		return FID;
	}
	public void setFID(Integer fID) {
		FID = fID;
	}
	public Integer getPriv() {
		return Priv;
	}
	public void setPriv(Integer priv) {
		Priv = priv;
	}
	public String getText() {
		return Text;
	}
	public void setText(String text) {
		Text = text;
	}
	public Double getLng() {
		return Lng;
	}
	public void setLng(Double lng) {
		Lng = lng;
	}
	public Double getLat() {
		return Lat;
	}
	public void setLat(Double lat) {
		Lat = lat;
	}
	public Date getTime() {
		return Time;
	}
	public void setTime(Date time) {
		Time = time;
	}
	public String getAddress() {
		return Address;
	}
	public void setAddress(String address) {
		Address = address;
	}
	public String getPics() {
		return Pics;
	}
	public void setPics(String pics) {
		Pics = pics;
	}
	public Integer getCommentCnt() {
		return CommentCnt;
	}
	public void setCommentCnt(Integer commentCnt) {
		CommentCnt = commentCnt;
	}
	/**
	 * 自定义
	 * -100:表示仅通知可见,>=0表示可见
	 * @return
	 */
	public Long getLastCheckTime() {
		return LastCheckTime;
	}
	/**
	 * 自定义
	 * -100:表示仅通知可见,>=0表示可见
	 * @return
	 */
	public void setLastCheckTime(Long lastCheckTime) {
		LastCheckTime = lastCheckTime;
	}
	
	/**
	 * <=0：表示没有分页; >0:表示有分页
	 * @return
	 */
	public int getHavePage() {
		return HavePage;
	}
	
	/**
	 * <=0：表示没有分页; >0:表示有分页
	 * @param havePage
	 */
	public void setHavePage(int havePage) {
		HavePage = havePage;
	}
	
}
