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

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.ResultObject;
import org.eclipse.birt.data.engine.expression.ExpressionCompilerUtil;
import org.eclipse.birt.data.engine.impl.document.stream.VersionManager;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultObject;

/**
 * 
 */
public class ResultSetUtil
{	 	
	private static final int NULL_INDICATOR = 1;
	private static final int EXCEPTION_INDICATOR = 2;
	private static final int VALID_VALUE_INDICATOR = 0;
	//----------------------service for result object save and load--------------
	
	/**
	 * Write the result object value if it is used in column binding map
	 * 
	 * @param dos
	 * @param resultObject
	 * @param nameSet
	 * @throws DataException
	 * @throws IOException
	 */
	public static int writeResultObject( DataOutputStream dos,
			IResultObject resultObject, int count, Set nameSet )
			throws DataException, IOException
	{
		if ( resultObject.getResultClass( ) == null )
			return 0;
		
		ByteArrayOutputStream tempBaos = new ByteArrayOutputStream( );
		BufferedOutputStream tempBos = new BufferedOutputStream( tempBaos );
		DataOutputStream tempDos = new DataOutputStream( tempBos );
		
		byte[] typeIndicator = new byte[count];
		for ( int i = 1; i <= count; i++ )
		{
			if ( nameSet != null
					&& ( nameSet.contains( resultObject.getResultClass( )
							.getFieldName( i ) ) || nameSet.contains( resultObject.getResultClass( )
							.getFieldAlias( i ) ) ) )
			{
				Class dataType = resultObject.getResultClass( ).getFieldValueClass( i );
				Object fieldValue = resultObject.getFieldValue( i );
				
				if( fieldValue == null || fieldValue instanceof Exception )
				{
					if( fieldValue == null )
						typeIndicator[i-1] = NULL_INDICATOR;
					else
						typeIndicator[i-1] = EXCEPTION_INDICATOR;
				}
				else if( dataType == Integer.class && fieldValue instanceof Integer )
				{
					IOUtil.writeInt( tempDos, ((Integer)resultObject.getFieldValue( i )).intValue( ) );
				}
				else if( dataType == Double.class && fieldValue instanceof Double )
				{
					IOUtil.writeDouble( tempDos, ((Double)fieldValue).doubleValue( ) );
				}	
				else if( dataType == String.class && fieldValue instanceof String )
				{
					IOUtil.writeString( tempDos, fieldValue.toString( ));
				}	
				else if ( dataType == BigDecimal.class )
				{
					IOUtil.writeObject( tempDos, fieldValue );
				}
				else if ( dataType == java.util.Date.class && fieldValue instanceof java.util.Date )
				{
					IOUtil.writeLong( tempDos, ((java.util.Date)fieldValue).getTime( ) );
				}   
				else if ( dataType == java.sql.Date.class && fieldValue instanceof java.sql.Date )
				{
					IOUtil.writeLong( tempDos, ((java.sql.Date)fieldValue).getTime( ) );
				}
				else if ( dataType == Time.class && fieldValue instanceof Time )
				{
					IOUtil.writeLong( tempDos, ((Time)fieldValue).getTime( ) );
				}	
				else if ( dataType == Timestamp.class && fieldValue instanceof Timestamp )
				{
					IOUtil.writeLong( tempDos, ((Timestamp)fieldValue).getTime( ) );
				}
				else if ( dataType == Boolean.class && fieldValue instanceof Boolean )
				{
					IOUtil.writeBool( tempDos, ((Boolean)fieldValue).booleanValue( ) );
				}
				else 
				{
					IOUtil.writeObject( tempDos, resultObject.getFieldValue( i ) );
				}
			}
		}
		
		tempDos.flush( );
		tempBos.flush( );
		tempBaos.flush( );

		ByteArrayOutputStream tempLeadingBos = new ByteArrayOutputStream( );
		DataOutputStream tempLeadingDos = new DataOutputStream( new BufferedOutputStream( tempLeadingBos ) );
		byte[] leadingBytes = createLeadingBytes( typeIndicator );
		IOUtil.writeBytes( tempLeadingDos, leadingBytes );
		tempLeadingDos.flush( );
		
		byte[] bytes1 = tempLeadingBos.toByteArray( );
		byte[] bytes2 = tempBaos.toByteArray( );
		
		int rowBytes = bytes1.length + bytes2.length;
		
		IOUtil.writeRawBytes( dos, bytes1 );
		IOUtil.writeRawBytes( dos, bytes2 );

		tempBaos = null;
		tempBos = null;
		tempDos = null;
		
		tempLeadingBos = null;
		tempLeadingDos = null;
		return rowBytes;
	}

