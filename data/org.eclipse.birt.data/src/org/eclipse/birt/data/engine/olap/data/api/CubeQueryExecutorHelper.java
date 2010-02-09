
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.eclipse.birt.core.archive.FileArchiveReader;
import org.eclipse.birt.core.archive.FileArchiveWriter;
import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.StopSign;
import org.eclipse.birt.data.engine.impl.document.stream.VersionManager;
import org.eclipse.birt.data.engine.olap.data.api.cube.ICube;
import org.eclipse.birt.data.engine.olap.data.api.cube.IDimension;
import org.eclipse.birt.data.engine.olap.data.document.IDocumentManager;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationResultSetSaveUtil;
import org.eclipse.birt.data.engine.olap.data.impl.Cube;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.AggregationExecutor;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.CubeDimensionReader;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.DataSetFromOriginalCube;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.IDataSet4Aggregation;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.filter.AggrMeasureFilterHelper;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.filter.AggregationFilterHelper;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.filter.LevelFilter;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.filter.LevelFilterHelper;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.filter.SimpleLevelFilter;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.sort.AggrSortHelper;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.sort.ITargetSort;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Dimension;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.DimensionResultIterator;
import org.eclipse.birt.data.engine.olap.data.impl.facttable.FactTableRowIterator;
import org.eclipse.birt.data.engine.olap.data.util.IDiskArray;
import org.eclipse.birt.data.engine.olap.util.filter.IAggrMeasureFilterEvalHelper;
import org.eclipse.birt.data.engine.olap.util.filter.ICubePosFilter;
import org.eclipse.birt.data.engine.olap.util.filter.IJSFacttableFilterEvalHelper;
import org.eclipse.birt.data.engine.olap.util.filter.IJSFilterHelper;
import org.eclipse.birt.data.engine.olap.util.sort.IJSSortHelper;

/**
 * 
 */

public class CubeQueryExecutorHelper implements ICubeQueryExcutorHelper
{
	private Cube cube;
	private List levelFilters = null;
	private List simpleLevelFilters = null;	// list for SimepleLevelFilter
	private List measureFilters = null;
	private Map dimJSFilterMap = null;
	private Map dimRowForFilterMap = null;
	
	private List rowSort = null;
	private List columnSort = null;
	private List pageSort = null;
	
	private boolean isBreakHierarchy = true;
	
	private IComputedMeasureHelper computedMeasureHelper = null;
	
	private List aggrFilterHelpers;
	private List aggrMeasureFilters;
	
	private List cubePosFilters;
	private static Logger logger = Logger.getLogger( CubeQueryExecutorHelper.class.getName( ) );
	
	/**
	 * 
	 * @param cube
	 */
	public CubeQueryExecutorHelper( ICube cube ) throws DataException
	{
		this(cube, null);
	}
	
