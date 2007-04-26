
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
package org.eclipse.birt.data.engine.olap.data.impl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.core.archive.RAInputStream;
import org.eclipse.birt.core.archive.RAOutputStream;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.AggregationResultRow;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.AggregationResultSet;

/**
 * 
 */

public class AggregationResultSetSaveUtil
{
	/**
	 * 
	 * @param name
	 * @param resultSets
	 * @param writer
	 * @throws IOException
	 */
	public static void save( String name, IAggregationResultSet[] resultSets, IDocArchiveWriter writer ) throws IOException
	{
		RAOutputStream outputStream = writer.createRandomAccessStream( name );
		DataOutputStream dataOutputStream = new DataOutputStream( outputStream );
		//write resultset length
		if(resultSets==null)
		{
			dataOutputStream.writeInt( -1 );
			return;
		}
		dataOutputStream.writeInt( resultSets.length );
		for( int i=0;i<resultSets.length;i++)
		{
			saveOneResultSet( dataOutputStream, resultSets[i] );
		}
	}
	
	/**
	 * 
	 * @param name
	 * @param reader
	 * @throws IOException
	 */
	public static IAggregationResultSet[] load( String name, IDocArchiveReader reader ) throws IOException
	{
		RAInputStream inputStream = reader.getStream( name );
		DataInputStream dataInputStream = new DataInputStream( inputStream );
		 
		int size = dataInputStream.readInt( );
		if( size <= 0 )
		{
			return null;
		}
		IAggregationResultSet[] result = new IAggregationResultSet[size];
		for( int i=0;i<size;i++)
		{
			result[i] = loadOneResultSet( dataInputStream );
		}
		return result;
	}

	private static IAggregationResultSet loadOneResultSet(
			DataInputStream dataInputStream ) throws IOException
	{
		//read level
		String[] levelNames = convertToStringArray( readObjectArray( dataInputStream ) );
		//read keys
		String[][] keyNames = convertToMDStringArray( readMDObjectArray( dataInputStream ) );
		int[][] keyDataTypes = readMDIntArray( dataInputStream );
		//read attribute
		String[][] attributeNames = convertToMDStringArray( readMDObjectArray( dataInputStream ) );
		int[][] attributeDataTypes = readMDIntArray( dataInputStream );
		//read sortType
		int[] sortTypes = readIntArray( dataInputStream );
		
		//read aggregation
		String[] aggregationNames = convertToStringArray( readObjectArray( dataInputStream ) );
		int[] aggregationDataType = readIntArray( dataInputStream );
		
		//read row size
		int size = IOUtil.readInt( dataInputStream );
		
		return new CachedAggregationResultSet( dataInputStream,
				size,
				levelNames,
				sortTypes,
				keyNames,
				attributeNames,
				keyDataTypes,
				attributeDataTypes,
				aggregationNames,
				aggregationDataType );
	}
	
	/**
	 * 
	 * @param objs
	 * @return
	 */
	private static String[] convertToStringArray( Object[] objs )
	{
		if( objs == null )
		{
			return null;
		}
		String[] result = new String[objs.length];
		System.arraycopy( objs, 0, result, 0, objs.length );
		return result;
	}
	
	/**
	 * 
	 * @param objs
	 * @return
	 */
	private static String[][] convertToMDStringArray( Object[][] objs )
	{
		if( objs == null )
		{
			return null;
		}
		String[][] result = new String[objs.length][];
		for( int i=0;i<result.length;i++)
		{
			result[i] = convertToStringArray( objs[i] );
		}
		return result;
	}
	
	/**
	 * 
	 * @param outputStream
	 * @param resultSet
	 * @throws IOException
	 */
	private static void saveOneResultSet( DataOutputStream outputStream, IAggregationResultSet resultSet ) throws IOException
	{
		if( resultSet instanceof AggregationResultSet )
		{
			saveMetaData( outputStream, (AggregationResultSet) resultSet );
			saveAggregationRowSet( outputStream, (AggregationResultSet) resultSet );
		}
	}
	
	/**
	 * 
	 * @param outputStream
	 * @param resultSet
	 * @throws IOException 
	 */
	private static void saveAggregationRowSet( DataOutputStream outputStream, AggregationResultSet resultSet ) throws IOException
	{
		IOUtil.writeInt( outputStream, resultSet.length( ) );
		for( int i=0;i<resultSet.length( );i++)
		{
			resultSet.seek( i );
			saveAggregationRow( outputStream, resultSet.getCurrentRow( ) );
			
		}
	}
	
	/**
	 * 
	 * @param outputStream
	 * @param resultRow
	 * @throws IOException
	 */
	private static void saveAggregationRow( DataOutputStream outputStream, AggregationResultRow resultRow ) throws IOException
	{
		writeObjectArray( outputStream, resultRow.getFieldValues( ) );
	}
	
	/*
	 * 
	 */
	public static AggregationResultRow loadAggregationRow(
			DataInputStream inputStream ) throws IOException
	{
		Object[] objects = readObjectArray( inputStream );
		if( objects == null || objects.length == 0 )
		{
			return null;
		}
		return (AggregationResultRow) AggregationResultRow.getCreator( )
				.createInstance( objects );
	}
	
