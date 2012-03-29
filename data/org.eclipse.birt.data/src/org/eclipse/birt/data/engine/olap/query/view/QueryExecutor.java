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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.archive.FileArchiveReader;
import org.eclipse.birt.core.archive.compound.ArchiveFile;
import org.eclipse.birt.core.archive.compound.ArchiveWriter;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.ICollectionConditionalExpression;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.IScriptExpression;
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
import org.eclipse.birt.data.engine.olap.data.impl.Cube;
import org.eclipse.birt.data.engine.olap.data.impl.DrilledAggregation;
import org.eclipse.birt.data.engine.olap.data.impl.DrilledAggregationDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.AggregationResultRow;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.AggregationResultSet;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.SortedAggregationRowArray;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.filter.AggrMeasureFilterHelper;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.filter.AggregationFilterHelper;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.sort.AggrSortDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.sort.ITargetSort;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Member;
import org.eclipse.birt.data.engine.olap.data.util.BufferedStructureArray;
import org.eclipse.birt.data.engine.olap.data.util.CompareUtil;
import org.eclipse.birt.data.engine.olap.data.util.IDiskArray;
import org.eclipse.birt.data.engine.olap.driver.CubeResultSet;
import org.eclipse.birt.data.engine.olap.driver.IResultSet;
import org.eclipse.birt.data.engine.olap.impl.query.CubeOperationsExecutor;
import org.eclipse.birt.data.engine.olap.impl.query.CubeQueryDefinitionIOUtil;
import org.eclipse.birt.data.engine.olap.impl.query.CubeQueryExecutor;
import org.eclipse.birt.data.engine.olap.impl.query.IncrementExecutionHint;
import org.eclipse.birt.data.engine.olap.util.OlapExpressionCompiler;
import org.eclipse.birt.data.engine.olap.util.OlapExpressionUtil;
import org.eclipse.birt.data.engine.olap.util.filter.AggrMeasureFilterEvalHelper;
import org.eclipse.birt.data.engine.olap.util.filter.BaseDimensionFilterEvalHelper;
import org.eclipse.birt.data.engine.olap.util.filter.IFacttableRow;
import org.eclipse.birt.data.engine.olap.util.filter.IJSFacttableFilterEvalHelper;
import org.eclipse.birt.data.engine.olap.util.filter.IJSFilterHelper;
import org.eclipse.birt.data.engine.olap.util.filter.JSFacttableFilterEvalHelper;
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
		cubeQueryExecutorHelper.setCubeQueryExecutor( executor );
		
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
		
		switch ( executor.getContext( ).getMode( ) )
		{
			case DataEngineContext.MODE_GENERATION:
			{
				rs = populateRs( view, finalAggregation, cubeQueryExecutorHelper, 
						stopSign,
						true, fetcher );
				rs = processOperationOnQuery( view, stopSign, rs, aggrDefns );
				
				break;
			}
			case DataEngineContext.DIRECT_PRESENTATION:
			{
				rs = populateRs( view, finalAggregation, cubeQueryExecutorHelper, 
						stopSign, false, fetcher );
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
							new VersionManager( executor.getContext( ) ).getVersion( executor.getCubeQueryDefinition().getQueryResultsID() ),
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
					rs = applyNoAggrUpdateFilters(getNoAggrUpdateFilters( executor.getCubeQueryDefinition( ).getFilters( ) ), executor, rs, cube, fetcher );
					
					//process mirror operation
					MirrorOperationExecutor moe = new MirrorOperationExecutor( );
					rs = moe.execute( rs, view, cubeQueryExecutorHelper );
					validateLimitSetting( view, rs );

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
					rs = applyNoAggrUpdateFilters(getNoAggrUpdateFilters( executor.getCubeQueryDefinition( ).getFilters( ) ), executor, rs, cube, fetcher );
					
					//process mirror operation
					MirrorOperationExecutor moe = new MirrorOperationExecutor( );
					rs = moe.execute( rs, view, cubeQueryExecutorHelper );
					this.validateLimitSetting( view, rs );
				}
				else
				{
					//increment execute the query based on the saved aggregation result sets.
					rs = AggregationResultSetSaveUtil.load( id,
							executor.getContext( ).getDocReader( ),
							new VersionManager( executor.getContext( ) ).getVersion( id ),
							cubeQueryExecutorHelper.getMemoryCacheSize( ) );
					
					//Restore{@code AggregationDefinition} info first which are lost during saving aggregation result sets
					initLoadedAggregationResultSets( rs, finalAggregation );
					incrementExecute( rs, ieh );
					if (ieh.getFilters() != null && ieh.getFilters().length > 0)
					{
						IFilterDefinition[] filters =ieh.getFilters();
						List finalFilters = new ArrayList();
						for(int j = 0 ; j < filters.length;j++)
						{
							finalFilters.add(filters[j]);
						}
						rs = applyNoAggrUpdateFilters(finalFilters,executor, rs, cube, fetcher);
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

				rs = processOperationOnQuery( view,
						stopSign,
						rs,
						aggrDefns );
			}
		}
		
		return new CubeResultSet( rs, view, cubeQueryExecutorHelper );
	}
	
	private IAggregationResultSet[] applyNoAggrUpdateFilters ( List finalFilters, CubeQueryExecutor executor , IAggregationResultSet[] rs , ICube cube, IBindingValueFetcher fetcher ) throws DataException, IOException
	{
		if( !finalFilters.isEmpty( ) )
		{
			List aggrEvalList = new ArrayList<AggrMeasureFilterEvalHelper>( );
			List dimEvalList = new ArrayList<IJSFilterHelper>( );
			List<IFilterDefinition> drillFilterList = new ArrayList<IFilterDefinition>( );
			for ( int i = 0; i < finalFilters.size( ); i++ )
			{
				IFilterDefinition filter = (IFilterDefinition) finalFilters.get( i );
				int type = executor.getFilterType( filter,
						executor.getDimLevelsDefinedInCubeQuery( ) );
			
				if ( type == executor.DIMENSION_FILTER )
				{
					dimEvalList.add( BaseDimensionFilterEvalHelper.createFilterHelper( executor.getOuterResults( ),
							executor.getScope( ),
							executor.getCubeQueryDefinition( ),
							filter,
							executor.getSession( )
									.getEngineContext( )
									.getScriptContext( ) ) );
				}
				else if ( type == executor.AGGR_MEASURE_FILTER )
				{
					aggrEvalList.add( new AggrMeasureFilterEvalHelper( executor.getOuterResults( ),
							executor.getScope( ),
							executor.getCubeQueryDefinition( ),
							filter,
							executor.getSession( )
									.getEngineContext( )
									.getScriptContext( ) ) );
				}
				else if( type == executor.FACTTABLE_FILTER )
				{
					drillFilterList.add( filter );
				}
			}
			List<Integer> affectedAggrResultSetIndex = new ArrayList<Integer>();
			if( aggrEvalList.size( ) > 0)
			{
				AggrMeasureFilterHelper aggrFilterHelper = new AggrMeasureFilterHelper( cube,
						rs );
				rs = aggrFilterHelper.removeInvalidAggrRows( aggrEvalList, affectedAggrResultSetIndex );
				
			}
			if ( dimEvalList.size( ) > 0 )
			{
				AggregationFilterHelper helper = new AggregationFilterHelper( (Cube)cube, dimEvalList, fetcher );
				rs = helper.generateFilteredAggregationResultSet( rs , affectedAggrResultSetIndex );
			}
			
			
			Map<DimLevel, IJSFacttableFilterEvalHelper> edgeDrillFilterMap = populateEdgeDrillFilterMap( executor, drillFilterList );
		
			List<IAggregationResultSet> edgeResultSet =	populateAndFilterEdgeResultSet(rs, edgeDrillFilterMap);
			
			for ( int i = 0; i < edgeResultSet.size( ); i++ )
			{
				for ( int j = 0; j < affectedAggrResultSetIndex.size( ); j++ )
				{
					this.applyJoin( edgeResultSet.get( i ),
							rs[affectedAggrResultSetIndex.get( j ).intValue( )] );
				}
			}
			
			if( edgeResultSet.size( ) > 1 )
			{
				combineEdgeResultSetsInfo( edgeResultSet );
			}
		}
		
		return rs;
	}
	
	private void combineEdgeResultSetsInfo( List<IAggregationResultSet> edgeResultSet )
	{
		int index = -1;
		for( int i = 0; i < edgeResultSet.size( ); i++ )
		{
			IAggregationResultSet rs = edgeResultSet.get( i );
			if( rs.length() == 0 )
				index = i;
		}
		if( index >= 0 )
		{
			for( int i = 0; i < edgeResultSet.size( ); i++ )
			{
				if ( i != index ) 
				{
					IAggregationResultSet rs = edgeResultSet.get( i );
					IDiskArray newRsRows = new BufferedStructureArray( AggregationResultRow.getCreator( ), rs.length( ) );
					if ( rs instanceof AggregationResultSet )
						( (AggregationResultSet) rs ).setAggregationResultRows( newRsRows );
					else if (rs instanceof CachedAggregationResultSet)
						( (CachedAggregationResultSet) rs ).setAggregationResultRows( newRsRows );
				}
			}
		}
	}

	private List<IAggregationResultSet> populateAndFilterEdgeResultSet(IAggregationResultSet[] rs,
			Map<DimLevel, IJSFacttableFilterEvalHelper> edgeDrillFilterMap)
			throws IOException, DataException 
	{
		List<IAggregationResultSet> edgeResultSet = new ArrayList<IAggregationResultSet>();	

		for ( int i = 0; i < rs.length; i++ )
		{
			if ( rs[i].getAggregationDefinition( )
					.getAggregationFunctions( ) == null )
			{
				edgeResultSet.add( rs[i] );
				
				if( edgeDrillFilterMap.isEmpty() )
					continue;
		
				filterEdgeAggrSet(edgeDrillFilterMap,  rs[i]);
			}		
		}
		
		return edgeResultSet;
	}

	private void filterEdgeAggrSet(
			Map<DimLevel, IJSFacttableFilterEvalHelper> edgeDrillFilterMap,
			IAggregationResultSet edgeAggrSet) throws IOException,
			DataException 
	{
		IJSFacttableFilterEvalHelper drillFilterHelper = null;
		for( DimLevel dimLevel : edgeAggrSet.getAllLevels() )
		{
			if( (drillFilterHelper = edgeDrillFilterMap.get( dimLevel ))!= null )
			{
				AggregateRowWrapper aggrRowWrapper = new AggregateRowWrapper( edgeAggrSet );
				IDiskArray newRs = new BufferedStructureArray( AggregationResultRow.getCreator( ), 2000 );
				for( int j = 0; j < edgeAggrSet.length(); j++ )
				{
					edgeAggrSet.seek( j );
					if( drillFilterHelper.evaluateFilter( aggrRowWrapper ))
					{
						newRs.add(  edgeAggrSet.getCurrentRow() );
					}
				}
				reSetAggregationResultSetDiskArray(edgeAggrSet, newRs);
			}
		}
	}

	private void reSetAggregationResultSetDiskArray(
			IAggregationResultSet edgeAggrSet, IDiskArray newRs) 
	{
		if( edgeAggrSet instanceof AggregationResultSet )
			((AggregationResultSet)edgeAggrSet).setAggregationResultRows( newRs );
		else if( edgeAggrSet instanceof CachedAggregationResultSet )
			((CachedAggregationResultSet)edgeAggrSet).setAggregationResultRows( newRs );
	}

	/**
	 * Populate Edge Drill Filter Map. There will be one random level picked from edge to map to a drill filter. 
	 * 
	 * @param executor
	 * @param drillFilterList
	 * @param edgeDrillFilterMap
	 * @throws DataException
	 */
	private  Map<DimLevel, IJSFacttableFilterEvalHelper> populateEdgeDrillFilterMap(CubeQueryExecutor executor,
			List<IFilterDefinition> drillFilterList )
			throws DataException 
	{
		Map<DimLevel, IJSFacttableFilterEvalHelper> result = new HashMap<DimLevel, IJSFacttableFilterEvalHelper>();

		for( IFilterDefinition filterDefn: drillFilterList )
		{
			assert filterDefn instanceof ICollectionConditionalExpression;
			Collection<IScriptExpression> exprs = ( ( ICollectionConditionalExpression )( filterDefn.getExpression( ) ) ).getExpr( );
			Iterator<IScriptExpression> exprsIterator = exprs.iterator( );
			DimLevel containedDimLevel = null;
			while ( exprsIterator.hasNext( ) )
			{
				Iterator dimLevels = OlapExpressionCompiler.getReferencedDimLevel( exprsIterator.next( ),
						new ArrayList() )
						.iterator( );
				while ( dimLevels.hasNext( ) )
				{
					containedDimLevel = (DimLevel) dimLevels.next( );
					break;
				}
				if( containedDimLevel!= null )
					break;
			}
			
			if( containedDimLevel == null )
				continue;
			
			
			result.put( containedDimLevel, new JSFacttableFilterEvalHelper(
					executor.getScope(), executor.getSession()
							.getEngineContext().getScriptContext(),
					filterDefn, null, null));
		}
		return result;
	}
	
	private int getPos(String[][] joinLevelKeys, String[][] detailLevelKeys)
	{
		for (int i = 0; i < detailLevelKeys.length; i++)
		{
			if (CompareUtil.compare(joinLevelKeys[0], detailLevelKeys[i] )==0)
			{
				return i;
			}
		}
		return -1;
	}
	
	private void applyJoin(IAggregationResultSet joinRS, IAggregationResultSet detailRS) throws IOException 
	{
		String[][] detailLevelKeys = detailRS.getLevelKeys( );
		List<Members> detailMember = new ArrayList<Members>();
		String[][] joinLevelKeys = null;
		Member[] members = null;
		IDiskArray aggregationResultRows = null;

    	joinLevelKeys = joinRS.getLevelKeys( );

    	int pos = getPos(joinLevelKeys, detailLevelKeys);
    	if( pos < 0 )
    		return;
    	
    	for (int index = 0; index < detailRS.length( ); index++)
    	{
    		detailRS.seek( index );
    		members = detailRS.getCurrentRow( ).getLevelMembers( );
    		if (members == null)
    		{
    			continue;
    		}
    		List<Member> tmpMembers = new ArrayList<Member>();
    		for (int j = pos; j < pos + joinLevelKeys.length; j++)
        	{
    			if ( j > members.length - 1)
    			{
    				break;
    			}
    			if (CompareUtil.compare(joinLevelKeys[j - pos], detailLevelKeys[j] )==0)
    			{
    				tmpMembers.add (members[j]);
    			}
                
        	}
    		detailMember.add( new Members(tmpMembers.toArray( new Member[]{} )) );
    	}
    	Collections.sort( detailMember );
    	if( joinRS instanceof AggregationResultSet )
    		aggregationResultRows = ((AggregationResultSet)joinRS).getAggregationResultRows();
    	else if( joinRS instanceof CachedAggregationResultSet )
    		aggregationResultRows = ((CachedAggregationResultSet)joinRS).getAggregationResultRows();
		IDiskArray newRsRows = new BufferedStructureArray(AggregationResultRow.getCreator( ), aggregationResultRows.size( ));
		int result;
		for (int index = 0; index < joinRS.length( ); index++)
    	{
			joinRS.seek( index );
    		result = Collections.binarySearch( detailMember, new Members( joinRS.getCurrentRow( ).getLevelMembers( ) ) );
    		
    		if (result >= 0 )
    		{
    			newRsRows.add(aggregationResultRows.get( index ));
    		}
    	}
		reSetAggregationResultSetDiskArray(joinRS, newRsRows);
    	detailMember.clear( );
    
	}
	
	private class Members implements Comparable<Members>
	{

		public Member[] members;
		public Members(Member[] members)
		{
			this.members = members;
		}
		
		public int compareTo( Members other )
		{
			for (int i = 0; i < members.length; i++)
			{
				int result = members[i].compareTo( other.members[i] );
				if (result != 0)
				{
					return result;
				}
			}
			return 0;
		}

	}
	
	private List getNoAggrUpdateFilters( List filters )
	{
		List NoAggrUpdateFilters = new ArrayList( );

		for ( int i = 0; i < filters.size( ); i++ )
		{
			if ( !( (IFilterDefinition) filters.get( i ) ).updateAggregation( ) )
			{	
				NoAggrUpdateFilters.add(filters.get(i));
			}
		}
		return NoAggrUpdateFilters;
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
							new VersionManager( executor.getContext( ) ).getVersion( id ),
							cubeQueryExecutorHelper.getMemoryCacheSize( ) );
					initLoadedAggregationResultSets( rs, aggrDefns );

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
		rs = applyNoAggrUpdateFilters( getNoAggrUpdateFilters( executor.getCubeQueryDefinition( ).getFilters( ) ),executor, rs, view.getCube( ) , fetcher );
		//process mirror operation
		MirrorOperationExecutor moe = new MirrorOperationExecutor( );
		rs = moe.execute( rs, view, cubeQueryExecutorHelper );

		validateLimitSetting( view, rs );	
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
	
	/**
	 * If the length of edge cursor exceed the limit setting, throw exception.
	 * @param cubeView
	 * @param rsArray
	 * @throws DataException
	 */
	private void validateLimitSetting( BirtCubeView cubeView, IAggregationResultSet[] rsArray ) throws DataException
	{
		int count = 0;
		if ( cubeView.getColumnEdgeView( ) != null )
		{
			if( cubeView.getAppContext( )!= null )
			{
				int limitSize = populateFetchLimitSize( cubeView.getAppContext( )
						.get( DataEngine.CUBECURSOR_FETCH_LIMIT_ON_COLUMN_EDGE ) );
				if ( limitSize > 0 && limitSize < rsArray[count].length( ) )
				{
					throw new DataException( ResourceConstants.RESULT_LENGTH_EXCEED_COLUMN_LIMIT,
							new Object[]{
								limitSize
							} );
				}
			}
			count++;
		}
		if ( cubeView.getRowEdgeView( ) != null )
		{
			if ( cubeView.getAppContext( ) != null )
			{
				int limitSize = populateFetchLimitSize( cubeView.getAppContext( )
						.get( DataEngine.CUBECUSROR_FETCH_LIMIT_ON_ROW_EDGE ) );
				if ( limitSize > 0 && limitSize < rsArray[count].length( ) )
				{
					throw new DataException( ResourceConstants.RESULT_LENGTH_EXCEED_ROW_LIMIT,
							new Object[]{
								limitSize
							} );
				}
			}
			count++;
		}
	}
	
	/**
	 * 
	 * @param propValue
	 * @return
	 */
	private int populateFetchLimitSize( Object propValue )
	{
		int fetchLimit = -1;
		String fetchLimitSize = propValue == null ? "-1" : propValue.toString( );

		if ( fetchLimitSize != null )
			fetchLimit = Integer.parseInt( fetchLimitSize );

		return fetchLimit;
	}
	
	/**
	 * The class that wrap an IAggregationResultSet instance into IFacttableRow. The actual IAggregationResultRow instance returned
	 * is controlled by internal cursor in IAggregationResultSet instance.
	 * 
	 * @author lzhu
	 *
	 */
	private class AggregateRowWrapper implements IFacttableRow
	{
		private IAggregationResultSet aggrResultSet;
		public AggregateRowWrapper( IAggregationResultSet aggrResultSet )
		{
			this.aggrResultSet = aggrResultSet;
		}
		
		/**
		 * Not implemented. We only expect this being used in drill filter in which only level member is used.
		 */
		public Object getMeasureValue(String measureName) throws DataException 
		{
			throw new UnsupportedOperationException();
		}

		public Object[] getLevelKeyValue(String dimensionName, String levelName)
				throws DataException, IOException 
		{
			return this.aggrResultSet.getLevelKeyValue( this.aggrResultSet.getLevelIndex( new DimLevel( dimensionName, levelName ) ));
		}

		/**
		 * Not implemented. We only expect this being used in drill filter in which only level member is used.
		 */
		public Object getLevelAttributeValue(String dimensionName,
				String levelName, String attributeName) throws DataException,
				IOException 
		{
			throw new UnsupportedOperationException();
		}
		
	}
}
