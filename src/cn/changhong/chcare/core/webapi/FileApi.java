package cn.changhong.chcare.core.webapi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;

import com.changhong.activity.util.PictureUtil;
import com.google.gson.reflect.TypeToken;

import cn.changhong.chcare.core.webapi.bean.ResponseBean;
import cn.changhong.chcare.core.webapi.server.ChCareWebApiRequestErrorType;
import cn.changhong.chcare.core.webapi.server.IFileService;
import cn.changhong.chcare.core.webapi.util.HttpRequestException;
import cn.changhong.chcare.core.webapi.util.MultipartUtility;

public class FileApi extends IFileService{

	@SuppressLint("NewApi")
	@Override
	public ResponseBean<?> uploadFiles(ArrayList<String> paths, int width, int height) throws HttpRequestException {
		ArrayList<File> files = new ArrayList<File>();
		for(String path : paths){
			files.add(new File(path));
		}
		String url = BASE_URL + "File";
		System.out.println(url + paths);
		String response = null;
		MultipartUtility multipart = new MultipartUtility(url, charset);
		if (files != null && files.size() != 0) {

			InputStream instream = null;
			Bitmap bitmap = null;
			ByteArrayOutputStream baos = null;
			for (int i = 0; i < files.size(); i++) {
				File file = files.get(i);
				if (!file.exists() || !file.isFile()) {
					throw new HttpRequestException(
							"File Not Exits![" + file.getAbsolutePath(),
							ChCareWebApiRequestErrorType.CHCAREWEBAPI_REQUEST_ERROR);
				}

				try {
					bitmap = PictureUtil.decodeSampledBitmapFromFile(
							file.getPath(), width, height);
					
					int size;
					baos = new ByteArrayOutputStream();
					if (!bitmap.hasAlpha()) {
						int options = 100;
						do {
							baos.reset();// 重置baos即清空baos
							// 这里压缩options%，把压缩后的数据存放到baos中
							bitmap.compress(Bitmap.CompressFormat.JPEG,
									options, baos);
							size = baos.size() / 1024;
							options -= 10;// 每次都减少10
						} while ((size > 200) && (options > 0));
					} else {
						// options无效
						bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
						if (file.length() > baos.size()) {
							// 优化判断, PNG压缩后可能比原图大
							size = baos.size() / 1024;
						} else {
							size = 0;
						}
					}
					if (size > 0) {
						instream = new ByteArrayInputStream(baos.toByteArray());
					}
				} catch (Exception e) {
					e.printStackTrace();
					instream = null;
				} finally {
					if (baos != null) {
						try {
							baos.close();
							baos = null;
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (bitmap != null) {
						bitmap.recycle();
					}
				}

				try {
					if (instream == null) {
						instream = new FileInputStream(file);
					}
					multipart.addFilePart(file.getName(), instream);
				} catch (FileNotFoundException e) {
					throw new HttpRequestException(
							"Open File Stream Failed!Error Stack["
									+ e.getLocalizedMessage() + "]",
							ChCareWebApiRequestErrorType.CHCAREWEBAPI_REQUEST_ERROR);
				} finally {
					try {
						instream.close();
						instream = null;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}

		}
		List<String> responses = multipart.finish();
		if (responses != null && responses.size() > 0) {
			response = responses.get(0);
		} else {
			throw new HttpRequestException("Http Response Stream Is Null!",
					ChCareWebApiRequestErrorType.CHCAREWEBAPI_RESPONSE_ERROR);
		}
		System.out.println(response);
		ResponseBean<?> result = this.transToBean(response);
		if (result != null && result.getState() == 0) {
			Type type = new TypeToken<ResponseBean<String>>() {
			}.getType();
			result = this.transToBean(response, type);
		}
		return result;
	}

}
