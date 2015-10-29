package com.aaa.activity.detail;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaa.activity.main.FlexibleSpaceWithImageRecyclerViewFragment;
import com.aaa.activity.main.MainActivity;
import com.aaa.db.AppDownloadState;
import com.aaa.db.DownloadState;
import com.aaa.util.AppType;
import com.aaa.util.Constant;
import com.aaa.util.DMUtil;
import com.aaa.util.MyTools;
import com.changhong.activity.BaseActivity;
import com.changhong.activity.widget.HorizontalListView;
import com.changhong.annotation.CHInjectView;
import com.changhong.util.bitmap.CHBitmapCacheWork;
import com.changhong.util.db.bean.CacheManager;
import com.changhong.util.download.DownLoadCallback;
import com.changhong.util.download.DownloadManager;
import com.llw.AppStore.R;

public class DetailActivity extends BaseActivity {

	@CHInjectView(id = R.id.logo)
	private ImageView logo;
	@CHInjectView(id = R.id.state_img)
	private ImageView state_img;
	@CHInjectView(id = R.id.name)
	private TextView name;
	@CHInjectView(id = R.id.txt_title)
	private TextView txt_title;
	@CHInjectView(id = R.id.tag)
	private TextView tag;
	@CHInjectView(id = R.id.version)
	private TextView version;
	@CHInjectView(id = R.id.size)
	private TextView size;
	@CHInjectView(id = R.id.developer)
	private TextView developer;
	@CHInjectView(id = R.id.state_txt)
	private TextView state_txt;
	@CHInjectView(id = R.id.desc)
	private TextView desc;
	@CHInjectView(id = R.id.state_txt_bottom)
	private TextView state_txt_bottom;
	@CHInjectView(id = R.id.operation)
	private RelativeLayout operation;
	@CHInjectView(id = R.id.operation_bottom)
	private FrameLayout operation_bottom;
	@CHInjectView(id = R.id.progressBar)
	private ProgressBar progressBar;
	@CHInjectView(id = R.id.list)
	private HorizontalListView list;
	
	public static final String DATA = "DetailActivity.DATA";
	
	private AppDownloadState data;
	private PhotoAdapter adapter;
	private CHBitmapCacheWork imageFetcherLogo;
	private CHBitmapCacheWork imageFetcherDesc;
	
