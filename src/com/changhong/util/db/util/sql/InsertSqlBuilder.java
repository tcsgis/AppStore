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

import org.apache.http.NameValuePair;

import com.changhong.common.CHStringUtils;
import com.changhong.exception.CHDBException;
import com.changhong.util.db.annotation.CHPrimaryKey;
import com.changhong.util.db.entity.CHArrayList;
import com.changhong.util.db.util.DBUtils;

/**
 * @Title SqlBuilder
 * @Package com.changhong.util.db.util.sql
 * @Description 插入sql语句构建器类
 * @version V1.0
 */
public class InsertSqlBuilder extends SqlBuilder
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
		StringBuilder columns = new StringBuilder(256);
		StringBuilder values = new StringBuilder(256);
		columns.append("INSERT INTO ");
		columns.append(tableName).append(" (");
		values.append("(");
		CHArrayList updateFields = getUpdateFields();
		if (updateFields != null)
		{
			boolean first = true;
			for (int i = 0; i < updateFields.size(); i++)
			{
				NameValuePair nameValuePair = updateFields.get(i);
				String value = nameValuePair.getValue();
				//skip NULL
				if (value != null) {
					if (first) {
						first = false;
					} else {
						columns.append(", ");
						values.append(", ");
					}
					columns.append(nameValuePair.getName());
					values.append((CHStringUtils.isNumeric(value) || CHStringUtils.isDouble(value)) ? value
							: ("'" + value.replace("'", "''") + "'"));
				}
			}
		} else
		{
			throw new CHDBException("插入数据有误！");
		}
		columns.append(") values ");
		values.append(")");
		columns.append(values);
		return columns.toString();
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
