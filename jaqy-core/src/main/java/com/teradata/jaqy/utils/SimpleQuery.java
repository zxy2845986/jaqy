/*
 * Copyright (c) 2017-2018 Teradata
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.teradata.jaqy.utils;

/**
 * @author	Heng Yuan
 */
public class SimpleQuery
{
	/**
	 * The SQL query to be executed.
	 */
	public final String sql;
	/**
	 * The field of the result set to be retrieved.  If multiple rows are
	 * returned, the values from the column index specified will be
	 * concatenated.
	 */
	public final int columnIndex;

	public SimpleQuery (String s, int column)
	{
		this.sql = s;
		this.columnIndex = column;
	}
}
