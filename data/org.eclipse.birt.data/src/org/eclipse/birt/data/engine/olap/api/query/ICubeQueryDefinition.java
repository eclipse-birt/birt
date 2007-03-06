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
 * 
 */

public interface ICubeQueryDefinition extends INamedObject
{
	public static final int ROW_EDGE = 1;
	public static final int COLUMN_EDGE = 2;
	
	public IEdgeDefinition createEdge( int type );
	public IMeasureDefinition createMeasure( String measureName );
	public List getMeasures();
	public IEdgeDefinition getEdge( int type );
	public void addBinding ( IBinding binding );
	public List getBindings ();
	public void addSort( ISortDefinition sort );
	public void addFilter ( IFilterDefinition filter );
	public List getSorts( );
	public List getFilters();
	
}
