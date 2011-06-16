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

package org.eclipse.birt.data.engine.olap.query.view;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.birt.core.archive.FileArchiveReader;
import org.eclipse.birt.core.archive.compound.ArchiveFile;
import org.eclipse.birt.core.archive.compound.ArchiveWriter;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.core.security.FileSecurity;
import org.eclipse.birt.data.engine.executor.cache.CacheUtil;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.StopSign;
import org.eclipse.birt.data.engine.impl.document.stream.VersionManager;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ICubeSortDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDrillFilter;
import org.eclipse.birt.data.engine.olap.api.query.ILevelDefinition;
import org.eclipse.birt.data.engine.olap.data.api.CubeQueryExecutorHelper;
import org.eclipse.birt.data.engine.olap.data.api.DimLevel;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;
import org.eclipse.birt.data.engine.olap.data.api.IBindingValueFetcher;
import org.eclipse.birt.data.engine.olap.data.api.cube.ICube;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationResultSetSaveUtil;
import org.eclipse.birt.data.engine.olap.data.impl.CachedAggregationResultSet;
import org.eclipse.birt.data.engine.olap.data.impl.DrilledAggregation;
import org.eclipse.birt.data.engine.olap.data.impl.DrilledAggregationDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.AggregationResultSet;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.SortedAggregationRowArray;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.sort.AggrSortDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.sort.ITargetSort;
import org.eclipse.birt.data.engine.olap.driver.CubeResultSet;
import org.eclipse.birt.data.engine.olap.driver.IResultSet;
import org.eclipse.birt.data.engine.olap.impl.query.CubeOperationsExecutor;
import org.eclipse.birt.data.engine.olap.impl.query.CubeQueryDefinitionIOUtil;
import org.eclipse.birt.data.engine.olap.impl.query.CubeQueryExecutor;
import org.eclipse.birt.data.engine.olap.impl.query.IncrementExecutionHint;
import org.eclipse.birt.data.engine.olap.util.OlapExpressionCompiler;
import org.eclipse.birt.data.engine.olap.util.OlapExpressionUtil;
import org.eclipse.birt.data.engine.olap.util.sort.DimensionSortEvalHelper;
import org.eclipse.birt.data.engine.script.ScriptConstants;
import org.mozilla.javascript.Scriptable;

/**
 * 
 * 
 */
public class QueryExecutor
{
	
	private CubeQueryExecutorHelper cubeQueryExecutorHelper;

