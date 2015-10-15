package com.aaa.activity.download;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aaa.activity.me.MeActivity;
import com.aaa.db.AppDownloadState;
import com.aaa.db.DownloadState;
import com.aaa.util.Constant;
import com.changhong.activity.BaseActivity;
import com.changhong.annotation.CHInjectView;
import com.llw.AppStore.R;

public class DownloadActivity extends BaseActivity {

	@CHInjectView(id = R.id.downloaded_ll)
	private LinearLayout downloaded_ll;
	@CHInjectView(id = R.id.downloading_ll)
	private LinearLayout downloading_ll;
	@CHInjectView(id = R.id.downloading_list)
	private ListView downloading_list;
	@CHInjectView(id = R.id.downloaded_list)
	private ListView downloaded_list;
	@CHInjectView(id = R.id.btn_downloading)
	private TextView btn_downloading;
	@CHInjectView(id = R.id.btn_downloaded)
	private TextView btn_downloaded;
	@CHInjectView(id = R.id.null_txt)
	private TextView null_txt;
	@CHInjectView(id = R.id.txt_title)
	private TextView txt_title;
	
	private ArrayList<AppDownloadState> downloding = new ArrayList<AppDownloadState>();
	private ArrayList<AppDownloadState> downloded = new ArrayList<AppDownloadState>();
	private DownloadListAdapter adapter;
	
	@Override
	protected void onAfterOnCreate(Bundle savedInstanceState) {
		super.onAfterOnCreate(savedInstanceState);
		initTitle();
		doGetData();
		initList();
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
		test();
	}

	private void initList() {
		if(downloding.size() == 0){
			downloading_ll.setVisibility(View.GONE);
		}
		if(downloded.size() == 0){
			downloaded_ll.setVisibility(View.GONE);
		}
		if(downloded.size() == 0 && downloding.size() == 0){
			null_txt.setVisibility(View.VISIBLE);
			return;
		}
		
		//downloding
		btn_downloading.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		
		adapter = new DownloadListAdapter(this, downloding, Constant.TYPE_DOWNLADING_LIST, new HeightChangeListener() {
			
			@Override
			public void onHeightChanged(int height) {
				LayoutParams lp = downloading_list.getLayoutParams();
				lp.height = height;
				downloading_list.setLayoutParams(lp);
			}
		});
		downloading_list.setAdapter(adapter);
		
		//downloaded
		btn_downloaded.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		
		adapter = new DownloadListAdapter(this, downloded, Constant.TYPE_DOWNLADED_LIST, new HeightChangeListener() {
			
			@Override
			public void onHeightChanged(int height) {
				LayoutParams lp = downloaded_list.getLayoutParams();
				lp.height = height;
				downloaded_list.setLayoutParams(lp);
			}
		});
		downloaded_list.setAdapter(adapter);
	}


	private void test() {
		AppDownloadState a1 = new AppDownloadState();
		a1.setName("请问请问颇大是大");
		a1.setProgress(2.5f);
		a1.setSize(15.2f);
		a1.setDownloadState(DownloadState.PAUSE);
		downloding.add(a1);
		
		AppDownloadState a2 = new AppDownloadState();
		a2.setName("在常州常州");
		a2.setProgress(0.5f);
		a2.setSize(45.2f);
		a2.setDownloadState(DownloadState.DOWNLOADING);
		downloding.add(a2);
		
		AppDownloadState a3 = new AppDownloadState();
		a3.setName("看；老凯；看；");
		a3.setProgress(5.5f);
		a3.setSize(9.2f);
		a3.setDownloadState(DownloadState.DOWNLOADED);
		downloding.add(a3);
		
		downloded.add(a1);
		downloded.add(a2);
		downloded.add(a3);
	}
}
