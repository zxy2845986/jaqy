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
package com.teradata.jaqy.connection;

import java.io.InputStream;
import java.io.Reader;
import java.sql.*;

import com.teradata.jaqy.interfaces.JaqyHelper;

/**
 * @author Heng Yuan
 */
public class JaqyPreparedStatement extends JaqyStatement
{
	private final PreparedStatement m_statement;
	private JaqyParameterMetaData m_parameterMetaData;

	public JaqyPreparedStatement (PreparedStatement stmt, JaqyConnection conn)
	{
		super (stmt, conn);
		m_statement = stmt;
	}

	public JaqyHelper getHelper ()
	{
		return getConnection ().getHelper ();
	}

	public JaqyParameterMetaData getParameterMetaData () throws SQLException
	{
		if (m_parameterMetaData == null)
		{
			m_parameterMetaData = new JaqyParameterMetaData (m_statement.getParameterMetaData (), getConnection ());
		}
		return m_parameterMetaData;
	}

	public boolean execute () throws SQLException
	{
		return m_statement.execute ();
	}

	public int[] executeBatch () throws SQLException
	{
		return m_statement.executeBatch ();
	}

	public int getParameterCount () throws SQLException
	{
		return getParameterMetaData ().getParameterCount ();
	}

	public void clearParameters () throws SQLException
	{
		m_statement.clearParameters ();
	}

	public void clearBatch () throws SQLException
	{
		m_statement.clearBatch ();
	}

	public void addBatch () throws SQLException
	{
		m_statement.addBatch ();
	}

    public void setString(int parameter, String x) throws SQLException
    {
		m_statement.setString (parameter, x);
    }

    public void setBytes(int parameter, byte[] x) throws SQLException
    {
		m_statement.setBytes (parameter, x);
    }

    public void setCharacterStream(int parameter, Reader reader) throws SQLException
    {
		m_statement.setCharacterStream (parameter, reader);
    }

    public void setBinaryStream(int parameter, InputStream x) throws SQLException
    {
		m_statement.setBinaryStream (parameter, x);
    }

    public void setObject (int parameter, Object obj) throws SQLException
	{
		m_statement.setObject (parameter, obj);
	}

    public void setClob (int parameter, Clob x) throws SQLException
	{
		m_statement.setClob (parameter, x);
	}

    public void setNClob (int parameter, NClob x) throws SQLException
	{
		m_statement.setNClob (parameter, x);
	}

    public void setBlob (int parameter, Blob x) throws SQLException
	{
		m_statement.setBlob (parameter, x);
	}

    public void setSQLXML (int parameter, SQLXML x) throws SQLException
	{
		m_statement.setSQLXML (parameter, x);
	}

	public void setObject (int parameter, Object obj, int sqlType) throws SQLException
	{
		m_statement.setObject (parameter, obj, sqlType);
	}

    public void setNull (int parameterIndex, int sqlType, String typeName) throws SQLException
    {
    	m_statement.setNull (parameterIndex, sqlType, typeName);
    }
}
