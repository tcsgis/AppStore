package com.aaa.activity.main;

import java.util.ArrayList;

import com.aaa.activity.detail.DetailActivity;
import com.aaa.db.AppDetail;
import com.llw.AppStore.R;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MyRecyclerAdapter extends
		RecyclerView.Adapter<RecyclerView.ViewHolder> {
	private static final int VIEW_TYPE_HEADER = 0;
	private static final int VIEW_TYPE_ITEM = 1;

	private LayoutInflater mInflater;
	private ArrayList<AppDetail> mItems;
	private View mHeaderView;
	private Context mContext;

	public MyRecyclerAdapter(Context context, ArrayList<AppDetail> items,
			View headerView) {
		mInflater = LayoutInflater.from(context);
		mItems = items;
		mHeaderView = headerView;
		mContext = context;
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
	public void onBindViewHolder(RecyclerView.ViewHolder viewHolder,
			int position) {
		if (viewHolder instanceof ItemViewHolder) {
			((ItemViewHolder) viewHolder).name.setText(mItems.get(position - 1).getName());

		}
	}

	private class ItemViewHolder extends RecyclerView.ViewHolder {
		TextView name;
		TextView type;
		TextView size;
		TextView state_txt;
		TextView desc;
		ImageView logo;
		ImageView state_img;

		public ItemViewHolder(View view) {
	            super(view);
	            name = (TextView) view.findViewById(R.id.name);
	            type = (TextView) view.findViewById(R.id.type);
	            size = (TextView) view.findViewById(R.id.size);
	            state_txt = (TextView) view.findViewById(R.id.state_txt);
	            desc = (TextView) view.findViewById(R.id.desc);
	            logo = (ImageView) view.findViewById(R.id.logo);
	            state_img = (ImageView) view.findViewById(R.id.state_img);
	            
	            //start activity
	            view.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						try {
							Intent intent = new Intent(mContext, DetailActivity.class);
							intent.putExtra(DetailActivity.DATA, mItems.get(getAdapterPosition()));
							mContext.startActivity(intent);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
	            
	            //dowaload operation
	            view.findViewById(R.id.operation).setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
					}
				});
	        }
	}

	private class HeaderViewHolder extends RecyclerView.ViewHolder {
		public HeaderViewHolder(View view) {
			super(view);
		}
	}
}
