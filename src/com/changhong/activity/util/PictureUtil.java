package com.changhong.activity.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ThumbnailUtils;
import android.net.Uri;

import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.utils.ImageSizeUtils;

public class PictureUtil {
	private Context mContext = null;
	public PictureUtil (Context context){
		this.mContext = context;
	}
	
	/*
	 * 将图片依照参数转化为圆形
	 */
	public static Bitmap toRoundBitmap(Bitmap bitmap, int width, int height){
		try{
			Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
			Canvas canvas = new Canvas(output);
			Paint paint = new Paint();
			int cx = width/2;
			int cy = height/2;
			int radius = Math.min(cx, cy);
			canvas.save();
			canvas.drawCircle(cx, cy, radius, paint);
			paint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.SRC_IN));
			canvas.drawBitmap(bitmap, 0, 0, paint);		
			canvas.restore();
			return output;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}

	}

	public static Bitmap getGreyImage(Bitmap bmp) {  
		try {
			int width = bmp.getWidth(); // 获取位图的宽  
			int height = bmp.getHeight(); // 获取位图的高  
			int[] pixels = new int[width * height]; // 通过位图的大小创建像素点数组  
			
			bmp.getPixels(pixels, 0, width, 0, 0, width, height);  
			int alpha = 0xFF << 24;  
			for (int i = 0; i < height; i++) {  
				for (int j = 0; j < width; j++) {  
					int grey = pixels[width * i + j];  
					
					//分离三原色  
					int red = ((grey & 0x00FF0000) >> 16);  
					int green = ((grey & 0x0000FF00) >> 8);  
					int blue = (grey & 0x000000FF);  
					
					//转化成灰度像素  
					grey = (int) (red * 0.3 + green * 0.59 + blue * 0.11);  
					grey = alpha | (grey << 16) | (grey << 8) | grey;  
					pixels[width * i + j] = grey;  
				}  
			}  
			//新建图片  
			Bitmap newBmp = Bitmap.createBitmap(width, height, Config.RGB_565);  
			//设置图片数据  
			newBmp.setPixels(pixels, 0, width, 0, 0, width, height);  
			
			return newBmp;  
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    }  
	
//	public static Bitmap getGreyImage(Bitmap old) { 
//		try {
//			int width, height;     
//			height = old.getHeight();     
//			width = old.getWidth();         
//			Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);     
//			Canvas c = new Canvas(old);     
//			Paint paint = new Paint();     
//			ColorMatrix cm = new ColorMatrix();     
//			cm.setSaturation(0);     
//			ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);     
//			paint.setColorFilter(f);     
//			c.drawBitmap(b, 0, 0, paint);     
//			return b;     
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//    }  
	
	/*
	 * 将图片转化为圆形
	 */
	public static Bitmap toRoundBitmap(Bitmap bitmap) {
		try {
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			float roundPx;
			float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
			if (width <= height) {
				roundPx = width / 2;
				top = 0;
				bottom = width;
				left = 0;
				right = width;
				height = width;
				dst_left = 0;
				dst_top = 0;
				dst_right = width;
				dst_bottom = width;
			} else {
				roundPx = height / 2;
				float clip = (width - height) / 2;
				left = clip;
				right = width - clip;
				top = 0;
				bottom = height;
				width = height;
				dst_left = 0;
				dst_top = 0;
				dst_right = height;
				dst_bottom = height;
			}
			Bitmap output = Bitmap
					.createBitmap(width, height, Config.ARGB_8888);
			Canvas canvas = new Canvas(output);
			final int color = 0xff424242;
			final Paint paint = new Paint();
			final Rect src = new Rect((int) left, (int) top, (int) right,
					(int) bottom);
			final Rect dst = new Rect((int) dst_left, (int) dst_top,
					(int) dst_right, (int) dst_bottom);
			final RectF rectF = new RectF(dst);
			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(color);
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas.drawBitmap(bitmap, src, dst, paint);
			bitmap.recycle();
			bitmap = null;
			return output;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	/**
	 * 
	 * @param filename 文件路径
	 * @param reqWidth 高度
	 * @param reqHeight　宽度
	 * @return
	 */
	public static Bitmap decodeSampledBitmapFromFile(String filename,
			int reqWidth, int reqHeight) {
		// First decode with inJustDecodeBounds=true to check dimensions
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;// 只读边,不读内容
		BitmapFactory.decodeFile(filename, newOpts);
		Bitmap bitmap = null;
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;

		if(reqWidth == 0 && reqHeight == 0){
			reqWidth = w;
			reqHeight = h;
		}
		
		ImageSize simpleSize = new ImageSize(reqWidth, reqHeight);
		//获取设备能显示的最大采样率
		int be = ImageSizeUtils.computeMinImageSampleSize(simpleSize);
		
		if(be < 2) {
			be = (int) ((w / reqWidth + h/ reqHeight) / 2);
			if (be <= 0) {
				be = 1;
			}
		}
		
		newOpts.inSampleSize = be;// 设置采样率
		newOpts.inPreferredConfig = Config.ARGB_8888;
		newOpts.inPurgeable = true;// 同时设置才会有效
		newOpts.inInputShareable = true;// 当系统内存不够时候图片自动被回收

		for (int i = 1; i <= 3; i++) {
			try {
				if(i == 3) newOpts.inPreferredConfig = Config.RGB_565;
				bitmap = BitmapFactory.decodeFile(filename, newOpts);
				break;
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
				newOpts.inSampleSize = newOpts.inSampleSize + i;
			}
		}
		return bitmap;
	}

	//显示原生图片尺寸大小
	public Bitmap getPathBitmap(Uri imageFilePath, int dw, int dh) throws FileNotFoundException {
		// 获取屏幕的宽和高
		BitmapFactory.Options op = new BitmapFactory.Options();
		op.inJustDecodeBounds = true;
		// 由于使用了MediaStore存储，这里根据URI获取输入流的形式
		InputStream is = null;
		try {
			is = mContext.getContentResolver().openInputStream(imageFilePath);
			BitmapFactory.decodeStream(is, null, op);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException ignored) {
				}
			}
		}

		int wRatio = (int) Math.ceil(op.outWidth / (float) dw); // 计算宽度比例
		int hRatio = (int) Math.ceil(op.outHeight / (float) dh); // 计算高度比例

		/**
		 * 接下来，我们就需要判断是否需要缩放以及到底对宽还是高进行缩放。 如果高和宽不是全都超出了屏幕，那么无需缩放。
		 * 如果高和宽都超出了屏幕大小，则如何选择缩放呢》 这需要判断wRatio和hRatio的大小
		 * 大的一个将被缩放，因为缩放大的时，小的应该自动进行同比率缩放。 缩放使用的还是inSampleSize变量
		 */
		if (wRatio > 1 && hRatio > 1) {
			if (wRatio > hRatio) {
				op.inSampleSize = wRatio;
			} else {
				op.inSampleSize = hRatio;
			}
		}
		op.inJustDecodeBounds = false; // 注意这里，一定要设置为false，因为上面我们将其设置为true来获取图片尺寸了
		op.inPreferredConfig = Config.ARGB_8888;
		op.inPurgeable = true;// 同时设置才会有效
		op.inInputShareable = true;// 当系统内存不够时候图片自动被回收

		Bitmap pic = null;
		is = null;
		try {
			is = mContext.getContentResolver().openInputStream(imageFilePath);
			for (int i = 1; i <= 3; i++) {
				try {
					if(i == 3) {
						op.inPreferredConfig = Config.RGB_565;
					}
					pic = BitmapFactory.decodeStream(is, null, op);
					break;
				} catch (OutOfMemoryError e1) {
					e1.printStackTrace();
					op.inSampleSize = op.inSampleSize + i;
				}
			}
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return pic;
	}
}
