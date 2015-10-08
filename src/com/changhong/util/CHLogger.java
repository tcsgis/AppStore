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
package com.changhong.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.changhong.util.log.ILogger;
import com.changhong.util.log.LoggerConfig;
import com.changhong.util.log.PrintToLogCatLogger;

/**
 * @Title CHLogger
 * @Package com.changhong.util.log
 * @Description CHLogger是一个日志打印类
 * @version V1.0
 */
public class CHLogger
{
	/**
	 * Priority constant for the println method; use TALogger.v.
	 */
	public static final int VERBOSE = 2;

	/**
	 * Priority constant for the println method; use TALogger.d.
	 */
	public static final int DEBUG = 3;

	/**
	 * Priority constant for the println method; use TALogger.i.
	 */
	public static final int INFO = 4;

	/**
	 * Priority constant for the println method; use TALogger.w.
	 */
	public static final int WARN = 5;

	/**
	 * Priority constant for the println method; use TALogger.e.
	 */
	public static final int ERROR = 6;
	/**
	 * Priority constant for the println method.
	 */
	public static final int ASSERT = 7;
	private static HashMap<String, ILogger> loggerHashMap = new HashMap<String, ILogger>();
	private static final ILogger defaultLogger = new PrintToLogCatLogger();

	public static boolean addLogger(ILogger logger) {
		String loggerName = logger.getClass().getName();
		String defaultLoggerName = defaultLogger.getClass().getName();
		boolean isSuccess = true;
		if (!loggerHashMap.containsKey(loggerName)
				&& !defaultLoggerName.equalsIgnoreCase(loggerName))
		{
			try {
				logger.open();
				loggerHashMap.put(loggerName, logger);
			} catch (Exception ex) {
				//NPE for nosdcard, IOEx
				ex.printStackTrace();
				isSuccess = false;
			}
		}
		return isSuccess;

	}

	public static void removeLogger(ILogger logger)
	{
		String loggerName = logger.getClass().getName();
		if (loggerHashMap.containsKey(loggerName))
		{
			logger.close();
			loggerHashMap.remove(loggerName);
		}

	}

	public static void d(Object object, String message)
	{

		printLoger(DEBUG, object, message);

	}

	public static void e(Object object, String message)
	{

		printLoger(ERROR, object, message);

	}

	public static void i(Object object, String message)
	{

		printLoger(INFO, object, message);

	}

	public static void v(Object object, String message)
	{

		printLoger(VERBOSE, object, message);

	}

	public static void w(Object object, String message)
	{

		printLoger(WARN, object, message);

	}

	public static void e(Object tag, Throwable ex)
	{

		printLoger(ERROR, tag, dumpThrowable(ex));

	}

	public static void w(Object tag, Throwable ex)
	{

		printLoger(WARN, tag, dumpThrowable(ex));

	}

	public static void d(String tag, String message)
	{

		printLoger(DEBUG, tag, message);

	}

	public static void e(String tag, String message)
	{

		printLoger(ERROR, tag, message);

	}

	public static void i(String tag, String message)
	{

		printLoger(INFO, tag, message);

	}

	public static void v(String tag, String message)
	{

		printLoger(VERBOSE, tag, message);

	}

	public static void w(String tag, String message)
	{

		printLoger(WARN, tag, message);

	}

	public static void e(String tag, Throwable ex)
	{

		printLoger(ERROR, tag, dumpThrowable(ex));

	}

	public static void w(String tag, Throwable ex)
	{

		printLoger(WARN, tag, dumpThrowable(ex));

	}

	private static String dumpThrowable(Throwable ex)
	{
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);
		return sw.toString();
	}

	public static void println(int priority, String tag, String message)
	{
		printLoger(priority, tag, message);
	}

	private static void printLoger(int priority, Object object, String message)
	{
		Class<?> cls = object.getClass();
		String tag = cls.getName();
		//yyj: 减少多余的数组分配
		int dot = tag.lastIndexOf('.');
		if (dot != -1) {
			tag = tag.substring(dot + 1);
		}
//		String arrays[] = tag.split("\\.");
//		tag = arrays[arrays.length - 1];
		printLoger(priority, tag, message);
	}

	private static void printLoger(int priority, String tag, String message)
	{
		if (LoggerConfig.DEBUG)
		{
			if (message == null)message = "";
			printLoger(defaultLogger, priority, tag, message);
			for (ILogger logger : loggerHashMap.values())
			{
				if (logger != null)
				{
					printLoger(logger, priority, tag, message);
				}
			}
		}
	}

	private static void printLoger(ILogger logger, int priority, String tag,
			String message)
	{

		switch (priority)
		{
		case VERBOSE:
			logger.v(tag, message);
			break;
		case DEBUG:
			logger.d(tag, message);
			break;
		case INFO:
			logger.i(tag, message);
			break;
		case WARN:
			logger.w(tag, message);
			break;
		case ERROR:
			logger.e(tag, message);
			break;
		default:
			break;
		}
	}

	public static void flush()
	{
		defaultLogger.flush();
		for (ILogger logger : loggerHashMap.values())
		{
			if (logger != null)
			{
				logger.flush();
			}
		}
	}
}
