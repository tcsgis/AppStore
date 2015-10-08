package com.changhong.activity.widget.other.datepicker;

import java.util.ArrayList;
import java.util.HashMap;
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

import com.llw.AppStore.R;
import com.changhong.activity.widget.other.datepicker.WheelDoubleRollPicker.DoublePickerListener;
import com.changhong.activity.widget.other.datepicker.WheelOnRollPicker.GenderPickerListener;
import com.changhong.util.CollectionUtils;

public class RollPickerPopup extends PopupWindow implements GenderPickerListener,DoublePickerListener,OnClickListener{

	public static final int SUBMIT_INDEX=108;
	public static final int SUBMIT_STRING=109;
	public static final int SUBMIT_DOUBLE_STRING=110;
//	public static final int SUBMIT_DOUBLE_INDEX=111;
	private final String BELONG_CODE="sBelongCode";
	private final String VALUE_CODE="sName";
	private final String EMPTY_BYTE=" ";
	private WheelOnRollPicker mOneRollPicker;
	private WheelDoubleRollPicker mDoubleRollPicker;
	private Button btnComfirm;
	private String mPickedStr;
	private String[] mDoublePickArray;
	private Handler mHandler;
	private String mCurrentStr,mCurrentDoubleF,mCurrentDoubleS;
	private ArrayList<String>mContentList;
	private boolean isOneRoll=false;
	private boolean hasKey=false;
	private String[]keyArray;
	private Activity mActivity;
	private ArrayList<HashMap<String, Object>> mDataList=new ArrayList<HashMap<String,Object>>();
	private int mFirstN,mSecondN;
	/**
	 * 只有一个滚轮用于显示文字信息的滑动控件
	 * @param context
	 * @param handler   handler发送的msg为可选：
	 *                  SUBMIT_INDEX：发送String[]数组中所选项的Index
	 *                  SUBMIT_STRING：发送String[]数组中所选项的String值
	 *                  msg信息均以object类型打包
	 * @param contentStr  控件内需要展示的文字信息，以String[]数组的形式存放
	 * @param currentStr  控件当前初始化后默认显示已选择的String
	 */
	public RollPickerPopup(Activity activity,Handler handler,String[]contentStr,String currentStr){
		this.mActivity=activity;
		this.mHandler=handler;
		isOneRoll=true;

		if(!CollectionUtils.isEmpty(contentStr)){
			this.mContentList=Array2List(contentStr);
		}else{
			mContentList.add("null");
		}
		checkCurrentStr(new String[] {currentStr}, null, null, mContentList);
		initView(activity,mContentList,mCurrentStr);

	}
	/**
	 * 只有一个滚轮用于显示文字信息的滑动控件
	 * @param context
	 * @param handler   handler发送的msg为可选：
	 *                  SUBMIT_INDEX：发送String[]数组中所选项的Index
	 *                  SUBMIT_STRING：发送String[]数组中所选项的String值
	 *                  msg信息均以object类型打包
	 * @param contentList 控件内需要展示的文字信息，以ArrayList<String>的形式存放
	 * @param currentStr  控件当前初始化后默认显示已选择的String
	 */
	public RollPickerPopup(Activity activity,Handler handler,ArrayList<String>contentList,String currentStr){
		this.mActivity=activity;
		this.mHandler=handler;
		isOneRoll=true;
		if(!CollectionUtils.isEmpty(contentList)){
			this.mContentList=contentList;
		}else{
			mContentList.add("null");
		}
		checkCurrentStr(new String[] {currentStr}, null, null, mContentList);
		initView(activity,mContentList,mCurrentStr);
	}

