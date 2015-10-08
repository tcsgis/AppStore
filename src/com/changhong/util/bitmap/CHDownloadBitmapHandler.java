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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import cn.changhong.chcare.core.webapi.server.CHCareWebApiProvider;
import cn.changhong.chcare.core.webapi.server.CHCareWebApiProvider.WebApiServerType;
import cn.changhong.chcare.core.webapi.server.IAccountService;

import com.changhong.common.AndroidVersionCheckUtils;
import com.changhong.util.CHLogger;
import com.changhong.util.cache.CHExternalOverFroyoUtils;
import com.changhong.util.cache.CHExternalUnderFroyoUtils;
import com.changhong.util.cache.DiskLruCache;
import com.changhong.util.config.MyProperties;
import cn.changhong.chcare.core.webapi.util.ConnFactory;

public class CHDownloadBitmapHandler extends CHResizerBitmapHandler
{
	private static final String TAG = "TABitmapFetcher";
	private static final int ADD_SIZE = 10; //因为头像不清楚所以在这里加10个像素
	private static final int HTTP_CACHE_SIZE = 10 * 1024 * 1024; // 10MB
	private static final String HTTP_CACHE_DIR = "http";
	private static final int IO_BUFFER_SIZE = 8 * 1024;
	private DiskLruCache mHttpDiskCache;
	private File mHttpCacheDir;
	private boolean mHttpDiskCacheStarting = true;
	private final Object mHttpDiskCacheLock = new Object();
	private static final int DISK_CACHE_INDEX = 0;

	private final String BASE_URL = MyProperties.getMyProperties().getServerIp();

	static {
		disableConnectionReuseIfNecessary();
	}

	/**
	 * 初始化一个目标提供图像的宽度和高度的来处理图像
	 * 
	 * @param context
	 * @param imageWidth
	 * @param imageHeight
	 */
	public CHDownloadBitmapHandler(Context context, int imageWidth,
			int imageHeight)
	{
		super(context, imageWidth + ADD_SIZE, imageHeight + ADD_SIZE);
		init(context);
	}

	/**
	 * Initialize providing a single target image size (used for both width and
	 * height);
	 * 
	 * @param context
	 * @param imageSize
	 */
	public CHDownloadBitmapHandler(Context context, int imageSize)
	{
		super(context, imageSize + ADD_SIZE);
		init(context);
	}

	private void init(Context context)
	{
		if (AndroidVersionCheckUtils.hasGingerbread())
		{
			mHttpCacheDir = CHExternalOverFroyoUtils.getDiskCacheDir(context,
					HTTP_CACHE_DIR);
		} else
		{
			mHttpCacheDir = CHExternalUnderFroyoUtils.getDiskCacheDir(context,
					HTTP_CACHE_DIR);
		}
		initDiskCacheInternal();
	}

	protected void initDiskCacheInternal()
	{
		initHttpDiskCache();
	}

	private void initHttpDiskCache()
	{
		try {
			if (!mHttpCacheDir.exists())
			{
				CHLogger.i(TAG, mHttpCacheDir.getPath());
				mHttpCacheDir.mkdirs();
			}
		} catch (Exception e) {
			// TODO: handle exception
			CHLogger.e(TAG, e.getMessage());
		}
		
		synchronized (mHttpDiskCacheLock)
		{
			long usableSpace = 0;
			if (AndroidVersionCheckUtils.hasGingerbread())
			{
				usableSpace = CHExternalOverFroyoUtils
						.getUsableSpace(mHttpCacheDir);
			} else
			{
				usableSpace = CHExternalUnderFroyoUtils
						.getUsableSpace(mHttpCacheDir);
			}
			if (usableSpace > HTTP_CACHE_SIZE)
			{
				try
				{
					mHttpDiskCache = DiskLruCache.open(mHttpCacheDir, 1, 1,
							HTTP_CACHE_SIZE);
				} catch (IOException e)
				{
					mHttpDiskCache = null;
				}
			}
			mHttpDiskCacheStarting = false;
			mHttpDiskCacheLock.notifyAll();
		}
	}

	protected void clearCacheInternal()
	{
		synchronized (mHttpDiskCacheLock)
		{
			if (mHttpDiskCache != null && !mHttpDiskCache.isClosed())
			{
				try
				{
					mHttpDiskCache.delete();

				} catch (IOException e)
				{
					CHLogger.e(TAG, "clearCacheInternal - " + e);
				}
				mHttpDiskCache = null;
				mHttpDiskCacheStarting = true;
				initHttpDiskCache();
			}
		}
	}

