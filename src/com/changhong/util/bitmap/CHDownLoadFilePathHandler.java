package com.changhong.util.bitmap;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import android.content.Context;
import android.os.Build;
import com.changhong.common.AndroidVersionCheckUtils;
import com.changhong.util.CHLogger;
import com.changhong.util.cache.CHExternalOverFroyoUtils;
import com.changhong.util.cache.CHExternalUnderFroyoUtils;
import com.changhong.util.cache.DiskLruFamilyCache;
import com.changhong.util.config.MyProperties;

public class CHDownLoadFilePathHandler extends CHProcessFilePathHandler
{
	private static final String TAG = "CHDownloadFamilyHandler";
	private static final int HTTP_CACHE_SIZE = 20 * 1024 * 1024; // 10MB
	private static final String HTTP_CACHE_DIR = "familyphoto";
	private static final int IO_BUFFER_SIZE = 8 * 1024;
	private DiskLruFamilyCache mFamilyDiskCache;
	private File mFamilyCacheDir;
	private boolean mFamilyDiskCacheStarting = true;
	private final Object mFamilyDiskCacheLock = new Object();
	private static final int DISK_CACHE_INDEX = 0;
	private int version = 1;

	private final String BASE_URL = MyProperties.getMyProperties().getServerIp();

	static {
		disableConnectionReuseIfNecessary();
	}
	
	public CHDownLoadFilePathHandler(Context context)
	{
		init(context);
	}

	private void init(Context context)
	{
		String path = HTTP_CACHE_DIR+File.separator;
		path += version;

		if (AndroidVersionCheckUtils.hasGingerbread())
		{
			mFamilyCacheDir = CHExternalOverFroyoUtils.getDiskCacheDir(context,
					path);
		} else
		{
			mFamilyCacheDir = CHExternalUnderFroyoUtils.getDiskCacheDir(context,
					path);
		}
		initDiskCacheInternal();
	}

	protected void initDiskCacheInternal()
	{
		initFamilyDiskCache();
	}

	private void initFamilyDiskCache()
	{
		if (!mFamilyCacheDir.exists())
		{
			mFamilyCacheDir.mkdirs();
		}
		synchronized (mFamilyDiskCacheLock)
		{
			long usableSpace = 0;
			if (AndroidVersionCheckUtils.hasGingerbread())
			{
				usableSpace = CHExternalOverFroyoUtils
						.getUsableSpace(mFamilyCacheDir);
			} else
			{
				usableSpace = CHExternalUnderFroyoUtils
						.getUsableSpace(mFamilyCacheDir);
			}
			if (usableSpace > HTTP_CACHE_SIZE)
			{
				try
				{
					mFamilyDiskCache = DiskLruFamilyCache.open(mFamilyCacheDir, version, 1,
							HTTP_CACHE_SIZE);
				} catch (IOException e)
				{
					CHLogger.e(TAG, e.getMessage());
					mFamilyDiskCache = null;
				}
			}
			mFamilyDiskCacheStarting = false;
			mFamilyDiskCacheLock.notifyAll();
		}
	}

	protected void clearCacheInternal()
	{
		synchronized (mFamilyDiskCacheLock)
		{
			if (mFamilyDiskCache != null && !mFamilyDiskCache.isClosed())
			{
				try
				{
					mFamilyDiskCache.delete();

				} catch (IOException e)
				{
					CHLogger.e(TAG, "clearCacheInternal - " + e);
				}
				mFamilyDiskCache = null;
				mFamilyDiskCacheStarting = true;
				initFamilyDiskCache();
			}
		}
	}

	public void removeCacheFile(String url)
	{
		synchronized (mFamilyDiskCacheLock)
		{
			if (mFamilyDiskCache != null)
			{
				try
				{
					String key = getKey(url);
					mFamilyDiskCache.remove(key);

				} catch (IOException e)
				{
					CHLogger.e(TAG, "removeCacheFile - " + e);
				}
			}
		}
	}

	protected void flushCacheInternal()
	{
		synchronized (mFamilyDiskCacheLock)
		{
			if (mFamilyDiskCache != null)
			{
				try
				{
					mFamilyDiskCache.flush();

				} catch (IOException e)
				{
					CHLogger.e(TAG, "flush - " + e);
				}
			}
		}
	}

