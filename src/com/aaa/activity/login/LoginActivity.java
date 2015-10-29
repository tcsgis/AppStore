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
import cn.changhong.chcare.core.webapi.bean.ResponseBean;
import cn.changhong.chcare.core.webapi.handler.AsyncResponseCompletedHandler;
import cn.changhong.chcare.core.webapi.server.CHCareWebApiProvider;
import cn.changhong.chcare.core.webapi.server.CHCareWebApiProvider.WebApiServerType;
import cn.changhong.chcare.core.webapi.server.ChCareWepApiServiceType;
import cn.changhong.chcare.core.webapi.server.IASAccountService;

import com.aaa.db.AppUser;
import com.aaa.util.Constant;
import com.aaa.util.MyTools;
import com.aaa.util.Role;
import com.changhong.activity.BaseActivity;
import com.changhong.annotation.CHInjectView;
import com.changhong.util.config.CHIConfig;
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
	private boolean getVerify = false;
	
	private IASAccountService accountService = (IASAccountService) CHCareWebApiProvider.Self
			.defaultInstance().getDefaultWebApiService(
					WebApiServerType.AS_ACCOUNT_SERVER);

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
						doVerify();
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
		getVerify = false;
		accountService.getVetifyCode(phone_num, 2, Role.UNDIFINED, 
				new AsyncResponseCompletedHandler<String>() {

			@Override
			public String doCompleted(ResponseBean<?> response, ChCareWepApiServiceType servieType) {
				if(response.getState() == 0){
					handler.sendEmptyMessage(100);
					getVerify = true;
					hideAllDialog();
					Toast.makeText(LoginActivity.this, R.string.login_string10, Toast.LENGTH_SHORT).show();
				}else{
					doResponseInfo(response);
				}
				return null;
			}
		});
	}
	
	private void doVerify() {
		accountService.verifyCode(phone_num, yanzhengma.getEditableText().toString(), 2,
				new AsyncResponseCompletedHandler<String>(){

					@Override
					public String doCompleted(ResponseBean<?> response,
							ChCareWepApiServiceType servieType) {
						if(response.getState() == 0){
							doLogin();
						}else{
							doResponseInfo(response);
						}
						return null;
					}
			
		});
	}
	
	private void doLogin() {
		showWaitDialog();
		accountService.login(phone_num, yanzhengma.getEditableText().toString(), 
				new AsyncResponseCompletedHandler<String>() {
			
			@Override
			public String doCompleted(ResponseBean<?> response,
					ChCareWepApiServiceType servieType) {
				if(response.getState() >= 0){
					hideAllDialog();
					AppUser user = (AppUser) response.getData();
					CacheManager.INSTANCE.setCurrentUser(user);
					MyTools.saveToken(LoginActivity.this);
					
					CHIConfig config = LoginActivity.this.getCHApplication().getPreferenceConfig();
					if(phone_num != null){
						config.setString(Constant.USERNAME, phone_num);
					}
					
					if(bundle != null){
						Intent data = new Intent();
						data.putExtras(bundle);
						setResult(Constant.RESULT_LOGIN_SUCCEED, data);
					}
					
					finish();
				}else{
					doResponseInfo(response);
				}
				return null;
			}
		});
	}
	
	private boolean validYanzhengma(){
		if(MyTools.getText(yanzhengma).length() != 6){
			return false;
		}
		return true;
	}
	
	private void doResponseInfo(ResponseBean<?> reBean){
		
		if(reBean.getState() == 0){
			
		}else{
			doVerfiyCodeErr(reBean.getState());
		}
		
		hideAllDialog();
	}
	
	private void doVerfiyCodeErr(int err) {
		if(err == -16){
			showToast(R.string.err_verfiycode_timeout);
		}else if(err == -17){
			showToast(R.string.err_phone_isused);
		}else if(err == -3){
			showToast(R.string.err_forget_phone);
		}else if(err == -8){
			showToast(R.string.err_verfiycode);
		}else if(err == -4){
			showToast(R.string.login_string24);
		}else if(err == -18){
			showToast(R.string.login_string25);
		}
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
