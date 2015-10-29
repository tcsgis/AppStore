package cn.changhong.chcare.core.webapi.server;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import com.aaa.db.AppDetail;

import cn.changhong.chcare.core.webapi.AbstractChCareWebApi;
import cn.changhong.chcare.core.webapi.bean.ResponseBean;
import cn.changhong.chcare.core.webapi.handler.AsyncResponseCompletedHandler;
import cn.changhong.chcare.core.webapi.util.HttpRequestException;

public abstract class IAdminService extends AbstractChCareWebApi implements IService{

	public abstract ResponseBean uploadAppDetail(AppDetail app)throws HttpRequestException;
	public <T> Future<ResponseBean> uploadAppDetail(final AppDetail app,
			final AsyncResponseCompletedHandler<T> handler) {
		handler.onStart(app);
		Future<ResponseBean> future = executorProvider
				.doTask(new Callable<ResponseBean>() {

					@Override
					public ResponseBean call() {
						ResponseBean bean = null;
						try {
							bean = uploadAppDetail(app);
							handler.onCompleted(
									bean,
									ChCareWepApiServiceType.Admin_uploadAppDetail);
						} catch (HttpRequestException e) {
							handler.onThrowable(
									e,
									ChCareWepApiServiceType.Admin_uploadAppDetail);
						}
						return bean;
					}
				});
		return future;
	}
	
	public abstract ResponseBean deleteApp(int id)throws HttpRequestException;
	public <T> Future<ResponseBean> deleteApp(final int id,
			final AsyncResponseCompletedHandler<T> handler) {
		handler.onStart(id);
		Future<ResponseBean> future = executorProvider
				.doTask(new Callable<ResponseBean>() {
					
					@Override
					public ResponseBean call() {
						ResponseBean bean = null;
						try {
							bean = deleteApp(id);
							handler.onCompleted(
									bean,
									ChCareWepApiServiceType.Admin_deleteApp);
						} catch (HttpRequestException e) {
							handler.onThrowable(
									e,
									ChCareWepApiServiceType.Admin_deleteApp);
						}
						return bean;
					}
				});
		return future;
	}
}
