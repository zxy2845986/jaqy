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
package com.teradata.jaqy.resultset;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.sql.SQLXML;

import javax.xml.transform.Result;
import javax.xml.transform.Source;

import com.teradata.jaqy.utils.ExceptionUtils;
import com.teradata.jaqy.utils.FileUtils;

/**
 * @author	Heng Yuan
 */
public class InMemSQLXML implements SQLXML, Comparable<SQLXML>
{
	private String m_xml;

	public InMemSQLXML (SQLXML xml) throws SQLException
	{
		m_xml = xml.getString ();
		xml.free ();
	}

	@Override
	public void free () throws SQLException
	{
		m_xml = null;
	}

	@Override
	public InputStream getBinaryStream () throws SQLException
	{
		return new ByteArrayInputStream (m_xml.getBytes (Charset.forName ("utf-8")));
	}

	@Override
	public OutputStream setBinaryStream () throws SQLException
	{
		throw ExceptionUtils.getNotImplemented ();
	}

	@Override
	public Reader getCharacterStream ()
	{
		return new StringReader (m_xml);
	}

	@Override
	public Writer setCharacterStream () throws SQLException
	{
		throw ExceptionUtils.getNotImplemented ();
	}

	@Override
	public String getString () throws SQLException
	{
		return m_xml;
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

	public int getLength ()
	{
		return m_xml.length ();
	}

	@Override
	public int compareTo (SQLXML o)
	{
		if (o instanceof InMemSQLXML)
		{
			return m_xml.compareTo (((InMemSQLXML)o).m_xml);
		}
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
}