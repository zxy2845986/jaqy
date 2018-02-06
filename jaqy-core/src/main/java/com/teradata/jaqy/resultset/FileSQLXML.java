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
package com.teradata.jaqy.resultset;

import java.io.*;
import java.sql.SQLException;
import java.sql.SQLXML;

import javax.xml.transform.Result;
import javax.xml.transform.Source;

import com.teradata.jaqy.JaqyException;
import com.teradata.jaqy.utils.ExceptionUtils;
import com.teradata.jaqy.utils.FileUtils;

/**
 * @author	Heng Yuan
 */
public class FileSQLXML implements SQLXML, CloseableData, Comparable<SQLXML>
{
	private int m_length;
	private File m_file;

	public FileSQLXML (SQLXML xml, char[] charBuffer) throws SQLException
	{
		try
		{
			m_file = FileUtils.createTempFile ();
			m_length = (int)FileUtils.writeFile (m_file, xml.getCharacterStream (), charBuffer);
			xml.free ();
		}
		catch (IOException ex)
		{
			throw new JaqyException (ex);
		}
	}

	@Override
	public void free () throws SQLException
	{
		m_file.delete ();
	}

	@Override
	public InputStream getBinaryStream () throws SQLException
	{
		try
		{
			return new FileInputStream (m_file);
		}
		catch (IOException ex)
		{
			throw new JaqyException (ex);
		}
	}

	@Override
	public OutputStream setBinaryStream () throws SQLException
	{
		throw ExceptionUtils.getNotImplemented ();
	}

	@Override
	public Reader getCharacterStream ()
	{
		try
		{
			return new InputStreamReader (new FileInputStream (m_file), "UTF-8");
		}
		catch (IOException ex)
		{
			throw new JaqyException (ex);
		}
	}

	@Override
	public Writer setCharacterStream () throws SQLException
	{
		throw ExceptionUtils.getNotImplemented ();
	}

	@Override
	public String getString () throws SQLException
	{
		try
		{
			return FileUtils.readString (m_file, 0, m_length);
		}
		catch (IOException ex)
		{
			throw new JaqyException (ex);
		}
	}

	@Override
	public void setString (String value) throws SQLException
	{
		throw ExceptionUtils.getNotImplemented ();
	}

	@Override
	public <T extends Source> T getSource (Class<T> sourceClass) throws SQLException
	{
		throw ExceptionUtils.getNotImplemented ();
	}

	@Override
	public <T extends Result> T setResult (Class<T> resultClass) throws SQLException
	{
		throw ExceptionUtils.getNotImplemented ();
	}

	@Override
	public int compareTo (SQLXML o)
	{
		try
		{
			return FileUtils.compare (getCharacterStream(), o.getCharacterStream ());
		}
		catch (Exception ex)
		{
			// shouldn't reach here
			return -1;
		}
	}

	@Override
	public void close ()
	{
		m_file.delete ();
	}
}