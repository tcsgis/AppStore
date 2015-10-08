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
package com.changhong.util.bitmap;

import android.content.Context;
import android.content.res.Resources;
import android.widget.ImageView;

import com.changhong.util.cache.CHFileCache;
import com.changhong.util.cache.CHFileCache.CHCacheParams;
import com.changhong.util.cache.CHFileCacheWork;

public class CHBitmapCacheWork extends CHFileCacheWork<ImageView>
{

	protected Resources mResources;
	private CHCacheParams mCacheParams;
	private static final String TAG = "CHBitmapCacheWork";
	private Context mContext;

	public CHBitmapCacheWork(Context context)
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
			CHDownloadBitmapHandler downloadBitmapFetcher = new CHDownloadBitmapHandler(
					mContext, 100, 100);
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
		mCacheParams = cacheParams;
		setFileCache(new CHFileCache(cacheParams));
	}

	@Override
	protected void initDiskCacheInternal()
	{
		// TODO Auto-generated method stub
		CHDownloadBitmapHandler downloadBitmapFetcher = (CHDownloadBitmapHandler) getProcessDataHandler();
		super.initDiskCacheInternal();
		if (downloadBitmapFetcher != null)
		{
			downloadBitmapFetcher.initDiskCacheInternal();
		}
	}

	protected void clearCacheInternal()
	{
		super.clearCacheInternal();
		CHDownloadBitmapHandler downloadBitmapFetcher = (CHDownloadBitmapHandler) getProcessDataHandler();
		if (downloadBitmapFetcher != null)
		{
			downloadBitmapFetcher.clearCacheInternal();
		}
	}

	protected void flushCacheInternal()
	{
		super.flushCacheInternal();
		CHDownloadBitmapHandler downloadBitmapFetcher = (CHDownloadBitmapHandler) getProcessDataHandler();
		if (downloadBitmapFetcher != null)
		{
			downloadBitmapFetcher.flushCacheInternal();
		}
	}

	protected void closeCacheInternal()
	{
		super.closeCacheInternal();
		CHDownloadBitmapHandler downloadBitmapFetcher = (CHDownloadBitmapHandler) getProcessDataHandler();
		if (downloadBitmapFetcher != null)
		{
			downloadBitmapFetcher.closeCacheInternal();
		}
	}
}
