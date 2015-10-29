package com.aaa.activity.main;

import java.util.ArrayList;

import cn.changhong.chcare.core.webapi.bean.ResponseBean;
import cn.changhong.chcare.core.webapi.handler.AsyncResponseCompletedHandler;
import cn.changhong.chcare.core.webapi.server.CHCareWebApiProvider;
import cn.changhong.chcare.core.webapi.server.ChCareWepApiServiceType;
import cn.changhong.chcare.core.webapi.server.IASAccountService;
import cn.changhong.chcare.core.webapi.server.CHCareWebApiProvider.WebApiServerType;

import com.aaa.activity.admin.EditDetailActivity;
import com.aaa.activity.detail.DetailActivity;
import com.aaa.db.AppDownloadState;
import com.aaa.db.DownloadState;
import com.aaa.util.AppType;
import com.aaa.util.Constant;
import com.aaa.util.DMUtil;
import com.aaa.util.MyTools;
import com.aaa.util.Role;
import com.changhong.CHApplication;
import com.changhong.util.CHLogger;
import com.changhong.util.bitmap.CHBitmapCacheWork;
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
	private CHBitmapCacheWork imgFetcher;
	
	private IASAccountService accountService = (IASAccountService) CHCareWebApiProvider.Self
			.defaultInstance().getDefaultWebApiService(
					WebApiServerType.AS_ACCOUNT_SERVER);

	public MyRecyclerAdapter(Activity context, ArrayList<AppDownloadState> items, View headerView, int type) {
		mInflater = LayoutInflater.from(context);
		mItems = items;
//		mPreItems = items;
		mHeaderView = headerView;
		mContext = context;
		this.type = type;
		tag = MyTools.getTag(type);
		imgFetcher = MyTools.getImageFetcher(context, (CHApplication)context.getApplication(), false, 0, 
				DMUtil.getFacePhotoWidth(context), DMUtil.getFacePhotoHeight(context));
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
			try {
				imgFetcher.loadFormCache(item.getLogoUrl(), vh.logo);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			vh.type.setText(item.getRealTag());
			
			//start activity
			vh.whole.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					try {
						Intent intent = null;
						if(CacheManager.INSTANCE.getCurrentUser() != null && CacheManager.INSTANCE.getCurrentUser().getRole() == Role.ADMIN){
							intent = new Intent(mContext, EditDetailActivity.class);
							intent.putExtra(Constant.APP_DETAIL, MyTools.toAppDetail(item));
						}else{
							intent = new Intent(mContext, DetailActivity.class);
							intent.putExtra(DetailActivity.DATA, item);
							
							if(CacheManager.INSTANCE.getCurrentUser() != null && CacheManager.INSTANCE.getCurrentUser().getRole() == Role.CUSTOM){
								accountService.uploadUserOperation(new int[]{item.getID()}, new AsyncResponseCompletedHandler<String>() {
									
									@Override
									public String doCompleted(ResponseBean<?> response, ChCareWepApiServiceType servieType) {
										return null;
									}
								});
							}
						}
						mContext.startActivity(intent);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			
			//operation state
			int appType = item.getType();
			if(appType == AppType.GAME || appType == AppType.APP){
				switch (item.getDownloadState()) {
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
			}
			else if(appType == AppType.WEB){
				vh.state_img.setImageResource(R.drawable.install);
				vh.state_txt.setText(R.string.ma_string2);
			}
			
			//dowaload operation
			if(CacheManager.INSTANCE.getCurrentUser() != null && CacheManager.INSTANCE.getCurrentUser().getRole() == Role.ADMIN){
				vh.operation.setClickable(false);
			}else{
				vh.operation.setClickable(true);
				if(item.getType() == AppType.GAME || item.getType() == AppType.APP){
					vh.operation.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							switch (item.getDownloadState()) {
							case DownloadState.DOWNLOADING:
								MyTools.pauseDownload(item);
								refreshItem(position - 1);
								break;
								
							case DownloadState.DOWNLOADED:
								MyTools.installApk(mContext, item);
								break;
								
							case DownloadState.PAUSE:
								MyTools.continueDownload(item);
								refreshItem(position - 1);
								break;
								
							case DownloadState.NONE:
								if(MyTools.toLogin(mContext, item)){
									
								}else{
									MyTools.startDownload(item);
									refreshItem(position - 1);
								}
								break;
								
							case DownloadState.INSTALLED:
							case DownloadState.INSTALLED | DownloadState.DOWNLOADED:
								MyTools.openAnotherApp(mContext, item);
								break;
							}
						}
					});
				}
				else if(item.getType() == AppType.WEB){
					vh.operation.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if(MyTools.toLogin(mContext, item)){
								
							}else{
								MyTools.openWeb(mContext, item.getDownloadUrl());
							}
						}
					});
				}
			}
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
