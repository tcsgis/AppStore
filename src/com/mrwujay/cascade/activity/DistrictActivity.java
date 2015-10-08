package com.mrwujay.cascade.activity;

import com.llw.AppStore.R;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

public class DistrictActivity extends DistrictBaseActivity implements OnClickListener, OnWheelChangedListener {
	public static final String DIVIDER = "-";
	
	private WheelView mViewProvince;
	private WheelView mViewCity;
	private WheelView mViewDistrict;
	private Button mBtnConfirm;
	private String curDistrict = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.district_activity);
			curDistrict = getIntent().getStringExtra("address");
			setUpViews();
			setUpListener();
			setUpData();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setUpViews() {
		mViewProvince = (WheelView) findViewById(R.id.id_province);
		mViewCity = (WheelView) findViewById(R.id.id_city);
		mViewDistrict = (WheelView) findViewById(R.id.id_district);
		mBtnConfirm = (Button) findViewById(R.id.btn_confirm);
	}
	
	private void setUpListener() {
    	mViewProvince.addChangingListener(this);
    	mViewCity.addChangingListener(this);
    	mViewDistrict.addChangingListener(this);
    	mBtnConfirm.setOnClickListener(this);
    }
	
	private void setUpData() {
		initProvinceDatas();
		mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(DistrictActivity.this, mProvinceDatas));
		mViewProvince.setVisibleItems(7);
		mViewCity.setVisibleItems(7);
		mViewDistrict.setVisibleItems(7);
		
		if(curDistrict == null || curDistrict.equals("")){
			mViewProvince.setCurrentItem(24);//si chuan
			mCurrentProviceName = mProvinceDatas[24];
			updateCities();
			updateAreas();
		}else{
			String[] ss = curDistrict.split(DIVIDER);
			for(int i = 0; i < mProvinceDatas.length; i++){
				if(mProvinceDatas[i].equals(ss[0])){
					mViewProvince.setCurrentItem(i);
					mCurrentProviceName = ss[0];
					break;
				}
			}
			
			String[] cities = mCitisDatasMap.get(mCurrentProviceName);
			if (cities == null) {
				cities = new String[] { "" };
			}
			for(int j = 0; j < cities.length; j++){
				if(cities[j].equals(ss[1])){
					mCurrentCityName = ss[1];
					mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
					mViewCity.setCurrentItem(j);
					break;
				}
			}
			
			String[] areas = mDistrictDatasMap.get(mCurrentCityName);
			if (areas == null) {
				areas = new String[] { "" };
			}
			for(int k = 0; k < areas.length; k++){
				if(areas[k].equals(ss[2])){
					mCurrentDistrictName = ss[2];
					mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
					mViewDistrict.setCurrentItem(k);
					break;
				}
			}
		}
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		// TODO Auto-generated method stub
		if (wheel == mViewProvince) {
			updateCities();
		} else if (wheel == mViewCity) {
			updateAreas();
		} else if (wheel == mViewDistrict) {
			mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
//			mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
		}
	}

	private void updateAreas() {
		int pCurrent = mViewCity.getCurrentItem();
		mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
		String[] areas = mDistrictDatasMap.get(mCurrentCityName);

		if (areas == null) {
			areas = new String[] { "" };
		}
		mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
		mViewDistrict.setCurrentItem(0);
		mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[0];
	}

	private void updateCities() {
		int pCurrent = mViewProvince.getCurrentItem();
		mCurrentProviceName = mProvinceDatas[pCurrent];
		String[] cities = mCitisDatasMap.get(mCurrentProviceName);
		if (cities == null) {
			cities = new String[] { "" };
		}
		mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
		mViewCity.setCurrentItem(0);
		updateAreas();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_confirm:
			Intent data = new Intent();
			String district = mCurrentProviceName + DIVIDER + mCurrentCityName + DIVIDER + mCurrentDistrictName;
			data.putExtra("address", district);
			setResult(RESULT_OK, data);
			finish();
			break;
		}
	}
}
