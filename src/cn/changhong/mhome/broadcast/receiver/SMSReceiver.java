package cn.changhong.mhome.broadcast.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

import com.changhong.activity.util.Tools;
import com.llw.AppStore.R;

public class SMSReceiver extends BroadcastReceiver{
	@Override
	public void onReceive(Context _context, Intent _intent) {
		switch (getResultCode()) {
		//delivery broadcast
		case Activity.RESULT_OK:
			Tools.showToast(_context, R.string.send_success);
			break;
		case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
			Tools.showToast(_context, R.string.send_failed);
			break;
		case SmsManager.RESULT_ERROR_RADIO_OFF:
			Tools.showToast(_context, R.string.send_failed);
			break;
		case SmsManager.RESULT_ERROR_NULL_PDU:
			Tools.showToast(_context, R.string.send_failed);
			break;
		case SmsManager.RESULT_ERROR_NO_SERVICE:
			Tools.showToast(_context, R.string.send_failed);
			break;
		case SmsManager.STATUS_ON_ICC_UNSENT:
			Tools.showToast(_context, R.string.send_failed);
			break;
		case SmsManager.STATUS_ON_ICC_SENT:
			Tools.showToast(_context, R.string.send_failed);
			break;
//		case SmsManager.STATUS_ON_ICC_UNREAD:
//			Tools.showToast(mContext, R.string.delivery_failed);
//			break;
//		case SmsManager.STATUS_ON_ICC_READ:
//			Tools.showToast(mContext, R.string.delivery_failed);
//			break;
		case SmsManager.STATUS_ON_ICC_FREE:
			Tools.showToast(_context, R.string.send_failed);
			break;
		}
		_context.unregisterReceiver(this);
	}
}
