package cn.changhong.chcare.core.webapi.server;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import cn.changhong.chcare.core.webapi.AbstractChCareWebApi;
import cn.changhong.chcare.core.webapi.bean.ResponseBean;
import cn.changhong.chcare.core.webapi.handler.AsyncResponseCompletedHandler;
import cn.changhong.chcare.core.webapi.util.HttpRequestException;

public abstract class IFileService extends AbstractChCareWebApi implements IService {
	
	public abstract ResponseBean uploadFiles(ArrayList<String> paths, int width, int height)throws HttpRequestException;
	
	public <T> Future<ResponseBean<?>> uploadFiles(final ArrayList<String> paths, final int width, final int height,
			final AsyncResponseCompletedHandler<T> handler) {
		handler.onStart(paths, width, height);
		Future<ResponseBean<?>> future = executorProvider
				.doTask(new Callable<ResponseBean<?>>() {

					@Override
					public ResponseBean call() {
						ResponseBean bean = null;
						try {
							bean = uploadFiles(paths, width, height);
							handler.onCompleted(
									bean,
									ChCareWepApiServiceType.File_uploadFiles);
						} catch (HttpRequestException e) {
							handler.onThrowable(
									e,
									ChCareWepApiServiceType.File_uploadFiles);
						}
						return bean;
					}
				});
		return future;
	}
}
