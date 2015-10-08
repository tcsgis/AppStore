package cn.changhong.chcare.core.webapi.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by SilverFox on 2015/6/19.
 */
public class HttpReaderUtils {

	public static String readToEnd(InputStream in) throws IOException {
		InputStreamReader sr = null;
		try {
			//BufferedReader.readLine()对\r\n不是原样还原的
			sr = new InputStreamReader(new BufferedInputStream(in), "UTF-8");

			StringBuilder sb = new StringBuilder();
			int ch;
//			while ((ch = sr.read()) != -1) {
//				sb.append((char)ch);
//			}
			char[] data = new char[1024];
			while ((ch = sr.read(data)) != -1) {
				sb.append(data, 0, ch);
			}

			return sb.length() > 0 ? sb.toString() : null;

		} finally {
			if (sr != null) {
				try {
					sr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
