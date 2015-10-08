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
package com.changhong.exception;

/**
 * @Title CHNoSuchCommandException
 * @package com.changhong.exception
 * @Description CHNoSuchCommandException是当没有找到相应资源ID名字的资源时，抛出此异常！
 * @version V1.0
 */
public class CHNoSuchCommandException extends CHException
{
	private static final long serialVersionUID = 1L;

	public CHNoSuchCommandException()
	{
		super();
	}

	public CHNoSuchCommandException(String detailMessage)
	{
		super(detailMessage);

	}

}
