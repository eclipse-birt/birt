/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A util class to read or write primitive Java data type. Please notice, every
 * method has a stream which might be input stream or output stream as
 * parameters. This stream should be already added a buffered layer underlying
 * it.
 */
public class IOUtil
{	
	/**
	 * Read an int value from an input stream
	 * 
	 * @param inputStream
	 * @return int value
	 * @throws IOException
	 */
	public final static int readInt( InputStream inputStream )
			throws IOException
	{
		int ch1 = inputStream.read( );
		int ch2 = inputStream.read( );
		int ch3 = inputStream.read( );
		int ch4 = inputStream.read( );

		return ( ( ch1 << 24 ) + ( ch2 << 16 ) + ( ch3 << 8 ) + ( ch4 << 0 ) );
	}

	/**
	 * Write an int value to an output stream
	 * 
	 * @param outputStream
	 * @param value
	 * @throws IOException
	 */
	public final static void writeInt( OutputStream outputStream, int value )
			throws IOException
	{
		outputStream.write( ( value >>> 24 ) & 0xFF );
		outputStream.write( ( value >>> 16 ) & 0xFF );
		outputStream.write( ( value >>> 8 ) & 0xFF );
		outputStream.write( ( value >>> 0 ) & 0xFF );
	}
	
	/**
	 * Read a bool value from an input stream
	 * 
	 * @param inputStream
	 * @return boolean value
	 * @throws IOException
	 */
	public final static boolean readBool( InputStream inputStream )
			throws IOException
	{
		return inputStream.read( ) == 0 ? false : true;
	}

	/**
	 * Write a boolean value to an output stream
	 * 
	 * @param outputStream
	 * @param bool
	 * @throws IOException
	 */
	public final static void writeBool( OutputStream outputStream, boolean bool )
			throws IOException
	{
		outputStream.write( bool == false ? 0 : 1 );
	}
	
	/**
	 * Read a float value from an input stream
	 * 
	 * @param inputStream
	 * @return int value
	 * @throws IOException
	 */
	public final static float readFloat( DataInputStream inputStream )
			throws IOException
	{
		return inputStream.readFloat( );
	}

	/**
	 * Write a float value to an output stream
	 * 
	 * @param outputStream
	 * @param value
	 * @throws IOException
	 */
	public final static void writeFloat( DataOutputStream outputStream,
			float value ) throws IOException
	{
		outputStream.writeFloat( value );
	}

	/**
	 * Read a double value from an input stream
	 * 
	 * @param inputStream
	 * @return int value
	 * @throws IOException
	 */
	public final static double readDouble( DataInputStream inputStream )
			throws IOException
	{
		return inputStream.readDouble( );
	}

	/**
	 * Write a double value to an output stream
	 * 
	 * @param outputStream
	 * @param value
	 * @throws IOException
	 */
	public final static void writeDouble( DataOutputStream outputStream,
			double value ) throws IOException
	{
		outputStream.writeDouble( value );
	}

	/**
	 * Read a long value from an input stream
	 * 
	 * @param inputStream
	 * @return int value
	 * @throws IOException
	 */
	public final static long readLong( DataInputStream inputStream )
			throws IOException
	{
		return inputStream.readLong( );
	}

	/**
	 * Write a long value to an output stream
	 * 
	 * @param outputStream
	 * @param value
	 * @throws IOException
	 */
	public final static void writeLong( DataOutputStream outputStream,
			long value ) throws IOException
	{
		outputStream.writeLong( value );
	}

	/**
	 * Read a String from an input stream
	 * 
	 * @param inputStream
	 * @return an String
	 * @throws IOException
	 */
	public final static String readString( DataInputStream inputStream )
			throws IOException
	{
		return inputStream.read( ) == 0 ? null : inputStream.readUTF( );
	}

	/**
	 * Write a String value to an output stream
	 * 
	 * @param outputStream
	 * @param str
	 * @throws IOException
	 */
	public final static void writeString( DataOutputStream outputStream,
			String str ) throws IOException
	{
		outputStream.write( str == null ? 0 : 1 );

		if ( str != null )
			outputStream.writeUTF( str );
	}
	
	private static Map type2IndexMap;
	private static Map index2TypeMap;

