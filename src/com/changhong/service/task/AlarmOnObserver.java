package com.changhong.service.task;

import java.util.Date;

import android.os.Bundle;

public interface AlarmOnObserver {

	/**
	 * 轮询操作
	 * @param type
	 */
	public Bundle onTimeOut(Date date);
	
	/**
	 * 退出轮询时的操作
	 * @return
	 */
	public void doExit();
	
}
