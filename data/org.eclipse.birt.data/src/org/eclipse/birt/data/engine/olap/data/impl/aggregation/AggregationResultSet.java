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

package org.eclipse.birt.data.engine.olap.data.impl.aggregation;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationFunctionDefinition;
import org.eclipse.birt.data.engine.olap.data.util.DataType;
import org.eclipse.birt.data.engine.olap.data.util.IDiskArray;

/**
 * Default implement class of the interface IAggregationResultSet.
 */

public class AggregationResultSet implements IAggregationResultSet
{

	private AggregationDefinition aggregation;
	private Map aggregationResultNameMap = null;
	private IDiskArray aggregationResultRow;
	private int currentPosition;
	private String[][] keyNames;
	private String[][] attributeNames;
	private int[][] keyDataTypes;
	private int[][] attributeDataTypes;
	private int[] aggregationDataType;
	private AggregationResultRow resultObject;

	/**
	 * 
	 * @param aggregation
	 * @param aggregationResultRow
	 * @param keyNames
	 * @param attributeNames
	 * @throws IOException
	 */
	AggregationResultSet( AggregationDefinition aggregation,
			IDiskArray aggregationResultRow, String[][] keyNames,
			String[][] attributeNames ) throws IOException
	{
		this.aggregation = aggregation;
		this.aggregationResultRow = aggregationResultRow;
		produceaggregationNameMap( );
		this.keyNames = keyNames;
		this.attributeNames = attributeNames;
		this.resultObject = (AggregationResultRow) aggregationResultRow.get( 0 );
		if ( resultObject.getLevelMembers() != null )
		{
			keyDataTypes = new int[resultObject.getLevelMembers().length][];
			attributeDataTypes = new int[resultObject.getLevelMembers().length][];

			for ( int i = 0; i < resultObject.getLevelMembers().length; i++ )
			{
				keyDataTypes[i] = new int[resultObject.getLevelMembers()[i].getKeyValues().length];
				for ( int j = 0; j < resultObject.getLevelMembers()[i].getKeyValues().length; j++ )
				{
					keyDataTypes[i][j] = DataType.getDataType( 
							resultObject.getLevelMembers()[i].getKeyValues()[j].getClass( ) );
				}
				if ( resultObject.getLevelMembers()[i].getAttributes() != null )
				{
					attributeDataTypes[i] = new int[resultObject.getLevelMembers()[i].getAttributes().length];

					for ( int j = 0; j < attributeDataTypes[i].length; j++ )
					{
						if ( resultObject.getLevelMembers()[i].getAttributes()[j]!= null )
						attributeDataTypes[i][j] = DataType.getDataType(
							resultObject.getLevelMembers()[i].getAttributes()[j].getClass( ) );
					}
				}
			}
		}
		if ( resultObject.getAggregationValues() != null )
		{
			aggregationDataType = new int[resultObject.getAggregationValues().length];
			for ( int i = 0; i < resultObject.getAggregationValues().length; i++ )
			{
				aggregationDataType[i] = DataType.getDataType(
						resultObject.getAggregationValues()[i].getClass( ) );
			}
		}
	}

