
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
 * Defines a function which is used in cube aggregation.
 */

public class AggregationFunctionDefinition
{
	private String measureName;
	private String functionName;
	
	/**
	 * 
	 * @param measurename
	 * @param functionName
	 */
	public AggregationFunctionDefinition( String	measurename, String functionName )
	{
		this.measureName = measurename;
		this.functionName = functionName;
	}
	
	/**
	 * @return the functionName
	 */
	public String getFunctionName()
	{
		return functionName;
	}

	/**
	 * @return the measureName
	 */
	public String getMeasureName()
	{
		return measureName;
	}
}