	/**
	 * @param dis
	 * @param rsMeta
	 * @param count
	 * @return
	 * @throws IOException
	 * @throws DataException 
	 */
	public static IResultObject readResultObject( DataInputStream dis,
			IResultClass rsMeta, int count, int version ) throws IOException, DataException
	{
		Object[] obs = new Object[count];

		if ( version < VersionManager.VERSION_2_2_1_3 )
		{
			for ( int i = 0; i < count; i++ )
				obs[i] = IOUtil.readObject( dis );
		}
		else
		{
			byte[] control = readLeadingBytes( IOUtil.readBytes( dis ), rsMeta.getFieldCount( ));
			for( int i = 0; i < control.length; i++ )
			{
				if( control[i] == VALID_VALUE_INDICATOR )
				{
					Class dataType = rsMeta.getFieldValueClass( i+1 );
					
					if( dataType == Integer.class )
					{
						obs[i] = new Integer( IOUtil.readInt( dis ));
					}
					else if( dataType == Double.class )
					{
						obs[i] = new Double( IOUtil.readDouble( dis ));
					}	
					else if( dataType == String.class )
					{
						obs[i] = IOUtil.readString( dis );
					}	
					else if ( dataType == BigDecimal.class )
					{
						obs[i] = IOUtil.readObject( dis );
					}
					else if ( dataType == java.util.Date.class )
					{
						obs[i] = new java.util.Date(IOUtil.readLong( dis ));
					}   
					else if ( dataType == java.sql.Date.class )
					{
						obs[i] = new java.sql.Date( IOUtil.readLong( dis ));
					}
					else if ( dataType == Time.class )
					{
						obs[i] = new Time( IOUtil.readLong( dis ));
					}	
					else if ( dataType == Timestamp.class )
					{
						obs[i] = new Timestamp( IOUtil.readLong( dis ));
					}
					else if ( dataType == Boolean.class )
					{
						obs[i] = new Boolean( IOUtil.readBool( dis ));
					}
					else 
					{
						obs[i] = IOUtil.readObject( dis );
					}
				}
				else if ( control[i] == NULL_INDICATOR || control[i] == EXCEPTION_INDICATOR )
				{
					obs[i] = null;
				}
			}
		}
		return new ResultObject( rsMeta, obs );
	}
	
	/**
	 * Get result set column name collection from column binding map
	 * 
	 * @param cacheRequestMap
	 * @return
	 * @throws DataException
	 */
	public static Set getRsColumnRequestMap( Map cacheRequestMap )
			throws DataException
	{
		Set resultSetNameSet = new HashSet( );
		if ( cacheRequestMap != null )
		{
			Set exprSet = cacheRequestMap.entrySet( );
			Iterator iter = exprSet.iterator( );
			List dataSetColumnList = null;
			while ( iter.hasNext( ) )
			{
				Entry entry = (Entry) iter.next( );
				dataSetColumnList = entry.getValue( ) == null
						? null
						: ExpressionCompilerUtil.extractDataSetColumnExpression( ((IBinding)entry.getValue( )).getExpression( ) );
				if ( dataSetColumnList != null )
				{
					resultSetNameSet.addAll( dataSetColumnList );
				}
			}
		}
		return resultSetNameSet;
	}
	
	public static byte[] readLeadingBytes( byte[] source, int length )
	{
		byte b = source[0];
		byte b1 = (byte) ( (byte) ( b & -128 ) == 0?0:1 );
		byte b2 = (byte) ( (byte) ( b & 64 ) == 0?0:1 );
		if ( b1 == 0 && b2 == 0 )
			return new byte[length];

		byte[] result = new byte[length];
		if ( ( b1 == 0 && b2 == 1 ) || ( b1 == 1 && b2 == 0 ) )
		{
			byte typeIndicator = (byte) ( ( b1 == 0 ) ? NULL_INDICATOR
					: EXCEPTION_INDICATOR );
			for ( int i = 0; i < source.length; i++ )
			{
				int startOffset = 0;
				if ( i == 0 )
					startOffset = 2;
				int start = i == 0 ? 0 : ( i * 8 - 2 );
				int offset = 0;
				for ( offset = 0; offset < 8
						&& startOffset < 8 && start + offset < length; offset++ )
				{
					result[start + offset] = (byte) ( ( source[i] & ( 1 << 7 - startOffset ) ) > 0
							? typeIndicator : VALID_VALUE_INDICATOR );
					startOffset++;
				}
			}
		}
		else
		{
			for ( int i = 0; i < source.length; i++ )
			{
				int startOffset = 0;
				if ( i == 0 )
					startOffset = 2;
				int start = i == 0 ? 0 : ( i * 4 - 1 );
				int offset = 0;
				for ( offset = 0; offset < 4
						&& startOffset < 8 && start + offset < length; offset++ )
				{
					result[start + offset] = (byte) ( ( source[i] & ( 3 << 6 - startOffset ) ) >> 6 - startOffset );
					startOffset = startOffset + 2;
				}
			}
		}
		return result;
	}
	
