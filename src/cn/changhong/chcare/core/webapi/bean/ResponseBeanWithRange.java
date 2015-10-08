/**     
 * @Title: ResponseBeanWithRange.java  
 * @Package cn.changhong.chcare.core.webapi.bean  
 * @Description: TODO  
 * @author guoyang2011@gmail.com    
 * @date 2014-9-17 下午2:12:13  
 * @version V1.0     
 */
package cn.changhong.chcare.core.webapi.bean;

import java.io.Serializable;

/**
 * @ClassName: ResponseBeanWithRange
 * @Description: TODO
 * @author guoyang2011@gmail.com
 * @date 2014-9-17 下午2:12:13
 * 
 */
public class ResponseBeanWithRange<T> extends ResponseBean<T> implements Serializable{
	/**  
	 * @Fields serialVersionUID : TODO  
	*/
	private static final long serialVersionUID = 1L;
	private int Limit;
	private int Count;
	private int StartIndex;
	private int EndIndex;
	
	private long Timestamp;

	public int getLimit() {
		return Limit;
	}

	public void setLimit(int limit) {
		Limit = limit;
	}

	public int getCount() {
		return Count;
	}

	public void setCount(int count) {
		Count = count;
	}

	public int getStartIndex() {
		return StartIndex;
	}

	public void setStartIndex(int startIndex) {
		StartIndex = startIndex;
	}

	public int getEndIndex() {
		return EndIndex;
	}

	public void setEndIndex(int endIndex) {
		EndIndex = endIndex;
	}

	public long getTimestamp() {
		return Timestamp;
	}

	public void setTimestamp(long timestamp) {
		Timestamp = timestamp;
	}


}
