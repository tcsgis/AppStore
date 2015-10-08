package com.changhong.activity.widget.other.datepicker;


import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import com.llw.AppStore.R;
import com.changhong.activity.widget.other.datepicker.WheelScrollableView.ScrollListener;



/**
 * 时间选择器
 * 
 */
public class WheelTimePicker extends WheelBaseLayout {

	/** 
	 * 模式：CALENDAR_MODE=1 日历模式
	 * CUSTOM_MODE=2 自定义日期区间模式
	 */
	public static final int CALENDAR_MODE=1;
	public static final int CUSTOM_MODE=2;
	private static final int MSG_TIME_PICKED = 0;
	/** 滚轮：年 */
	private WheelView mWheelYear;
	/** 滚轮：月 */
	private WheelView mWheelMonth;
	/** 滚轮：日 */
	private WheelView mWheelDay;
	/** 滚轮适配器：年 */
	private WheelNumberWheelAdapter mAdapterYear;
	/** 滚轮适配器：月 */
	private WheelNumberWheelAdapter mAdapterMonth;
	/** 滚轮适配器：日*/
	private WheelNumberWheelAdapter mAdapterDay;
	/** 滚轮监听器*/
	private TimePickerListener mTimePickerListener;

	/**
	 * 当前 年、月、日
	 * 最大显示年份
	 * 控件模式
	 */
	private int nowYear=0;
	private int nowMonth=0;
	private int nowDay=0;
	private int maxYear=0;
	private int viewMode=CALENDAR_MODE;
	
