/*
 *************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */

package org.eclipse.birt.data.engine.olap.data.impl.aggregation;

import java.io.IOException;
import java.util.Arrays;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.DataResourceHandle;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.olap.data.api.IDimensionResultIterator;
import org.eclipse.birt.data.engine.olap.data.api.ILevel;
import org.eclipse.birt.data.engine.olap.data.api.MeasureInfo;
import org.eclipse.birt.data.engine.olap.data.impl.DimColumn;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Member;
import org.eclipse.birt.data.engine.olap.data.impl.facttable.IFactTableRowIterator;

/**
 * The data prepared for aggregation is from cube
 */
class DataSetFromOriginalCube implements IDataSet4Aggregation
{
	
	IFactTableRowIterator factTableRowIterator;
	
	//All the dimensions, dimIndex and levelIndex are got from it
	IDimensionResultIterator[] dimensionResultIterators;

	private int[] positions = null;

	public DataSetFromOriginalCube( IFactTableRowIterator factTableRowIterator,
			IDimensionResultIterator[] dimensionResultIterators )
	{
		this.dimensionResultIterators = dimensionResultIterators;
		this.factTableRowIterator = factTableRowIterator;
		this.positions = new int[dimensionResultIterators.length];
		Arrays.fill( positions, 0 );
	}

	public MetaInfo getMetaInfo( )
	{
		return new IDataSet4Aggregation.MetaInfo( ) {

			public String[] getAttributeNames( int dimIndex, int levelIndex )
			{
				IDimensionResultIterator itr = dimensionResultIterators[dimIndex];
				return itr.getDimesion( ).getHierarchy( ).getLevels( )[levelIndex].getAttributeNames( );
			}

			public ColumnInfo getColumnInfo( DimColumn dimColumn )
					throws DataException
			{
				String dimensionName = dimColumn.getDimensionName( );
				String levelName = dimColumn.getLevelName( );
				String columnName = dimColumn.getColumnName( );

				int dimIndex = getDimensionIndex( dimensionName );
				if ( dimIndex < 0 )
				{
					throw new DataException( DataResourceHandle.getInstance( )
							.getMessage( ResourceConstants.NONEXISTENT_DIMENSION )
							+ dimensionName );
				}
				IDimensionResultIterator itr = dimensionResultIterators[dimIndex];
				int levelIndex = itr.getLevelIndex( levelName );
				if ( levelIndex < 0 )
				{
					throw new DataException( DataResourceHandle.getInstance( )
							.getMessage( ResourceConstants.NONEXISTENT_LEVEL )
							+ "<" + dimensionName + " , " + levelName + ">" );
				}
				ILevel levelInfo = itr.getDimesion( )
						.getHierarchy( )
						.getLevels( )[levelIndex];
				int columnIndex = -1;
				boolean isKey = false;
				for ( int i = 0; i < levelInfo.getKeyNames( ).length; i++ )
				{
					if ( levelInfo.getKeyNames( )[i].equals( columnName ) )
					{
						columnIndex = i;
						isKey = true;
						break;
					}
				}
				if ( !isKey )
				{
					for ( int i = 0; i < levelInfo.getAttributeNames( ).length; i++ )
					{
						if ( levelInfo.getAttributeNames( )[i].equals( columnName ) )
						{
							columnIndex = i;
							break;
						}
					}
				}
				if ( columnIndex < 0 )
				{
					throw new DataException( DataResourceHandle.getInstance( )
							.getMessage( ResourceConstants.NONEXISTENT_KEY_OR_ATTR )
							+ "<"
							+ dimensionName
							+ " , "
							+ levelName
							+ " , "
							+ columnName + ">" );
				}
				return new IDataSet4Aggregation.ColumnInfo( dimIndex,
						levelIndex,
						columnIndex,
						isKey );
			}

			public int getDimensionIndex( String dimensionName )
			{
				for ( int i = 0; i < dimensionResultIterators.length; i++ )
				{
					if ( dimensionResultIterators[i].getDimesion( )
							.getName( )
							.equals( dimensionName ) )
					{
						return i;
					}
				}
				return -1;
			}

			public String[] getKeyNames( int dimIndex, int levelIndex )
			{
				IDimensionResultIterator itr = dimensionResultIterators[dimIndex];
				return itr.getDimesion( ).getHierarchy( ).getLevels( )[levelIndex].getKeyNames( );
			}

			public int getLevelIndex( String dimensionName, String levelName )
			{
				int dimIndex = getDimensionIndex( dimensionName );
				if ( dimIndex >= 0 )
				{
					IDimensionResultIterator itr = dimensionResultIterators[dimIndex];
					return itr.getLevelIndex( levelName );
				}
				return -1;
			}

			public int getMeasureIndex( String measureName )
			{
				return factTableRowIterator.getMeasureIndex( measureName );
			}

			public MeasureInfo[] getMeasureInfos( )
			{
				return factTableRowIterator.getMeasureInfos( );
			}

		};
	}

	public boolean next( ) throws DataException, IOException
	{
		return factTableRowIterator.next( );
	}

	public Object getMeasureValue( int measureIndex )
	{
		return factTableRowIterator.getMeasure( measureIndex );
	}

	public Member getMember( int dimIndex, int levelIndex )
			throws BirtException, IOException
	{
		String dimensionName = dimensionResultIterators[dimIndex].getDimesion( )
				.getName( );
		int indexInFact = factTableRowIterator.getDimensionIndex( dimensionName );
		return getLevelObject( dimIndex,
				levelIndex,
				factTableRowIterator.getDimensionPosition( indexInFact ) );
	}

	/**
	 * 
	 * @param iteratorIndex
	 * @param levelIndex
	 * @param dimensionPosition
	 * @return
	 * @throws BirtException
	 * @throws IOException
	 */
	private Member getLevelObject( int dimIndex, int levelIndex,
			int dimensionPosition ) throws BirtException, IOException
	{
		while ( true )
		{
			dimensionResultIterators[dimIndex].seek( positions[dimIndex] );
			int curDimPosition = dimensionResultIterators[dimIndex].getDimesionPosition( );
			if ( curDimPosition > dimensionPosition )
			{
				positions[dimIndex]--;
				if ( positions[dimIndex] < 0 )
				{
					positions[dimIndex] = 0;
					return null;
				}
			}
			else if ( curDimPosition < dimensionPosition )
			{
				positions[dimIndex]++;
				if ( positions[dimIndex] >= dimensionResultIterators[dimIndex].length( ) )
				{
					positions[dimIndex]--;
					return null;
				}
			}
			else
			{
				return dimensionResultIterators[dimIndex].getLevelMember( levelIndex );
			}
		}
	}
}
