package com.aaa.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aaa.db.AppUser;
import com.aaa.util.Constant;
import com.aaa.util.MyTools;
import com.changhong.activity.BaseActivity;
import com.changhong.annotation.CHInjectView;
import com.changhong.util.CHLogger;
import com.changhong.util.db.bean.CacheManager;
import com.llw.AppStore.R;

public class LoginActivity extends BaseActivity {

	@CHInjectView(id = R.id.btn_yanzheng)
	private Button btn_yanzheng;
	@CHInjectView(id = R.id.yanzhengma)
	private EditText yanzhengma;
	@CHInjectView(id = R.id.phone)
	private EditText phone;
	
	private MyHandler handler = new MyHandler();
	private int time = 0;
	private String phone_num;
	private Bundle bundle;

	@Override
	protected void onAfterOnCreate(Bundle savedInstanceState) {
		super.onAfterOnCreate(savedInstanceState);
		bundle = getIntent().getExtras();
		initView();
	}

	private void initView() {
		findViewById(R.id.rl_left).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		findViewById(R.id.done).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(MyTools.validPhone(phone)){
					if(validYanzhengma()){
						doVerity();
					}else{
						Toast.makeText(LoginActivity.this, R.string.login_string8, Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(LoginActivity.this, R.string.login_string9, Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		btn_yanzheng.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(MyTools.validPhone(phone)){
					phone_num = MyTools.getText(phone);
					doGetVerify();
				}else{
					Toast.makeText(LoginActivity.this, R.string.login_string9, Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	
	private void doGetVerify(){
		handler.sendEmptyMessage(100);
	}
	
	private void doVerity() {
		doLogin();
	}
	
	private void doLogin() {
		test();
		if(bundle != null){
			Intent data = new Intent();
			data.putExtras(bundle);
			setResult(Constant.RESULT_LOGIN_SUCCEED, data);
		}
		finish();
	}
	
	private boolean validYanzhengma(){
		if(MyTools.getText(yanzhengma).length() != 6){
			return false;
		}
		return true;
	}
	
	private void test(){
		AppUser user = new AppUser();
		user.setID(1);
		user.setName(MyTools.getText(phone));
		CacheManager.INSTANCE.setCurrentUser(user);
	}
	
	private class MyHandler extends Handler{
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 100:
				btn_yanzheng.setBackgroundResource(R.drawable.salon_btn_invalid);
				btn_yanzheng.setClickable(false);
				time = Constant.VERIFY_CODE_INTERVAL;
				handler.sendEmptyMessageDelayed(101, 10);
				break;

			case 101:
				btn_yanzheng.setText(getResources().getString(R.string.login_string6, time));
				time--;
				handler.sendEmptyMessageDelayed(101, 1000);
				if(time == 1){
					handler.removeMessages(101);
					handler.sendEmptyMessageDelayed(102, 1000);
				}
				break;
			
			case 102:
				btn_yanzheng.setBackgroundResource(R.drawable.salon_btn);
				btn_yanzheng.setText(R.string.login_string4);
				btn_yanzheng.setClickable(true);
				break;
			}
		}
	}
}
