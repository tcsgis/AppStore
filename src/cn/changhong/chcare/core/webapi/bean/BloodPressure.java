package cn.changhong.chcare.core.webapi.bean;

import java.io.Serializable;
import java.sql.Date;

public class BloodPressure implements Serializable{
	/**  
	 * @Fields serialVersionUID : TODO  
	*/
	private static final long serialVersionUID = 1L;
	private int ID;
	private int UserID;
	private int SBP;
	private int DBP;
	private int Pulse;
	private Date EamTime;
	private String ExamResult;
	private String DeviceName;
	private String ExamStatus;

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public int getUserID() {
		return UserID;
	}

	public void setUserID(int userID) {
		UserID = userID;
	}

	public int getSBP() {
		return SBP;
	}

	public void setSBP(int sBP) {
		SBP = sBP;
	}

	public int getDBP() {
		return DBP;
	}

	public void setDBP(int dBP) {
		DBP = dBP;
	}

	public int getPulse() {
		return Pulse;
	}

	public void setPulse(int pulse) {
		Pulse = pulse;
	}

	public Date getEamTime() {
		return EamTime;
	}

	public void setEamTime(Date eamTime) {
		EamTime = eamTime;
	}

	public String getExamResult() {
		return ExamResult;
	}

	public void setExamResult(String examResult) {
		ExamResult = examResult;
	}

	public String getDeviceName() {
		return DeviceName;
	}

	public void setDeviceName(String deviceName) {
		DeviceName = deviceName;
	}

	public String getExamStatus() {
		return ExamStatus;
	}

	public void setExamStatus(String examStatus) {
		ExamStatus = examStatus;
	}

}