	/**
	 * 有两个滚轮用于显示文字信息的滑动控件
	 * @param context
	 * @param handler  handler发送的msg：SUBMIT_DOUBLE_STRING：以String[]形式发送ArrayList中所选项的值,其中String[0]为第一个滚轮值，String[1]为第二个滚轮值
	 * @param dataList ArrayList<HashMap<String, Object>> 显示内容数据结构
	 * @param currentStr 当前默认显示值，str[0]第一个滚轮默认值，str[2]第二个滚轮默认值
	 * @param key  dataList中Map数据的key值，数组中有存放顺序，数据结构可以参照AreaCommend.class
	 */
	public RollPickerPopup(Activity activity,Handler handler,ArrayList<HashMap<String, Object>> dataList,String[] currentStr,String[] key){
		this.mActivity=activity;
		this.mHandler=handler;
		this.mDataList=dataList;
		isOneRoll=false;
		if(!CollectionUtils.isEmpty(dataList)&&!CollectionUtils.isEmpty(key)){
			hasKey=true;
			keyArray=key;
//			if(keyArray.length>3){
//				try {
//					throw new KeyArrayException("key的格式有误");
//				} catch (KeyArrayException e) {
//					e.printStackTrace();
//				}
//			}
			checkCurrentStr(currentStr, keyArray[1], dataList,null);
		}else if(CollectionUtils.isEmpty(dataList)||key==null){
			ArrayList<String> list=new ArrayList<String>();
			list.add(EMPTY_BYTE);
			dataList=List2List(list, list);
		}
		initView(activity,dataList,this.mCurrentDoubleF,this.mCurrentDoubleS);

	}
	/**
	 * 有两个滚轮用于显示文字信息的滑动控件
	 * @param context
	 * @param handler  handler发送的msg：SUBMIT_DOUBLE_STRING：以String[]形式发送ArrayList中所选项的值,其中String[0]为第一个滚轮值，String[1]为第二个滚轮值
	 * @param contentArray 二维数组形式显示内容，array[x][0]为第一个滚轮显示内容 ，array[x][y](y>0)为第一个滚轮的每一项对应第二个滚轮中的内容。
	 * @param currentStr 当前默认显示值，str[0]第一个滚轮默认值，str[2]第二个滚轮默认值
	 */
 public RollPickerPopup(Activity activity,Handler handler,String[][]contentArray,String[] currentStr){
		this.mActivity=activity;
		this.mHandler=handler;
		isOneRoll=false;
		ArrayList<HashMap<String, Object>> dataList=new ArrayList<HashMap<String,Object>>();
		if(!CollectionUtils.isEmpty(contentArray)){
			hasKey=false;
			dataList = Array2List(contentArray);
			checkCurrentStr(currentStr,VALUE_CODE,dataList,null);
		}else{
			String[][] array={{EMPTY_BYTE,EMPTY_BYTE}};
			dataList = Array2List(array);
		}

		initView(activity,dataList,this.mCurrentDoubleF,this.mCurrentDoubleS);

	}
	/**
	 * 有两个滚轮用于显示文字信息的滑动控件
	 * @param context
	 * @param handler  handler发送的msg：SUBMIT_DOUBLE_STRING：以String[]形式发送ArrayList中所选项的值,其中String[0]为第一个滚轮值，String[1]为第二个滚轮值
	 * @param listF 第一个滚轮显示内容的list
	 * @param listS 第二个滚轮显示内容的list
	 * @param currentStr 当前默认显示值，str[0]第一个滚轮默认值，str[2]第二个滚轮默认值
	 */
	public RollPickerPopup(Activity activity,Handler handler,ArrayList<String>listF,ArrayList<String>listS,String[] currentStr){
		this.mActivity=activity;
		this.mHandler=handler;
		isOneRoll=false;
		ArrayList<HashMap<String, Object>> dataList=new ArrayList<HashMap<String,Object>>();
		if(!CollectionUtils.isEmpty(listF)&&!CollectionUtils.isEmpty(listS)){
			hasKey=false;
			dataList = List2List(listF,listS);
			checkCurrentStr(currentStr,VALUE_CODE, dataList,null);

		}else{
			ArrayList<String> list=new ArrayList<String>();
			list.add(EMPTY_BYTE);
			List2List(list, list);
		}
		initView(activity,dataList,this.mCurrentDoubleF,this.mCurrentDoubleS);

	}

