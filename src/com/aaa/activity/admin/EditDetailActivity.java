package com.aaa.activity.admin;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import cn.changhong.chcare.core.webapi.bean.ResponseBean;
import cn.changhong.chcare.core.webapi.handler.AsyncResponseCompletedHandler;
import cn.changhong.chcare.core.webapi.server.CHCareWebApiProvider;
import cn.changhong.chcare.core.webapi.server.ChCareWepApiServiceType;
import cn.changhong.chcare.core.webapi.server.IAdminService;
import cn.changhong.chcare.core.webapi.server.IFileService;
import cn.changhong.chcare.core.webapi.server.CHCareWebApiProvider.WebApiServerType;

import com.aaa.db.AppDetail;
import com.aaa.db.AppDownloadState;
import com.aaa.util.Constant;
import com.aaa.util.DMUtil;
import com.aaa.util.IDNumUtil;
import com.aaa.util.MyTools;
import com.aaa.util.PhotoType;
import com.changhong.activity.BaseActivity;
import com.changhong.activity.util.PictureUtil;
import com.changhong.activity.widget.AppMainDialog;
import com.changhong.activity.widget.HorizontalListView;
import com.changhong.activity.widget.PhotoSelectPopupView;
import com.changhong.annotation.CHInjectView;
import com.changhong.util.CHLogger;
import com.changhong.util.bitmap.CHBitmapCacheWork;
import com.changhong.util.db.bean.CacheManager;
import com.llw.AppStore.R;

public class EditDetailActivity extends BaseActivity{

	@CHInjectView(id = R.id.name)
	private EditText name;
	@CHInjectView(id = R.id.size)
	private EditText size;
	@CHInjectView(id = R.id.version)
	private EditText version;
	@CHInjectView(id = R.id.developer)
	private EditText developer;
	@CHInjectView(id = R.id.download_url)
	private EditText download_url;
	@CHInjectView(id = R.id.order)
	private EditText order;
	@CHInjectView(id = R.id.package_name)
	private EditText package_name;
	@CHInjectView(id = R.id.type)
	private EditText type;
	@CHInjectView(id = R.id.tag)
	private EditText tag;
	@CHInjectView(id = R.id.desc)
	private EditText desc;
	@CHInjectView(id = R.id.logo)
	private ImageView logo;
	@CHInjectView(id = R.id.list)
	private HorizontalListView list;
	@CHInjectView(id = R.id.operation_bottom)
	private FrameLayout upload;
	@CHInjectView(id = R.id.delete)
	private RelativeLayout delete;
	
	private final int DESC_IMG = 1111;
	private final int LOGO = 1112;
	
	private PhotoSelectPopupView mPopupAltView;
	private PhotoAdapter adapter;
	private Uri mPhotoUri;
	private String logoPath = null;
	private int clickWhich = 0;
	private CHBitmapCacheWork imageFetcherLogo;
	private CHBitmapCacheWork imageFetcherDesc;
	private AppDetail app;
	
	private IFileService fileService = (IFileService) CHCareWebApiProvider.Self
	.defaultInstance().getDefaultWebApiService(
			WebApiServerType.FILE_SERVER);
	private IAdminService adminService = (IAdminService) CHCareWebApiProvider.Self
			.defaultInstance().getDefaultWebApiService(
					WebApiServerType.ADMIN_SERVER);
	
