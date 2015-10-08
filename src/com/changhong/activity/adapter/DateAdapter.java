package com.changhong.activity.adapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.changhong.activity.util.SpecialCalendar;
import com.changhong.util.CHLogger;
import com.llw.AppStore.R;

public class DateAdapter extends BaseAdapter {
    private final Calendar calendar = Calendar.getInstance();
    private boolean isLeapyear = false; 
    private int daysOfMonth = 0; //此月总天数
    private int dayOfWeek = 0; //某月一号是星期几
    private int lastDaysOfMonth = 0; //上月总天数
    private Context context;
    private SpecialCalendar sc;
    private String[] dayNumber = new String[7];//这7天分别是几号
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    
    private String sysDate = "";
    private String sys_year = "";
    private String sys_month = "";
    private String sys_day = "";
    private String currentYear = "";
    private String currentMonth = "";
    private String currentWeek = "";
    private int weeksOfMonth;
    private int clickTemp = -1;//点击的第几会变化
    private int temp = 0;//adapter初始化时选择的第几
    private boolean isStart;

    //点击的位置
    public void setSeclection(int position) {
        clickTemp = position;
    }
    

    public DateAdapter() {
        Date date = new Date();
        sysDate = sdf.format(date); 
        sys_year = sysDate.split("-")[0];
        sys_month = sysDate.split("-")[1];
        sys_day = sysDate.split("-")[2];
    }

    public DateAdapter(Context context, Resources rs, Date date, int year_c, int month_c,
            int week_c, int week_num, int default_postion, boolean isStart) {
        this();
        this.context = context;
        this.isStart = isStart;
        sc = new SpecialCalendar();

        currentYear = String.valueOf(year_c);
        currentMonth = String.valueOf(month_c); 
                                               
        getCalendar(Integer.parseInt(currentYear),
                Integer.parseInt(currentMonth));
        currentWeek = String.valueOf(week_c);
        getWeek(Integer.parseInt(currentYear), Integer.parseInt(currentMonth),
                Integer.parseInt(currentWeek));

    }

    //获取今天的位置
    public int getTodayPosition() {
        int todayWeek = sc.getWeekDayOfLastMonth(Integer.parseInt(sys_year),
                Integer.parseInt(sys_month), Integer.parseInt(sys_day));
        if (todayWeek == 7) {
            clickTemp = 0;
            temp = 0;
        } else {
            clickTemp = todayWeek;
            temp = todayWeek;
        }
        return clickTemp;
    }
//当前第几月
    public int getCurrentMonth(int position) {
        int thisDayOfWeek = sc.getWeekdayOfMonth(Integer.parseInt(currentYear),
                Integer.parseInt(currentMonth));
        if (isStart) {
            if (thisDayOfWeek != 7) {
                if (position < thisDayOfWeek) {
                    return Integer.parseInt(currentMonth) - 1 == 0 ? 12
                            : Integer.parseInt(currentMonth) - 1;
                } else {
                    return Integer.parseInt(currentMonth);
                }
            } else {
                return Integer.parseInt(currentMonth);
            }
        } else {
            return Integer.parseInt(currentMonth);
        }

    }
//当前第几年
    public int getCurrentYear(int position) {
        int thisDayOfWeek = sc.getWeekdayOfMonth(Integer.parseInt(currentYear),
                Integer.parseInt(currentMonth));
        if (isStart) {
            if (thisDayOfWeek != 7) {
                if (position < thisDayOfWeek) {
                    return Integer.parseInt(currentMonth) - 1 == 0 ? Integer
                            .parseInt(currentYear) - 1 : Integer
                            .parseInt(currentYear);
                } else {
                    return Integer.parseInt(currentYear);
                }
            } else {
                return Integer.parseInt(currentYear);
            }
        } else {
            return Integer.parseInt(currentYear);
        }
    }

    public void getCalendar(int year, int month) {
        isLeapyear = sc.isLeapYear(year); 
        daysOfMonth = sc.getDaysOfMonth(isLeapyear, month); 
        dayOfWeek = sc.getWeekdayOfMonth(year, month); 
        lastDaysOfMonth = sc.getDaysOfMonth(isLeapyear, month - 1);
    }