	static
	{
		int i = 0;
		type2IndexMap = new HashMap( );
		type2IndexMap.put( Integer.class, new Integer( i++ ) );
		type2IndexMap.put( Float.class, new Integer( i++ ) );
		type2IndexMap.put( Double.class, new Integer( i++ ) );
		type2IndexMap.put( BigDecimal.class, new Integer( i++ ) );
		type2IndexMap.put( Date.class, new Integer( i++ ) );
		type2IndexMap.put( Time.class, new Integer( i++ ) );
		type2IndexMap.put( Timestamp.class, new Integer( i++ ) );
		type2IndexMap.put( Boolean.class, new Integer( i++ ) );
		type2IndexMap.put( String.class, new Integer( i++ ) );
		type2IndexMap.put( byte[].class, new Integer( i++ ) );
		type2IndexMap.put( List.class, new Integer( i++ ) );
		type2IndexMap.put( Map.class, new Integer( i++ ) );
		
		i = 0;
		index2TypeMap = new HashMap( );
		index2TypeMap.put( new Integer( i++ ), Integer.class );
		index2TypeMap.put( new Integer( i++ ), Float.class );
		index2TypeMap.put( new Integer( i++ ), Double.class );
		index2TypeMap.put( new Integer( i++ ), BigDecimal.class );
		index2TypeMap.put( new Integer( i++ ), Date.class );
		index2TypeMap.put( new Integer( i++ ), Time.class );
		index2TypeMap.put( new Integer( i++ ), Timestamp.class );
		index2TypeMap.put( new Integer( i++ ), Boolean.class );
		index2TypeMap.put( new Integer( i++ ), String.class );
		index2TypeMap.put( new Integer( i++ ), byte[].class );
		index2TypeMap.put( new Integer( i++ ), List.class );
		index2TypeMap.put( new Integer( i++ ), Map.class );
	}
	
	/**
	 * Currently these data types are supported.
	 * 
	 * Integer
	 * Float
	 * Double
	 * BigDecimal
	 * Date
	 * Time
	 * Timestamp
	 * Boolean
	 * String
	 * byte[]
	 * List
	 * Map
	 * 
	 * @return 
	 * @throws IOException 
	 */
	public final static Object readObject( DataInputStream dis )
			throws IOException
	{
		// check whether it is null
		if ( readBool( dis ) == false )
			return null;

		// read data type from its index value
		int indexValue = readInt( dis );
		Object type = index2TypeMap.get( new Integer( indexValue ) );
		assert type != null;
		
		// read real data
		Object obValue = null;		
		if ( type.equals( Integer.class ) )
		{
			obValue = new Integer( dis.readInt( ) );
		}
		else if ( type.equals( Float.class ) )
		{
			obValue = new Float( dis.readFloat( ) );
		}
		else if ( type.equals( Double.class ) )
		{
			obValue = new Double( dis.readDouble( ) );
		}
		else if ( type.equals( BigDecimal.class ) )
		{
			obValue = new BigDecimal( dis.readUTF( ) );
		}
		else if ( type.equals( Date.class ) )
		{
			obValue = new Date( dis.readLong( ) );
		}
		else if ( type.equals( Time.class ) )
		{
			obValue = new Time( dis.readLong( ) );
		}
		else if ( type.equals( Timestamp.class ) )
		{
			obValue = new Timestamp( dis.readLong( ) );
		}
		else if ( type.equals( Boolean.class ) )
		{
			obValue = new Boolean( dis.readBoolean( ) );
		}
		else if ( type.equals( String.class ) )
		{
			obValue = dis.readUTF( );
		}
		else if ( type.equals( byte[].class ) )
		{
			int len = readInt( dis );
			if ( len == 0 )
			{
				obValue = null;
			}
			else
			{
				byte[] bytes = new byte[len];
				dis.read( bytes );
				obValue = bytes;
			}
		}
		else if ( type.equals( List.class ) )
		{
			obValue = readList( dis );
		}
		else if ( type.equals( Map.class ) )
		{
			obValue = readMap( dis );
		}
		else
		{
			assert false;
		}
		
		return obValue;
	} 
	
