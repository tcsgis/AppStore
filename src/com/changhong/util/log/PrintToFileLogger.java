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
package com.changhong.util.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.changhong.CHApplication;
import com.changhong.common.AndroidVersionCheckUtils;
import com.changhong.util.cache.CHExternalOverFroyoUtils;
import com.changhong.util.cache.CHExternalUnderFroyoUtils;

/**
 * @Title PrintToFileLogger
 * @Package com.changhong.core.util.log
 * @Description PrintToFileLogger是TA框架中打印到sdcard上面的日志类
 * @version V1.0
 */
public class PrintToFileLogger implements ILogger
{

	public static final int VERBOSE = 2;

	public static final int DEBUG = 3;

	public static final int INFO = 4;

	public static final int WARN = 5;

	public static final int ERROR = 6;

	public static final int ASSERT = 7;
	private String mPath;
	private Writer mWriter;

	private static final SimpleDateFormat TIMESTAMP_FMT = new SimpleDateFormat(
			"[yyyy-MM-dd HH:mm:ss] ", Locale.US);
	private String basePath = "";
	private static final String LOG_DIR = "log";
	private static final String BASE_FILENAME = "ta.log";
	private File logDir;

	public PrintToFileLogger()
	{

	}

	public void open() throws IOException
	{
		if (AndroidVersionCheckUtils.hasFroyo())
		{
			logDir = CHExternalOverFroyoUtils.getDiskCacheDir(CHApplication
					.getApplication().getApplicationContext(), LOG_DIR);
		} else
		{
			logDir = CHExternalUnderFroyoUtils.getDiskCacheDir(CHApplication
					.getApplication().getApplicationContext(), LOG_DIR);
		}
		if (!logDir.exists())
		{
			logDir.mkdirs();
			// do not allow media scan
			try
			{
				new File(logDir, ".nomedia").createNewFile();
			} catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		basePath = logDir.getAbsolutePath() + "/"+ getCurrentTimeString() + BASE_FILENAME;
		try
		{
			File file = new File(basePath);
			if (!file.exists())
			{
				file.createNewFile();
			}
			mPath = file.getAbsolutePath();
			mWriter = new BufferedWriter(new FileWriter(file), 2048);
		} finally {
			if (mWriter == null)
				mPath = null;
		}

	}

	private String getCurrentTimeString()
	{
		Date now = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyyMMddHHmmss", Locale.US);
		return simpleDateFormat.format(now);
	}

	public String getPath()
	{
		return mPath;
	}

	@Override
	public void d(String tag, String message)
	{
		// TODO Auto-generated method stub
		println(DEBUG, tag, message);
	}

	@Override
	public void e(String tag, String message)
	{
		println(ERROR, tag, message);
	}

	@Override
	public void i(String tag, String message)
	{
		println(INFO, tag, message);
	}

	@Override
	public void v(String tag, String message)
	{
		println(VERBOSE, tag, message);
	}

	@Override
	public void w(String tag, String message)
	{
		println(WARN, tag, message);
	}

	@Override
	public void println(int priority, String tag, String message)
	{
		String printMessage = "";
		switch (priority)
		{
		case VERBOSE:
			printMessage = "[V]|"
					+ tag
					+ "|"
					+ CHApplication.getApplication().getApplicationContext()
							.getPackageName() + "|" + message;
			break;
		case DEBUG:
			printMessage = "[D]|"
					+ tag
					+ "|"
					+ CHApplication.getApplication().getApplicationContext()
							.getPackageName() + "|" + message;
			break;
		case INFO:
			printMessage = "[I]|"
					+ tag
					+ "|"
					+ CHApplication.getApplication().getApplicationContext()
							.getPackageName() + "|" + message;
			break;
		case WARN:
			printMessage = "[W]|"
					+ tag
					+ "|"
					+ CHApplication.getApplication().getApplicationContext()
							.getPackageName() + "|" + message;
			break;
		case ERROR:
			printMessage = "[E]|"
					+ tag
					+ "|"
					+ CHApplication.getApplication().getApplicationContext()
							.getPackageName() + "|" + message;
			break;
		default:

			break;
		}
		println(printMessage);

	}

	public void println(String message)
	{
		try
		{
			final Writer writer = mWriter;
			synchronized (this) {
				writer.write(TIMESTAMP_FMT.format(new Date()));
				writer.write(message);
				writer.write('\n');
//				writer.flush();
			}
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void close()
	{
		try
		{
			mPath = null;
			if (mWriter != null)
			{
				synchronized (this)
				{
					if (mWriter != null)
					{
						mWriter.close();
						//保留以提供checkNotClosed
//						mWriter = null;
					}
				}
			}
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void flush()
	{
		if (mWriter != null)
		{
			synchronized (this)
			{
				if (mWriter != null)
				{
					try
					{
						mWriter.flush();
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			}
		}
	}
}
