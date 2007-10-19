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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.birt.core.archive.FileArchiveReader;
import org.eclipse.birt.core.archive.FileArchiveWriter;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.StopSign;
import org.eclipse.birt.data.engine.impl.document.QueryResultIDUtil;
import org.eclipse.birt.data.engine.impl.document.stream.VersionManager;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ICubeSortDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ILevelDefinition;
import org.eclipse.birt.data.engine.olap.data.api.CubeQueryExecutorHelper;
import org.eclipse.birt.data.engine.olap.data.api.DimLevel;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;
import org.eclipse.birt.data.engine.olap.data.api.IDimensionSortDefn;
import org.eclipse.birt.data.engine.olap.data.api.cube.DocManagerMap;
import org.eclipse.birt.data.engine.olap.data.api.cube.ICube;
import org.eclipse.birt.data.engine.olap.data.document.DocumentManagerFactory;
import org.eclipse.birt.data.engine.olap.data.document.IDocumentManager;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationFunctionDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationResultSetSaveUtil;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.sort.AggrSortDefinition;
import org.eclipse.birt.data.engine.olap.driver.CubeResultSet;
import org.eclipse.birt.data.engine.olap.driver.IResultSet;
import org.eclipse.birt.data.engine.olap.impl.query.CubeQueryExecutor;
import org.eclipse.birt.data.engine.olap.util.OlapExpressionCompiler;
import org.eclipse.birt.data.engine.olap.util.OlapExpressionUtil;

/**
 * 
 * 
 */
public class QueryExecutor
{

	/**
	 * @param view
	 * @param query
	 * @param stopSign
	 * @return
	 * @throws IOException
	 * @throws BirtException
	 */
	public IResultSet execute( BirtCubeView view, CubeQueryExecutor executor,
			MeasureNameManager manager, StopSign stopSign ) throws IOException, BirtException
	{
		AggregationDefinition[] aggrDefns = prepareCube( executor.getCubeQueryDefinition( ),
				manager.getCalculatedMembers( ) );
		if ( aggrDefns == null || aggrDefns.length == 0 )
			return null;
		IDocumentManager documentManager = getDocumentManager( executor );
		ICube cube = loadCube( documentManager, executor );
		CubeQueryValidator.validateCubeQueryDefinition( view,
				cube,
				manager.getCalculatedMembers( ) );
		CubeQueryExecutorHelper cubeQueryExcutorHelper = new CubeQueryExecutorHelper( cube );
		cubeQueryExcutorHelper.addJSFilter( executor.getDimensionFilterEvalHelpers( ) );
		populateAggregationSort( executor, cubeQueryExcutorHelper, true );
		populateAggregationSort( executor, cubeQueryExcutorHelper, false );
		IAggregationResultSet[] rs = null;
		cubeQueryExcutorHelper.setBreakHierarchy( executor.getCubeQueryDefinition( )
				.getFilterOption( ) == 0 );
		
		if ( executor.getContext( ).getMode( ) == DataEngineContext.MODE_GENERATION )
		{
			rs = populateRs( executor, aggrDefns, cubeQueryExcutorHelper, true );
		}
		else if ( executor.getContext( ).getMode( ) == DataEngineContext.DIRECT_PRESENTATION )
		{
			rs = populateRs( executor, aggrDefns, cubeQueryExcutorHelper, false );
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
		}
		cube.close( );
		return new CubeResultSet( rs, view, manager, cubeQueryExcutorHelper );
	}

	/**
	 * Populate the Result Set, either by re-execution ( If it has not been executed yet ) or 
	 * get it from local time folder.
	 * @param executor
	 * @param aggrDefns
	 * @param cubeQueryExcutorHelper
	 * @return
	 * @throws IOException
	 * @throws BirtException
	 */
	private IAggregationResultSet[] populateRs( CubeQueryExecutor executor,
			AggregationDefinition[] aggrDefns,
			CubeQueryExecutorHelper cubeQueryExcutorHelper, boolean saveToRD )
			throws IOException, BirtException
	{
		IAggregationResultSet[] rs;
		String id = null;
		//If not load from local dir
		if ( executor.getCubeQueryDefinition( ).getQueryResultsID( ) == null )
		{
			rs = cubeQueryExcutorHelper.execute( aggrDefns, new StopSign( ) );

			//If need save to local dir
			if ( executor.getCubeQueryDefinition( ).cacheQueryResults( ) )
			{
				id = QueryResultIDUtil.nextID( );
				FileArchiveWriter writer = new FileArchiveWriter( executor.getContext( )
						.getTmpdir( ) + "Cache");
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
					id = QueryResultIDUtil.nextID( );
					AggregationResultSetSaveUtil.save( id, rs, executor.getContext( )
							.getDocWriter( ) );
				}
				
			}	
		}
		else
		{
			//If query definition has query result id, that means a document has been save.
			id = executor.getCubeQueryDefinition( ).getQueryResultsID( );
			rs = AggregationResultSetSaveUtil.load( id,
					new FileArchiveReader( executor.getContext( )
							.getTmpdir( ) + "Cache" ), VersionManager.getLatestVersion( ) );
		}
		