	protected void flushCacheInternal()
	{
		synchronized (mHttpDiskCacheLock)
		{
			if (mHttpDiskCache != null)
			{
				try
				{
					mHttpDiskCache.flush();

				} catch (IOException e)
				{
					CHLogger.e(TAG, "flush - " + e);
				}
			}
		}
	}

	protected void closeCacheInternal()
	{
		synchronized (mHttpDiskCacheLock)
		{
			if (mHttpDiskCache != null)
			{
				try
				{
					if (!mHttpDiskCache.isClosed())
					{
						mHttpDiskCache.close();
						mHttpDiskCache = null;
					}
				} catch (IOException e)
				{
					CHLogger.e(TAG, "closeCacheInternal - " + e);
				}
			}
		}
	}

	private Bitmap processBitmap(String data, String locUrl,String downType)
	{
		String key = getKey(data);
		FileDescriptor fileDescriptor = null;
		FileInputStream fileInputStream = null;
		DiskLruCache.Snapshot snapshot;
		if (mHttpDiskCacheStarting) {
			synchronized (mHttpDiskCacheLock)
			{
				// Wait for disk cache to initialize
				while (mHttpDiskCacheStarting)
				{
					try
					{
						mHttpDiskCacheLock.wait();
					} catch (InterruptedException e)
					{
					}
				}
			}
		}

		if (mHttpDiskCache != null)
		{
			try
			{
				snapshot = mHttpDiskCache.get(key);
				if (snapshot == null) {
					//不存在已知缓存
					final Object lock = mHttpDiskCache.getLock(key);
					synchronized (lock) {
						snapshot = mHttpDiskCache.get(key);
						if (snapshot == null) {
							DiskLruCache.Editor editor = mHttpDiskCache.edit(key);

							//编辑中返回null
							if (editor != null)
							{
								boolean isDownOk = false;
								try {
									if("HttpUrl".equals(downType)){
										isDownOk = downloadUrlToStream(data, locUrl,editor.newOutputStream(DISK_CACHE_INDEX));
									}else{
										isDownOk = doApiDownFile(data, locUrl, editor.newOutputStream(DISK_CACHE_INDEX));
									}
								} finally {
									if (isDownOk)
									{
										editor.commit();
									} else
									{
										editor.abort();
									}
								}
							}
							snapshot = mHttpDiskCache.get(key);
						}
					}
				}
				if (snapshot != null)
				{
					fileInputStream = (FileInputStream) snapshot
							.getInputStream(DISK_CACHE_INDEX);
					fileDescriptor = fileInputStream.getFD();
				}
			} catch (IOException e)
			{
				CHLogger.e(TAG, "processBitmap - " + e);
			} catch (IllegalStateException e)
			{
				CHLogger.e(TAG, "processBitmap - " + e);
			} finally
			{
				if (fileDescriptor == null && fileInputStream != null)
				{
					try
					{
						fileInputStream.close();
					} catch (IOException e)
					{
					}
				}
			}
		}

		Bitmap bitmap = null;
		if (fileDescriptor != null)
		{
			bitmap = decodeSampledBitmapFromDescriptor(fileDescriptor,
					mImageWidth, mImageHeight);
			if (fileInputStream != null)
			{
					//fileDescriptor == null的情况已在finally中close
				try
				{
					fileInputStream.close();
				} catch (IOException e)
				{
				}
			}
		}
		return bitmap;
	}

	@Override
	protected Bitmap processBitmap(Object data, Map<String,String> params)
	{
		String locUrl = null ,downType = null;
		
		if(params != null){
			locUrl = params.get("LocalUrl");
			downType = params.get("DownType");
		}
		
		return processBitmap(String.valueOf(data), locUrl, downType);
	}

