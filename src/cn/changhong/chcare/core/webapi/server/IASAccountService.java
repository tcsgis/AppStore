package cn.changhong.chcare.core.webapi.server;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import com.aaa.db.AppUser;

import cn.changhong.chcare.core.webapi.AbstractChCareWebApi;
import cn.changhong.chcare.core.webapi.bean.ResponseBean;
import cn.changhong.chcare.core.webapi.handler.AsyncResponseCompletedHandler;
import cn.changhong.chcare.core.webapi.util.HttpRequestException;

public abstract class IASAccountService extends AbstractChCareWebApi implements IService{
	
	public abstract ResponseBean getVetifyCode(String phoneNumber, int type, byte role)throws HttpRequestException;
	public <T> Future<ResponseBean> getVetifyCode(final String phoneNumber,
			final int type, final byte role, final AsyncResponseCompletedHandler<T> handler) {
		handler.onStart(phoneNumber,type);
		Future<ResponseBean> future = executorProvider
				.doTask(new Callable<ResponseBean>() {

					@Override
					public ResponseBean call() {
						ResponseBean bean = null;
						try {
							bean = getVetifyCode(phoneNumber, type, role);
							handler.onCompleted(
									bean,
									ChCareWepApiServiceType.AS_Account_getVetufyCode);
						} catch (HttpRequestException e) {
							handler.onThrowable(
									e,
									ChCareWepApiServiceType.AS_Account_getVetufyCode);
						}
						return bean;
					}
				});
		return future;
	}
	
	public abstract ResponseBean verifyCode(String username, String code, int type)throws HttpRequestException;
	public <T> Future<ResponseBean> verifyCode(final String username,
			final String code, final int type,
			final AsyncResponseCompletedHandler<T> handler){
		handler.onStart(username,type);
		Future<ResponseBean> future = executorProvider
				.doTask(new Callable<ResponseBean>() {

					@Override
					public ResponseBean call() {
						ResponseBean bean = null;
						try {
							bean = verifyCode(username, code, type);
							handler.onCompleted(
									bean,
									ChCareWepApiServiceType.AS_Account_VetufyCode);
						} catch (HttpRequestException e) {
							handler.onThrowable(
									e,
									ChCareWepApiServiceType.AS_Account_VetufyCode);
						}
						return bean;
					}
				});
		return future;
	}
	
	public abstract ResponseBean login(String username, String verifyCode)
			throws HttpRequestException;
	
	public <T> Future<ResponseBean> login(final String username,
			final String verifyCode,
			final AsyncResponseCompletedHandler<T> handler) {
		handler.onStart(username,verifyCode);
		Future<ResponseBean> future = executorProvider
				.doTask(new Callable<ResponseBean>() {

					@Override
					public ResponseBean call() {
						ResponseBean bean = null;
						try {
							bean = login(username, verifyCode);
							handler.onCompleted(
									bean,
									ChCareWepApiServiceType.AS_Account_login);
						} catch (HttpRequestException e) {
							handler.onThrowable(
									e,
									ChCareWepApiServiceType.AS_Account_login);
						}
						return bean;
					}
				});
		return future;
	}
	
public abstract ResponseBean logout() throws HttpRequestException;
	
	public <T> Future<ResponseBean> logout(
			final AsyncResponseCompletedHandler<T> handler) {
		Future<ResponseBean> future = executorProvider
				.doTask(new Callable<ResponseBean>() {
					@Override
					public ResponseBean call() {
						ResponseBean bean = null;
						try {
							bean = logout();
							handler.onCompleted(
									bean,
									ChCareWepApiServiceType.AS_Account_logout);
						} catch (HttpRequestException e) {
							handler.onThrowable(
									e,
									ChCareWepApiServiceType.AS_Account_logout);
						}
						return bean;
					}
				});
		return future;
	}
	
	public abstract ResponseBean updateSelfMg(AppUser user) throws HttpRequestException;
	public <T> Future<ResponseBean> updateSelfMg(final AppUser user,
			final AsyncResponseCompletedHandler<T> handler) {
		handler.onStart(user);
		Future<ResponseBean> future = executorProvider
				.doTask(new Callable<ResponseBean>() {
					@Override
					public ResponseBean call() {
						ResponseBean bean = null;
						try {
							bean = updateSelfMg(user);
							handler.onCompleted(
									bean,
									ChCareWepApiServiceType.AS_Account_updateSelfMg);
						} catch (HttpRequestException e) {
							handler.onThrowable(
									e,
									ChCareWepApiServiceType.AS_Account_updateSelfMg);
						}
						return bean;
					}
				});
		return future;
	}
	
	public abstract ResponseBean getApps(String tag) throws HttpRequestException;
	public <T> Future<ResponseBean> getApps(final String tag,
			final AsyncResponseCompletedHandler<T> handler) {
		handler.onStart(tag);
		Future<ResponseBean> future = executorProvider
				.doTask(new Callable<ResponseBean>() {
					@Override
					public ResponseBean call() {
						ResponseBean bean = null;
						try {
							bean = getApps(tag);
							handler.onCompleted(
									bean,
									ChCareWepApiServiceType.Account_getApps);
						} catch (HttpRequestException e) {
							handler.onThrowable(
									e,
									ChCareWepApiServiceType.Account_getApps);
						}
						return bean;
					}
				});
		return future;
	}
	
	public abstract ResponseBean uploadUserOperation(int[] ids) throws HttpRequestException;
	public <T> Future<ResponseBean> uploadUserOperation(final int[] ids,
			final AsyncResponseCompletedHandler<T> handler) {
		handler.onStart(ids);
		Future<ResponseBean> future = executorProvider
				.doTask(new Callable<ResponseBean>() {
					@Override
					public ResponseBean call() {
						ResponseBean bean = null;
						try {
							bean = uploadUserOperation(ids);
							handler.onCompleted(
									bean,
									ChCareWepApiServiceType.Account_uploadUserOperation);
						} catch (HttpRequestException e) {
							handler.onThrowable(
									e,
									ChCareWepApiServiceType.Account_uploadUserOperation);
						}
						return bean;
					}
				});
		return future;
	}
}
