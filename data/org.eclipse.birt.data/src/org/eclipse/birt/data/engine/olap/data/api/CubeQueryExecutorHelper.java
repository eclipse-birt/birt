
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
import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IShutdownListener;
import org.eclipse.birt.data.engine.cache.Constants;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.cache.CacheUtil;
import org.eclipse.birt.data.engine.i18n.DataResourceHandle;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.DataEngineSession;
import org.eclipse.birt.data.engine.impl.StopSign;
import org.eclipse.birt.data.engine.impl.document.stream.VersionManager;
import org.eclipse.birt.data.engine.olap.api.query.CubeFilterDefinition;
import org.eclipse.birt.data.engine.olap.data.api.cube.ICube;
import org.eclipse.birt.data.engine.olap.data.api.cube.IDimension;
import org.eclipse.birt.data.engine.olap.data.document.IDocumentManager;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationFunctionDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationResultSetSaveUtil;
import org.eclipse.birt.data.engine.olap.data.impl.Cube;
import org.eclipse.birt.data.engine.olap.data.impl.SecuredCube;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.AggregationExecutor;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.AggregationResultRow;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.AggregationResultRowComparator;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.AggregationResultSet;
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
import org.eclipse.birt.data.engine.olap.data.impl.dimension.DimensionRow;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Level;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Member;
import org.eclipse.birt.data.engine.olap.data.impl.facttable.FactTableRowIterator;
import org.eclipse.birt.data.engine.olap.data.util.BufferedStructureArray;
import org.eclipse.birt.data.engine.olap.data.util.DiskSortedStack;
import org.eclipse.birt.data.engine.olap.data.util.IDiskArray;
import org.eclipse.birt.data.engine.olap.impl.query.CubeQueryExecutor;
import org.eclipse.birt.data.engine.olap.util.OlapExpressionCompiler;
import org.eclipse.birt.data.engine.olap.util.filter.BaseDimensionFilterEvalHelper;
import org.eclipse.birt.data.engine.olap.util.filter.IAggrMeasureFilterEvalHelper;
import org.eclipse.birt.data.engine.olap.util.filter.ICubePosFilter;
import org.eclipse.birt.data.engine.olap.util.filter.IJSFacttableFilterEvalHelper;
import org.eclipse.birt.data.engine.olap.util.filter.IJSFilterHelper;
import org.eclipse.birt.data.engine.olap.util.filter.JSFacttableFilterEvalHelper;
import org.eclipse.birt.data.engine.olap.util.sort.IJSSortHelper;

/**
 * 
 */

public class CubeQueryExecutorHelper implements ICubeQueryExcutorHelper
{
	protected Cube cube;
	protected List levelFilters = null;
	protected List simpleLevelFilters = null;	// list for SimepleLevelFilter
	protected List measureFilters = null;
	private Map dimJSFilterMap = null;
	
	private List rowSort = null;
	private List columnSort = null;
	private List pageSort = null;
	
	protected boolean isBreakHierarchy = true;
	
	protected IComputedMeasureHelper computedMeasureHelper = null;
	
	private List aggrFilterHelpers;
	private List aggrMeasureFilters;
	
	protected List cubePosFilters;
	private static Logger logger = Logger.getLogger( CubeQueryExecutorHelper.class.getName( ) );
	
	public int maxDataObjectRows = -1;
	public long memoryCacheSize = 0;
	
	private IBindingValueFetcher fetcher;
	private CubeQueryExecutor cubeQueryExecutor;
	
	private Map appContext;
	/**
	 * 
	 * @param cube
	 */
	public CubeQueryExecutorHelper( ICube cube ) throws DataException
	{
		this(cube, null, null);
	}
	