	/**
	 * @param view
	 * @param query
	 * @param stopSign
	 * @return
	 * @throws IOException
	 * @throws BirtException
	 */
	public IResultSet execute( BirtCubeView view, StopSign stopSign, ICube cube, IBindingValueFetcher fetcher )
			throws IOException, BirtException
	{
		CubeQueryExecutor executor = view.getCubeQueryExecutor( );
		AggregationDefinition[] aggrDefns = prepareCube( executor.getCubeQueryDefinition( ),
				view.getAggregationRegisterTable( ).getCalculatedMembersFromQuery( ) );
		
		if ( aggrDefns == null || aggrDefns.length == 0 )
			return null;
		
		DrilledAggregationDefinition[] drillAggrDefns = preparedDrillAggregation( executor.getCubeQueryDefinition( ),
				aggrDefns );
		int size = aggrDefns.length + drillAggrDefns.length;
		AggregationDefinition[] finalAggregation = new AggregationDefinition[size];
		if ( drillAggrDefns.length > 0 )
		{
			System.arraycopy( aggrDefns,
					0,
					finalAggregation,
					0,
					aggrDefns.length );
			System.arraycopy( drillAggrDefns,
					0,
					finalAggregation,
					aggrDefns.length,
					drillAggrDefns.length );
		}
		else
		{
			finalAggregation = aggrDefns;
		}
		String cubeName = executor.getCubeQueryDefinition( ).getName( );
		if ( cubeName == null || cubeName.trim( ).length( ) == 0 )
		{
			throw new DataException( ResourceConstants.CUBE_QUERY_NO_CUBE_BINDING );
		}

		CubeQueryValidator.validateCubeQueryDefinition( view,
				cube );
		cubeQueryExecutorHelper = new CubeQueryExecutorHelper( cube,
				executor.getComputedMeasureHelper( ), fetcher );
		
		cubeQueryExecutorHelper.setMemoryCacheSize( CacheUtil.computeMemoryBufferSize( view.getAppContext( ) ) );
		cubeQueryExecutorHelper.setMaxDataObjectRows( CacheUtil.getMaxRows( view.getAppContext( ) ) );
		
		cubeQueryExecutorHelper.addJSFilter( executor.getDimensionFilterEvalHelpers( ) );
		cubeQueryExecutorHelper.addSimpleLevelFilter( executor.getdimensionSimpleFilter( ) );
		cubeQueryExecutorHelper.addAggrMeasureFilter( executor.getMeasureFilterEvalHelpers( ) );
		cubeQueryExecutorHelper.addMeasureFilter( executor.getFacttableBasedFilterHelpers( ) );
		
		populateAggregationSort( executor, cubeQueryExecutorHelper, ICubeQueryDefinition.COLUMN_EDGE );
		populateAggregationSort( executor, cubeQueryExecutorHelper, ICubeQueryDefinition.ROW_EDGE );
		populateAggregationSort( executor, cubeQueryExecutorHelper, ICubeQueryDefinition.PAGE_EDGE );
		
		IAggregationResultSet[] rs = null;

		cubeQueryExecutorHelper.setBreakHierarchy( executor.getCubeQueryDefinition( )
				.getFilterOption( ) == 0 );
		
		switch ( executor.getContext( ).getMode( ))
		{
			case DataEngineContext.MODE_GENERATION:
			{
				rs = populateRs( view, finalAggregation, cubeQueryExecutorHelper, 
						stopSign,
						true );
				
				rs = processOperationOnQuery( view, stopSign, rs, aggrDefns );
				
				break;
			}
			case DataEngineContext.DIRECT_PRESENTATION:
			{
				rs = populateRs( view, finalAggregation, cubeQueryExecutorHelper, 
						stopSign, false );
				
				rs = processOperationOnQuery( view, stopSign, rs, aggrDefns );

				break;
			}
			case DataEngineContext.MODE_PRESENTATION:
			{
				if ( executor.getCubeQueryDefinition( ).getQueryResultsID( ) != null )
				{// In presentation mode, we need to load aggregation result set
					// from report document.
					rs = AggregationResultSetSaveUtil.load( executor.getCubeQueryDefinition( )
							.getQueryResultsID( ),
							executor.getContext( ).getDocReader( ),
							new VersionManager( executor.getContext( ) ).getVersion( ),
							cubeQueryExecutorHelper.getMemoryCacheSize( ) );
					initLoadedAggregationResultSets( rs, finalAggregation );

					rs = processOperationOnQuery( view,
							stopSign,
							rs,
							aggrDefns );
					break;
				}
				else
				{
					rs = cubeQueryExecutorHelper.execute( finalAggregation, stopSign );
					//process mirror operation
					MirrorOperationExecutor moe = new MirrorOperationExecutor( );
					rs = moe.execute( rs, view, cubeQueryExecutorHelper );
					
					rs = processOperationOnQuery( view,
							stopSign,
							rs,
							aggrDefns );
					break;
				}
			}
			default:
			{
				String id = executor.getCubeQueryDefinition( ).getQueryResultsID( );
				IncrementExecutionHint ieh = null;
				if ( CubeQueryDefinitionIOUtil.existStream( 
						executor.getContext( ).getDocReader( ), id )) 
				{
					ICubeQueryDefinition savedQuery = CubeQueryDefinitionIOUtil.load( 
							id, executor.getContext( ).getDocReader( ) );
					ieh = org.eclipse.birt.data.engine.olap.impl.query.CubeQueryDefinitionUtil.getIncrementExecutionHint( 
							savedQuery, executor.getCubeQueryDefinition( ) );
				}
				if ( !CubeQueryDefinitionIOUtil.existStream( executor.getContext( ).getDocReader( ), id ) 
						|| ieh == null
						
						//Currently, do not support increment execution when cube operations are involved.
						|| (!ieh.isNoIncrement( ) && executor.getCubeQueryDefinition( ).getCubeOperations( ).length > 0) 
				)
				{
					//need to re-execute the query.
					rs = cubeQueryExecutorHelper.execute( finalAggregation, stopSign );
					//process mirror operation
					MirrorOperationExecutor moe = new MirrorOperationExecutor( );
					rs = moe.execute( rs, view, cubeQueryExecutorHelper );
				}
				else
				{
					//increment execute the query based on the saved aggregation result sets.
					rs = AggregationResultSetSaveUtil.load( id,
							executor.getContext( ).getDocReader( ),
							new VersionManager( executor.getContext( ) ).getVersion( ),
							cubeQueryExecutorHelper.getMemoryCacheSize( ) );
					
					//Restore{@code AggregationDefinition} info first which are lost during saving aggregation result sets
					initLoadedAggregationResultSets( rs, finalAggregation );
					
					incrementExecute( rs, ieh );
				}
				if ( executor.getContext( ).getDocWriter( ) != null )
				{
					if ( id == null )
					{
						id = executor.getSession( )
								.getQueryResultIDUtil( )
								.nextID( );
					}
					// save rs back to report document
					CubeQueryDefinitionIOUtil.save( id, executor.getContext( )
							.getDocWriter( ), executor.getCubeQueryDefinition( ) );
					AggregationResultSetSaveUtil.save( id,
							rs,
							executor.getContext( ).getDocWriter( ) );
					executor.setQueryResultsId( id );
				}

				rs = processOperationOnQuery( view,
						stopSign,
						rs,
						aggrDefns );
			}
		}
		
		return new CubeResultSet( rs, view, cubeQueryExecutorHelper );
	}