	/**
	 * 
	 */
	void produceaggregationNameMap( )
	{
		AggregationFunctionDefinition[] functions = aggregation.getAggregationFunctions( );
		aggregationResultNameMap = new HashMap( );
		for ( int i = 0; i < functions.length; i++ )
		{
			if(functions[i].getName( )!=null)
			{
				aggregationResultNameMap.put( functions[i].getName( ),
					new Integer( i ) );
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.IAggregationResultSet#getAggregationDataType(int)
	 */
	public int getAggregationDataType( int aggregationIndex )
			throws IOException
	{
		if ( aggregationDataType == null || aggregationIndex < 0 )
			return DataType.UNKNOWN_TYPE;
		return aggregationDataType[aggregationIndex];
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.IAggregationResultSet#getAggregationValue(int)
	 */
	public Object getAggregationValue( int aggregationIndex )
			throws IOException
	{
		if ( resultObject.getAggregationValues() == null || aggregationIndex < 0 )
			return null;
		return resultObject.getAggregationValues()[aggregationIndex];
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.IAggregationResultSet#getLevelAttribute(int, int)
	 */
	public Object getLevelAttribute( int levelIndex, int attributeIndex )
	{
		if ( resultObject.getLevelMembers() == null || levelIndex < 0
				|| resultObject.getLevelMembers()[levelIndex].getAttributes() == null )
		{
			return null;
		}
		return resultObject.getLevelMembers()[levelIndex].getAttributes()[attributeIndex];
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.IAggregationResultSet#getLevelAttributeDataType(java.lang.String, java.lang.String)
	 */
	public int getLevelAttributeDataType( String levelName, String attributeName )
	{
		int levelIndex = getLevelIndex( levelName );
		if ( attributeDataTypes == null || attributeDataTypes[levelIndex] == null )
		{
			return DataType.UNKNOWN_TYPE;
		}
		return this.attributeDataTypes[levelIndex][getLevelAttributeIndex( levelName,
				attributeName )];
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.IAggregationResultSet#getLevelAttributeIndex(java.lang.String, java.lang.String)
	 */
	public int getLevelAttributeIndex( String levelName, String attributeName )
	{
		int levelIndex = getLevelIndex( levelName );
		if ( attributeNames == null || attributeNames[levelIndex] == null )
		{
			return -1;
		}
		for ( int i = 0; i < attributeNames[levelIndex].length; i++ )
		{
			if ( attributeNames[levelIndex][i].equals( attributeName ) )
			{
				return i;
			}
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.IAggregationResultSet#getLevelAttributeIndex(int, java.lang.String)
	 */
	public int getLevelAttributeIndex( int levelIndex, String attributeName )
	{
		if ( attributeNames == null || levelIndex < 0 || attributeNames[levelIndex] == null )
		{
			return -1;
		}
		for ( int i = 0; i < attributeNames[levelIndex].length; i++ )
		{
			if ( attributeNames[levelIndex][i].equals( attributeName ) )
			{
				return i;
			}
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.IAggregationResultSet#getLevelIndex(java.lang.String)
	 */
	public int getLevelIndex( String levelName )
	{
		if ( aggregation.getLevelNames( ) == null )
		{
			return -1;
		}
		for ( int i = 0; i < aggregation.getLevelNames( ).length; i++ )
		{
			if ( aggregation.getLevelNames( )[i].equals( levelName ) )
			{
				return i;
			}
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.IAggregationResultSet#getLevelKeyDataType(java.lang.String, java.lang.String)
	 */
	public int getLevelKeyDataType( String levelName, String keyName )
	{
		if ( keyDataTypes == null )
		{
			return DataType.UNKNOWN_TYPE;
		}
		return getLevelKeyDataType( getLevelIndex( levelName ), keyName );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.IAggregationResultSet#getLevelKeyValue(int)
	 */
	public Object[] getLevelKeyValue( int levelIndex )
	{
		if ( resultObject.getLevelMembers() == null || levelIndex < 0 )
		{
			return null;
		}
		return resultObject.getLevelMembers()[levelIndex].getKeyValues();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.IAggregationResultSet#length()
	 */
	public int length( )
	{
		return aggregationResultRow.size( );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.IAggregationResultSet#seek(int)
	 */
	public void seek( int index ) throws IOException
	{
		currentPosition = index;
		resultObject = (AggregationResultRow) aggregationResultRow.get( index );
	}

	/*
	 * 
	 */
	public int getPosition( )
	{
		return currentPosition;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.IAggregationResultSet#getSortType(int)
	 */
	public int getSortType( int levelIndex )
	{
		if ( aggregation.getSortTypes( ) == null )
		{
			return -100;
		}
		return aggregation.getSortTypes( )[levelIndex];
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.IAggregationResultSet#getLevelAttributeDataType(int, java.lang.String)
	 */
	public int getLevelAttributeDataType( int levelIndex, String attributeName )
	{
		if ( attributeDataTypes == null
				|| levelIndex < 0 || attributeDataTypes[levelIndex] == null )
		{
			return DataType.UNKNOWN_TYPE;
		}
		return attributeDataTypes[levelIndex][getLevelAttributeIndex( levelIndex,
				attributeName )];
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.IAggregationResultSet#getAllAttributes(int)
	 */
	public String[] getAllAttributes( int levelIndex )
	{
		if ( attributeNames == null )
		{
			return null;
		}
		return attributeNames[levelIndex];
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.IAggregationResultSet#getLevelKeyDataType(int, java.lang.String)
	 */
	public int getLevelKeyDataType( int levelIndex, String keyName )
	{
		if ( keyDataTypes == null
				|| levelIndex < 0 || keyDataTypes[levelIndex] == null )
		{
			return DataType.UNKNOWN_TYPE;
		}
		return keyDataTypes[levelIndex][getLevelKeyIndex( levelIndex, keyName )];
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.IAggregationResultSet#getLevelKeyIndex(int, java.lang.String)
	 */
	public int getLevelKeyIndex( int levelIndex, String keyName )
	{
		if ( keyNames == null || levelIndex < 0 || keyNames[levelIndex] == null )
		{
			return DataType.UNKNOWN_TYPE;
		}
		for ( int i = 0; i < keyNames[levelIndex].length; i++ )
		{
			if ( keyNames[levelIndex][i].equals( keyName ) )
			{
				return i;
			}
		}
		return DataType.UNKNOWN_TYPE;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.IAggregationResultSet#getLevelKeyIndex(java.lang.String, java.lang.String)
	 */
	public int getLevelKeyIndex( String levelName, String keyName )
	{
		if ( keyNames == null )
		{
			return -1;
		}
		return getLevelKeyIndex( getLevelIndex( levelName ), keyName );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.IAggregationResultSet#getLevelAttributeColCount(int)
	 */
	public int getLevelAttributeColCount( int levelIndex )
	{
		if ( attributeNames == null || attributeNames[levelIndex] == null )
			return 0;
		return attributeNames[levelIndex].length;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.IAggregationResultSet#getLevelKeyColCount(int)
	 */
	public int getLevelKeyColCount( int levelIndex )
	{
		if ( keyNames == null || keyNames[levelIndex] == null )
			return 0;
		return keyNames[levelIndex].length;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.IAggregationResultSet#getLevelCount()
	 */
	public int getLevelCount( )
	{
		if ( keyNames == null )
			return 0;
		return keyNames.length;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet#getAggregationIndex(java.lang.String)
	 */
	public int getAggregationIndex( String name ) throws IOException
	{
		return ((Integer)aggregationResultNameMap.get( name )).intValue( );
	}

}
