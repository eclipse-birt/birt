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

import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationDefinition;
import org.eclipse.birt.data.engine.olap.data.util.DataType;
import org.eclipse.birt.data.engine.olap.data.util.IDiskArray;

/**
 * Default implement class of the interface IAggregationResultSet.
 */

public class AggregationResultSet implements IAggregationResultSet
{

	private AggregationDefinition aggregation;
	private IDiskArray aggregationRow;
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
	 * @param aggregationRow
	 * @param keyNames
	 * @param attributeNames
	 * @throws IOException
	 */
	AggregationResultSet( AggregationDefinition aggregation,
			IDiskArray aggregationRow, String[][] keyNames,
			String[][] attributeNames ) throws IOException
	{
		this.aggregation = aggregation;
		this.aggregationRow = aggregationRow;
		this.keyNames = keyNames;
		this.attributeNames = attributeNames;
		this.resultObject = (AggregationResultRow) aggregationRow.get( 0 );
		if ( resultObject.levelMembers != null )
		{
			keyDataTypes = new int[resultObject.levelMembers.length][];
			attributeDataTypes = new int[resultObject.levelMembers.length][];

			for ( int i = 0; i < resultObject.levelMembers.length; i++ )
			{
				keyDataTypes[i] = new int[resultObject.levelMembers[i].keyValues.length];
				for ( int j = 0; j < resultObject.levelMembers[i].keyValues.length; j++ )
				{
					keyDataTypes[i][j] = DataType.getDataType( 
							resultObject.levelMembers[i].keyValues[j].getClass( ) );
				}
				if ( resultObject.levelMembers[i].attributes != null )
				{
					attributeDataTypes[i] = new int[resultObject.levelMembers[i].attributes.length];

					for ( int j = 0; j < attributeDataTypes[i].length; j++ )
					{
						attributeDataTypes[i][i] = DataType.getDataType(
							resultObject.levelMembers[i].attributes[i].getClass( ) );
					}
				}
			}
		}
		if ( resultObject.aggregationValues != null )
		{
			aggregationDataType = new int[resultObject.aggregationValues.length];
			for ( int i = 0; i < resultObject.aggregationValues.length; i++ )
			{
				aggregationDataType[i] = DataType.getDataType(
						resultObject.aggregationValues[i].getClass( ) );
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
		if ( resultObject.aggregationValues == null || aggregationIndex < 0 )
			return null;
		return resultObject.aggregationValues[aggregationIndex];
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.IAggregationResultSet#getLevelAttribute(int, int)
	 */
	public Object getLevelAttribute( int levelIndex, int attributeIndex )
	{
		if ( resultObject.levelMembers == null || levelIndex < 0
				|| resultObject.levelMembers[levelIndex].attributes == null )
		{
			return null;
		}
		return resultObject.levelMembers[levelIndex].attributes[attributeIndex];
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
		if ( resultObject.levelMembers == null || levelIndex < 0 )
		{
			return null;
		}
		return resultObject.levelMembers[levelIndex].keyValues;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.IAggregationResultSet#length()
	 */
	public int length( )
	{
		return aggregationRow.size( );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.IAggregationResultSet#seek(int)
	 */
	public void seek( int index ) throws IOException
	{
		currentPosition = index;
		resultObject = (AggregationResultRow) aggregationRow.get( index );
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

}
