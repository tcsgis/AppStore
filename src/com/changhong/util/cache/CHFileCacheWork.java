/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.changhong.util.cache;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import com.changhong.common.AsyncTask;
import com.changhong.util.CHLogger;
import com.changhong.util.bitmap.CHDownLoadFilePathHandler;

/**
 * @Title CHFileCacheWork
 * @Package com.changhong.util.cache
 * @Description 缓存的工作类
 * @version V1.0
 */
public class CHFileCacheWork<ResponseObject>
{
	private CHFileCache mCHFileCache;
	private volatile boolean mExitTasksEarly = false;
	protected boolean mPauseWork = false;
	private final Object mPauseWorkLock = new Object();
	protected static final int MESSAGE_CLEAR = 0;
	protected static final int MESSAGE_INIT_DISK_CACHE = 1;
	protected static final int MESSAGE_FLUSH = 2;
	protected static final int MESSAGE_CLOSE = 3;
	//FIXME:处理聊天的清空问题
	private WeakHashMap<ResponseObject, CHCacheEntity<ResponseObject>> mCacheEntityHashMap = new WeakHashMap<ResponseObject, CHCacheEntity<ResponseObject>>();
	private CHCallBackHandler<ResponseObject> mCallBackHandler;
	private CHProcessDataHandler mProcessDataHandler;

