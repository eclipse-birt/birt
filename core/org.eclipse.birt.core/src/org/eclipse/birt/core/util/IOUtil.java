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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
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

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.JavascriptEvalUtil;
import org.mozilla.javascript.IdScriptableObject;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.Scriptable;

/**
 * A util class to read or write primitive Java data type. Please notice, every
 * method has a stream which might be input stream or output stream as
 * parameters. This stream should be already added a buffered layer underlying
 * it.
 */
public class IOUtil
{

	public static final int INT_LENGTH = 4;
	
	public static final int RA_STREAM_BUFFER_LENGTH = 8192;
	
	public static final int MAX_NUMBER_OF_STREAM_BUFFER = 128;
	
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
	 * Assemble four bytes to an int value, make sure that the passed bytes
	 * length is 4.
	 * 
	 * @param bytes
	 * @return int value of bytes
	 */
	public final static int getInt( byte[] bytes )
	{
		assert bytes.length == 4;
		
		int ch1 = bytes[0];
		int ch2 = bytes[1];
		int ch3 = bytes[2];
		int ch4 = bytes[3];
		
		return ( ( ch1 << 24 ) + ( ch2 << 16 ) + ( ch3 << 8 ) + ( ch4 << 0 ) );
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
	 * Write a byte array to an output stream only with its raw content.
	 * 
	 * @param dos
	 * @param bytes,
	 *            it can not be null
	 * @throws IOException
	 */
	public final static void writeRawBytes( DataOutputStream dos, byte[] bytes )
			throws IOException
	{
		assert bytes != null;
		dos.write( bytes );
	}
	
	//------------for object read/write-------------------
	
	private static Map type2IndexMap;

	private static final int TYPE_NULL = 0;
	private static final int TYPE_INT = 1;
	private static final int TYPE_FLOAT = 2;
	private static final int TYPE_DOUBLE = 3;
	private static final int TYPE_BIG_DECIMAL = 4;
	private static final int TYPE_DATE = 5;
	private static final int TYPE_TIME = 6;
	private static final int TYPE_TIME_STAMP = 7;
	private static final int TYPE_BOOLEAN = 8;
	private static final int TYPE_STRING = 9;
	private static final int TYPE_BYTES = 10;
	private static final int TYPE_LIST = 11;
	private static final int TYPE_MAP = 12;
	private static final int TYPE_SERIALIZABLE = 13;
	
	private static final int TYPE_JSObject = 14;

	static
	{
		type2IndexMap = new HashMap( );
		type2IndexMap.put( Integer.class, new Integer( TYPE_INT ) );
		type2IndexMap.put( Float.class, new Integer( TYPE_FLOAT ) );
		type2IndexMap.put( Double.class, new Integer( TYPE_DOUBLE ) );
		type2IndexMap.put( BigDecimal.class, new Integer( TYPE_BIG_DECIMAL ) );
		type2IndexMap.put( Date.class, new Integer( TYPE_DATE ) );
		type2IndexMap.put( Time.class, new Integer( TYPE_TIME ) );
		type2IndexMap.put( Timestamp.class, new Integer( TYPE_TIME_STAMP ) );
		type2IndexMap.put( Boolean.class, new Integer( TYPE_BOOLEAN ) );
		type2IndexMap.put( String.class, new Integer( TYPE_STRING ) );
		type2IndexMap.put( byte[].class, new Integer( TYPE_BYTES ) );
		type2IndexMap.put( List.class, new Integer( TYPE_LIST ) );
		type2IndexMap.put( Map.class, new Integer( TYPE_MAP ) );
		type2IndexMap.put( Serializable.class, new Integer( TYPE_SERIALIZABLE ) );
		type2IndexMap.put( null, new Integer( TYPE_NULL ) );
		
		type2IndexMap.put( IdScriptableObject.class, new Integer( TYPE_JSObject ) );
	}

	/**
	 * from object class to its type index value
	 * 
	 * @param obValue
	 * @return
	 */
	private static int getTypeIndex( Object obValue )
	{
		if ( obValue == null )
			return TYPE_NULL;
		
		Integer indexOb = (Integer) type2IndexMap.get( obValue.getClass( ) );
		if ( indexOb == null )
		{
			if ( obValue instanceof Map )
			{
				return TYPE_MAP;
			}
			if ( obValue instanceof List )
			{
				return TYPE_LIST;
			}
			if ( obValue instanceof Scriptable )
			{
				return TYPE_JSObject;
			}
			if ( obValue instanceof Serializable )
			{
				return TYPE_SERIALIZABLE;
			}
			return -1;
		}
		return indexOb.intValue( );
	}

	/**
	 * Currently these data types are supported.
	 * 
	 * Integer Float Double BigDecimal Date Time Timestamp Boolean String byte[]
	 * List Map
	 * 
	 * @return
	 * @throws IOException
	 */
	public final static Object readObject( DataInputStream dis )
			throws IOException
	{
		// read data type from its index value
		int typeIndex = readInt( dis );
		// read real data
		Object obValue = null;
		switch ( typeIndex )
		{
			case TYPE_NULL :
				break;
			case TYPE_INT :
				obValue = new Integer( dis.readInt( ) );
				break;
			case TYPE_FLOAT :
				obValue = new Float( dis.readFloat( ) );
				break;
			case TYPE_DOUBLE :
				obValue = new Double( dis.readDouble( ) );
				break;
			case TYPE_BIG_DECIMAL :
				obValue = new BigDecimal( dis.readUTF( ) );
				break;
			case TYPE_DATE :
				obValue = new Date( dis.readLong( ) );
				break;
			case TYPE_TIME :
				obValue = new Time( dis.readLong( ) );
				break;
			case TYPE_TIME_STAMP :
				obValue = new Timestamp( dis.readLong( ) );
				break;
			case TYPE_BOOLEAN :
				obValue = new Boolean( dis.readBoolean( ) );
				break;
			case TYPE_STRING :
				obValue = dis.readUTF( );
				break;
			case TYPE_BYTES :
				int len = readInt( dis );
				byte[] bytes = new byte[len];
				if ( len > 0 )
					dis.readFully( bytes );
				obValue = bytes;
				break;
			case TYPE_LIST :
				obValue = readList( dis );
				break;
			case TYPE_MAP :
				obValue = readMap( dis );
				break;
			case TYPE_SERIALIZABLE :
				len = readInt( dis );
				if ( len != 0 )
				{
					bytes = new byte[len];
					dis.readFully( bytes );
					try
					{
						ObjectInputStream oo = new ObjectInputStream( new ByteArrayInputStream( bytes ) );
						obValue = oo.readObject( );
					}
					catch ( Exception ex )
					{
					}
				}
				break;
			case TYPE_JSObject :
				Object ob = IOUtil.readObject( dis );
				obValue = JavascriptEvalUtil.convertToJavascriptValue( ob );
				break;
			default :
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
		// write data type index first
		int typeIndex = getTypeIndex( obValue );
		if ( typeIndex == -1 )
		{
			writeInt( dos, TYPE_NULL );
			throw new IOException( "Data type of "
					+ obValue.getClass( ).toString( )
					+ " is not supported to be serialized" );
		}
		
		writeInt( dos, typeIndex );

		// write real data
		switch ( typeIndex )
		{
			case TYPE_NULL :
				break;
			case TYPE_INT :
				dos.writeInt( ( (Integer) obValue ).intValue( ) );
				break;
			case TYPE_FLOAT :
				dos.writeFloat( ( (Float) obValue ).floatValue( ) );
				break;
			case TYPE_DOUBLE :
				dos.writeDouble( ( (Double) obValue ).doubleValue( ) );
				break;
			case TYPE_BIG_DECIMAL :
				dos.writeUTF( ( (BigDecimal) obValue ).toString( ) );
				break;
			case TYPE_DATE :
				dos.writeLong( ( (Date) obValue ).getTime( ) );
				break;
			case TYPE_TIME :
				dos.writeLong( ( (Time) obValue ).getTime( ) );
				break;
			case TYPE_TIME_STAMP :
				dos.writeLong( ( (Timestamp) obValue ).getTime( ) );
				break;
			case TYPE_BOOLEAN :
				dos.writeBoolean( ( (Boolean) obValue ).booleanValue( ) );
				break;
			case TYPE_STRING :
				dos.writeUTF( obValue.toString( ) );
				break;
			case TYPE_BYTES :
				byte[] bytes = (byte[]) obValue;
				int length = bytes.length;
				writeInt( dos, length );
				if ( length > 0 )
					dos.write( bytes );
				break;
			case TYPE_LIST :
				writeList( dos, (List) obValue );
				break;
			case TYPE_MAP :
				writeMap( dos, (Map) obValue );
				break;
			case TYPE_SERIALIZABLE :
				bytes = null;
				try
				{
					ByteArrayOutputStream buff = new ByteArrayOutputStream( );
					ObjectOutputStream oo = new ObjectOutputStream( buff );
					oo.writeObject( obValue );
					oo.close( );
					bytes = buff.toByteArray( );
				}
				catch ( Exception ex )
				{
				}
				if ( bytes == null || bytes.length == 0 )
				{
					writeInt( dos, 0 );
				}
				else
				{
					writeInt( dos, bytes.length );
					dos.write( bytes );
				}
				break;
			case TYPE_JSObject :
				if (obValue instanceof IdScriptableObject)
				{
					IdScriptableObject jsObject = ( (IdScriptableObject) obValue );
					if ( jsObject.getClassName( ).equals( "Date" ) )
					{
						Date date = (Date) JavascriptEvalUtil.convertJavascriptValue( obValue );
						writeObject( dos, date );
					}
					else
					{
						// other data types are not supported yet.
						writeObject( dos, null );
					}
				}
				else if (obValue instanceof NativeJavaObject)
				{
					obValue= JavascriptEvalUtil.convertJavascriptValue( obValue );
					writeObject( dos, obValue );
				}
				else
				{
					// other data types are not supported yet.
					writeObject( dos, null );
				}
				break;
			default :
				assert false;
		}
	}

	/**
	 * Read a String from an input stream
	 * 
	 * @param inputStream
	 * @return an String
	 * @throws IOException
	 */
	public final static String readString( DataInputStream dis )
			throws IOException
	{
		if ( readInt( dis ) == TYPE_NULL )
			return null;

		return dis.readUTF( );
	}

	/**
	 * Write a String value to an output stream
	 * 
	 * @param outputStream
	 * @param str
	 * @throws IOException
	 */
	public final static void writeString( DataOutputStream dos, String str )
			throws IOException
	{
		if ( str == null )
		{
			writeInt( dos, TYPE_NULL );
			return;
		}
		else
		{
			writeInt( dos, TYPE_STRING );
		}

		dos.writeUTF( str );
	}

	/**
	 * Read a list from an input stream
	 * 
	 * @param dos
	 * @return
	 * @throws IOException
	 * @throws BirtException
	 */
	public final static byte[] readBytes( DataInputStream dis )
			throws IOException
	{
		// check null
		if ( readInt( dis ) == TYPE_NULL )
			return null;

		// read bytes size
		int size = readInt( dis );
		byte[] bytes = new byte[size];
		if ( size != 0 )
			dis.readFully( bytes );

		return bytes;
	}

	/**
	 * Write a bytes to an output stream
	 * 
	 * @param dos
	 * @param dataMap
	 * @throws IOException
	 * @throws BirtException
	 */
	public final static void writeBytes( DataOutputStream dos, byte[] bytes )
			throws IOException
	{
		// check null
		if ( bytes == null )
		{
			writeInt( dos, TYPE_NULL );
			return;
		}
		else
		{
			writeInt( dos, TYPE_BYTES );
		}

		// write byte size and its content
		int size = bytes.length;
		writeInt( dos, size );
		if ( size == 0 )
			return;
		dos.write( bytes );
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
		if ( readInt( dis ) == TYPE_NULL )
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
		if ( list == null )
		{
			writeInt( dos, TYPE_NULL );
			return;
		}
		else
		{
			writeInt( dos, TYPE_MAP );
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
		if ( readInt( dis ) == TYPE_NULL )
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
	 * @param map
	 * @throws IOException
	 * @throws BirtException
	 */
	public final static void writeMap( DataOutputStream dos, Map map )
			throws IOException
	{
		// check null
		if ( map == null )
		{
			writeInt( dos, TYPE_NULL );
			return;
		}
		else
		{
			writeInt( dos, TYPE_MAP );
		}
		
		// write map size
		int size = map.size( );
		writeInt( dos, size );
		if ( size == 0 )
			return;

		// write real data
		Set keySet = map.keySet( );
		Iterator it = keySet.iterator( );
		while ( it.hasNext( ) )
		{
			Object key = it.next( );
			Object value = map.get( key );
			writeObject( dos, key );
			writeObject( dos, value );
		}
	}

}