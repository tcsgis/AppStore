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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.changhong.activity.util.PictureUtil;
import com.changhong.util.cache.CHCallBackHandler;

public class CHBitmapCallBackHanlder extends CHCallBackHandler<ImageView>
{
	private Bitmap mLoadingBitmap;
	private int width, height;
	private boolean isRound = false;
	private boolean isGray = false;
	
	@Override
	public void onStart(ImageView t, Object data)
	{
		// TODO Auto-generated method stub
		super.onStart(t, data);
		onSuccess(t, data, null);
	}

	@Override
	public void onSuccess(ImageView imageView, Object data, byte[] buffer)
	{
		// TODO Auto-generated method stub
		super.onSuccess(imageView, data, buffer);
		if (buffer != null && imageView != null)
		{
			Bitmap bitmap = null;
			try
			{
				if (buffer != null)
				{
					bitmap = BitmapFactory.decodeByteArray(buffer, 0,
							buffer.length);
				}
				setImageBitmap(imageView, bitmap);
			} catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else
		{
			if (mLoadingBitmap != null)
			{
				setLoadBitmap(imageView, mLoadingBitmap);
			}
		}
	}
	
	/**
	 * 通过文件路径加载图片
	 * @param t
	 * @param data
	 * @param filePath
	 */
	public void onFileSuccess(ImageView imageView, Object data, String filePath)
	{
		super.onFileSuccess(imageView, data, filePath);
		if (filePath != null && imageView != null)
		{
			Bitmap bitmap = null;
			try
			{
				bitmap = PictureUtil.decodeSampledBitmapFromFile(filePath, width, height);
				
				setImageBitmap(imageView, bitmap);
			} catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else
		{
			if (mLoadingBitmap != null)
			{
				setLoadBitmap(imageView, mLoadingBitmap);
			}
		}
	}


	@Override
	public void onFailure(ImageView t, Object data)
	{
		// TODO Auto-generated method stub
		super.onFailure(t, data);
	}

	/**
	 * 设置默认的加载图片
	 * 
	 * @param defaultBitmap
	 */
	public void setLoadingImage(Bitmap bitmap)
	{
		this.mLoadingBitmap = bitmap;
		initWithHeigth();
	}

	public Bitmap getmLoadingBitmap() {
		return mLoadingBitmap;
	}

	public void setLoadingImage(Context context, int resId)
	{
		this.mLoadingBitmap = BitmapFactory.decodeResource(
				context.getResources(), resId);
		initWithHeigth();
	}
	
	private void initWithHeigth(){
		
		if(mLoadingBitmap != null && this.width == 0){
			this.width = mLoadingBitmap.getWidth();
			this.height = mLoadingBitmap.getHeight();
		}
	}
	
	/**
	 * 设置圆形参数
	 */
	public void setCircleParams(boolean isRound){
		this.isRound = isRound;
	};
	
	public void setGrayParams(boolean isGray){
		this.isGray = isGray;
	}

	/**
	 * 设置Bitmap到ImageView
	 * 
	 * @param imageView
	 * @param bitmap
	 */
	private void setImageBitmap(ImageView imageView, Bitmap bitmap)
	{
		if(imageView != null){
			if(isGray){
				Bitmap bitmap2 = PictureUtil.getGreyImage(bitmap);
				if(bitmap2 != null){
					bitmap = bitmap2;
				}
			}
			if(isRound){
				bitmap = PictureUtil.toRoundBitmap(bitmap);
				if(bitmap != null)
				imageView.setImageBitmap(bitmap);
			}else{
				imageView.setImageBitmap(bitmap);
			}
		}
	}
	
	private void setLoadBitmap(ImageView imageView, Bitmap bitmap){
		
		if(bitmap != null && imageView !=null){
			imageView.setImageBitmap(bitmap);
		}
	}
}