	private DrilledAggregationDefinition[] preparedDrillAggregation(
			ICubeQueryDefinition cubeQueryDefinition,
			AggregationDefinition[] aggrDefns )
	{
		IEdgeDefinition columnEdge = cubeQueryDefinition.getEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cubeQueryDefinition.getEdge( ICubeQueryDefinition.ROW_EDGE );
		List<DrillOnDimensionHierarchy> columnDrill = CubeQueryDefinitionUtil.flatternDrillFilter( columnEdge );
		List<DrillOnDimensionHierarchy> rowDrill = CubeQueryDefinitionUtil.flatternDrillFilter( rowEdge );
		List<DrillOnDimensionHierarchy> combinedDrill = new ArrayList<DrillOnDimensionHierarchy>( );
		combinedDrill.addAll( rowDrill );
		combinedDrill.addAll( columnDrill );

		if ( combinedDrill.isEmpty( ) )
			return new DrilledAggregationDefinition[0];

		List<DrilledAggregation> aggregation = new ArrayList<DrilledAggregation>();
		for ( int i = 0; i < aggrDefns.length; i++ )
		{
			if ( aggrDefns[i].getAggregationFunctions( ) == null )
				continue;
			DimLevel[] levels = aggrDefns[i].getLevels( );
			if ( levels == null )
				continue;

			List<List<DimLevel>> groupByDimension = new ArrayList<List<DimLevel>>( );
			String dimensionName = null;
			List<DimLevel> list = null;
			for ( int j = 0; j < levels.length - 1; j++ )
			{
				if ( dimensionName != null
						&& dimensionName.equals( levels[j].getDimensionName( ) ) )
				{
					if ( isDrilledLevel( levels[j], combinedDrill ) )
						list.add( levels[j] );
				}
				else
				{
					list = new ArrayList<DimLevel>( );
					if ( isDrilledLevel( levels[j], combinedDrill ) )
						list.add( levels[j] );
					dimensionName = levels[j].getDimensionName( );
					groupByDimension.add( list );
				}
			}
			if ( groupByDimension.isEmpty( ) )
				continue;

			List<DimLevel[]> tagetLevels = new ArrayList<DimLevel[]>( );
			tagetLevels.add( levels );

			buildAggregationDimLevel( tagetLevels, groupByDimension, 0 );

			for ( int k = 1; k < tagetLevels.size( ); k++ )
			{
				boolean exist = false;
				for ( int t = 0; t < aggregation.size( ); t++ )
				{
					if ( aggregation.get( t )
							.matchTargetlevels( tagetLevels.get( k ) ) )
					{
						aggregation.get( t )
								.addOriginalAggregation( aggrDefns[i] );
						exist = true;
						break;
					}
				}
				if ( exist )
					continue;
				DrilledAggregation aggr = new DrilledAggregation( tagetLevels.get( k ), cubeQueryDefinition );
				aggr.addOriginalAggregation( aggrDefns[i] );
				aggregation.add( aggr );
			}
		}

		DrilledAggregationDefinition[] a = new DrilledAggregationDefinition[aggregation.size( )];
		for ( int i = 0; i < aggregation.size( ); i++ )
		{
			a[i] = new DrilledAggregationDefinition( aggregation.get( i ),
					aggregation.get( i ).getSortType( ),
					aggregation.get( i ).getAggregationFunctionDefinition( ) );
		}
		return a;
	}

