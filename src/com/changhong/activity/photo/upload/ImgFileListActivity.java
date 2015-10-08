package com.changhong.activity.photo.upload;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.changhong.activity.photo.upload.ImgsAdapter.OnItemClickClass;
import com.changhong.activity.widget.PhotoSelectPopupView;
import com.llw.AppStore.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

public class ImgFileListActivity extends Activity implements OnItemClickListener, OnClickListener {

	private ListView mListView;
	private Util mUtil;
	private ImgFileListAdapter mListAdapter;
	private List<FileTraversal> mLocalList;
	private Button mVerifyBtn;
	private ImageButton mBackBtn;
	private GridView mImgGridView;
	private FileTraversal mFileTraversal;
	private ImgsAdapter mImgsAdapter;
	private int mFromFlag = 0;
	private ArrayList<String> mSelectPics = new ArrayList<String>();//批量处理

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.imgfilelist);
		mFromFlag = getIntent().getIntExtra("from_layout", 0);
		initView();
		initData();
	}

	private void initView() {
		mListView = (ListView) findViewById(R.id.listView1);
		mVerifyBtn = (Button) findViewById(R.id.verify);
		mBackBtn = (ImageButton) findViewById(R.id.btn_back);
		mImgGridView = (GridView) findViewById(R.id.gridView);

		if(mFromFlag == 1){
			mVerifyBtn.setVisibility(View.VISIBLE);
		}else{
			mVerifyBtn.setVisibility(View.GONE);
		}
		mVerifyBtn.setOnClickListener(this);
		mBackBtn.setOnClickListener(this);

		mUtil = new Util(this);
		mLocalList = mUtil.LocalImgFileList();
	}

	private void initData() {
		List<HashMap<String, String>> listdata = new ArrayList<HashMap<String, String>>();
		Bitmap bitmap[] = null;
		if (mLocalList != null) {
			bitmap = new Bitmap[mLocalList.size()];
			for (int i = 0; i < mLocalList.size(); i++) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("filecount", mLocalList.get(i).filecontent.size() + "张");
				map.put("imgpath", mLocalList.get(i).filecontent.get(0) == null ? null : (mLocalList.get(i).filecontent.get(0)));
				map.put("filename", mLocalList.get(i).filename);
				listdata.add(map);
			}
		}
		mListAdapter = new ImgFileListAdapter(this, listdata);
		mListView.setAdapter(mListAdapter);
		mListView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		mListView.setVisibility(View.GONE);
		mImgGridView.setVisibility(View.VISIBLE);

		mFileTraversal = mLocalList.get(position);
		mImgsAdapter = new ImgsAdapter(this, mFileTraversal.filecontent, onItemClickClass);
		mImgGridView.setAdapter(mImgsAdapter);
	}

	ImgsAdapter.OnItemClickClass onItemClickClass = new OnItemClickClass() {
		@Override
		public void OnItemClick(View v, int Position, ImageView selectView) {
			if(mFromFlag == PhotoSelectPopupView.FROM_DIARY_ACTIVITY){//从家庭日志跳转
				String filepath = mFileTraversal.filecontent.get(Position);
				if (selectView.isShown()) {
					selectView.setVisibility(View.GONE);
					if(mSelectPics.contains(filepath)){
						mSelectPics.remove(filepath);
					}
				} else {
					selectView.setVisibility(View.VISIBLE);
					if(!mSelectPics.contains(filepath)){
						mSelectPics.add(filepath);
					}
				}
			}else{//从其他界面跳转
				String filepath = mFileTraversal.filecontent.get(Position);
				Intent intent = new Intent();
				intent.putExtra("filepath", filepath);
				setResult(RESULT_OK, intent);
				finish();
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		//批量
		case R.id.verify:
			Intent intent = new Intent();
			intent.putStringArrayListExtra("select_pic_list", mSelectPics);
			setResult(RESULT_OK, intent);
			finish();
			break;
		case R.id.btn_back:
			this.finish();
			break;

		default:
			break;
		}
	}

}
