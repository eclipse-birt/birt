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

package org.eclipse.birt.data.engine.executor.cache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.ResultObject;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultObject;

/**
 * Utility class for ResultObject for serialize and deserialize. Since available
 * memory is bound in a specific case, data needs to be export to file and
 * import from it. Therefore, the operation of serialization and deserialization
 * is necessary and important.
 */
class ResultObjectUtil
{
	// column count of current processed table
	private int columnCount;

	// data type array of result set
	private Class[] typeArray;

	// meta data of result set
	private IResultClass rsMetaData;

	// how many bytes is used for int value
	private static int FixedBytesLengOfInt = 8;
	
	/**
	 * In serializaing data to file and deserializing it from file, metadata
	 * information is necessary to know which data type a column is, and then
	 * proper read/write method will be called. This method must be called at
	 * first when any actual read/write action is taken. Since multi thread
	 * might call DtE at the same time, an instance needs to be new to
	 * correspond to different metadata.
	 * 
	 * @param rsMetaData
	 * @throws DataException
	 */
	public static ResultObjectUtil newInstance( IResultClass rsMetaData )
	{
		ResultObjectUtil instance = new ResultObjectUtil( );
		int length = rsMetaData.getFieldCount( );
		instance.typeArray = new Class[length];
		for ( int i = 0; i < length; i++ )
		{
			try
			{
				instance.typeArray[i] = rsMetaData.getFieldValueClass( i + 1 );
			}
			catch ( DataException e )
			{
				// the index will be always valid
			}
		}

		instance.columnCount = rsMetaData.getFieldCount( );
		instance.rsMetaData = rsMetaData;
		return instance;
	}

	/**
	 * Use a single instance pattern.
	 * 
	 * @param rsMetaData
	 * @throws DataException
	 */
	private ResultObjectUtil( )
	{
	}

	/**
	 * New a instance of ResultObject according to the parameter of object array
	 * plus the metadata stored before.
	 * 
	 * @param ob
	 * @return RowData
	 */
	public ResultObject newResultObject( Object[] rowData )
	{
		return new ResultObject( rsMetaData, rowData );
	}

	/**
	 * Deserialze result object array from input stream.
	 * 
	 * Serialize datatype is follwing: UNKNOWN_TYPE (String) ANY_TYPE (String)
	 * BOOLEAN_TYPE (boolean) INTEGER_TYPE (int) DOUBLE_TYPE (double)
	 * DECIMAL_TYPE (String) STRING_TYPE (String) DATE_TYPE (long) BLOB_TYPE
	 * (not supported yet)
	 * 
	 * Timestamp (from ODA)
	 * 
	 * Corresponds to executor#setDataType
	 * 
	 * A very difficult problem is how to process NULL.
	 * 
	 * @param br
	 *            input stream
	 * @param length
	 *            how many objects needs to be read
	 * @return result object array
	 * @throws IOException
	 */
	public IResultObject[] readData( BufferedInputStream bis, int length )
			throws IOException
	{
		ResultObject[] rowDatas = new ResultObject[length];

		byte[] intBytes = new byte[FixedBytesLengOfInt];
		int rowDataLen;
		byte[] rowDataBytes;

		ByteArrayInputStream bais;
		ObjectInputStream ois;

		for ( int i = 0; i < length; i++ )
		{
			bis.read( intBytes );
			rowDataLen = getIntOfBytes( intBytes );
			rowDataBytes = new byte[rowDataLen];
			bis.read( rowDataBytes );
			bais = new ByteArrayInputStream( rowDataBytes );
			ois = new ObjectInputStream( bais );

			Object[] obs = new Object[columnCount];
			for ( int j = 0; j < columnCount; j++ )
			{
				if ( typeArray[j].equals( Integer.class ) )
					obs[j] = new Integer( ois.readInt( ) );
				else if ( typeArray[j].equals( Double.class ) )
					obs[j] = new Double( ois.readDouble( ) );
				else if ( typeArray[j].equals( BigDecimal.class ) )
					obs[j] = new BigDecimal( ois.readUTF( ) );
				else if ( typeArray[j].equals( Date.class ) )
					obs[j] = new Date( ois.readLong( ) );
				else if ( typeArray[j].equals( Time.class ) )
					obs[j] = new Time( ois.readLong( ) );
				else if ( typeArray[j].equals( Timestamp.class ) )
					obs[j] = new Timestamp( ois.readLong( ) );
				else if ( typeArray[j].equals( Boolean.class ) )
					obs[j] = new Boolean( ois.readBoolean( ) );
				else if ( typeArray[j].equals( String.class )
						|| typeArray[j].equals( DataType.getClass( DataType.ANY_TYPE ) ) )
					obs[j] = ois.readUTF( );
			}
			rowDatas[i] = newResultObject( obs );

			rowDataBytes = null;
			bais = null;
			ois = null;
		}

		return rowDatas;
	}

