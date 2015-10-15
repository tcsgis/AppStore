package com.aaa.activity.login;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.aaa.util.Constant;
import com.changhong.activity.BaseActivity;
import com.changhong.annotation.CHInjectView;
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

	@Override
	protected void onAfterOnCreate(Bundle savedInstanceState) {
		super.onAfterOnCreate(savedInstanceState);
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

			}
		});
		
		btn_yanzheng.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				doGetVerify();
			}
		});
	}
	
	private void doGetVerify(){
		handler.sendEmptyMessage(100);
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
