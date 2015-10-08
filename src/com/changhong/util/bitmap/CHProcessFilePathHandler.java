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

import java.util.Map;

import com.changhong.util.cache.CHProcessDataHandler;

public abstract class CHProcessFilePathHandler extends CHProcessDataHandler
{

	@Override
	public byte[] processData(Object url, Map<String,String> params)
	{
		// TODO Auto-generated method stub
		String filePath = null;
		try {
			filePath = processDownFile(url, params);
			if(filePath != null){
				return filePath.getBytes();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	/**
	 * 
	 * @param url　请求路径
	 * @param params　参数
	 * @return 返回下载成功后保存的文件路径
	 * @throws Exception
	 */
	protected abstract String processDownFile(Object url, Map<String,String> params) throws Exception;

}