	/**
	 * 
	 * @param outputStream
	 * @param aggregationDef
	 * @throws IOException 
	 */
	private static void saveMetaData( DataOutputStream outputStream, AggregationResultSet resultSet ) throws IOException
	{
		//write level
			//level names
		String[] levels = resultSet.getAllLevels( );
		writeObjectArray( outputStream, levels );
			//level keys
		writeObjectArray( outputStream, resultSet.getLevelKeys( ) );
		writeIntArray( outputStream, resultSet.getLevelKeyDataType( ) );
			//level attribute
		writeObjectArray( outputStream, resultSet.getLevelAttributes( ) );
		writeIntArray( outputStream, resultSet.getLevelAttributeDataType( ) );
			//level sortType
		writeIntArray( outputStream, resultSet.getSortType( ) );
		
		//write aggregation
		AggregationFunctionDefinition[] functions = resultSet.getAggregationDef( )
				.getAggregationFunctions( );
		String[] aggregationNames = null;
		if ( functions != null )
		{
			aggregationNames = new String[functions.length];
			for ( int i = 0; i < aggregationNames.length; i++ )
			{
				aggregationNames[i] = functions[i].getName( );
			}
		}
		writeObjectArray( outputStream, aggregationNames );

		writeIntArray( outputStream, resultSet.getAggregationDataType( ) );
	}
	
	/**
	 * 
	 * @param outputStream
	 * @param objects
	 * @throws IOException
	 */
	private static void writeObjectArray( DataOutputStream outputStream, Object[] objects ) throws IOException
	{
		if ( objects == null )
		{
			IOUtil.writeInt( outputStream, -1 );
			return;
		}
		IOUtil.writeInt( outputStream, objects.length );
		for ( int i = 0; i < objects.length; i++ )
		{
			IOUtil.writeObject( outputStream, objects[i] );
		}
	}
	
	/**
	 * 
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	private static Object[] readObjectArray( DataInputStream inputStream ) throws IOException
	{
		int size = IOUtil.readInt( inputStream );
		if( size == -1 )
			return null;
		Object[] result = new Object[size];
		for ( int i = 0; i < result.length; i++ )
		{
			result[i] = IOUtil.readObject( inputStream );
		}
		return result;
	}
	
	/**
	 * 
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	private static Object[][] readMDObjectArray( DataInputStream inputStream ) throws IOException
	{
		int size = IOUtil.readInt( inputStream );
		if( size == -1 )
			return null;
		Object[][] result = new Object[size][];
		for ( int i = 0; i < result.length; i++ )
		{
			result[i] = readObjectArray( inputStream );
		}
		return result;
	}
	
	/**
	 * 
	 * @param outputStream
	 * @param objects
	 * @throws IOException
	 */
	private static void writeObjectArray( DataOutputStream outputStream, Object[][] objects ) throws IOException
	{
		if ( objects == null )
		{
			IOUtil.writeInt( outputStream, -1 );
			return;
		}
		IOUtil.writeInt( outputStream, objects.length );
		for ( int i = 0; i < objects.length; i++ )
		{
			writeObjectArray( outputStream, objects[i] );
		}
	}
	
	/**
	 * 
	 * @param outputStream
	 * @return
	 * @throws IOException 
	 */
	private static int[] readIntArray( DataInputStream inputStream ) throws IOException
	{
		int size = IOUtil.readInt( inputStream );
		if( size == -1 )
			return null;
		int[] result = new int[size];
		for ( int i = 0; i < result.length; i++ )
		{
			result[i] = IOUtil.readInt( inputStream );
		}
		return result;
	}
	
	/**
	 * 
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	private static int[][] readMDIntArray( DataInputStream inputStream ) throws IOException
	{
		int size = IOUtil.readInt( inputStream );
		if( size == -1 )
			return null;
		int[][] result = new int[size][];
		for ( int i = 0; i < result.length; i++ )
		{
			result[i] = readIntArray( inputStream );
		}
		return result;
	}
	
	/**
	 * 
	 * @param outputStream
	 * @param objects
	 * @throws IOException
	 */
	private static void writeIntArray( DataOutputStream outputStream, int[] iA ) throws IOException
	{
		if ( iA == null )
		{
			IOUtil.writeInt( outputStream, -1 );
			return;
		}
		IOUtil.writeInt( outputStream, iA.length );
		for ( int i = 0; i < iA.length; i++ )
		{
			IOUtil.writeInt( outputStream, iA[i] );
		}
	}
	
	/**
	 * 
	 * @param outputStream
	 * @param iA
	 * @throws IOException
	 */
	private static void writeIntArray( DataOutputStream outputStream, int[][] iA ) throws IOException
	{
		if ( iA == null )
		{
			IOUtil.writeInt( outputStream, -1 );
			return;
		}
		IOUtil.writeInt( outputStream, iA.length );
		for ( int i = 0; i < iA.length; i++ )
		{
			writeIntArray( outputStream, iA[i] );
		}
	}
}
