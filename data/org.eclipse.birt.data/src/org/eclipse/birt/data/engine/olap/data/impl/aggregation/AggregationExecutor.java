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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.DataResourceHandle;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.StopSign;
import org.eclipse.birt.data.engine.olap.data.api.DimLevel;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;
import org.eclipse.birt.data.engine.olap.data.api.IDimensionSortDefn;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationFunctionDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.Constants;
import org.eclipse.birt.data.engine.olap.data.impl.DimColumn;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Member;
import org.eclipse.birt.data.engine.olap.data.util.DiskSortedStack;

/**
 * Execute aggregation on a cube. 
 */

public class AggregationExecutor
{
	private AggregationCalculator[] aggregationCalculators = null;
	private DiskSortedStackWrapper[] sortedFactRows = null;
	private List allSortedFactRows = null;
	private int[][] levelIndex = null;
	
	//the parameter sequence corresponding with <code>Row4Aggregation.getParameterValues()</code>  
	private DimColumn[] paraColumns = null;
	
	//for every dimColumn in paraColumns, save <dimIndex, levelIndex, columnIndex, isKey>
	private ColumnInfo[] paraInfos;
	
	private IDataSet4Aggregation dataSet4Aggregation;
	protected static Logger logger = Logger.getLogger( AggregationExecutor.class.getName( ) );

	/**
	 * 
	 * @param dimensionResultIterators
	 * @param factTableRowIterator
	 * @param aggregations
	 * @throws BirtOlapException 
	 */
	public AggregationExecutor(
			ICubeDimensionReader cubeDimensionReader, 
			IDataSet4Aggregation dataSet4Aggregation,
			AggregationDefinition[] aggregations ) throws IOException, DataException
	{
		Object[] params = {
				dataSet4Aggregation, aggregations
		};
		logger.entering( AggregationExecutor.class.getName( ),
				"AggregationExecutor",
				params );
		this.dataSet4Aggregation = dataSet4Aggregation;
		
		getParameterColIndex( aggregations );
	
		this.aggregationCalculators = new AggregationCalculator[aggregations.length];
		for ( int i = 0; i < this.aggregationCalculators.length; i++ )
		{
			this.aggregationCalculators[i] = new AggregationCalculator( aggregations[i], paraColumns, 
					dataSet4Aggregation.getMetaInfo( ), cubeDimensionReader );
		}
		sortedFactRows = new DiskSortedStackWrapper[aggregations.length];
		
		getAggregationLevelIndex( );
		
		logger.exiting( AggregationExecutor.class.getName( ),
				"AggregationExecutor" );
	}

	/**
	 * 
	 * @param stopSign
	 * @return
	 * @throws IOException
	 * @throws BirtException
	 */
	public IAggregationResultSet[] execute( StopSign stopSign )
			throws IOException, DataException
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
//					aggregationCalculators[calculatorIndexs[j]].onRow( cut( row,
//							levelIndex[calculatorIndexs[j]].length / 2 ) );
					aggregationCalculators[calculatorIndexs[j]].onRow( row );
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
		this.dataSet4Aggregation.close( );
		return resultSets;
	}
	
	/**
	 * 
	 * @param row
	 * @param levelCount
	 * @return
	 */