	private void buildAggregationDimLevel( List<DimLevel[]> tagetLevels,
			List<List<DimLevel>> groupByDimension, int dimIndex )
	{
		List<DimLevel> l = (List<DimLevel>) groupByDimension.get( dimIndex );
		List<DimLevel[]> temp = new ArrayList<DimLevel[]>( );
		for ( int t = 0; t < l.size( ); t++ )
		{
			DimLevel dimLevel = l.get( t );
			for ( int i = 0; i < tagetLevels.size( ); i++ )
				temp.add( getDrilledDimLevel( dimLevel, tagetLevels.get( i ) ) );
		}
		tagetLevels.addAll( temp );
		dimIndex++;
		if ( dimIndex < groupByDimension.size( ) )
		{
			buildAggregationDimLevel( tagetLevels, groupByDimension, dimIndex );
		}
	}

	private DimLevel[] getDrilledDimLevel( DimLevel dimLevel, DimLevel[] levels )
	{
		boolean find = false;
		List<DimLevel> d = new ArrayList<DimLevel>( );
		for ( int i = 0; i < levels.length; i++ )
		{
			if ( !dimLevel.getDimensionName( )
					.equals( levels[i].getDimensionName( ) ) )
			{
				d.add( levels[i] );
			}
			else
			{
				if ( dimLevel.equals( levels[i] ) )
				{
					find = true;
					d.add( levels[i] );
				}
				if ( !find )
					d.add( levels[i] );
			}
		}
		DimLevel[] dim = new DimLevel[d.size( )];
		for( int i=0; i< dim.length; i++ )
		{
			dim[i] = d.get( i );
		}
		return dim;
	}

	private boolean isDrilledLevel( DimLevel levels,
			List<DrillOnDimensionHierarchy> combinedDrill )
	{
		for( int i=0; i< combinedDrill.size( ); i++ )
		{
			DrillOnDimensionHierarchy dim = combinedDrill.get( i );
			List<IEdgeDrillFilter> filters = dim.getDrillFilterByLevel( levels );
			if( filters!= null&& !filters.isEmpty( ) )
			{
				return true;
			}
		}
		return false;
	}

