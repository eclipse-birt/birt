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
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.archive.FileArchiveReader;
import org.eclipse.birt.core.archive.compound.ArchiveFile;
import org.eclipse.birt.core.archive.compound.ArchiveWriter;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.core.security.FileSecurity;
import org.eclipse.birt.data.engine.impl.StopSign;
import org.eclipse.birt.data.engine.impl.document.stream.VersionManager;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ICubeSortDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ILevelDefinition;
import org.eclipse.birt.data.engine.olap.data.api.CubeQueryExecutorHelper;
import org.eclipse.birt.data.engine.olap.data.api.DimLevel;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;
import org.eclipse.birt.data.engine.olap.data.api.cube.DocManagerMap;
import org.eclipse.birt.data.engine.olap.data.api.cube.ICube;
import org.eclipse.birt.data.engine.olap.data.document.DocumentManagerFactory;
import org.eclipse.birt.data.engine.olap.data.document.IDocumentManager;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationResultSetSaveUtil;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.sort.AggrSortDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.sort.ITargetSort;
import org.eclipse.birt.data.engine.olap.driver.CubeResultSet;
import org.eclipse.birt.data.engine.olap.driver.IResultSet;
import org.eclipse.birt.data.engine.olap.impl.query.CubeOperationsExecutor;
import org.eclipse.birt.data.engine.olap.impl.query.CubeQueryExecutor;
import org.eclipse.birt.data.engine.olap.util.OlapExpressionCompiler;
import org.eclipse.birt.data.engine.olap.util.OlapExpressionUtil;
import org.eclipse.birt.data.engine.olap.util.filter.IAggrMeasureFilterEvalHelper;
import org.eclipse.birt.data.engine.olap.util.sort.DimensionSortEvalHelper;
import org.eclipse.birt.data.engine.script.ScriptConstants;
import org.mozilla.javascript.Scriptable;

/**
 * 
 * 
 */
public class QueryExecutor
{
	
	private CubeQueryExecutorHelper cubeQueryExcutorHelper;

