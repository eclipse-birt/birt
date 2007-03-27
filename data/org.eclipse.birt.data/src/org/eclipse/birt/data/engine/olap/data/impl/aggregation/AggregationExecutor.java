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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.api.cube.StopSign;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;
import org.eclipse.birt.data.engine.olap.data.api.IDimensionSortDefn;
import org.eclipse.birt.data.engine.olap.data.api.IDimensionResultIterator;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.Constants;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Member;
import org.eclipse.birt.data.engine.olap.data.impl.facttable.IFactTableRowIterator;
import org.eclipse.birt.data.engine.olap.data.util.DiskSortedStack;

/**
 * Execute aggregation on a cube.
 */

public class AggregationExecutor
{

	private IDimensionResultIterator[] dimesionResultIterators = null;
	private AggregationCalculator[] aggregationCalculators = null;
	private IFactTableRowIterator facttableRowIterator = null;
	private DiskSortedStackWrapper[] sortedFactRows = null;
	private List allSortedFactRows = null;
	private int[][] levelIndex = null;

	/**
	 * 
	 * @param dimesionResultIterators
	 * @param facttableRowIterator
	 * @param aggregations
	 * @throws BirtOlapException 
	 */
	public AggregationExecutor(
			IDimensionResultIterator[] dimesionResultIterators,
			IFactTableRowIterator facttableRowIterator,
			AggregationDefinition[] aggregations ) throws DataException
	{
		this.dimesionResultIterators = dimesionResultIterators;
		this.aggregationCalculators = new AggregationCalculator[aggregations.length];
		for ( int i = 0; i < this.aggregationCalculators.length; i++ )
		{
			this.aggregationCalculators[i] = new AggregationCalculator( aggregations[i],
					facttableRowIterator );
		}
		sortedFactRows = new DiskSortedStackWrapper[aggregations.length];
		this.facttableRowIterator = facttableRowIterator;

		getAllAggregationLevelIndex( );
	}

	/**
	 * 
	 * @param stopSign
	 * @return
	 * @throws IOException
	 * @throws BirtException
	 */
	public IAggregationResultSet[] excute( StopSign stopSign )
			throws IOException, BirtException
	{
		populateSortedFactRows( stopSign );
		for ( int i = 0; i < allSortedFactRows.size( ); i++ )
		{
			DiskSortedStackWrapper diskSortedStackWrapper = (DiskSortedStackWrapper) allSortedFactRows.get( i );
			int[] calculatorIndexs = new int[sortedFactRows.length];
			int pos = 0;
			for ( int j = 0; j < calculatorIndexs.length; j++ )
			{
				if ( sortedFactRows[j] == diskSortedStackWrapper )
				{
					calculatorIndexs[pos] = j;
					pos++;
				}
			}
			while ( diskSortedStackWrapper.pop( ) != null
					&& !stopSign.isStopped( ) )
			{
				Row4Aggregation row = (Row4Aggregation) diskSortedStackWrapper.getCurrentObject( );
				for ( int j = 0; j < pos; j++ )
				{
					aggregationCalculators[calculatorIndexs[j]].onRow( cut( row,
							levelIndex[calculatorIndexs[j]].length / 2 ) );
				}
			}
		}
		IAggregationResultSet[] resultSets = 
			new IAggregationResultSet[aggregationCalculators.length];
		for ( int i = 0; i < aggregationCalculators.length; i++ )
		{
			resultSets[i] = new AggregationResultSet( aggregationCalculators[i].aggregation,
					aggregationCalculators[i].getResult( ),
					getKeyNames( i ),
					getAttributeNames( i ) );
		}
		return resultSets;
	}
	
	/**
	 * 
	 * @param row
	 * @param levelCount
	 * @return
	 */
	private static Row4Aggregation cut( Row4Aggregation row, int levelCount )
	{
		Row4Aggregation result = new Row4Aggregation( );
		if ( levelCount > 0 )
		{
			result.levelMembers = new Member[levelCount];
			System.arraycopy( row.levelMembers,
					0,
					result.levelMembers,
					0,
					levelCount );
		}
		result.measures = row.measures;
		return result;
	}

	/**
	 * 
	 * @param aggregationIndex
	 * @return
	 */
	private String[][] getKeyNames( int aggregationIndex )
	{
		String[][] result = new String[levelIndex[aggregationIndex].length/2][];
		int[] tmpLevelIndex = levelIndex[aggregationIndex];
		for ( int i = 0; i < levelIndex[aggregationIndex].length / 2; i++ )
		{
			result[i] = dimesionResultIterators[tmpLevelIndex[i * 2]].getDimesion( )
					.getHierarchy( )
					.getLevels( )[tmpLevelIndex[i * 2 + 1]].getKeyName( );
		}
		return result;
	}