	/**
	 * Serialze result object array to file
	 * 
	 * @param bos output stream
	 * @param resultObjects result objects needs to be deserialized
	 * @param length how many objects to be deserialized
	 * @throws IOException
	 * @throws DataException
	 * @throws Exception
	 */
	public void writeData( BufferedOutputStream bos,
			IResultObject[] resultObjects, int length ) throws IOException
	{
		byte[] rowsDataByte;

		ByteArrayOutputStream baos;
		ObjectOutputStream oos;
		
		for ( int i = 0; i < length; i++ )
		{
			baos = new ByteArrayOutputStream( );
			oos = new ObjectOutputStream( baos );
			for ( int j = 0; j < columnCount; j++ )
			{
				Object fieldValue = null;
				try
				{
					fieldValue = resultObjects[i].getFieldValue( j + 1 );
					if ( fieldValue == null )
						throw new IllegalArgumentException( "Currently null value is not supported in large data set" );
				}
				catch ( DataException e )
				{
					// should never get here since the index value is always
					// correct
				}
				
				if ( typeArray[j].equals( Integer.class ) )
					oos.writeInt( ( (Integer) fieldValue ).intValue( ) );
				else if ( typeArray[j].equals( Double.class ) )
					oos.writeDouble( ( (Double) fieldValue ).doubleValue( ) );
				else if ( typeArray[j].equals( BigDecimal.class ) )
					oos.writeUTF( ( (BigDecimal) fieldValue ).toString( ) );
				else if ( typeArray[j].equals( Date.class ) )
					oos.writeLong( ( (Date) fieldValue ).getTime( ) );
				else if ( typeArray[j].equals( Time.class ) )
					oos.writeLong( ( (Time) fieldValue ).getTime( ) );
				else if ( typeArray[j].equals( Timestamp.class ) )
					oos.writeLong( ( (Timestamp) fieldValue ).getTime( ) );
				else if ( typeArray[j].equals( Boolean.class ) )
					oos.writeBoolean( ( (Boolean) fieldValue ).booleanValue( ) );
				else if ( typeArray[j].equals( String.class )
						|| typeArray[j].equals( DataType.getClass( DataType.ANY_TYPE ) ) )
					oos.writeUTF( fieldValue.toString( ) );
			}
			oos.flush( );

			rowsDataByte = baos.toByteArray( );
			bos.write( getBytesOfInt( rowsDataByte.length ) );
			bos.write( rowsDataByte );

			rowsDataByte = null;
			baos = null;
			oos = null;
		}
	}

	/**
	 * Convert int to byte array with fixed byte array length
	 * 
	 * @param intValue
	 * @return bytes array of an int value
	 */
	private static byte[] getBytesOfInt( int intValue )
	{
		byte[] byteOfInt = new byte[FixedBytesLengOfInt];
		for ( int i = 0; i < 8; i++ )
			byteOfInt[i] = 30;

		byte[] tempByte = Integer.toString( intValue ).getBytes( );
		System.arraycopy( tempByte, 0, byteOfInt, 0, tempByte.length );
		return byteOfInt;
	}

	/**
	 * Convert byte array to its corresponding int value
	 * 
	 * @param byteOfInt
	 * @return int value converted from bytes
	 */
	private static int getIntOfBytes( byte[] byteOfInt )
	{
		return Integer.parseInt( new String( byteOfInt ).trim( ) );
	}
	
}