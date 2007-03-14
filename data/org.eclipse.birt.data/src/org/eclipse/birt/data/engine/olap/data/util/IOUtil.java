/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.olap.data.util;

import java.io.IOException;
import java.util.Date;
import java.math.BigDecimal;

/**
 * 
 */

public class IOUtil
{

	public static IObjectWriter getRandomWriter( int dataType )
	{
		switch ( dataType )
		{
			case DataType.BOOLEAN_TYPE :
				return new BooleanRandomWriter( );
			case DataType.INTEGER_TYPE :
				return new IntegerRandomWriter( );
			case DataType.BYTES_TYPE:
				return new BytesRandomWriter( );
			case DataType.DOUBLE_TYPE :
				return new DoubleRandomWriter( );
			case DataType.STRING_TYPE :
				return new StringRandomWriter( );
			case DataType.DATE_TYPE :
				return new DateRandomWriter( );
			case DataType.BIGDECIMAL_TYPE :
				return new BigDecimalRandomWriter( );
			default :
				return null;
		}
	}

	public static IObjectReader getRandomReader( int dataType )
	{
		switch ( dataType )
		{
			case DataType.BOOLEAN_TYPE :
				return new BooleanRandomReader( );
			case DataType.INTEGER_TYPE :
				return new IntegerRandomReader( );
			case DataType.BYTES_TYPE:
				return new BytesRandomReader( );
			case DataType.DOUBLE_TYPE :
				return new DoubleRandomReader( );
			case DataType.STRING_TYPE :
				return new StringRandomReader( );
			case DataType.DATE_TYPE :
				return new DateRandomReader( );
			case DataType.BIGDECIMAL_TYPE :
				return new BigDecimalRandomReader( );
			default :
				return null;
		}
	}
}

class IntegerRandomWriter implements IObjectWriter
{

	public void write( BufferedRandomAccessFile file, Object obj )
			throws IOException
	{
		try
		{
			file.writeInt( ( (Integer) obj ).intValue( ) );
		}
		catch( ClassCastException ce )
		{
			file.writeInt( Integer.MAX_VALUE );
		}
	}
}

class BytesRandomWriter implements IObjectWriter
{

	public void write( BufferedRandomAccessFile file, Object obj )
			throws IOException
	{
		try
		{
			file.writeBytes( (Bytes) obj );
		}
		catch( ClassCastException ce )
		{
			file.writeInt( Integer.MAX_VALUE );
		}
	}
}

class BooleanRandomWriter implements IObjectWriter
{

	public void write( BufferedRandomAccessFile file, Object obj )
			throws IOException
	{
		try
		{
			file.writeBoolean( ( (Boolean) obj ).booleanValue( ) );
		}
		catch( ClassCastException ce )
		{
			ce.printStackTrace( );
		}
	}
}

class DoubleRandomWriter implements IObjectWriter
{

	public void write( BufferedRandomAccessFile file, Object obj )
			throws IOException
	{
		try
		{
			file.writeDouble( ( (Double) obj ).doubleValue( ) );
		}
		catch( ClassCastException ce )
		{
			ce.printStackTrace( );
		}
	}
}

class StringRandomWriter implements IObjectWriter
{

	public void write( BufferedRandomAccessFile file, Object obj )
			throws IOException
	{
		try
		{
			file.writeUTF( (String) obj );
		}
		catch( ClassCastException ce )
		{
			ce.printStackTrace( );
		}
	}
}

class DateRandomWriter implements IObjectWriter
{

	public void write( BufferedRandomAccessFile file, Object obj )
			throws IOException
	{
		try
		{
			file.writeDate( (Date) obj );
		}
		catch( ClassCastException ce )
		{
			ce.printStackTrace( );
		}
	}
}

class BigDecimalRandomWriter implements IObjectWriter
{

	public void write( BufferedRandomAccessFile file, Object obj )
			throws IOException
	{
		try
		{
			file.writeBigDecimal( (BigDecimal) obj );
		}
		catch( ClassCastException ce )
		{
			ce.printStackTrace( );
		}
	}
}

class IntegerRandomReader implements IObjectReader
{

	public Object read( BufferedRandomAccessFile file ) throws IOException
	{
		return new Integer( file.readInt( ) );
	}
}

class BytesRandomReader implements IObjectReader
{

	public Object read( BufferedRandomAccessFile file ) throws IOException
	{
		return file.readBytes( );
	}
}

class BooleanRandomReader implements IObjectReader
{

	public Object read( BufferedRandomAccessFile file ) throws IOException
	{
		return new Boolean( file.readBoolean( ) );
	}
}

class DoubleRandomReader implements IObjectReader
{

	public Object read( BufferedRandomAccessFile file ) throws IOException
	{
		return new Double( file.readDouble( ) );
	}
}

class StringRandomReader implements IObjectReader
{

	public Object read( BufferedRandomAccessFile file ) throws IOException
	{
		return file.readUTF( );
	}
}

class DateRandomReader implements IObjectReader
{

	public Object read( BufferedRandomAccessFile file ) throws IOException
	{
		return file.readDate( );
	}
}

class BigDecimalRandomReader implements IObjectReader
{

	public Object read( BufferedRandomAccessFile file ) throws IOException
	{
		return file.readBigDecimal( );
	}
}