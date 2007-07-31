
/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.olap.data.api;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.eclipse.birt.core.archive.FileArchiveReader;
import org.eclipse.birt.core.archive.FileArchiveWriter;
import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.document.stream.VersionManager;
import org.eclipse.birt.data.engine.olap.api.query.ICubeFilterDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ILevelDefinition;
import org.eclipse.birt.data.engine.olap.data.api.cube.ICube;
import org.eclipse.birt.data.engine.olap.data.api.cube.IDimension;
import org.eclipse.birt.data.engine.olap.data.api.cube.StopSign;
import org.eclipse.birt.data.engine.olap.data.document.IDocumentManager;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationFunctionDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationResultSetSaveUtil;
import org.eclipse.birt.data.engine.olap.data.impl.Constants;
import org.eclipse.birt.data.engine.olap.data.impl.Cube;
import org.eclipse.birt.data.engine.olap.data.impl.SelectionFactory;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.AggregationExecutor;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.filter.LevelFilter;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.filter.TopBottomFilter;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.sort.AggrSortDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.sort.AggrSortHelper;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Dimension;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.DimensionResultIterator;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.DimensionRow;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Level;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Member;
import org.eclipse.birt.data.engine.olap.data.impl.facttable.FactTableRowIterator;
import org.eclipse.birt.data.engine.olap.data.util.BufferedPrimitiveDiskArray;
import org.eclipse.birt.data.engine.olap.data.util.CompareUtil;
import org.eclipse.birt.data.engine.olap.data.util.IDiskArray;
import org.eclipse.birt.data.engine.olap.data.util.OrderedDiskArray;
import org.eclipse.birt.data.engine.olap.data.util.SelectionUtil;
import org.eclipse.birt.data.engine.olap.util.filter.IJSDimensionFilterHelper;
import org.eclipse.birt.data.engine.olap.util.filter.IJSFilterHelper;
import org.eclipse.birt.data.engine.olap.util.filter.IJSTopBottomFilterHelper;
import org.eclipse.birt.data.engine.olap.util.filter.IResultRow;

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
	
	private List rowSort = null;
	private List columnSort = null;
	private List topbottomFilters;
	
	private boolean isEmptyXTab;
	private boolean isBreakHierarchy = true;
	
	private IComputedMeasureHelper computedMeasureHelper = null;
	
	private static Logger logger = Logger.getLogger( CubeQueryExecutorHelper.class.getName( ) );
	
	/**
	 * 
	 * @param cube
	 */
	public CubeQueryExecutorHelper( ICube cube )
	{
		logger.entering( CubeQueryExecutorHelper.class.getName( ),
				"CubeQueryExecutorHelper",
				cube );
		this.cube = (Cube) cube;
		this.filters = new ArrayList( );
		this.aggrFilters = new ArrayList( );
		this.dimJSFilterMap = new HashMap( );
		this.dimRowForFilterMap = new HashMap( );
		
		this.rowSort = new ArrayList( );
		this.columnSort = new ArrayList( );
		this.topbottomFilters = new ArrayList( );
		logger.exiting( CubeQueryExecutorHelper.class.getName( ),
				"CubeQueryExecutorHelper" );
	}
	
	/**
	 * TODO: get the members according to the specified level.
	 * @param level
	 * @return
	 */
	public IDiskArray getLevelMembers( DimLevel level )
	{
		return null;
	}
	
	/**
	 * get the attribute reference name.
	 * @param dimName
	 * @param levelName
	 * @param attrName
	 * @return
	 */
	public static String getAttrReference( String dimName, String levelName, String attrName )
	{
		return dimName + '/' + levelName + '/' + attrName;
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
		IDocArchiveWriter writer = new FileArchiveWriter( getTmpFileName( pathName, name ) );
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
		return AggregationResultSetSaveUtil.load( name, reader, VersionManager.getLatestVersion( ) );
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
		IDocArchiveReader reader = new FileArchiveReader( getTmpFileName( pathName,
				name ) );
		IAggregationResultSet[] result = AggregationResultSetSaveUtil.load( name,
				reader,
				VersionManager.getLatestVersion( ) );
		reader.close( );
		return result;
	}
	
	/**
	 * 
	 * @param pathName
	 * @param name
	 * @return
	 */
	private static String getTmpFileName( String pathName, String name )
	{
		return pathName + File.separator + "cubequeryresult" +name;
	}
	
	/**
	 * 
	 * @param computedMeasureHelper
	 */
	public void setComputedMeasure( IComputedMeasureHelper computedMeasureHelper )
	{
		this.computedMeasureHelper = computedMeasureHelper;
	}
	
	/**
	 * 
	 * @param sort
	 */
	public void addRowSort( AggrSortDefinition sort )
	{
		this.rowSort.add( sort );
	}
	
	/**
	 * 
	 * @return sortDefinition list on row edge
	 */
	public List getRowSort( )
	{
		return this.rowSort;
	}
	
	/**
	 * 
	 * @return sortDefinition list on column edge
	 */
	public List getColumnSort( )
	{
		return this.columnSort;
	}
	
	/**
	 * 
	 * @param sort
	 */
	public void addColumnSort( AggrSortDefinition sort )
	{
		this.columnSort.add( sort );
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.ICubeQueryExcutorHelper#addFilter(java.lang.String, org.eclipse.birt.data.olap.data.api.ISelection[])
	 */
	public void addFilter( LevelFilter levelFilter )
	{		
		filters.add( levelFilter );
	}
	
	/**
	 * @param levelFilterList
	 */
	private void addLevelFilters( List levelFilterList )
	{
		this.filters.addAll( levelFilterList );
	}
	

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.ICubeQueryExcutorHelper#clear()
	 */
	public void clear( )
	{
		filters.clear( );
		aggrFilters.clear( );
		topbottomFilters.clear( );
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
		topbottomFilters = null;
		dimJSFilterMap = null;
		dimRowForFilterMap = null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.ICubeQueryExcutorHelper#excute(org.eclipse.birt.data.olap.data.impl.AggregationDefinition[], org.eclipse.birt.data.olap.data.impl.StopSign)
	 */
	public IAggregationResultSet[] execute(
			AggregationDefinition[] aggregations, StopSign stopSign )
			throws IOException, BirtException
	{
		IAggregationResultSet[] resultSet = onePassExecute( aggregations,
				stopSign );
		
		applyAggrFilters( aggregations, resultSet, stopSign );
		
		applyAggrSort( resultSet );
		
		return resultSet;
	}	
	
	/**
	 * 
	 * @param aggregations
	 * @param resultSet
	 * @param levelFilterList
	 * @throws IOException 
	 * @throws DataException 
	 */
	private void applyTopBottomFilters( AggregationDefinition[] aggregations,
			IAggregationResultSet[] resultSet, List levelFilterList) throws DataException, IOException
	{
		for ( int i = 0; i < aggregations.length; i++ )
		{
			if ( aggregations[i].getAggregationFunctions( ) == null )
				continue;
			Map levelFilterMap = new HashMap();
			for ( Iterator j = topbottomFilters.iterator( ); j.hasNext( ); )
			{
				TopBottomFilter filter = (TopBottomFilter) j.next( );
				if ( filter.getFilterHelper( ).isAggregationFilter( ) )
				{// aggregation top/bottom filter
					if ( isEqualLevels( aggregations[i].getLevels( ),
							filter.getAggrLevels( ) ) )
					{
						IDiskArray levelKeyList = populateLevelKeyList( aggregations[i],
								resultSet[i],
								filter );
						IDiskArray selectedLevelKeys = null;
						if ( levelFilterMap.containsKey( filter.getTargetLevel( ) ) )
						{
							selectedLevelKeys = (IDiskArray) levelFilterMap.get( filter.getTargetLevel( ) );
							selectedLevelKeys = interKeys( selectedLevelKeys,
									levelKeyList );
						}
						else
						{
							selectedLevelKeys = levelKeyList;
						}
						levelFilterMap.put( filter.getTargetLevel( ),
								selectedLevelKeys );
					}
				}			
			}
			// generate level filters according to the selected level keys
			for ( Iterator j = levelFilterMap.keySet( ).iterator( ); j.hasNext( ); )
			{
				DimLevel target = (DimLevel) j.next( );
				IDiskArray selectedKeyArray = (IDiskArray) levelFilterMap.get( target );
				if ( selectedKeyArray.size( ) == 0 )
					continue;
				Object[][] keys = new Object[selectedKeyArray.size( )][];
				for ( int k = 0; k < keys.length; k++ )
				{
					keys[k] = ( (MultiKey) selectedKeyArray.get( k ) ).levelKey;
				}
				ISelection selection = SelectionFactory.createMutiKeySelection( keys );
				LevelFilter filter = new LevelFilter( target, new ISelection[]{
					selection
				} );
				levelFilterList.add( filter );				
			}
		}
	}

	/***
	 * 
	 * @param levelKeyArray1
	 * @param levelKeyArray2
	 * @return 
	 * @throws IOException 
	 */
	private IDiskArray interKeys( IDiskArray levelKeyArray1, IDiskArray levelKeyArray2 ) throws IOException
	{
		IDiskArray result = new OrderedDiskArray( );
		int i = 0, j = 0;
		while ( i < levelKeyArray1.size( ) && j < levelKeyArray2.size( ) )
		{
			MultiKey key1 = (MultiKey) levelKeyArray1.get( i );
			MultiKey key2 = (MultiKey) levelKeyArray2.get( j );
			int ret = key1.compareTo( key2 );
			if ( ret == 0 )
			{
				result.add( key1 );
				i++;
				j++;
			}
			else if ( ret < 0 )
			{
				i++;
			}
			else
				j++;
		}
		levelKeyArray1.close( );
		levelKeyArray2.close( );
		return result;
	}
	
	/**
	 * 
	 * @param result
	 * @param dimPositionArray
	 * @return
	 * @throws IOException 
	 */
	private IDiskArray interPosition( IDiskArray posArray1,
			IDiskArray posArray2 ) throws IOException
	{
		IDiskArray result = new OrderedDiskArray( );
		int i = 0, j = 0;
		while ( i < posArray1.size( ) && j < posArray2.size( ) )
		{
			Integer pos1 = (Integer) posArray1.get( i );
			Integer pos2 = (Integer) posArray2.get( j );
			int ret = pos1.compareTo( pos2 );
			if ( ret == 0 )
			{
				result.add( pos1 );
				i++;
				j++;
			}
			else if ( ret < 0 )
			{
				i++;
			}
			else
				j++;
		}
		posArray1.close( );
		posArray2.close( );
		return result;
	}

	/**
	 * @param resultSet
	 * @throws DataException
	 */
	private void applyAggrSort( IAggregationResultSet[] resultSet )
			throws DataException
	{
		if ( !this.columnSort.isEmpty( ) )
		{
			IAggregationResultSet column = AggrSortHelper.sort( this.columnSort,
					resultSet );
			resultSet[findMatchedResultSetIndex( resultSet, column )] = column;
		}
		if ( !this.rowSort.isEmpty( ) )
		{
			IAggregationResultSet row = AggrSortHelper.sort( this.rowSort,
					resultSet );
			resultSet[findMatchedResultSetIndex( resultSet, row )] = row;
		}
	}

	/**
	 * @param aggregations
	 * @param resultSet
	 * @param stopSign
	 * @throws IOException
	 * @throws DataException
	 * @throws BirtException
	 */
	private void applyAggrFilters( AggregationDefinition[] aggregations,
			IAggregationResultSet[] resultSet, StopSign stopSign )
			throws IOException, DataException, BirtException
	{
		if ( aggrFilters.isEmpty( ) == false
				|| topbottomFilters.isEmpty( ) == false )
		{
			List oldFilters = new ArrayList( filters );
			// add new filters for another aggregation computation
			addLevelFilters( generateLevelFilters( aggregations, resultSet ) );
			if ( isEmptyXTab )
			{
				for ( int i = 0; i < resultSet.length; i++ )
				{// clear all aggregation result sets to be empty
					resultSet[i].clear( );
				}
			}
			else
			{
				for ( int i = 0; i < resultSet.length; i++ )
				{// release all previous aggregation result sets
					resultSet[i].close( );
					resultSet[i] = null;
				}
				// recalculate the aggregation according to new filters
				IAggregationResultSet[] temp = onePassExecute( aggregations,
						stopSign );
				// overwrite result with the second pass aggregation result set
				System.arraycopy( temp, 0, resultSet, 0, resultSet.length );
			}
			// restore to original filter list to avoid conflict
			filters = oldFilters;
		}
	}
	 
		
	/**
	 * @param rSets
	 * @param source
	 * @return
	 * @throws DataException
	 */
	private int findMatchedResultSetIndex( IAggregationResultSet[] rSets, IAggregationResultSet source ) throws DataException
	{
		for( int i = 0; i < rSets.length; i++ )
		{
			if( AggrSortHelper.isEdgeResultSet( rSets[i] ))
			{
				if( source.getLevel( 0 ).equals( rSets[i].getLevel( 0 ) ))
					return i;
			}
		}
		throw new DataException("Invalid");
	}
	
	/**
	 * This method is responsible for computing the aggregation result according
	 * to the specified aggregation definitions.
	 * @param aggregations
	 * @param stopSign
	 * @return
	 * @throws DataException
	 * @throws IOException
	 * @throws BirtException
	 */
	private IAggregationResultSet[] onePassExecute(
			AggregationDefinition[] aggregations, StopSign stopSign )
			throws DataException, IOException, BirtException
	{
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
				computedMeasureHelper,
				stopSign );

		DimensionResultIterator[] dimensionResultIterator = populateDimensionResultIterator( dimPosition );

		AggregationExecutor aggregationCalculatorExecutor = new AggregationExecutor( dimensionResultIterator,
				facttableRowIterator,
				aggregations );
		return aggregationCalculatorExecutor.execute( stopSign );
	}
	
	/**
	 * generate level filters.
	 * @param aggregations
	 * @param resultSet
	 * @return
	 * @throws IOException
	 * @throws DataException
	 */
	private List generateLevelFilters( AggregationDefinition[] aggregations,
			IAggregationResultSet[] resultSet ) throws IOException,
			DataException
	{
		List levelFilterList = new ArrayList( );
		for ( Iterator i = aggrFilters.iterator( ); i.hasNext( ); )
		{
			AggrFilter filter = (AggrFilter) i.next( );
			for ( int j = 0; !isEmptyXTab && j < aggregations.length; j++ )
			{
				if ( aggregations[j].getAggregationFunctions( ) != null
						&& isEqualLevels( aggregations[j].getLevels( ),
								filter.getAggrLevels( ) ) )
				{
					applyAggrFilter( aggregations,
							resultSet,
							j,
							filter,
							levelFilterList );
				}
			}
		}
		applyTopBottomFilters( aggregations, resultSet, levelFilterList);
		return levelFilterList;
	}

	/**
	 * 
	 * @param aggregation
	 * @param resultSet
	 * @param filter
	 * @param levelFilters
	 * @return 
	 */
	private IDiskArray populateLevelKeyList( AggregationDefinition aggregation,
			IAggregationResultSet resultSet, TopBottomFilter filter )
			throws IOException, DataException
	{
		IJSTopBottomFilterHelper filterHelper = filter.getFilterHelper( );
		int n = -1;
		if ( filterHelper.isPercent( ) == false )
		{
			n = (int) filterHelper.getN( );
		}
		
		IDiskArray aggrValueArray = new OrderedDiskArray( n, filterHelper.isTop( ) );
		AggregationFunctionDefinition[] aggrFuncs = aggregation.getAggregationFunctions( );
		DimLevel[] aggrLevels = filter.getAggrLevels( );
		// currently we just support one level key
		// generate a row against levels and aggrNames
		String[] fields = getAllFieldNames( aggrLevels, resultSet );
		String[] aggrNames = new String[aggrFuncs.length];
		for ( int k = 0; k < aggrFuncs.length; k++ )
		{
			aggrNames[k] = aggrFuncs[k].getName( );
		}
		for ( int k = 0; k < resultSet.length( ); k++ )
		{
			resultSet.seek( k );
			int fieldIndex = 0;
			Object[] fieldValues = new Object[fields.length];
			Object[] aggrValues = new Object[aggrFuncs.length];
			// fill field values
			for ( int m = 0; m < aggrLevels.length; m++ )
			{
				int levelIndex = resultSet.getLevelIndex( aggrLevels[m] );
				if ( levelIndex < 0 || levelIndex >= resultSet.getLevelCount( ) )
					continue;
				fieldValues[fieldIndex++] = resultSet.getLevelKeyValue( levelIndex )[0];
			}
			// fill aggregation names and values
			for ( int m = 0; m < aggrFuncs.length; m++ )
			{
				int aggrIndex = resultSet.getAggregationIndex( aggrNames[m] );
				aggrValues[m] = resultSet.getAggregationValue( aggrIndex );
			}
			RowForFilter row = new RowForFilter( fields, aggrNames );
			row.setFieldValues( fieldValues );
			row.setAggrValues( aggrValues );
			
			int levelIndex = resultSet.getLevelIndex( filter.getTargetLevel( ) );
			Object[] levelKey = resultSet.getLevelKeyValue( levelIndex );
			if ( levelKey!=null && filterHelper.isQualifiedRow( row ) )
			{
				Object aggrValue = filterHelper.evaluateFilterExpr( row );
				aggrValueArray.add( new ValueObject( aggrValue,
						new MultiKey( levelKey ) ) );
			}
		}
		
		return fetchLevelKeys( aggrValueArray, filterHelper );
	}

	/**
	 * @param aggrValueArray
	 * @param filterHelper
	 * @return
	 * @throws IOException
	 */
	private IDiskArray fetchLevelKeys( IDiskArray aggrValueArray,
			IJSTopBottomFilterHelper filterHelper ) throws IOException
	{
		IDiskArray levelKeyArray = new OrderedDiskArray( );
		int start = 0; // level key start index in aggrValueArray
		int end   = aggrValueArray.size( ); // level key end index (not including) in aggrValueArray
		if ( filterHelper.isPercent( ) )
		{// top/bottom percentage filter
			int size = aggrValueArray.size( ); // target level member size
			int n = getTargetN( size, filterHelper.getN( ) );
			if ( filterHelper.isTop( ) )
				start = size - n;
			else
				end = n;
		}
		Object preKey = null;
		for ( int i = start; i < end; i++ )
		{
			ValueObject aggrValue = (ValueObject) aggrValueArray.get( i );
			if ( preKey == null
					|| CompareUtil.compare( preKey, aggrValue.index ) != 0 )
				levelKeyArray.add( aggrValue.index );
			preKey = aggrValue.index;
		}
		return levelKeyArray;
	}

	/**
	 * @param total
	 * @param N
	 * @return
	 */
	private final int getTargetN( long total, double N )
	{
		return (int) Math.round( N / 100 * total );
	}

	/**
	 * @param aggregations
	 * @param resultSet
	 * @param j
	 * @param filter
	 * @param levelFilters
	 * @throws IOException
	 * @throws DataException
	 */
	private void applyAggrFilter( AggregationDefinition[] aggregations,
			IAggregationResultSet[] resultSet, int j,
			AggrFilter filter, List levelFilters )
			throws IOException, DataException
	{
		List selKeyValueList = new ArrayList( );
		AggregationFunctionDefinition[] aggrFuncs = aggregations[j].getAggregationFunctions( );
		DimLevel[] aggrLevels = filter.getAggrLevels( );
		// currently we just support one level key
		// generate a row against levels and aggrNames
		String[] fields = getAllFieldNames( aggrLevels,
				resultSet[j] );
		String[] aggrNames = new String[aggrFuncs.length];
		for ( int k = 0; k < aggrFuncs.length; k++ )
		{
			aggrNames[k] = aggrFuncs[k].getName( );
		}
		for ( int k = 0; k < resultSet[j].length( ); k++ )
		{
			resultSet[j].seek( k );
			int fieldIndex = 0;
			Object[] fieldValues = new Object[fields.length];
			Object[] aggrValues = new Object[aggrFuncs.length];
			// fill field values
			for ( int m = 0; m < aggrLevels.length; m++ )
			{
				int levelIndex = resultSet[j].getLevelIndex( aggrLevels[m] );
				if ( levelIndex < 0
						|| levelIndex >= resultSet[j].getLevelCount( ) )
					continue;							
				fieldValues[fieldIndex++] = resultSet[j].getLevelKeyValue( levelIndex )[0];
				
			}
			// fill aggregation names and values
			for ( int m = 0; m < aggrFuncs.length; m++ )
			{
				int aggrIndex = resultSet[j].getAggregationIndex( aggrNames[m] );
				aggrValues[m] = resultSet[j].getAggregationValue( aggrIndex );
			}
			RowForFilter row = new RowForFilter( fields, aggrNames );
			row.setFieldValues( fieldValues );
			row.setAggrValues( aggrValues );
			boolean isSelect = filter.getFilterHelper( )
					.evaluateFilter( row );
			if ( isSelect )
			{// generate level filter here
				int levelIndex = resultSet[j].getLevelIndex( filter.getTargetLevel( ) );
				// select aggregation row
				Object[] levelKeyValue = resultSet[j].getLevelKeyValue( levelIndex );
				if ( levelKeyValue != null && levelKeyValue[0] != null )
					selKeyValueList.add( levelKeyValue );
			}
		}				
		//---------------------------------------------------------------------------------
		if ( selKeyValueList.isEmpty( ) )
		{// filter is empty, so that the final x-Tab will be empty
			isEmptyXTab = true;
		}
		else
		{
			Object[][] keyValues = new Object[selKeyValueList.size( )][];
			for ( int i = 0; i < selKeyValueList.size( ); i++ )
			{
				keyValues[i] = (Object[]) selKeyValueList.get( i );
			}
			ISelection selection = SelectionFactory.createMutiKeySelection( keyValues );
			LevelFilter levelFilter = new LevelFilter( filter.getTargetLevel( ),
					new ISelection[]{
						selection
					} );
			levelFilters.add( levelFilter );
		}
	}

	
	
	/**
	 * get all field names of a level, including key column names and attribute column names.
	 * TODO: we just get all the field names, and will further support key names and attributes as field names.
	 * @param levels
	 * @param resultSet
	 * @return
	 */
	private String[] getAllFieldNames( DimLevel[] levels,
			IAggregationResultSet resultSet )
	{
		List fieldNameList = new ArrayList( );
		for ( int i = 0; i < levels.length; i++ )
		{
			int levelIndex = resultSet.getLevelIndex( levels[i] );
			if ( levelIndex < 0 || levelIndex >= resultSet.getLevelCount( ) )
				continue;
			fieldNameList.add( levels[i].getDimensionName( )
					+ '/' + levels[i].getLevelName( ) );	
		}
		String[] fieldNames = new String[fieldNameList.size( )];
		fieldNameList.toArray( fieldNames );
		return fieldNames;
	}

	/**
	 * compare two level arrays to determine whether they are equal or not.
	 * @param levels1
	 * @param levels2
	 * @return
	 */
	private boolean isEqualLevels( DimLevel[] levels1, DimLevel[] levels2 )
	{
		if ( levels1 == null && levels2 == null )
			return true;
		else if ( levels1 == null || levels2 == null )
			return false;

		if ( levels1.length != levels2.length )
			return false;
		for ( int i = 0; i < levels1.length; i++ )
		{
			if ( levels1[i].equals( levels2[i] ) == false )
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
	/*private DimLevel[][] getAllResultLevels( AggregationDefinition[] aggregation ) throws DataException
	{
		IDimension[] dimensions = cube.getDimesions( );
		List[] dimLevelList = new List[dimensions.length];
		
		for ( int i = 0; i < dimLevelList.length; i++ )
		{
			dimLevelList[i] = new ArrayList( );
		}
		List allAggregationLevel = new ArrayList( );
		for ( int i = 0; i < aggregation.length; i++ )
		{
			if ( aggregation[i].getLevels( ) != null )
			{
				for ( int j = 0; j < aggregation[i].getLevels( ).length; j++ )
				{
					allAggregationLevel.add( aggregation[i].getLevels( )[j] );
				}
				
			}
		}
		Object[] distinctAggregationLevel = distinct( allAggregationLevel.toArray( ) );
		for ( int i = 0; i < distinctAggregationLevel.length; i++ )
		{
			dimLevelList[getDimensionIndex( dimensions,
					(DimLevel)distinctAggregationLevel[i] )].add( distinctAggregationLevel[i] );
		}
		DimLevel[][] result = new DimLevel[dimensions.length][];
		for ( int i = 0; i < dimensions.length; i++ )
		{
			if( dimLevelList[i].size( ) == 0 )
			{
				continue;
			}
			result[i] = new DimLevel[dimLevelList[i].size( )];
			System.arraycopy( dimLevelList[i].toArray( ),
					0,
					result[i],
					0,
					dimLevelList[i].size( ) );
		}
		return result;
	}*/
	
	/**
	 * 
	 * @param objs
	 * @return
	 */
	/*private static Object[] distinct( Object[] objs )
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
	}*/
	
	/**
	 * 
	 * @param levelNameArray
	 * @param level
	 * @return
	 * @throws DataException 
	 */
/*	private static int getDimensionIndex( IDimension[] dimensions,
			DimLevel level ) throws DataException
	{
		for ( int i = 0; i < dimensions.length; i++ )
		{
			if ( dimensions[i].getName( ).equals( level.getDimensionName( ) ) )
			{
				ILevel[] levels = dimensions[i].getHierarchy( ).getLevels( );
				for ( int j = 0; j < levels.length; j++ )
				{
					if ( levels[j].getName( ).equals( level.getLevelName( ) ) )
					{
						return i;
					}
				}
			}
		}
		throw new DataException( ResourceConstants.MEASURE_NAME_NOT_FOUND,
				level );
	}*/
	
	/**
	 * 
	 * @param dimensions
	 * @return
	 */
	/*private static String[][] getAllLevelNames( IDimension[] dimensions )
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
	}*/
	
	/**
	 * 
	 * @param resultLevels
	 * @param position
	 * @return
	 * @throws DataException
	 * @throws IOException
	 */
	private DimensionResultIterator[] populateDimensionResultIterator( IDiskArray[] position ) throws DataException, IOException
	{
		IDimension[] dimensions = cube.getDimesions( );
		DimensionResultIterator[] dimResultSet = new DimensionResultIterator[dimensions.length];
		int count = 0;
		for ( int i = 0; i < dimensions.length; i++ )
		{
			if ( position[i] == null )
				{
					dimResultSet[i] = new DimensionResultIterator( (Dimension) dimensions[i],
							dimensions[i].findAll( ));
				}
				else
				{
					dimResultSet[i] = new DimensionResultIterator( (Dimension) dimensions[i],
							position[i] );
				}
				count++;			
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
		
		List filterList = getDimensionJSFilterList( dimension.getName( ) );
		List dimFilterList = new ArrayList( );
		List topBottomfilterList = new ArrayList( );
		for ( int j = 0; j < filterList.size( ); j++ )
		{
			Object filterHelper = filterList.get( j );
			if ( filterHelper instanceof IJSDimensionFilterHelper )
			{
				dimFilterList.add( filterHelper );
			}
			else if ( filterHelper instanceof IJSTopBottomFilterHelper )
			{
				topBottomfilterList.add( filterHelper );
			}
		}
		
		IDiskArray result = getDimFilterPositions( dimension, dimPosition, dimFilterList );	
		if ( topBottomfilterList.isEmpty( ) )
		{
			return result;
		}
		else
		{// top/bottom dimension filters
			IDiskArray result2 = getTopbottomFilterPositions( dimension,
					dimPosition,
					topBottomfilterList );
			return interPosition( result, result2 );
		}
	}

	/**
	 * @param dimension
	 * @param dimPosition
	 * @param dimFilterList 
	 * @return
	 * @throws IOException
	 * @throws DataException
	 */
	private IDiskArray getDimFilterPositions( Dimension dimension,
			IDiskArray dimPosition, List dimFilterList ) throws IOException, DataException
	{
		IDiskArray result = new BufferedPrimitiveDiskArray( Constants.LIST_BUFFER_SIZE );
		for ( int i = 0; i < dimPosition.size( ); i++ )
		{
			Integer pos = (Integer) dimPosition.get( i );
			if ( isDimPositionSelected( dimension, pos.intValue( ), dimFilterList) )
				result.add( pos );
		}
		return result;
	}

	
	/**
	 * 
	 * @param dimension
	 * @param pos
	 * @param dimFilterList 
	 * @throws IOException
	 * @throws DataException
	 */
	private boolean isDimPositionSelected( Dimension dimension, int pos,
			List dimFilterList ) throws IOException, DataException
	{
		DimensionRow dimRow = dimension.getRowByPosition( pos );
		RowForFilter rowForFilter = getRowForFilter( dimension, dimRow );
		for ( int j = 0; j < dimFilterList.size( ); j++ )
		{
			IJSDimensionFilterHelper filterHelper = (IJSDimensionFilterHelper) dimFilterList.get( j );
			if ( !filterHelper.evaluateFilter( rowForFilter ) )
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
	 * generate a RowForFilter instance for IJsFilter to evaluate.
	 * @param dimension
	 * @param dimRow
	 * @return
	 */
	private RowForFilter getRowForFilter( Dimension dimension, DimensionRow dimRow )
	{
		RowForFilter rowForFilter = (RowForFilter) dimRowForFilterMap.get( dimension.getName( ) );
		if ( rowForFilter == null )
		{
			String[] fieldNames = getAllFieldNames( dimension );
			rowForFilter = new RowForFilter( fieldNames );
			dimRowForFilterMap.put( dimension.getName( ), rowForFilter );
		}
		// fill values for this row
		List fields = new ArrayList( );
		for ( int i = 0; i < dimRow.getMembers( ).length; i++ )
		{
			if ( dimRow.getMembers( )[i].getKeyValues( ) != null )
			{
				for ( int j = 0; j < dimRow.getMembers( )[i].getKeyValues( ).length; j++ )
				{
					fields.add( dimRow.getMembers( )[i].getKeyValues( )[j] );
				}
			}
			if ( dimRow.getMembers( )[i].getAttributes( ) != null )
			{
				for ( int j = 0; j < dimRow.getMembers( )[i].getAttributes( ).length; j++ )
				{
					fields.add( dimRow.getMembers( )[i].getAttributes( )[j] );
				}
			}
		}
		rowForFilter.setFieldValues( fields.toArray( ) );
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
		List fieldNameList = new ArrayList( );
		for ( int i = 0; i < levels.length; i++ )
		{
			String[] keyNames = levels[i].getKeyNames( );
			if ( keyNames != null )
			{
				for ( int j = 0; j < keyNames.length; j++ )
				{
					fieldNameList.add( CubeQueryExecutorHelper.getAttrReference( dimension.getName( ),
							levels[i].getName( ),
							keyNames[j] ) );
				}
			}
			
			String[] attrNames = levels[i].getAttributeNames( );
			if ( attrNames != null )
			{
				for ( int j = 0; j < attrNames.length; j++ )
				{
					fieldNameList.add( CubeQueryExecutorHelper.getAttrReference( dimension.getName( ),
							levels[i].getName( ), attrNames[j] ) );
				}
			}
		}
		String[] fieldNames = new String[fieldNameList.size( )];
		fieldNameList.toArray( fieldNames );
		return fieldNames;
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
	 * get the top/bottom filter selected dimension positions.
	 * @param dimension
	 * @param dimPosition
	 * @param topBottomfilterList 
	 * @return
	 * @throws IOException 
	 * @throws DataException 
	 */
	private IDiskArray getTopbottomFilterPositions( Dimension dimension,
			IDiskArray dimPosition, List filterList ) throws IOException,
			DataException
	{
		IDiskArray result = dimPosition;
		ILevel[] levels = dimension.getHierarchy( ).getLevels( );
		for ( int i = 0; i < filterList.size( ); i++ )
		{
			IJSTopBottomFilterHelper filter = (IJSTopBottomFilterHelper) filterList.get( i );
			List dimValueArrayList = evaluateFilter( dimension,
					dimPosition,
					levels,
					filter );
			IDiskArray dimPositionArray = fetchDimPositions( dimValueArrayList,
					filter );
			result = interPosition( result, dimPositionArray );
		}
		return result;
	}

	/**
	 * evaluate the filter to dimension positions in <code>dimPosition</code> and 
	 * store the evaluate result to <code>dimValueArrayList</code>, which contains
	 * one or multiple IDiskArray instances.
	 * @param dimension
	 * @param dimPosition
	 * @param levels
	 * @param filter
	 * @return
	 * @throws DataException
	 * @throws IOException
	 */
	private List evaluateFilter( Dimension dimension, IDiskArray dimPosition,
			ILevel[] levels, IJSTopBottomFilterHelper filter )
			throws DataException, IOException
	{
		List dimValueArrayList = new ArrayList( ); // 
		// get target level index, also equals to the length of parent levels
		int index = getTargetLevelIndex( levels, filter );
		
		Member[] preMembers = null;
		Object[] preValue = null;
		IDiskArray dimValueArray = null;
		int n = -1;
		if ( filter.isPercent( ) == false )
		{
			n = (int) filter.getN( );
		}
		// when using break hierarchy mode, it applies top/bottom filters to  the whole 
		// dimension values of specified level; otherwise, it should apply them to separate
		// parent levels 
		if ( isBreakHierarchy )
		{
			dimValueArray = new OrderedDiskArray( n, filter.isTop( ) );
			dimValueArrayList.add( dimValueArray );
		}
		IntRange range = null;
		// first-pass filter evaluation, store the qualified values
		// and position index to dimValueArrayList
		for ( int j = 0; j < dimPosition.size( ); j++ )
		{
			Integer pos = (Integer) dimPosition.get( j );
			DimensionRow dimRow = dimension.getRowByPosition( pos.intValue( ) );

			boolean shareParentLevels = shareParentLevels( dimRow.getMembers( ),
							preMembers,
							index );
			if ( isBreakHierarchy == false )
			{// maintain the dimension hierarchy
				if ( shareParentLevels )
				{
					dimValueArray = (IDiskArray) dimValueArrayList.get( dimValueArrayList.size( ) - 1 );
				}
				else
				{
					dimValueArray = new OrderedDiskArray( n, filter.isTop( ) );
					dimValueArrayList.add( dimValueArray );
				}
			}
			preMembers = dimRow.getMembers( );
			Object[] levelValue = dimRow.getMembers( )[index].getKeyValues( );
			if ( preValue == null
					|| shareParentLevels == false
					|| CompareUtil.compare( preValue, levelValue ) != 0 )
			{
				RowForFilter rowForFilter = getRowForFilter( dimension, dimRow );
				Object value = filter.evaluateFilterExpr( rowForFilter );
				range = new IntRange( pos.intValue( ), pos.intValue( ) );
				dimValueArray.add( new ValueObject( value, range ) );
			}
			else
			{
				range.end = pos.intValue( );
			}
			preValue = levelValue;
		}
		return dimValueArrayList;
	}

	/**
	 * get all selected dimension positions.
	 * @param dimValueArrayList
	 * @param filterHelper
	 * @param dimPositionArray
	 * @return 
	 * @throws IOException
	 */
	private IDiskArray fetchDimPositions( List dimValueArrayList,
			IJSTopBottomFilterHelper filterHelper ) throws IOException
	{
		IDiskArray dimPositionArray = new OrderedDiskArray( ); // final selection positions
		for ( Iterator itr = dimValueArrayList.iterator( ); itr.hasNext( ); )
		{
			IDiskArray dimValues = (IDiskArray) itr.next( );
			int size = dimValues.size( );
			int start = 0;
			int end = size;
			if ( filterHelper.isPercent( ) )
			{
				int n = getTargetN( size, filterHelper.getN( ) );
				if ( filterHelper.isTop( ) )
					start = size - n;
				else
					end = n;
			}
			for ( int j = start; j < end; j++ )
			{
				ValueObject aggrValue = (ValueObject) dimValues.get( j );
				IntRange range = (IntRange) aggrValue.index;
				for ( int k = range.start; k <= range.end; k++ )
				{
					dimPositionArray.add( new Integer( k ) );
				}
			}
		}
		return dimPositionArray;
	}

	/**
	 * @param levels
	 * @param filter
	 * @return
	 * @throws DataException
	 */
	private int getTargetLevelIndex( ILevel[] levels,
			IJSTopBottomFilterHelper filter ) throws DataException
	{
		int index = 0;
		DimLevel targetLevel = filter.getTargetLevel( );
		for ( index = 0; index < levels.length; index++ )
		{
			if ( levels[index].getName( )
					.equals( targetLevel.getLevelName( ) ) )
			{
				return index;
			}
		}
		return -1;
	}


	/**
	 * To check whether two dimension rows share the same parent levels
	 * regarding the specified target level.
	 * 
	 * @param members
	 * @param previous
	 * @param end -
	 *            the member index of the target level.
	 * @return
	 */
	private boolean shareParentLevels( Member[] members, Member[] previous,
			int end )
	{
		if ( previous == null )
			return false;
		for ( int i = 0; i < end; i++ )
		{
			if ( members[i].equals( previous[i] ) == false )
			{
				return false;
			}
		}
		return true;
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
			if ( filter.getDimensionName( ).equals( dimension.getName( ) ) == false )
			{
				continue;
			}
			int index = getIndex( levels, filter.getLevelName( ) );
			if ( index >= 0 )
			{
				if ( selections[index] == null )
				{
					selections[index] = filter.getSelections( );
					filterCount++;
				}
				else
				{
					selections[index] = SelectionUtil.intersect( selections[index],
							filter.getSelections( ) );
				}
			}
		}
		if(filterCount==0)
		{
			return null;
		}
		Level[] filterLevel = new Level[filterCount];
		ISelection[][] selects = new ISelection[filterCount][];
		int pos = 0;
		for( int i=0;i<selections.length;i++)
		{
			if ( selections[i] != null )
			{
				filterLevel[pos] = (Level)levels[i];
				selects[pos] = selections[i];
				pos++;
			}
		}
		return dimension.find( filterLevel, selects );
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
	public void addJSFilter( IJSFilterHelper filterEvalHelper )
	{
		if ( filterEvalHelper instanceof IJSDimensionFilterHelper )
		{
			if ( filterEvalHelper.isAggregationFilter( ) == false )
			{// Dimension filter
				String dimesionName = filterEvalHelper.getDimensionName( );
				List filterList = getDimensionJSFilterList( dimesionName );
				filterList.add( filterEvalHelper );
			}
			else
			{// Aggregation filter
				IJSDimensionFilterHelper helper = (IJSDimensionFilterHelper) filterEvalHelper;
				aggrFilters.add( new AggrFilter( helper ) );
			}
		}
		else if ( filterEvalHelper instanceof IJSTopBottomFilterHelper )
		{// top/bottom N/percent filter
			if ( filterEvalHelper.isAggregationFilter( ) == false )
			{// Dimension filter
				String dimesionName = filterEvalHelper.getDimensionName( );
				List filterList = getDimensionJSFilterList( dimesionName );
				filterList.add( filterEvalHelper );
			}
			else
			{// Aggregation filter
				IJSTopBottomFilterHelper helper = (IJSTopBottomFilterHelper) filterEvalHelper;
				topbottomFilters.add( new TopBottomFilter( helper ) );
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.data.api.ICubeQueryExcutorHelper#addJSFilter(java.util.List)
	 */
	public void addJSFilter( List filterEvalHelperList )
	{
		for ( int i = 0; i < filterEvalHelperList.size( ); i++ )
		{
			addJSFilter( (IJSFilterHelper) filterEvalHelperList.get( i ) );
		}
	}

	
	/**
	 * @param isBreakHierarchy the isBreakHierarchy to set
	 */
	public void setBreakHierarchy( boolean isBreakHierarchy )
	{
		this.isBreakHierarchy = isBreakHierarchy;
	}	
}

/**
 *
 */
class IntRange
{
	int start;
	int end;
	IntRange( int start, int end )
	{
		this.start = start;
		this.end = end;
	}
}

/**
 * 
 */
class ValueObject implements Comparable
{
	Object value;
	Object index;

	public ValueObject( Object value, Object index )
	{
		this.value = value;
		this.index = index;
	}

	public int compareTo( Object obj )
	{
		if ( obj instanceof ValueObject )
		{
			ValueObject objValue = (ValueObject) obj;
			return CompareUtil.compare( value, objValue.value );
		}
		return -1;
	}
}

/**
 * 
 */
class MultiKey implements Comparable
{
	Object[] levelKey;
	MultiKey( Object[] key )
	{
		this.levelKey = key;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(T)
	 */
	public int compareTo( Object obj )
	{
		if ( obj == null )
			return -1;
		else if ( obj instanceof MultiKey )
		{
			MultiKey key = (MultiKey) obj;
			return CompareUtil.compare( levelKey, key.levelKey );
		}
		return -1;
	}
}
/**
 * 
 *
 */
class AggrFilter
{
	private DimLevel[] aggrLevels;
	private IJSDimensionFilterHelper aggrFilterHelper;
	private DimLevel targetLevel;
	private DimLevel[] axisQualifierLevels;
	private Object[] axisQualifierValues;
	
	AggrFilter( IJSDimensionFilterHelper filterEvalHelper)
	{
		aggrFilterHelper = filterEvalHelper;
		ICubeFilterDefinition cubeFilter = filterEvalHelper.getCubeFilterDefinition( );
		targetLevel = new DimLevel(cubeFilter.getTargetLevel( ));
		aggrLevels = filterEvalHelper.getAggrLevels( );
		ILevelDefinition[] axisLevels = cubeFilter.getAxisQualifierLevels( );
		if ( axisLevels != null )
		{
			axisQualifierLevels = new DimLevel[axisLevels.length];
			for ( int i = 0; i < axisLevels.length; i++ )
			{
				axisQualifierLevels[i] = new DimLevel(axisLevels[i]);
			}
		}
		axisQualifierValues = cubeFilter.getAxisQualifierValues( );
	}

	
	/**
	 * @return the axisQualifierLevelNames
	 */
	DimLevel[] getAxisQualifierLevels( )
	{
		return axisQualifierLevels;
	}

	
	/**
	 * @return the axisQualifierLevelValues
	 */
	Object[] getAxisQualifierValues( )
	{
		return axisQualifierValues;
	}

	/**
	 * @return the aggrLevels
	 */
	DimLevel[] getAggrLevels( )
	{
		return aggrLevels;
	}
	
	/**
	 * @return the aggrFilter
	 */
	IJSDimensionFilterHelper getFilterHelper( )
	{
		return aggrFilterHelper;
	}
	
	/**
	 * @return the targetLevel
	 */
	DimLevel getTargetLevel( )
	{
		return targetLevel;
	}
}

/**
 * 
 */
class RowForFilter implements IResultRow
{

	private HashMap fieldMap = new HashMap( );
	private HashMap aggrMap = new HashMap( );
	private Object[] fieldValues;
	private Object[] aggrValues;

	/**
	 * 
	 * @param fieldNames
	 */
	RowForFilter( String[] fieldNames )
	{
		for ( int i = 0; i < fieldNames.length; i++ )
		{
			fieldMap.put( fieldNames[i].toString( ), new Integer( i ) );
		}
	}

	/**
	 * 
	 * @param fieldNames
	 * @param aggrNames
	 */
	RowForFilter( String[] fieldNames, String[] aggrNames )
	{
		this( fieldNames );
		for ( int i = 0; i < aggrNames.length; i++ )
		{
			aggrMap.put( aggrNames[i], new Integer( i ) );
		}
	}

	/**
	 * 
	 * @param levelValues
	 */
	void setFieldValues( Object[] levelValues )
	{
		this.fieldValues = levelValues;
	}

	/**
	 * @param dataValues
	 */
	void setAggrValues( Object[] dataValues )
	{
		this.aggrValues = dataValues;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.util.filter.IResultRow#getDataValue(java.lang.String)
	 */
	public Object getAggrValue( String aggrName ) throws DataException
	{
		Object index = aggrMap.get( aggrName );
		if ( index == null )
		{
			return null;
		}
		return aggrValues[( (Integer) index ).intValue( )];
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.util.filter.IResultRow#getFieldValue(java.lang.String)
	 */
	public Object getFieldValue( String field ) throws DataException
	{
		Object index = fieldMap.get( field );
		if ( index == null )
		{
			return null;
		}
		return fieldValues[( (Integer) index ).intValue( )];
	}
	
}