/*
 * Copyright (c) 2017 Teradata
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
package com.teradata.jaqy.helper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;

import com.teradata.jaqy.Globals;
import com.teradata.jaqy.connection.JaqyConnection;
import com.teradata.jaqy.connection.JaqyPreparedStatement;
import com.teradata.jaqy.connection.JaqyResultSet;
import com.teradata.jaqy.connection.JaqyStatement;
import com.teradata.jaqy.connection.JdbcFeatures;
import com.teradata.jaqy.interfaces.JaqyHelper;

/**
 * @author	Heng Yuan
 */
public class DefaultHelper implements JaqyHelper
{
	private final JaqyConnection m_conn;
	private final Globals m_globals;
	private final JdbcFeatures m_features;

	public DefaultHelper (JdbcFeatures features, JaqyConnection conn, Globals globals)
	{
		m_features = features;
		m_conn = conn;
		m_globals = globals;
	}

	public JdbcFeatures getFeatures ()
	{
		return m_features;
	}

	public Globals getGlobals ()
	{
		return m_globals;
	}

	@Override
	public JaqyResultSet getResultSet (ResultSet rs) throws SQLException
	{
		return new JaqyResultSet (rs, this);
	}

	@Override
	public JaqyConnection getConnection ()
	{
		return m_conn;
	}

	@Override
	public JaqyStatement createStatement () throws SQLException
	{
		Connection conn = m_conn.getConnection ();
		if (m_features.forwardOnlyRS)
			return new JaqyStatement (conn.createStatement (), m_conn);
		else
		{
			try
			{
				return new JaqyStatement (conn.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY), m_conn);
			}
			catch (SQLException ex)
			{
				// we are catching all SQLException rather than just
				// SQLFeatureNotSupportedException because some JDBC drivers
				// throw SQLException in all cases.
				getFeatures().forwardOnlyRS = true;
				return new JaqyStatement (conn.createStatement (), m_conn);
			}
		}
	}

	@Override
	public JaqyPreparedStatement preparedStatement (String sql) throws SQLException
	{
		Connection conn = m_conn.getConnection ();
		if (m_features.forwardOnlyRS)
			return new JaqyPreparedStatement (conn.prepareStatement (sql), m_conn);
		else
		{
			try
			{
				return new JaqyPreparedStatement (conn.prepareStatement (sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY), m_conn);
			}
			catch (SQLException ex)
			{
				// we are catching all SQLException rather than just
				// SQLFeatureNotSupportedException because some JDBC drivers
				// throw SQLException in all cases.
				getFeatures().forwardOnlyRS = true;
				return new JaqyPreparedStatement (conn.prepareStatement (sql), m_conn);
			}
		}
	}

	@Override
	public String getCatalog () throws SQLException
	{
		if (m_features.noCatalog)
			return null;
		try
		{
			return m_conn.getConnection ().getCatalog ();
		}
		catch (SQLFeatureNotSupportedException ex)
		{
			m_features.noCatalog = true;
			return null;
		}
		catch (SQLException ex)
		{
			throw ex;
		}
		catch (Throwable t)
		{
			m_features.noCatalog = true;
			return null;
		}
	}

	@Override
	public String getSchema () throws SQLException
	{
		if (m_features.noSchema)
			return null;
		try
		{
			return m_conn.getConnection ().getSchema ();
		}
		catch (SQLFeatureNotSupportedException ex)
		{
			getFeatures().noSchema = true;
			return null;
		}
		catch (SQLException ex)
		{
			throw ex;
		}
		catch (Throwable t)
		{
			m_features.noSchema = true;
			return null;
		}
	}

	@Override
	public String getURL () throws SQLException
	{
		String url = m_conn.getMetaData ().getURL ();
		if (url.startsWith ("jdbc:"))
			url = url.substring (5);
		return url;
	}

	@Override
	public String getPath () throws SQLException
	{
		boolean hasUser = false;
		StringBuilder buffer = new StringBuilder ();
		String user = m_conn.getMetaData ().getUserName ();
		if (user != null && user.length () > 0)
		{
			hasUser = true;
			buffer.append (user);
		}

		boolean hasHost = false;
		String url = m_conn.getMetaData ().getURL ();
		int start = url.indexOf ("//") + 2;
		if (start > 0)
		{
			hasHost = true;
			int end = url.indexOf ('/', start);
			if (end < 0)
				end = url.length ();
			String host = url.substring (start, end);
			if (hasUser)
				buffer.append (" @ ");
			buffer.append (host);
		}

		String path = null;
		String catalog = getCatalog ();
		String schema = getSchema ();
		if (catalog == null || catalog.length () == 0)
		{
			path = schema; 
		}
		else
		{
			if (schema == null || schema.length () == 0)
				path = catalog;
			path = catalog + m_conn.getCatalogSeparator () + schema;
		}
		if (path != null)
		{
			if (hasUser || hasHost)
			{
				buffer.append (" - ");
			}
			buffer.append (path);
		}
		return buffer.toString ();
	}
}
