package com.changhong.activity.photo.upload;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import com.changhong.activity.util.PictureUtil;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.widget.ImageView;

public class Util {
	Context context;

	public Util(Context context) {
		this.context = context;
	}

	/**
	 * 获取全部图片地址
	 * 
	 * @return
	 */
	public ArrayList<String> listAlldir() {
		Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		Uri uri = intent.getData();
		ArrayList<String> list = new ArrayList<String>();
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);// managedQuery(uri, proj, null, null, null);
		while (cursor.moveToNext()) {
			String path = cursor.getString(0);
			list.add(new File(path).getAbsolutePath());
		}
		cursor.close();
		return list;
	}

	public List<FileTraversal> LocalImgFileList() {
		List<FileTraversal> data;
		List<String> allimglist = listAlldir();
		if (allimglist != null && !allimglist.isEmpty()) {
			Map<String,FileTraversal> map = new TreeMap<String,FileTraversal>();
			// for (String path : allimglist) {
			for (int i = allimglist.size() - 1; i >= 0; i--) {
				String path = allimglist.get(i);
				String dir = getfileinfo(path);
				FileTraversal ftl = map.get(dir);
				if (ftl == null) {
					ftl = new FileTraversal();
					ftl.filename = dir;
					map.put(dir, ftl);
				}
				ftl.filecontent.add(path);
			}
			data = new ArrayList<FileTraversal>(map.values());
		} else {
			data = Collections.emptyList();
		}
		return data;
	}

	public String getfileinfo(String data) {
		if (data != null) {
			int index = data.lastIndexOf('/');
			if (index != -1) {
				int index2 = data.lastIndexOf('/', index - 1);
				data = data.substring(index2 + 1, index);
			} else {
				data = "";//???
			}
		} else {
			data = "";//???
		}
		return data;
	}

	public void imgExcute(ImageView imageView, ImgCallBack icb, String... params) {
		LoadBitAsync loadBitAsynk = new LoadBitAsync(imageView, icb);
		loadBitAsynk.execute(params);
	}

	public class LoadBitAsync extends AsyncTask<String, Integer, Bitmap> {
		ImageView imageView;
		ImgCallBack icb;

		LoadBitAsync(ImageView imageView, ImgCallBack icb) {
			this.imageView = imageView;
			this.icb = icb;
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			Bitmap bitmap = null;
			try {
				if (params != null) {
					final PictureUtil util = new PictureUtil(context);
					for (int i = 0; i < params.length; i++) {
						bitmap = util.getPathBitmap(Uri.fromFile(new File(params[i])), 200, 200);
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
			}
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			if (result != null) {
				icb.resultImgCall(imageView, result);
			}
		}
	}
}
