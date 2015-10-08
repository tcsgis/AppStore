package com.changhong.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.changhong.CHActivity;
import com.changhong.activity.util.SmartDialog;
import com.changhong.util.netstate.NetWorkUtil.netType;
import com.llw.AppStore.R;

public class BaseActivity extends CHActivity
{
	private Dialog waitDialog;
	@Override
	protected void onPreOnCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onPreOnCreate(savedInstanceState);
	}

	@Override
	public void onConnect(netType type)
	{
		// TODO Auto-generated method stub
		super.onConnect(type);
		Toast.makeText(this, "网络连接开启", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onDisConnect()
	{
		// TODO Auto-generated method stub
		super.onDisConnect();
		Toast.makeText(this, "网络连接关闭", Toast.LENGTH_LONG).show();
	}
	
	/**
	 * 用toast方法显示资源信息
	 */
	protected void showToast(int rId) {
		
		String msg = getResources().getString(rId);
//		SmartDialog.createToast(getApplicationContext(), SmartDialog.TYPE_TOAST_ERR, msg).show();
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	}
	
	public void showWaitDialog() {
		if(waitDialog == null){
			Intent intent = new Intent();
			intent.putExtra(SmartDialog.DIALOG_MSG,
					this.getText(R.string.data_loading));
			waitDialog = SmartDialog.createDialog(this,
					SmartDialog.TYPE_WAIT, R.string.data_loading, null,
					intent);
		}
		setmDialog(waitDialog, false);
		waitDialog.show();
	}
	
}