	private IAggregationResultSet[] processOperationOnQuery( BirtCubeView view,
			StopSign stopSign, IAggregationResultSet[] rs,
			AggregationDefinition[] aggrDefns ) throws DataException,
			IOException, BirtException
	{
		//process drill operation
		DrillOperationExecutor drillOp = new DrillOperationExecutor( );
		IAggregationResultSet[] baseRs = new IAggregationResultSet[aggrDefns.length];
		System.arraycopy( rs, 0, baseRs, 0, aggrDefns.length );

		IAggregationResultSet[] drillRs = new IAggregationResultSet[rs.length
				- aggrDefns.length];
		System.arraycopy( rs,
				aggrDefns.length,
				drillRs,
				0,
				drillRs.length );
		rs = drillOp.execute( baseRs,
				drillRs,
				view.getCubeQueryDefinition( ) );

		//process derived measure/nested aggregation
		CubeOperationsExecutor coe = new CubeOperationsExecutor( view.getCubeQueryDefinition( ),
				view.getPreparedCubeOperations( ),
				view.getCubeQueryExecutor( ).getScope( ),
				view.getCubeQueryExecutor( ).getSession( ).getEngineContext( ).getScriptContext( ));

		rs = coe.execute( rs, stopSign );
		
		return rs;
	}
	
	private void initLoadedAggregationResultSets( IAggregationResultSet[] arss, AggregationDefinition[] ads )
	{
		assert ads.length <= arss.length;
		for ( int i=0; i<ads.length; i++)
		{
			CachedAggregationResultSet cars = (CachedAggregationResultSet)arss[i];
			cars.setAggregationDefinition( ads[i] );
		}
	}
	
	private IAggregationResultSet sortAggregationResultSet( IAggregationResultSet rs ) throws IOException
	{
		SortedAggregationRowArray sarr = new SortedAggregationRowArray( rs );
		rs.close( );
		return new AggregationResultSet( rs.getAggregationDefinition( ),
				rs.getAllLevels( ),
				sarr.getSortedRows( ),
				rs.getKeyNames( ),
				rs.getAttributeNames( ));
	}
	
	private void incrementExecute( IAggregationResultSet[] baseResultSets, 
			IncrementExecutionHint ieh ) throws DataException, IOException
	{
		assert baseResultSets != null && ieh != null;
		applyIncrementSorts( baseResultSets );
	}
	
	private void applyIncrementSorts( IAggregationResultSet[] baseResultSets ) throws DataException, IOException
	{
		//Make sure all edge aggregation result sets are already sorted
		for ( int i = 0; i < baseResultSets.length; i++ )
		{
			if ( baseResultSets[i].getAggregationCount( ) == 0 ) //edge aggregation result set 
			{
				baseResultSets[i] = sortAggregationResultSet( baseResultSets[i] );
			}
		}
		cubeQueryExecutorHelper.applyAggrSort( baseResultSets );
	}

	private IAggregationResultSet[] populateRs( BirtCubeView view,
			AggregationDefinition[] aggrDefns,
			CubeQueryExecutorHelper cubeQueryExcutorHelper2,
			StopSign stopSign, boolean saveToRD ) throws IOException, BirtException
	{
		
		IAggregationResultSet[] rs = null;
		String id = null;
		CubeQueryExecutor executor = view.getCubeQueryExecutor( );
		//If not load from local dir
		if ( executor.getCubeQueryDefinition( ).getQueryResultsID( ) == null )
		{
			if ( saveToRD
					|| executor.getCubeQueryDefinition( ).cacheQueryResults( ) )
				id = executor.getSession( ).getQueryResultIDUtil( ).nextID( );

			rs = executeQuery( view, aggrDefns, saveToRD, id );
		}
		else
		{
			id = executor.getCubeQueryDefinition( ).getQueryResultsID( );

			if ( executor.getCubeQueryDefinition( ).cacheQueryResults( ) )
			{
				//If query definition has query result id, that means a cached document has been saved.
				rs = AggregationResultSetSaveUtil.load( id,
						new FileArchiveReader( executor.getSession( ).getTempDir( ) + "Cache" ),
						VersionManager.getLatestVersion( ),
						cubeQueryExecutorHelper.getMemoryCacheSize( ) );
				initLoadedAggregationResultSets( rs, aggrDefns );
				//TODO:Currently, share the same queryResultsID with the shared report item in the report document if the report document exists				
			}
			else
			{
				if ( executor.getContext( ).getDocReader( ) != null )
				{
					rs = AggregationResultSetSaveUtil.load( executor.getCubeQueryDefinition( )
							.getQueryResultsID( ),
							executor.getContext( ).getDocReader( ),
							new VersionManager( executor.getContext( ) ).getVersion( ),
							cubeQueryExecutorHelper.getMemoryCacheSize( ) );
					initLoadedAggregationResultSets( rs, aggrDefns );

				}
				else
				{
					rs = executeQuery( view, aggrDefns, saveToRD, id );
				}
			}
		}		
		executor.setQueryResultsId( id );
		
		return rs;
	}
	
