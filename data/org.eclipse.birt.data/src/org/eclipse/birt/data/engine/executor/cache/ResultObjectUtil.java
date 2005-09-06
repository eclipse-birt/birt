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
import java.util.HashSet;
import java.util.Set;

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

	// object index for read and write
	private int readIndex;
	private int writeIndex;
	
	// store the null object information with its row index and
	// column index
	private Set nullObjectSet;
	private Set tempNullObjectSet;
	
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
	static ResultObjectUtil newInstance( IResultClass rsMetaData )
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
		
		instance.readIndex = 0;
		instance.writeIndex = 0;
		instance.nullObjectSet = new HashSet( );
		
		return instance;
	}

	/**
	 * Contruction, private 
	 */
	private ResultObjectUtil( )
	{
	}

	/**
	 * Reset read index to 0. When reading data from start, this method must be
	 * called first.
	 */
	void startNewRead( )
	{
		readIndex = 0;
	}

	/**
	 * Reset write index to 0. This method is closely coupled with current
	 * implementation of treating Null object. Null object is not suitable to
	 * serialize or deserialize, since there is no proper way to handle it.
	 * 
	 * The approach adoped here is to keep a Set in memory which stores the Null
	 * object data with its rowIndex and colIndex. In serialization procedure,
	 * first checking whether the data is Null, if yes, the index info of the
	 * data will be stored in the nullObjectSet and the serialization action
	 * will be skipped. In de-serialization procedure, first looking for Null
	 * object information from nullObjectSet, if it can be found, then directly
	 * give it the Null value and de-serialization action will be skipped.
	 * 
	 * Below is accurate analysis of algorithm for read/write in export. When
	 * there is no sort, data will not be read but be written once. So this
	 * method will not be called. When there is sort, data will be read once and
	 * be written twice. So in the twic write, this method needs to be called
	 * explicitly.
	 */
	void startSecondWrite( )
	{
		writeIndex = 0;
		tempNullObjectSet = nullObjectSet;
		nullObjectSet = new HashSet( );
	}
	
	/**
	 * End second write. Do a clean job.
	 */
	void endSecondWrite( )
	{
		tempNullObjectSet = null;
	}	
	
	/**
	 * New a instance of ResultObject according to the parameter of object array
	 * plus the metadata stored before.
	 * 
	 * @param ob
	 * @return RowData
	 */
	ResultObject newResultObject( Object[] rowData )
	{
		return new ResultObject( rsMetaData, rowData );
	}

	/**
	 * Deserialze result object array from input stream.
	 * 
	 * Datatype Corresponds to executor#setDataType
	 * 
	 * One point needs to be noticed that the read and write procedure is strictly
	 * be conversed. 
	 * 
	 * @param br
	 *            input stream
	 * @param length
	 *            how many objects needs to be read
	 * @return result object array
	 * @throws IOException
	 */
	IResultObject[] readData( BufferedInputStream bis, int length )
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
				if ( isNullObject( readIndex, j ) )
					obs[j] = null;	
				else if ( typeArray[j].equals( Integer.class ) )
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
			
			readIndex++;
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
	void writeData( BufferedOutputStream bos,
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
				}
				catch ( DataException e )
				{
					// should never get here since the index value is always
					// correct
				}
				
				if ( fieldValue == null )
					putNullObject( writeIndex, j );				
				else if ( typeArray[j].equals( Integer.class ) )
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
			
			writeIndex++;
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
	
	/**
	 * @param rowIndex
	 * @param colIndex
	 */
	private void putNullObject( int rowIndex, int colIndex )
	{
		DataIndex index = new DataIndex( rowIndex, colIndex );
		nullObjectSet.add( index );
	}

	/**
	 * @param rowIndex
	 * @param colIndex
	 * @return true, Null object
	 */
	private boolean isNullObject( int rowIndex, int colIndex )
	{
		DataIndex index = new DataIndex( rowIndex, colIndex );
		if ( tempNullObjectSet != null )
			return tempNullObjectSet.contains( index );
		else
			return nullObjectSet.contains( index );
	}

	/**
	 * Save the row and col index information
	 */
	private class DataIndex
	{
		private int rowIndex, colIndex;

		/**
		 * @param rowIndex
		 * @param colIndex
		 */
		DataIndex( int rowIndex, int colIndex )
		{
			this.rowIndex = rowIndex;
			this.colIndex = colIndex;
		}

		/*
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		public boolean equals( Object obj )
		{
			DataIndex index = (DataIndex) obj;
			return rowIndex == index.rowIndex && colIndex == index.colIndex;
		}

		/*
		 * @see java.lang.Object#hashCode()
		 */
		public int hashCode( )
		{
			return 0;
		}
	}
	
}