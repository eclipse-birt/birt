
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
package org.eclipse.birt.data.engine.olap.data.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.archive.FileArchiveReader;
import org.eclipse.birt.core.archive.FileArchiveWriter;
import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.olap.api.cube.ICube;
import org.eclipse.birt.data.engine.olap.api.cube.IDimension;
import org.eclipse.birt.data.engine.olap.api.cube.StopSign;
import org.eclipse.birt.data.engine.olap.data.document.IDocumentManager;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationFunctionDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationResultSetSaveUtil;
import org.eclipse.birt.data.engine.olap.data.impl.Constants;
import org.eclipse.birt.data.engine.olap.data.impl.Cube;
import org.eclipse.birt.data.engine.olap.data.impl.SelectionFactory;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.AggregationExecutor;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Dimension;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.DimensionResultIterator;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.DimensionRow;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Level;
import org.eclipse.birt.data.engine.olap.data.impl.facttable.FactTableRowIterator;
import org.eclipse.birt.data.engine.olap.data.util.BufferedPrimitiveDiskArray;
import org.eclipse.birt.data.engine.olap.data.util.IDiskArray;
import org.eclipse.birt.data.engine.olap.util.filter.DimensionFilterEvalHelper;
import org.eclipse.birt.data.engine.olap.util.filter.IJsFilter;

/**
 * 
 */

public class CubeQueryExecutorHelper implements ICubeQueryExcutorHelper
{
	private Cube cube;
	private List filters = null;
	private List aggrFilters = null;
	private Map dimJSFilterMap = null;
	private Map dimRowForFilterMap = null;
	
	/**
	 * 
	 * @param cube
	 */
	public CubeQueryExecutorHelper( ICube cube )
	{
		this.cube = (Cube) cube;
		this.filters = new ArrayList( );
		this.aggrFilters = new ArrayList( );
		this.dimJSFilterMap = new HashMap( );
		this.dimRowForFilterMap = new HashMap( );
	}
	
	/**
	 * 
	 * @param cube
	 * @throws BirtException 
	 * @throws IOException 
	 */
	public static ICube loadCube( String cubeName,
			IDocumentManager documentManager, StopSign stopSign ) throws IOException, DataException
	{
		Cube cube = new Cube( cubeName, documentManager );
		cube.load( stopSign );
		return cube;
	}
	
	/**
	 * 
	 * @param name
	 * @param resultSets
	 * @param writer
	 * @throws IOException
	 */
	public static void saveAggregationResultSet( IDocArchiveWriter writer, String name, IAggregationResultSet[] resultSets ) throws IOException
	{
		AggregationResultSetSaveUtil.save( name, resultSets, writer );
	}
	
	/**
	 * 
	 * @param name
	 * @param resultSets
	 * @throws IOException
	 */
	public static void saveAggregationResultSet( String pathName ,String name, IAggregationResultSet[] resultSets ) throws IOException
	{
		IDocArchiveWriter writer = new FileArchiveWriter( pathName );
		AggregationResultSetSaveUtil.save( name, resultSets, writer );
		writer.flush( );
		writer.finish( );
	}
	
	
	/**
	 * 
	 * @param name
	 * @param reader
	 * @return
	 * @throws IOException
	 */
	public static IAggregationResultSet[] loadAggregationResultSet( IDocArchiveReader reader, String name ) throws IOException
	{
		return AggregationResultSetSaveUtil.load( name, reader );
	}
	
