package com.changhong.activity.widget.other.datepicker;

import java.util.ArrayList;

import com.llw.AppStore.R;
import com.changhong.activity.widget.other.datepicker.WheelScrollableView.ScrollListener;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

public class WheelOnRollPicker extends WheelBaseLayout{

	private static final int MSG_GENDER_PICKED = 0;
	private WheelView mWheelGender;

	private WheelArrayAdapter mAdapterGender;
	private GenderPickerListener mGenderPickerListener;
	private ArrayList<String> mContentList;
	private String mCurrentStr;
	
	public WheelOnRollPicker(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	protected void onInit(View inflater) {
			mWheelGender=(WheelView)inflater.findViewById(R.id.wheel_view_gender);
	}
	

	@Override
	protected int onInitLayoutResId() {
		return R.layout.ot_oneroll_layout;
	}
	
	public void init(){
		buildAdapter();
		mWheelGender.setAdapter(mAdapterGender);
		mWheelGender.setCurrentStringValue(mCurrentStr);
		ScrollListener listener = createScrollListener();
		mWheelGender.setScrollListener(listener);
	}
	
	private ScrollListener createScrollListener() {
		ScrollListener listener=new ScrollListener() {
			
			@Override
			public void onScrollEnd(View v) {
				mHandler.sendEmptyMessage(MSG_GENDER_PICKED);
			}
		};
		return listener;
	}
	private void buildAdapter() {
		if(!mContentList.isEmpty())
			mAdapterGender=new WheelArrayAdapter(mContentList);
	}
	
	private Handler mHandler = new Handler(){
		public void handleMessage(Message msg){
			switch(msg.what){
			case MSG_GENDER_PICKED:
				if(mGenderPickerListener!=null)
					mGenderPickerListener.onPick(mWheelGender.getCurrentItemString());
				break;
			}
		};
	};
	
	
	
	public void setGenderPickerListener(GenderPickerListener l) {
		 this.mGenderPickerListener = l;
	 }
	 
	public interface GenderPickerListener{
		void onPick(String gender);
	}
	public void setContentList(ArrayList<String> list){
		this.mContentList=list;
	}
	public void setCurrentStr(String str){
		this.mCurrentStr=str;
	}
}
