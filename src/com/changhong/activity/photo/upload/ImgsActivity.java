package com.changhong.activity.photo.upload;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import com.llw.AppStore.R;
import com.changhong.activity.BaseActivity;
import com.changhong.activity.photo.upload.ImgsAdapter.OnItemClickClass;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

public class ImgsActivity extends BaseActivity implements OnClickListener {

	private FileTraversal mFileTraversal;
	private GridView mImgGridView;
	private ImgsAdapter mImgsAdapter;
	private LinearLayout mSelectLayout;
	private Util mUtil;
//	private RelativeLayout mRelativeLayout;
	private HashMap<Integer, ImageView> mHashImage;
//	private Button mChooseBtn;
	private ArrayList<String> mFileList;
	private ImageButton mBackBtn;
	private Button mUploadBtn;
	private final static String TAG = "ImgsActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photogrally);
		initView();
	}

	private void initView() {
		mImgGridView = (GridView) findViewById(R.id.gridView1);
//		mSelectLayout = (LinearLayout) findViewById(R.id.selected_image_layout);
//		mRelativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout2);
//		mChooseBtn = (Button) findViewById(R.id.button3);
		mBackBtn = (ImageButton) findViewById(R.id.btn_back);
		mUploadBtn = (Button)findViewById(R.id.upload);
		mBackBtn.setOnClickListener(this);
		mUploadBtn.setOnClickListener(this);

		mSelectLayout = new LinearLayout(getApplicationContext());

		mFileTraversal = getIntent().getExtras().getParcelable("data");
		mImgsAdapter = new ImgsAdapter(this, mFileTraversal.filecontent, onItemClickClass);
		mImgGridView.setAdapter(mImgsAdapter);

		mHashImage = new HashMap<Integer, ImageView>();
		mFileList = new ArrayList<String>();
		// imgGridView.setOnItemClickListener(this);
		mUtil = new Util(this);
	}

	class BottomImgIcon implements OnItemClickListener {
		int index;

		public BottomImgIcon(int index) {
			this.index = index;
		}

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {}
	}

	@SuppressLint("NewApi")
	public ImageView iconImage(String filepath, int index, ImageView selectView/*CheckBox checkBox*/)
			throws FileNotFoundException {
		LinearLayout.LayoutParams params = new LayoutParams(10, 10);
		ImageView imageView = new ImageView(this);
		imageView.setLayoutParams(params);
		imageView.setBackgroundResource(R.drawable.empty_photo);
		float alpha = 100;
		imageView.setAlpha(alpha);
		mUtil.imgExcute(imageView, imgCallBack, filepath);
		imageView.setOnClickListener(new ImgOnclick(filepath, selectView));
		return imageView;
	}

	ImgCallBack imgCallBack = new ImgCallBack() {
		@Override
		public void resultImgCall(ImageView imageView, Bitmap bitmap) {
			imageView.setImageBitmap(bitmap);
		}
	};

	class ImgOnclick implements OnClickListener {
		String filepath;
		// CheckBox checkBox;
		ImageView selectView;

		public ImgOnclick(String filepath, ImageView selectView/*CheckBox checkBox*/) {
			this.filepath = filepath;
			this.selectView = selectView;
		}

		@Override
		public void onClick(View view) {
			selectView.setVisibility(View.GONE);
			// checkBox.setChecked(false);
			mSelectLayout.removeView(view);
			mUploadBtn.setText("确定(" + mSelectLayout.getChildCount() + ")");
			mFileList.remove(filepath);
		}
	}

	ImgsAdapter.OnItemClickClass onItemClickClass = new OnItemClickClass() {
		@Override
		public void OnItemClick(View v, int Position, ImageView selectView/*CheckBox checkBox*/) {
			String filapath = mFileTraversal.filecontent.get(Position);
			if(selectView.isShown()){
				selectView.setVisibility(View.GONE);
				mSelectLayout.removeView(mHashImage.get(Position));
				mFileList.remove(filapath);
				mUploadBtn.setText("确定(" + mSelectLayout.getChildCount() + ")");
			}else{
				try {
					// checkBox.setChecked(true);
					selectView.setVisibility(View.VISIBLE);
					Log.i("img", "img choise position->" + Position);
					ImageView imageView = iconImage(filapath, Position, selectView);
					if (imageView != null) {
						mHashImage.put(Position, imageView);
						mFileList.add(filapath);
						mSelectLayout.addView(imageView);
						mUploadBtn.setText("确定(" + mSelectLayout.getChildCount() + ")");
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			this.finish();
			break;
		case R.id.upload:
			finish();
			break;
		default:
			break;
		}
	}

	private void showToast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}
}