	/**
	 * 
	 * @param aggregationIndex
	 * @return
	 */
	private String[][] getAttributeNames( int aggregationIndex )
	{
		String[][] result = new String[levelIndex[aggregationIndex].length/2][];
		int[] tmpLevelIndex = levelIndex[aggregationIndex];
		for ( int i = 0; i < levelIndex[aggregationIndex].length / 2; i++ )
		{
			result[i] = dimesionResultIterators[tmpLevelIndex[i * 2]].getDimesion( )
					.getHierarchy( )
					.getLevels( )[tmpLevelIndex[i * 2 + 1]].getAttributeNames( );
		}
		return result;
	}

	/**
	 * 
	 * @param stopSign
	 * @throws IOException
	 * @throws BirtException
	 */
	private void populateSortedFactRows( StopSign stopSign ) throws IOException,
			BirtException
	{
		Row4AggregationPopulator aggregationRowPopulator = new Row4AggregationPopulator( dimesionResultIterators,
				facttableRowIterator );

		prepareSortedStacks( );
		int measureCount = facttableRowIterator.getMeasureCount( );

		while ( facttableRowIterator.next( ) && !stopSign.isStopped( ) )
		{
			for ( int i = 0; i < allSortedFactRows.size( ); i++ )
			{
				DiskSortedStackWrapper diskSortedStackWrapper = ( (DiskSortedStackWrapper) allSortedFactRows.get( i ) );

				int[] levelIndex = diskSortedStackWrapper.levelIndex;

				Row4Aggregation aggregationRow = new Row4Aggregation( );
				aggregationRow.levelMembers = aggregationRowPopulator.getLevelMembers( levelIndex );
				if ( aggregationRow.levelMembers == null )
				{
					continue;
				}
				aggregationRow.measures = new Object[measureCount];
				for ( int j = 0; j < measureCount; j++ )
				{
					aggregationRow.measures[j] = facttableRowIterator.getMeasure( j );
				}
				diskSortedStackWrapper.diskSortedStack.push( aggregationRow );
			}
		}
	}

	/**
	 * 
	 *
	 */
	private void prepareSortedStacks( )
	{
		allSortedFactRows = new ArrayList( );
		while ( true )
		{
			int maxLevelCount = 0;
			int aggregationIndex = -1;
			int[] levelSortType = null;
			for ( int i = 0; i < aggregationCalculators.length; i++ )
			{
				if ( sortedFactRows[i] == null
						&& aggregationCalculators[i].aggregation.getLevelNames( ) != null
						&& aggregationCalculators[i].aggregation.getLevelNames( ).length > maxLevelCount )
				{
					aggregationIndex = i;
					maxLevelCount = levelIndex[i].length;
					levelSortType = aggregationCalculators[i].aggregation.getSortTypes( );
				}
			}
			if ( aggregationIndex == -1 )
			{
				break;
			}

			Comparator comparator = new Row4AggregationComparator( levelSortType );
			DiskSortedStack diskSortedStack = new DiskSortedStack( Constants.FACT_TABLE_BUFFER_SIZE,
					false,
					comparator,
					Row4Aggregation.getCreator( ) );
			DiskSortedStackWrapper diskSortedStackReader = new DiskSortedStackWrapper( diskSortedStack,
					levelIndex[aggregationIndex] );
			this.allSortedFactRows.add( diskSortedStackReader );

			for ( int i = 0; i < aggregationCalculators.length; i++ )
			{
				if ( sortedFactRows[i] == null
						&& cover( levelIndex[aggregationIndex],
								levelIndex[i] ) )
				{
					sortedFactRows[i] = diskSortedStackReader;
				}
			}
		}
	}

