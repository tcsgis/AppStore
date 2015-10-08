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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.apache.http.NameValuePair;

import com.changhong.common.CHStringUtils;
import com.changhong.exception.CHDBException;
import com.changhong.util.CHLogger;
import com.changhong.util.db.annotation.CHPrimaryKey;
import com.changhong.util.db.entity.CHArrayList;
import com.changhong.util.db.util.DBUtils;

/**
 * @Title TASqlBuilder
 * @Package com.changhong.util.db.util.sql
 * @Description 更新sql语句构建器类
 * @author 白猫
 * @date 2013-1-20
 * @version V1.0
 */
public class UpdateSqlBuilder extends SqlBuilder
{

	@Override
	public void onPreGetStatement() throws CHDBException,
			IllegalArgumentException, IllegalAccessException
	{
		// TODO Auto-generated method stub
		if (getUpdateFields() == null)
		{
			setUpdateFields(getFieldsAndValue(entity));
		}
		super.onPreGetStatement();
	}

	@Override
	public String buildSql() throws CHDBException, IllegalArgumentException,
			IllegalAccessException
	{
		// TODO Auto-generated method stub
		StringBuilder stringBuilder = new StringBuilder(256);
		stringBuilder.append("UPDATE ");
		stringBuilder.append(tableName).append(" SET ");

		CHArrayList needUpdate = getUpdateFields();
		for (int i = 0; i < needUpdate.size(); i++)
		{
			NameValuePair nameValuePair = needUpdate.get(i);
			String value = nameValuePair.getValue();
			stringBuilder
					.append(nameValuePair.getName())
					.append(" = ")
					.append((value != null) ? (CHStringUtils.isNumeric(value) || CHStringUtils.isDouble(value) ?
							value : ("'" + value.replace("'", "''") + "'")) : "NULL");
			if (i + 1 < needUpdate.size())
			{
				stringBuilder.append(", ");
			}
		}
		if (!CHStringUtils.isEmpty(this.where))
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
					Annotation annotation = field
							.getAnnotation(CHPrimaryKey.class);
					if (annotation != null)
					{
						String columnName = DBUtils.getColumnByField(field);
						whereArrayList.add((columnName != null && !columnName
								.equals("")) ? columnName : field.getName(),
								field.get(entity).toString());
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

	/**
	 * 从实体加载,更新的数据
	 * 
	 * @return
	 * @throws CHDBException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static CHArrayList getFieldsAndValue(Object entity)
			throws CHDBException, IllegalArgumentException,
			IllegalAccessException
	{
		// TODO Auto-generated method stub
		CHArrayList arrayList = new CHArrayList();
		if (entity == null)
		{
			throw new CHDBException("没有加载实体类！");
		}
		Class<?> clazz = entity.getClass();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields)
		{

			if (!DBUtils.isTransient(field))
			{
				if (DBUtils.isBaseDateType(field))
				{
					CHPrimaryKey annotation = field
							.getAnnotation(CHPrimaryKey.class);
					if (annotation == null || !annotation.autoIncrement())
					{
						String columnName = DBUtils.getColumnByField(field);
						columnName = (columnName != null && !columnName
								.equals("")) ? columnName : field.getName();
						field.setAccessible(true);
						String val = DBUtils.toString(field.get(entity));
						arrayList.add(columnName, val);
					}
				}
			}
		}
		return arrayList;
	}
}