	/**
	 * 从缓存加载数据
	 * 
	 * @param data
	 *            缓存的标识
	 * @param responseObject
	 *            对缓存结果响应的类
	 * @param localUrl 本地文件路径    
	 * @param downType　下载方式,HttpUrl:表示通过URL进行下载(照片墙为此下载方式);默认为通过api接口下载                 
	 */
	public void loadFormCache(Object data, ResponseObject responseObject,String localUrl, String downType){
//		localUrl = (String) data;
		CHCacheEntity<ResponseObject> cacheEntity;
		if (!mCacheEntityHashMap.containsKey(responseObject))
		{
			cacheEntity = new CHCacheEntity<ResponseObject>();
			cacheEntity.setT(responseObject);
			mCacheEntityHashMap.put(responseObject, cacheEntity);
		} else
		{
			cacheEntity = mCacheEntityHashMap.get(responseObject);
		}
		if (data == null)
		{
			;
		}
		byte[] buffer = null;

		if (mCHFileCache != null)
		{
			buffer = mCHFileCache.getBufferFromMemCache(String.valueOf(data));
		}
		if (buffer != null)
		{
			// 如果返回不为空
			if (mCallBackHandler != null)
			{
				callBackSucess(data, responseObject, buffer);
			}

		} else if (cancelPotentialWork(data, cacheEntity))
		{
			//FIXME:什么逻辑
			final BufferWorkerTask task = new BufferWorkerTask(cacheEntity);
			final CHAsyncEntity asyncEntity = new CHAsyncEntity(task);
			if (mCallBackHandler != null)
			{
				mCallBackHandler.onStart(responseObject, data);
			}
			cacheEntity.setAsyncEntity(asyncEntity);
			task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, data, localUrl, downType);
		}
	}
	
	private void callBackSucess(Object data, ResponseObject responseObject, byte[] buffer){
		
		if(CHDownLoadFilePathHandler.class.isInstance(mProcessDataHandler) && buffer != null){
			mCallBackHandler.onFileSuccess(responseObject, data, new String(buffer));
		}else{
			mCallBackHandler.onSuccess(responseObject, data, buffer);
		}
	}
	
	public void loadFormCache(Object data, ResponseObject responseObject)
	{
		if(data == null) return;
		this.loadFormCache(data, responseObject, null, null);
	}

	/**
	 * 设置文件缓存
	 * 
	 * @param fileCache
	 */
	public void setFileCache(CHFileCache fileCache)
	{
		this.mCHFileCache = fileCache;
//		if (this.mCHFileCache.hasNotInited()) {
//			new CacheAsyncTask().execute(MESSAGE_INIT_DISK_CACHE);
//		}
	}

	/**
	 * 获取缓存的回调对象
	 * 
	 * @return 如果没有设置，返回为null
	 */
	public CHCallBackHandler<ResponseObject> getCallBackHandler()
	{
		return mCallBackHandler;
	}

	public void setCallBackHandler(
			CHCallBackHandler<ResponseObject> callBackHandler)
	{
		this.mCallBackHandler = callBackHandler;
	}

	public void setProcessDataHandler(CHProcessDataHandler processDataHandler)
	{
		this.mProcessDataHandler = processDataHandler;
	}

	public CHProcessDataHandler getProcessDataHandler()
	{
		return mProcessDataHandler;
	}

	/**
	 * 是否退出以前的任务，如果为设置为true则退出以前的Task
	 * 
	 * @param exitTasksEarly
	 */
	public void setExitTasksEarly(boolean exitTasksEarly)
	{
		mExitTasksEarly = exitTasksEarly;
		setPauseWork(false);
	}

	/**
	 * 取消任何挂起的连接到提供给object工作。
	 * 
	 * @param responseObject
	 */
	public void cancelWork(ResponseObject responseObject)
	{

		CHCacheEntity<ResponseObject> cacheEntity;
		if (!mCacheEntityHashMap.containsKey(responseObject))
		{
//			cacheEntity = new CHCacheEntity<ResponseObject>();
//			cacheEntity.setT(responseObject);
//			mCacheEntityHashMap.put(string, cacheEntity);
		} else
		{
			cacheEntity = mCacheEntityHashMap.get(responseObject);
			final BufferWorkerTask bufferWorkerTask = getBufferWorkerTask(cacheEntity);
			if (bufferWorkerTask != null)
			{
				bufferWorkerTask.cancel(true);
			}
		}
	}

	public boolean cancelPotentialWork(Object data, CHCacheEntity<ResponseObject> cacheEntity)
	{
		final BufferWorkerTask responseWorkerTask = getBufferWorkerTask(cacheEntity);

		if (responseWorkerTask != null)
		{
			final Object bitmapData = responseWorkerTask.data;
			if (bitmapData == null || !bitmapData.equals(data))
			{
				responseWorkerTask.cancel(true);
			} else
			{
				// The same work is already in progress.
				return false;
			}
		}
		return true;
	}

	private BufferWorkerTask getBufferWorkerTask(CHCacheEntity<ResponseObject> cacheEntity)
	{
		if (cacheEntity != null)
		{
			final CHAsyncEntity asyncEntity = cacheEntity.getAsyncEntity();
			if (asyncEntity != null)
			{
				return asyncEntity.getBufferWorkerTask();
			}
		}
		return null;
	}

	public void setPauseWork(boolean pauseWork)
	{
		synchronized (mPauseWorkLock)
		{
			mPauseWork = pauseWork;
			if (!mPauseWork)
			{
				mPauseWorkLock.notifyAll();
			}
		}
	}

	protected void initDiskCacheInternal()
	{
		if (mCHFileCache != null)
		{
			mCHFileCache.initDiskCache();
		}
	}

	protected void clearCacheInternal()
	{
		if (mCHFileCache != null)
		{
			mCHFileCache.clearCache();
		}
	}
	
	protected void removeCacheFile(String key)
	{
		if (mCHFileCache != null)
		{
			mCHFileCache.removeCacheFile(key);
		}
	}

	protected void flushCacheInternal()
	{
		if (mCHFileCache != null)
		{
			mCHFileCache.flush();
		}
	}

	protected void closeCacheInternal()
	{
		if (mCHFileCache != null)
		{
			mCHFileCache.close();
			mCHFileCache = null;
		}
	}

	protected class CacheAsyncTask extends AsyncTask<Object, Void, Void>
	{
		public CacheAsyncTask()
		{
			// TODO Auto-generated constructor stub
		}

		@Override
		protected Void doInBackground(Object... params)
		{
			switch ((Integer) params[0])
			{
			case MESSAGE_CLEAR:
				clearCacheInternal();
				break;
			case MESSAGE_INIT_DISK_CACHE:
				initDiskCacheInternal();
				break;
			case MESSAGE_FLUSH:
				flushCacheInternal();
				break;
			case MESSAGE_CLOSE:
				closeCacheInternal();
				break;
			}
			return null;
		}
	}

	/**
	 * 位图的惟一标识符来存储清除内存和磁盘缓存都TAFileCache与此相关 对象。注意,这包括磁盘访问,所以这是不应当的 上执行的主要/ UI线程。
	 */
	public void clearCache()
	{
		new CacheAsyncTask().execute(MESSAGE_CLEAR);
	}

	/**
	 * 磁盘缓存初始化TAFileCache与此相关的对象。注意, 这包括磁盘访问,所以这应该不会被执行的主要/ UI 线程。
	 */
	public void initCache()
	{
		new CacheAsyncTask().execute(MESSAGE_INIT_DISK_CACHE);
	}

	/**
	 * 磁盘缓存刷新TAFileCache与此相关的对象。注意, 这包括磁盘访问,所以这应该不会被执行的主要/ UI 线程。
	 */
	public void flushCache()
	{
		new CacheAsyncTask().execute(MESSAGE_FLUSH);
	}

	/**
	 * 关闭磁盘缓存与此相关TAFileCache对象。注意,这包括磁盘访问,所以这应该不会被执行的主要/ UI线程。
	 */
	public void closeCache()
	{
		new CacheAsyncTask().execute(MESSAGE_CLOSE);
	}

	//TODO:内存泄露
	public class BufferWorkerTask extends AsyncTask<Object, Void, byte[]>
	{
		private Object data;

		private final WeakReference<CHCacheEntity<ResponseObject>> cacheEntityReference;

		public BufferWorkerTask(CHCacheEntity<ResponseObject> cacheEntity)
		{
			this.cacheEntityReference = new WeakReference<CHCacheEntity<ResponseObject>>(
					cacheEntity);
		}

		/**
		 * Background processing.
		 */
		@Override
		protected byte[] doInBackground(Object... params)
		{
			/*
			 * if (BuildConfig.DEBUG) { Log.d(TAG,
			 * "doInBackground - starting work"); }
			 */
			data = params[0];
			
			final String dataString = String.valueOf(data);
			byte[] buffer = null;

			// Wait here if work is paused and the task is not cancelled
			if (mPauseWork && !isCancelled())
			{
				synchronized (mPauseWorkLock)
				{
					while (mPauseWork && !isCancelled())
					{
						try
						{
							mPauseWorkLock.wait();
						} catch (InterruptedException e)
						{
							//任务已取消
//							Thread.currentThread().interrupt();
//							return null;
						}
					}
				}
			}
			if (mCHFileCache != null && !isCancelled()
					&& getAttachedCacheEntity() != null && !mExitTasksEarly)
			{
				buffer = mCHFileCache
						.getBufferFromDiskCache(dataString);
			}
			if (buffer == null && !isCancelled()
					&& getAttachedCacheEntity() != null && !mExitTasksEarly)
			{
				if (mProcessDataHandler != null)
				{
					final Object localUrl = params[1];
					final Object downType = params[2];

					final Map<String, String> map = new HashMap<String,String>();
					if(localUrl != null)map.put("LocalUrl", localUrl.toString());
					CHLogger.d(this, "-------- localUrl " + localUrl);
					if(downType != null)map.put("DownType", downType.toString());
					buffer = mProcessDataHandler.processData(dataString, map);
				}
			}
			//FIXME:检查磁盘缓存是否添加了两次
			if (buffer != null && mCHFileCache != null)
			{
				mCHFileCache.addBufferToCache(dataString, buffer);
			}
			return buffer;
		}

		/**
		 * Once the image is processed, associates it to the imageView
		 */
		@Override
		protected void onPostExecute(byte[] buffer)
		{
			// if cancel was called on this task or the "exit early" flag is set
			// then we're done
			if (isCancelled() || mExitTasksEarly)
			{
				buffer = null;
			}
			final CHCacheEntity<ResponseObject> cacheEntity = getAttachedCacheEntity();
			if (mCallBackHandler != null && cacheEntity != null)
			{
				callBackSucess(data, (ResponseObject) cacheEntity.getT(), buffer);
			}

		}

		private CHCacheEntity<ResponseObject> getAttachedCacheEntity()
		{
			final CHCacheEntity<ResponseObject> cacheEntity = cacheEntityReference.get();
			final BufferWorkerTask bufferWorkerTask = getBufferWorkerTask(cacheEntity);

			if (this == bufferWorkerTask)
			{
				return cacheEntity;
			}

			return null;
		}

		@Override
		protected void onCancelled(byte[] inputStream)
		{
			super.onCancelled(inputStream);
			synchronized (mPauseWorkLock)
			{
				mPauseWorkLock.notifyAll();
			}
		}
	}
}
