package com.aaa.activity.detail;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaa.db.AppDetail;
import com.aaa.util.MyTools;
import com.changhong.activity.BaseActivity;
import com.changhong.annotation.CHInjectView;
import com.llw.AppStore.R;

public class DetailActivity extends BaseActivity {

	@CHInjectView(id = R.id.logo)
	private ImageView logo;
	@CHInjectView(id = R.id.name)
	private TextView name;
	@CHInjectView(id = R.id.txt_title)
	private TextView txt_title;
	@CHInjectView(id = R.id.type)
	private TextView type;
	@CHInjectView(id = R.id.version)
	private TextView version;
	@CHInjectView(id = R.id.size)
	private TextView size;
	@CHInjectView(id = R.id.developer)
	private TextView developer;
	@CHInjectView(id = R.id.state_txt)
	private TextView state_txt;
	@CHInjectView(id = R.id.desc)
	private TextView desc;
	@CHInjectView(id = R.id.state_txt_bottom)
	private TextView state_txt_bottom;
	@CHInjectView(id = R.id.operation)
	private RelativeLayout operation;
	@CHInjectView(id = R.id.operation_bottom)
	private FrameLayout operation_bottom;
	@CHInjectView(id = R.id.progressBar)
	private ProgressBar progressBar;
	
	public static final String DATA = "DetailActivity.DATA";
	
	private AppDetail data;
	
	@Override
	protected void onAfterOnCreate(Bundle savedInstanceState) {
		super.onAfterOnCreate(savedInstanceState);
		data = (AppDetail) getIntent().getSerializableExtra(DATA);
		if(data == null){
			finish();
		}else{
			initView();
		}
	}

	private void initView() {
		test();
		String sizeString = MyTools.float2String(data.getSize());
		txt_title.setText(data.getName());
		name.setText(data.getName());
		version.setText(data.getVersion());
		type.setText("网络游戏");//todo
		size.setText(getString(R.string.da_string2, sizeString));
		developer.setText(data.getDeveloper());
		state_txt.setText("下载");
		desc.setText(data.getDesc());
		state_txt_bottom.setText(getString(R.string.da_string3, sizeString));
		Drawable drawable = getResources().getDrawable(R.drawable.download_bottom);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
		state_txt_bottom.setCompoundDrawables(drawable, null, null, null);
		
		findViewById(R.id.rl_left).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void test() {
		data.setName("梦幻西游");
		data.setType((byte)21);
		data.setDesc(getString(R.string.da_string4));
		data.setSize(268.6f);
		data.setDeveloper("网易");
		data.setVersion("1.40.0");
	}
}
