/**     
 * @Title: CHCareDownloadStream.java  
 * @Package cn.changhong.chcare.core.webapi.bean  
 * @Description: TODO  
 * @author guoyang2011@gmail.com    
 * @date 2014-9-11 下午2:30:47  
 * @version V1.0     
 */
package cn.changhong.chcare.core.webapi.bean;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

/**
 * @ClassName: CHCareFileInStream
 * @Description: TODO
 * @author guoyang2011@gmail.com
 * @date 2014-9-11 下午2:30:47
 * 
 */
public class CHCareFileInStream implements Serializable{
	/**  
	 * @Fields serialVersionUID : TODO  
	*/
	private static final long serialVersionUID = 1L;
	private ByteArrayOutputStream inputStream;
	private String fileType;

	public CHCareFileInStream(ByteArrayOutputStream inputStream, String fileType) {
		this.inputStream = inputStream;
		this.fileType = fileType;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public ByteArrayOutputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(ByteArrayOutputStream inputStream) {
		this.inputStream = inputStream;
	}
}
