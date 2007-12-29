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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.olap.OLAPException;
import javax.olap.cursor.CubeCursor;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.StopSign;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDefinition;
import org.eclipse.birt.data.engine.olap.cursor.CubeCursorImpl;
import org.eclipse.birt.data.engine.olap.driver.IResultSet;
import org.eclipse.birt.data.engine.olap.impl.query.CubeQueryExecutor;

/**
 * A <code>BirtCubeView</code> represents a multi-dimensional selection of
 * values. A view contains a collection of <code>BirtEdgeView</code> that
 * group dimensions into a logical layout. This view has three types of
 * edges:rowEdgeView, coulumnEdgeView, measureEdgeView. A BirtCubeView has
 * association with a CubeCursor. This association will provide a user a way to
 * get data for the current intersection of the multi-dimensional selection.
 * 
 */
public class BirtCubeView
{

	private ICubeQueryDefinition queryDefn;
	private BirtEdgeView columnEdgeView, rowEdgeView;
	private BirtEdgeView calculatedMemberView[];
	private MeasureNameManager manager;
	private CubeQueryExecutor executor;
	private Map appContext;
	private Map measureMapping;
	private QueryExecutor queryExecutor;
	private IResultSet parentResultSet;

	/**
	 * Constructor: construct the row/column/measure EdgeView.
	 * 
	 * @param queryExecutor
	 * @throws DataException 
	 */
	public BirtCubeView( CubeQueryExecutor queryExecutor, Map appContext ) throws DataException
	{
		this.queryDefn = queryExecutor.getCubeQueryDefinition( );
		this.executor = queryExecutor;
		columnEdgeView = createBirtEdgeView( this.queryDefn.getEdge( ICubeQueryDefinition.COLUMN_EDGE ) );
		rowEdgeView = createBirtEdgeView( this.queryDefn.getEdge( ICubeQueryDefinition.ROW_EDGE ) );
		this.executor = queryExecutor;
		this.appContext = appContext;
		measureMapping = new HashMap( );
		CalculatedMember[] members = CubeQueryDefinitionUtil.getCalculatedMembers( queryDefn,
				queryExecutor.getSession( ).getSharedScope( ),
				measureMapping );
		if ( members != null && members.length > 0 )
		{
			Set rsIDSet = new HashSet( );
			calculatedMemberView = new BirtEdgeView[members.length];
			int index =0;
			for ( int i = 0; i < members.length; i++ )
			{
				if ( rsIDSet.contains( new Integer( members[i].getRsID( ) ) ) )
					continue;
				calculatedMemberView[index] = this.createBirtEdgeView( members[i] );
				rsIDSet.add( new Integer( members[i].getRsID( ) ) );
				index++;
			}
		}
		manager = new MeasureNameManager( members );
	}
	
	/**
	 * Constructor: construct the row/column/measure EdgeView.
	 * 
	 * @param queryExecutor
	 * @throws DataException 
	 */
	public BirtCubeView( CubeQueryExecutor queryExecutor ) throws DataException
	{
		this( queryExecutor, null );
	}

	/**
	 * Get cubeCursor for current cubeView.
	 * @param stopSign
	 * @return CubeCursor
	 * @throws OLAPException
	 * @throws DataException 
	 */
	public CubeCursor getCubeCursor( StopSign stopSign ) throws OLAPException, DataException
	{
		Map relationMap = CubeQueryDefinitionUtil.getRelationWithMeasure( queryDefn,
				measureMapping );
		queryExecutor = new QueryExecutor( );
		try
		{
			parentResultSet = queryExecutor.execute( this, executor, manager, stopSign );
		}
		catch ( IOException e )
		{
			throw new OLAPException( e.getLocalizedMessage( ) );
		}
		catch ( BirtException e )
		{
			throw new OLAPException( e.getLocalizedMessage( ) );
		}
		CubeCursor cubeCursor;
		if ( this.appContext != null &&
				this.executor.getContext( ).getMode( ) == DataEngineContext.DIRECT_PRESENTATION )
		{
			cubeCursor = new CubeCursorImpl( this,
					parentResultSet,
					relationMap,
					manager,
					appContext );
		}
		else
		{
			cubeCursor = new CubeCursorImpl( this, parentResultSet, relationMap, manager );
		}
		return cubeCursor;
	}
	
