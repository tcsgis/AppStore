package cn.changhong.chcare.core.webapi.handler;

import java.lang.reflect.Method;
import java.util.List;

import android.app.Dialog;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import cn.changhong.chcare.core.webapi.bean.ResponseBean;
import cn.changhong.chcare.core.webapi.server.ChCareWepApiServiceType;
import cn.changhong.chcare.core.webapi.util.HttpRequestException;

import com.changhong.CHActivity;
import com.changhong.CHApplication;
import com.changhong.activity.util.SmartDialog;
import com.changhong.activity.util.Tools;
import com.changhong.util.CHLogger;
import com.changhong.util.db.SQLiteDB;
import com.llw.AppStore.R;
/**
 * shdjsahdjs
 * dsaddfdsfdhgfhgf
 * @ClassName: AsyncResponseCompletedHandler  
 * @Description: TODO  
 * @author guoyang2011@gmail.com  
 * @date 2014-9-24 下午5:10:04  
 *   
 * @param <T>
 */
public abstract class AsyncResponseCompletedHandler<T> extends Handler{


	private static final String METHOD_SAVE = "Save";
	private static final int DO_FINISH = -10000;

	public AsyncResponseCompletedHandler(){
		super(Looper.getMainLooper());
	}

	/**
	 * ad
	 * @Title: onCompleted  
	 * @Description: TODO  
	 * @param @param response
	 * @param @return      
	 * @return TTTTTTTTT  
	 * @throws
	 */
	private SQLiteDB sqldb;
	private boolean isActivityDb;

	private boolean showErrToast = true;
	protected boolean getLoctionData = false;
	private boolean haveParams = false;

	private int retState;
	private ChCareWepApiServiceType servieType;
	protected Object[] params;

	public void onStart(Object... params) {

		haveParams = true;
		this.params = params;
	}
	
	public void onCompleted(ResponseBean<?> response,ChCareWepApiServiceType servieType){

		boolean isOk = false;

		if(response == null){
			CHLogger.e(this, "接口信息错误！");
			return;
		}


		try {

			this.retState = response.getState();
			this.servieType = servieType;
			isOk = checkHttpState(response.getState());
			
			/*if(isOk){
				doSaveLocationDatas(response, servieType);
				sendOkInfo(response);
			}else if(getLoctionData){

				this.sendEmptyMessage(R.string.get_location_data);
			}else{  

				sendOkInfo(response);非网络异常的情况不允许获取本地数据
			}*/
			sendOkInfo(response, isOk?0:-1);
			
			this.sendEmptyMessage(DO_FINISH);
			
		} catch (Exception e) {
			// TODO: handle exception
			CHLogger.e(this, e.getMessage());
		}

	}

	public void onThrowable(HttpRequestException t,ChCareWepApiServiceType servieType){

		this.servieType = servieType;
		this.retState = t.getErrorType().getValue();

		if(showErrToast){
			int toastId = R.string.unable_get_data;
//			int toastId = R.string.err_net_connected;

			switch(t.getErrorType().getValue()){
			case -200:
			case -300:{
				break;
			}
			case -500:
			case -100:{
				toastId = R.string.unable_get_data;
				break;
			}
			}
//			this.sendEmptyMessage(toastId);
		}

		if(getLoctionData){
			this.sendEmptyMessage(R.string.get_location_data);
		}else{

			ResponseBean<?> response = new ResponseBean<String>();
			response.setState(retState);
			response.setDesc(t.getMessage());
			sendOkInfo(response, -1);
		}

		this.sendEmptyMessage(DO_FINISH);
	}
	
	/**
	 * 
	 * @param response
	 * @param checkState 0表示正常,非0表示异常
	 */
	private void sendOkInfo(ResponseBean<?> response, int checkState){
		
		Message msg = new Message();
		msg.obj = response;
		msg.arg1 = checkState;
		msg.what = R.string.ok_queren;
		this.sendMessage(msg);
	}

	public abstract T doCompleted(ResponseBean<?> response,ChCareWepApiServiceType servieType);



	@Override
	public void handleMessage(Message msg) {

		if(CHApplication.getApplication() == null) return;

		try {
			switch (msg.what) {
			case R.string.login_other_alertinfo:{//异地登录界面
				showDialog(msg.what);
				break;
			}
			case R.string.get_location_data:{//获取本地数据
				ResponseBean<?> response = doGetLocationDatas(servieType, params);
				if(response == null) response = new ResponseBean<List<?>>();
				response.setLocData(true);
				response.setState(retState);

				this.doCompleted(response,servieType);
				break;
			}
			case R.string.ok_queren:{//执行doCompleted
				
				ResponseBean<?> response = (ResponseBean<?>)msg.obj;
				int checkState = msg.arg1;
				
				try {
					if(checkState == 0){
						doSaveLocationDatas(response, servieType);
					}
				} catch (Exception e) {
					// TODO: handle exception
					CHLogger.e(this, e.getMessage());
				}
				
				this.doCompleted(response,servieType);
				break;
			}
			case DO_FINISH :
				releaseSQLite();
				break;
			default:{
				Tools.showToast(CHApplication.getApplication(), msg.what);
				break;
			}
			}
		} catch (Exception e) {
			CHLogger.e(this, e);
			try {
				ResponseBean<?> response = new ResponseBean<String>();
				response.setState(retState);
				this.doCompleted(response, servieType);
			} catch (Exception e2){
				//ignore it
				CHLogger.e(this, e2);
			}
		}

	}


