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
package com.changhong.util.layoutloader;

import android.content.pm.PackageManager.NameNotFoundException;

import com.changhong.exception.CHNoSuchNameLayoutException;

/**
 * @Title ILayoutLoader
 * @Package com.changhong.util.layoutloader
 * @Description ILayoutLoader 通过资源名获取布局
 * @version V1.0
 */
public interface ILayoutLoader
{
	public int getLayoutID(String resIDName) throws ClassNotFoundException,
			IllegalArgumentException, IllegalAccessException,
			NameNotFoundException, CHNoSuchNameLayoutException;

}