	private IAggregationResultSet[] executeQuery( BirtCubeView view,
			AggregationDefinition[] aggrDefns, boolean saveToRD,
			String queryResutID ) throws IOException, BirtException
	{
		IAggregationResultSet[] rs;
		CubeQueryExecutor executor = view.getCubeQueryExecutor( );
		
		rs = cubeQueryExecutorHelper.execute( aggrDefns, executor.getSession( ).getStopSign( ) );
		//process mirror operation
		MirrorOperationExecutor moe = new MirrorOperationExecutor( );
		rs = moe.execute( rs, view, cubeQueryExecutorHelper );
		
		//If need save to local dir
		if ( executor.getCubeQueryDefinition( ).cacheQueryResults( ) )
		{
			File tmpDir = new File( executor.getSession( ).getTempDir( ) );
			if (!FileSecurity.fileExist( tmpDir ) || ! FileSecurity.fileIsDirectory( tmpDir ))
			{
				FileSecurity.fileMakeDirs( tmpDir );
			}
			ArchiveWriter writer = new ArchiveWriter( new ArchiveFile( executor.getSession( )
					.getTempDir( )
					+ "Cache",
					"rw+" ) );
			AggregationResultSetSaveUtil.save( queryResutID,
					rs,
					writer );
			writer.finish( );
		}		
		//only save the raw aggregation result into RD.
		if ( saveToRD )
		{
			CubeQueryDefinitionIOUtil.save( queryResutID, executor.getContext( )
					.getDocWriter( ), executor.getCubeQueryDefinition( ) );
			AggregationResultSetSaveUtil.save( queryResutID, rs, executor.getContext( )
					.getDocWriter( ) );
		}
		return rs;
	}

	/**
	 * 
	 * @param parentResultSet
	 * @param view
	 * @param startingColumnLevelIndex
	 * @param startingRowLevelIndex
	 * @return
	 * @throws IOException
	 */
	public IResultSet executeSubQuery( IResultSet parentResultSet,
			BirtCubeView view, int startingColumnLevelIndex,
			int startingRowLevelIndex ) throws IOException
	{
		return new CubeResultSet( parentResultSet,
				view,
				cubeQueryExecutorHelper,
				startingColumnLevelIndex,
				startingRowLevelIndex );
	}
	