	private void checkCurrentStr(String[] currentStr,String key,ArrayList<HashMap<String, Object>> dataList,ArrayList<String> list){
		if(isOneRoll){
			if(CollectionUtils.isEmpty(currentStr)&&!CollectionUtils.isEmpty(list)){
				this.mCurrentStr=list.get(0);
			}else{
				this.mCurrentStr=currentStr[0];
			}
		}else{
			if(CollectionUtils.isEmpty(currentStr)){
				String str1,str2;
				if(!CollectionUtils.isEmpty(key)){
					str1=(String)dataList.get(0).get(key);
					str2=(String)dataList.get(1).get(key);
				}
				else{
					str1=EMPTY_BYTE;
					str2=EMPTY_BYTE;
				}
				this.mCurrentDoubleF = str1;
				this.mCurrentDoubleS = str2;
			}
			else{
				String strs[] = currentStr;
				this.mCurrentDoubleF = strs[0];
				this.mCurrentDoubleS = strs[strs.length == 1? 0:1];
			}
		}
	}

	private void initView(Context context,ArrayList<HashMap<String, Object>> dataList,String currentStr1,String currentStr2) {	
		try {
			RelativeLayout layout=initLayout(context,R.id.popup_double_picker,2);
			if(hasKey){
				mDoubleRollPicker.setKeyStr(keyArray[0], keyArray[1]);
				mDoubleRollPicker.setDataList(dataList);
			}
			else{
				mDoubleRollPicker.setKeyStr(BELONG_CODE, VALUE_CODE);
				mDoubleRollPicker.setDataList(dataList);
			}
			mDoubleRollPicker.setCurrentStr(currentStr1,currentStr2);
			mDoubleRollPicker.init();
			mDoubleRollPicker.setDoublePickerListener(this);
			mDoubleRollPicker.setVisibility(View.VISIBLE);
			initPop(layout);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initView(Context context,ArrayList<String> contentList,String currentStr) {
		try {
			RelativeLayout layout=initLayout(context,R.id.popup_gender_picker,1);
			if(!CollectionUtils.isEmpty(contentList)){
				mOneRollPicker.setContentList(contentList);
			}
			mOneRollPicker.setCurrentStr(currentStr);
			mOneRollPicker.init();
			mOneRollPicker.setGenderPickerListener(this);
			initPop(layout);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private RelativeLayout initLayout(Context context,int viewId,int wheelNum){
		LayoutInflater inflater = LayoutInflater.from(context);
		RelativeLayout layout = (RelativeLayout) inflater.inflate(
				R.layout.ot_oneroll_popup, null);
		switch(wheelNum){
		case 1:
			mOneRollPicker = (WheelOnRollPicker) layout
			.findViewById(viewId);
			break;
		case 2:
			mDoubleRollPicker = (WheelDoubleRollPicker) layout
			.findViewById(viewId);
			break;
		}
		return layout;
	}

	private void initPop(RelativeLayout layout){
		btnComfirm = (Button) layout.findViewById(R.id.btn_gender_comfirm);
		btnComfirm.setOnClickListener(this);
		this.setContentView(layout);
		this.setWidth(LayoutParams.MATCH_PARENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setAnimationStyle(R.style.popup_alt_anim);
		this.setFocusable(true);
		ColorDrawable dw = new ColorDrawable(Color.WHITE);
		this.setBackgroundDrawable(dw);
		WindowManager.LayoutParams lp =mActivity.getWindow().getAttributes();  
		lp.alpha = 0.35f;  
		mActivity.getWindow().setAttributes(lp);
		this.update();
	}

	private ArrayList<HashMap<String, Object>> Array2List(String[][] array){

		ArrayList<HashMap<String, Object>> datalist=new ArrayList<HashMap<String,Object>>();

		for(int i=0;i<array.length;i++){
			HashMap<String, Object> map1=new HashMap<String, Object>();
			map1.put(BELONG_CODE, 0);
			map1.put(VALUE_CODE, array[i][0]);
			datalist.add(map1);
			for(int j=1;j<array[i].length;j++){
				HashMap<String, Object> map2=new HashMap<String, Object>();
				map2.put(BELONG_CODE, i+1);
				map2.put(VALUE_CODE, array[i][j]);
				datalist.add(map2);
				
			}
		}
		return datalist;
	}

	private ArrayList<HashMap<String, Object>> List2List(ArrayList<String> listF,ArrayList<String> listS){

		ArrayList<HashMap<String, Object>> datalist = new ArrayList<HashMap<String,Object>>();
		int listFSize=listF.size();
		int listSSize=listS.size();
		for(int i=0;i<listFSize;i++){
			HashMap<String, Object> map=new HashMap<String, Object>();
			map.put(BELONG_CODE, -1);
			map.put(VALUE_CODE, listF.get(i));
			datalist.add(map);
		}
		for(int j=0;j<listSSize;j++){
			HashMap<String, Object> map2=new HashMap<String, Object>();
			map2.put(BELONG_CODE, -2);
			map2.put(VALUE_CODE, listS.get(j));
			datalist.add(map2);
		}
		return datalist;
	}

	private ArrayList<String> Array2List (String[] array){
		int length=array.length;
		ArrayList<String> list=new ArrayList<String>();
		for(int i=0;i<length;i++){
			list.add(array[i]);
		}
		return list;
	}
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		int id = view.getId();
		switch(id){
		case R.id.btn_gender_comfirm:
			if(isOneRoll){
				if(!CollectionUtils.isEmpty(mPickedStr)){
					submitIndex(mPickedStr);
				}else{
					submitIndex(mCurrentStr);
				}

				submitString(mPickedStr);	
			}else if(!isOneRoll){
				if(!CollectionUtils.isEmpty(mDoublePickArray)){
					submitDoubleString(mDoublePickArray);
//					Log.e("rollpicker", Integer.toString(mFirstN)+"--------"+Integer.toString(mSecondN));
				}
				else{
					String[] array=new String[2];
					array[0]=mCurrentDoubleF;
					array[1]=mCurrentDoubleS;
					submitDoubleString(array);
				}	
			}
			this.dismiss();
			break;
		}
	}

	private void submitDoubleString(String[] str) {
		// TODO Auto-generated method stub
		Message msg = Message.obtain();
		msg.what = SUBMIT_DOUBLE_STRING;
		msg.obj = str;
		if (mHandler != null) {
			mHandler.sendMessage(msg);
		}
	}

	private void submitIndex(String str) {
		try {
			int index=String2Index(str);
			//			Log.e("RemindPop", "index"+index);
			Message msg = Message.obtain();
			msg.what = SUBMIT_INDEX;
			msg.obj = index;
			if (mHandler != null) {
				mHandler.sendMessage(msg);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void submitString(String str) {
		Message msg = Message.obtain();
		msg.what = SUBMIT_STRING;
		msg.obj = str;
		if (mHandler != null) {
			mHandler.sendMessage(msg);
		}

	}

	private int String2Index(String str) {
		int index=0;
		int size=mContentList.size();
		for(int j=0;j<size;j++){
			if(str.equals(mContentList.get(j))){
				index=j;
				break;
			}
		}
		return index;
	}
	
	@Override
	public void onPick(String gender) {
		this.mPickedStr=gender;
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
	class KeyArrayException extends Exception{

		private static final long serialVersionUID = 1L;
		public KeyArrayException(){
			super();
		}
		public KeyArrayException(String message){
			super(message);
		}
	}
	
	@Override
	public void onDoublePick(String[] array, int mFirstN, int mSecondN) {
		// TODO Auto-generated method stub
		mDoublePickArray=array;
		this.mFirstN=mFirstN;
		this.mSecondN=mSecondN;
	}
	/**
	 * 用于获取传入ArrayList<HashMap<String, Object>>数据时，获取Map中其他Key值所对应的Value
	 * @param keyIndex key[]中的index
	 * @return String[] arr : arr[0]第一个滚轮值，arr[1]第二个滚轮值
	 */
	public String[] getOtherValue(int keyIndex){
		String[] array=new String[2];
		HashMap<String,Object> map1=mDataList.get(mFirstN);
		HashMap<String,Object> map2=mDataList.get(mSecondN);
		array[0]=map1.get(keyArray[keyIndex]).toString();
		array[1]=map2.get(keyArray[keyIndex]).toString();
		return array;
	}

	
}
