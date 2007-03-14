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
package org.eclipse.birt.data.engine.olap.cursor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.olap.OLAPException;
import javax.olap.cursor.CubeCursor;

import org.eclipse.birt.data.engine.olap.driver.EdgeAxis;
import org.eclipse.birt.data.engine.olap.driver.IResultSet;
import org.eclipse.birt.data.engine.olap.query.view.BirtCubeView;
import org.eclipse.birt.data.engine.olap.query.view.BirtEdgeView;
import org.eclipse.birt.data.engine.olap.query.view.MeasureNameManager;

/**
 * The CubeCursor provide the user with a method of organizing EdgeCursor to
 * navigate the cube. And it provide the data accessor method to get the value
 * of measures.
 * 
 */
public class CubeCursorImpl extends AbstractCursorSupport implements CubeCursor
{

	private List ordinateEdge = new ArrayList( );
	private List pageEdge = new ArrayList( );

	/**
	 * 
	 * @param cubeView
	 * @param result
	 * @param relationMap
	 * @param manager
	 * @throws OLAPException
	 */
	public CubeCursorImpl( BirtCubeView cubeView, IResultSet result,
			Map relationMap, MeasureNameManager manager ) throws OLAPException
	{
		super( null, new AggregationAccessor( cubeView,
				result,
				relationMap,
				manager ) );

		if ( result == null )
			return;

		EdgeAxis[] calculatedMemberAxis = result.getMeasureResult( );
		BirtEdgeView[] calculatedMemberView = cubeView.getMeasureEdgeView( );

		if ( calculatedMemberAxis != null )
		{
			for ( int i = 0; i < calculatedMemberAxis.length; i++ )
			{
				pageEdge.add( new EdgeCursorImpl( calculatedMemberView[i],
						true,
						calculatedMemberAxis[i],
						this ) );
			}
		}
		if ( cubeView.getColumnEdgeView( ) != null )
			ordinateEdge.add( new EdgeCursorImpl( cubeView.getColumnEdgeView( ),
					false,
					result.getColumnEdgeResult( ),
					this ) );
		// create row edge cursor
		if ( cubeView.getRowEdgeView( ) != null )
		{
			ordinateEdge.add( new EdgeCursorImpl( cubeView.getRowEdgeView( ),
					false,
					result.getRowEdgeResult( ),
					this ) );

		}
	}

	/*
	 * @see javax.olap.cursor.CubeCursor#getOrdinateEdge()
	 */
	public List getOrdinateEdge( ) throws OLAPException
	{
		return this.ordinateEdge;
	}

	/*
	 * @see javax.olap.cursor.CubeCursor#getPageEdge()
	 */
	public Collection getPageEdge( ) throws OLAPException
	{
		return this.pageEdge;
	}

	/*
	 * @see javax.olap.cursor.CubeCursor#synchronizePages()
	 */
	public void synchronizePages( ) throws OLAPException
	{
		//no-op
	}
	
}