	/**
	 * 
	 * @param cube
	 */
	public CubeQueryExecutorHelper( ICube cube, IComputedMeasureHelper computedMeasureHelper, IBindingValueFetcher fetcher ) throws DataException
	{
		Object[] params = {cube, computedMeasureHelper};
		logger.entering( CubeQueryExecutorHelper.class.getName( ),
				"CubeQueryExecutorHelper",//$NON-NLS-1$
				params );
		this.cube = (Cube) cube;
		this.fetcher = fetcher;
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
			IDocumentManager documentManager, DataEngineSession session ) throws IOException, DataException
	{
		if ( documentManager == null )
		{
			throw new DataException( ResourceConstants.FAIL_LOAD_CUBE, cubeName );
		}
		final Cube cube = new Cube( cubeName, documentManager );
		cube.load( session.getStopSign( ) );
		session.getEngine( ).addShutdownListener( new IShutdownListener(){

			public void dataEngineShutdown( )
			{
				try
				{
					cube.close( );
				}
				catch ( Exception e )
				{
				}

			}} );
		return cube;
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
		final Cube cube = new Cube( cubeName, documentManager );
		cube.load( stopSign );
		return cube;
	}
	/**
	 * 
	 * @param cube
	 * @throws BirtException 
	 * @throws IOException 
	 */
	public static ICube loadCube( String cubeName,
			IDocumentManager documentManager, StopSign stopSign, Map<String, Set<String>> notAccessibleDimLvls ) throws IOException, DataException
	{
		if ( documentManager == null )
		{
			throw new DataException( ResourceConstants.FAIL_LOAD_CUBE, cubeName );
		}
		SecuredCube cube = new SecuredCube( cubeName, documentManager, notAccessibleDimLvls );
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
		return AggregationResultSetSaveUtil.load( name, reader, VersionManager.getLatestVersion( ), 5*1024*1024  );
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
				VersionManager.getLatestVersion( ),
				5*1024*1024 );
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
	
	public void addSimpleLevelFilter( List<SimpleLevelFilter> simpleLevelFilter )
	{		
		simpleLevelFilters.addAll( simpleLevelFilter );
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
		if( !isDimensionTableQuery( aggregations ) )
		{
			return executeFactTableQuery( aggregations, stopSign );
		}
		else
		{
			IAggregationResultSet[] resultSet = new IAggregationResultSet[1];
			resultSet[0] = executeDimensionTableQuery( aggregations[0], stopSign );
			return resultSet;
		}
	}
	
	/**
	 * 
	 * @param aggregations
	 * @return
	 */
	private boolean isDimensionTableQuery( AggregationDefinition[] aggregations )
	{
		if( aggregations.length > 1 )
			return false;
		if( aggregations[0].getAggregationFunctions( ) != null )
			return false;
		DimLevel[] levels = aggregations[0].getLevels( );
		if( levels == null || levels.length == 0 )
			return false;
		for( int i = 0; i < levels.length; i++ )
		{
			for( int j = 0; j < levels.length; j++ )
			{
				if( !levels[i].getDimensionName( ).equals( levels[j].getDimensionName( ) ) )
				{
					return false;
				}
			}
		}
		
		if( measureFilters.size( ) > 0 )
			return false;
		
		if( aggrFilterHelpers.size( ) > 0 )
			return false;
		
		if( dimJSFilterMap.size( ) > 1 ||
				( dimJSFilterMap.size( ) == 1 && dimJSFilterMap.get( levels[0].getDimensionName()) == null) )
			return false;
		
		for( int i = 0; i < simpleLevelFilters.size( ); i++ )
		{
			if( ! levels[0].getDimensionName( ).equals( 
					( ( SimpleLevelFilter ) simpleLevelFilters.get( i ) ).getDimensionName( ) ) )
				return false;
		}
				
		return true;
	}
	
	/**
	 * 
	 * @param aggregations
	 * @param stopSign
	 * @return
	 * @throws IOException
	 * @throws BirtException
	 */
	private IAggregationResultSet[] executeFactTableQuery(
			AggregationDefinition[] aggregations, StopSign stopSign )
			throws IOException, BirtException
	{
		IAggregationResultSet[] resultSet = onePassExecute( aggregations,
				stopSign );
		
		resultSet = processDimensionFiltersInAggrBindingFilter( resultSet );
		
		applyAggrFilters( aggregations, resultSet, stopSign );
		
		applyAggrSort( resultSet );
		
		return resultSet;
	}
	
	
	public void setCubeQueryExecutor(CubeQueryExecutor executor)
	{
		this.cubeQueryExecutor = executor;
	}
	
