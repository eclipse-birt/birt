
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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
package org.eclipse.birt.data.engine.olap.data.impl;

import java.util.logging.Logger;

import org.eclipse.birt.data.engine.api.timefunction.ITimeFunction;
import org.eclipse.birt.data.engine.olap.data.api.DimLevel;
import org.eclipse.birt.data.engine.olap.util.filter.IJSFacttableFilterEvalHelper;

/**
 * Defines a function which is used in cube aggregation.
 */

public class AggregationFunctionDefinition {
	private String name;
	private String measureName;
	private String paraColName;
	private String functionName;
	private DimLevel paraLevel;
	private IJSFacttableFilterEvalHelper filterEvalHelper;
	private Object paraValue;
	private ITimeFunction timeFunction;
	private ITimeFunction timeFunctionFilter;

	public ITimeFunction getTimeFunctionFilter() {
		return timeFunctionFilter;
	}

	public void setTimeFunctionFilter(ITimeFunction timeFunctionFilter) {
		this.timeFunctionFilter = timeFunctionFilter;
	}

	public Object getParaValue() {
		return paraValue;
	}

	public void setParaValue(Object paraValue) {
		this.paraValue = paraValue;
	}

	private static Logger logger = Logger.getLogger(AggregationFunctionDefinition.class.getName());

	/**
	 * 
	 * @param name
	 * @param measureName
	 * @param functionName
	 */
	public AggregationFunctionDefinition(String name, String measureName, String functionName) {
		this(name, measureName, null, null, functionName, null);
	}

	public ITimeFunction getTimeFunction() {
		return timeFunction;
	}

	public void setTimeFunction(ITimeFunction timeFunction) {
		this.timeFunction = timeFunction;
	}

	/**
	 * 
	 * @param name
	 * @param measureName
	 * @param paraLevel
	 * @param paraColName
	 * @param functionName
	 */
	public AggregationFunctionDefinition(String name, String measureName, DimLevel paraLevel, String paraColName,
			String functionName) {
		this(name, measureName, paraLevel, paraColName, functionName, null);
	}

	/**
	 * 
	 * @param name
	 * @param measureName
	 * @param paraColNames
	 * @param functionName
	 */
	public AggregationFunctionDefinition(String name, String measureName, DimLevel paraLevel, String paraColName,
			String functionName, IJSFacttableFilterEvalHelper filterEvalHelper) {
		Object[] params = { name, measureName, functionName };
		logger.entering(AggregationFunctionDefinition.class.getName(), "AggregationFunctionDefinition", params);
		this.name = name;
		this.paraLevel = paraLevel;
		this.paraColName = paraColName;
		this.measureName = measureName;
		this.functionName = functionName;
		this.filterEvalHelper = filterEvalHelper;
		logger.exiting(AggregationFunctionDefinition.class.getName(), "AggregationFunctionDefinition");
	}

	/**
	 * 
	 * @param measurename
	 * @param functionName
	 */
	public AggregationFunctionDefinition(String measureName, String functionName) {
		this(null, measureName, null, null, functionName, null);
	}

	/**
	 * @return the functionName
	 */
	public String getFunctionName() {
		return functionName;
	}

	/**
	 * @return the measureName
	 */
	public String getMeasureName() {
		return measureName;
	}

	/**
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @return
	 */
	public DimColumn getParaCol() {
		if (paraLevel == null)
			return null;
		return new DimColumn(paraLevel.getDimensionName(), paraLevel.getLevelName(), paraColName);
	}

	/**
	 * 
	 * @return
	 */
	public IJSFacttableFilterEvalHelper getFilterEvalHelper() {
		return filterEvalHelper;
	}
}