	public boolean isShowAbleErrToast() {
		return showErrToast;
	}

	public void setShowAbleErrToast(boolean showErrToast) {
		this.showErrToast = showErrToast;
	}

	/**
	 * 对请求返回状态进行分类处理
	 * //0成功;-1数据库错误;-2 未登录;-3 无此用户; -5参数非法;-15 异地登陆
	 * @param object
	 */
	protected boolean checkHttpState(int state)
	{
		boolean ret = false;
		CHLogger.e(this, "state="+state);
		int toastId = -1;
		
		if(state >= 0){
			return true;
		}
		
		switch(state){
		case -15:{
			toastId = R.string.login_other_alertinfo;
			break;
		}
		case -12:
		case -7:{
//			toastId = R.string.err_opt_noright;
			break;
		}
		case -1:{
//			toastId = R.string.err_server_param;
			break;
		}
		case -2:{
//			toastId = R.string.err_login_invalid;
			break;
		}
		case -3:{
//			toastId = R.string.err_login;
			break;
		}
		}
		if(toastId != -1)this.sendEmptyMessage(toastId);
		return ret;
	}

	/**
	 * 异地登录对话框
	 * @return
	 */
	private void showDialog(int resId){

		try {
			CHActivity thisAc = CHApplication.getApplication().getCurrentActivity();
			if(!(thisAc == null || thisAc.isFinishing())){
				thisAc.hideAllDialog();
				Dialog dialog = SmartDialog.createReloginDialog(thisAc, resId);
				thisAc.setmDialog(dialog, false);
				dialog.show();
			}
		} catch (Exception e) {
			// TODO: handle exception
			CHLogger.e(this, e.getMessage());
		}
	}



	/**
	 * 执行获取本地数据的方法
	 * @param response
	 * @param servieType
	 * @return
	 */
	private ResponseBean<?> doGetLocationDatas(ChCareWepApiServiceType servieType, Object[] params) {
		// TODO Auto-generated method stub

		try {

			if(haveParams && getSqlitdb() != null){
				Method[] methods= this.getClass().getMethods();
				String methName = servieType.getValue();
				for(Method md: methods){
					if(md.getName().equals(methName)){
						Object obj = md.invoke(this, params);

						if(ResponseBean.class.isInstance(obj)){
							ResponseBean<?> locRes = (ResponseBean<?>)obj;
							locRes.setLocData(true);
							return locRes;
						}else{
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			CHLogger.e(this, e.getMessage());
		}
		return null;
	}

	/**
	 * 执行保存本地数据的方法
	 * @param response
	 * @param servieType
	 * @return
	 */
	private void doSaveLocationDatas(ResponseBean<?> response, ChCareWepApiServiceType servieType) {
		// TODO Auto-generated method stub

		try {

			Method[] methods= this.getClass().getMethods();
			String methName = servieType.getValue() + METHOD_SAVE;
			for(Method md: methods){
				if(md.getName().equals(methName)){
					md.invoke(this, response);
					break;
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			CHLogger.e(this, e.getMessage());
		}
	}

	protected SQLiteDB getSqlitdb() {

		try {
			if(sqldb == null) {
				CHActivity mActivity = CHApplication.getApplication().getCurrentActivity();
				if(mActivity != null) {
					sqldb = mActivity.getSqlitdb();
					isActivityDb = true;
				} else {
					sqldb = CHApplication.getApplication().getSQLiteDatabasePool().getSQLiteDatabase();
					isActivityDb = false;
					CHLogger.e(this, "NO activity found.");
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			CHLogger.e(this, e);
		}
		
		return sqldb;
	}

	protected void saveOrUpateObj(Object obj , String where){
		this.saveOrUpateObj(obj , where, true);
	}
	protected void saveOrUpateObj(Object obj , String where, boolean isUpdate){

		try {

			if(obj == null) return;
			List<?> objs= getSqlitdb().query(obj.getClass(), true, where, null, null, null, "1");

			
			if(objs == null || objs.isEmpty()){
				getSqlitdb().insert(obj);
			}else{
				int len = objs.size();
				if(len > 1){
					for(Object entry: objs){
						getSqlitdb().delete(entry);
					}
					getSqlitdb().insert(obj);
				}else if(isUpdate){
					getSqlitdb().update(obj);
				}
				
			}

		} catch (Exception e) {
			// TODO: handle exception
			CHLogger.e(this, e.getMessage());
		}

	}
	
	protected void saveObj(Object obj , String where){

		this.saveOrUpateObj(obj , where, false);
	}
	
	protected void deleteObj(Class<?> clazz , String where){

		try {

			getSqlitdb().delete(clazz, where);
		} catch (Exception e) {
			// TODO: handle exception
			CHLogger.e(this, e.getMessage());
		}

	}
	
	private void releaseSQLite(){
		
		try {
			if(!isActivityDb){
				if(CHApplication.getApplication().getSQLiteDatabasePool() != null)
					CHApplication.getApplication().getSQLiteDatabasePool().releaseSQLiteDatabase(sqldb);
				sqldb = null;
				isActivityDb = true;
			}
		} catch (Exception e) {
			// TODO: handle exception
			CHLogger.e(this, e.getMessage());
		}
	}
}