	private int startYear,startMonth,startDay,endYear,endMonth,endDay
	,defaultYear,defaultMonth,defaultDay;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_TIME_PICKED:
				if (mTimePickerListener != null)
					mTimePickerListener.onPick(getDateTime());
				break;
			}
		};
	};

	public WheelTimePicker(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	protected void onInit(View inflater) {
		mWheelYear=(WheelView)inflater.findViewById(R.id.wheel_view_year);
		mWheelMonth=(WheelView)inflater.findViewById(R.id.wheel_view_month);
		mWheelDay=(WheelView)inflater.findViewById(R.id.wheel_view_day);
	}

	
	public void initMode(int mode){
		if(mode!=CALENDAR_MODE&&mode!=CUSTOM_MODE){
			viewMode=CALENDAR_MODE;
		}else{
			viewMode=mode;
		}
	}

	/*
	 * Custom模式下设置日期区间
	 */
	 public void setCustomDate(int startYear,int startMonth,int startDay,
			 int endYear,int endMonth,int endDay,int dYear,int dMonth,int dDay){

			 this.startYear=startYear;
			 this.startMonth=startMonth;
			 this.startDay=startDay;
			 this.endYear=endYear;
			 this.endMonth=endMonth;
			 this.endDay=endDay;
			 if((dYear!=0)&&(dMonth!=0)&&(dDay!=0)){
				 this.defaultYear=dYear;
				 this.defaultMonth=dMonth;
				 this.defaultDay=dDay;
			 }else{
				 defaultYear=startYear;
				 defaultDay=startDay;
				 defaultMonth=startMonth;
			 }
	 }

	 @Override
	 protected int onInitLayoutResId() {
		 return R.layout.ot_date_layout;
	 }


	 public void init(){

		 buildAdapters();

		 if(viewMode==CALENDAR_MODE){

			 mWheelYear.setAdapter(mAdapterYear);
			 mWheelYear.setCurrentValue(nowYear);
			 mWheelMonth.setAdapter(mAdapterMonth);
			 mWheelMonth.setCurrentValue(nowMonth);
			 mWheelDay.setAdapter(mAdapterDay);
			 mWheelDay.setCurrentValue(nowDay);

		 }

		 if(viewMode==CUSTOM_MODE){
			 mWheelYear.setAdapter(mAdapterYear);
			 mWheelYear.setCurrentValue(defaultYear);
			 mWheelMonth.setAdapter(mAdapterMonth);
			 mWheelMonth.setCurrentValue(defaultMonth);
			 mWheelDay.setAdapter(mAdapterDay);
			 mWheelDay.setCurrentValue(defaultDay);
		 }
		 ScrollListener listener = createScrollListener();
		 mWheelYear.setScrollListener(listener);
		 mWheelMonth.setScrollListener(listener);
		 mWheelDay.setScrollListener(listener);
		 mWheelYear.getCurrentItemString();
	 }

	/**
	  * 滑动事件监听
	  * 
	  * @return ScrollListener
	  */
	 private ScrollListener createScrollListener() {
		 return new ScrollListener() {

			 @Override
			 public void onScrollEnd(View v) {
				 onSelected(v);
				 mHandler.removeMessages(MSG_TIME_PICKED);
				 mHandler.sendEmptyMessage(MSG_TIME_PICKED);
			 }
		 };
	 }
	 public void onSelected(View v) {
		 computeMaxDay(mWheelYear, mWheelMonth);
	 }

	 /**
	  * 创建适配器
	  */
	 private void buildAdapters() { 	
		 if(viewMode==CALENDAR_MODE){
			 int maxDay=findMaxDay(nowYear, nowMonth);
			 mAdapterYear = new WheelNumberWheelAdapter(maxYear-100, maxYear+1, 1, "年");
			 mAdapterMonth = new WheelNumberWheelAdapter(1, 13, 1, "月");
			 mAdapterDay = new WheelNumberWheelAdapter(1, maxDay+1, 1, "日");
		 }

		 if(viewMode==CUSTOM_MODE){
			 int maxDay=findMaxDay(defaultYear, defaultMonth);
			 mAdapterYear = new WheelNumberWheelAdapter(startYear, endYear+1, 1, "年");
			 mAdapterMonth = new WheelNumberWheelAdapter(1, 13, 1, "月");
			 mAdapterDay = new WheelNumberWheelAdapter(1, maxDay+1, 1, "日");
		 }
	 }
	 /**
	  * 计算最大日期
	  * @param year
	  * @param month
	  */
	 private void computeMaxDay(WheelView year,WheelView month){
		
			int maxDay = findMaxDay(year.getCurrentValue(),
					month.getCurrentValue());
			WheelNumberWheelAdapter adapterDay = new WheelNumberWheelAdapter(1,
					maxDay + 1, 1, "日");
			mWheelDay.setAdapter(adapterDay);

		/*
		 * 动态加载待做
		 */
//		 if(viewMode==CUSTOM_MODE){
//			 int maxDay = findMaxDay(year.getCurrentValue(),
//						month.getCurrentValue());
//			 if(year.getCurrentValue()==startYear){
//				 mAdapterMonth = new WheelNumberWheelAdapter(startMonth, 13, 1, "月");
//				 mAdapterDay = new WheelNumberWheelAdapter(startDay, maxDay+1, 1, "日");
//			 }
//			 else if(year.getCurrentValue()==endYear){
//				 mAdapterMonth = new WheelNumberWheelAdapter(1, endMonth+1, 1, "月");
//				 if(month.getCurrentValue()==endMonth){
//					 mAdapterDay = new WheelNumberWheelAdapter(1, endDay+1, 1, "日");
//				 }else{
//					 mAdapterDay = new WheelNumberWheelAdapter(1, maxDay+1, 1, "日");
//				 }
//			 }
//			 else{	 
//				 mAdapterMonth = new WheelNumberWheelAdapter(1, 13, 1, "月");		 
//				 mAdapterDay = new WheelNumberWheelAdapter(1, maxDay+1, 1, "日");
//			 }
//
//			 mWheelMonth.setAdapter(mAdapterMonth);
//			 mWheelDay.setAdapter(mAdapterDay);
//		 }

		 mAdapterMonth.notifyChanged();
		 mAdapterDay.notifyChanged();
	 }

	 private int findMaxDay(int year,int month){
		 int maxDay=0;
		 Calendar calendar=Calendar.getInstance();
		 calendar.set(Calendar.YEAR, year);
		 calendar.set(Calendar.MONTH,month-1);  	
		 maxDay=calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		 return maxDay;
	 }
	 /**
	  * 获取选择的时间
	  * 
	  * @return calendar.getTime()
	  */
	 public Date getDateTime() {
		 Calendar calendar=Calendar.getInstance();
		 calendar.set(mWheelYear.getCurrentValue(), mWheelMonth.getCurrentValue()-1,mWheelDay.getCurrentValue(), 0, 0, 0);	 
		 return calendar.getTime();
	 }
	 public void setTimePickerListener(TimePickerListener l) {
		 this.mTimePickerListener = l;
	 }
	 public interface TimePickerListener {
		 void onPick(Date time);
	 }
	 public void setSelectedYear(int year){
		 nowYear=year;
	 }
	 public void setSelectedMonth(int month){
		 nowMonth=month;
	 }
	 public void setSelectedDay(int day){
		 nowDay=day;
	 }
	 public void setMaxYear(int year){
		maxYear=year;
	 }
}
