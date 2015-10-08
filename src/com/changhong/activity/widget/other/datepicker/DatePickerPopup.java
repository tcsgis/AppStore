package com.changhong.activity.widget.other.datepicker;

import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.changhong.activity.util.TimeParseUtil;
import com.changhong.activity.widget.other.datepicker.WheelTimePicker.TimePickerListener;
import com.llw.AppStore.R;

public class DatePickerPopup extends PopupWindow implements TimePickerListener,
OnClickListener {

	private static final String yyyyMMdd = "yyyy-MM-dd";
	private WheelTimePicker mTimePicker;
	private Activity mActivity;
	private Button btnComfirm;
	private Date mTime;
	public Date mSystemNowTime;
	public static final int SUBMIT_BIRTHDAY = 101;
	public Handler mHandler = new Handler();
	private Date mLastBirthday;
	private int mMode=WheelTimePicker.CALENDAR_MODE;
	private Date mStartDate,mEndDate;
	/**
	 * 日期选择窗口：日历形式，最早的日期为当前年份的前100年，最晚日期为当前年份12月，默认不能选择当前日期之后的日期,
	 * 日期选择窗口：自定义区间形式，自定义最早年月日和最晚年月日以及默认当前选择年月日，显示区间为最早年——最晚年的日期区间，选择区间为自定义区间，超过则提示
	 * @param context
	 * @param handler
	 * @param startDate 开始日期，日历模式下为null，自定义模式下开始日期早于结束日期
	 * @param endDate  结束日期，日历模式下为null
	 * @param currentDate 默认当前选择日期，如果为null，则默认为开始日期
	 * @param mode WheelTimePicker.CALENDAR_MODE：日历模式  ，WheelTimePicker.CUSTOM_MODE：自定义模式
	 * 
	 */
	public DatePickerPopup(Activity activity,Handler handler,Date startDate,Date endDate,Date currentDate,int mode){
		this.mActivity = activity;
		initView(activity);
		mMode = mode;
		if(mMode==WheelTimePicker.CUSTOM_MODE){
			if(startDate == null){
				startDate = TimeParseUtil.stringToDate("1920-10-1", yyyyMMdd); 
			} 
			this.mStartDate =startDate;


			if(endDate == null){
				endDate = TimeParseUtil.stringToDate("2100-10-1", yyyyMMdd); 
			} 

			this.mEndDate=endDate;
		}
		if(handler!=null){
			mHandler=handler;
		}
		mSystemNowTime = new Date();

		setShowDate(currentDate);
	}

	public void setShowDate(Date date) {

		if (date == null) {
			mLastBirthday = new Date();
		}else {
			mLastBirthday = date;
		}
		refreshView();
	}

	private void refreshView(){
		if (mLastBirthday == null) {
			mLastBirthday = new Date();
		}		
		String lastStr = TimeParseUtil.dateToString(mLastBirthday, yyyyMMdd);
		String systemStr = TimeParseUtil.dateToString(mSystemNowTime, yyyyMMdd);
		String []date = lastStr.split("-");
		String []nowDate = systemStr.split("-");
		int year=Integer.parseInt(date[0]);
		int month=Integer.parseInt(date[1]);
		int day=Integer.parseInt(date[2]);
		
		if(mMode == WheelTimePicker.CALENDAR_MODE){
			
			int maxYear=Integer.parseInt(nowDate[0]);
			mTimePicker.setSelectedYear(year);
			mTimePicker.setSelectedMonth(month);
			mTimePicker.setSelectedDay(day);
			mTimePicker.setMaxYear(maxYear);
		}else{
	
			String strStartDate=TimeParseUtil.dateToString(mStartDate, yyyyMMdd);
			String []startD=strStartDate.split("-");
			String strEndDate=TimeParseUtil.dateToString(mEndDate, yyyyMMdd);
			String []endD=strEndDate.split("-");
			mTimePicker.setCustomDate(Integer.parseInt(startD[0]), Integer.parseInt(startD[1])-1, Integer.parseInt(startD[2]),
					Integer.parseInt(endD[0]), Integer.parseInt(endD[1])-1, Integer.parseInt(endD[2]), 
					year, month, day);

		}

		mTimePicker.initMode(mMode);
		mTimePicker.init();
		mTimePicker.setTimePickerListener(this);
	}

	private void initView(Context context){
		LayoutInflater inflater = LayoutInflater.from(context);
		RelativeLayout layout = (RelativeLayout) inflater.inflate(
				R.layout.ot_date_popup, null);
		mTimePicker = (WheelTimePicker) layout
				.findViewById(R.id.popup_time_picker);
		btnComfirm = (Button) layout.findViewById(R.id.btn_comfirm);
		btnComfirm.setOnClickListener(this);
		this.setContentView(layout);
		this.setWidth(LayoutParams.MATCH_PARENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setAnimationStyle(R.style.popup_alt_anim);
		this.setFocusable(true);
		ColorDrawable dw = new ColorDrawable(Color.WHITE);
		this.setBackgroundDrawable(dw);
		WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();  
		lp.alpha = 0.35f;  
		mActivity.getWindow().setAttributes(lp);
		this.update();
	}

	@Override
	public void onPick(Date time) {
		mTime = time;
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		switch (id) {

		case R.id.btn_comfirm:
			if (mTime != null&&mMode==WheelTimePicker.CALENDAR_MODE) {

				if (isReasonableDate(mSystemNowTime, mTime)) {
					submitDate(mTime);
					this.dismiss();
				}else{
					Toast.makeText(mActivity, mActivity.getResources().getString(R.string.select_birthday_unreasonable),
							Toast.LENGTH_SHORT).show();
				}
			} else if(mTime == null&&mMode==WheelTimePicker.CALENDAR_MODE){
				submitDate(mLastBirthday);
				this.dismiss();
				Log.e("DatePop", "mTime is null");
			}
			if(mTime!=null&&mMode==WheelTimePicker.CUSTOM_MODE){
				if (isRightDate(mStartDate, mEndDate, mTime)) {
					submitDate(mTime);
					this.dismiss();
				}else{
					Toast.makeText(mActivity, mActivity.getResources().getString(R.string.select_birthday_unreasonable),
							Toast.LENGTH_SHORT).show();
				}
			} else if(mTime == null&&mMode==WheelTimePicker.CUSTOM_MODE){

				submitDate(mLastBirthday);
				this.dismiss();
				Log.e("DatePop", "mTime is null");
			}
			break;
		}
	}

	private void submitDate(Date date){
		Message msg = Message.obtain();
		msg.what = SUBMIT_BIRTHDAY;
		msg.obj = date;
		if (mHandler != null) {
			mHandler.sendMessage(msg);
		}
	}

	private boolean isRightDate(Date startDate, Date endDate,
			Date currentDate) {

		return (!(currentDate.after(endDate)))&&(!(currentDate.before(startDate)));
	}
	private boolean isReasonableDate(Date nowTime, Date selectTime) {

		return selectTime.before(nowTime);
	}
	public void show(){
		this.showAtLocation(getContentView(), Gravity.BOTTOM, 0, 0);
	}
	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		super.dismiss();
		WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();  
        lp.alpha = 1f;  
        mActivity.getWindow().setAttributes(lp);
	}
}
