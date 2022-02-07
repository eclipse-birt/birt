/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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

package org.eclipse.birt.report.model.api.simpleapi;

import java.io.IOException;

import org.eclipse.birt.report.model.api.activity.SemanticException;

/**
 * Script wrapper of ReportDesignHandle
 * 
 */

public interface IReportDesign extends IDesignElement {

	/**
	 * Gets master page script instance.
	 * 
	 * @param name
	 * @return master page script instance
	 */

	IMasterPage getMasterPage(String name);

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
	 * method to get a label item by name
	 * 
	 * @param name
	 * @return label
	 */

	ILabel getLabel(String name);

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
	 * method to get a data item by name
	 * 
	 * @param name
	 * @return data item
	 */

	IDataItem getDataItem(String name);

	/**
	 * method to get a text item by name
	 * 
	 * @param name
	 * @return text item
	 */

	ITextItem getTextItem(String name);

	/**
	 * Sets the resource key of the display name.
	 * 
	 * @param displayNameKey the resource key of the display name
	 * @throws SemanticException if the display name resource-key property is locked
	 *                           or not defined on this element.
	 */

	void setDisplayNameKey(String displayNameKey) throws SemanticException;

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
	 * @throws SemanticException if the display name property is locked or not
	 *                           defined on this element.
	 */

	void setDisplayName(String displayName) throws SemanticException;

	/**
	 * Gets the display name.
	 * 
	 * @return the display name
	 */

	String getDisplayName();

	/**
	 * Saves the module to an existing file name. Call this only when the file name
	 * has been set.
	 * 
	 * @throws IOException if the file cannot be saved on the storage. Or the file
	 *                     name is not valid.
	 * 
	 * @see #saveAs(String)
	 */

	void save() throws IOException;

	/**
	 * Saves the design to the file name provided. The file name is saved in the
	 * design, and subsequent calls to <code>save( )</code> will save to this new
	 * name.
	 * 
	 * @param newName the new file name
	 * @throws IOException if the file cannot be saved. Or the file name is not
	 *                     valid.
	 * 
	 * @see #save()
	 */

	void saveAs(String newName) throws IOException;

	/**
	 * Gets the theme for this report design.
	 * 
	 * @return the theme for this report design.
	 */
	String getTheme();

	/**
	 * Sets the theme for this report design.
	 * 
	 * @param theme
	 * @throws SemanticException
	 */
	void setTheme(String theme) throws SemanticException;

	/**
	 * generic method to get report item by id
	 * 
	 * @param name
	 * @return report element
	 */
	IReportElement getReportElementByID(long id);

	/**
	 * Create <code>IFilterCondition</code>
	 * 
	 * @return instance
	 */

	IFilterCondition createFilterCondition();

	/**
	 * Create IHideRule instance
	 * 
	 * @return IHideRule
	 */

	IHideRule createHideRule();

	/**
	 * Create IHighLightRule
	 * 
	 * @return instance
	 */

	IHighlightRule createHighLightRule();

	/**
	 * Create ISortCondition
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

	public IAction createAction();

	/**
	 * Create <code>IDataBinding</code>
	 * 
	 * @return instance
	 */

	public IDataBinding createDataBinding();

}