	public static byte[] createLeadingBytes( byte[] indicator )
	{
		byte[] result = null;
		byte[] header = createHead( indicator );
		if( header[0] == 0 && header[1] == 0 )
			result = new byte[]{0,0,0,0};
		else if( ( header[0] == 0 && header[1] == 1 ) || ( header[0] == 1 && header[1] == 0 ))
		{
			result = new byte[2+indicator.length];
			result[0] = header[0];
			result[1] = header[1];
			for( int i = 2; i < indicator.length+2; i++ )
			{
				result[i] = (byte) ( (indicator[i-2] == VALID_VALUE_INDICATOR)?0:1 );
			}
		}
		else if ( header[0] == 1 && header[1] == 1)
		{
			byte[] candidate = translateIndicator( indicator );
			result = new byte[2+candidate.length];
			result[0] = header[0];
			result[1] = header[1];
			for( int i = 2; i < candidate.length+2; i++ )
			{
				result[i] = candidate[i-2];
			}
		}
		return makeCompactByte( result );
	}
	
	/**
	 * 
	 * @param bytes
	 * @return
	 */
	private static byte[] makeCompactByte( byte[] bytes )
	{
		int length = ( bytes.length % 8 == 0 ) ? bytes.length / 8
				: bytes.length / 8 + 1;
		byte[] result = new byte[length];
		for( int i = 0; i < length; i++ )
		{
			byte b = 0;
			int baseIndex = i*8;
			for( int offset = 0; offset < 8; offset++)
				b = makeAnAnd( bytes, b, baseIndex, offset );
			result[i] = b;
		}
		return result;
	}

	/**
	 * 
	 * @param bytes
	 * @param b
	 * @param baseIndex
	 * @param offset
	 * @return
	 */
	private static byte makeAnAnd( byte[] bytes, byte b, int baseIndex, int offset )
	{
		if( baseIndex + offset < bytes.length )
		{
			b ^= ( bytes[baseIndex+offset] << (7-offset)) ;
		}
		return b;
	}
	
	/**
	 * 
	 * @param indicator
	 * @return
	 */
	private static byte[] translateIndicator( byte[] indicator )
	{
		byte[] result = new byte[indicator.length*2];
		for( int i = 0; i < indicator.length; i++ )
		{
			switch( indicator[i])
			{
				case VALID_VALUE_INDICATOR:
					break;
				case NULL_INDICATOR:
					result[i*2] = 0;
					result[i*2+1] = 1;
					break;
				case EXCEPTION_INDICATOR:
					result[i*2] = 1;
					result[i*2+1] = 0;
			}			
		}
		return result;
	}
	/**
	 * 00:all the value are valid
	 * 01:include Null value, but not include exception
	 * 10:include Exception, but not include Null value
	 * 11:include both Exception and Null value
	 * @param indicator
	 * @return
	 */
	private static byte[] createHead( byte[] indicator )
	{
		boolean inclNull = false;
		boolean inclException = false;
		
		for( int i = 0; i < indicator.length; i++ )
		{
			switch (indicator[i])
			{
				case NULL_INDICATOR:
					inclNull = true;
					break;
				case EXCEPTION_INDICATOR:
					inclException = true;
					break;
				default:
					break;
			}
		}
		
		if( !inclNull && !inclException )
		{
			return new byte[]{0,0};
		}
		else if ( inclNull && !inclException )
		{
			return new byte[]{0,1};
		}
		else if ( !inclNull && inclException )
		{
			return new byte[]{1,0};
		}
		else 
		{
			return new byte[]{1,1};
		}
	}
}
