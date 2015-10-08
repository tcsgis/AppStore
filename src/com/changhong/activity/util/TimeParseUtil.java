package com.changhong.activity.util;

/**     
 * @Title: PictureUtil.java  
 * @Package com.changhong.activity.util  
 * @Description:Time工具类
 * @author quxy 
 */
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.changhong.util.CHLogger;

public class TimeParseUtil {

	public static SimpleDateFormat getFormat(String partten) {
		return new SimpleDateFormat(partten);
	}

	/**
	 * 计算月数
	 * 
	 * @return
	 */
	private static int calculationDaysOfMonth(int year, int month) {
		int day = 0;
		switch (month) {
		// 31天
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			day = 31;
			break;
		// 30天
		case 4:
		case 6:
		case 9:
		case 11:
			day = 30;
			break;
		// 计算2月天数
		case 2:
			day = year % 100 == 0 ? year % 400 == 0 ? 29 : 28
					: year % 4 == 0 ? 29 : 28;
			break;
		}

		return day;
	}

	/**
	 * 
	 * @param dateStr
	 *            时间字符串
	 * @param format
	 *            转换格式如"yyyy-MM-dd"
	 * @return 把时间字符串转换成Date,如果dateStr非时间字符串或format格式不对返回为null
	 */
	public static Date stringToDate(String dateStr, String format) {
		if (format == null || dateStr == null || format.length() < 10) {
			return null;
		}

		Date date = null;
		try {
			SimpleDateFormat formatDate = new SimpleDateFormat(format);
			date = formatDate.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 
	 * @param date
	 *            时间
	 * @param format
	 *            转换格式如"yyyy-MM-dd"
	 * @return 把Date转换成时间字符串,如果format格式不对返回为null
	 */
	public static String dateToString(Date date, String format) {
		if (format == null || date == null ) {
			return null;
		}

		String dateStr = "";

		try {
			SimpleDateFormat formatDate = new SimpleDateFormat(format);
			dateStr = formatDate.format(date);
			CHLogger.i("lyxung","时间是:"+dateStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateStr;
	}

	/*
	 * 获取系统当前时间
	 */
	public static String getSystemNowTime() {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(new Date().getTime());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		if (dateFormat.format(c.getTime()) == null) {
			return "camera";
		}
		return dateFormat.format(c.getTime());
	}
}
