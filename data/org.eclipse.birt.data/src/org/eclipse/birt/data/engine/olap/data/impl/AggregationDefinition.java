
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
package org.eclipse.birt.data.engine.olap.data.impl;

import org.eclipse.birt.data.engine.olap.data.api.DimLevel;


/**
 * Defines a cube aggregation.
 */

public class AggregationDefinition
{
	private DimLevel[] levels;
	private int[] sortTypes;
	private AggregationFunctionDefinition[] aggregationFunctions;
	
	/**
	 * 
	 * @param levelNames
	 * @param sortTypes
	 * @param aggregationFunctions
	 */
	public AggregationDefinition( DimLevel[] levels, int[] sortTypes, AggregationFunctionDefinition[] aggregationFunctions )
	{
		this.levels = levels;
		this.aggregationFunctions = aggregationFunctions;
		this.sortTypes = sortTypes;
	}

	/**
	 * 
	 * @return
	 */
	public AggregationFunctionDefinition[] getAggregationFunctions( )
	{
		return aggregationFunctions;
	}

	/**
	 * 
	 * @return
	 */
	public DimLevel[] getLevels( )
	{
		return levels;
	}

	/**
	 * 
	 * @return
	 */
	public int[] getSortTypes( )
	{
		return sortTypes;
	}

	
}
