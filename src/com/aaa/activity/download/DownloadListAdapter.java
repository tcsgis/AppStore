package com.aaa.activity.download;

import java.util.ArrayList;

import com.aaa.db.AppDownloadState;
import com.aaa.db.DownloadState;
import com.aaa.util.Constant;
import com.aaa.util.MyTools;
import com.changhong.activity.widget.AppMainDialog;
import com.changhong.util.db.bean.CacheManager;
import com.llw.AppStore.R;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DownloadListAdapter extends BaseAdapter{

	private ArrayList<MyItem> datas;
	private Context context;
	private HeightChangeListener listener;
	private int topHeight;
	private int bottomHeight;
	private int marginHeight;
	private boolean needOperation;
	private boolean needProgress;
	private Handler parentHandler;
	private int listType;
	
	public DownloadListAdapter(Context c, ArrayList<AppDownloadState> list, Handler handler, int listType, HeightChangeListener l){
		context = c;
		listener = l;
		parentHandler = handler;
		this.listType = listType;
		topHeight = context.getResources().getDimensionPixelSize(R.dimen.download_activity_item_height1);
		bottomHeight = context.getResources().getDimensionPixelSize(R.dimen.download_activity_item_height2);
		marginHeight = context.getResources().getDimensionPixelSize(R.dimen.download_activity_item_margin);
		
		datas = new ArrayList<MyItem>();
		for(AppDownloadState ads : list){
			datas.add(new MyItem(ads));
		}
		
		refreshHeight();
		
		if(listType == Constant.TYPE_DOWNLADED_LIST){
			needOperation = true;
			needProgress = false;
		}
		else if(listType == Constant.TYPE_DOWNLADING_LIST){
			needOperation = true;
			needProgress = true;
		}
		else if(listType == Constant.TYPE_HISTORY_LIST){
			needOperation = false;
			needProgress = false;
		}
	}
	
	private void refreshHeight(){
		boolean selected = false;
		for(MyItem item : datas){
			if(item.isSelected){
				selected = true;
				break;
			}
		}
		
		int totalHeight = (topHeight + marginHeight) * datas.size();
		if(selected)
			totalHeight += bottomHeight + 1;
		
		if(listener != null)
			listener.onHeightChanged(totalHeight);
	}
	
	private void setSelected(int position){
		if(datas.get(position).isSelected){
			datas.get(position).isSelected = false;
		}else{
			datas.get(position).isSelected = true;
			for(MyItem item : datas){
				if(item.isSelected && datas.indexOf(item) != position){
					item.isSelected = false;
					break;
				}
			}
		}
		refreshHeight();
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public MyItem getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
	
	public void refresh(){
		if(listType == Constant.TYPE_DOWNLADING_LIST){
			datas.clear();
			
			for(AppDownloadState item : CacheManager.INSTANCE.getDownload()){
				if(item.getDownloadState() == DownloadState.DOWNLOADING || item.getDownloadState() == DownloadState.PAUSE){
					datas.add(new MyItem(item.clone()));
				}
			}
//			for(int i = 0; i < datas.size(); i++){
//				boolean hasItem = false;
//				for(AppDownloadState item : CacheManager.INSTANCE.getDownload()){
//					if(item.getID() == datas.get(i).data.getID()
//							&& (item.getDownloadState() == DownloadState.DOWNLOADING || item.getDownloadState() == DownloadState.PAUSE)){
//						datas.get(i).data = item.clone();
//						hasItem = true;
//						break;
//					}
//				}
//				if(! hasItem){
//					datas.remove(i);
//					i--;
//				}
//			}
		}
		
		if(listType == Constant.TYPE_DOWNLADED_LIST){
			datas.clear();
			
			for(AppDownloadState item : CacheManager.INSTANCE.getDownload()){
				if(item.getDownloadState() == DownloadState.DOWNLOADED || 
						item.getDownloadState() == (DownloadState.DOWNLOADED | DownloadState.INSTALLED)){
					datas.add(new MyItem(item.clone()));
				}
			}
//			for(int i = 0; i < CacheManager.INSTANCE.getDownload().size(); i++){
//				if(CacheManager.INSTANCE.getDownload().get(i).getDownloadState() == DownloadState.DOWNLOADED
//						|| CacheManager.INSTANCE.getDownload().get(i).getDownloadState() == (DownloadState.INSTALLED | DownloadState.DOWNLOADED)){
//					boolean hasItem = false;
//					for(MyItem item : datas){
//						if(item.data.getID() == CacheManager.INSTANCE.getDownload().get(i).getID()){
//							item.data = CacheManager.INSTANCE.getDownload().get(i).clone();
//							hasItem = true;
//							break;
//						}
//					}
//					if(! hasItem){
//						MyItem mi = new MyItem(CacheManager.INSTANCE.getDownload().get(i).clone());
//						datas.add(mi);
//					}
//				}
//			}
		}
		
		if(datas.size() == 0 && parentHandler != null){
			if(listType == Constant.TYPE_DOWNLADED_LIST){
				parentHandler.sendEmptyMessage(Constant.DONWLOADED_CLEAR);
			}
			else if(listType == Constant.TYPE_DOWNLADING_LIST){
				parentHandler.sendEmptyMessage(Constant.DONWLOADING_CLEAR);
			}
			else if(listType == Constant.TYPE_HISTORY_LIST){
				parentHandler.sendEmptyMessage(Constant.HISTORY_CLEAR);
			}
		}
		
		refreshHeight();
		notifyDataSetChanged();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		final MyItem item = datas.get(position);
		if (convertView == null) {
			vh = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_dowload_list, null);
			vh.logo = (ImageView) convertView.findViewById(R.id.logo);
			vh.state_img = (ImageView) convertView.findViewById(R.id.state_img);
			vh.indicator = (ImageView) convertView.findViewById(R.id.indicator);
			vh.delete_img = (ImageView) convertView.findViewById(R.id.delete_img);
			vh.name = (TextView) convertView.findViewById(R.id.name);
			vh.progress = (TextView) convertView.findViewById(R.id.progress);
			vh.speed = (TextView) convertView.findViewById(R.id.speed);
			vh.state_txt = (TextView) convertView.findViewById(R.id.state_txt);
			vh.delete_txt = (TextView) convertView.findViewById(R.id.delete_txt);
			vh.operation = (RelativeLayout) convertView.findViewById(R.id.operation);
			vh.delete_rl = (RelativeLayout) convertView.findViewById(R.id.delete_rl);
			vh.detail_rl = (RelativeLayout) convertView.findViewById(R.id.detail_rl);
			vh.bottom_ll = (LinearLayout) convertView.findViewById(R.id.bottom_ll);
			vh.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		
		//todo logo
		
		vh.name.setText(item.data.getName());
		
		//operation
		if(! needOperation){
			vh.operation.setVisibility(View.INVISIBLE);
		}else{
			if(item.data.getDownloadState() == DownloadState.PAUSE){
				vh.state_img.setImageResource(R.drawable.continue_green);
				vh.state_txt.setText(R.string.continue_txt);
				vh.operation.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						MyTools.continueDownload(item.data);
						refresh();
					}
				});
			}else if(item.data.getDownloadState() == DownloadState.DOWNLOADING){
				vh.state_img.setImageResource(R.drawable.pause);
				vh.state_txt.setText(R.string.pause);
				vh.operation.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						MyTools.pauseDownload(item.data);
						refresh();
					}
				});
			}else if(item.data.getDownloadState() == DownloadState.DOWNLOADED){
				vh.state_img.setImageResource(R.drawable.install);
				vh.state_txt.setText(R.string.install);
				vh.operation.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						MyTools.installApk(context, item.data);
					}
				});
			}else if(item.data.getDownloadState() >= DownloadState.INSTALLED){
				vh.state_img.setImageResource(R.drawable.open);
				vh.state_txt.setText(R.string.open_app);
				vh.operation.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						MyTools.openAnotherApp(context, item.data);
					}
				});
			}
		}
		
		//progress
		if(! needProgress){
			vh.progress.setVisibility(View.GONE);
			vh.speed.setVisibility(View.GONE);
			vh.progressBar.setVisibility(View.GONE);
		}else{
			if(item.data.getDownloadState() == DownloadState.PAUSE){
				vh.progress.setVisibility(View.VISIBLE);
				vh.speed.setVisibility(View.VISIBLE);
				vh.progressBar.setVisibility(View.VISIBLE);
				vh.progress.setText(MyTools.float2String(item.data.getProgress() / 1024 / 1024f) + "M/" + MyTools.float2String(item.data.getSize()) + "M");
				vh.speed.setText(context.getString(R.string.doA_string5));
				vh.progressBar.setProgress((int)(item.data.getPercent()));
			}
			else if(item.data.getDownloadState() == DownloadState.DOWNLOADING){
				vh.progress.setVisibility(View.VISIBLE);
				vh.speed.setVisibility(View.VISIBLE);
				vh.progressBar.setVisibility(View.VISIBLE);
				vh.progress.setText(MyTools.float2String(item.data.getProgress() / 1024 / 1024f) + "M/" + MyTools.float2String(item.data.getSize()) + "M");
				vh.speed.setText(MyTools.float2String(item.data.getSpeed()) + "K/s");
				vh.progressBar.setProgress((int)(item.data.getPercent()));
			}
			else /*if(item.data.getDownloadState() == DownloadState.DOWNLOADED || item.data.getDownloadState() == DownloadState.INSTALLED
					|| item.data.getDownloadState() == (DownloadState.DOWNLOADED | DownloadState.INSTALLED))*/{
				vh.progress.setVisibility(View.GONE);
				vh.speed.setVisibility(View.GONE);
				vh.progressBar.setVisibility(View.GONE);
			}
		}
		
		//item click
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setSelected(position);
			}
		});
		
		//bottom view
		if(item.isSelected){
			vh.bottom_ll.setVisibility(View.VISIBLE);
			vh.indicator.setImageResource(R.drawable.up);
		}else{
			vh.bottom_ll.setVisibility(View.GONE);
			vh.indicator.setImageResource(R.drawable.down);
		}
		
		//left rl
		if(! needProgress || item.data.getDownloadState() == DownloadState.DOWNLOADED 
				|| item.data.getDownloadState() == (DownloadState.DOWNLOADED | DownloadState.INSTALLED)){
			vh.delete_img.setImageResource(R.drawable.delete);
			vh.delete_txt.setText(R.string.delete);
			vh.delete_rl.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					final AppMainDialog dialog = new AppMainDialog(context, R.style.appdialog);
					dialog.withTitle(R.string.dialog_title)
					.withMessage(R.string.doA_string12)
					.setOKClick(R.string.ok_queren, new View.OnClickListener() {
						
						@Override
						public void onClick(View view) {
							MyTools.removeDownloadedFile(item.data);
							refresh();
							dialog.dismiss();
						}
					})
					.setCancelClick(R.string.cancel_quxiao).show();
				}
			});
		}else{
			vh.delete_img.setImageResource(R.drawable.cancel);
			vh.delete_txt.setText(R.string.cancel);
			vh.delete_rl.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					final AppMainDialog dialog = new AppMainDialog(context, R.style.appdialog);
					dialog.withTitle(R.string.dialog_title)
					.withMessage(R.string.doA_string11)
					.setOKClick(R.string.ok_queren, new View.OnClickListener() {
						
						@Override
						public void onClick(View view) {
							MyTools.cancelDownload(item.data);
							refresh();
							dialog.dismiss();
						}
					})
					.setCancelClick(R.string.cancel_quxiao).show();
				}
			});
		}
		//right rl
		vh.detail_rl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(context, "detail", Toast.LENGTH_SHORT).show();
			}
		});
		
		return convertView;
	}

	final class ViewHolder{
		private ImageView logo;
		private ImageView state_img;
		private ImageView indicator;
		private ImageView delete_img;
		private TextView name;
		private TextView progress;
		private TextView speed;
		private TextView state_txt;
		private TextView delete_txt;
		private RelativeLayout operation;
		private RelativeLayout delete_rl;
		private RelativeLayout detail_rl;
		private LinearLayout bottom_ll;
		private ProgressBar progressBar;
	}
	
	private class MyItem{
		private AppDownloadState data;
		private boolean isSelected;
		
		public MyItem(AppDownloadState ads){
			data = ads;
			isSelected = false;
		}
	}
}
