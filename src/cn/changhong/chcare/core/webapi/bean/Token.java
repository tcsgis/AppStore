/**     
 * @Title: Token.java  
 * @Package cn.changhong.chcare.core.webapi.bean  
 * @Description: TODO  
 * @author guoyang2011@gmail.com    
 * @date 2014-11-21 下午3:34:34  
 * @version V1.0     
*/  
package cn.changhong.chcare.core.webapi.bean;  
  
/**   
 * @ClassName: Token  
 * @Description: TODO  
 * @author guoyang2011@gmail.com  
 * @date 2014-11-21 下午3:34:34  
 *     
 */
public class Token {
	private String Token;
	private String FileToken;
	private String Extra;
	public String getToken() {
		return Token;
	}
	public void setToken(String token) {
		Token = token;
	}
	public String getFileToken() {
		return FileToken;
	}
	public void setFileToken(String fileToken) {
		FileToken = fileToken;
	}
	public String getExtra() {
		return Extra;
	}
	public void setExtra(String extra) {
		Extra = extra;
	}
}
