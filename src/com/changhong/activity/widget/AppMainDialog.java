package com.changhong.activity.widget;


import com.llw.AppStore.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class AppMainDialog extends Dialog implements DialogInterface {

	private View layout;
	private TextView mTitle;
	private TextView mMessage;
	private Button mCancel;
	private Button mOK;
	private ImageView mDivide;

	private Context context;

	public AppMainDialog(Context context) {
		super(context);
		init(context);
		this.context = context;
	}

	public AppMainDialog(Context context, int theme) {
		super(context, theme);
		init(context);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/**
		 * Dialog布局
		 */
		DisplayMetrics Metrics = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(Metrics);
		WindowManager.LayoutParams params = getWindow().getAttributes();
		params.width = (int) (Metrics.widthPixels * 0.86);
		getWindow().setAttributes(params);
	}

	private void init(Context context) {
		layout = View.inflate(context, R.layout.app_main_dialog, null);

		mTitle = (TextView) layout.findViewById(R.id.title);
		mMessage = (TextView) layout.findViewById(R.id.message);
		mCancel = (Button) layout.findViewById(R.id.cancel_btn);
		mOK = (Button) layout.findViewById(R.id.confirm_btn);
		mDivide = (ImageView) layout.findViewById(R.id.divide_line);

		setContentView(layout);
	}

	public AppMainDialog withTitle(int title) {
		mTitle.setText(title);
		return this;
	}

	public AppMainDialog withTitle(String title) {
		mTitle.setText(title);
		return this;
	}

	public AppMainDialog withMessage(int msg) {
		mMessage.setVisibility(View.VISIBLE);
		mMessage.setText(msg);
		return this;
	}

	public AppMainDialog withMessage(int msg, int num) {
		mMessage.setVisibility(View.VISIBLE);
		mMessage.setText(context.getResources().getString(msg, num));
		return this;
	}

	public AppMainDialog withMessage(String msg) {
		mMessage.setVisibility(View.VISIBLE);
		mMessage.setText(msg);
		return this;
	}

	public AppMainDialog setCancelClick(int text, View.OnClickListener click) {
		mDivide.setVisibility(View.VISIBLE);
		mCancel.setVisibility(View.VISIBLE);
		mCancel.setText(text);
		mCancel.setOnClickListener(click);
		return this;
	}

	public AppMainDialog setCancelClick(int text) {
		mDivide.setVisibility(View.VISIBLE);
		mCancel.setVisibility(View.VISIBLE);
		mCancel.setText(text);
		mCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				cancel();
			}
		});
		return this;
	}

	public AppMainDialog setOKClick(int text, View.OnClickListener click) {
		mOK.setVisibility(View.VISIBLE);
		mOK.setText(text);
		mOK.setOnClickListener(click);
		return this;
	}

	public AppMainDialog isCancelbleOnTouchOutsDialog(boolean cancelable) {
		this.setCanceledOnTouchOutside(cancelable);
		return this;
	}

	public AppMainDialog isCancelable(boolean cancelable) {
		this.setCancelable(cancelable);
		return this;
	}

	@Override
	public void show() {
		super.show();
	}
}
