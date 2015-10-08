package com.changhong.activity.widget;

import java.io.File;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.aaa.util.DMUtil;
import com.aaa.util.PhotoType;
import com.changhong.CHActivity;
import com.llw.AppStore.R;
import com.changhong.activity.crop.CropImageActivity;
import com.changhong.activity.photo.upload.ImgFileListActivity;
import com.changhong.activity.util.TimeParseUtil;
import com.changhong.activity.widget.PopupAltView.onPopupAltListner;
import com.changhong.common.AndroidVersionCheckUtils;
import com.changhong.util.CHLogger;
import com.changhong.util.FileUtils;
import com.changhong.util.cache.CHExternalOverFroyoUtils;
import com.changhong.util.cache.CHExternalUnderFroyoUtils;


public class PhotoSelectPopupView{

	private static final String TAG = "PhotoSelectPopupView";
	
	public static final int TAKE_PHOTO_FROM_LOCAL = 19001;
	public static final int TAKE_PHOTO_FROM_CAMERA = 19002;
	public static final int CUT_PHOTO = 19003;
	public static final int FROM_DIARY_ACTIVITY = 1;
	public static final int FROM_OTHER_ACTIVITY = 2;

	private Uri mCameraPhotoUri;
	private String TEMP_NAME= "cuttempimg.jpg";
	private String TEMP_DIR= "cuttmp";

	private CHActivity mActivity;
	private PopupAltView mPopupAltView;
	private onRestorePhotoListner mListener;

	public interface onRestorePhotoListner{
		public void onRestorePhoto();
	}

	public PhotoSelectPopupView(CHActivity activity) {
		// TODO Auto-generated constructor stub
		this.mActivity = activity;
		initPopupAltView(false);
	}

	public PhotoSelectPopupView(CHActivity activity, onRestorePhotoListner l){
		this.mActivity = activity;
		mListener = l;
		initPopupAltView(true);
	}

	private void initPopupAltView(boolean restorable) {
		if(! restorable){
			String line1 = mActivity.getResources().getString(R.string.photo_select_camera);
			String line2 = mActivity.getResources().getString(R.string.photo_select_local);
			mPopupAltView = new PopupAltView(mActivity, line1, line2, mPopAltListener);
		}else{
			String line1 = mActivity.getResources().getString(R.string.photo_select_local);
			String line2 = mActivity.getResources().getString(R.string.photo_select_camera);
			String line3 = mActivity.getResources().getString(R.string.photo_select_restore);
			mPopupAltView = new PopupAltView(mActivity, line1, line2, line3, mPopAltListener);
		}
	}

	private onPopupAltListner mPopAltListener = new onPopupAltListner() {
		@Override
		public void onPopupAltSelected(int result) {
			switch (result) {
			case PopupAltView.LINE_2:
				//TODO quxy 获取本地照片
				int flag = 0;
				if(mActivity.getLayouName().equals("publish_diary_activity")){
					flag = FROM_DIARY_ACTIVITY;//从发布日志跳转
				}else{
					flag = FROM_OTHER_ACTIVITY;//从其他界面跳转
				}
				Intent intent = new Intent(mActivity, ImgFileListActivity.class);
				intent.putExtra("from_layout", flag);
				mActivity.startActivityForResult(intent, TAKE_PHOTO_FROM_LOCAL);
				break;

			case PopupAltView.LINE_1:
				takeCamera();
				break;

			case PopupAltView.LINE_3:
				restorePhoto();
				break;

			default:
				break;
			}
			mPopupAltView.dismiss();
		}
	};

	private void selectLocalPhoto() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		intent.setAction(Intent.ACTION_GET_CONTENT);
		//4.4+ 用下面这个使用的是旧的选图, 但可能选到google auto backup的文件
		/*intent = new Intent(
				Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);*/
		mActivity.startActivityForResult(intent, TAKE_PHOTO_FROM_LOCAL);
	}

	private void takeCamera() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		File tempDir = null;
		if (AndroidVersionCheckUtils.hasGingerbread()) {
			tempDir = CHExternalOverFroyoUtils.getDiskCacheDir(mActivity, TEMP_DIR);
		} else {
			tempDir = CHExternalUnderFroyoUtils.getDiskCacheDir(mActivity, TEMP_DIR);
		}
		
