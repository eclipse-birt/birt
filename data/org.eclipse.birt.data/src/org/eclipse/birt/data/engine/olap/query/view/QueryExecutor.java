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
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.StopSign;
import org.eclipse.birt.data.engine.impl.document.stream.VersionManager;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ICubeSortDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ILevelDefinition;
import org.eclipse.birt.data.engine.olap.data.api.CubeQueryExecutorHelper;
import org.eclipse.birt.data.engine.olap.data.api.DimLevel;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;
import org.eclipse.birt.data.engine.olap.data.api.cube.ICube;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationResultSetSaveUtil;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.filter.LevelFilter;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.sort.AggrSortDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.sort.ITargetSort;
import org.eclipse.birt.data.engine.olap.driver.CubeResultSet;
import org.eclipse.birt.data.engine.olap.driver.IResultSet;
import org.eclipse.birt.data.engine.olap.impl.query.CubeOperationsExecutor;
import org.eclipse.birt.data.engine.olap.impl.query.CubeQueryExecutor;
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
	public IResultSet execute( BirtCubeView view,
			StopSign stopSign, ICube cube, boolean needSaveToDocWhenUpate ) throws IOException, BirtException
	{
		CubeQueryExecutor executor = view.getCubeQueryExecutor( );
		AggregationDefinition[] aggrDefns = prepareCube( executor.getCubeQueryDefinition( ),
				view.getAggregationRegisterTable( ).getCalculatedMembersFromQuery( ) );
		if ( aggrDefns == null || aggrDefns.length == 0 )
			return null;
		String cubeName = executor.getCubeQueryDefinition( ).getName( );
		if ( cubeName == null || cubeName.trim( ).length( ) == 0 )
		{
			throw new DataException( ResourceConstants.CUBE_QUERY_NO_CUBE_BINDING );
		}

		CubeQueryValidator.validateCubeQueryDefinition( view,
				cube );
		cubeQueryExecutorHelper = new CubeQueryExecutorHelper( cube,
				executor.getComputedMeasureHelper( ) );
		cubeQueryExecutorHelper.addJSFilter( executor.getDimensionFilterEvalHelpers( ) );
		cubeQueryExecutorHelper.addAggrMeasureFilter( executor.getMeasureFilterEvalHelpers( ) );
		cubeQueryExecutorHelper.addMeasureFilter( executor.getFacttableBasedFilterHelpers( ) );
		
		if ( view.getCubeQueryDefinition( ) instanceof DrillCubeQueryDefinition )
		{
			DrillCubeQueryDefinition query = (DrillCubeQueryDefinition) view.getCubeQueryDefinition( );
			for ( int i = 0; i < query.getLevelFilter( ).size( ); i++ )
			{
				cubeQueryExecutorHelper.addFilter( (LevelFilter) query.getLevelFilter( )
						.get( i ) );
			}
		}
		
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
				rs = populateRs( view, aggrDefns, cubeQueryExecutorHelper, 
						stopSign, true );
				break;
			}
			case DataEngineContext.DIRECT_PRESENTATION:
			{
				rs = populateRs( view, aggrDefns, cubeQueryExecutorHelper, 
						stopSign, false );
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
							new VersionManager( executor.getContext( ) ).getVersion( ) );
					break;
				}
				else
				{
					rs = cubeQueryExecutorHelper.execute( aggrDefns, stopSign );
					CubeOperationsExecutor coe = new CubeOperationsExecutor( view.getCubeQueryDefinition( ),
							view.getPreparedCubeOperations( ),
							view.getCubeQueryExecutor( ).getScope( ),
							view.getCubeQueryExecutor( ).getSession( ).getEngineContext( ).getScriptContext( ));

					rs = coe.execute( rs, stopSign );
					break;
				}
			}
			default:
			{
				//In Interactive viewing mode, we always re-execute the query.
				rs = cubeQueryExecutorHelper.execute( aggrDefns, stopSign );
				CubeOperationsExecutor coe = new CubeOperationsExecutor( view.getCubeQueryDefinition( ),
						view.getPreparedCubeOperations( ),
						view.getCubeQueryExecutor( ).getScope( ),
						view.getCubeQueryExecutor( ).getSession( ).getEngineContext( ).getScriptContext( ));

				rs = coe.execute( rs, stopSign );
				
				String id = executor.getCubeQueryDefinition( ).getQueryResultsID( );
				if (id == null) 
				{
					id = executor.getSession( ).getQueryResultIDUtil( ).nextID( );
				}
				
				if ( needSaveToDocWhenUpate )
				{
					// save rs back to report document
					AggregationResultSetSaveUtil.save( id,
							rs,
							executor.getContext( ).getDocWriter( ) );
					executor.setQueryResultsId( id );
				}
			}
		}
		
		return new CubeResultSet( rs, view, cubeQueryExecutorHelper );
	}

	private IAggregationResultSet[] populateRs( BirtCubeView view,
			AggregationDefinition[] aggrDefns,
			CubeQueryExecutorHelper cubeQueryExcutorHelper2,
			StopSign stopSign, boolean saveToRD ) throws IOException, BirtException
	{
		
		IAggregationResultSet[] rs;
		String id = null;
		CubeQueryExecutor executor = view.getCubeQueryExecutor( );
		//If not load from local dir
		if ( executor.getCubeQueryDefinition( ).getQueryResultsID( ) == null )
		{
			rs = cubeQueryExecutorHelper.execute( aggrDefns, executor.getSession( ).getStopSign( ) );
			
			CubeOperationsExecutor coe = new CubeOperationsExecutor(view.getCubeQueryDefinition( ),
					view.getPreparedCubeOperations( ),
					view.getCubeQueryExecutor( ).getScope( ),
					view.getCubeQueryExecutor( ).getSession( ).getEngineContext( ).getScriptContext( ));
			
			rs = coe.execute( rs, stopSign );

			//If need save to local dir
			if ( executor.getCubeQueryDefinition( ).cacheQueryResults( ) )
			{
				id = executor.getSession( ).getQueryResultIDUtil( ).nextID( );
				File tmpDir = new File( executor.getSession( ).getTempDir( ) );
				if (!FileSecurity.fileExist( tmpDir ) || ! FileSecurity.fileIsDirectory( tmpDir ))
				{
					FileSecurity.fileMakeDirs( tmpDir );
				}
				ArchiveWriter writer = new ArchiveWriter( new ArchiveFile( executor.getSession( )
						.getTempDir( )
						+ "Cache",
						"rw+" ) );
				AggregationResultSetSaveUtil.save( id,
						rs,
						writer );
				writer.finish( );
			}		
			
			if ( saveToRD)
			{
				//Save to RD using same id.
				if ( id != null )
				{
					AggregationResultSetSaveUtil.save( id, rs, executor.getContext( )
						.getDocWriter( ) );
				}else
				{
					id = executor.getSession( ).getQueryResultIDUtil( ).nextID( );
					AggregationResultSetSaveUtil.save( id, rs, executor.getContext( )
							.getDocWriter( ) );
				}
				
			}	
		}
		else
		{
			//If query definition has query result id, that means a cached document has been saved.
			id = executor.getCubeQueryDefinition( ).getQueryResultsID( );
			rs = AggregationResultSetSaveUtil.load( id,
					new FileArchiveReader( executor.getSession( ).getTempDir( ) + "Cache" ), VersionManager.getLatestVersion( ) );
			//TODO:Currently, share the same queryResultsID with the shared report item in the report document if the report document exists
		}
		
		executor.setQueryResultsId( id );
		
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
			default :
				return;
		}
		for ( int i = 0; i < columnSort.size( ); i++ )
		{
			ICubeSortDefinition cubeSort = (ICubeSortDefinition) columnSort.get( i );
			ICubeQueryDefinition queryDefn = executor.getCubeQueryDefinition( );
			String expr = cubeSort.getExpression( ).getText( );
			ITargetSort targetSort =  null;
			if ( cubeSort.getAxisQualifierLevels( ).length == 0
					&& ( OlapExpressionUtil.isComplexDimensionExpr( expr ) || OlapExpressionUtil.isReferenceToAttribute( cubeSort.getExpression( ),
							queryDefn.getBindings( ) ) ) )
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
					if ( measureName != null )
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