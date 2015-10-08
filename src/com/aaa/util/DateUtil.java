package com.aaa.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DateUtil {

    /** 锁对象 */
    private static final Object lockObj = new Object();

    /** 存放不同的日期模板格式的sdf的Map */
    private static Map<String, ThreadLocal<SimpleDateFormat>> sdfMap = new HashMap<String, ThreadLocal<SimpleDateFormat>>();

    /**
     * 返回一个ThreadLocal的sdf,每个线程只会new一次sdf
     * 
     * @param pattern
     * @return
     */
    private static SimpleDateFormat getSdf(final String pattern) {
        ThreadLocal<SimpleDateFormat> tl = sdfMap.get(pattern);

        // 此处的双重判断和同步是为了防止sdfMap这个单例被多次put重复的sdf
        if (tl == null) {
            synchronized (lockObj) {
                tl = sdfMap.get(pattern);
                if (tl == null) {
                    // 只有Map中还没有这个pattern的sdf才会生成新的sdf并放入map
                    System.out.println("put new sdf of pattern " + pattern + " to map");

                    // 这里是关键,使用ThreadLocal<SimpleDateFormat>替代原来直接new SimpleDateFormat
                    tl = new ThreadLocal<SimpleDateFormat>() {

                        @Override
                        protected SimpleDateFormat initialValue() {
                            System.out.println("thread: " + Thread.currentThread() + " init pattern: " + pattern);
                            return new SimpleDateFormat(pattern, Locale.CHINA);
                        }
                    };
                    sdfMap.put(pattern, tl);
                }
            }
        }

        return tl.get();
    }

    /**
     * 是用ThreadLocal<SimpleDateFormat>来获取SimpleDateFormat,这样每个线程只会有一个SimpleDateFormat
     * 
     * @param date
     * @param pattern
     * @return
     */
    public static String format(Date date, String pattern) {
        return getSdf(pattern).format(date);
    }

    public static Date parse(String dateStr, String pattern) throws ParseException {
        return getSdf(pattern).parse(dateStr);
    }
    
    /**
     * @param date
     * @return 周一 周二。。
     */
    public static String getDayOfWeek(Date date){
    	return getSdf("E").format(date);
    }
    
    public static int getDayInMonth(Date date){
    	return Integer.valueOf(getSdf("d").format(date));
    }

    public static int getDayInYear(Date date){
    	return Integer.valueOf(getSdf("D").format(date));
    }
    
    public static int getMonth(Date date){
    	return Integer.valueOf(getSdf("M").format(date));
    }
    
    /**
     * @param date
     * @return 0~23
     */
    public static int getHour(Date date){
    	return Integer.valueOf(getSdf("H").format(date));
    }
    
    public static int getMinute(Date date){
    	return Integer.valueOf(getSdf("m").format(date));
    }
    
    /**
     * @param date1
     * @param date2
     * @return dayDiff = date1 - date2
     */
    public static int getDayDiff(Date date1, Date date2){
    	return (int) ((date1.getTime() - date2.getTime()) / 86400000);
    }
}
