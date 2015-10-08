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
package com.changhong.util.db.util.sql;

import java.lang.reflect.Field;

import com.changhong.exception.CHDBException;
import com.changhong.util.db.entity.CHArrayList;
import com.changhong.util.db.util.DBUtils;

/**
 * @Title SqlBuilder
 * @Package com.changhong.util.db.util.sql
 * @Description 删除sql语句构建器类
 * @version V1.0
 */
public class DeleteSqlBuilder extends SqlBuilder
{
	@Override
	public String buildSql() throws CHDBException, IllegalArgumentException,
			IllegalAccessException
	{
		// TODO Auto-generated method stub
		StringBuilder stringBuilder = new StringBuilder(256);
		stringBuilder.append("DELETE FROM ");
		stringBuilder.append(tableName);
		if (entity == null)
		{
			stringBuilder.append(buildConditionString());
		} else
		{
			stringBuilder.append(buildWhere(buildWhere(this.entity)));
		}

		return stringBuilder.toString();
	}

	/**
	 * 创建Where语句
	 * 
	 * @param entity
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws CHDBException
	 */
	public CHArrayList buildWhere(Object entity)
			throws IllegalArgumentException, IllegalAccessException,
			CHDBException
	{
		Class<?> clazz = entity.getClass();
		CHArrayList whereArrayList = new CHArrayList();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields)
		{
			field.setAccessible(true);
			if (!DBUtils.isTransient(field))
			{
				if (DBUtils.isBaseDateType(field))
				{
					// 如果ID不是自动增加的
					if (!DBUtils.isAutoIncrement(field))
					{
						String columnName = DBUtils.getColumnByField(field);
						if (null != field.get(entity)
								&& field.get(entity).toString().length() > 0)
						{
							whereArrayList.add(
									(columnName != null && !columnName
											.equals("")) ? columnName : field
											.getName(), DBUtils.toString(field.get(entity)));
						}
					}
				}
			}
		}
		if (whereArrayList.isEmpty())
		{
			throw new CHDBException("不能创建Where条件，语句");
		}
		return whereArrayList;
	}
}
