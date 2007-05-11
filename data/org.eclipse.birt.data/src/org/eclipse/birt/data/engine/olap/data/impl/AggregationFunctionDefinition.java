
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

import java.util.logging.Logger;

/**
 * Defines a function which is used in cube aggregation.
 */

public class AggregationFunctionDefinition
{
	private String name;
	private String measureName;
	private String functionName;

	private static Logger logger = Logger.getLogger( AggregationFunctionDefinition.class.getName( ) );

	/**
	 * 
	 * @param name
	 * @param measurename
	 * @param functionName
	 */
	public AggregationFunctionDefinition( String name, String measurename,
			String functionName )
	{
		Object[] params = {
				name, measurename, functionName
		};
		logger.entering( AggregationFunctionDefinition.class.getName( ),
				"AggregationFunctionDefinition",
				params );
		this.name = name;
		this.measureName = measurename;
		this.functionName = functionName;
		logger.exiting( AggregationFunctionDefinition.class.getName( ),
				"AggregationFunctionDefinition" );
	}
	
	/**
	 * 
	 * @param measurename
	 * @param functionName
	 */
	public AggregationFunctionDefinition( String measurename,
			String functionName )
	{
		Object[] params = {
				measurename, functionName
		};
		logger.entering( AggregationFunctionDefinition.class.getName( ),
				"AggregationFunctionDefinition",
				params );
		this.measureName = measurename;
		this.functionName = functionName;
		logger.exiting( AggregationFunctionDefinition.class.getName( ),
				"AggregationFunctionDefinition" );
	}

	/**
	 * @return the functionName
	 */
	public String getFunctionName( )
	{
		return functionName;
	}

	/**
	 * @return the measureName
	 */
	public String getMeasureName( )
	{
		return measureName;
	}

	public String getName( )
	{
		return name;
	}
}
