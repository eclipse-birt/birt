/*
 *************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */ 
package org.eclipse.birt.data.engine.api;

import java.util.Collection;

/**
 * Defines a "report query": a set of data transforms that provides data for
 * a list-like element in the report. The report query encapsulates three types of information:<br>
 * 
 * 1. A data set, including computed columns together with the parameter bindings. <br>
 * 2. Data transforms that are defined on report items, i.e., sorting, filtering, 
 * grouping, aggregation functions, and so on. <br>
 * 3. Subqueries that are contained in the current report query.<br> 
 *
 */
public interface IQueryDefinition extends IBaseQueryDefinition
{
	/**
	 * Gets the name of the data set used by this report query
	 */
	public String getDataSetName( );
		
	/**
	 * Returns the set of input parameter bindings as an unordered collection
	 * of IInputParamBinding objects.
	 * 
	 * @return the input parameter bindings. If no binding is defined, null is returned.
	 */
	public Collection getInputParamBindings( );
		
	/**
	 * Provides a column projection hint to the data engine. The caller informs the data engine that only
	 * a selected list of columns defined by the data set are used by this report query. The names of 
	 * those columns (the "projected columns") are passed in as an array of string. <br>
	 * If a column projection is set, runtime error may occur if the report query uses columns 
	 * that are not defined in the projected column list. 
	 */
	public String[] getColumnProjection();
}