	private IAggregationResultSet[] processDimensionFiltersInAggrBindingFilter(IAggregationResultSet[] rs) throws IOException, BirtException
	{
		for( int i = 0; i< rs.length ; i++ )
		{
			AggregationFunctionDefinition[] functions  = rs[i].getAggregationDefinition().getAggregationFunctions();
			
			if(functions != null)
			{
				IAggregationResultSet[] subAggrRs = new IAggregationResultSet[functions.length];
				boolean applyMerge = false;
				for (int j = 0; j < functions.length; j++) 
				{
					if (functions[j].getFilterEvalHelper() != null && functions[j].getFilterEvalHelper() instanceof JSFacttableFilterEvalHelper)
					{
						
						IBaseExpression expr = ((JSFacttableFilterEvalHelper) functions[j]
								.getFilterEvalHelper()).getFilterExpression();
						Set dimLevelSet = OlapExpressionCompiler
								.getReferencedDimLevel(expr, cubeQueryExecutor
										.getCubeQueryDefinition().getBindings());
						if (dimLevelSet.size() > 0) 
						{
							if ( OlapExpressionCompiler.getReferencedScriptObject( expr,
									ExpressionUtil.MEASURE_INDICATOR ) != null
									|| containsMultiDimension( dimLevelSet ) )
							{
								applyMerge = false;
								break;
							}
							applyMerge = true;
							CubeQueryExecutorHelper executorHelper = createCubeQueryExecutorHelper(
									this.cube, cubeQueryExecutor.getComputedMeasureHelper(),fetcher);
							
							executorHelper.setBreakHierarchy(cubeQueryExecutor.getCubeQueryDefinition().getFilterOption() == 0);
							CubeFilterDefinition cubeFilter = new CubeFilterDefinition(
									expr);
							executorHelper.addJSFilter(BaseDimensionFilterEvalHelper.createFilterHelper(
									cubeQueryExecutor.getOuterResults(),
									cubeQueryExecutor.getScope(), cubeQueryExecutor
													.getCubeQueryDefinition(),
											cubeFilter, cubeQueryExecutor.getContext()
													.getScriptContext()));

							AggregationDefinition[] aggregations = new AggregationDefinition[1];
							AggregationFunctionDefinition[] func = new AggregationFunctionDefinition[1];
							func[0] = new AggregationFunctionDefinition("temp_"
									+ functions[j].getName(),
									functions[j].getMeasureName(),
									functions[j].getFunctionName());
							if ( functions[j].getTimeFunction( ) != null )
							{
								func[0].setTimeFunction( functions[j].getTimeFunction( ) );
							}
							if ( functions[j].getTimeFunctionFilter( ) != null )
							{
								func[0].setTimeFunctionFilter( functions[j].getTimeFunctionFilter( ) );
							}
							aggregations[0] = new AggregationDefinition(rs[i]
									.getAggregationDefinition().getLevels(),
									rs[i].getAggregationDefinition()
											.getSortTypes(), func);

							IAggregationResultSet[] result = executorHelper
									.execute(aggregations, cubeQueryExecutor
											.getSession().getStopSign());
							subAggrRs[j] = result[0];
						}
					}
				}
				
				if(applyMerge)
				{
					mergeAggrResultsSetsResult(rs[i],subAggrRs);
				}
			}
		}
		return rs;
	}
	