		executor.setQueryResultsId( id );
		
		return rs;
	}

	/**
	 * 
	 * @param cubeQueryDefinition
	 * @param cubeQueryExcutorHelper
	 * @throws DataException
	 */
	private void populateAggregationSort( CubeQueryExecutor executor,
			CubeQueryExecutorHelper cubeQueryExcutorHelper, boolean isRow )
			throws DataException
	{
		List columnSort = isRow ? executor.getRowEdgeSort( )
				: executor.getColumnEdgeSort( );
		for ( int i = 0; i < columnSort.size( ); i++ )
		{
			ICubeSortDefinition cubeSort = (ICubeSortDefinition) columnSort.get( i );
			String bindingName = OlapExpressionUtil.getBindingName( cubeSort.getExpression( )
					.getText( ) );
			if ( bindingName == null )
				continue;
			List bindings = executor.getCubeQueryDefinition( ).getBindings( );
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
				
				String measureName = OlapExpressionCompiler.getReferencedScriptObject( binding.getExpression( ), "measure");
				if( measureName == null )
					continue;
				
				List measureAggrOns = CubeQueryDefinitionUtil.populateMeasureAggrOns( executor.getCubeQueryDefinition( ) );
				aggrOnLevels = new DimLevel[measureAggrOns.size( )];
				for ( int k = 0; k < measureAggrOns.size( ); k++ )
				{
					aggrOnLevels[k] = (DimLevel) measureAggrOns.get( k );
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
			AggrSortDefinition sort = new AggrSortDefinition( aggrOnLevels,
					bindingName,
					axisLevels,
					cubeSort.getAxisQualifierValues( ),
					new DimLevel( cubeSort.getTargetLevel( ) ),
					cubeSort.getSortDirection( ) == 1 ? false : true );
			if ( isRow )
				cubeQueryExcutorHelper.addRowSort( sort );
			else
				cubeQueryExcutorHelper.addColumnSort( sort );
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
							executor.getContext( ).getTmpdir( ) +
							executor.getSession( ).getEngine( ).hashCode( ) +
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

		int aggregationCount = getDistinctCalculatedMemberCount( calculatedMember );

		AggregationDefinition[] aggregations;
		if ( columnEdgeDefn == null && rowEdgeDefn == null )
			aggregations = new AggregationDefinition[aggregationCount];
		else if ( columnEdgeDefn == null || rowEdgeDefn == null )
			aggregations = new AggregationDefinition[aggregationCount + 1];
		else
			aggregations = new AggregationDefinition[aggregationCount + 2];

		int aggrIndex = 0;

		int[] sortType;
		if ( columnEdgeDefn != null )
		{
			DimLevel[] levelsForFilter = new DimLevel[levelsOnColumn.length];
			sortType = new int[levelsOnColumn.length];
			for ( int i = 0; i < levelsOnColumn.length; i++ )
			{
				levelsForFilter[i] = new DimLevel( levelsOnColumn[i] );
				sortType[i] = getSortDirection( levelsForFilter[i], query );
			}
			aggregations[aggrIndex] = new AggregationDefinition( levelsForFilter,
					sortType,
					null );
			aggrIndex++;
		}
		if ( rowEdgeDefn != null )
		{
			DimLevel[] levelsForFilter = new DimLevel[levelsOnRow.length];
			sortType = new int[levelsOnRow.length];
			for ( int i = 0; i < levelsOnRow.length; i++ )
			{
				levelsForFilter[i] = new DimLevel( levelsOnRow[i] );
				sortType[i] = getSortDirection( levelsForFilter[i], query );
			}
			aggregations[aggrIndex] = new AggregationDefinition( levelsForFilter,
					sortType,
					null );
			aggrIndex++;
		}

		if ( calculatedMember != null && calculatedMember.length > 0 )
		{
			List list;
			Set rsIDSet = new HashSet( );
			for ( int i = 0; i < calculatedMember.length; i++ )
			{
				if ( rsIDSet.contains( new Integer( calculatedMember[i].getRsID( ) ) ) )
					continue;
				list = getCalculatedMemberWithSameRSId( calculatedMember, i );
				AggregationFunctionDefinition[] funcitons = new AggregationFunctionDefinition[list.size( )];
				for ( int index = 0; index < list.size( ); index++ )
				{
					String[] dimInfo = ( (CalculatedMember) list.get( index ) ).getFirstArgumentInfo( );
					String dimName = null;
					String levelName = null;
					String attributeName = null;
					DimLevel dimLevel = null;
					if ( dimInfo != null && dimInfo.length == 3 )
					{
						dimName = ( (CalculatedMember) list.get( index ) ).getFirstArgumentInfo( )[0];
						levelName = ( (CalculatedMember) list.get( index ) ).getFirstArgumentInfo( )[1];
						attributeName = ( (CalculatedMember) list.get( index ) ).getFirstArgumentInfo( )[2];
						dimLevel = new DimLevel( dimName, levelName );
					}
					funcitons[index] = new AggregationFunctionDefinition( ( (CalculatedMember) list.get( index ) ).getName( ),
							( (CalculatedMember) list.get( index ) ).getMeasureName( ),
							dimLevel,
							attributeName,
							( (CalculatedMember) list.get( index ) ).getAggrFunction( ),
							( (CalculatedMember) list.get( index ) ).getFilterEvalHelper( ) );
				}

				DimLevel[] levels = new DimLevel[calculatedMember[i].getAggrOnList( )
						.size( )];
				sortType = new int[calculatedMember[i].getAggrOnList( ).size( )];
				for ( int index = 0; index < calculatedMember[i].getAggrOnList( )
						.size( ); index++ )
				{
					Object obj = calculatedMember[i].getAggrOnList( )
							.get( index );
					levels[index] = (DimLevel) obj;
					sortType[index] = getSortDirection( levels[index], query );
				}

				rsIDSet.add( new Integer( calculatedMember[i].getRsID( ) ) );
				aggregations[aggrIndex] = new AggregationDefinition( levels,
						sortType,
						funcitons );
				aggrIndex++;
			}
		}
		return aggregations;
	}

	/**
	 * 
	 * @param calMember
	 * @param index
	 * @return
	 */
	private List getCalculatedMemberWithSameRSId( CalculatedMember[] calMember,
			int index )
	{
		CalculatedMember member = calMember[index];
		List list = new ArrayList( );
		list.add( member );

		for ( int i = index + 1; i < calMember.length; i++ )
		{
			if ( calMember[i].getRsID( ) == member.getRsID( ) )
				list.add( calMember[i] );
		}
		return list;
	}

	/**
	 * 
	 * @param calMember
	 * @return
	 */
	private int getDistinctCalculatedMemberCount( CalculatedMember[] calMember )
	{
		Set rsIDSet = new HashSet( );
		for ( int i = 0; i < calMember.length; i++ )
		{
			if ( rsIDSet.contains( new Integer( calMember[i].getRsID( ) ) ) )
				continue;
			rsIDSet.add( new Integer( calMember[i].getRsID( ) ) );
		}
		return rsIDSet.size( );
	}

	/**
	 * 
	 * @param levelDefn
	 * @param query
	 * @return
	 * @throws DataException 
	 */
	private int getSortDirection( DimLevel level, ICubeQueryDefinition query ) throws DataException
	{
		if ( query.getSorts( ) != null && !query.getSorts( ).isEmpty( ) )
		{
			for ( int i = 0; i < query.getSorts( ).size( ); i++ )
			{
				ISortDefinition sortDfn = ( (ISortDefinition) query.getSorts( )
						.get( i ) );
				String expr = sortDfn.getExpression( ).getText( );
			
				DimLevel info = getDimLevel( expr, query.getBindings( ) );

				if ( level.equals( info ) )
				{
					return sortDfn.getSortDirection( );
				}
			}
		}
		return IDimensionSortDefn.SORT_UNDEFINED;
	}
	
	/**
	 * Get dim level from an expression.
	 * @param expr
	 * @param bindings
	 * @return
	 * @throws DataException
	 */
	private DimLevel getDimLevel( String expr, List bindings ) throws DataException
	{
		String bindingName = OlapExpressionUtil.getBindingName( expr );
		if( bindingName != null )
		{
			for( int j = 0; j < bindings.size( ); j++ )
			{
				IBinding binding = (IBinding)bindings.get( j );
				if( binding.getBindingName( ).equals( bindingName ))
				{
					if (! (binding.getExpression( ) instanceof IScriptExpression))
						return null;
					return getDimLevel( ((IScriptExpression)binding.getExpression( )).getText( ), bindings );
				}
			}
		}
		if ( OlapExpressionUtil.isReferenceToDimLevel( expr ) == false )
			return null;
		else 
			return OlapExpressionUtil.getTargetDimLevel( expr );
	}
}