	@Override
	protected void onAfterOnCreate(Bundle savedInstanceState) {
		super.onAfterOnCreate(savedInstanceState);
		app = (AppDetail) getIntent().getSerializableExtra(Constant.APP_DETAIL);
		imageFetcherLogo = MyTools.getImageFetcher(this, getCHApplication(), false, 0, DMUtil.getFacePhotoWidth(this), DMUtil.getFacePhotoHeight(this));
		imageFetcherDesc = MyTools.getImageFetcher(this, getCHApplication(), false, 0, DMUtil.getElsePhotoWidth(this), DMUtil.getElsePhotoHeight(this));
		mPopupAltView = new PhotoSelectPopupView(this);
		
		findViewById(R.id.rl_left).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		logo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				clickWhich = LOGO;
				mPopupAltView.show();
			}
		});
		
		upload.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				checkFinish();
			}
		});
		
		initView();
	}
			
	private void initView() {
		if(app != null){
			logoPath = app.getLogoUrl();
			name.setText(app.getName());
			size.setText(String.valueOf(app.getSize()));
			version.setText(app.getVersion());
			developer.setText(app.getDeveloper());
			download_url.setText(app.getDownloadUrl());
			type.setText(String.valueOf(app.getType()));
			order.setText(String.valueOf(app.getOrder()));
			desc.setText(app.getDesc());
			tag.setText(app.getTag());
			package_name.setText(app.getPackageName());
			adapter = new PhotoAdapter(app.getDescUrl(), this, DESC_IMG);
			list.setAdapter(adapter);
			delete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					final AppMainDialog dialog = new AppMainDialog(EditDetailActivity.this, R.style.appdialog);
					dialog.withTitle(R.string.dialog_title)
					.withMessage(R.string.doA_string13)
					.setOKClick(R.string.ok_queren, new View.OnClickListener() {
						
						@Override
						public void onClick(View view) {
							doDelete();
							dialog.dismiss();
						}
					})
					.setCancelClick(R.string.cancel_quxiao).show();
				}
			});
			
			try {
				imageFetcherLogo.loadFormCache(app.getLogoUrl(), logo);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			delete.setVisibility(View.INVISIBLE);
			adapter = new PhotoAdapter(null, this, DESC_IMG);
			list.setAdapter(adapter);
		}
	}
	
	private void doDelete(){
		showWaitDialog();
		adminService.deleteApp(app.getID(), new AsyncResponseCompletedHandler<String>() {

			@Override
			public String doCompleted(ResponseBean<?> response, ChCareWepApiServiceType servieType) {
				if(response.getState() >= 0){
					Toast.makeText(EditDetailActivity.this, R.string.da_string10, Toast.LENGTH_SHORT).show();
					MyTools.removeAppDetail(app);
					finish();
				}
				return null;
			}
		});
	}
	
	private void checkFinish() {
		if(logoPath != null && MyTools.editNotNull(name) /*&& MyTools.editNotNull(size) */
				/*&& MyTools.editNotNull(version)*/ && MyTools.editNotNull(type) && MyTools.editNotNull(tag)
				/*&& MyTools.editNotNull(developer)*/ && MyTools.editNotNull(download_url)/*&& MyTools.editNotNull(order)*/
				&& MyTools.editNotNull(desc) /*&& MyTools.editNotNull(package_name)*/){
			
			doUpload();
		}else{
			Toast.makeText(this, R.string.da_string9, Toast.LENGTH_SHORT).show();
		}
	}
	
	private void doUpload() {
		if(app == null){
			app = new AppDetail();
		}
		uploadLogo();
	}

	private void uploadLogo() {
		if(logoPath.equals(app.getLogoUrl())){
			uploadDescPhoto();
		}else{
			showWaitDialog();
			ArrayList<String> ss = new ArrayList<String>();
			ss.add(logoPath);
			fileService.uploadFiles(ss, DMUtil.getFacePhotoWidth(this), DMUtil.getFacePhotoHeight(this), 
					new AsyncResponseCompletedHandler<String>() {
				
				@Override
				public String doCompleted(ResponseBean<?> response,
						ChCareWepApiServiceType servieType) {
					if(response.getState() >= 0){
						try {
							ArrayList<String> photos = MyTools.splitPhoto((String)response.getData());
							app.setLogoUrl(photos.get(0));
							uploadDescPhoto();
						} catch (Exception e) {
							e.printStackTrace();
							showToast(R.string.upload_fail);
							hideAllDialog();
						}
					}else{
						showToast(R.string.upload_fail);
						hideAllDialog();
					}
					return null;
				}
			});
		}
	}

	private void uploadDescPhoto(){
		final ArrayList<String> newPhotos = adapter.getPhotoPaths();
		ArrayList<String> uploadPhotos = new ArrayList<String>(); 
		if(newPhotos.size() > 0){
			for(int i = 0; i < newPhotos.size(); i++){
				if(app.getDescUrl() == null || ! app.getDescUrl().contains(newPhotos.get(i))){
					uploadPhotos.add(newPhotos.get(i));
					newPhotos.remove(i);
					i--;
				}
			}
		}
		
		if(uploadPhotos.size() == 0){
			app.setDescUrl(newPhotos);
			uploadAppDetail();
		}else{
			showWaitDialog();
			fileService.uploadFiles(uploadPhotos, DMUtil.getElsePhotoWidth(this), DMUtil.getElsePhotoWidth(this), 
					new AsyncResponseCompletedHandler<String>() {
				
				@Override
				public String doCompleted(ResponseBean<?> response,
						ChCareWepApiServiceType servieType) {
					if(response.getState() >= 0){
						try {
							ArrayList<String> photos = MyTools.splitPhoto((String)response.getData());
							newPhotos.addAll(photos);
							app.setDescUrl(newPhotos);
							uploadAppDetail();
						} catch (Exception e) {
							e.printStackTrace();
							showToast(R.string.upload_fail);
							hideAllDialog();
						}
					}else{
						showToast(R.string.upload_fail);
						hideAllDialog();
					}
					return null;
				}
			});
		}
	}
	
	private void uploadAppDetail(){
		app.setName(MyTools.getText(name));
		app.setSize(Float.valueOf(MyTools.getText(size)));
		app.setVersion(MyTools.getText(version));
		app.setDeveloper(MyTools.getText(developer));
		app.setDownloadUrl(MyTools.getText(download_url));
		app.setOrder(Integer.valueOf(MyTools.getText(order)));
		app.setPackageName(MyTools.getText(package_name));
		app.setDesc(MyTools.getText(desc));
		app.setTag(MyTools.getText(tag));//详情页中显示的小标签
		int t = Integer.valueOf(MyTools.getText(type));
		app.setType((byte)t);
		
		showWaitDialog();
		adminService.uploadAppDetail(app, new AsyncResponseCompletedHandler<String>() {

			@Override
			public String doCompleted(ResponseBean<?> response,
					ChCareWepApiServiceType servieType) {
				if(response.getState() >= 0){
					int id = (Integer) response.getData();
					app.setID(id);
					AppDownloadState data = new AppDownloadState(app);
					CacheManager.INSTANCE.putAppData(data);
					hideAllDialog();
					Toast.makeText(EditDetailActivity.this, R.string.upload_success, Toast.LENGTH_SHORT).show();
					finish();
				}else{
					Toast.makeText(EditDetailActivity.this, R.string.upload_fail, Toast.LENGTH_SHORT).show();
					hideAllDialog();
				}
				return null;
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			if (resultCode == RESULT_OK) {
				if (requestCode == PhotoSelectPopupView.TAKE_PHOTO_FROM_LOCAL){
					if (data != null) {
						String filepath = data.getStringExtra("filepath");
						Uri uri = Uri.fromFile(new File(filepath));
						mPopupAltView.cutPhoto(uri, getCutSize());
					}
				}
				else if(requestCode == PhotoSelectPopupView.TAKE_PHOTO_FROM_CAMERA) {
					Uri uri = mPopupAltView.getPhotoUri();
					mPopupAltView.cutPhoto(uri, getCutSize());
				}
				else if(requestCode == PhotoSelectPopupView.CUT_PHOTO){
					mPhotoUri = mPopupAltView.getPhotoUri();
					if(mPhotoUri != null && mPhotoUri.getPath() != null){
						File file = new File(mPhotoUri.getPath());
						if(file.exists() && file.isFile()){
							switch (clickWhich) {
							case LOGO:
								try {
									logoPath = file.getPath();
									logo.setImageBitmap(PictureUtil.
											decodeSampledBitmapFromFile(file.getPath(), 
													logo.getWidth(), 
													logo.getHeight()));
								}catch (Exception e) {
									e.printStackTrace();
								}catch (OutOfMemoryError e) {
									e.printStackTrace();
								}
								break;
								
							case DESC_IMG:
								adapter.addsalonPhoto(file.getPath());
								break;
							}
						}
						
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private int getCutSize(){
		int size = -1;
		switch (clickWhich) {
		case LOGO:
			size = PhotoType.PHOTO_FACE;
			break;
			
		case DESC_IMG:
			size = PhotoType.PHOTO_ELSE;
			break;
		}
		
		return size;
	}
	
	private class PhotoAdapter extends BaseAdapter{

		private ArrayList<String> list;
		private Context context;
		private int clickWhich = 0;
		
		public PhotoAdapter(ArrayList<String> list, Context context, int clickWhich) {
			if(list != null){
				this.list = (ArrayList<String>) list.clone();
			}else{
				this.list = new ArrayList<String>();
			}
			this.context = context;
			this.clickWhich = clickWhich;
			if(this.list.size() < Constant.MAX_DESC_IMG){
				this.list.add(this.list.size(), null);
			}
		}
		
		public PhotoAdapter(Context context, int clickWhich) {
			list = new ArrayList<String>();
			list.add(null);
			this.context = context;
			this.clickWhich = clickWhich;
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

		private void addsalonPhoto(String path){
			if(list.size() == Constant.MAX_DESC_IMG){
				list.remove(Constant.MAX_DESC_IMG - 1);
				list.add(path);
			}else if(list.size() < Constant.MAX_DESC_IMG){
				list.add(list.size() - 1, path);
			}
			
			notifyDataSetChanged();
		}
		
		private void removesalonPhoto(String path){
			if(list.size() == Constant.MAX_DESC_IMG){
				list.remove(path);
				if(list.get(list.size() - 1) != null){
					list.add(null);
				}
			}else if(list.size() < Constant.MAX_DESC_IMG){
				list.remove(path);
			}
			
			notifyDataSetChanged();
		}
		
		private ArrayList<String> getPhotoPaths(){
			ArrayList<String> ret = (ArrayList<String>) list.clone();
			ret.remove(null);
			return ret;
		}
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			final String path = list.get(position);
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(R.layout.item_photo_list, null);
				viewHolder.photo = (ImageView) convertView.findViewById(R.id.photo);
				viewHolder.delete = (Button) convertView.findViewById(R.id.delete);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
				
			viewHolder.photo.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(path == null){
						EditDetailActivity.this.clickWhich = clickWhich;
						mPopupAltView.show();
					}
				}
			});
			
			viewHolder.delete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					final AppMainDialog dialog = new AppMainDialog(context, R.style.appdialog);
					dialog.withTitle(R.string.dialog_title)
							.withMessage(R.string.da_string8)
									.setOKClick(R.string.ok_queren, new View.OnClickListener() {

										@Override
										public void onClick(View arg0) {
											removesalonPhoto(path);
											dialog.dismiss();
										}
									})
									.setCancelClick(R.string.cancel_quxiao).show();
				}
			});
			
			if(path == null){
				viewHolder.delete.setVisibility(View.INVISIBLE);
				viewHolder.photo.setImageResource(R.drawable.add_photo);
			}else{
				viewHolder.delete.setVisibility(View.VISIBLE);
				try {
					File file = new File(path);
					if(file.exists()){
						viewHolder.photo.setImageBitmap(PictureUtil.
								decodeSampledBitmapFromFile(path, 
										viewHolder.photo.getWidth(), 
										viewHolder.photo.getHeight()));
					}else{
						imageFetcherDesc.loadFormCache(path, viewHolder.photo);
					}
				}catch (Exception e) {
					e.printStackTrace();
				}catch (OutOfMemoryError e) {
					e.printStackTrace();
				}
			}
			
			return convertView;
		}
		
		final class ViewHolder{
			public ImageView photo;
			public Button delete;
		}
	}
}