	/**
	 * http请求下载
	 * @param urlString
	 * @param locUrl
	 * @param outputStream
	 * @return
	 */
	public boolean downloadUrlToStream(String urlString,
			String locUrl, OutputStream outputStream)
	{
		boolean isDownOk = false;
		disableConnectionReuseIfNecessary();
		HttpURLConnection con = null;
		BufferedOutputStream out = null;
		BufferedInputStream in = null;

		try
		{
			out = new BufferedOutputStream(outputStream, IO_BUFFER_SIZE);

			if(locUrl == null){

				urlString = BASE_URL+urlString;
				URL photoUrl = new URL(urlString);
				
				con = doConnectResponse(photoUrl);
				if (con != null) {
					if(con.getResponseCode() == HttpURLConnection.HTTP_OK){
						in = new BufferedInputStream(con.getInputStream());
					}else if(con.getResponseCode() == HttpURLConnection.HTTP_NO_CONTENT){

						int lastParam = urlString.lastIndexOf("&");
						urlString = urlString.substring(0, lastParam);
						photoUrl = new URL(urlString);
						con = doConnectResponse(photoUrl);
						if (con != null && con.getResponseCode() == HttpURLConnection.HTTP_OK) {
							in = new BufferedInputStream(con.getInputStream());
						}
					}
				}

				if (in != null) {
					byte[] b = new byte[1024];
					int length;
					while ((length = in.read(b)) != -1) {
						out.write(b, 0, length);
					}
					isDownOk = true;
				}
			}else{

				File locFile = new File(locUrl);
				if(locFile.exists()){

					in = new BufferedInputStream(new FileInputStream(locFile), IO_BUFFER_SIZE);

					byte[] b = new byte[1024];
					int length;
					while ((length = in.read(b)) != -1) {
						out.write(b, 0, length);
					}
					isDownOk = true;
				}
			}

			
		} catch (final IOException e)
		{
			CHLogger.e(TAG, "Error in downloadBitmap - " + e);
		} finally
		{
			if (con != null)
			{
				con.disconnect();
			}
			if (out != null)
			{
				try
				{
					out.close();
				} catch (final IOException e)
				{
				}
			}
			if (in != null)
			{
				try
				{
					in.close();
				} catch (final IOException e)
				{
				}
			}
		}
		return isDownOk;
	}
	
	/**cxp图片下载处理
	 * Download a bitmap from a URL and write the content to an output stream.
	 * 
	 * @param urlString
	 *            The URL to fetch
	 * @return true if successful, false otherwise
	 */
	public boolean doApiDownFile(String urlString,
			String locUrl, OutputStream outputStream)
	{
		if(urlString == null || urlString.length() == 0){
			CHLogger.w(this, "url: "+urlString);
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
				}
			}
			return false;
		}

//		disableConnectionReuseIfNecessary();
		BufferedOutputStream out = null;
		BufferedInputStream in = null;

		try
		{
			out = new BufferedOutputStream(outputStream, IO_BUFFER_SIZE);
			
			if(locUrl == null){

				IAccountService mAccountService = (IAccountService) CHCareWebApiProvider.Self
						.defaultInstance().getDefaultWebApiService(
								WebApiServerType.CHCARE_ACCOUNT_SERVER);
				
				boolean isDownOk = mAccountService.downloadFile(urlString, out);
				return isDownOk;
			}else{

				File locFile = new File(locUrl);
				if(locFile.exists()){
					in = new BufferedInputStream(new FileInputStream(locFile), IO_BUFFER_SIZE);

					byte[] b = new byte[1024];
					int length;
					while ((length = in.read(b)) != -1) {
						out.write(b, 0, length);
					}
					return true;
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			CHLogger.e(TAG, "Error in downloadBitmap"+urlString+" --- " + e);
		} finally
		{
				if (out != null)
				{
					try {
						out.close();
					} catch (IOException e) {
					}
				}
				if (in != null)
				{
					try {
						in.close();
					} catch (IOException e) {
					}
				}
		}
		return false;
	}

	/**
	 * http下载
	 * @param photoUrl
	 * @return
	 */
	private HttpURLConnection doConnectResponse(URL photoUrl){
		HttpURLConnection con = null;

		try {
			con = (HttpURLConnection) ConnFactory.getInstance().open(photoUrl);
			con.setConnectTimeout(5 * 1000);
			con.setReadTimeout(15 * 1000);
			
			if(con.getResponseCode() == HttpURLConnection.HTTP_OK ||
				con.getResponseCode() == HttpURLConnection.HTTP_NO_CONTENT){
				return con;
			}else{
				con.disconnect();
			}
		} catch (final IOException e)
		{
			CHLogger.e(TAG, "Error in doConnectResponse - " + e);
		}
		return null;

	}

	/**
	 * 获取本地缓存文件夹路径
	 * @return 绝对路径
	 */

	public String getFileFloderPath() {
		return mHttpCacheDir.getPath();
	}

	/**
	 * Workaround for bug pre-Froyo, see here for more info:
	 * http://android-developers.blogspot.com/2011/09/androids-http-clients.html
	 */
	public static void disableConnectionReuseIfNecessary()
	{
		// HTTP connection reuse which was buggy pre-froyo
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO)
		{
			System.setProperty("http.keepAlive", "false");
		}
	}

	private String getKey(String data){

		String key;
		if (AndroidVersionCheckUtils.hasGingerbread())
		{
			key = CHExternalOverFroyoUtils.hashKeyForDisk(data);
		} else
		{
			key = CHExternalUnderFroyoUtils.hashKeyForDisk(data);
		}

		return key;
	}

}
