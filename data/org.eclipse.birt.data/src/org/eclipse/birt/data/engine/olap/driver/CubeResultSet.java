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

import javax.olap.OLAPException;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;
import org.eclipse.birt.data.engine.olap.query.view.BirtCubeView;
import org.eclipse.birt.data.engine.olap.query.view.MeasureNameManager;

public class CubeResultSet implements IResultSet
{

	private EdgeAxis rowEdgeAxis;
	private EdgeAxis columnEdgeAxis;
	private EdgeAxis[] calculatedEdgeAxis;
	private MeasureNameManager manager;
	
	/**
	 * 
	 * @param rsArray
	 * @param view
	 * @throws IOException 
	 * @throws OLAPException
	 */
	public CubeResultSet( IAggregationResultSet[] rsArray, BirtCubeView view, MeasureNameManager manager )
			throws IOException
	{
		int count = 0;
		if ( view.getColumnEdgeView( ) != null )
		{
			this.columnEdgeAxis = new EdgeAxis( rsArray[count],
					view.getColumnEdgeView( ),
					false );
			count++;
		}
		if ( view.getRowEdgeView( ) != null )
		{
			this.rowEdgeAxis = new EdgeAxis( rsArray[count],
					view.getRowEdgeView( ),
					false );
			count++;
		}

		if ( rsArray.length > count )
		{
			calculatedEdgeAxis = new EdgeAxis[rsArray.length - count];
			for ( int i = count; i < rsArray.length; i++ )
			{
				calculatedEdgeAxis[i - count] = new EdgeAxis( rsArray[i],
						view.getMeasureEdgeView( )[i - count],
						true );
			}
		}
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
