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

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.DataEngineContext;
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
		AggregationDefinition[] aggrDefns = prepareCube( cube, executor.getCubeQueryDefinition( ) );
		CubeQueryExcutorHelper cubeQueryExcutorHelper = new CubeQueryExcutorHelper( cube );
		IAggregationResultSet[] rs = cubeQueryExcutorHelper.excute( aggrDefns,
				new StopSign( ) );
		return new CubeResultSet( rs, view , manager );
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
		
		AggregationDefinition[] aggregations;
		if ( columnEdgeDefn == null && rowEdgeDefn == null )
			aggregations = new AggregationDefinition[calculatedMember.length];
		else if ( columnEdgeDefn == null || rowEdgeDefn == null )
			aggregations = new AggregationDefinition[calculatedMember.length + 1];
		else
			aggregations = new AggregationDefinition[calculatedMember.length + 2];			
	
		int aggrIndex = 0;
			
		// TODO put level sortDefinition
		int[] sortType = new int[1];
		sortType[0] = IDimensionSortDefn.SORT_ASC;
		if ( columnEdgeDefn != null )
		{
			String[] levelNamesForFilter = new String[levelsOnColumn.length];
			for ( int i = 0; i < levelsOnColumn.length; i++ )
			{
				levelNamesForFilter[i] = levelsOnColumn[i].getName( );
			}
			aggregations[aggrIndex] = new AggregationDefinition( levelNamesForFilter,
					sortType,
					null );
			aggrIndex++;
		}
		if ( rowEdgeDefn != null )
		{
			String[] levelNamesForFilter = new String[levelsOnRow.length];
			for ( int i = 0; i < levelsOnRow.length; i++ )
			{
				levelNamesForFilter[i] = levelsOnRow[i].getName( );
			}
			aggregations[aggrIndex] = new AggregationDefinition( levelNamesForFilter,
					sortType,
					null );
			aggrIndex++;
		}
		
		if ( calculatedMember != null && calculatedMember.length > 0 )
		{
			for ( int i = 0; i < calculatedMember.length; i++ )
			{
				AggregationFunctionDefinition[] funcitons = new AggregationFunctionDefinition[1];
				funcitons[0] = new AggregationFunctionDefinition( calculatedMember[i].getMeasureName( ),
						calculatedMember[i].getAggrFunction( ) );

				String[] levelNamesForFilter = new String[calculatedMember[i].getAggrOnList( )
						.size( )];
				for ( int index = 0; index < calculatedMember[i].getAggrOnList( )
						.size( ); index++ )
				{
					levelNamesForFilter[index] = (String) calculatedMember[i].getAggrOnList( )
							.get( index );
				}
				aggregations[aggrIndex] = new AggregationDefinition( levelNamesForFilter,
						sortType,
						funcitons );
				aggrIndex++;
			}
		}
		
		return aggregations;
	}
}
