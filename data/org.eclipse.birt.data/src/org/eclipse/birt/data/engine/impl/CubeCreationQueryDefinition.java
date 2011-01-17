
/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.impl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;


/**
 * 
 */

public class CubeCreationQueryDefinition extends QueryDefinition
{

	private List<IFilterDefinition> dataSetFilter = new ArrayList<IFilterDefinition>( );

	/**
	 * Returns the filters defined in this transform, as an ordered list of
	 * <code>IFilterDefintion</code> objects.
	 * 
	 * @return the filters. null if no filter is defined.
	 */
	public List<IFilterDefinition> getDataSetFilters( )
	{
		return dataSetFilter;
	}

	/**
	 * Add one filter to the filter list
	 */
	public void addDataSetFilter( IFilterDefinition filter )
	{
		dataSetFilter.add( filter );
	}
}
