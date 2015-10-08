package com.changhong.activity.widget.other.datepicker;

import java.util.ArrayList;
import java.util.HashMap;

import com.llw.AppStore.R;
import com.changhong.activity.widget.other.datepicker.WheelScrollableView.ScrollListener;
import com.changhong.util.CollectionUtils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

public class WheelDoubleRollPicker extends WheelBaseLayout {
	private static final int MSG_DOUBLE_PICKED = 1;
	private WheelView mWheelDouble1,mWheelDouble2;
	private WheelArrayAdapter mAdapter1,mAdapter2;
	private String mCurrentStr1,mCurrentStr2;
	private ArrayList<String>mContentList1=new ArrayList<String>();
	private ArrayList<String>mContentList2=new ArrayList<String>();
	private DoublePickerListener mDoublePickerListener;
	private ArrayList<HashMap<String, Object>> mDataList=new ArrayList<HashMap<String,Object>>();
	private String mKey1,mKey2;
	private int mFirstN,mSecondN;
	
	public WheelDoubleRollPicker(Context context, AttributeSet attrs) {
		super(context, attrs);
	}


	protected void onInit(View inflater) {
		mWheelDouble1=(WheelView)inflater.findViewById(R.id.wheel_double_roll1);
		mWheelDouble2=(WheelView)inflater.findViewById(R.id.wheel_double_roll2);
	}
	@Override
	protected int onInitLayoutResId() {
		return R.layout.ot_double_roll_layout;
	}
	
	public void init(){
		buildAdapter();
		mWheelDouble1.setAdapter(mAdapter1);
		mWheelDouble1.setCurrentStringValue(mCurrentStr1);
		mWheelDouble2.setAdapter(mAdapter2);
		mWheelDouble2.setCurrentStringValue(mCurrentStr2);
		ScrollListener listener = createScrollListener();
		mWheelDouble1.setScrollListener(listener);
		mWheelDouble2.setScrollListener(listener);
	}


	private ScrollListener createScrollListener() {
			ScrollListener listener=new ScrollListener() {
			
			@Override
			public void onScrollEnd(View v) {
				updateSecondList();
				mHandler.sendEmptyMessage(MSG_DOUBLE_PICKED);
			}
		};
		return listener;
	}
	private void updateSecondList(){
		try {
				String str=mWheelDouble1.getCurrentItemString();
				int position=findPosition(mContentList1, str);
				mFirstN=position;
				ArrayList<String> contentList=getSecondList(mDataList, position);
				WheelArrayAdapter adapter=new WheelArrayAdapter(contentList);
				mWheelDouble2.setAdapter(adapter);
				mAdapter2.notifyChanged();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	private void buildAdapter() {
		
		if(!CollectionUtils.isEmpty(mDataList)){
			mContentList1=getFirstList(mDataList);
			int positionF=findPosition(mContentList1,mCurrentStr1);
			mContentList2=getSecondList(mDataList,positionF);
		}
		
		mAdapter1=new WheelArrayAdapter(mContentList1);
		mAdapter2=new WheelArrayAdapter(mContentList2);
		
	}
	
	private int findPosition(ArrayList<String>list,String str) {
		int position=0;
		for(int i=0;i<list.size();i++){
			if(list.get(i).equals(str)){
				position=i;
				break;
			}
		}
		
		return position;
	}

	private ArrayList<String> getSecondList(
			ArrayList<HashMap<String, Object>> dataList, int positionF) {

		ArrayList<String> list=new ArrayList<String>();
		for (int i = 0; i < dataList.size(); i++) {
			int sBelongCode = (Integer) dataList.get(i).get(mKey1);
			String sName =dataList.get(i).get(mKey2).toString();
				if (sBelongCode == positionF +1) {
					list.add(sName);
					mSecondN=i;
				}

				if(sBelongCode==-2){
					list.add(sName);
			}
		
		}
		return list;
	}


	private ArrayList<String> getFirstList(
			ArrayList<HashMap<String, Object>> dataList) {
		ArrayList<String> list=new ArrayList<String>();
		for (int i = 0; i < dataList.size(); i++) {
			int sBelongCode = (Integer)dataList.get(i).get(mKey1);
			String sName = dataList.get(i).get(mKey2).toString();
			if(sBelongCode == 0){
				list.add(sName);
			}else if(sBelongCode==-1){
				list.add(sName);
			}
		}
		return list;
	}
		
	private Handler mHandler = new Handler(){
		public void handleMessage(Message msg){
			switch(msg.what){
			case MSG_DOUBLE_PICKED:
				try {
					if(mDoublePickerListener!=null){
						mDoublePickerListener.onDoublePick(stringFactory(mWheelDouble1.getCurrentItemString(), mWheelDouble2.getCurrentItemString()),mFirstN,mSecondN);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
		};
	};
	private String[] stringFactory(String str1,String str2){
		String[] array = new String[2];
		array[0]=str1;
		array[1]=str2;
		return array;
	}
	
	public void setDoublePickerListener(DoublePickerListener l) {
		 this.mDoublePickerListener = l;
	 }
	 
	public interface DoublePickerListener{
		void onDoublePick(String[] array, int mFirstN, int mSecondN);
	}
	
	public void setCurrentStr(String str1,String str2){
		this.mCurrentStr1=str1;
		this.mCurrentStr2=str2;
	}
	public void setKeyStr(String key1,String key2){
		this.mKey1=key1;
		this.mKey2=key2;
	}
	public void setDataList(ArrayList<HashMap<String, Object>> datalist){
		mDataList=datalist;
	}
	
}
