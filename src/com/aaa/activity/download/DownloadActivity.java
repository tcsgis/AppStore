package com.aaa.activity.download;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.aaa.db.AppDownloadState;
import com.aaa.db.DownloadState;
import com.aaa.util.Constant;
import com.aaa.util.MyTools;
import com.changhong.activity.BaseActivity;
import com.changhong.annotation.CHInjectView;
import com.changhong.util.CHLogger;
import com.changhong.util.db.bean.CacheManager;
import com.changhong.util.download.DownLoadCallback;
import com.changhong.util.download.DownloadManager;
import com.llw.AppStore.R;

public class DownloadActivity extends BaseActivity {

	@CHInjectView(id = R.id.downloading_ll)
	private LinearLayout downloading_ll;
	@CHInjectView(id = R.id.downloading_list)
	private ListView downloading_list;
	@CHInjectView(id = R.id.downloaded_ll)
	private LinearLayout downloaded_ll;
//	@CHInjectView(id = R.id.downloaded_list)
//	private ListView downloaded_list;
	@CHInjectView(id = R.id.btn_downloading)
	private TextView btn_downloading;
	@CHInjectView(id = R.id.btn_downloaded)
	private TextView btn_downloaded;
	@CHInjectView(id = R.id.null_txt)
	private TextView null_txt;
	@CHInjectView(id = R.id.txt_title)
	private TextView txt_title;
	
	private ArrayList<AppDownloadState> downloding = new ArrayList<AppDownloadState>();
//	private ArrayList<AppDownloadState> downloded = new ArrayList<AppDownloadState>();
//	private DownloadListAdapter downloadedAdapter;
	private DownloadListAdapter downloadingAdapter;
	private MyHandler handler;
	
	private class MyHandler extends Handler{
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == Constant.DONWLOADING_CLEAR){
				downloading_ll.setVisibility(View.GONE);
//				if(downloaded_ll.getVisibility() != View.VISIBLE){
					null_txt.setVisibility(View.VISIBLE);
//				}
			}
			else if(msg.what == Constant.DONWLOADED_CLEAR){
//				downloaded_ll.setVisibility(View.GONE);
				if(downloading_ll.getVisibility() != View.VISIBLE){
					null_txt.setVisibility(View.VISIBLE);
				}
			}
		}
	}
	
	@Override
	protected void onAfterOnCreate(Bundle savedInstanceState) {
		super.onAfterOnCreate(savedInstanceState);
		initTitle();
		doGetData();
		initList();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		DownloadManager.getDownloadManager().setDownLoadCallback(new DownLoadCallback()
		{
			@Override
 			public void onSuccess(String url) {
 				super.onSuccess(url);
// 				downloaded_ll.setVisibility(View.VISIBLE);
 				downloadingAdapter.refresh();
// 				downloadedAdapter.refresh();
 			}
			
 			@Override
 			public void onFailure(String url, String strMsg) {
 				super.onFailure(url, strMsg);
 				MyTools.toastLoadFailure(DownloadActivity.this);
 				downloadingAdapter.refresh();
 			}
			
			@Override
			public void onLoading(String url, long totalSize, long currentSize, long speed)
			{
				super.onLoading(url, totalSize, currentSize, speed);
				downloadingAdapter.refresh();
			}
		});
		//for install apk
//		downloadedAdapter.refresh();
	}
	
	private void initTitle() {
		txt_title.setText(R.string.doA_string1);
		findViewById(R.id.rl_right).setVisibility(View.GONE);
		findViewById(R.id.rl_left).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void doGetData() {
//		downloded.clear();
		downloding.clear();
		
		for(AppDownloadState item : CacheManager.INSTANCE.getDownload()){
//			if(item.getDownloadState() == DownloadState.DOWNLOADED || 
//					item.getDownloadState() == (DownloadState.DOWNLOADED | DownloadState.INSTALLED)){
//				downloded.add(item.clone());
//			}
//			else 
			if(item.getDownloadState() == DownloadState.DOWNLOADING || item.getDownloadState() == DownloadState.PAUSE){
				downloding.add(item.clone());
			}
		}
	}

	private void initList() {
		downloaded_ll.setVisibility(View.GONE);
		
		if(downloding.size() == 0){
			downloading_ll.setVisibility(View.GONE);
		}
//		if(downloded.size() == 0){
//			downloaded_ll.setVisibility(View.GONE);
//		}
		if(/*downloded.size() == 0 && */downloding.size() == 0){
			null_txt.setVisibility(View.VISIBLE);
		}
		
		handler = new MyHandler();
		
		//downloding
		btn_downloading.setVisibility(View.GONE);
		btn_downloading.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		
		downloadingAdapter = new DownloadListAdapter(this, downloding, handler, Constant.TYPE_DOWNLADING_LIST, new HeightChangeListener() {
			
			@Override
			public void onHeightChanged(int height) {
				LayoutParams lp = downloading_list.getLayoutParams();
				lp.height = height;
				downloading_list.setLayoutParams(lp);
			}
		});
		downloading_list.setAdapter(downloadingAdapter);
		
		//downloaded
		btn_downloaded.setVisibility(View.GONE);
		btn_downloaded.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		
//		downloadedAdapter = new DownloadListAdapter(this, downloded, handler, Constant.TYPE_DOWNLADED_LIST, new HeightChangeListener() {
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
}
