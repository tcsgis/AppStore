package cn.changhong.chcare.core.webapi.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChCareDateFormatProvider {
	private static String FORMAT_TYPE = "yyyy-MM-dd'T'HH:mm:ss";

	public static Date transStrToDate(String dateStr) {
		DateFormat format = new SimpleDateFormat(FORMAT_TYPE);
		Date result = null;
		try {
			result = format.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
}
