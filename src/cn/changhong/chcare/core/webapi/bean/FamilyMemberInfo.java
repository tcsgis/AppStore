/**     
 * @Title: FamilyMemberInfo.java  
 * @Package cn.changhong.chcare.core.webapi.bean  
 * @Description: TODO  
 * @author guoyang2011@gmail.com    
 * @date 2014-9-19 下午1:56:23  
 * @version V1.0     
*/  
package cn.changhong.chcare.core.webapi.bean;  

import java.io.Serializable;

import com.changhong.util.db.annotation.CHPrimaryKey;
import com.changhong.util.db.annotation.CHTransient;
  
/**  
 * @ClassName: FamilyMemberInfo  
 * @Description: TODO  
 * @author guoyang2011@gmail.com  
 * @date 2014-9-19 下午1:56:23  
 *     
 */
public class FamilyMemberInfo implements Serializable{
	/**  
	 * @Fields serialVersionUID : TODO  
	*/
	@CHTransient
	private static final long serialVersionUID = 1L;
	
	private String MemberName;
	private int MemberState;
	private double UserExp;
	@CHTransient
	private User UserInfo;
	
	
	//自定义
	private int FamilyId;
	@CHPrimaryKey()
	private int UserId;
	
	public String getMemberName() {
		return MemberName;
	}
	public void setMemberName(String memberName) {
		MemberName = memberName;
	}
	public int getMemberState() {
		return MemberState;
	}
	public void setMemberState(int memberState) {
		MemberState = memberState;
	}
	public double getUserExp() {
		return UserExp;
	}
	public void setUserExp(double userExp) {
		UserExp = userExp;
	}
	public User getUserInfo() {
		return UserInfo;
	}
	public void setUserInfo(User userInfo) {
		UserInfo = userInfo;
	}
	public int getFamilyId() {
		return FamilyId;
	}
	public void setFamilyId(int familyId) {
		FamilyId = familyId;
	}
	public int getUserId() {
		return UserId;
	}
	public void setUserId(int userId) {
		UserId = userId;
	}
	
	
}