	private boolean containsMultiDimension( Set dimLevelSet )
	{
		HashSet dimSet = new HashSet();
	    for ( Object dimLevel : dimLevelSet )
	    {
	    	if ( dimLevel instanceof DimLevel )
	    	{
	    		dimSet.add( dimLevel );
	    	}
	    }
		return dimSet.size( ) > 1;
	}
	
	
	private int[] getKeyLevelIndexs( DimLevel[] keyLevels, IAggregationResultSet rs ) throws DataException
	{
		if ( ( keyLevels == null ) || ( keyLevels.length == 0 ) )
		{
			return new int[0];
		}
		int[] keyLevelIndexes = new int[keyLevels.length];
		DimLevel[] allLevels = rs.getAllLevels( );
		for ( int i = 0; i < keyLevels.length; i++ )
		{
			keyLevelIndexes[i] = -1;
			for ( int j = 0; j < allLevels.length; j++ )
			{
				if ( keyLevels[i].equals( allLevels[j] ) )
					keyLevelIndexes[i] = j;
			}
			if( keyLevelIndexes[i] == -1 )
			{
				throw new DataException( DataResourceHandle.getInstance( )
						.getMessage( ResourceConstants.NONEXISTENT_LEVEL )
						+ keyLevels[i].getLevelName( ) );
			}
		}
		return keyLevelIndexes;
	}
	
	
	private void mergeAggrResultsSetsResult(IAggregationResultSet source, IAggregationResultSet[] result) throws IOException, DataException
	{
		IDiskArray sourceRows = ((AggregationResultSet)source).getAggregationResultRows();
		AggregationResultRowComparator comparator = new AggregationResultRowComparator(getKeyLevelIndexs(source.getAggregationDefinition().getLevels(),source),source.getSortType());
		for( int i = 0 ; i< source.length(); i++)
		{
			IAggregationResultRow row = (IAggregationResultRow)sourceRows.get(i);
			
			
			for( int j = 0; j< result.length; j++)
			{
				IAggregationResultSet rs = result[j];
				if(rs == null)
					continue;
				IDiskArray subRows =  ((AggregationResultSet)rs).getAggregationResultRows();
				boolean find = false;
				for( int k = 0 ; k<subRows.size();k++)
				{
					IAggregationResultRow subRow = (IAggregationResultRow)subRows.get(k);
					if(comparator.compare(row, subRow) == 0)
					{
						find = true;
						row.getAggregationValues()[j] = subRow.getAggregationValues()[0];
						break;
					}
				}
				if(!find)
					row.getAggregationValues()[j] = null;
			}
			
		}
	}
	
	/**
	 * 
	 * @param aggregations
	 * @param stopSign
	 * @return
	 * @throws IOException
	 * @throws BirtException
	 */
	private IAggregationResultSet executeDimensionTableQuery(
			AggregationDefinition aggregations, StopSign stopSign )
			throws IOException, BirtException
	{
		
		DimLevel[] levels = aggregations.getLevels( );
		String dimensionName = levels[0].getDimensionName( );
		
		IDimension[] dimensions = cube.getDimesions( );
		
		Dimension sourceDimension = null;
		for ( int i = 0; i < dimensions.length; i++ )
		{
			if( dimensionName.equals( dimensions[i].getName( ) ) )
			{
				sourceDimension = (Dimension)dimensions[i];
				break;
			}
		}
		IDiskArray dimensionrow = 
			getFiltedDistinctDiemensionRow( sourceDimension, levels, aggregations.getSortTypes( ), stopSign );
		
		IAggregationResultSet resultSet = 
			new AggregationResultSet( aggregations, dimensionrow, getKeyColumnName( aggregations ) 
					,getAttributeColumnName( aggregations, sourceDimension ) );
		
		return resultSet;
	}
	
	private static String[][] getAttributeColumnName( AggregationDefinition aggregation, Dimension dimension )
	{
		DimLevel[] levels = aggregation.getLevels( );
		String[][] result = new String[levels.length][1];
		for( int i = 0; i < levels.length; i++ )
		{
			result[i][0] = levels[i].getLevelName( );
			ILevel[] dimLevels = dimension.getHierarchy( ).getLevels( );
			for( int j = 0; j < dimLevels.length; j++ )
			{
				if( dimLevels[j].getName( ).equals( levels[i].getLevelName( ) ) )
				{
					result[i] = dimLevels[j].getAttributeNames( );
					break;
				}
			}
		}
		return result;
	}
	
