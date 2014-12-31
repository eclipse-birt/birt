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
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.core.security.FileSecurity;
import org.eclipse.birt.data.engine.executor.cache.CacheUtil;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.StopSign;
import org.eclipse.birt.data.engine.impl.document.stream.VersionManager;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.data.api.CubeQueryExecutorHelper;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;
import org.eclipse.birt.data.engine.olap.data.api.IBindingValueFetcher;
import org.eclipse.birt.data.engine.olap.data.api.cube.ICube;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationResultSetSaveUtil;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.MergedAggregationResultSet;
import org.eclipse.birt.data.engine.olap.driver.CubeResultSet;
import org.eclipse.birt.data.engine.olap.driver.IResultSet;
import org.eclipse.birt.data.engine.olap.impl.query.CubeOperationsExecutor;
import org.eclipse.birt.data.engine.olap.impl.query.CubeQueryDefinitionIOUtil;
import org.eclipse.birt.data.engine.olap.impl.query.CubeQueryExecutor;
import org.eclipse.birt.data.engine.olap.impl.query.IPreparedCubeOperation;
import org.eclipse.birt.data.engine.olap.impl.query.IncrementExecutionHint;
import org.eclipse.birt.data.engine.olap.impl.query.PreparedAddingNestAggregations;
import org.eclipse.birt.data.engine.olap.util.CubeAggrDefn;
import org.eclipse.birt.data.engine.olap.util.CubeNestAggrDefn;
import org.eclipse.birt.data.engine.olap.util.DrillFilterHelper;
/**
 * 
 * 
 */
public class QueryExecutorV0 implements IQueryExecutor
{
	
	private CubeQueryExecutorHelper cubeQueryExecutorHelper;
	private NoUpdateAggregateFilterHelper noUpdateFilterHelper;

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
		AggregationDefinition[] cube_Aggregation = QueryExecutorUtil.prepareCube( executor.getCubeQueryDefinition( ),
				view.getAggregationRegisterTable( )
						.getCalculatedMembersFromQuery( ),
				executor.getScope( ),
				executor.getSession( ).getEngineContext( ).getScriptContext( ) );
		
		if ( cube_Aggregation == null || cube_Aggregation.length == 0 )
			return null;
			
		 AggregationDefinition[] drilled_aggregation = DrillFilterHelper.preparedDrillAggregation( executor.getCubeQueryDefinition( ),
				cube_Aggregation );
	
		int size = cube_Aggregation.length + drilled_aggregation.length;
		AggregationDefinition[] finalAggregation = new AggregationDefinition[size];
		if ( drilled_aggregation.length > 0 )
		{
			System.arraycopy( cube_Aggregation,
					0,
					finalAggregation,
					0,
					cube_Aggregation.length );
			System.arraycopy( drilled_aggregation,
					0,
					finalAggregation,
					cube_Aggregation.length,
					drilled_aggregation.length );
		}
		else
		{
			finalAggregation = cube_Aggregation;
		}
		
		String cubeName = executor.getCubeQueryDefinition( ).getName( );
		if ( cubeName == null || cubeName.trim( ).length( ) == 0 )
		{
			throw new DataException( ResourceConstants.CUBE_QUERY_NO_CUBE_BINDING );
		}

		CubeQueryValidator.validateCubeQueryDefinition( view,
				cube );
		noUpdateFilterHelper = new NoUpdateAggregateFilterHelper( );
		
		cubeQueryExecutorHelper = new CubeQueryExecutorHelper( cube,
				executor.getComputedMeasureHelper( ), fetcher );
		cubeQueryExecutorHelper.setCubeQueryExecutor( executor );
		
		cubeQueryExecutorHelper.setMemoryCacheSize( CacheUtil.computeMemoryBufferSize( view.getAppContext( ) ) );
		cubeQueryExecutorHelper.setAppContext( view.getAppContext( ));
		cubeQueryExecutorHelper.setMaxDataObjectRows( CacheUtil.getMaxRows( view.getAppContext( ) ) );
		
		cubeQueryExecutorHelper.addJSFilter( executor.getDimensionFilterEvalHelpers( ) );
		cubeQueryExecutorHelper.addSimpleLevelFilter( executor.getdimensionSimpleFilter( ) );
		cubeQueryExecutorHelper.addAggrMeasureFilter( executor.getMeasureFilterEvalHelpers( ) );
		cubeQueryExecutorHelper.addMeasureFilter( executor.getFacttableBasedFilterHelpers( ) );
		
		QueryExecutorUtil.populateAggregationSort( executor, cubeQueryExecutorHelper, ICubeQueryDefinition.COLUMN_EDGE );
		QueryExecutorUtil.populateAggregationSort( executor, cubeQueryExecutorHelper, ICubeQueryDefinition.ROW_EDGE );
		QueryExecutorUtil.populateAggregationSort( executor, cubeQueryExecutorHelper, ICubeQueryDefinition.PAGE_EDGE );
		
