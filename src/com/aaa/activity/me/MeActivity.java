package com.aaa.activity.me;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.aaa.activity.download.DownloadListAdapter;
import com.aaa.activity.download.HeightChangeListener;
import com.aaa.activity.login.LoginActivity;
import com.aaa.db.AppDownloadState;
import com.aaa.db.DownloadState;
import com.aaa.util.Constant;
import com.aaa.util.MyTools;
import com.aaa.util.PhotoType;
import com.changhong.activity.BaseActivity;
import com.changhong.activity.util.PictureUtil;
import com.changhong.activity.widget.PhotoSelectPopupView;
import com.changhong.annotation.CHInjectView;
import com.changhong.util.bitmap.CHBitmapCacheWork;
import com.changhong.util.db.bean.CacheManager;
import com.changhong.util.download.DownLoadCallback;
import com.changhong.util.download.DownloadManager;
import com.llw.AppStore.R;

public class MeActivity extends BaseActivity{

	@CHInjectView(id = R.id.downloaded_list)
	private ListView downloaded_list;
	@CHInjectView(id = R.id.btn_downloaded)
	private TextView btn_downloaded;
	@CHInjectView(id = R.id.null_txt)
	private TextView null_txt;
	@CHInjectView(id = R.id.login)
	private TextView login;
	@CHInjectView(id = R.id.portrait)
	private ImageView photo;
	
	private ArrayList<AppDownloadState> downloded = new ArrayList<AppDownloadState>();
	private DownloadListAdapter downloadedAdapter;
	private PhotoSelectPopupView mPopupAltView;
	private Uri mPhotoUri;
	private String newPhotoPath;
	private CHBitmapCacheWork imgFetcher;
	private MyHandler handler;
	
	private class MyHandler extends Handler{
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == Constant.DONWLOADED_CLEAR){
				downloaded_list.setVisibility(View.GONE);
				null_txt.setVisibility(View.VISIBLE);
			}
		}
	}
	
	@Override
	protected void onAfterOnCreate(Bundle savedInstanceState) {
		super.onAfterOnCreate(savedInstanceState);
		initView();
		doGetData();
		initList();
	}

	private void initView() {
		findViewById(R.id.rl_left).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		if(CacheManager.INSTANCE.getCurrentUser() != null){
			login.setText(CacheManager.INSTANCE.getCurrentUser().getName());
		}else{
			login.setText(R.string.me_string2);
			login.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent i = new Intent(MeActivity.this, LoginActivity.class);
					startActivity(i);
				}
			});
		}
		
		photo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mPopupAltView == null){
					mPopupAltView = new PhotoSelectPopupView(MeActivity.this);
				}
				mPopupAltView.show();
			}
		});
		
		if(CacheManager.INSTANCE.getCurrentUser() != null && CacheManager.INSTANCE.getCurrentUser().getPhoto() != null){
			int photoSize = getResources().getDimensionPixelSize(R.dimen.me_activity_photo_zise);
			imgFetcher = MyTools.getImageFetcher(this, getCHApplication(), false, 0, photoSize, photoSize);
			try {
				imgFetcher.loadFormCache(CacheManager.INSTANCE.getCurrentUser().getPhoto(), photo);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		if(CacheManager.INSTANCE.getCurrentUser() == null || CacheManager.INSTANCE.getCurrentUser().getID() == 0){
		}else{
			login.setText(R.string.me_string2);
			Drawable d = getResources().getDrawable(R.drawable.right_white);
			login.setCompoundDrawables(null, null, null, d);
			login.setText(CacheManager.INSTANCE.getCurrentUser().getName());
		}
		
		DownloadManager.getDownloadManager().setDownLoadCallback(new DownLoadCallback()
		{
			@Override
 			public void onSuccess(String url) {
 				super.onSuccess(url);
 				downloaded_list.setVisibility(View.VISIBLE);
 				downloadedAdapter.refresh();
 			}
		});
		//for install apk
		downloadedAdapter.refresh();
	}

	private void doGetData() {
		downloded.clear();
		
		for(AppDownloadState item : CacheManager.INSTANCE.getDownload()){
			if(item.getDownloadState() == DownloadState.DOWNLOADED || 
					item.getDownloadState() == (DownloadState.DOWNLOADED | DownloadState.INSTALLED)){
				downloded.add(item.clone());
			}
		}
	}

	private void initList() {
		if(downloded.size() == 0){
			downloaded_list.setVisibility(View.GONE);
			null_txt.setVisibility(View.VISIBLE);
		}
		
		//downloaded
		btn_downloaded.setVisibility(View.GONE);
		btn_downloaded.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		
		handler = new MyHandler();
		downloadedAdapter = new DownloadListAdapter(this, downloded, handler, Constant.TYPE_DOWNLADED_LIST, new HeightChangeListener() {
			
			@Override
			public void onHeightChanged(int height) {
				LayoutParams lp = downloaded_list.getLayoutParams();
				lp.height = height;
				downloaded_list.setLayoutParams(lp);
			}
		});
		downloaded_list.setAdapter(downloadedAdapter);
		
		
//		if(downloded.size() == 0){
//			downloaded_list.setVisibility(View.GONE);
//			null_txt.setVisibility(View.VISIBLE);
//			return;
//		}
//		
//		btn_downloaded.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Toast.makeText(MeActivity.this, "click", Toast.LENGTH_SHORT).show();
//			}
//		});
//		
//		downloadedAdapter = new DownloadListAdapter(this, downloded, new MyHandler(), Constant.TYPE_HISTORY_LIST, new HeightChangeListener() {
//			
//			@Override
//			public void onHeightChanged(int height) {
//				LayoutParams lp = downloaded_list.getLayoutParams();
//				lp.height = height;
//				downloaded_list.setLayoutParams(lp);
//			}
//		});
//		downloaded_list.setAdapter(downloadedAdapter);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			if (resultCode == Activity.RESULT_OK) {
				if (requestCode == PhotoSelectPopupView.TAKE_PHOTO_FROM_LOCAL){
					if (data != null) {
						String filepath = data.getStringExtra("filepath");
						Uri uri = Uri.fromFile(new File(filepath));
						mPopupAltView.cutPhoto(uri, PhotoType.PHOTO_BID);
					}
				}
				else if(requestCode == PhotoSelectPopupView.TAKE_PHOTO_FROM_CAMERA) {
					Uri uri = mPopupAltView.getPhotoUri();
					mPopupAltView.cutPhoto(uri, PhotoType.PHOTO_BID);
				}
				else if(requestCode == PhotoSelectPopupView.CUT_PHOTO){
					mPhotoUri = mPopupAltView.getPhotoUri();
					if(mPhotoUri != null && mPhotoUri.getPath() != null){
						File file = new File(mPhotoUri.getPath());
						if(file.exists() && file.isFile()){
							newPhotoPath = file.getPath();
							photo.setImageBitmap(PictureUtil.
									decodeSampledBitmapFromFile(file.getPath(), 
											photo.getWidth(), 
											photo.getHeight()));
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