	private static String[][] getKeyColumnName( AggregationDefinition aggregation )
	{
		DimLevel[] levels = aggregation.getLevels( );
		String[][] result = new String[levels.length][1];
		for( int i = 0; i < levels.length; i++ )
		{
			result[i][0] = levels[i].getLevelName( );
		}
		return result;
	}

	private IDiskArray getFiltedDistinctDiemensionRow( 
			Dimension dimension, DimLevel[] levels, int[] sortType, StopSign stopSign ) throws DataException, IOException
	{
		List jsFilters = getDimensionJSFilterList( dimension.getName( ) );
		LevelFilterHelper filterHelper = new LevelFilterHelper( dimension,
				simpleLevelFilters,
				levelFilters );
		IDiskArray filtedPosition = filterHelper.getJSFilterResult( jsFilters, isBreakHierarchy );
		IDiskArray filtedRow = null;
		int levelIndex[] = new int[levels.length];
		for( int i = 0; i < levels.length; i++ )
		{
			levelIndex[i] = getLevelIndex( dimension, levels[i] );
		}
		
		if( filtedPosition != null )
		{
			filtedRow = dimension.getDimensionRowByPositions( filtedPosition, stopSign );
		}
		else
		{
			int levelSize = dimension.getHierarchy( ).getLevels( ).length;
			if( levels.length == 1 && levelIndex[0] < ( levelSize - 1 ) && ( levelSize == 2 
					|| ( levelIndex[0] < ( levelSize - 2 ) && levelSize > 2 ) ) )
			{
				Level targetLevel = (Level) dimension.getHierarchy( ).getLevels( )[levelIndex[0]];
				filtedRow = dimension.getDimensionRowByPositions( targetLevel.getAllPosition( ), stopSign );
			}
			else
			{
				filtedRow = dimension.getAllRows( stopSign );
			}
		}
		
		IDiskArray result = new BufferedStructureArray( AggregationResultRow.getCreator( ), Constants.LIST_BUFFER_SIZE );
		DimensionRow dimensionRow = null;
		Member[] members = null;
		
		boolean isAscending = true;
		if( sortType[0] == IDimensionSortDefn.SORT_DESC )
			isAscending = false;
		DiskSortedStack sortedRow = new DiskSortedStack( filtedRow.size(), isAscending, true, AggregationResultRow.getCreator( ) );
		for( int i = 0; i < filtedRow.size( ); i++ )
		{
			dimensionRow = ( DimensionRow )filtedRow.get( i );
			members = new Member[levels.length];
			
			for( int j = 0; j < members.length; j++ )
			{
				members[j] = dimensionRow.getMembers( )[levelIndex[j]];
			}
			sortedRow.push( new AggregationResultRow( members, null ) );
		}
		
		Object obj = sortedRow.pop( );
		while( obj != null )
		{
			result.add( ( AggregationResultRow )obj );
			obj = sortedRow.pop( );
		}
		
		return result;
	}
	
