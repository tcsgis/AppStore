package com.changhong.activity.photo.upload;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.llw.AppStore.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ImgFileListAdapter extends BaseAdapter {

	private Context context;
	private List<HashMap<String, String>> mListData;
	private Util util;
	private Bitmap[] bitmaps;
	private int index = -1;
	private List<View> holderlist;
	private String filecount = "filecount";
	private String filename = "filename";
	private String imgpath = "imgpath";

	public ImgFileListAdapter(Context context, List<HashMap<String, String>> listdata) {
		this.context = context;
		this.mListData = listdata;
		bitmaps = new Bitmap[listdata.size()];
		util = new Util(context);
		holderlist = new ArrayList<View>();
	}

	@Override
	public int getCount() {
		return mListData.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mListData.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int position, View view, ViewGroup arg2) {
		Holder holder;
		if (position != index && position > index) {
			holder=new Holder();
			view=LayoutInflater.from(context).inflate(R.layout.imgfileadapter, null);
			holder.photo_imgview=(ImageView) view.findViewById(R.id.filephoto_imgview);
			holder.filecount_textview=(TextView) view.findViewById(R.id.filecount_textview);
			holder.filename_textView=(TextView) view.findViewById(R.id.filename_textview);
			view.setTag(holder);
			holderlist.add(view);
		}else{
			holder= (Holder)holderlist.get(position).getTag();
			view=holderlist.get(position);
		}

		holder.filename_textView.setText(mListData.get(position).get(filename));
		holder.filecount_textview.setText(mListData.get(position).get(filecount));
		if (bitmaps[position] == null) {
			util.imgExcute(holder.photo_imgview, new ImgCallBack() {
				@Override
				public void resultImgCall(ImageView imageView, Bitmap bitmap) {
					bitmaps[position] = bitmap;
					imageView.setImageBitmap(bitmap);
				}
			}, mListData.get(position).get(imgpath));
		} else {
			holder.photo_imgview.setImageBitmap(bitmaps[position]);
		}

		return view;
	}

	class Holder {
		public ImageView photo_imgview;
		public TextView filecount_textview;
		public TextView filename_textView;
	}
}