	/**
	 * 
	 * @param cubeQueryDefinition
	 * @param cubeQueryExcutorHelper
	 * @throws DataException
	 */
	private void populateAggregationSort( CubeQueryExecutor executor,
			CubeQueryExecutorHelper cubeQueryExcutorHelper, int type )
			throws DataException
	{
		List columnSort;
		switch ( type )
		{
			case ICubeQueryDefinition.COLUMN_EDGE :
				columnSort = executor.getColumnEdgeSort( );
				break;
			case ICubeQueryDefinition.ROW_EDGE :
				columnSort = executor.getRowEdgeSort( );
				break;
			case ICubeQueryDefinition.PAGE_EDGE :
				columnSort = executor.getPageEdgeSort( );
				break;
			default :
				return;
		}
		for ( int i = 0; i < columnSort.size( ); i++ )
		{
			ICubeSortDefinition cubeSort = (ICubeSortDefinition) columnSort.get( i );
			ICubeQueryDefinition queryDefn = executor.getCubeQueryDefinition( );
			String expr = cubeSort.getExpression( ).getText( );
			ITargetSort targetSort =  null;
			if ( ( cubeSort.getAxisQualifierLevels( ).length == 0 && ( OlapExpressionUtil.isComplexDimensionExpr( expr ) || OlapExpressionUtil.isReferenceToAttribute( cubeSort.getExpression( ),
					queryDefn.getBindings( ) ) ) )
					|| ( !OlapExpressionUtil.isDirectRerenrence( cubeSort.getExpression( ),
							executor.getCubeQueryDefinition( ).getBindings( ) ) ) )
			{
				Scriptable scope = executor.getSession( ).getSharedScope( );
				targetSort = new DimensionSortEvalHelper( executor.getOuterResults( ),
						scope,
						queryDefn,
						cubeSort,
						executor.getSession( ).getEngineContext( ).getScriptContext( )
						);
			}
			else
			{
				String bindingName = OlapExpressionUtil.getBindingName( expr );
				if ( bindingName == null )
					continue;
				List bindings = queryDefn.getBindings( );
				List aggrOns = null;
				IBinding binding = null;
				for ( int j = 0; j < bindings.size( ); j++ )
				{
					binding = (IBinding) bindings.get( j );
					if ( binding.getBindingName( ).equals( bindingName ) )
					{
						aggrOns = binding.getAggregatOns( );
						break;
					}
				}
				
				DimLevel[] aggrOnLevels = null;
	
				if ( aggrOns == null || aggrOns.size( ) == 0 )
				{
					if ( binding == null )
						continue;
					
					String measureName = OlapExpressionCompiler.getReferencedScriptObject( binding.getExpression( ), ScriptConstants.MEASURE_SCRIPTABLE);
					if ( measureName == null )
					{
						IBinding referBinding = OlapExpressionUtil.getDirectMeasureBinding( binding,
								bindings );
						if( referBinding != null )
						{
							measureName = OlapExpressionUtil.getMeasure( referBinding.getExpression( ) );
							bindingName = referBinding.getBindingName( );
							aggrOns = referBinding.getAggregatOns( );
						}
					}
					if ( aggrOns != null && aggrOns.size( ) > 0 )
					{
						aggrOnLevels = new DimLevel[aggrOns.size( )];
						for ( int j = 0; j < aggrOnLevels.length; j++ )
						{
							aggrOnLevels[j] = OlapExpressionUtil.getTargetDimLevel( aggrOns.get( j )
									.toString( ) );
						}
					}
					else if ( measureName != null )
					{
						List measureAggrOns = CubeQueryDefinitionUtil.populateMeasureAggrOns( queryDefn );
						aggrOnLevels = new DimLevel[measureAggrOns.size( )];
						for ( int k = 0; k < measureAggrOns.size( ); k++ )
						{
							aggrOnLevels[k] = (DimLevel) measureAggrOns.get( k );
						}
					}
				}
				else
				{
					aggrOnLevels = new DimLevel[aggrOns.size( )];
					for ( int j = 0; j < aggrOnLevels.length; j++ )
					{
						aggrOnLevels[j] = OlapExpressionUtil.getTargetDimLevel( aggrOns.get( j )
								.toString( ) );
					}
				}
				DimLevel[] axisLevels = new DimLevel[cubeSort.getAxisQualifierLevels( ).length];
				for ( int k = 0; k < axisLevels.length; k++ )
				{
					axisLevels[k] = new DimLevel( cubeSort.getAxisQualifierLevels( )[k] );
				}
				targetSort = new AggrSortDefinition( aggrOnLevels,
						bindingName,
						axisLevels,
						cubeSort.getAxisQualifierValues( ),
						new DimLevel( cubeSort.getTargetLevel( ) ),
						cubeSort.getSortDirection( ) );
			}
			switch( type)
			{
				case ICubeQueryDefinition.COLUMN_EDGE:
					cubeQueryExcutorHelper.addColumnSort( targetSort );
					break;
				case ICubeQueryDefinition.ROW_EDGE:
					cubeQueryExcutorHelper.addRowSort( targetSort );
					break;
				case ICubeQueryDefinition.PAGE_EDGE:
					cubeQueryExcutorHelper.addPageSort( targetSort );
			}
		}
	}

