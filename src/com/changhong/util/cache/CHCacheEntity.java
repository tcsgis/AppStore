/*
 *
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

import com.changhong.common.CHBaseEntity;

import java.lang.ref.WeakReference;

public class CHCacheEntity<T> extends CHBaseEntity
{
	private static final long serialVersionUID = 1L;
	private WeakReference<T> t;
	private CHAsyncEntity asyncEntity;

	public T getT()
	{
		final WeakReference<T> t = this.t;
		return (t != null) ? t.get() : null;
	}

	public void setT(T t)
	{
		this.t = new WeakReference<T>(t);
	}

	public CHAsyncEntity getAsyncEntity()
	{
		return asyncEntity;
	}

	public void setAsyncEntity(CHAsyncEntity asyncEntity)
	{
		this.asyncEntity = asyncEntity;
	}

}