	/**
	 * When obValue is not supported te be serialized, an IOException will be
	 * thrown.
	 * 
	 * @param dos
	 * @param obValue
	 * @throws IOException
	 */
	public final static void writeObject( DataOutputStream dos, Object obValue )
			throws IOException
	{
		// check whether it is null
		if ( obValue == null )
		{
			writeBool( dos, false );
			return;
		}
		else
		{
			writeBool( dos, true );
		}

		// write data type index first
		Object indexOb = type2IndexMap.get( obValue.getClass( ) );
		if ( indexOb == null )
		{
			throw new IOException( "Data type of "
					+ obValue.getClass( ).toString( )
					+ " is not supported to be serialized" );
		}
		int indexValue = ( (Integer) indexOb ).intValue( );
		writeInt( dos, indexValue );
		
		// write real data
		if ( obValue instanceof Integer )
		{
			dos.writeInt( ( (Integer) obValue ).intValue( ) );
		}
		else if ( obValue instanceof Float )
		{
			dos.writeFloat( ( (Float) obValue ).floatValue( ) );
		}
		else if ( obValue instanceof Double )
		{
			dos.writeDouble( ( (Double) obValue ).doubleValue( ) );
		}
		else if ( obValue instanceof BigDecimal )
		{
			dos.writeUTF( ( (BigDecimal) obValue ).toString( ) );
		}
		else if ( obValue instanceof Date )
		{
			dos.writeLong( ( (Date) obValue ).getTime( ) );
		}
		else if ( obValue instanceof Time )
		{
			dos.writeLong( ( (Time) obValue ).getTime( ) );
		}
		else if ( obValue instanceof Timestamp )
		{
			dos.writeLong( ( (Timestamp) obValue ).getTime( ) );
		}
		else if ( obValue instanceof Boolean )
		{
			dos.writeBoolean( ( (Boolean) obValue ).booleanValue( ) );
		}
		else if ( obValue instanceof String )
		{
			dos.writeUTF( obValue.toString( ) );
		}
		else if ( obValue instanceof byte[] )
		{
			byte[] bytes = (byte[]) obValue;
			if ( bytes == null || bytes.length == 0 )
			{
				writeInt( dos, 0 );
			}
			else
			{
				writeInt( dos, bytes.length );
				dos.write( (byte[]) obValue );
			}
		}
		else if ( obValue instanceof List )
		{
			writeList( dos, (List) obValue );
		}
		else if ( obValue instanceof Map )
		{
			writeMap( dos, (Map) obValue );
		}
		else
		{
			assert false;
		}
	}

	/**
	 * Read a list from an input stream
	 * 
	 * @param dos
	 * @return
	 * @throws IOException
	 * @throws BirtException
	 */
	public final static List readList( DataInputStream dis ) throws IOException
	{
		// check null
		if ( readBool( dis ) == false )
			return null;

		// read map size
		List dataList = new ArrayList( );
		int size = readInt( dis );
		if ( size == 0 )
			return dataList;

		// write real data
		for ( int i = 0; i < size; i++ )
			dataList.add( readObject( dis ) );

		return dataList;
	}
	
	/**
	 * Write a list to an output stream
	 * 
	 * @param dos
	 * @param dataMap
	 * @throws IOException 
	 * @throws BirtException 
	 */
	public final static void writeList( DataOutputStream dos, List list )
			throws IOException
	{
		// check null
		if ( list == null )
		{
			writeBool( dos, false );
			return;
		}
		else
		{
			writeBool( dos, true );
		}

		// write map size
		int size = list.size( );
		writeInt( dos, size );
		if ( size == 0 )
			return;

		// write real data
		for ( int i = 0; i < size; i++ )
			writeObject( dos, list.get( i ) );
	}
	
	/**
	 * Read a Map from an input stream
	 * 
	 * @param dos
	 * @return
	 * @throws IOException
	 * @throws BirtException
	 */
	public final static Map readMap( DataInputStream dis ) throws IOException
	{
		// check null
		if ( readBool( dis ) == false )
			return null;

		// read map size
		Map dataMap = new HashMap( );
		int size = readInt( dis );
		if ( size == 0 )
			return dataMap;

		// write real data
		for ( int i = 0; i < size; i++ )
		{
			Object key = readObject( dis );
			Object value = readObject( dis );
			dataMap.put( key, value );
		}

		return dataMap;
	}
	
	/**
	 * Write a Map to an output stream
	 * 
	 * @param dos
	 * @param dataMap
	 * @throws IOException 
	 * @throws BirtException 
	 */
	public final static void writeMap( DataOutputStream dos, Map dataMap )
			throws IOException
	{
		// check null
		if ( dataMap == null )
		{
			writeBool( dos, false );
			return;
		}
		else
		{
			writeBool( dos, true );
		}
		
		// write map size
		int size = dataMap.size( );
		writeInt( dos, size );
		if ( size == 0 )
			return;
		
		// write real data
		Set keySet = dataMap.keySet( );
		Iterator it = keySet.iterator( );
		while ( it.hasNext( ) )
		{
			Object key = it.next( );
			Object value = dataMap.get( key );
			writeObject( dos, key );
			writeObject( dos, value );
		}
	}
	
}