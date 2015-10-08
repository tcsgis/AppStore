package cn.changhong.chcare.core.webapi.bean;

import java.io.Serializable;
import java.util.Date;

import com.changhong.util.db.annotation.CHPrimaryKey;
import com.changhong.util.db.annotation.CHTransient;

public class DiaryComment implements Serializable{
	
	/**
	 * 
	 */
	@CHTransient
	private static final long serialVersionUID = 1L;
	private Integer DiaryID;      //日志ID，该评论所属的日记ID  
	@CHPrimaryKey
	private Integer ID;           //评论ID，评论提交时为空
	private Integer UID;          //该评论的用户ID， 例如：UID 回复 ReplyID：这是评论内容 
	private Integer FamilyId;          //该评论的用户ID， 例如：UID 回复 ReplyID：这是评论内容 
	private Integer ReplyID;      //被评论用户ID   
	private String Text;      //评论内容[最长200英文/100汉字, GBK]  
	private Date Time;     //评论时间，添加评论时为空
	
	//自定义
	private long LastCheckTime = 0l;
	private int HavePage = 0;//-1表示通知内容;-2:表示通知的分页;0:即是通知也是非通知;1:非通知;2:非通知的分页;3:通知也是非通知分页;4:通知分页也是非通知;5:通知分页也是非通知分页
	
	@CHTransient
	private String UName;
	@CHTransient
	private String ReplyUName;
	
	/**
	 * 日记消息可用
	 */
	@CHTransient
	private String Pics;
	@CHTransient
	private String Content;
	
	public Integer getDiaryID() {
		return DiaryID;
	}
	public void setDiaryID(Integer diaryID) {
		DiaryID = diaryID;
	}
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
	
	public Integer getReplyID() {
		return ReplyID;
	}
	public void setReplyID(Integer replyID) {
		ReplyID = replyID;
	}
	public String getText() {
		return Text;
	}
	public void setText(String text) {
		Text = text;
	}
	public Date getTime() {
		return Time;
	}
	public void setTime(Date time) {
		Time = time;
	}
	
	/**
	 * 自定义
	 * @return
	 */
	public String getUName() {
		return UName;
	}
	public void setUName(String uName) {
		UName = uName;
	}
	
	/**
	 * 自定义
	 * @return
	 */
	public String getReplyUName() {
		return ReplyUName;
	}
	public void setReplyUName(String replyUName) {
		ReplyUName = replyUName;
	}
	
	/**
	 * -1表示通知内容;-2:表示通知的分页;0:即是通知也是非通知;1:非通知;2:非通知的分页;3:通知也是非通知分页;4:通知分页也是非通知;5:通知分页也是非通知分页
	 * @return
	 */
	public int getHavePage() {
		return HavePage;
	}
	/**
	 * -1表示通知内容;-2:表示通知的分页;0:即是通知也是非通知;1:非通知;2:非通知的分页;3:通知也是非通知分页;4:通知分页也是非通知;5:通知分页也是非通知分页
	 * @param havePage
	 */
	public void setHavePage(int havePage) {
		HavePage = havePage;
	}
	public long getLastCheckTime() {
		return LastCheckTime;
	}
	
	public void setLastCheckTime(long lastCheckTime) {
		LastCheckTime = lastCheckTime;
	}
	
	/**
	 * 日记消息可用
	 */
	public String getPics() {
		return Pics;
	}
	/**
	 * 日记消息可用
	 */
	public void setPics(String pics) {
		Pics = pics;
	}
	/**
	 * 被回复内容, 日记消息可用
	 */
	public String getContent() {
		return Content;
	}
	/**
	 * 日记消息可用
	 */
	public void setContent(String content) {
		Content = content;
	}
	public Integer getFamilyId() {
		return FamilyId;
	}
	public void setFamilyId(Integer familyId) {
		FamilyId = familyId;
	}
	
}
