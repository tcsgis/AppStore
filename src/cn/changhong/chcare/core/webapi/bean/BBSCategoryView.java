package cn.changhong.chcare.core.webapi.bean;

import java.io.Serializable;

public class BBSCategoryView implements Serializable{
	private static final long serialVersionUID = 1L;
	private int ID;
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
	public String getInfo() {
		return Info;
	}
	public void setInfo(String info) {
		Info = info;
	}
	public int getPic() {
		return Pic;
	}
	public void setPic(int pic) {
		Pic = pic;
	}
	public int getThreadCnt() {
		return ThreadCnt;
	}
	public void setThreadCnt(int threadCnt) {
		ThreadCnt = threadCnt;
	}
	public int getCommentCnt() {
		return CommentCnt;
	}
	public void setCommentCnt(int commentCnt) {
		CommentCnt = commentCnt;
	}
	public int getCID() {
		return CID;
	}
	public void setCID(int cID) {
		CID = cID;
	}
	private String Name;
	private String Info;
	private int Pic;
	private int ThreadCnt;
	private int CommentCnt;
	private int CID;//分类ID

}
