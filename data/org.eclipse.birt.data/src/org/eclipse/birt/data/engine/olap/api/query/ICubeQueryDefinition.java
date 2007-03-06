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

package org.eclipse.birt.data.engine.olap.api.query;

import java.util.List;

import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.ISortDefinition;

/**
 * ICubeQueryDefinition is the entry point of a cube query. It defines the
 * edges, bindings, etc. for the cube query.
 */

public interface ICubeQueryDefinition extends INamedObject
{

	// The row edge type
	public static final int ROW_EDGE = 1;

	// The column edge type
	public static final int COLUMN_EDGE = 2;

	/**
	 * Create an edge of the cube. At initial stage we only support two types of
	 * edges: row edge and column edge. The edge created will automatically be
	 * linked to the cube.
	 * 
	 * @param type
	 * @return
	 */
	public IEdgeDefinition createEdge( int type );

	/**
	 * Create a measure which is used in the query. A measure is a specific
	 * dimension. The measure created by this method will be automatically
	 * lindked to the cube.
	 * 
	 * @param measureName
	 * @return
	 */
	public IMeasureDefinition createMeasure( String measureName );

	/**
	 * Return the list of measures defined.
	 * 
	 * @return
	 */
	public List getMeasures( );

	/**
	 * Get the specific EdgeDefn, for each type of Edge there is only one Edge
	 * instance.
	 * 
	 * @param type
	 * @return
	 */
	public IEdgeDefinition getEdge( int type );

	/**
	 * Add bindings to the query definition.
	 * 
	 * @param binding
	 */
	public void addBinding( IBinding binding );

	/**
	 * Return the list of bindings defined in the cube query.
	 * 
	 * @return
	 */
	public List getBindings( );

	/**
	 * Add the sort. Currently we only support sorts which are based on one
	 * single level.
	 * 
	 * @param sort
	 */
	public void addSort( ISortDefinition sort );

	/**
	 * Add the filter. Currently we only support filters which are based on one
	 * single level. The multiple filters defined in cube will have an “AND”
	 * relationship.
	 * 
	 * @param filter
	 */
	public void addFilter( IFilterDefinition filter );

	/**
	 * Return the sorts defined
	 * 
	 * @return
	 */
	public List getSorts( );

	/**
	 * Return the filters defined
	 * 
	 * @return
	 */
	public List getFilters( );

}
