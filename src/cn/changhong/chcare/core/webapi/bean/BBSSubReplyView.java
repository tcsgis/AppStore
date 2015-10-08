package cn.changhong.chcare.core.webapi.bean;

import java.io.Serializable;
import java.util.Date;

public class BBSSubReplyView implements Serializable {
	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 1L;

	private String UName;//用户名
	private int ID; // 子楼层ID, 回复子楼层时为空
	private int CID;//分类ID
	private int TID; // 帖子ID
	private int RID; // 楼层ID
	private int UID; // 用户ID, 回复子楼层时为空
	private String RName; // 被评论用户名, 可选, GBK(50字节), 回复子楼层时为空
	private Integer ReplySRID; // 被评论子楼层ID, 可选
	private Integer ReplyUID; //被评论用户ID, 可选
	private String Content;
	private Date Time;
	private long Timestamp;
	public String getUName() {
		return UName;
	}

	public void setUName(String uName) {
		UName = uName;
	}
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

	public int getTID() {
		return TID;
	}

	public void setTID(int tID) {
		TID = tID;
	}

	public int getRID() {
		return RID;
	}

	public void setRID(int rID) {
		RID = rID;
	}

	public int getUID() {
		return UID;
	}

	public void setUID(int uID) {
		UID = uID;
	}

	public String getRName() {
		return RName;
	}

	public void setRName(String rName) {
		RName = rName;
	}

	public Integer getReplySRID() {
		return ReplySRID;
	}

	public void setReplySRID(Integer replySRID) {
		ReplySRID = replySRID;
	}
	

	public Integer getReplyUID() {
		return ReplyUID;
	}

	public void setReplyUID(Integer replyUID) {
		ReplyUID = replyUID;
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
}