	/**
	 * 
	 * @param dimensionIndex1
	 * @param dimensionIndex2
	 * @return
	 */
	private static boolean cover( int[] dimensionIndex1, int[] dimensionIndex2 )
	{
		if ( dimensionIndex2 == null || dimensionIndex2.length == 0 )
		{
			return true;
		}
		if ( dimensionIndex1.length < dimensionIndex2.length )
		{
			return false;
		}
		for ( int i = 0; i < dimensionIndex2.length; i++ )
		{
			if ( dimensionIndex1[i] != dimensionIndex2[i] )
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * 
	 */
	private void getAllAggregationLevelIndex( )
	{
		levelIndex = new int[aggregationCalculators.length][];
		for ( int i = 0; i < aggregationCalculators.length; i++ )
		{
			String[] levelNames = aggregationCalculators[i].aggregation.getLevelNames( );
			if ( levelNames == null || levelNames.length == 0 )
			{
				levelIndex[i] = new int[0];
				continue;
			}
			int[] tmpLevelIndex = new int[levelNames.length * 2];
			for ( int j = 0; j < tmpLevelIndex.length / 2; j++ )
			{
				tmpLevelIndex[j * 2] = findDimensionIterator( levelNames[j] );
				tmpLevelIndex[j * 2 + 1] = dimesionResultIterators[tmpLevelIndex[j * 2]].getLevelIndex( levelNames[j] );
			}
			levelIndex[i] = tmpLevelIndex;
		}
	}

	/**
	 * 
	 * @param levelName
	 * @return
	 */
	private int findDimensionIterator( String levelName )
	{
		for ( int i = 0; i < dimesionResultIterators.length; i++ )
		{
			if ( dimesionResultIterators[i].getLevelIndex( levelName ) >= 0 )
			{
				return i;
			}
		}
		return -1;
	}
}

/**
 * 
 * @author Administrator
 *
 */
class Row4AggregationPopulator
{

	private int[] position = null;
	private IDimensionResultIterator[] dimesionResultIterators = null;
	private IFactTableRowIterator facttableRowIterator = null;
	private int[] dimensionIndexs = null;

	/**
	 * 
	 * @param dimesionResultIterators
	 * @param facttableRowIterator
	 */
	Row4AggregationPopulator( IDimensionResultIterator[] dimesionResultIterators,
			IFactTableRowIterator facttableRowIterator )
	{
		this.dimesionResultIterators = dimesionResultIterators;
		this.facttableRowIterator = facttableRowIterator;
		this.position = new int[dimesionResultIterators.length];
		this.dimensionIndexs = new int[dimesionResultIterators.length];
		for ( int i = 0; i < dimensionIndexs.length; i++ )
		{
			dimensionIndexs[i] = facttableRowIterator.getDimensionIndex( 
					dimesionResultIterators[i].getDimesion( ).getName( ) );
		}
	}

	/**
	 * 
	 * @param levelIndex
	 * @return
	 * @throws BirtException
	 * @throws IOException
	 */
	Member[] getLevelMembers( int[] levelIndex ) throws BirtException,
			IOException
	{
		Member[] result = new Member[levelIndex.length / 2];
		for ( int i = 0; i < result.length; i++ )
		{
			int iteratorIndex = levelIndex[i * 2];
			int iteratorLevelIndex = levelIndex[i * 2 + 1];
			int dimensionIndex = dimensionIndexs[iteratorIndex];
			result[i] = getLevelObject( iteratorIndex,
					iteratorLevelIndex,
					facttableRowIterator.getDimensionPosition( dimensionIndex ) );
			if ( result[i] == null )
			{
				return null;
			}
		}
		return result;
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
	private Member getLevelObject( int iteratorIndex, int levelIndex,
			int dimensionPosition ) throws BirtException, IOException
	{
		while ( true )
		{
			dimesionResultIterators[iteratorIndex].seek( position[iteratorIndex] );
			int curDimPosition = dimesionResultIterators[iteratorIndex].getDimesionPosition( );
			if ( curDimPosition > dimensionPosition )
			{
				position[iteratorIndex]--;
				if ( position[iteratorIndex] < 0 )
				{
					position[iteratorIndex] = 0;
					return null;
				}
			}
			else if ( curDimPosition < dimensionPosition )
			{
				position[iteratorIndex]++;
				if ( position[iteratorIndex] >= dimesionResultIterators[iteratorIndex].length( ) )
				{
					position[iteratorIndex]--;
					return null;
				}
			}
			else
			{
				return dimesionResultIterators[iteratorIndex].getLevelMember( levelIndex );
			}
		}
	}

}

/**
 * 
 * @author Administrator
 *
 */
class Row4AggregationComparator implements Comparator
{

	private int[] sortType = null;

	/**
	 * 
	 * @param sortType
	 */
	Row4AggregationComparator( int[] sortType )
	{
		this.sortType = sortType;
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare( Object o1, Object o2 )
	{
		Row4Aggregation row1 = (Row4Aggregation) o1;
		Row4Aggregation row2 = (Row4Aggregation) o2;

		assert row1.levelMembers.length == row2.levelMembers.length;

		for ( int i = 0; i < row1.levelMembers.length; i++ )
		{
			if ( sortType == null
					|| sortType.length <= i
					|| sortType[i] == IDimensionSortDefn.SORT_UNDEFINED
					|| sortType[i] == IDimensionSortDefn.SORT_ASC )
			{
				if ( row1.levelMembers[i].compareTo( row2.levelMembers[i] ) < 0 )
				{
					return -1;
				}
				else if ( row1.levelMembers[i].compareTo( row2.levelMembers[i] ) > 0 )
				{
					return 1;
				}
			}
			else
			{
				if ( row1.levelMembers[i].compareTo( row2.levelMembers[i] ) < 0 )
				{
					return 1;
				}
				else if ( row1.levelMembers[i].compareTo( row2.levelMembers[i] ) > 0 )
				{
					return -1;
				}
			}
		}
		return 0;
	}

}

/**
 * 
 * @author Administrator
 *
 */
class DiskSortedStackWrapper
{

	DiskSortedStack diskSortedStack = null;
	Object currentObj = null;
	int[] levelIndex = null;

	/**
	 * 
	 * @param diskSortedStack
	 * @param levelIndex
	 */
	DiskSortedStackWrapper( DiskSortedStack diskSortedStack, int[] levelIndex )
	{
		this.diskSortedStack = diskSortedStack;
		this.levelIndex = levelIndex;
	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	Object pop( ) throws IOException
	{
		currentObj = diskSortedStack.pop( );
		return currentObj;
	}

	/**
	 * 
	 * @return
	 */
	Object getCurrentObject( )
	{
		return currentObj;
	}
}
