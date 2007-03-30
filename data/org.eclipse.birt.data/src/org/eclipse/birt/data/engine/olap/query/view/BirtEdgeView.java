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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.olap.cursor.EdgeCursor;

import org.eclipse.birt.data.engine.olap.api.query.IDimensionDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDefinition;

/**
 * An BirtEdgeView is part of the logical layout of a BirtCubeView.It aggregates
 * a set of BirtDimensionView, which defines the shape and content of the edge.
 * 
 */
public class BirtEdgeView
{

	private EdgeCursor edgeCursor;
	private BirtCubeView cubeView;
	private List dimensionViewList;
	private String name;
	private final static String CALCULATED_MEMBER ="CALCULATED_MEMBER";

	/**
	 * 
	 * @param cubeView
	 * @param edgeDefn
	 */
	public BirtEdgeView( BirtCubeView cubeView, IEdgeDefinition edgeDefn )
	{
		this.cubeView = cubeView;
		this.dimensionViewList = new ArrayList( );
		populateDimensionView( edgeDefn );
		if ( edgeDefn != null )
			this.name = edgeDefn.getName( );
	}

	/**
	 * 
	 * @param calculatedMember
	 */
	public BirtEdgeView( CalculatedMember calculatedMember )
	{
		this.name = CALCULATED_MEMBER + calculatedMember.getRsID( );
	}

	/**
	 * 
	 * @param edgeDefn
	 */
	private void populateDimensionView( IEdgeDefinition edgeDefn )
	{
		if( edgeDefn== null )
			return;
		Iterator dims = edgeDefn.getDimensions( ).iterator( );
		while ( dims.hasNext( ) )
		{
			IDimensionDefinition defn = (IDimensionDefinition) dims.next( );
			BirtDimensionView view = new BirtDimensionView( defn );
			dimensionViewList.add( view );
		}
	}

	/**
	 * 
	 * @return
	 */
	public EdgeCursor getEdgeCursor( )
	{
		return this.edgeCursor;
	}

	/**
	 * 
	 * @param edgeCursor
	 */
	public void setEdgeCursor( EdgeCursor edgeCursor )
	{
		this.edgeCursor = edgeCursor;
	}

	/**
	 * 
	 * @return
	 */
	public BirtCubeView getOrdinateOwner( )
	{
		return this.cubeView;
	}

	/**
	 * 
	 * @return
	 */
	public List getDimensionViews( )
	{
		return dimensionViewList;
	}

	/**
	 * 
	 * @return
	 */
	public String getName( )
	{
		return this.name;
	}
}