//	private static Row4Aggregation cut( Row4Aggregation row, int levelCount )
//	{
//		Row4Aggregation result = new Row4Aggregation( );
//		if ( levelCount > 0 )
//		{
//			result.setLevelMembers( new Member[levelCount] );
//			System.arraycopy( row.getLevelMembers(),
//					0,
//					result.getLevelMembers(),
//					0,
//					levelCount );
//		}
//		result.setMeasures( row.getMeasures() );
//		result.setParameterValues( row.getParameterValues( ) );
//		return result;
//	}

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
			result[i] = dataSet4Aggregation.getMetaInfo( ).getKeyNames( tmpLevelIndex[i * 2], tmpLevelIndex[i * 2 + 1] );
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
			result[i] = dataSet4Aggregation.getMetaInfo( ).getAttributeNames( tmpLevelIndex[i * 2], tmpLevelIndex[i * 2 + 1] );
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
			DataException
	{
//		Row4AggregationPopulator aggregationRowPopulator = new Row4AggregationPopulator( dimesionResultIterators,
//				facttableRowIterator, parameterColIndexs );

		prepareSortedStacks( );
		int measureCount = dataSet4Aggregation.getMetaInfo( ).getMeasureInfos( ).length;
		try
		{
			while ( dataSet4Aggregation.next( ) && !stopSign.isStopped( ) )
			{
				for ( int i = 0; i < allSortedFactRows.size( ); i++ )
				{
					DiskSortedStackWrapper diskSortedStackWrapper = ( (DiskSortedStackWrapper) allSortedFactRows.get( i ) );

					int[] levelIndex = diskSortedStackWrapper.levelIndex;

					Row4Aggregation aggregationRow = new Row4Aggregation( );
					aggregationRow.setDimPos( dataSet4Aggregation.getDimensionPosition( ) );
					aggregationRow.setLevelMembers( getLevelMembers( levelIndex ) );

					if ( aggregationRow.getLevelMembers( ) == null )
					{
						continue;
					}
					aggregationRow.setMeasures( new Object[measureCount] );
					for ( int j = 0; j < measureCount; j++ )
					{
						aggregationRow.getMeasures( )[j] = dataSet4Aggregation.getMeasureValue( j );
					}
					aggregationRow.setParameterValues( getParameterValues( ) );
					diskSortedStackWrapper.diskSortedStack.push( aggregationRow );

				}
			}
		}
		catch ( BirtException e )
		{
			throw DataException.wrap( e );
		}
	}
	
	Member[] getLevelMembers( int[] levelIndex ) throws BirtException, IOException 
	{
		Member[] result = new Member[levelIndex.length / 2];
		for ( int i = 0; i < result.length; i++ )
		{
			int dim = levelIndex[i * 2];
			int level = levelIndex[i * 2 + 1];
			result[i] = dataSet4Aggregation.getMember( dim, level );
			if ( result[i] == null )
			{
				return null;
			}
		}
		return result;
	}
	
	Object[] getParameterValues( ) throws BirtException, IOException
	{
		if( paraInfos == null || paraInfos.length == 0 )
		{
			return null;
		}
		Object[] reValues = new Object[paraInfos.length];
		for ( int i = 0; i < reValues.length; i++ )
		{
			Member member = dataSet4Aggregation.getMember( paraInfos[i].getDimIndex( ), paraInfos[i].getLevelIndex( ) );
			if( paraInfos[i].isKey( ) )
			{
				reValues[i] = member.getKeyValues( )[paraInfos[i].getColumnIndex( )];
			}
			else
			{
				reValues[i] = member.getAttributes( )[paraInfos[i].getColumnIndex( )];
			}
		}
		return reValues;
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
				if ( sortedFactRows[i] == null &&
						( ( aggregationCalculators[i].aggregation.getLevels( ) != null 
							&& aggregationCalculators[i].aggregation.getLevels( ).length > maxLevelCount ) 
								|| aggregationCalculators[i].aggregation.getLevels( ) == null ) )
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
	private void getAggregationLevelIndex( ) throws DataException
	{
		if( aggregationCalculators == null )
		{
			return;
		}
		levelIndex = new int[aggregationCalculators.length][];
		for ( int i = 0; i < aggregationCalculators.length; i++ )
		{
			DimLevel[] levels = aggregationCalculators[i].aggregation.getLevels( );
			if ( levels == null || levels.length == 0 )
			{
				levelIndex[i] = new int[0];
				continue;
			}
			int[] tmpLevelIndex = new int[levels.length * 2];
			for ( int j = 0; j < tmpLevelIndex.length / 2; j++ )
			{
				String dimensionName = levels[j].getDimensionName( );
				String levelName = levels[j].getLevelName( );
				int dimIndex = dataSet4Aggregation.getMetaInfo( ).getDimensionIndex( dimensionName );
				if ( dimIndex < 0 )
				{
					throw new DataException( DataResourceHandle.getInstance( )
							.getMessage( ResourceConstants.NONEXISTENT_DIMENSION )
							+ dimensionName );
				}
				int levelIndex = dataSet4Aggregation.getMetaInfo( ).getLevelIndex( dimensionName, levelName );
				if ( levelIndex < 0 )
				{
					throw new DataException( DataResourceHandle.getInstance( )
							.getMessage( ResourceConstants.NONEXISTENT_LEVEL )
							+ "<" + dimensionName + " , " + levelName + ">" );
				}
				tmpLevelIndex[j * 2] = dimIndex;
				tmpLevelIndex[j * 2 + 1] = levelIndex;
			}
			levelIndex[i] = tmpLevelIndex;
		}
	}
	
	private void getParameterColIndex( AggregationDefinition[] aggregations ) throws DataException
	{
		Set paraCols = new HashSet( );
		for ( int i = 0; i < aggregations.length; i++ )
		{
			AggregationFunctionDefinition[] functions = aggregations[i].getAggregationFunctions( );
			if( functions == null )
			{
				continue;
			}
			for ( int j = 0; j < functions.length; j++ )
			{
				DimColumn paraCol = functions[j].getParaCol( );
				if( paraCol != null )
				{
					paraCols.add( paraCol );
				}
			}
		}
		if( paraCols.size( ) == 0 )
		{
			return;
		}
		paraColumns = new DimColumn[paraCols.size( )];
		paraCols.toArray( paraColumns );
		paraInfos = new ColumnInfo[paraColumns.length];
		findColumnIndex( );
	}
	
	/**
	 * 
	 * @throws DataException
	 */
	private void findColumnIndex( ) throws DataException
	{
		if( paraColumns == null )
		{
			return;
		}
		IDataSet4Aggregation.MetaInfo metaInfo = dataSet4Aggregation.getMetaInfo( );
		for ( int i = 0; i < paraColumns.length; i++ )
		{
			paraInfos[i] = metaInfo.getColumnInfo( paraColumns[i] );
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

		assert row1.getLevelMembers().length == row2.getLevelMembers().length;

		for ( int i = 0; i < row1.getLevelMembers().length; i++ )
		{
			if ( sortType == null
					|| sortType.length <= i
					|| sortType[i] == IDimensionSortDefn.SORT_UNDEFINED
					|| sortType[i] == IDimensionSortDefn.SORT_ASC )
			{
				if ( row1.getLevelMembers()[i].compareTo( row2.getLevelMembers()[i] ) < 0 )
				{
					return -1;
				}
				else if ( row1.getLevelMembers()[i].compareTo( row2.getLevelMembers()[i] ) > 0 )
				{
					return 1;
				}
			}
			else
			{
				if ( row1.getLevelMembers()[i].compareTo( row2.getLevelMembers()[i] ) < 0 )
				{
					return 1;
				}
				else if ( row1.getLevelMembers()[i].compareTo( row2.getLevelMembers()[i] ) > 0 )
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