	/**
	 * @param view
	 * @param query
	 * @param stopSign
	 * @return
	 * @throws IOException
	 * @throws BirtException
	 */
	public IResultSet execute( BirtCubeView view,
			StopSign stopSign ) throws IOException, BirtException
	{
		CubeQueryExecutor executor = view.getCubeQueryExecutor( );
		AggregationDefinition[] aggrDefns = prepareCube( executor.getCubeQueryDefinition( ),
				view.getMeasureNameManger( ).getCalculatedMembersFromQuery( ) );
		if ( aggrDefns == null || aggrDefns.length == 0 )
			return null;
		IDocumentManager documentManager = getDocumentManager( executor );
		ICube cube = loadCube( documentManager, executor );
		CubeQueryValidator.validateCubeQueryDefinition( executor.getCubeQueryDefinition( ),
				view,
				cube,
				view.getMeasureNameManger( ).getCalculatedMembersFromQuery( ) );
		cubeQueryExcutorHelper = new CubeQueryExecutorHelper( cube,
				executor.getComputedMeasureHelper( ) );
		cubeQueryExcutorHelper.addJSFilter( executor.getDimensionFilterEvalHelpers( ) );
		List list = executor.getMeasureFilterEvalHelpers( );
		for ( Iterator itr = list.iterator( ); itr.hasNext( ); )
		{
			IAggrMeasureFilterEvalHelper filterHelper = (IAggrMeasureFilterEvalHelper) itr.next( );
			cubeQueryExcutorHelper.addAggrMeasureFilter( filterHelper );
		}
		populateAggregationSort( executor, cubeQueryExcutorHelper, ICubeQueryDefinition.COLUMN_EDGE );
		populateAggregationSort( executor, cubeQueryExcutorHelper, ICubeQueryDefinition.ROW_EDGE );
		populateAggregationSort( executor, cubeQueryExcutorHelper, ICubeQueryDefinition.PAGE_EDGE );
		
		IAggregationResultSet[] rs = null;
		cubeQueryExcutorHelper.setBreakHierarchy( executor.getCubeQueryDefinition( )
				.getFilterOption( ) == 0 );
		
		if ( executor.getContext( ).getMode( ) == DataEngineContext.MODE_GENERATION )
		{
			rs = populateRs( view, aggrDefns, cubeQueryExcutorHelper, 
							stopSign, true );
		}
		else if ( executor.getContext( ).getMode( ) == DataEngineContext.DIRECT_PRESENTATION )
		{
			rs = populateRs( view, aggrDefns, cubeQueryExcutorHelper, 
							stopSign, false );
		}
		else if ( executor.getContext( ).getMode( ) == DataEngineContext.MODE_PRESENTATION )
		{
			assert executor.getCubeQueryDefinition( ).getQueryResultsID( ) != null;
			//In presentation mode, we need to load aggregation result set from report document.
			rs = AggregationResultSetSaveUtil.load( executor.getCubeQueryDefinition( )
					.getQueryResultsID( ),
					executor.getContext( ).getDocReader( ),new VersionManager( executor.getContext()).getVersion( ));
		}
		else
		{
			//In Interactive viewing mode, we always re-execute the query.
			rs = cubeQueryExcutorHelper.execute( aggrDefns, stopSign );
			CubeOperationsExecutor coe = new CubeOperationsExecutor( view.getCubeQueryDefinition( ),
					view.getPreparedCubeOperations( ) );

			rs = coe.execute( rs, stopSign );
			
			String id = executor.getCubeQueryDefinition( ).getQueryResultsID( );
			if (id == null) 
			{
				id = executor.getSession( ).getQueryResultIDUtil( ).nextID( );
			}
			//save rs back to report document
			AggregationResultSetSaveUtil.save( id, rs, executor.getContext( )
					.getDocWriter( ) );
			
			//TODO:affect the shared report item??
			
		}
		
		cube.close( );
		return new CubeResultSet( rs, view, cubeQueryExcutorHelper );
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
			rs = cubeQueryExcutorHelper.execute( aggrDefns, new StopSign( ) );
			
			CubeOperationsExecutor coe = new CubeOperationsExecutor(view.getCubeQueryDefinition( ),
					view.getPreparedCubeOperations( ));
			
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
				cubeQueryExcutorHelper,
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
	 * @param cubeName
	 * @return
	 * @throws IOException
	 * @throws DataException
	 */
	private ICube loadCube( IDocumentManager documentManager, CubeQueryExecutor executor ) throws DataException,
			IOException
	{
		ICube cube = null;
		
		cube = CubeQueryExecutorHelper.loadCube( executor.getCubeQueryDefinition( )
				.getName( ),
				documentManager,
				new StopSign( ) );

		return cube;
	}

	/**
	 * Get the document manager.
	 * 
	 * @param executor
	 * @return
	 * @throws DataException
	 * @throws IOException
	 */
	private IDocumentManager getDocumentManager( CubeQueryExecutor executor )
			throws DataException, IOException
	{
		if ( executor.getContext( ).getMode( ) == DataEngineContext.DIRECT_PRESENTATION
				|| executor.getContext( ).getMode( ) == DataEngineContext.MODE_GENERATION )
		{
			return DocManagerMap.getDocManagerMap( ).get
					( String.valueOf( executor.getSession( ).getEngine( ).hashCode( ) ),
							executor.getSession( ).getTempDir( ) +
							executor.getCubeQueryDefinition( ).getName( ) );
		}
		else
		{
			return DocumentManagerFactory.createRADocumentManager( executor.getContext( )
					.getDocReader( ) );
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
					null ));
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
			= CubeQueryDefinitionUtil.createAggregationDefinitons( calculatedMember, query );
		
		aggregations.addAll( Arrays.asList( fromCalculatedMembers ) );
		
		return aggregations.toArray( new AggregationDefinition[0] );
	}
}