	/**
	 * Get cubeCursor for current cubeView.
	 * 
	 * @param stopSign
	 * @return CubeCursor
	 * @throws OLAPException
	 * @throws DataException
	 */
	public CubeCursor getCubeCursor( StopSign stopSign,
			String startingColumnLevel, String startingRowLevel,
			String startingPageLevel, BirtCubeView parentView ) throws OLAPException, DataException
	{
		if ( parentView == null || parentView.getCubeQueryExecutor( ) == null )
		{
			throw new DataException( ResourceConstants.NO_PARENT_RESULT_CURSOR );
		}
		Map relationMap = CubeQueryDefinitionUtil.getRelationWithMeasure( queryDefn,
				measureMapping );

		int startingColumnLevelIndex = -1, startingRowLevelIndex = -1;
		if ( startingColumnLevel != null )
			startingColumnLevelIndex = CubeQueryDefinitionUtil.getLevelIndex( this.queryDefn,
					startingColumnLevel,
					ICubeQueryDefinition.COLUMN_EDGE );
		if ( startingRowLevel != null )
			startingRowLevelIndex = CubeQueryDefinitionUtil.getLevelIndex( this.queryDefn,
					startingRowLevel,
					ICubeQueryDefinition.ROW_EDGE );
		if ( startingColumnLevelIndex == -1 && startingRowLevelIndex == -1 )
		{
			startingColumnLevelIndex = CubeQueryDefinitionUtil.getLevelsOnEdge( this.queryDefn.getEdge( ICubeQueryDefinition.COLUMN_EDGE ) ).length - 1;
			startingRowLevelIndex = CubeQueryDefinitionUtil.getLevelsOnEdge( this.queryDefn.getEdge( ICubeQueryDefinition.ROW_EDGE ) ).length - 1;
		}
		this.queryExecutor = parentView.getQueryExecutor( );
		try
		{
			parentResultSet = queryExecutor.executeSubQuery( parentView.getResultSet( ),
					this,
					startingColumnLevelIndex,
					startingRowLevelIndex );
		}
		catch ( IOException e )
		{
			throw new OLAPException( e.getLocalizedMessage( ) );
		}
		CubeCursor cubeCursor = new CubeCursorImpl( this,
				parentResultSet,
				relationMap,
				manager );
		return cubeCursor;
	}
	
	/**
	 * 
	 * @return
	 */
	public QueryExecutor getQueryExecutor( )
	{
		return this.queryExecutor;
	}
	
	/**
	 * 
	 * @return
	 */
	public CubeQueryExecutor getCubeQueryExecutor( )
	{
		return this.executor;
	}
	
	/**
	 * 
	 * @return
	 */
	public IResultSet getResultSet( )
	{
		return this.parentResultSet;
	}

	/**
	 * @return
	 */
	public BirtEdgeView getRowEdgeView( )
	{
		return this.rowEdgeView;
	}

	/**
	 * @return
	 */
	public BirtEdgeView getColumnEdgeView( )
	{
		return this.columnEdgeView;
	}

	/**
	 * 
	 * @return
	 */
	public BirtEdgeView[] getMeasureEdgeView( )
	{
		return this.calculatedMemberView;
	}

	/**
	 * 
	 * @return
	 */
	public Map getMeasureMapping( )
	{
		return measureMapping;
	}

	/**
	 * 
	 * @param edgeDefn
	 * @return
	 */
	private BirtEdgeView createBirtEdgeView( IEdgeDefinition edgeDefn )
	{
		if ( edgeDefn == null )
			return null;
		return new BirtEdgeView( this, edgeDefn );
	}

	/**
	 * 
	 * @param calculatedMember
	 * @return
	 */
	private BirtEdgeView createBirtEdgeView( CalculatedMember calculatedMember )
	{
		return new BirtEdgeView( calculatedMember );
	}
}
