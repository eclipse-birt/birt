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
import java.math.BigDecimal;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

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
			case DataType.BLOB_TYPE :
				return new BlobRandomWriter( );
			case DataType.BIGDECIMAL_TYPE :
				return new BigDecimalRandomWriter( );
			case DataType.SQL_DATE_TYPE :
				return new DateRandomWriter( );
			case DataType.SQL_TIME_TYPE :
				return new DateRandomWriter( );
			case DataType.JAVA_OBJECT_TYPE :
				return new ObjectRandomWriter( );
			default :
				return new ObjectRandomWriter( );
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
				return new DateTimeRandomReader( );
			case DataType.BLOB_TYPE :
				return new BlobRandomReader( );
			case DataType.BIGDECIMAL_TYPE :
				return new BigDecimalRandomReader( );
			case DataType.SQL_DATE_TYPE :
				return new DateRandomReader( );
			case DataType.SQL_TIME_TYPE :
				return new TimeRandomReader( );
			case DataType.JAVA_OBJECT_TYPE :
				return new ObjectRandomReader( );
			default :
				return new ObjectRandomReader( );
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

class BlobRandomWriter implements IObjectWriter
{

	public void write( BufferedRandomAccessFile file, Object obj )
			throws IOException
	{
		try
		{
			byte[] bytesValue = (byte[]) obj;
			file.writeBytes( new Bytes( bytesValue ) );
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
	private static Logger logger = Logger.getLogger( BooleanRandomWriter.class.getName( ) );

	public void write( BufferedRandomAccessFile file, Object obj )
			throws IOException
	{
		try
		{
			file.writeBoolean( ( (Boolean) obj ).booleanValue( ) );
		}
		catch( ClassCastException ce )
		{
			logger.log( Level.FINE, ce.getMessage( ), ce );
		}
	}
}

class DoubleRandomWriter implements IObjectWriter
{
	private static Logger logger = Logger.getLogger( DoubleRandomWriter.class.getName( ) );

	public void write( BufferedRandomAccessFile file, Object obj )
			throws IOException
	{
		try
		{
			file.writeDouble( ( (Double) obj ).doubleValue( ) );
		}
		catch( ClassCastException ce )
		{
			logger.log( Level.FINE, ce.getMessage( ), ce );
		}
	}
}

class StringRandomWriter implements IObjectWriter
{
	private static Logger logger = Logger.getLogger( StringRandomWriter.class.getName( ) );

	public void write( BufferedRandomAccessFile file, Object obj )
			throws IOException
	{
		try
		{
			file.writeUTF( (String) obj );
		}
		catch( ClassCastException ce )
		{
			logger.log( Level.FINE, ce.getMessage( ), ce );
		}
	}
}

class DateRandomWriter implements IObjectWriter
{
	private static Logger logger = Logger.getLogger( DateRandomWriter.class.getName( ) );

	public void write( BufferedRandomAccessFile file, Object obj )
			throws IOException
	{
		try
		{
			file.writeDate( (Date) obj );
		}
		catch( ClassCastException ce )
		{
			logger.log( Level.FINE, ce.getMessage( ), ce );
		}
	}
}


class BigDecimalRandomWriter implements IObjectWriter
{
	private static Logger logger = Logger.getLogger( BigDecimalRandomWriter.class.getName( ) );

	public void write( BufferedRandomAccessFile file, Object obj )
			throws IOException
	{
		try
		{
			if (obj instanceof BigDecimal) 
			{
				file.writeBigDecimal( ( BigDecimal ) obj );
			}
			else 
			{
				file.writeBigDecimal( BigDecimal.valueOf(((Number) obj ).doubleValue()));
			}
		}
		catch( ClassCastException ce )
		{
			logger.log( Level.FINE, ce.getMessage( ), ce );
		}
	}
}

class ObjectRandomWriter implements IObjectWriter
{
	private static Logger logger = Logger.getLogger( BigDecimalRandomWriter.class.getName( ) );

	public void write( BufferedRandomAccessFile file, Object obj )
			throws IOException
	{
		try
		{
			file.writeObject( obj );
		}
		catch( ClassCastException ce )
		{
			logger.log( Level.FINE, ce.getMessage( ), ce );
		}
	}
}

class IntegerRandomReader implements IObjectReader
{

	public Object read( BufferedRandomAccessFile file ) throws IOException
	{
		return Integer.valueOf( file.readInt( ) );
	}
}

class BlobRandomReader implements IObjectReader
{

	public Object read( BufferedRandomAccessFile file ) throws IOException
	{
		return file.readBytes( ).bytesValue( );
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
		return Boolean.valueOf( file.readBoolean( ) );
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

class DateTimeRandomReader implements IObjectReader
{

	public Object read( BufferedRandomAccessFile file ) throws IOException
	{
		return file.readDate( );
	}
}

class DateRandomReader implements IObjectReader
{

	public Object read( BufferedRandomAccessFile file ) throws IOException
	{
		Date date = file.readDate( );
		if( date == null )
			return null;
		return new java.sql.Date( date.getTime() );
	}
}

class TimeRandomReader implements IObjectReader
{

	public Object read( BufferedRandomAccessFile file ) throws IOException
	{
		Date time = file.readDate( );
		if( time == null )
			return null;
		return new java.sql.Time( time.getTime() );
	}
}

class BigDecimalRandomReader implements IObjectReader
{

	public Object read( BufferedRandomAccessFile file ) throws IOException
	{
		return file.readBigDecimal( );
	}
}

class ObjectRandomReader implements IObjectReader
{
	public Object read( BufferedRandomAccessFile file ) throws IOException
	{
		return file.readObject( );
	}
}

