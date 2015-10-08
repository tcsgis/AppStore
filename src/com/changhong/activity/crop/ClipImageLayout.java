package com.changhong.activity.crop;

import java.io.IOException;

import com.changhong.activity.util.PictureUtil;
import com.changhong.util.CHLogger;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * http://blog.csdn.net/lmj623565791/article/details/39761281
 * @author zhy
 *
 */
public class ClipImageLayout extends RelativeLayout
{

	private ClipZoomImageView mZoomImageView;
	private ClipImageBorderView mClipImageView;

	public ClipImageLayout(Context context, int width, int height, Uri uri)
	{
		this(context, null, width, height, uri);
	}
	
	public ClipImageLayout(Context context, AttributeSet attrs, int width, int height, Uri uri)
	{
		super(context, attrs);
		
		CHLogger.d(this, "path " + uri.getPath());
		Bitmap bitmap = PictureUtil.decodeSampledBitmapFromFile(uri.getPath(), width, height);
		try {
			ExifInterface ei = new ExifInterface(uri.getPath());
			int tag = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
			int degree = 0;
			if (tag == ExifInterface.ORIENTATION_ROTATE_90) {
				degree = 90;
			} else if (tag == ExifInterface.ORIENTATION_ROTATE_180) {
				degree = 180;
			} else if (tag == ExifInterface.ORIENTATION_ROTATE_270) {
				degree = 270;
			}
			
			if (degree != 0 && bitmap != null) {
				Matrix m = new Matrix();
				m.setRotate(degree, bitmap.getWidth()/2, bitmap.getHeight()/2);
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		catch(OutOfMemoryError e){
			e.printStackTrace();
		}
		
		mZoomImageView = new ClipZoomImageView(context, width, height);
		mClipImageView = new ClipImageBorderView(context, width, height);

		android.view.ViewGroup.LayoutParams lp = new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		
		this.addView(mZoomImageView, lp);
		this.addView(mClipImageView, lp);
		
		mZoomImageView.setImageBitmap(bitmap);
		
	}

	/**
	 * 裁切图片
	 * 
	 * @return
	 */
	public Bitmap clip()
	{
		return mZoomImageView.clip();
	}

}
