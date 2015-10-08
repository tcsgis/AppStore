package com.changhong.activity.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.changhong.util.CHLogger;
import com.llw.AppStore.R;


public class PopupAltView implements OnClickListener, OnKeyListener {
	
	public static final int LINE_1 = 100;
	public static final int LINE_2 = 101;
	public static final int LINE_3 = 102;
	public static final int CANCEL = 103;
	private final int WIDTH = 720;
	private static final int HEIGHT_2 = 310;
	private static final int HEIGHT_3 = 310 + 96;
	private static final int HEIGHT_1 = 264;
	private final int BASE = 720;
	
	private Activity mActivity;
	private String mLine1;
	private String mLine2;
	private String mLine3;
	private PopupWindow mPopup;
	private TextView mTxtLine1;
	private TextView mTxtLine2;
	private TextView mTxtLine3;
	private int mColor = Color.BLACK;
	private onPopupAltListner mListener;
	private Button mBtnBottom;
	
	public interface onPopupAltListner{
		public void onPopupAltSelected(int result);
	}
	
	public PopupAltView(Activity activity, String line1, String line2, onPopupAltListner l){
		mActivity = activity;
		mLine1 = line1;
		mLine2 = line2;
		mListener = l;
		initPopup(false, true);
	}
	
	public PopupAltView(Activity activity, String line1, String line2, String line3, onPopupAltListner l){
		mActivity = activity;
		mLine1 = line1;
		mLine2 = line2;
		mLine3 = line3;
		mListener = l;
		initPopup(true, true);
	}
	
	public PopupAltView(Activity activity, String line1, int color, onPopupAltListner l) {
		mActivity = activity;
		mLine1 = line1;
		mListener = l;
		mColor = color;
		initPopup(false, false);
	}
	private void initPopup(boolean triple, boolean delete){
		
		LayoutInflater layoutInflater = LayoutInflater.from(mActivity); 
		View popup = layoutInflater.inflate(R.layout.popup_alternative, null);	
		if(triple){
			mTxtLine3 = (TextView) popup.findViewById(R.id.txt_popup_alt_line3);
			mTxtLine3.setText(mLine3);
			mTxtLine3.setOnClickListener(this);
			popup.findViewById(R.id.divider2).setVisibility(View.VISIBLE);
		}else{
			popup.findViewById(R.id.rela_popup_alt_line3).setVisibility(View.GONE);
			popup.findViewById(R.id.divider2).setVisibility(View.GONE);
		}
		if (delete) {
			mTxtLine2 = (TextView) popup.findViewById(R.id.txt_popup_alt_line2);
			mTxtLine2.setText(mLine2);
			mTxtLine2.setOnClickListener(this);
			popup.findViewById(R.id.divider).setVisibility(View.VISIBLE);
		}
		else {
			popup.findViewById(R.id.rela_popup_alt_line2).setVisibility(View.GONE);
			popup.findViewById(R.id.divider).setVisibility(View.GONE);
		}
		
		mTxtLine1 = (TextView) popup.findViewById(R.id.txt_popup_alt_line1);
		mTxtLine1.setText(mLine1);
//		mTxtLine1.setTextColor(mColor);
		mTxtLine1.setOnClickListener(this);
		mBtnBottom = (Button) popup.findViewById(R.id.btn_popup_alt_cancel);	
		mBtnBottom.setOnClickListener(this);				
		
		DisplayMetrics dm = new DisplayMetrics();
		mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		//先乘后除, 避免不能整除造成的分辨率问题
		int width = dm.widthPixels * WIDTH / BASE;
		int height = dm.widthPixels * (triple ? HEIGHT_3 : (delete ? HEIGHT_2 : HEIGHT_1)) / BASE;
		mPopup = new PopupWindow(popup, width, height);
		mPopup.setAnimationStyle(R.style.popup_alt_anim);
		//设置Focusable和空背景后才能响应自动返回事件(缺陷: 不监听onDismissListener无法调用dismissEffect)
		//mPopup.setFocusable(true);
		//mPopup.setBackgroundDrawable(new ColorDrawable());//new BitmapDrawable()
		//手动处理返回
		mPopup.setFocusable(true);
		popup.setFocusable(true);
		popup.setFocusableInTouchMode(true);
		popup.setOnKeyListener(this);
	}
	
	/**
	 * 多了一个恢复默认图片的选项
	 */
	private void initPopup2() {
		// TODO Auto-generated method stub
		
	}
	
	private void hideKeyboard(View v) {
		InputMethodManager imm = (InputMethodManager)mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);//hide keyboard
	}
	
	public void show(){
		hideKeyboard(mBtnBottom);
		mPopup.showAtLocation(mBtnBottom, Gravity.CENTER|Gravity.BOTTOM, 0, 0);
		showEffect();
	}
	
	public void dismiss(){
		mPopup.dismiss();
		dismissEffect();
	}
	
	@Override
	public void onClick(View v) {
		//Avoid memory leak
		this.dismiss();
		
		switch (v.getId()) {
		case R.id.txt_popup_alt_line1:
			mListener.onPopupAltSelected(LINE_1);
			break;
			
		case R.id.txt_popup_alt_line2:
			mListener.onPopupAltSelected(LINE_2);
			break;
			
		case R.id.txt_popup_alt_line3:
			mListener.onPopupAltSelected(LINE_3);
			break;
			
		case R.id.btn_popup_alt_cancel:
			mListener.onPopupAltSelected(CANCEL);
			break;

		default:
			break;
		}
	}
	
	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (mPopup.isShowing()) {
			if ((keyCode == android.view.KeyEvent.KEYCODE_BACK) &&
					(event.getAction() == android.view.KeyEvent.ACTION_DOWN)) {
				PopupAltView.this.dismiss();
				return true;
			}
		}
		return false;
	}

	private void showEffect(){
		WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();  
		lp.alpha = 0.7f;  
		mActivity.getWindow().setAttributes(lp);
	}
	
	private void dismissEffect(){
		WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();  
        lp.alpha = 1f;  
        mActivity.getWindow().setAttributes(lp);
	}
}