	/**
	 * 
	 * @param cube
	 */
	public CubeQueryExecutorHelper( ICube cube, IComputedMeasureHelper computedMeasureHelper ) throws DataException
	{
		Object[] params = {cube, computedMeasureHelper};
		logger.entering( CubeQueryExecutorHelper.class.getName( ),
				"CubeQueryExecutorHelper",//$NON-NLS-1$
				params );
		this.cube = (Cube) cube;
		this.computedMeasureHelper = computedMeasureHelper;
		if (this.computedMeasureHelper != null) 
		{
			validateComputedMeasureNames();
		}
		this.simpleLevelFilters = new ArrayList( );
		this.levelFilters = new ArrayList( );
		this.measureFilters = new ArrayList( );
		this.aggrFilterHelpers = new ArrayList( );
		this.aggrMeasureFilters = new ArrayList( );
		this.dimJSFilterMap = new HashMap( );
		this.dimRowForFilterMap = new HashMap( );
		
		this.rowSort = new ArrayList( );
		this.columnSort = new ArrayList( );
		this.pageSort = new ArrayList( );
		
		logger.exiting( CubeQueryExecutorHelper.class.getName( ),
				"CubeQueryExecutorHelper" );//$NON-NLS-1$
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
	 * 
	 * @param cube
	 * @throws BirtException 
	 * @throws IOException 
	 */
	public static ICube loadCube( String cubeName,
			IDocumentManager documentManager, StopSign stopSign ) throws IOException, DataException
	{
		if ( documentManager == null )
		{
			throw new DataException( ResourceConstants.FAIL_LOAD_CUBE, cubeName );
		}
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
		return pathName + File.separator + "cubequeryresult" +name;//$NON-NLS-1$
	}
	
	
	/**
	 * 
	 * @param sort
	 */
	public void addRowSort( ITargetSort sort )
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
	public void addColumnSort( ITargetSort sort )
	{
		this.columnSort.add( sort );
	}
	
	/**
	 * 
	 * @return
	 */
	public List getPageSort( )
	{
		return this.pageSort;
	}
	
	/**
	 * 
	 * @param sort
	 */
	public void addPageSort( ITargetSort sort )
	{
		this.pageSort.add( sort );
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.ICubeQueryExcutorHelper#addFilter(java.lang.String, org.eclipse.birt.data.olap.data.api.ISelection[])
	 */
	public void addFilter( LevelFilter levelFilter )
	{		
		levelFilters.add( levelFilter );
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.data.api.ICubeQueryExcutorHelper#addSimpleLevelFilter(org.eclipse.birt.data.engine.olap.data.impl.aggregation.filter.SimpleLevelFilter)
	 */
	public void addSimpleLevelFilter( SimpleLevelFilter simpleLevelFilter )
	{		
		simpleLevelFilters.add( simpleLevelFilter );
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.ICubeQueryExcutorHelper#clear()
	 */
	public void clear( )
	{
		levelFilters.clear( );
		aggrFilterHelpers.clear( );
		aggrMeasureFilters.clear( );
		dimJSFilterMap.clear( );
		dimRowForFilterMap.clear( );
		rowSort.clear( );
		columnSort.clear( );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.ICubeQueryExcutorHelper#close()
	 */
	public void close( )
	{
		levelFilters = null;
		aggrFilterHelpers = null;
		aggrMeasureFilters = null;
		dimJSFilterMap = null;
		dimRowForFilterMap = null;
		rowSort = null;
		columnSort = null;
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
	 * @param resultSet
	 * @throws DataException
	 */
	public void applyAggrSort( IAggregationResultSet[] resultSet )
			throws DataException
	{
		if ( !this.columnSort.isEmpty( ) )
		{
			AggrSortHelper.sort( this.columnSort, resultSet );
			closeSortHelpers( columnSort );
		}
		if ( !this.rowSort.isEmpty( ) )
		{
			AggrSortHelper.sort( this.rowSort, resultSet );
			closeSortHelpers( rowSort );
		}
	}

	/**
	 * 
	 * @param sorts
	 */
	private void closeSortHelpers( List sorts )
	{
		for ( Iterator i = sorts.iterator( ); i.hasNext( ); )
		{
			ITargetSort targetSot = (ITargetSort) i.next( );
			if ( targetSot instanceof IJSSortHelper )
			{
				IJSSortHelper sortHelper = (IJSSortHelper) targetSot;
				sortHelper.close( );
			}
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
		boolean recalculate = false;
		
		if ( !aggrMeasureFilters.isEmpty( ) )
		{
			AggrMeasureFilterHelper filter = new AggrMeasureFilterHelper( cube,
					resultSet );
			cubePosFilters = filter.getCubePosFilters( aggrMeasureFilters );
			if( cubePosFilters == null )
			{
				for ( int i = 0; i < resultSet.length; i++ )
				{// clear all aggregation result sets to be empty
					resultSet[i].clear( );
				}
				return;
			}
			recalculate = true;
		}
		
		if ( !aggrFilterHelpers.isEmpty( ) )
		{
			AggregationFilterHelper filterHelper = new AggregationFilterHelper( cube,
					aggrFilterHelpers );
			// add new filters for another aggregation computation
			List newFilters = filterHelper.generateLevelFilters( aggregations,
					resultSet );
			if ( newFilters == null )
			{// the final x-tab is empty
				for ( int i = 0; i < resultSet.length; i++ )
				{// clear all aggregation result sets to be empty
					resultSet[i].clear( );
					recalculate = false;
				}
			}
			else
			{
				levelFilters.addAll( newFilters );
				for ( int i = 0; i < resultSet.length; i++ )
				{// release all previous aggregation result sets
					resultSet[i].close( );
					resultSet[i] = null;
				}
				recalculate = true;
			}
		}
		
		
		
		if ( recalculate )
		{
			// recalculate the aggregation according to new filters
			IAggregationResultSet[] temp = onePassExecute( aggregations,
					stopSign );
			// overwrite result with the second pass aggregation result set
			System.arraycopy( temp, 0, resultSet, 0, resultSet.length );
		}
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
		FactTableRowIterator factTableRowIterator = new FactTableRowIterator( cube.getFactTable( ),
				validDimensionName,
				validDimPosition,
				cube.getDimesions( ),
				null,
				stopSign );
		if ( cubePosFilters != null && !cubePosFilters.isEmpty( ) )
		{// add fact table filter if it's necessary
			for ( Iterator itr = cubePosFilters.iterator( ); itr.hasNext( ); )
			{
				ICubePosFilter cubePosFilter = (ICubePosFilter) itr.next( );
				factTableRowIterator.addCubePosFilter( cubePosFilter );
			}
		}
		
		for( int i=0;i<measureFilters.size( );i++)
		{
			factTableRowIterator.addMeasureFilter( (IJSFacttableFilterEvalHelper)measureFilters.get( i ) );
		}
		DimensionResultIterator[] dimensionResultIterators = populateDimensionResultIterator( dimPosition, stopSign );

		IDataSet4Aggregation dataSet4Aggregation = new DataSetFromOriginalCube( factTableRowIterator,
				dimensionResultIterators,
				computedMeasureHelper );
		AggregationExecutor aggregationCalculatorExecutor = new AggregationExecutor( new CubeDimensionReader( cube ),
				dataSet4Aggregation,
				aggregations );
		return aggregationCalculatorExecutor.execute( stopSign );
	}
	
	
	/**
	 * 
	 * @param resultLevels
	 * @param position
	 * @param stopSign
	 * @return
	 * @throws DataException
	 * @throws IOException
	 */
	private DimensionResultIterator[] populateDimensionResultIterator( IDiskArray[] position, StopSign stopSign ) throws DataException, IOException
	{
		IDimension[] dimensions = cube.getDimesions( );
		DimensionResultIterator[] dimResultSet = new DimensionResultIterator[dimensions.length];

		for ( int i = 0; i < dimensions.length; i++ )
		{
			dimResultSet[i] = new DimensionResultIterator( (Dimension) dimensions[i],
						position[i], stopSign);
		}
		
		return dimResultSet;
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
	 * @return
	 * @throws DataException
	 * @throws IOException
	 */
	private IDiskArray[] getFilterResult( ) throws DataException, IOException
	{
		try
		{
		IDimension[] dimensions = cube.getDimesions( );
		IDiskArray[] dimPosition = new IDiskArray[dimensions.length];
		for ( int i = 0; i < dimPosition.length; i++ )
		{
			Dimension dimension = (Dimension) dimensions[i];
			List jsFilters = getDimensionJSFilterList( dimension.getName( ) );
			LevelFilterHelper filterHelper = new LevelFilterHelper( dimension,
					simpleLevelFilters,
					levelFilters );
			dimPosition[i] = filterHelper.getJSFilterResult( jsFilters, isBreakHierarchy );
		}
		return dimPosition;
		}
		catch( IOException ie )
		{
			ie.printStackTrace( );
			throw ie;
		}
		catch( DataException de )
		{
			de.printStackTrace( );
			throw de;
		}
		catch( Throwable te )
		{
			te.printStackTrace( );
			return null;
		}
	}
	
	/**
	 * 
	 * @param aggrFilterHelper
	 */
	public void addAggrMeasureFilter(List<IAggrMeasureFilterEvalHelper> aggrFilterHelper)
	{
		this.aggrMeasureFilters.addAll( aggrFilterHelper );
	}

	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.data.api.ICubeQueryExcutorHelper#addJSFilter(org.eclipse.birt.data.engine.olap.util.filter.DimensionFilterEvalHelper)
	 */
	public void addJSFilter( IJSFilterHelper filterEvalHelper )
	{
		if ( filterEvalHelper.isAggregationFilter( ) == false )
		{// Dimension filter
			String dimesionName = filterEvalHelper.getDimensionName( );
			List filterList = getDimensionJSFilterList( dimesionName );
			filterList.add( filterEvalHelper );
		}
		else
		{// Aggregation filter
			aggrFilterHelpers.add( filterEvalHelper );
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

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.data.api.ICubeQueryExcutorHelper#addMeasureFilter(org.eclipse.birt.data.engine.olap.util.filter.IJSMeasureFilterEvalHelper)
	 */
	public void addMeasureFilter( List<IJSFacttableFilterEvalHelper> measureFilter )
	{
		measureFilters.addAll( measureFilter );
	}
	
	/**
	 * @param isBreakHierarchy the isBreakHierarchy to set
	 */
	public void setBreakHierarchy( boolean isBreakHierarchy )
	{
		this.isBreakHierarchy = isBreakHierarchy;
	}
	
	/**
	 * 
	 * @throws DataException
	 */
	private void validateComputedMeasureNames() throws DataException
	{
		Set existNames = new HashSet(Arrays.asList( cube.getMeasureNames( ) ));
		MeasureInfo[] mis = computedMeasureHelper.getAllComputedMeasureInfos( );
		for (int i=0; i<mis.length; i++) 
		{
			String name = mis[i].getMeasureName( );
			if (existNames.contains( name ))
			{
				throw new DataException(ResourceConstants.DUPLICATE_MEASURE_NAME, name);
			}
			existNames.add( name );
		}
		
	}
}