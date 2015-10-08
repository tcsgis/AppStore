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
package com.changhong.util.cache;

/**
 * @Title CHCallBackHandler
 * @Package com.changhong.util.cache
 * @Description 缓存结果的回调类
 * @version V1.0
 */
public class CHCallBackHandler<T>
{
	/**
	 * 缓存运行开始
	 * 
	 * @param t
	 *            响应的对象
	 * @param data
	 *            数据唯一标识
	 */
	public void onStart(T t, Object data)
	{
	}

	/**
	 * 缓存运行开始
	 * 
	 * @param t
	 *            响应的对象
	 * @param data
	 *            数据唯一标识
	 * @param inputStream
	 *            标识对应的响应数据
	 */
	public void onSuccess(T t, Object data, byte[] buffer)
	{
	}
	
	/**
	 * 缓存运行开始
	 * 
	 * @param t
	 *            响应的对象
	 * @param data
	 *            数据唯一标识
	 * @param String
	 *            标识对应的响应数据的文件路径
	 */
	public void onFileSuccess(T t, Object data, String filePath)
	{
	}

	/**
	 * 缓存运行失败
	 * 
	 * @param t
	 *            响应的对象
	 * @param data
	 *            数据唯一标识
	 */
	public void onFailure(T t, Object data)
	{

	}
}
