package com.changhong.activity.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SpecialCalendar {

	private int daysOfMonth = 0;      //ĳ�µ�����
	private int dayOfWeek = 0;        //����ĳһ�������ڼ�
	private int eachDayOfWeek = 0;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	
	// �ж��Ƿ�Ϊ����
	public boolean isLeapYear(int year) {
		if (year % 100 == 0 && year % 400 == 0) {
			return true;
		} else if (year % 100 != 0 && year % 4 == 0) {
			return true;
		}
		return false;
	}

	//�õ�ĳ���ж�������
	public int getDaysOfMonth(boolean isLeapyear, int month) {
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			daysOfMonth = 31;
			break;
		case 4:
		case 6:
		case 9:
		case 11:
			daysOfMonth = 30;
			break;
		case 2:
			if (isLeapyear) {
				daysOfMonth = 29;
			} else {
				daysOfMonth = 28;
			}

		}
		return daysOfMonth;
	}
	
	public int getWeekdayOfMonth(int year, int month){
		Calendar cal = Calendar.getInstance();
		cal.set(year, month-1, 1);
		dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)-1;
		return dayOfWeek;
	}
	public int getWeekDayOfLastMonth(int year,int month,int day){
		Calendar cal = Calendar.getInstance();
		cal.set(year, month-1, day);
		eachDayOfWeek = cal.get(Calendar.DAY_OF_WEEK)-1;
		return eachDayOfWeek;
	}
	
	/**
	 * 判断某年某月所有的星期数
	 * 
	 * @param year
	 * @param month
	 */
	public int getWeeksOfMonth(int year, int month) {
		int weeksOfMonth = 0;
		// 先判断某月的第一天为星期几
		int preMonthRelax = 0;
		int dayFirst = getWeekdayOfMonth(year, month);
		int days = getDaysOfMonth(isLeapYear(year), month);
		if (dayFirst != 7) {
			preMonthRelax = dayFirst;
		}
		if ((days + preMonthRelax) % 7 == 0) {
			weeksOfMonth = (days + preMonthRelax) / 7;
		} else {
			weeksOfMonth = (days + preMonthRelax) / 7 + 1;
		}
		return weeksOfMonth;

	}

}