    public void getWeek(int year, int month, int week) {
        for (int i = 0; i < dayNumber.length; i++) {
            if (dayOfWeek == 7) {
                dayNumber[i] = String.valueOf((i + 1) + 7 * (week - 1));
            } else {
                if (week == 1) {
                    if (i < dayOfWeek) {
                        dayNumber[i] = String.valueOf(lastDaysOfMonth
                                - (dayOfWeek - (i + 1)));
                    } else {
                        dayNumber[i] = String.valueOf(i - dayOfWeek + 1);
                    }
                } else {
                    dayNumber[i] = String.valueOf((7 - dayOfWeek + 1 + i) + 7
                            * (week - 2));
                }
            }
        }

    }

    public String[] getDayNumbers() {
        return dayNumber;
    }
    
    public String getDayNumber(int seq){
    	
    	if(seq < 7){
    		return dayNumber[seq];
    	}else{
    		return null;
    	}
    }

    /**
     * 此月共有几个星期
     */
    public int getWeeksOfMonth() {
        // getCalendar(year, month);
        int preMonthRelax = 0;
        if (dayOfWeek != 7) {
            preMonthRelax = dayOfWeek;
        }
        if ((daysOfMonth + preMonthRelax) % 7 == 0) {
            weeksOfMonth = (daysOfMonth + preMonthRelax) / 7;
        } else {
            weeksOfMonth = (daysOfMonth + preMonthRelax) / 7 + 1;
        }
        return weeksOfMonth;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return dayNumber.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_calendar, null);
        }
        int tempDay = Integer.parseInt(dayNumber[position]);
        int tYear = calendar.get(Calendar.YEAR);
        int tMonth = calendar.get(Calendar.MONTH)+1;
        int tDay = calendar.get(Calendar.DAY_OF_MONTH);
        
        String now = tYear + "." + tMonth + "." + tDay;
        String viewDate = getCurrentYear(position) + "." + getCurrentMonth(position) + "." + tempDay;
        
        TextView tvCalendar = (TextView) convertView.findViewById(R.id.tv_calendar);
        
        tvCalendar.setText(String.valueOf((tempDay < 10) ? "0" + tempDay
                : tempDay));
        if(clickTemp == temp) {
        	CHLogger.i("lyxung","dianji 1");
            if (viewDate.equals(String.valueOf(now))) {
                tvCalendar.setBackgroundResource(R.drawable.currentday);
                tvCalendar.setTextColor(Color.parseColor("#FFFFFF"));
                tvCalendar.setGravity(Gravity.CENTER);
                CHLogger.i("lyxung","dianji 1-0");
            }
        }
        if (clickTemp == position) {
            tvCalendar.setSelected(true);
            CHLogger.i("lyxung","dianji 2");
            tvCalendar.setTextColor(Color.parseColor("#3DD1CF"));
            if (viewDate.equals(String.valueOf(now))) {
            	 CHLogger.i("lyxung","dianji 2-0");
                tvCalendar.setBackgroundResource(R.drawable.currentday);
                tvCalendar.setTextColor(Color.parseColor("#FFFFFF"));
                tvCalendar.setGravity(Gravity.CENTER);
            }
        } else {
            tvCalendar.setSelected(false);
            CHLogger.i("lyxung","dianji 3");
            if(!viewDate.equals(String.valueOf(now))) {
            	 CHLogger.i("lyxung","dianji 3-0");
                tvCalendar.setTextColor(Color.parseColor("#3c3c3c"));
                tvCalendar.setBackgroundColor(Color.TRANSPARENT);
            } else {
            	 CHLogger.i("lyxung","dianji 3-3");
                tvCalendar.setBackgroundResource(R.drawable.currentday);
                tvCalendar.setTextColor(Color.parseColor("#FFFFFF"));
                tvCalendar.setGravity(Gravity.CENTER);
            }
        }

        return convertView;
    }

}