		IAggregationResultSet[] rs = null;

		cubeQueryExecutorHelper.setBreakHierarchy( executor.getCubeQueryDefinition( )
				.getFilterOption( ) == 0 );
		
		switch ( executor.getContext( ).getMode( ) )
		{
			case DataEngineContext.MODE_GENERATION:
			{
				rs = populateRs( view, finalAggregation, cubeQueryExecutorHelper, 
						stopSign,
						true, fetcher );
				rs = processOperationOnQuery( executor, view, stopSign, rs, cube_Aggregation , fetcher );
				
				break;
			}
			case DataEngineContext.DIRECT_PRESENTATION:
			{
				rs = populateRs( view, finalAggregation, cubeQueryExecutorHelper, 
						stopSign, false, fetcher );
				rs = processOperationOnQuery( executor, view, stopSign, rs, cube_Aggregation , fetcher );
				
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
							new VersionManager( executor.getContext( ) ).getVersion( executor.getCubeQueryDefinition().getQueryResultsID() ),
							cubeQueryExecutorHelper.getMemoryCacheSize( ) );
					QueryExecutorUtil.initLoadedAggregationResultSets( rs, finalAggregation );

					rs = processOperationOnQuery( executor, view, stopSign, rs, cube_Aggregation , fetcher );
					break;
				}
				else
				{
					rs = cubeQueryExecutorHelper.execute( finalAggregation, stopSign );
					rs = QueryExecutorUtil.applyFilterOnOperation( view,
							cubeQueryExecutorHelper,
							executor,
							finalAggregation,
							rs,
							fetcher,
							stopSign );
					rs = noUpdateFilterHelper.applyNoAggrUpdateFilters( executor.getCubeQueryDefinition( )
							.getFilters( ),
							executor,
							rs,
							cube,
							fetcher,
							false );
					
					//process mirror operation
					MirrorOperationExecutor moe = new MirrorOperationExecutor( );
					rs = moe.execute( rs, view, cubeQueryExecutorHelper );
					QueryExecutorUtil.validateLimitSetting( view, rs );

					rs = processOperationOnQuery( executor, view, stopSign, rs, cube_Aggregation ,fetcher );
					
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
							id, executor.getContext( ) );
					ieh = org.eclipse.birt.data.engine.olap.impl.query.CubeQueryDefinitionUtil.getIncrementExecutionHint( 
							savedQuery, executor.getCubeQueryDefinition( ) );
				}
				if ( !CubeQueryDefinitionIOUtil.existStream( executor.getContext( ).getDocReader( ), id ) 
						|| ieh == null
						//|| ieh.isNoIncrement()
						//Currently, do not support increment execution when cube operations are involved.
						|| (!ieh.isNoIncrement( ) && executor.getCubeQueryDefinition( ).getCubeOperations( ).length > 0) 
				)
				{
					//need to re-execute the query.
					rs = cubeQueryExecutorHelper.execute( finalAggregation, stopSign );
					rs = QueryExecutorUtil.applyFilterOnOperation( view,
							cubeQueryExecutorHelper,
							executor,
							finalAggregation,
							rs,
							fetcher,
							stopSign );
					rs = noUpdateFilterHelper.applyNoAggrUpdateFilters( executor.getCubeQueryDefinition( )
							.getFilters( ),
							executor,
							rs,
							cube,
							fetcher,
							false );
					
					//process mirror operation
					MirrorOperationExecutor moe = new MirrorOperationExecutor( );
					rs = moe.execute( rs, view, cubeQueryExecutorHelper );
					QueryExecutorUtil.validateLimitSetting( view, rs );
				}
				else
				{
					//increment execute the query based on the saved aggregation result sets.
					rs = AggregationResultSetSaveUtil.load( id,
							executor.getContext( ).getDocReader( ),
							new VersionManager( executor.getContext( ) ).getVersion( id ),
							cubeQueryExecutorHelper.getMemoryCacheSize( ) );
					
					//Restore{@code AggregationDefinition} info first which are lost during saving aggregation result sets
					QueryExecutorUtil.initLoadedAggregationResultSets( rs, finalAggregation );
					incrementExecute( rs, ieh );
					if (ieh.getFilters() != null && ieh.getFilters().length > 0)
					{
						IFilterDefinition[] filters =ieh.getFilters();
						List finalFilters = new ArrayList();
						for(int j = 0 ; j < filters.length;j++)
						{
							finalFilters.add(filters[j]);
						}
						rs = noUpdateFilterHelper.applyNoAggrUpdateFilters( finalFilters,
								executor,
								rs,
								cube,
								fetcher,
								false );
					}
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
					CubeQueryDefinitionIOUtil.save( id, executor.getContext( ),
							executor.getCubeQueryDefinition( ) );
					AggregationResultSetSaveUtil.save( id,
							rs,
							executor.getContext( ).getDocWriter( ) );
					executor.setQueryResultsId( id );
				}

				rs = processOperationOnQuery( executor,view, stopSign, rs, cube_Aggregation , fetcher );
				
			}
		}
		
		return new CubeResultSet( rs, view, cubeQueryExecutorHelper );
	}
	

	private IAggregationResultSet[] processOperationOnQuery( CubeQueryExecutor executor,BirtCubeView view,
			StopSign stopSign, IAggregationResultSet[] resultSet,
			AggregationDefinition[] aggrDefns,IBindingValueFetcher fetcher ) throws DataException,
			IOException, BirtException
	{
		IAggregationResultSet[] rs = new IAggregationResultSet[aggrDefns.length];
		System.arraycopy( resultSet, 0, rs, 0, aggrDefns.length );

		IAggregationResultSet[] drillRs = new IAggregationResultSet[resultSet.length
				- aggrDefns.length];
		System.arraycopy( resultSet,
				aggrDefns.length,
				drillRs,
				0,
				drillRs.length );
		
		//process derived measure/nested aggregation
		CubeOperationsExecutor coe = new CubeOperationsExecutor( view.getCubeQueryDefinition( ),
				view.getPreparedCubeOperations( ),
				view.getCubeQueryExecutor( ).getScope( ),
				view.getCubeQueryExecutor( ).getSession( ).getEngineContext( ).getScriptContext( ) );

		int rsLenBefore = rs.length;
		rs = coe.execute( rs, stopSign, fetcher );
		int rsLenAfter = rs.length;
		
		List noAggrUpdateFilters = noUpdateFilterHelper.getNoAggrUpdateFilters( executor.getCubeQueryDefinition( )
				.getFilters( ) );
		if ( noAggrUpdateFilters.size() > 0 ) 
		{
			IAggregationResultSet[] result = null;
			if ( rsLenBefore < rsLenAfter )
			{
				result = new IAggregationResultSet[rsLenAfter - rsLenBefore];

				for ( int i = 0; i < result.length; i++ )
				{
					result[i] = rs[rsLenBefore + i];
				}

				result = noUpdateFilterHelper.applyNoAggrUpdateFilters( noAggrUpdateFilters,
						executor,
						result,
						view.getCube( ),
						fetcher,
						true );

				for ( int i = 0; i < result.length; i++ )
				{
					rs[i + rsLenBefore] = result[i];
				}
			}
			else if ( rsLenBefore == rsLenAfter )
			{
				List<IAggregationResultSet> mergedResult = new ArrayList<IAggregationResultSet>(); 
				for( int i = 0 ; i < rs.length; i++)
				{
					if ( rs[i].getAggregationDefinition( )
							.getAggregationFunctions( ) != null
							&& rs[i] instanceof MergedAggregationResultSet )
					{
						IAggregationResultSet[] applyResults = noUpdateFilterHelper.applyNoAggrUpdateFilters( noAggrUpdateFilters,
								executor,
								new IAggregationResultSet[]{
									rs[i]
								},
								view.getCube( ),
								fetcher,
								true );
						rs[i] = applyResults[0];
						mergedResult.add( rs[i] );
					}
				}
				result = mergedResult.toArray( new IAggregationResultSet[0] );
			}

			List<IAggregationResultSet> edgeResultSet = noUpdateFilterHelper.populateAndFilterEdgeResultSet(rs, null);

			for (int i = 0; i < edgeResultSet.size(); i++) 
			{
				for (int j = 0; j < result.length; j++) 
				{
					noUpdateFilterHelper.applyJoin(edgeResultSet.get(i), result[j]);
				}
			}
		}
		
		if( DrillFilterHelper.containsDrillFilter( view.getCubeQueryDefinition( ) ) )
		{
			IPreparedCubeOperation[] ops = view.getPreparedCubeOperations( );
			
			List<PreparedAddingNestAggregations> operations = new ArrayList<PreparedAddingNestAggregations>();
			List<CubeNestAggrDefn> nestedAggr = new ArrayList<CubeNestAggrDefn>( );
			List<AggregationDefinition> aggregations = new ArrayList<AggregationDefinition>();

			for( int i=0; i< ops.length; i++ )
			{
				List<AggregationDefinition> nested_aggregation = ops[i].getAggregationDefintions( );
				for( int k=0; k< nested_aggregation.size( ); k++)
				{
					AggregationDefinition[] drilled_nested_aggregation = DrillFilterHelper.preparedDrillForNestedAggregation( executor.getCubeQueryDefinition( ),
							new CubeAggrDefn[]{
								ops[i].getNewCubeAggrDefns( )[k]
							},
							new AggregationDefinition[]{
								nested_aggregation.get( k )
							} );
					for( AggregationDefinition aggregation: drilled_nested_aggregation )
					{
						CubeNestAggrDefn defn = (CubeNestAggrDefn) ( ops[i].getNewCubeAggrDefns( )[k] );
						nestedAggr.add( new CubeNestAggrDefn( defn.getName( ), 
								defn.getBasedExpression( ), Arrays.asList( ops[i].getNewCubeAggrDefns( )[k] ), defn.getAggrName( ),
								defn.getArguments( ), defn.getFilter( ) ) );
						aggregations.add( aggregation );
					}
				}
				
				if( !nestedAggr.isEmpty( ) )
				{
					PreparedAddingNestAggregations drill_coe = new PreparedAddingNestAggregations( );
					
					drill_coe.prepare( executor.getScope( ),
							view.getCubeQueryExecutor( )
									.getSession( )
									.getEngineContext( )
									.getScriptContext( ),
							view.getAggregationRegisterTable( ),
							nestedAggr.toArray( new CubeNestAggrDefn[0] ),
							aggregations );
					
					operations.add( drill_coe );
				}
			}
			if( !operations.isEmpty( ) )
			{				
				coe = new CubeOperationsExecutor( view.getCubeQueryDefinition( ),
						operations.toArray( new PreparedAddingNestAggregations[0] ),
						view.getCubeQueryExecutor( ).getScope( ),
						view.getCubeQueryExecutor( )
								.getSession( )
								.getEngineContext( )
								.getScriptContext( ) );
				
				drillRs = coe.execute( drillRs, stopSign, fetcher );			
			}
			
			//process drill operation
			DrillOperationExecutor drillOp = new DrillOperationExecutor( );

			rs = drillOp.execute( rs,
					drillRs,
					view.getCubeQueryDefinition( ) );
		}
		
		return rs;
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
				baseResultSets[i] = QueryExecutorUtil.sortAggregationResultSet( baseResultSets[i] );
			}
		}
		cubeQueryExecutorHelper.applyAggrSort( baseResultSets );
	}

	private IAggregationResultSet[] populateRs( BirtCubeView view,
			AggregationDefinition[] aggrDefns,
			CubeQueryExecutorHelper cubeQueryExcutorHelper2,
			StopSign stopSign, boolean saveToRD, IBindingValueFetcher fetcher ) throws IOException, BirtException
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

			rs = executeQuery( view, aggrDefns, saveToRD, id ,fetcher );
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
				QueryExecutorUtil.initLoadedAggregationResultSets( rs, aggrDefns );
				//TODO:Currently, share the same queryResultsID with the shared report item in the report document if the report document exists				
			}
			else
			{
				if ( executor.getContext( ).getDocReader( ) != null && executor.getContext( ).getMode( ) != DataEngineContext.MODE_GENERATION )
				{
					rs = AggregationResultSetSaveUtil.load( executor.getCubeQueryDefinition( )
							.getQueryResultsID( ),
							executor.getContext( ).getDocReader( ),
							new VersionManager( executor.getContext( ) ).getVersion( id ),
							cubeQueryExecutorHelper.getMemoryCacheSize( ) );
					QueryExecutorUtil.initLoadedAggregationResultSets( rs, aggrDefns );

				}
				else
				{
					rs = executeQuery( view, aggrDefns, saveToRD, id ,fetcher );
				}
			}
		}		
		executor.setQueryResultsId( id );
		
		return rs;
	}
	
	private IAggregationResultSet[] executeQuery( BirtCubeView view,
			AggregationDefinition[] aggrDefns, boolean saveToRD,
			String queryResutID, IBindingValueFetcher fetcher ) throws IOException, BirtException
	{
		IAggregationResultSet[] rs;
		CubeQueryExecutor executor = view.getCubeQueryExecutor( );
		
		rs = cubeQueryExecutorHelper.execute( aggrDefns, executor.getSession( ).getStopSign( ) );
		rs = QueryExecutorUtil.applyFilterOnOperation( view,
				cubeQueryExecutorHelper,
				executor,
				aggrDefns,
				rs,
				fetcher,
				executor.getSession( ).getStopSign( ) );
		rs = noUpdateFilterHelper.applyNoAggrUpdateFilters( executor.getCubeQueryDefinition( )
				.getFilters( ),
				executor,
				rs,
				view.getCube( ),
				fetcher,
				false );
		//process mirror operation
		MirrorOperationExecutor moe = new MirrorOperationExecutor( );
		rs = moe.execute( rs, view, cubeQueryExecutorHelper );

		QueryExecutorUtil.validateLimitSetting( view, rs );	
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
					, executor.getCubeQueryDefinition( ) );
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
}
