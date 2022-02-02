
/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.olap.util;

import java.util.List;

import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.timefunction.ITimeFunction;

/**
 * This is a class used to describe a measure that need to be calculated during
 * Olap query execution.
 */
public abstract class CubeAggrDefn {
	//
	private String name;
	private List aggrLevels, arguments;
	private String aggrName;
	private IBaseExpression filterExpression;
	private ITimeFunction timeFunction;

	/*
	 * 
	 */
	CubeAggrDefn(String name, List aggrLevels, String aggrName, ITimeFunction timeFunction, List arguments,
			IBaseExpression filterExpression) {
		assert name != null;
		assert aggrLevels != null;

		this.name = name;
		this.aggrLevels = aggrLevels;
		this.aggrName = aggrName;
		this.arguments = arguments;
		this.filterExpression = filterExpression;
		this.timeFunction = timeFunction;
	}

	/**
	 * Return a list of levels that the aggregations is based.
	 * 
	 * @return
	 */
	public List getAggrLevelsInAggregationResult() {
		return this.aggrLevels;
	}

	public List getAggrLevelsInDefinition() {
		return this.aggrLevels;
	}

	/**
	 * Return a list of arguments that the aggregations is based.
	 * 
	 * @return
	 */
	public List getArguments() {
		return this.arguments;
	}

	/**
	 * Return the name of the cube aggregation definition. Usually it is a binding
	 * name.
	 * 
	 * @return
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Return the name of the aggregation operation.
	 * 
	 * @return
	 */
	public String getAggrName() {
		return this.aggrName;
	}

	/**
	 * Return FilterDefinition in aggregation definition
	 * 
	 * @return
	 */
	public IBaseExpression getFilter() {
		return this.filterExpression;
	}

	public String[] getFirstArgumentInfo() {
		if (this.arguments == null || this.arguments.isEmpty()) {
			return new String[0];
		} else
			return (String[]) this.arguments.get(0);
	}

	/**
	 * 
	 * @return the target measure of IDataSet4Aggregation where this aggregation
	 *         operates
	 */
	public abstract String getMeasure();

	/**
	 * 
	 * @return
	 */
	public ITimeFunction getTimeFunction() {
		return this.timeFunction;
	}
}
