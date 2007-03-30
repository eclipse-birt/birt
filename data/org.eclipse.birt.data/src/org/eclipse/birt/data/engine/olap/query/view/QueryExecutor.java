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

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.api.cube.ICube;
import org.eclipse.birt.data.engine.olap.api.cube.StopSign;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ILevelDefinition;
import org.eclipse.birt.data.engine.olap.api.query.impl.CubeQueryExecutor;
import org.eclipse.birt.data.engine.olap.data.api.CubeQueryExcutorHelper;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;
import org.eclipse.birt.data.engine.olap.data.api.IDimensionSortDefn;
import org.eclipse.birt.data.engine.olap.data.document.DocumentManagerFactory;
import org.eclipse.birt.data.engine.olap.data.document.IDocumentManager;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationFunctionDefinition;
import org.eclipse.birt.data.engine.olap.driver.CubeResultSet;
import org.eclipse.birt.data.engine.olap.driver.IResultSet;
import org.eclipse.birt.data.engine.olap.util.OlapExpressionUtil;

/**
 *  
 *
 */
public class QueryExecutor
{


	/**
	 * 
	 * @param view
	 * @param query
	 * @return
	 * @throws IOException
	 * @throws BirtException
	 */
	public IResultSet execute( BirtCubeView view, CubeQueryExecutor executor,
			MeasureNameManager manager ) throws IOException, BirtException
	{
		ICube cube = loadCube( executor );
		AggregationDefinition[] aggrDefns = prepareCube( cube,
				executor.getCubeQueryDefinition( ) );
		CubeQueryExcutorHelper cubeQueryExcutorHelper = new CubeQueryExcutorHelper( cube );
		cubeQueryExcutorHelper.addJSFilter( executor.getDimensionFilterEvalHelpers( ) );
		
		IAggregationResultSet[] rs = cubeQueryExcutorHelper.execute( aggrDefns,
				new StopSign( ) );
		return new CubeResultSet( rs, view, manager );
	}

	/**
	 * 
	 * @param cubeName
	 * @return
	 * @throws IOException 
	 * @throws DataException 
	 */
	private ICube loadCube( CubeQueryExecutor executor ) throws DataException, IOException 
	{
		ICube cube = null;
		IDocumentManager documentManager;
		documentManager = getDocumentManager( executor );

		cube = CubeQueryExcutorHelper.loadCube( executor.getCubeQueryDefinition( ).getName( ),
				documentManager,
				new StopSign( ) );

		return cube;
	}
	
	/**
	 * Get the document manager.
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
			return DocumentManagerFactory.loadFileDocumentManager( executor.getContext( )
					.getTmpdir( ),
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
	 */
	private AggregationDefinition[] prepareCube( ICube cube, ICubeQueryDefinition query )
	{
		CalculatedMember[] calculatedMember = CubeQueryDefinitionUtil.getCalculatedMembers( query );
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
			String[] levelNamesForFilter = new String[levelsOnColumn.length];
			sortType = new int[levelsOnColumn.length];
			for ( int i = 0; i < levelsOnColumn.length; i++ )
			{
				levelNamesForFilter[i] = levelsOnColumn[i].getName( );
				sortType[i] = getSortDirection( levelsOnColumn[i].getName( ), query );
			}
			aggregations[aggrIndex] = new AggregationDefinition( levelNamesForFilter,
					sortType,
					null );
			aggrIndex++;
		}
		if ( rowEdgeDefn != null )
		{
			String[] levelNamesForFilter = new String[levelsOnRow.length];
			sortType = new int[levelsOnRow.length];
			for ( int i = 0; i < levelsOnRow.length; i++ )
			{
				levelNamesForFilter[i] = levelsOnRow[i].getName( );
				sortType[i] = getSortDirection( levelsOnRow[i].getName( ), query );
			}
			aggregations[aggrIndex] = new AggregationDefinition( levelNamesForFilter,
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
				if ( rsIDSet.contains( calculatedMember[i].getRsID( ) ) )
					continue;
				list = getCalculatedMemberWithSameRSId( calculatedMember, i );
				AggregationFunctionDefinition[] funcitons = new AggregationFunctionDefinition[list.size( )];
				for ( int index = 0; index < list.size( ); index++ )
				{
					funcitons[index] = new AggregationFunctionDefinition( ( (CalculatedMember) list.get( index ) ).getMeasureName( ),
							( (CalculatedMember) list.get( index ) ).getAggrFunction( ) );
				}

				String[] levelNames = new String[calculatedMember[i].getAggrOnList( )
						.size( )];
				sortType = new int[calculatedMember[i].getAggrOnList( ).size( )];
				for ( int index = 0; index < calculatedMember[i].getAggrOnList( )
						.size( ); index++ )
				{
					levelNames[index] = calculatedMember[i].getAggrOnList( )
							.get( index )
							.toString( );
					sortType[index] = getSortDirection( levelNames[index],
							query );
				}

				rsIDSet.add( new Integer( calculatedMember[i].getRsID( ) ) );
				aggregations[aggrIndex] = new AggregationDefinition( levelNames,
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
	 */
	private int getSortDirection( String levelName,
			ICubeQueryDefinition query )
	{
		//TODO add dimension name specification
		if ( query.getSorts( ) != null && !query.getSorts( ).isEmpty( ) )
		{
			for ( int i = 0; i < query.getSorts( ).size( ); i++ )
			{
				ISortDefinition sortDfn = ( (ISortDefinition) query.getSorts( )
						.get( i ) );
				String[] info = OlapExpressionUtil.getTargetLevel( sortDfn.getExpression( )
						.getText( ) );
				if ( info.length == 2 )
				{
					if ( levelName.equals( info[1] ) )
					{
						return sortDfn.getSortDirection( );
					}
				}
			}
		}
		return IDimensionSortDefn.SORT_UNDEFINED;
	}
}