	private static int getLevelIndex( Dimension dimension, DimLevel level )
	{
		ILevel[] levels = dimension.getHierarchy( ).getLevels( );
		if ( levels == null )
		{
			return -1;
		}
		for ( int i = 0; i < levels.length; i++ )
		{
			if ( levels[i].getName( ).equals( level.getLevelName( ) ) )
			{
				return i;
			}
		}
		return -1;
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
			AggrSortHelper.sort( this.columnSort, resultSet, fetcher );
			closeSortHelpers( columnSort );
		}
		if ( !this.rowSort.isEmpty( ) )
		{
			AggrSortHelper.sort( this.rowSort, resultSet, fetcher );
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
	
	protected boolean populateAggrMeasureFilterResult( ICube cube,
			IAggregationResultSet[] resultSet, List aggrMeasureFilters,
			CubeQueryExecutor cubeQueryExecutor, IBindingValueFetcher fetcher )
			throws DataException, IOException
	{
		AggrMeasureFilterHelper filter = new AggrMeasureFilterHelper( cube,
				resultSet );
		filter.setQueryExecutor( cubeQueryExecutor );
		filter.setBindingValueFetcher( fetcher );

		cubePosFilters = filter.getCubePosFilters( aggrMeasureFilters );
		if ( cubePosFilters == null )
		{
			for ( int i = 0; i < resultSet.length; i++ )
			{// clear all aggregation result sets to be empty
				resultSet[i].clear( );
			}
			return false;
		}
		return true;
	}

	/**
	 * @param aggregations
	 * @param resultSet
	 * @param stopSign
	 * @throws IOException
	 * @throws DataException
	 * @throws BirtException
	 */
	public void applyAggrFilters( AggregationDefinition[] aggregations,
			IAggregationResultSet[] resultSet, StopSign stopSign )
			throws IOException, DataException, BirtException
	{
		boolean recalculate = false;
		
		if ( !aggrMeasureFilters.isEmpty( ) )
		{
			recalculate = populateAggrMeasureFilterResult( cube,
					resultSet,
					aggrMeasureFilters,
					cubeQueryExecutor,
					fetcher );
		}
		
		if ( !aggrFilterHelpers.isEmpty( ) )
		{
			AggregationFilterHelper filterHelper = new AggregationFilterHelper( cube,
					aggrFilterHelpers, fetcher );
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
			System.arraycopy( temp, 0, resultSet, 0, temp.length );
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
	protected IAggregationResultSet[] onePassExecute(
			AggregationDefinition[] aggregations, StopSign stopSign )
			throws DataException, IOException, BirtException
	{
		IDiskArray[] dimPosition = getFilterResult( );

		FactTableRowIterator factTableRowIterator = populateFactTableIterator( stopSign,
				dimPosition );
		DimensionResultIterator[] dimensionResultIterators = populateDimensionResultIterator( dimPosition, stopSign );

		IDataSet4Aggregation dataSet4Aggregation = new DataSetFromOriginalCube( factTableRowIterator,
				dimensionResultIterators,
				computedMeasureHelper );
		
		long memoryCacheSize = this.memoryCacheSize;
		if( this.appContext != null )
		{
			boolean use11SP3CubeQuery = CacheUtil.enableSP3CubeQueryChange( this.appContext );
			if( use11SP3CubeQuery )
				memoryCacheSize = -(memoryCacheSize);
		}
		AggregationExecutor aggregationCalculatorExecutor = new AggregationExecutor( new CubeDimensionReader( cube ),
				dataSet4Aggregation,
				aggregations,
				memoryCacheSize );
		
		aggregationCalculatorExecutor.setMaxDataObjectRows( maxDataObjectRows );
		
		return aggregationCalculatorExecutor.execute( stopSign );
	}

	/**
	 * 
	 * @param stopSign
	 * @param validDimensionName
	 * @param validDimPosition
	 * @return
	 * @throws IOException
	 */
	public FactTableRowIterator populateFactTableIterator( StopSign stopSign, IDiskArray[] dimPosition )
			throws IOException
	{
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
		return factTableRowIterator;
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
	protected List getDimensionJSFilterList( String dimensionName )
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
	public IDiskArray[] getFilterResult( ) throws DataException, IOException
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
	
	public void setMaxDataObjectRows( int rowSize )
	{
		this.maxDataObjectRows = rowSize;
	}
	
	public int getMaxDataObjectRows( )
	{
		return maxDataObjectRows;
	}
	
	public void setMemoryCacheSize( long memoryCacheSize )
	{
		this.memoryCacheSize = memoryCacheSize;
	}
	
	public void setAppContext( Map appContext )
	{
		this.appContext = appContext;
	}
	
	public long getMemoryCacheSize( )
	{
		return memoryCacheSize;
	}
	
	protected CubeQueryExecutorHelper createCubeQueryExecutorHelper(
			ICube cube, IComputedMeasureHelper computedMeasureHelper,
			IBindingValueFetcher fetcher ) throws DataException
	{
		return new CubeQueryExecutorHelper( cube,
				computedMeasureHelper,
				fetcher );
	}
}