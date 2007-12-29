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

package org.eclipse.birt.data.engine.olap.driver;

import java.io.IOException;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.data.api.CubeQueryExecutorHelper;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;
import org.eclipse.birt.data.engine.olap.query.view.BirtCubeView;
import org.eclipse.birt.data.engine.olap.query.view.MeasureNameManager;

public class CubeResultSet implements IResultSet
{

	private EdgeAxis rowEdgeAxis;
	private EdgeAxis columnEdgeAxis;
	private EdgeAxis[] calculatedEdgeAxis;
	private MeasureNameManager manager;
	
	private BirtCubeView cubeView;
	private IAggregationResultSet[] rsArray;
	private CubeQueryExecutorHelper cubeQueryExecutorHelper;
	
	/**
	 * 
	 * @param rsArray
	 * @param view
	 * @throws IOException 
	 */
	public CubeResultSet( IAggregationResultSet[] rsArray, BirtCubeView view,
			MeasureNameManager manager,
			CubeQueryExecutorHelper cubeQueryExecutorHelper ) throws IOException
	{
		this.cubeView = view;
		this.rsArray = rsArray;
		this.cubeQueryExecutorHelper = cubeQueryExecutorHelper;
		this.manager = manager;
		populateEdge( );
	}
	
	/**
	 * 
	 * @param resultSet
	 * @param view
	 * @param startingColumnIndex
	 * @param startingRowIndex
	 * @throws IOException
	 */
	public CubeResultSet( IResultSet resultSet, BirtCubeView view,
			CubeQueryExecutorHelper cubeQueryExcutorHelper,
			int startingColumnIndex, int startingRowIndex ) throws IOException
	{
		this.cubeView = view;
		populateEdgeOnSubQuery( resultSet,
				cubeQueryExcutorHelper,
				startingColumnIndex,
				startingRowIndex );
	}
	
	/**
	 * 
	 * @throws IOException
	 */
	private void populateEdge( ) throws IOException
	{
		int count = 0;
		if ( cubeView.getColumnEdgeView( ) != null )
		{
			this.columnEdgeAxis = new EdgeAxis( rsArray[count],
					cubeView.getColumnEdgeView( ),
					cubeQueryExecutorHelper.getColumnSort( ),
					false );
			count++;
		}
		if ( cubeView.getRowEdgeView( ) != null )
		{
			this.rowEdgeAxis = new EdgeAxis( rsArray[count],
					cubeView.getRowEdgeView( ),
					cubeQueryExecutorHelper.getRowSort( ),
					false );
			count++;
		}

		if ( rsArray.length > count )
		{
			calculatedEdgeAxis = new EdgeAxis[rsArray.length - count];
			for ( int i = count; i < rsArray.length; i++ )
			{
				calculatedEdgeAxis[i - count] = new EdgeAxis( rsArray[i],
						cubeView.getMeasureEdgeView( )[i - count],
						true );
			}
		}
	}
	
	private void populateEdgeOnSubQuery( IResultSet parentResult,
			CubeQueryExecutorHelper cubeQueryExcutorHelper,
			int startingColumnIndex, int startingRowIndex ) throws IOException
	{
		int count = 0;
		this.cubeQueryExecutorHelper = cubeQueryExcutorHelper;
		if ( cubeView.getColumnEdgeView( ) != null )
		{
			this.columnEdgeAxis = new EdgeAxis( parentResult.getColumnEdgeResult( )
					.getRowDataAccessor( ),
					cubeView.getColumnEdgeView( ),
					cubeQueryExcutorHelper.getColumnSort( ),
					false,
					startingColumnIndex );
			count++;
		}
		if ( cubeView.getRowEdgeView( ) != null )
		{
			this.rowEdgeAxis = new EdgeAxis( parentResult.getRowEdgeResult( )
					.getRowDataAccessor( ),
					cubeView.getRowEdgeView( ),
					cubeQueryExcutorHelper.getRowSort( ),
					false,
					startingRowIndex );
			count++;
		}
		this.calculatedEdgeAxis = parentResult.getMeasureResult( );
	}

	/*
	 * @see org.eclipse.birt.data.jolap.driver.IResultSet#getColumnEdgeResult()
	 */
	public EdgeAxis getColumnEdgeResult( )
	{
		return this.columnEdgeAxis;
	}

	/*
	 * @see org.eclipse.birt.data.jolap.driver.IResultSet#getRowEdgeResult()
	 */
	public EdgeAxis getRowEdgeResult( )
	{
		return this.rowEdgeAxis;
	}

	/*
	 * @see org.eclipse.birt.data.jolap.driver.IResultSet#getMeasureResult()
	 */
	public EdgeAxis[] getMeasureResult( )
	{
		return this.calculatedEdgeAxis;
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.driver.IResultSet#getMeasureResult(java.lang.String)
	 */
	public EdgeAxis getMeasureResult( String name ) throws DataException
	{
		int index = manager.getAggregationResultID( name );
		return this.calculatedEdgeAxis[index];
	}
}