	/**
	 * 
	 * @param pathName
	 * @param name
	 * @return
	 * @throws IOException
	 */
	public static IAggregationResultSet[] loadAggregationResultSet( String pathName, String name ) throws IOException
	{
		IDocArchiveReader reader = new FileArchiveReader( pathName );
		IAggregationResultSet[] result = AggregationResultSetSaveUtil.load( name, reader );
		reader.close( );
		return result;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.ICubeQueryExcutorHelper#addFilter(java.lang.String, org.eclipse.birt.data.olap.data.api.ISelection[])
	 */
	public void addFilter( String levelName, ISelection[] selections )
	{
		LevelFilter filter = new LevelFilter();
		filter.levelName = levelName;
		filter.selections = selections;
		filters.add( filter );
	}
	
	/**
	 * @param filters
	 */
	private void addLevelFilters( List filters )
	{
		for ( Iterator i = filters.iterator( ); i.hasNext( ); )
		{
			LevelFilter filter = (LevelFilter) i.next( );
			filters.add( filter );
		}
	}
	
	/**
	 * add an aggregation filter.
	 * @param aggrFilter
	 */
	public void addAggrFilter( AggrFilter aggrFilter )
	{
		aggrFilters.add( aggrFilter );
	}
	

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.ICubeQueryExcutorHelper#clear()
	 */
	public void clear( )
	{
		filters.clear( );
		aggrFilters.clear( );
		dimJSFilterMap.clear( );
		dimRowForFilterMap.clear( );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.ICubeQueryExcutorHelper#close()
	 */
	public void close( )
	{
		filters = null;
		aggrFilters = null;
		dimJSFilterMap = null;
		dimRowForFilterMap = null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.ICubeQueryExcutorHelper#excute(org.eclipse.birt.data.olap.data.impl.AggregationDefinition[], org.eclipse.birt.data.olap.data.impl.StopSign)
	 */
	public IAggregationResultSet[] execute(
			AggregationDefinition[] aggregation, StopSign stopSign )
			throws IOException, BirtException
	{
		IAggregationResultSet[] resultSet = onePassExecute( aggregation,
				stopSign );
		if ( aggrFilters.isEmpty( ) == false )
		{// find level filters according to the specified aggregation filters
			List newAddFilters = generateLevelFilters( aggregation, resultSet );
			List oldFilters = new ArrayList( filters );
			// add new filters for another aggregation computation
			addLevelFilters( newAddFilters );
			// recompute the aggregation according to new filters and return the
			// result
			resultSet = onePassExecute( aggregation, stopSign );
			filters = oldFilters;//restore to original filter list to avoid conflict
		}
		return resultSet;
	}

	/**
	 * This method is responsible for computing the aggregation result according to the specified aggregation definitions.
	 * @param aggregation
	 * @param stopSign
	 * @return
	 * @throws DataException
	 * @throws IOException
	 * @throws BirtException
	 */
	private IAggregationResultSet[] onePassExecute(
			AggregationDefinition[] aggregation, StopSign stopSign )
			throws DataException, IOException, BirtException
	{
		String[][] allResultLevels = getAllResultLevels( aggregation );
		IDiskArray[] dimPosition = getFilterResult( );

		int count = 0;
		for ( int i = 0; i < dimPosition.length; i++ )
		{
			if ( dimPosition[i] != null )
			{
				count++;
			}
		}
		IDimension[] dimensions = cube.getDimesions( );
		String[] validDimensionName = new String[count];
		IDiskArray[] validDimPosition = new IDiskArray[count];
		int pos = 0;
		for ( int i = 0; i < dimPosition.length; i++ )
		{
			if ( dimPosition[i] != null )
			{
				validDimPosition[pos] = dimPosition[i];
				validDimensionName[pos] = dimensions[i].getName( );
				pos++;
			}
		}
		FactTableRowIterator facttableRowIterator = new FactTableRowIterator( cube.getFactTable( ),
				validDimensionName,
				validDimPosition,
				stopSign );

		DimensionResultIterator[] dimensionResultIterator = populateDimensionResultIterator( allResultLevels,
				dimPosition );

		AggregationExecutor aggregationCalculatorExecutor = new AggregationExecutor( dimensionResultIterator,
				facttableRowIterator,
				aggregation );
		return aggregationCalculatorExecutor.execute( stopSign );
	}
	
	/**
	 * generate dimension(level) filter according to the specified aggregation
	 * filters that based on the aggregate definisiton and resultset.
	 * @param aggregation
	 * @param resultSet
	 * @return
	 * @throws IOException
	 * @throws DataException
	 */
	private List generateLevelFilters( AggregationDefinition[] aggregation,
			IAggregationResultSet[] resultSet ) throws IOException,
			DataException
	{
		List levelFilters = new ArrayList( );
		for ( Iterator i = aggrFilters.iterator( ); i.hasNext( ); )
		{
			AggrFilter filter = (AggrFilter) i.next( );
			String[] levels = filter.getAggrLevels( );
			for ( int j = 0; j < aggregation.length; j++ )
			{
				if ( isEqualLevels( aggregation[j].getLevelNames( ), levels ) )
				{
					RowForFilter row = getResultRow( levels,
							aggregation[j],
							resultSet[j] );
					boolean isFilter = filter.getAggrFilter( )
							.evaluateFilter( row );
					if ( isFilter )
					{// generate level filter here
						Object[] selectedKey = new Object[]{
							row.getLevelValue( filter.getTargetLevel( ) )
						};
						ISelection selection = SelectionFactory.createOneKeySelection( selectedKey );
						LevelFilter levelFilter = new LevelFilter( );
						levelFilter.levelName = filter.getTargetLevel( );
						levelFilter.selections = new ISelection[]{
							selection
						};
						levelFilters.add( levelFilter );
					}
				}
			}
		}
		return levelFilters;
	}
	
	/**
	 * construct a row that contains all the level and aggregation for filtering.
	 * @param levels
	 * @param aggrDefn
	 * @param resultSet
	 * @return a row instance for filtering.
	 * @throws IOException
	 */
	private RowForFilter getResultRow( String[] levels,
			AggregationDefinition aggrDefn, IAggregationResultSet resultSet )
			throws IOException
	{
		AggregationFunctionDefinition[] aggrFuncs = aggrDefn.getAggregationFunctions( );
		Object[] levelValues = new Object[levels.length];
		String[] aggrNames = new String[aggrFuncs.length];
		Object[] aggrValues = new Object[aggrFuncs.length];
		// fill level values
		for ( int i = 0; i < levels.length; i++ )
		{
			int levelIndex = resultSet.getLevelIndex( levels[i] );
			levelValues[i] = resultSet.getLevelKeyValue( levelIndex );
		}
		// fill aggregation names and values
		for ( int i = 0; i < aggrFuncs.length; i++ )
		{
			aggrNames[i] = aggrFuncs[i].getName( );
			int aggregationIndex = resultSet.getAggregationIndex( aggrFuncs[i].getName( ) );
			aggrValues[i] = resultSet.getAggregationValue( aggregationIndex );
		}
		// generate a row against levels and aggrNames
		RowForFilter row = new RowForFilter( levels, aggrNames );
		row.setLevelValues( levelValues );
		row.setDataValues( aggrValues );
		return row;
	}
	
	/**
	 * compare two level arrays to determine whether they are equal or not.
	 * @param levelNames1
	 * @param levelNames2
	 * @return
	 */
	private boolean isEqualLevels( String[] levelNames1, String[] levelNames2 )
	{
		if ( levelNames1.length != levelNames2.length )
			return false;
		for ( int i = 0; i < levelNames1.length; i++ )
		{
			if ( levelNames1[i].equals( levelNames2[i] ) == false )
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * @param aggregation
	 * @return
	 * @throws DataException 
	 */
	private String[][] getAllResultLevels( AggregationDefinition[] aggregation ) throws DataException
	{
		IDimension[] dimensions = cube.getDimesions( );
		String[][] dimLevels = getAllLevelNames( dimensions );
		List[] dimLevelList = new List[dimensions.length];
		
		for ( int i = 0; i < dimLevelList.length; i++ )
		{
			dimLevelList[i] = new ArrayList( );
		}
		List allAggregationLevel = new ArrayList( );
		for ( int i = 0; i < aggregation.length; i++ )
		{
			if ( aggregation[i].getLevelNames( ) != null )
			{
				for ( int j = 0; j < aggregation[i].getLevelNames( ).length; j++ )
				{
					allAggregationLevel.add( aggregation[i].getLevelNames( )[j] );
				}
			}
		}
		Object[] distinctAggregationLevel = removeDuplicated( allAggregationLevel.toArray( ) );
		for ( int i = 0; i < distinctAggregationLevel.length; i++ )
		{
			dimLevelList[getIndex( dimLevels,
					(String)distinctAggregationLevel[i] )].add( distinctAggregationLevel[i] );
		}
		String[][] result = new String[dimensions.length][];
		for ( int i = 0; i < dimensions.length; i++ )
		{
			if( dimLevelList[i].size( ) == 0 )
			{
				continue;
			}
			result[i] = new String[dimLevelList[i].size( )];
			System.arraycopy( dimLevelList[i].toArray( ),
					0,
					result[i],
					0,
					dimLevelList[i].size( ) );
		}
		return result;
	}
	
	/**
	 * 
	 * @param objs
	 * @return
	 */
	private static Object[] removeDuplicated( Object[] objs )
	{
		Arrays.sort( objs );
		List result = new ArrayList( );
		result.add( objs[0] );
		for ( int i = 1; i < objs.length; i++ )
		{
			if(((Comparable)objs[i]).compareTo( objs[i-1] )!=0)
			{
				result.add( objs[i] );
			}
		}
		return result.toArray( );
	}
	
	/**
	 * 
	 * @param levelNameArray
	 * @param levelName
	 * @return
	 * @throws DataException 
	 */
	private static int getIndex( String[][] levelNameArray, String levelName ) throws DataException
	{
		for ( int i = 0; i < levelNameArray.length; i++ )
		{
			for ( int j = 0; j < levelNameArray[i].length; j++ )
			{
				if ( levelNameArray[i][j].equals( levelName ) )
				{
					return i;
				}
			}
		}
		throw new DataException( ResourceConstants.MEASURE_NAME_NOT_FOUND,
				levelName );
	}
	
	/**
	 * 
	 * @param dimensions
	 * @return
	 */
	private static String[][] getAllLevelNames( IDimension[] dimensions )
	{
		String[][] result = new String[dimensions.length][];
		for ( int i = 0; i < result.length; i++ )
		{
			ILevel[] levels = dimensions[i].getHierarchy( ).getLevels( );
			result[i] = new String[levels.length];
			for( int j=0;j<levels.length;j++)
			{
				result[i][j] = levels[j].getName( );
			}
		}
		return result;
	}
	
	/**
	 * 
	 * @param resultLevel
	 * @param position
	 * @return
	 * @throws DataException
	 * @throws IOException
	 */
	private DimensionResultIterator[] populateDimensionResultIterator( String[][] resultLevel,
			IDiskArray[] position ) throws DataException, IOException
	{
		IDimension[] dimensions = cube.getDimesions( );
		DimensionResultIterator[] dimResultSet = new DimensionResultIterator[dimensions.length];
		int count = 0;
		for ( int i = 0; i < dimensions.length; i++ )
		{
			if ( resultLevel[i] != null )
			{
				if ( position[i] == null )
				{
					dimResultSet[i] = new DimensionResultIterator( (Dimension) dimensions[i],
							dimensions[i].findAll( ),
							resultLevel[i] );
				}
				else
				{
					dimResultSet[i] = new DimensionResultIterator( (Dimension) dimensions[i],
							position[i],
							resultLevel[i] );
				}
				count++;
			}
		}
		
		DimensionResultIterator[] result = new DimensionResultIterator[count];
		int pos = 0;
		for( int i=0;i<dimResultSet.length;i++)
		{
			if( dimResultSet[i] != null )
			{
				result[pos] = dimResultSet[i];
				pos++;
			}
		}
		return result;
	}
	
	
	/**
	 * 
	 * @param dimension
	 * @param dimPosition
	 * @return
	 * @throws DataException
	 * @throws IOException
	 */
	private IDiskArray getJSFilterResult( Dimension dimension, IDiskArray dimPosition ) throws DataException, IOException
	{
		if ( dimPosition == null )
		{
			if(getDimensionJSFilterList( dimension.getName( ) ).size() <= 0)
			{
				return null;
			}
			else
			{
				dimPosition = dimension.findAll( );
			}
		}
		IDiskArray result = new BufferedPrimitiveDiskArray( Constants.LIST_BUFFER_SIZE );
		for ( int i = 0; i < dimPosition.size( ); i++ )
		{
			Integer pos =(Integer) dimPosition.get( i );
			if ( getJSFilterResult( dimension, pos.intValue( ) ) )
				result.add( pos );
		}
		return result;
	}

	/**
	 * 
	 * @param dimension
	 * @param pos
	 * @throws IOException
	 * @throws DataException
	 */
	private boolean getJSFilterResult( Dimension dimension, int pos ) throws IOException, DataException
	{
		DimensionRow dimRow = dimension.getRowByPosition( pos );
		List filterList = getDimensionJSFilterList( dimension.getName( ) );
		RowForFilter rowForFilter = getRowForFilter( dimension );
		Object[] fieldValues = getAllFields( dimRow );
		rowForFilter.setLevelValues( fieldValues );
		
		for ( int j = 0; j < filterList.size( ); j++ )
		{
			IJsFilter filterHelper = (IJsFilter) filterList.get( j );
			if( !filterHelper.evaluateFilter( rowForFilter ) )
			{
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 
	 * @param dimensionName
	 * @return
	 */
	private List getDimensionJSFilterList( String dimensionName )
	{
		Object value = dimJSFilterMap.get( dimensionName );
		if( value != null )
		{
			return (List)value;
		}
		List list = new ArrayList();
		dimJSFilterMap.put( dimensionName, list );
		return list;
	}
	
	/**
	 * 
	 * @param dimension
	 * @return
	 */
	private RowForFilter getRowForFilter( Dimension dimension )
	{
		Object value = dimRowForFilterMap.get( dimension.getName( ) );
		if ( value != null )
		{
			return (RowForFilter) value;
		}
		String[] fieldNames = getAllFieldNames( dimension );
		RowForFilter rowForFilter = new RowForFilter( fieldNames );
		dimRowForFilterMap.put( dimension.getName( ), rowForFilter );
		return rowForFilter;
	}
	
	/**
	 * 
	 * @param dimension
	 * @return
	 */
	private static String[] getAllFieldNames( Dimension dimension )
	{
		ILevel[] levels = dimension.getHierarchy( ).getLevels( );
		List fieldNames = new ArrayList( );
		for ( int i = 0; i < levels.length; i++ )
		{
			String[] keyNames = levels[i].getKeyName( );
			if( keyNames!=null )
			{
				for ( int j = 0; j < keyNames.length; j++ )
				{
					fieldNames.add( keyNames[j] );
				}
			}
			String[] attrNames = levels[i].getAttributeNames( );
			if ( attrNames != null )
			{
				for ( int j = 0; j < attrNames.length; j++ )
				{
					fieldNames.add( attrNames[j] );
				}
			}
		}
		String[] result = new String[fieldNames.size( )];
		for( int i=0;i<result.length;i++)
		{
			result[i] = (String) fieldNames.get( i );
		}
		return result;
	}
	
	/**
	 * 
	 * @param dimRow
	 * @return
	 */
	private static Object[] getAllFields( DimensionRow dimRow )
	{
		List fields = new ArrayList( );
		for ( int i = 0; i < dimRow.getMembers().length; i++ )
		{
			if ( dimRow.getMembers()[i].getKeyValues() != null )
			{
				for ( int j = 0; j < dimRow.getMembers()[i].getKeyValues().length; j++ )
				{
					fields.add( dimRow.getMembers()[i].getKeyValues()[j] );
				}
			}
			if ( dimRow.getMembers()[i].getAttributes() != null )
			{
				for ( int j = 0; j < dimRow.getMembers()[i].getAttributes().length; j++ )
				{
					fields.add( dimRow.getMembers()[i].getAttributes()[j] );
				}
			}
		}
		return fields.toArray( );
	}
	
	/**
	 * 
	 * @return
	 * @throws DataException
	 * @throws IOException
	 */
	private IDiskArray[] getFilterResult( ) throws DataException, IOException
	{
		IDimension[] dimensions = cube.getDimesions( );
		IDiskArray[] dimPosition = new IDiskArray[dimensions.length];
		for ( int i = 0; i < dimPosition.length; i++ )
		{
			dimPosition[i] = getJSFilterResult( (Dimension) dimensions[i],
					getSimpleFilterResult( (Dimension) dimensions[i] ) );
		}
		return dimPosition;
	}
	
	/**
	 * 
	 * @param dimension
	 * @return
	 * @throws DataException
	 * @throws IOException
	 */
	private IDiskArray getSimpleFilterResult( Dimension dimension ) throws DataException, IOException
	{
		ILevel[] levels = dimension.getHierarchy( ).getLevels( );
		ISelection[][] selections = new ISelection[levels.length][];
		int filterCount = 0;
		for ( int i = 0; i < filters.size( ); i++ )
		{
			LevelFilter filter = (LevelFilter)filters.get( i );
			int index = getIndex( levels, filter.levelName );
			if ( index >= 0 && selections[index] == null )
			{
				selections[index] = filter.selections;
				filterCount ++;
			}
		}
		if(filterCount==0)
		{
			return null;
		}
		Level[] filterLevel = new Level[filterCount];
		ISelection[][] filters = new ISelection[filterCount][];
		int pos = 0;
		for( int i=0;i<selections.length;i++)
		{
			if ( selections[i] != null )
			{
				filterLevel[pos] = (Level)levels[i];
				filters[pos] = selections[i];
				pos++;
			}
		}
		return dimension.find( filterLevel, filters );
	}
	
	/**
	 * 
	 * @param levels
	 * @param levelName
	 * @return
	 */
	private int getIndex( ILevel[] levels, String levelName )
	{
		for( int i=0;i<levels.length;i++)
		{
			if( levels[i].getName( ).equals( levelName ))
			{
				return i;
			}
		}
		return -1;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.data.api.ICubeQueryExcutorHelper#addJSFilter(org.eclipse.birt.data.engine.olap.util.filter.DimensionFilterEvalHelper)
	 */
	public void addJSFilter( DimensionFilterEvalHelper filterEvalHelper )
	{
		String dimesionName = filterEvalHelper.getDimensionName( );
		List filterList = getDimensionJSFilterList( dimesionName );
		filterList.add( filterEvalHelper );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.data.api.ICubeQueryExcutorHelper#addJSFilter(java.util.List)
	 */
	public void addJSFilter( List filterEvalHelperList )
	{
		for ( int i = 0; i < filterEvalHelperList.size( ); i++ )
		{
			addJSFilter( (DimensionFilterEvalHelper) filterEvalHelperList.get( i ) );
		}
	}
}

/**
 * 
 * @author Administrator
 *
 */
class LevelFilter
{
	String dimensionName;
	String levelName;
	ISelection[] selections;
}