//		File tempDir = CHExternalOverFroyoUtils.getDiskCacheDir(mActivity, TEMP_DIR);
		if(!tempDir.exists()){
			boolean b = tempDir.mkdir();
		}

		TEMP_NAME = TimeParseUtil.getSystemNowTime() + ".jpg";
		File tempFile = new File(tempDir,TEMP_NAME);  
		
		mCameraPhotoUri = Uri.fromFile(tempFile);
		Log.i(TAG, "mCameraPhotoUri=[" + mCameraPhotoUri + "]");
		intent.putExtra(MediaStore.EXTRA_OUTPUT, mCameraPhotoUri);
		mActivity.startActivityForResult(intent, TAKE_PHOTO_FROM_CAMERA);
	}

	private void restorePhoto() {
		if(mListener != null)
			mListener.onRestorePhoto();
	}

	/**
	 * 
	 * @return 返回照片的路径
	 */
	public Uri getPhotoUri() {
		return mCameraPhotoUri;
	}

	public void cutPhoto(Uri uri, int type){
		int width = 0;
		int height = 0;
		
		switch (type) {
		case PhotoType.PHOTO_FACE:
			width = DMUtil.getFacePhotoWidth(mActivity);
			height = DMUtil.getFacePhotoHeight(mActivity);
			break;
			
		case PhotoType.PHOTO_BID:
			width = DMUtil.getBidPhotoWidth(mActivity);
			height = DMUtil.getBidPhotoHeight(mActivity);
			break;
			
		case PhotoType.PHOTO_AD:
			width = DMUtil.getAdPhotoWidth(mActivity);
			height = DMUtil.getAdPhotoHeight(mActivity);
			break;
			
		case PhotoType.PHOTO_ELSE:
			width = DMUtil.getElsePhotoWidth(mActivity);
			height = DMUtil.getElsePhotoHeight(mActivity);
			break;
			
			default:
				width = DMUtil.getFacePhotoWidth(mActivity);
				height = DMUtil.getFacePhotoHeight(mActivity);
				break;
		}
		
		cut(uri, width, height);
	}
	
	/**
	 * 裁剪uri中的图像，获得小图
	 * @param uri
	 * @param duri 裁剪后保存的路径
	 * @param width <= 200
	 * @param height <= 200
	 */
	private void cut(Uri uri, int width, int height) {
		//FIXME:更好的透明判断
		boolean isAlpha = false;
		//转换uri格式到标准格式
		final File file = FileUtils.getFile(this.mActivity, uri);
		if (file != null) {
			uri = Uri.fromFile(file);
			final String name = file.getName();
			int separatorIndex = name.lastIndexOf('.');
			final String ext = (separatorIndex < 0) ? name : name.substring(separatorIndex + 1, name.length());
			isAlpha = "png".equalsIgnoreCase(ext);
		}

		File tempDir = null;
		if (AndroidVersionCheckUtils.hasGingerbread()) {
			tempDir = CHExternalOverFroyoUtils.getDiskCacheDir(mActivity, TEMP_DIR);
		} else {
			tempDir = CHExternalUnderFroyoUtils.getDiskCacheDir(mActivity, TEMP_DIR);
		}
		
//		File tempDir = CHExternalOverFroyoUtils.getDiskCacheDir(mActivity, TEMP_DIR);
		if(!tempDir.exists()){
			tempDir.mkdir();
		}
		TEMP_NAME = TimeParseUtil.getSystemNowTime() + ".jpg";
		File tempFile = new File(tempDir,TEMP_NAME);  
		mCameraPhotoUri = Uri.fromFile(tempFile);

		Bundle b = new Bundle();
		b.putInt(CropImageActivity.WIDTH, width);
		b.putInt(CropImageActivity.HEIGHT, height);
		b.putString(CropImageActivity.SAVE_PATH, mCameraPhotoUri.getPath());
		b.putBoolean(CropImageActivity.IS_ALPHA, isAlpha);

		if (mActivity != null) {
			Intent intent = new Intent(mActivity, CropImageActivity.class);
			intent.putExtras(b);
			intent.setData(uri);
			mActivity.startActivityForResult(intent, CUT_PHOTO);
		}
	}

	/**
	 * 显示Popup
	 */
	public void show(){
		mPopupAltView.show();
	}

	/**
	 * 将从本地或从相机获取图片的数据转换为File
	 */
	public File UriToFile(Intent data, int requestCode){
		try {
			Uri uri = null;
			File file = null;
			if (requestCode == TAKE_PHOTO_FROM_LOCAL) {
				if(data != null){
					uri = data.getData();
					file = Uri2File(uri);
				}
			} else if (requestCode == TAKE_PHOTO_FROM_CAMERA) {
				uri = getPhotoUri();
				Log.i(TAG,"camera---uri.getPath()=["+uri.getPath()+"]");
				file = new File(uri.getPath());
			}
			if(null != file && file.exists()){
				return file;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			CHLogger.e(TAG, e.getMessage());
		}
		return null;
	}

	public File Uri2File(Uri uri){
		//4.4+ get -> content://com.android.providers.media.documents/document/image:8044
		return FileUtils.getFile(mActivity, uri);
	}
}
