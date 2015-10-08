package cn.changhong.chcare.core.webapi.bean;

import java.io.Serializable;

public class MsgThreadViewBean implements Serializable {
	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 1L;

	 /// <summary>
    /// 对方干了什么
    /// (1: 回复帖子, 2: 评论楼层, 3: 评论评论)
    /// </summary>
    private int Op;
    /// <summary>
    /// 我为什么会收到这条消息
    /// (1: 我是楼主, 2: 我是层主, 3: 我是被评论人)
    /// 按优先级从低到高排序
    /// </summary>
    private int MyOp;
    /// <summary>
    /// 分类ID
    /// </summary>
    private int CID;
    /// <summary>
    /// 帖子ID
    /// </summary>
    private int TID;
    /// <summary>
    /// 回复(楼层)ID
    /// </summary>
    private int RID;
    /// <summary>
    /// 评论(子楼层)ID
    /// </summary>
    private int SRID;
    /// <summary>
    /// 被评论用户ID, 可选
    /// </summary>
    private int ReplyUID;
    /// <summary>
    /// 被评论子楼层ID, 可选
    /// </summary>
    private int ReplySRID;
    /// <summary>
    /// 操作方用户名, GBK(50字节)
    /// </summary>
    private String FromUName;
    /// <summary>
    /// 操作方内容, GBK(200字节), 不可为null
    /// </summary>
    private String FromContent;
    /// <summary>
    /// 被动方用户名, GBK(50字节)
    /// </summary>
    private String ToUName;
    /// <summary>
    /// 被动方内容, GBK(200字节), 不可为null
    /// </summary>
    private String ToContent;
	public int getOp() {
		return Op;
	}
	public void setOp(int op) {
		Op = op;
	}
	public int getMyOp() {
		return MyOp;
	}
	public void setMyOp(int myOp) {
		MyOp = myOp;
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
	public int getSRID() {
		return SRID;
	}
	public void setSRID(int sRID) {
		SRID = sRID;
	}
	public int getReplyUID() {
		return ReplyUID;
	}
	public void setReplyUID(int replyUID) {
		ReplyUID = replyUID;
	}
	public int getReplySRID() {
		return ReplySRID;
	}
	public void setReplySRID(int replySRID) {
		ReplySRID = replySRID;
	}
	public String getFromUName() {
		return FromUName;
	}
	public void setFromUName(String fromUName) {
		FromUName = fromUName;
	}
	public String getFromContent() {
		return FromContent;
	}
	public void setFromContent(String fromContent) {
		FromContent = fromContent;
	}
	public String getToUName() {
		return ToUName;
	}
	public void setToUName(String toUName) {
		ToUName = toUName;
	}
	public String getToContent() {
		return ToContent;
	}
	public void setToContent(String toContent) {
		ToContent = toContent;
	}
    

}
