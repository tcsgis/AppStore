package cn.changhong.chcare.core.webapi.bean;

import java.io.Serializable;
import java.util.Date;

import cn.changhong.chcare.core.webapi.bean.BBSSubReplyView;

public class BBSReplyView implements Serializable{
	/**  
	 * @Fields serialVersionUID : TODO  
	*/
	private static final long serialVersionUID = 1L;
	private int ID;     //楼层ID, 回复楼层时为空
	private int CID;//分类ID
	private int FNo;    //楼层数, 回复时为空
	private int UID;    //用户ID, 回复时为空
	private String UName; //用户名, GBK(50字节), 回复时为空
	private int TID;    //帖子ID
	private String Content; //楼层内容, GBK(200字节), 不可为null
	private Date Time;   //楼层回复时间, 回复时为空
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public int getCID() {
		return CID;
	}
	public void setCID(int cID) {
		CID = cID;
	}
	public int getFNo() {
		return FNo;
	}
	public void setFNo(int fNo) {
		FNo = fNo;
	}
	public int getUID() {
		return UID;
	}
	public void setUID(int uID) {
		UID = uID;
	}
	public String getUName() {
		return UName;
	}
	public void setUName(String uName) {
		UName = uName;
	}
	public int getTID() {
		return TID;
	}
	public void setTID(int tID) {
		TID = tID;
	}
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}
	public Date getTime() {
		return Time;
	}
	public void setTime(Date time) {
		Time = time;
	}
	public long getTimestamp() {
		return Timestamp;
	}
	public void setTimestamp(long timestamp) {
		Timestamp = timestamp;
	}
	public BBSSubReplyView[] getSubReply() {
		return SubReply;
	}
	public void setSubReply(BBSSubReplyView[] subReply) {
		SubReply = subReply;
	}
	private long Timestamp; //修改时间, unix时间戳 * 1000, 回复时为空
	private BBSSubReplyView[] SubReply;//该楼层的最多前两条回复, 按时间先后排序, 回复时为空

}
