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
 * Script wrapper of ReportDesignHandle
 * 
 */

public interface IReportDesign extends IDesignElement {

	/**
	 * method to get data set design by name
	 * 
	 * @param name
	 * @return data set
	 */
	IDataSet getDataSet(String name);

	/**
	 * method to get data source design by name
	 * 
	 * @param name
	 * @return data source
	 */
	IDataSource getDataSource(String name);

	/**
	 * generic method to get report item by name
	 * 
	 * @param name
	 * @return report element
	 */
	IReportElement getReportElement(String name);

	/**
	 * Generic method to get report item by id.
	 * 
	 * @param id
	 * @return report element
	 */
	IReportElement getReportElementByID(long id);

	/**
	 * method to get a label item by name
	 * 
	 * @param name
	 * @return label
	 */

	ILabel getLabel(String name);

	/**
	 * method to get a master page by name
	 * 
	 * @param name
	 * @return master page
	 */

	IMasterPage getMasterPage(String name);

	/**
	 * method to get a grid item by name
	 * 
	 * @param name
	 * @return grid
	 */
	IGrid getGrid(String name);

	/**
	 * mathod to get a Image item by name
	 * 
	 * @param name
	 * @return iamge
	 */
	IImage getImage(String name);

	/**
	 * method to get a list item by name
	 * 
	 * @param name
	 * @return list
	 */
	IList getList(String name);

	/**
	 * method to get a table item by name
	 * 
	 * @param name
	 * @return table
	 */
	ITable getTable(String name);

	/**
	 * method to get a dynamic text data item by name.
	 * 
	 * @param name
	 * @return text data
	 */

	IDynamicText getDynamicText(String name);

	/**
	 * Sets the resource key of the display name.
	 * 
	 * @param displayNameKey the resource key of the display name
	 * @throws ScriptException if the display name resource-key property is locked
	 *                         or not defined on this element.
	 */

	void setDisplayNameKey(String displayNameKey) throws ScriptException;

	/**
	 * Gets the resource key of the display name.
	 * 
	 * @return the resource key of the display name
	 */

	String getDisplayNameKey();

	/**
	 * Sets the display name.
	 * 
	 * @param displayName the display name
	 * @throws ScriptException if the display name property is locked or not defined
	 *                         on this element.
	 */

	void setDisplayName(String displayName) throws ScriptException;

	/**
	 * Gets the display name.
	 * 
	 * @return the display name
	 */

	String getDisplayName();

	/**
	 * Gets the theme for this report design.
	 * 
	 * @return
	 */
	String getTheme();

	/**
	 * Sets the theme for this report design.
	 * 
	 * @param theme
	 * @throws ScriptException
	 */
	void setTheme(String theme) throws ScriptException;

	/**
	 * Create <code>IHideRule</code> instance
	 * 
	 * @return IHideRule
	 */

	IHideRule createHideRule();

	/**
	 * Create <code>IFilterCondition</code>
	 * 
	 * @return instance
	 */

	IFilterCondition createFilterCondition();

	/**
	 * Create <code>IDataBinding</code>
	 * 
	 * @return instance
	 */

	IDataBinding createDataBinding();

	/**
	 * Create <code>IHighLightRule</code>
	 * 
	 * @return instance
	 */

	IHighlightRule createHighLightRule();

	/**
	 * Create <code>ISortCondition</code>
	 * 
	 * @return instance
	 */

	ISortCondition createSortCondition();

	/**
	 * Creates the action structure.
	 * 
	 * @param action the structure handle
	 * @param handle the element handle that holds the action structure
	 * @return the action
	 */

	IAction createAction();

}