	/**
	 * 
	 * @param cube
	 * @param query
	 * @return
	 * @throws DataException 
	 */
	private AggregationDefinition[] prepareCube( ICubeQueryDefinition query,
			CalculatedMember[] calculatedMember ) throws DataException
	{
		IEdgeDefinition columnEdgeDefn = query.getEdge( ICubeQueryDefinition.COLUMN_EDGE );
		ILevelDefinition[] levelsOnColumn = CubeQueryDefinitionUtil.getLevelsOnEdge( columnEdgeDefn );
		IEdgeDefinition rowEdgeDefn = query.getEdge( ICubeQueryDefinition.ROW_EDGE );
		ILevelDefinition[] levelsOnRow = CubeQueryDefinitionUtil.getLevelsOnEdge( rowEdgeDefn );
		IEdgeDefinition pageEdgeDefn = query.getEdge( ICubeQueryDefinition.PAGE_EDGE );
		ILevelDefinition[] levelsOnPage = CubeQueryDefinitionUtil.getLevelsOnEdge( pageEdgeDefn );

		List<AggregationDefinition> aggregations = new ArrayList<AggregationDefinition>();

		int[] sortType;
		if ( columnEdgeDefn != null )
		{
			DimLevel[] levelsForFilter = new DimLevel[levelsOnColumn.length
					+ levelsOnPage.length];
			sortType = new int[levelsOnColumn.length + levelsOnPage.length];
			int index = 0;
			for ( ; index < levelsOnPage.length; )
			{
				levelsForFilter[index] = new DimLevel( levelsOnPage[index] );
				sortType[index] = CubeQueryDefinitionUtil.getSortDirection( levelsForFilter[index],
						query );
				index++;
			}
			for ( int i = 0; i < levelsOnColumn.length; i++ )
			{
				levelsForFilter[index] = new DimLevel( levelsOnColumn[i] );
				sortType[index] = CubeQueryDefinitionUtil.getSortDirection( levelsForFilter[i], query );
				index++;
			}
			aggregations.add(new AggregationDefinition( levelsForFilter,
					sortType,
					null ) );
		}
		if ( rowEdgeDefn != null )
		{
			DimLevel[] levelsForFilter = new DimLevel[levelsOnRow.length
					+ levelsOnPage.length];
			sortType = new int[levelsOnRow.length + levelsOnPage.length];
			int index = 0;
			for ( ; index < levelsOnPage.length; )
			{
				levelsForFilter[index] = new DimLevel( levelsOnPage[index] );
				sortType[index] = CubeQueryDefinitionUtil.getSortDirection( levelsForFilter[index],
						query );
				index++;
			}
			for ( int i = 0; i < levelsOnRow.length; i++ )
			{
				levelsForFilter[index] = new DimLevel( levelsOnRow[i] );
				sortType[index] = CubeQueryDefinitionUtil.getSortDirection( levelsForFilter[i], query );
				index++;
			}
			aggregations.add( new AggregationDefinition( levelsForFilter,
					sortType,
					null ));
		}
		if( pageEdgeDefn!= null )
		{
			DimLevel[] levelsForFilter = new DimLevel[levelsOnPage.length];
			sortType = new int[levelsOnPage.length];
			for ( int i = 0; i < levelsOnPage.length; i++ )
			{
				levelsForFilter[i] = new DimLevel( levelsOnPage[i] );
				sortType[i] = CubeQueryDefinitionUtil.getSortDirection( levelsForFilter[i], query );
			}
			aggregations.add(new AggregationDefinition( levelsForFilter,
					sortType,
					null ));
		}		
		
		AggregationDefinition[] fromCalculatedMembers
			= CubeQueryDefinitionUtil.createAggregationDefinitons( calculatedMember, query, null, null );
		
		aggregations.addAll( Arrays.asList( fromCalculatedMembers ) );
		
		return aggregations.toArray( new AggregationDefinition[0] );
	}
}