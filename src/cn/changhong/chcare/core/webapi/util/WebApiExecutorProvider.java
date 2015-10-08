package cn.changhong.chcare.core.webapi.util;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.changhong.util.CHLogger;

public class WebApiExecutorProvider {
	private ExecutorService executorService;

	private WebApiExecutorProvider() {
		if (this.executorService == null) {
			int count = Math.min(3, (int) (Runtime.getRuntime()
					.availableProcessors() * 1.2 + 1));
			// this.executorService = Executors.newFixedThreadPool(count);
			this.executorService = new ThreadPoolExecutor(count, count, 10000,
					TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(20,
							true));

		}
	}

	public <T> Future<T> doTask(Callable<T> task) {
		
		try {
			FutureTask<T> future = new FutureTask<T>(task);
			this.executorService.execute(future);
			return future;
		} catch (Exception e) {
			// TODO: handle exception
			CHLogger.e(this, e);
		}
		return null;
	}

	public void shutdown() {
		this.executorService.shutdown();
	}

	public static class Self {
		private static WebApiExecutorProvider instance;

		public static WebApiExecutorProvider defaultInstance() {
			if (instance == null)
				instance = new WebApiExecutorProvider();
			return instance;
		}
	}
}
