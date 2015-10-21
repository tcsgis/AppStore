package com.aaa.activity.main;

import java.util.ArrayList;

import com.aaa.activity.detail.DetailActivity;
import com.aaa.db.AppDownloadState;
import com.aaa.db.DownloadState;
import com.aaa.util.MyTools;
import com.changhong.util.db.bean.CacheManager;
import com.llw.AppStore.R;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyRecyclerAdapter extends
		RecyclerView.Adapter<RecyclerView.ViewHolder> {
	private static final int VIEW_TYPE_HEADER = 0;
	private static final int VIEW_TYPE_ITEM = 1;

	private LayoutInflater mInflater;
	private ArrayList<AppDownloadState> mItems;
//	private ArrayList<AppDownloadState> mPreItems;
	private View mHeaderView;
	private Activity mContext;
	private int type;
	private String tag;

	public MyRecyclerAdapter(Activity context, ArrayList<AppDownloadState> items, View headerView, int type) {
		mInflater = LayoutInflater.from(context);
		mItems = items;
//		mPreItems = items;
		mHeaderView = headerView;
		mContext = context;
		this.type = type;
		tag = MyTools.getTag(type);
	}

	@Override
	public int getItemCount() {
		if (mHeaderView == null) {
			return mItems.size();
		} else {
			return mItems.size() + 1;
		}
	}

	@Override
	public int getItemViewType(int position) {
		return (position == 0) ? VIEW_TYPE_HEADER : VIEW_TYPE_ITEM;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
			int viewType) {
		if (viewType == VIEW_TYPE_HEADER) {
			return new HeaderViewHolder(mHeaderView);
		} else {
			return new ItemViewHolder(mInflater.inflate(R.layout.item_app_list, parent, false));
		}
	}
	
	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
		if (viewHolder instanceof ItemViewHolder) {
			ItemViewHolder vh = (ItemViewHolder)viewHolder;
			final AppDownloadState item = mItems.get(position - 1);
			
//			try {
//				AppDownloadState item2 = mPreItems.get(position - 1);
//				if(item.getDownloadState() == DownloadState.DOWNLOADING && item2.getDownloadState() == DownloadState.DOWNLOADING && 
//						item.getPercent() != item2.getPercent()){
//					vh.state_txt.setText(MyTools.float2String(item.getPercent()) + "%");
//					return;
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
			
			vh.name.setText(item.getName());
			vh.desc.setText(item.getDesc());
			vh.size.setText(mContext.getString(R.string.da_string2, MyTools.float2String(item.getSize())));
			
			//logo
			
			//start activity
			vh.whole.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					try {
						Intent intent = new Intent(mContext, DetailActivity.class);
						intent.putExtra(DetailActivity.DATA, mItems.get(position - 1));
						mContext.startActivity(intent);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			
			//operation state
			switch (mItems.get(position - 1).getDownloadState()) {
			case DownloadState.DOWNLOADING:
				vh.state_img.setImageResource(R.drawable.pause);
				vh.state_txt.setText(R.string.ma_string1);
//				vh.state_txt.setText(MyTools.float2String(item.getPercent()) + "%");
				break;
				
			case DownloadState.DOWNLOADED:
				vh.state_img.setImageResource(R.drawable.install);
				vh.state_txt.setText(R.string.install);
				break;
				
			case DownloadState.PAUSE:
				vh.state_img.setImageResource(R.drawable.continue_gray);
				vh.state_txt.setText(R.string.continue_txt);
				break;
				
			case DownloadState.INSTALLED:
			case DownloadState.INSTALLED | DownloadState.DOWNLOADED:
				vh.state_img.setImageResource(R.drawable.open);
				vh.state_txt.setText(R.string.open_app);
				break;
				
			case DownloadState.NONE:
			default:
				vh.state_img.setImageResource(R.drawable.download);
				vh.state_txt.setText(R.string.download);
				break;
			}
			
			//dowaload operation
			vh.operation.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					switch (item.getDownloadState()) {
					case DownloadState.DOWNLOADING:
						MyTools.pauseDownload(mItems.get(position - 1));
						refreshItem(position - 1);
						break;
						
					case DownloadState.DOWNLOADED:
						MyTools.installApk(mContext, mItems.get(position - 1));
						break;
						
					case DownloadState.PAUSE:
						MyTools.continueDownload(mItems.get(position - 1));
						refreshItem(position - 1);
						break;
						
					case DownloadState.NONE:
						if(MyTools.toLogin(mContext, item)){
							
						}else{
							MyTools.startDownload(mItems.get(position - 1));
							refreshItem(position - 1);
						}
						break;
						
					case DownloadState.INSTALLED:
					case DownloadState.INSTALLED | DownloadState.DOWNLOADED:
						MyTools.openAnotherApp(mContext, mItems.get(position - 1));
						break;
					}
				}
			});
		}
	}
	
	public void refresh(){
//		mPreItems = (ArrayList<AppDownloadState>) mItems.clone();
		mItems = CacheManager.INSTANCE.getAppData(tag);
		notifyDataSetChanged();
	}

	public void refreshItem(int position){
//		mPreItems = (ArrayList<AppDownloadState>) mItems.clone();
		mItems = CacheManager.INSTANCE.getAppData(tag);
		notifyItemChanged(position + 1);
	}
	
	private class ItemViewHolder extends RecyclerView.ViewHolder {
		TextView name;
		TextView type;
		TextView size;
		TextView state_txt;
		TextView desc;
		ImageView logo;
		ImageView state_img;
		LinearLayout whole;
		RelativeLayout operation;

		public ItemViewHolder(View view) {
			super(view);
			name = (TextView) view.findViewById(R.id.name);
			type = (TextView) view.findViewById(R.id.type);
			size = (TextView) view.findViewById(R.id.size);
			state_txt = (TextView) view.findViewById(R.id.state_txt);
			desc = (TextView) view.findViewById(R.id.desc);
			logo = (ImageView) view.findViewById(R.id.logo);
			state_img = (ImageView) view.findViewById(R.id.state_img);
			whole = (LinearLayout) view.findViewById(R.id.whole);
			operation = (RelativeLayout) view.findViewById(R.id.operation);
		}
	}

	private class HeaderViewHolder extends RecyclerView.ViewHolder {
		public HeaderViewHolder(View view) {
			super(view);
		}
	}
}
