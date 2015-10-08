package com.changhong.activity.crop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.changhong.CHActivity;
import com.changhong.annotation.CHInjectView;
import com.changhong.util.CHLogger;
import com.llw.AppStore.R;

public class CropImageActivity extends CHActivity implements OnClickListener{

	public static final String WIDTH = "width";
	public static final String HEIGHT = "height";
	public static final String SAVE_PATH = "save_uri";
	public static final String IS_ALPHA = "is_alpha";
	
	private static final float WIDTH_LIMIT = 0.67f;
	
	@CHInjectView(id = R.id.rela_crop)
	private LinearLayout mRelaLayout;
	
	@CHInjectView(id = R.id.btn_comfirm)
	private Button mBtnComfirm;
	
	@CHInjectView(id = R.id.btn_cancel)
	private Button mBtnCancel;
	
	private ClipImageLayout mClipImageLayout;
	private String mSavePath;
	private int mCropWidth = 0;
	private int mCropHeight = 0;
	private int mSaveWidth = 0;
	private int mSaveHeight = 0;
	private boolean mIsAlpha = false;//默认是jpeg
	
	@Override
	protected void onPreOnCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onPreOnCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);  
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	}
	
	@Override
	protected void onAfterOnCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onAfterOnCreate(savedInstanceState);
		Bundle b = getIntent().getExtras();
		mSaveWidth = b.getInt(WIDTH);
		mSaveHeight = b.getInt(HEIGHT);
		mSavePath = b.getString(SAVE_PATH);
		mIsAlpha = b.getBoolean(IS_ALPHA, false);
		Uri uri = getIntent().getData();
		
		if(mSaveWidth <= 0 || mSaveHeight <= 0 || uri == null || mSavePath == null){
			finish();
			return;
		}
		
		initLength();
		mClipImageLayout = new ClipImageLayout(this, mCropWidth, mCropHeight, uri);
		android.view.ViewGroup.LayoutParams lp = new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		mRelaLayout.addView(mClipImageLayout, lp);
		
		mBtnComfirm.setOnClickListener(this);
		mBtnCancel.setOnClickListener(this);
	}

	private void initLength() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		
		float max = Math.max(mSaveWidth, mSaveHeight);
		if(max < dm.widthPixels * WIDTH_LIMIT){
			mCropWidth = (int) (dm.widthPixels * WIDTH_LIMIT /max * mSaveWidth);
			mCropHeight = (int) (dm.widthPixels * WIDTH_LIMIT / max * mSaveHeight);
		}
		else if(max > dm.widthPixels){
			mCropWidth = (int) (dm.widthPixels / max * mSaveWidth);
			mCropHeight = (int) (dm.widthPixels / max * mSaveHeight);
		}
		else {
			mCropWidth = mSaveWidth;
			mCropHeight = mSaveHeight;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_comfirm:
			try {
				Bitmap bitmap = mClipImageLayout.clip();
				bitmap = Bitmap.createScaledBitmap(bitmap, mSaveWidth, mSaveHeight, false);
				
				File file = new File(mSavePath);
				FileOutputStream os = new FileOutputStream(file);
				
				if(mIsAlpha){
					bitmap.compress(Bitmap.CompressFormat.PNG, 90, os);
				}else{
					bitmap.compress(Bitmap.CompressFormat.JPEG, 90, os);
				}
				
				os.flush();
				os.close();
				bitmap.recycle();
				
				mRelaLayout = null;
				mClipImageLayout = null;
				
				setResult(RESULT_OK);
			} 
			catch (FileNotFoundException e) {
				e.printStackTrace();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
			catch (Exception e){
				e.printStackTrace();
			}
			
			finish();
			break;
		case R.id.btn_cancel:{
			finish();
			break;
		}
		default:
			break;
		}
	}
}
