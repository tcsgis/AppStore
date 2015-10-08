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
package com.changhong.util.db.util;

import com.changhong.util.db.util.sql.DeleteSqlBuilder;
import com.changhong.util.db.util.sql.InsertSqlBuilder;
import com.changhong.util.db.util.sql.QuerySqlBuilder;
import com.changhong.util.db.util.sql.SqlBuilder;
import com.changhong.util.db.util.sql.UpdateSqlBuilder;

/**
 * @Title SqlBuilderFactory
 * @Package com.changhong.util.db.util.sql
 * @Description Sql构建器工厂,生成sql语句构建器
 * @version V1.0
 */
public class SqlBuilderFactory
{
	private static SqlBuilderFactory instance;
	/**
	 * 调用getSqlBuilder(int operate)返回插入sql语句构建器传入的参数
	 */
	public static final int INSERT = 0;
	/**
	 * 调用getSqlBuilder(int operate)返回查询sql语句构建器传入的参数
	 */
	public static final int SELECT = 1;
	/**
	 * 调用getSqlBuilder(int operate)返回删除sql语句构建器传入的参数
	 */
	public static final int DELETE = 2;
	/**
	 * 调用getSqlBuilder(int operate)返回更新sql语句构建器传入的参数
	 */
	public static final int UPDATE = 3;

	/**
	 * 单例模式获得Sql构建器工厂
	 * 
	 * @return sql构建器
	 */
	public static SqlBuilderFactory getInstance()
	{
		if (instance == null)
		{
			instance = new SqlBuilderFactory();
		}
		return instance;
	}

	/**
	 * 获得sql构建器
	 * 
	 * @param operate
	 * @return 构建器
	 */
	public synchronized SqlBuilder getSqlBuilder(int operate)
	{
		SqlBuilder sqlBuilder = null;
		switch (operate)
		{
		case INSERT:
			sqlBuilder = new InsertSqlBuilder();
			break;
		case SELECT:
			sqlBuilder = new QuerySqlBuilder();
			break;
		case DELETE:
			sqlBuilder = new DeleteSqlBuilder();
			break;
		case UPDATE:
			sqlBuilder = new UpdateSqlBuilder();
			break;
		default:
			break;
		}
		return sqlBuilder;
	}
}