	protected void closeCacheInternal()
	{
		synchronized (mFamilyDiskCacheLock)
		{
			if (mFamilyDiskCache != null)
			{
				try
				{
					if (!mFamilyDiskCache.isClosed())
					{
						mFamilyDiskCache.close();
						mFamilyDiskCache = null;
					}
				} catch (IOException e)
				{
					CHLogger.e(TAG, "closeCacheInternal - " + e);
				}
			}
		}
	}

	private String doDownFile(String data, Map<String,String> params)
	{
		String urlData = data;
		String locUrl = null ,downType = null;
		
		if(params != null){
			locUrl = params.get("LocalUrl");
			downType = params.get("DownType");
		}
		
		String key = getKey(urlData);
		String filePath = null;
		DiskLruFamilyCache.Snapshot snapshot;
		if (mFamilyDiskCacheStarting) {
			synchronized (mFamilyDiskCacheLock)
			{
				// Wait for disk cache to initialize
				while (mFamilyDiskCacheStarting)
				{
					try
					{
						mFamilyDiskCacheLock.wait();
					} catch (InterruptedException e)
					{
					}
				}
			}
		}

		if (mFamilyDiskCache != null)
		{
			try
			{
				snapshot = mFamilyDiskCache.get(key);
				if (snapshot == null)
				{
					//不存在已知缓存
					final Object lock = mFamilyDiskCache.getLock(key);
					synchronized (lock) {
						snapshot = mFamilyDiskCache.get(key);
						if (snapshot == null) {
							DiskLruFamilyCache.Editor editor = mFamilyDiskCache.edit(key);
							if (editor != null)
							{
								boolean isDownOk = false;

								try {
									if("HttpUrl".equals(downType)){
										isDownOk = downloadUrlToStream(urlData, locUrl,editor.newOutputStream(DISK_CACHE_INDEX));
									}else{
										isDownOk = doApiDownFile(urlData, editor.newOutputStream(DISK_CACHE_INDEX));
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
							snapshot = mFamilyDiskCache.get(key);
						}
					}
				}
				if (snapshot != null)
				{
					filePath = snapshot.getFilePath(DISK_CACHE_INDEX);
				}
			} catch (IOException e)
			{
				CHLogger.e(TAG, "processBitmap - " + e);
			} catch (IllegalStateException e)
			{
				CHLogger.e(TAG, "processBitmap - " + e);
			}
		}
		
		return filePath;
	}

	@Override
	protected String processDownFile(Object url, Map<String,String> params)
	{
		return doDownFile(String.valueOf(url), params);
	}

	/**cxp图片下载处理
	 * Download a bitmap from a URL and write the content to an output stream.
	 * 
	 * @param urlString
	 *            The URL to fetch
	 * @return true if successful, false otherwise
	 */

	public boolean downloadUrlToStream(String urlString,
			String locUrl, OutputStream outputStream)
	{
		boolean isDownOk = false;
		//disableConnectionReuseIfNecessary();
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

	
	/**
	 * 通过api接口下载文件
	 * @param urlString
	 * @return
	 */
	private boolean doApiDownFile(String urlString, OutputStream outputStream){

		boolean isDownOk = false;
		//disableConnectionReuseIfNecessary();
		BufferedOutputStream out = null;
		try {
			
			out = new BufferedOutputStream(outputStream, IO_BUFFER_SIZE);
			
		} catch (Exception e) {
			// TODO: handle exception
			CHLogger.e(TAG, "Error in doApiDownFile - " + e);
			e.printStackTrace();
		}finally
		{
			if (out != null)
			{
				try
				{
					out.close();
				} catch (final IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		
		return isDownOk;
	}
	/**
	 * http下载
	 * @param photoUrl
	 * @return
	 */
	private HttpURLConnection doConnectResponse(URL photoUrl){
		HttpURLConnection con = null;

		return null;

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

	private String getKey(String urlData){

		String key = "";
		if (AndroidVersionCheckUtils.hasGingerbread())
		{
			key = CHExternalOverFroyoUtils.hashKeyForDisk(urlData);
		} else
		{
			key = CHExternalUnderFroyoUtils.hashKeyForDisk(urlData);
		}

		return key;
	}
	
	public String getDirPath(){
		return mFamilyCacheDir.getPath();
	}

}
