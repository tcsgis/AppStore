package com.changhong.activity.widget;

import java.io.Serializable;

public class TextInputBean implements Serializable{
	
	private String textType;
	private int inputType = -1;
	private String content;
	private int unit = -1;
	private int title = -1;
	
	private int maxLen = -1;
	private String maxLenMsg = "";
	
	private int minLen = -1;
	private String minLenMsg = "";
	
	private String hintMsg = "";
	
	public String getTextType() {
		return textType;
	}
	
	/**
	 * 是否为大文本框
	 * @param isTextBox
	 */
	public void setTextType(boolean isTextBox) {
		
		if(isTextBox){
			this.textType = "TextBox";
		}else{
			this.textType = "Text";
		}
	}
	/**
	 * 显示的内容
	 * @return
	 */
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
	public int getUnit() {
		return unit;
	}
	
	/**
	 * 显示的单位
	 * @return
	 */
	public void setUnit(int unit) {
		this.unit = unit;
	}
	public int getTitle() {
		return title;
	}
	
	/**
	 * 显示的标题
	 * @return
	 */
	public void setTitle(int title) {
		this.title = title;
	}
	public int getMaxLen() {
		return maxLen;
	}
	
	/**
	 * 输入最大值限制,如果是输入数字就是最大值,文本就是输入长度
	 * @param maxLen
	 */
	public void setMaxLen(int maxLen) {
		this.maxLen = maxLen;
	}
	public String getMaxLenMsg() {
		return maxLenMsg;
	}
	
	/**
	 * 超过最大值提示信息
	 * @param maxLenMsg
	 */
	public void setMaxLenMsg(String maxLenMsg) {
		this.maxLenMsg = maxLenMsg;
	}
	public int getMinLen() {
		return minLen;
	}
	/**
	 * 输入最小值限制,如果是输入数字就是最大值,文本就是输入长度
	 * @param maxLen
	 */
	public void setMinLen(int minLen) {
		this.minLen = minLen;
	}
	public String getMinLenMsg() {
		return minLenMsg;
	}
	
	/**
	 * 超过最小值提示信息
	 * @param maxLenMsg
	 */
	public void setMinLenMsg(String minLenMsg) {
		this.minLenMsg = minLenMsg;
	}
	public int getInputType() {
		return inputType;
	}
	public void setInputType(int inputType) {
		this.inputType = inputType;
	}

	public String getHintMsg() {
		return hintMsg;
	}

	public void setHintMsg(String hintMsg) {
		this.hintMsg = hintMsg;
	}

}
