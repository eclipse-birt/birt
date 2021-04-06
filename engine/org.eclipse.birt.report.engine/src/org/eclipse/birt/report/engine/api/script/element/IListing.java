/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.api.script.element;

import org.eclipse.birt.report.engine.api.script.ScriptException;

/**
 * Represents the design of an Listing in the scripting environment
 * 
 */
public interface IListing extends IReportItem {

	/**
	 * Returns all filter conditions
	 * 
	 * @return all filter conditions
	 */

	IFilterCondition[] getFilterConditions();

	/**
	 * Adds filter condition.expr of IFilterCondition is required.
	 * 
	 * @param condition
	 * @throws ScriptException
	 */

	void addFilterCondition(IFilterCondition condition) throws ScriptException;

	/**
	 * Removes filter condition.
	 * 
	 * @throws ScriptException
	 */

	void removeFilterConditions() throws ScriptException;

	/**
	 * Removes filter condition.
	 * 
	 * @param condition
	 * @throws ScriptException
	 */

	void removeFilterCondition(IFilterCondition condition) throws ScriptException;

	/**
	 * Returns all sort conditions.
	 * 
	 * @return all sort conditions.
	 */

	ISortCondition[] getSortConditions();

	/**
	 * Adds sort condition.key of ISortCondition is required.
	 * 
	 * @param condition
	 * 
	 * @throws ScriptException
	 */

	void addSortCondition(ISortCondition condition) throws ScriptException;

	/**
	 * Removes all sort conditions
	 * 
	 * @throws ScriptException
	 */

	void removeSortConditions() throws ScriptException;

	/**
	 * Removes sort condition.
	 * 
	 * @param condition
	 * @throws ScriptException
	 */

	void removeSortCondition(ISortCondition condition) throws ScriptException;
}
