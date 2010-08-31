/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.olap.data.impl;

public class DrilledAggregationDefinition extends AggregationDefinition
{

	private DrilledAggregation aggregation;

	public DrilledAggregationDefinition( DrilledAggregation aggregation, int[] sortTypes,
			AggregationFunctionDefinition[] aggregationFunctions )
	{
		super( aggregation.getTargetLevels( ), sortTypes, aggregationFunctions );
		this.aggregation = aggregation;
	}
	
	public boolean useByAggregation( AggregationDefinition aggr )
	{
		return this.aggregation.usedByAggregation( aggr );
	}

}