	@Override
	protected void onAfterOnCreate(Bundle savedInstanceState) {
		super.onAfterOnCreate(savedInstanceState);
		data = (AppDownloadState) getIntent().getSerializableExtra(DATA);
		if(data == null){
			finish();
		}else{
			imageFetcherLogo = MyTools.getImageFetcher(this, getCHApplication(), false, 0, DMUtil.getFacePhotoWidth(this), DMUtil.getFacePhotoHeight(this));
			imageFetcherDesc = MyTools.getImageFetcher(this, getCHApplication(), false, 0, DMUtil.getElsePhotoWidth(this), DMUtil.getElsePhotoHeight(this));
			initView();
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		DownloadManager.getDownloadManager().setDownLoadCallback(new DownLoadCallback()
 		{
			@Override
 			public void onSuccess(String url) {
 				super.onSuccess(url);
 				refresh();
 			}

			@Override
 			public void onFailure(String url, String strMsg) {
 				super.onFailure(url, strMsg);
 				MyTools.toastLoadFailure(DetailActivity.this);
 				refresh();
 			}
			
 			@Override
 			public void onLoading(String url, long totalSize, long currentSize, long speed)
 			{
 				super.onLoading(url, totalSize, currentSize, speed);
 				refresh();
 			}
 		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		refresh();
	}
	
	private void initView() {
		String sizeString = MyTools.float2String(data.getSize());
		txt_title.setText(data.getName());
		name.setText(data.getName());
		version.setText(data.getVersion());
		tag.setText(data.getRealTag());//todo
		size.setText(getString(R.string.da_string2, sizeString));
		developer.setText(data.getDeveloper());
//		state_txt.setText("下载");
		desc.setText(data.getDesc());
//		state_txt_bottom.setText(getString(R.string.da_string3, sizeString));
//		Drawable drawable = getResources().getDrawable(R.drawable.download_bottom);
//		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
//		state_txt_bottom.setCompoundDrawables(drawable, null, null, null);
		
		try {
			imageFetcherLogo.loadFormCache(data.getLogoUrl(), logo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		operation.setOnClickListener(operationClickListener);
		operation_bottom.setOnClickListener(operationClickListener);
		findViewById(R.id.rl_left).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		if(data.getDescUrl() != null && data.getDescUrl().size() > 0){
			adapter = new PhotoAdapter(data.getDescUrl(), this);
			list.setAdapter(adapter);
		}
	}
	
	private OnClickListener operationClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(data.getType() == AppType.APP || data.getType() == AppType.GAME){
				switch(data.getDownloadState()){
				case DownloadState.DOWNLOADING:
					MyTools.pauseDownload(data);
					refresh();
					break;
					
				case DownloadState.DOWNLOADED:
					MyTools.installApk(DetailActivity.this, data);
					break;
					
				case DownloadState.PAUSE:
					MyTools.continueDownload(data);
					refresh();
					break;
					
				case DownloadState.INSTALLED:
				case DownloadState.INSTALLED | DownloadState.DOWNLOADED:
					MyTools.openAnotherApp(DetailActivity.this, data);
					break;
					
				case DownloadState.NONE:
					if(MyTools.toLogin(DetailActivity.this, data)){
						
					}else{
						MyTools.startDownload(data);
						refresh();
					}
				}
			}
			else if(data.getType() == AppType.WEB){
				if(MyTools.toLogin(DetailActivity.this, data)){
					
				}else{
					MyTools.openWeb(DetailActivity.this, data.getDownloadUrl());
				}
			}
		}
	};
	
	private void refresh(){
		if(! refreshData())
			return;
		
		if(data.getType() == AppType.APP || data.getType() == AppType.GAME){
			switch(data.getDownloadState()){
			case DownloadState.DOWNLOADING:
				state_img.setImageResource(R.drawable.pause);
				state_txt.setText(MyTools.float2String(data.getPercent()) + "%");
				progressBar.setProgress((int)(data.getPercent()));
				state_txt_bottom.setText(R.string.da_string6);
				break;
				
			case DownloadState.DOWNLOADED:
				state_img.setImageResource(R.drawable.install);
				state_txt.setText(R.string.da_string7);
				progressBar.setProgress(100);
				state_txt_bottom.setText(R.string.da_string7);
				break;
				
			case DownloadState.PAUSE:
				state_img.setImageResource(R.drawable.continue_gray);
				state_txt.setText(R.string.da_string5);
				progressBar.setProgress((int)(data.getPercent()));
				state_txt_bottom.setText(R.string.da_string5);
				break;
				
			case DownloadState.INSTALLED:
			case DownloadState.INSTALLED | DownloadState.DOWNLOADED:
				state_img.setImageResource(R.drawable.open);
				state_txt.setText(R.string.open_app);
				progressBar.setProgress(100);
				state_txt_bottom.setText(R.string.open_app);
				break;
				
			case DownloadState.NONE:
				state_img.setImageResource(R.drawable.download);
				state_txt.setText(R.string.download);
				progressBar.setProgress(0);
				String sizeString = MyTools.float2String(data.getSize());
				state_txt_bottom.setText(getString(R.string.da_string3, sizeString));
			default:
				
				break;
			}
		}
		else if(data.getType() == AppType.WEB){
			state_img.setImageResource(R.drawable.install);
			state_txt.setText(R.string.ma_string2);
			progressBar.setProgress(100);
			state_txt_bottom.setText(R.string.ma_string2);
		}
	}
	
	private boolean refreshData(){
		AppDownloadState cache;
		boolean ret = false;
		try {
			for(int i = 0; i < CacheManager.INSTANCE.getAppData(data.getTag()).size(); i++){
				cache = CacheManager.INSTANCE.getAppData(data.getTag()).get(i);
				if(cache.getID() == data.getID()){
					data = cache.clone();
					ret = true;
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == Constant.REQUEST_LOGIN && resultCode == Constant.RESULT_LOGIN_SUCCEED){
			AppDownloadState ads = (AppDownloadState) data.getExtras().getSerializable(Constant.APP_DOWNLOAD_STATE);
			if(ads != null){
				if(ads.getType() == AppType.APP || ads.getType() == AppType.GAME){
					MyTools.startDownload(ads);
					refresh();
				}
				else if(ads.getType() == AppType.WEB){
					MyTools.openWeb(DetailActivity.this, ads.getDownloadUrl());
				}
			}
		}
	}
	
	 private class PhotoAdapter extends BaseAdapter{

			private ArrayList<String> list;
			private Context context;
			
			public PhotoAdapter(ArrayList<String> list, Context context) {
				this.list = list;
				this.context = context;
			}
			
			@Override
			public int getCount() {
				return list.size();
			}

			@Override
			public Object getItem(int position) {
				return list.get(position);
			}

			@Override
			public long getItemId(int position) {
				return 0;
			}

			@Override
			public View getView(final int position, View convertView, ViewGroup parent) {
				ViewHolder viewHolder = null;
				final String url = list.get(position);
				if (convertView == null) {
					viewHolder = new ViewHolder();
					convertView = LayoutInflater.from(context).inflate(R.layout.item_photo_show_list, null);
					viewHolder.photo = (ImageView) convertView.findViewById(R.id.photo);
					convertView.setTag(viewHolder);
				} else {
					viewHolder = (ViewHolder) convertView.getTag();
				}
					
				try {
					imageFetcherDesc.loadFormCache(url, viewHolder.photo);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return convertView;
			}
			
			final class ViewHolder{
				public ImageView photo;
			}
		}
}
