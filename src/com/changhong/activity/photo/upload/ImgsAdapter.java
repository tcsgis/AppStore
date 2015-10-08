package com.changhong.activity.photo.upload;

import java.util.ArrayList;
import java.util.List;

import com.llw.AppStore.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImgsAdapter extends BaseAdapter {

	private Context context;
	private List<String> data;
	public Bitmap bitmaps[];
	private Util util;
	private OnItemClickClass onItemClickClass;
	private int index = -1;

	List<View> holderlist;

	public ImgsAdapter(Context context, List<String> data, OnItemClickClass onItemClickClass) {
		this.context = context;
		this.data = data;
		this.onItemClickClass = onItemClickClass;
		bitmaps = new Bitmap[data.size()];
		util = new Util(context);
		holderlist = new ArrayList<View>();
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		DisplayMetrics metric = new DisplayMetrics();
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(metric);
		int width = (metric.widthPixels - 4) / 3;

		Holder holder;
		if (position != index && position > index) {
			index = position;
			view = LayoutInflater.from(context).inflate(R.layout.imgsitem, null);
			view.setLayoutParams(new GridView.LayoutParams(width, width));
			holder = new Holder();
			holder.imageView = (ImageView) view.findViewById(R.id.imageView1);
			holder.selectView = (ImageView)view.findViewById(R.id.select_icon);
			holder.selectView.setVisibility(View.GONE);

			view.setTag(holder);
			holderlist.add(view);
		} else {
			holder = (Holder) holderlist.get(position).getTag();
			view = holderlist.get(position);
		}

		if (bitmaps[position] == null) {
			util.imgExcute(holder.imageView, new ImgClallBackLisner(position), data.get(position));
		} else {
			holder.imageView.setImageBitmap(bitmaps[position]);
		}

		view.setOnClickListener(new OnPhotoClick(position, holder.selectView));
		return view;
	}

	class Holder {
		ImageView imageView;
		ImageView selectView;
	}

	public class ImgClallBackLisner implements ImgCallBack {
		int num;

		public ImgClallBackLisner(int num) {
			this.num = num;
		}

		@Override
		public void resultImgCall(ImageView imageView, Bitmap bitmap) {
			bitmaps[num] = bitmap;
			imageView.setImageBitmap(bitmap);
		}
	}

	public interface OnItemClickClass {
		public void OnItemClick(View v, int Position, ImageView selectView);
	}

	class OnPhotoClick implements OnClickListener {
		int position;
		ImageView selectView;

		public OnPhotoClick(int position, ImageView selectView) {
			this.position = position;
			this.selectView = selectView;
		}

		@Override
		public void onClick(View v) {
			if (data != null && onItemClickClass != null) {
				onItemClickClass.OnItemClick(v, position, selectView);
			}
		}
	}

}
