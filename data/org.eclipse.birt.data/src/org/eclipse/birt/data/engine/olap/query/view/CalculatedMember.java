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

import java.util.List;

/**
 * A CalculatedMember is an Aggregation Object which need to be calculated in
 * olap.data
 * 
 */
class CalculatedMember
{

	private String aggrFunction, onMeasureName, name;
	private List aggrOnList;
	private int rsID;

	/**
	 * 
	 * @param onMeasureName
	 * @param aggrOnList
	 * @param aggrFunction
	 * @param isMeasure
	 */
	CalculatedMember( String name, String onMeasureName, List aggrOnList,
			String aggrFunction, int rsID )
	{
		this.name = name;
		this.onMeasureName = onMeasureName;
		this.aggrOnList = aggrOnList;
		this.aggrFunction = aggrFunction;
		this.rsID = rsID;
	}

	/**
	 * 
	 * @return
	 */
	String getAggrFunction( )
	{
		return this.aggrFunction;
	}

	/**
	 * 
	 * @return
	 */
	String getMeasureName( )
	{
		return onMeasureName;
	}

	/**
	 * 
	 * @return
	 */
	List getAggrOnList( )
	{
		return this.aggrOnList;
	}

	/**
	 * 
	 * @return
	 */
	String getName( )
	{
		return this.name;
	}
	
	/**
	 * 
	 * @return
	 */
	int getRsID( )
	{
		return this.rsID;
	}
}
