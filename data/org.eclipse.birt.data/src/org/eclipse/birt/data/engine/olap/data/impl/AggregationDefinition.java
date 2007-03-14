
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


/**
 * Defines a cube aggregation.
 */

public class AggregationDefinition
{
	private String[] levelNames;
	private int[] sortTypes;
	private AggregationFunctionDefinition[] aggregationFunctions;
	
	/**
	 * 
	 * @param levelNames
	 * @param sortTypes
	 * @param aggregationFunctions
	 */
	public AggregationDefinition( String[] levelNames, int[] sortTypes, AggregationFunctionDefinition[] aggregationFunctions )
	{
		this.levelNames = levelNames;
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
	public String[] getLevelNames( )
	{
		return levelNames;
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
