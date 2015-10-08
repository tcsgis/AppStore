package com.changhong.util.bitmap;

import android.content.Context;
import android.content.res.Resources;
import android.widget.ImageView;

import com.changhong.util.cache.CHFileCache;
import com.changhong.util.cache.CHFileCache.CHCacheParams;
import com.changhong.util.cache.CHFileCacheWork;

public class CHFamilyCacheWork extends CHFileCacheWork<ImageView>{

	protected Resources mResources;
	private static final String TAG = "CHFamilyCacheWork";
	private Context mContext;

	public CHFamilyCacheWork(Context context)
	{
		mResources = context.getResources();
		this.mContext = context;
	}

	@Override
	public void loadFormCache(Object data, ImageView responseObject, String locUrl, String downType)
	{
		// TODO Auto-generated method stub
		if (getCallBackHandler() == null)
		{
			CHBitmapCallBackHanlder callBackHanlder = new CHBitmapCallBackHanlder();
			setCallBackHandler(callBackHanlder);
		}
		if (getProcessDataHandler() == null)
		{
			CHDownLoadFilePathHandler downloadBitmapFetcher = new CHDownLoadFilePathHandler(mContext);
			setProcessDataHandler(downloadBitmapFetcher);
		}
		super.loadFormCache(data, responseObject, locUrl, downType);
	}

	/**
	 * 设置图片缓存
	 * 
	 * @param cacheParams
	 *            响应参数
	 */
	public void setBitmapCache(CHCacheParams cacheParams)
	{
		setFileCache(new CHFileCache(cacheParams));
	}

	@Override
	protected void initDiskCacheInternal()
	{
		// TODO Auto-generated method stub
		CHDownLoadFilePathHandler downloadBitmapFetcher = (CHDownLoadFilePathHandler) getProcessDataHandler();
		super.initDiskCacheInternal();
		if (downloadBitmapFetcher != null)
		{
			downloadBitmapFetcher.initDiskCacheInternal();
		}
	}

	public void removeCacheFile(String key)
	{
		super.removeCacheFile(key);
		CHDownLoadFilePathHandler downloadBitmapFetcher = (CHDownLoadFilePathHandler) getProcessDataHandler();
		if (downloadBitmapFetcher != null)
		{
			downloadBitmapFetcher.removeCacheFile(key);
		}
	}
	
	protected void clearCacheInternal()
	{
		super.clearCacheInternal();
		CHDownLoadFilePathHandler downloadBitmapFetcher = (CHDownLoadFilePathHandler) getProcessDataHandler();
		if (downloadBitmapFetcher != null)
		{
			downloadBitmapFetcher.clearCacheInternal();
		}
	}

	protected void flushCacheInternal()
	{
		super.flushCacheInternal();
		CHDownLoadFilePathHandler downloadBitmapFetcher = (CHDownLoadFilePathHandler) getProcessDataHandler();
		if (downloadBitmapFetcher != null)
		{
			downloadBitmapFetcher.flushCacheInternal();
		}
	}

	protected void closeCacheInternal()
	{
		super.closeCacheInternal();
		CHDownLoadFilePathHandler downloadBitmapFetcher = (CHDownLoadFilePathHandler) getProcessDataHandler();
		if (downloadBitmapFetcher != null)
		{
			downloadBitmapFetcher.closeCacheInternal();
		}
	}
}
