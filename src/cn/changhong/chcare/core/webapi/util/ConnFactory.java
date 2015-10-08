package cn.changhong.chcare.core.webapi.util;

import android.content.Context;
import android.os.Build;

//import com.squareup.okhttp.OkHttpClient;
//import com.squareup.okhttp.OkUrlFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by SilverFox on 2014/12/1.
 */
public abstract class ConnFactory {
	private static ConnFactory sInstance;

	private static String defaultUserAgent() {
		String agent = System.getProperty("http.agent");
		return agent != null ? agent : ("Java" + System.getProperty("java.version"));
	}

	private static Boolean sUseOkHttp;
	public static boolean useOkHttp() {
		if (sUseOkHttp == null) {
			/*boolean useOkhttp;
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
				useOkhttp = false;
			} else {
				try {
					//检查默认UA是否能编码
					String defUA = defaultUserAgent();
					useOkhttp = new String(defUA.getBytes("ISO-8859-1"), "ISO-8859-1").equals(defUA);
				} catch (Exception ignored){
					useOkhttp = false;
				}
			}
			sUseOkHttp = useOkhttp;*/
			sUseOkHttp = false;
		}
		return sUseOkHttp;
	}

	public static void Init(Context context){
		if (sInstance == null) {
			synchronized (ConnFactory.class) {
				if (sInstance == null) {
//					sInstance = useOkHttp() ?
//							new OkConnFactory(context) : new OldConnFactory();
					sInstance = new OldConnFactory();
				}
			}
		}
	}

	public static ConnFactory getInstance(){
		if (sInstance == null) {
			throw new IllegalStateException("Must Init first!!!");
		}
		return sInstance;
	}

	static {
		HttpURLConnection.setFollowRedirects(true);
	}

	public abstract URLConnection open(URL url) throws IOException;
	public abstract void reset(boolean force) throws UnsupportedOperationException;

	/*public static class OkConnFactory extends ConnFactory {
		private Context mContext;

		public OkUrlFactory mConnFactory;
		public OkConnFactory(Context context){
			this.mContext = context;

			IntentFilter filter = new IntentFilter();
			filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
			mContext.registerReceiver(new BroadcastReceiver() {
				@Override
				public void onReceive(Context context, Intent intent)
				{
					reset(false);
				}
			}, filter);
			reset(true);
		}

		@Override
		public URLConnection open(URL url) {
			final URLConnection conn = mConnFactory.open(url);
			//TODO: 在网络发生变化时重置Http池
			return conn;
		}

		@Override
		public void reset(boolean force) throws UnsupportedOperationException {
			if (force) {
				OkHttpClient client = new OkHttpClient();
				//加入HTTP缓存
				File root = CHExternalOverFroyoUtils.getDiskCacheDir(mContext, "httpCache");
				if (!root.exists())
					root.mkdirs();
				MyCache cache = new MyCache(root, (long) 16 * 1024 * 1024);
				cache.useFor(client);
				mConnFactory = new OkUrlFactory(client);
			} else {
				//copyWithDefaults不可见
				ConnectionPool pool = mConnFactory.client().getConnectionPool();
				if (pool == null)
					pool = ConnectionPool.getDefault();
				if (pool != null)
					pool.evictAll();
			}
		}
	}*/

	public static class OldConnFactory extends ConnFactory {

		public OldConnFactory(){
			disableConnectionReuseIfNecessary();
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

		@Override
		public URLConnection open(URL url) throws IOException {
			return url.openConnection();
		}

		@Override
		public void reset(boolean force) throws UnsupportedOperationException{
			throw new UnsupportedOperationException();
		}
	}
}
