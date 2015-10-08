package com.changhong.activity.util;

import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.changhong.CHActivity;
import com.changhong.CHApplication;
import com.llw.AppStore.R;
import com.changhong.activity.widget.AppMainDialog;

public class SmartDialog {
	private static final String TAG = "SmartDialog";
	public static final int RESULT_OK = 1;
	public static final int RESULT_CANCEL = 0;
	public static final int TYPE_INPUT = 0x100;
	public static final int TYPE_EXIT = 0x101;
	public static final int TYPE_DATE = 0x102;
	public static final int TYPE_WAIT = 0x103;
	public static final int TYPE_TOAST_ERR = 0x104;
	public static final String INPUT_TYPE = "inputType";
	public static final String DIALOG_TITLE = "title";
	public static final String DIALOG_MSG = "msg";
	public static final String YEAR = "year";
	public static final String MONTH = "month";
	public static final String DAY = "day";

	public interface OnDialogListener {
		public void onDialogClosed(int id, int resultCode, Object obj);
	}

	public static Dialog createDialog(Context context, int type, final int id,
			final OnDialogListener listener, Intent intent) {
		Dialog ret = null;
		switch (type) {
		case TYPE_INPUT:
			ret = createInputDialog(context, listener, id, intent);
			break;
		case TYPE_EXIT:
			ret = createExitDialog(context, listener, id, intent);
			break;
		case TYPE_DATE:
			ret = createDateDialog(context, listener, id, intent);
			break;
		case TYPE_WAIT:
			ret = createWaitDialog(context, listener, id, intent);
			break;
		}
		return ret;
	}

	public static Toast createToast(Context context, int type, String msg) {
		Toast toast = null;
		switch (type) {
		case TYPE_TOAST_ERR:
			toast = createErrorToast(context, msg);
			break;
		}
		return toast;
	}

	private static Toast createErrorToast(Context context, String msg) {
		Toast result = new Toast(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.dialog_err_toast, null);
		TextView textView = (TextView) layout.findViewById(R.id.toast_msg);
		textView.setText(msg);
		result.setView(layout);
		result.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 20);
		return result;
	}

	private static Dialog createWaitDialog(Context context,
			final OnDialogListener listener, final int id, Intent intent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View contentView = inflater.inflate(R.layout.dialog_wait, null);
		ImageView img = (ImageView) contentView.findViewById(R.id.iv_wait_img);
		TextView msg = (TextView) contentView.findViewById(R.id.tv_wait_msg);
		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
				context, R.anim.waitting);
		img.startAnimation(hyperspaceJumpAnimation);
		msg.setText(intent.getStringExtra(DIALOG_MSG));
		Dialog dialog = new Dialog(context, R.style.waitting_dialog);
		dialog.setContentView(contentView, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.MATCH_PARENT));
		dialog.setCanceledOnTouchOutside(false);
		return dialog;
	}

	private static Dialog createDateDialog(Context context,
			final OnDialogListener listener, final int id, Intent intent) {
		SmartDateSetListener dateListener = new SmartDateSetListener();
		dateListener.setOnListener(id, listener);
		DatePickerDialog dialog = new DatePickerDialog(context, dateListener,
				intent.getIntExtra(YEAR, 0), intent.getIntExtra(MONTH, 0),
				intent.getIntExtra(DAY, 0));
		if (intent.getStringExtra(DIALOG_TITLE) != null) {
			dialog.setTitle(intent.getStringExtra(DIALOG_TITLE));
		}
		dialog.setCancelable(false);
		dialog.setButton(context.getText(android.R.string.ok), dateListener);
		dialog.setButton2(context.getText(android.R.string.cancel),
				dateListener);
		return dialog;

	}

	private static Dialog createExitDialog(Context context,
			final OnDialogListener listener, final int id, Intent intent) {
		AlertDialog.Builder factory = new AlertDialog.Builder(context);
		factory.setTitle(intent.getStringExtra(DIALOG_TITLE));
		factory.setMessage(intent.getStringExtra(DIALOG_MSG));
		factory.setIcon(android.R.drawable.ic_dialog_alert);
		factory.setPositiveButton(android.R.string.ok,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (listener != null) {
							listener.onDialogClosed(id, RESULT_OK, null);
						}
					}
				});
		factory.setNegativeButton(android.R.string.cancel, null);
		return factory.create();
	}

	private static Dialog createInputDialog(Context context,
			final OnDialogListener listener, final int id, Intent intent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View contentView = inflater.inflate(R.layout.dialog_input, null);
		final EditText input = (EditText) contentView
				.findViewById(R.id.et_input);
		input.setFocusable(true);
		input.setFocusableInTouchMode(true);
		input.requestFocus();
		if (intent != null) {
			int type = intent
					.getIntExtra(INPUT_TYPE, InputType.TYPE_CLASS_TEXT);
			input.setInputType(type);
		}
		input.setOnEditorActionListener(new OnEditorActionListener() {
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					InputMethodManager imm = (InputMethodManager) v
							.getContext().getSystemService(
									Context.INPUT_METHOD_SERVICE);
					if (imm.isActive()) {
						imm.hideSoftInputFromWindow(
								v.getApplicationWindowToken(), 0);
					}
					if (listener != null) {
						listener.onDialogClosed(id, RESULT_OK, input.getText());
					}
				}
				return true;
			}
		});
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				InputMethodManager inputManager = (InputMethodManager) input
						.getContext().getSystemService(
								Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(input, 0);
			}
		}, 500);
		Dialog dialog = new Dialog(context, R.style.InputDialog);
		int width = DisplayUtil.getScreenWidth();
		dialog.setContentView(contentView, new LinearLayout.LayoutParams(width,
				LinearLayout.LayoutParams.MATCH_PARENT));
		dialog.getWindow().setGravity(Gravity.BOTTOM);
		return dialog;
	}

	private static class SmartDateSetListener implements
			DialogInterface.OnClickListener, OnDateSetListener {

		private boolean isReady = false;
		private OnDialogListener mListener;
		private int mId = 0;

		public void setOnListener(int id, OnDialogListener listener) {
			mId = id;
			mListener = listener;
		}

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			if (isReady) {
				Intent ret = new Intent();
				ret.putExtra(YEAR, year);
				ret.putExtra(MONTH, monthOfYear);
				ret.putExtra(DAY, dayOfMonth);
				if (mListener != null) {
					mListener.onDialogClosed(mId, RESULT_OK, ret);
				}
			}else{
				if (mListener != null) {
					mListener.onDialogClosed(mId, RESULT_CANCEL, null);
				}
			}
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			if (which == Dialog.BUTTON1) {
				isReady = true;
			} else {
				isReady = false;
			}
		}
	}
	
	/**
	 * 异地登录对话框
	 * @return
	 */
	public static Dialog createReloginDialog(CHActivity thisAc, final int resId){

		final AppMainDialog dialog = new AppMainDialog(thisAc, R.style.appdialog);
		dialog.withTitle(R.string.login_other_alert)
		.withMessage(R.string.login_other_alertinfo)
		.setOKClick(R.string.login_again, new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				if(resId == R.string.err_opt_noright){
					Tools.reMain();

				}else{
					Tools.reLogin(true);
				}
			}
		});
		dialog.isCancelbleOnTouchOutsDialog(false);
		return dialog;
	}
}
