/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.data;

import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.report.engine.ir.Report;

/**
 * Defines a set of data-related functions that engine needs from a data engine 
 * 
 * @version $Revision: 1.7 $ $Date: 2005/05/08 06:08:26 $
 */
public interface IDataEngine
{
	/**
	 * Prepare all the information that data engine needs to successfully obtain data used in the report.<p>
	 * The information includes
	 * <ul>
	 * <li>all data sources.
	 * <li>all datasets.
	 * <li>all report query definitons (including sub-query definitions)
	 * </ul> 
	 * <p> This method needs to prepare all report queries, Verifies the elements of a report 
	 * query spec and provides a hint to the query to prepare and optimize an execution plan.
	 * <p>
	 * @param report the report design
	 */
	void prepare(Report report);
	
	/**
	 * Executes the prepared (data) execution plan of a report item.  Returns
     * an IResultSet object 
     * <p>
	 * @param the query to be executed
	 * @return IResultSet object or null if the query is null
	 */
	IResultSet execute(IBaseQueryDefinition query);
	
	/**
	 * close the IResultSet of the last executing operator
	 */
	void close();
	
	/**
	 * shut down the data engine
	 */
	void shutdown();
	
	/**
	 * evaluate an expression by data engine. Data engine may inturn calls Rhino
	 * @param expr the data engine expression wrapped in <code>IBaseExpression</code>
	 * @return the evaluated result for the data engine expression
	 */
	Object evaluate(IBaseExpression expr);
	
	/**
	 * return the DTE's data engine.
	 * @return retuan a dataEngine of DTE.
	 */
	DataEngine getDataEngine();
}
