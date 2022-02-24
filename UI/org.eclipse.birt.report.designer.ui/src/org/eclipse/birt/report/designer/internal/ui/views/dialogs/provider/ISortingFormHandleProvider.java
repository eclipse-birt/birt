/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.designer.internal.ui.views.dialogs.provider;

import org.eclipse.birt.report.model.api.activity.NotificationEvent;

/**
 * FormPage assistant class, provides form type(Filter, Sorting, Groups and
 * Hight-lights) sensitive data and processes.
 * 
 * 
 */
public interface ISortingFormHandleProvider {

	/**
	 * Gets the column Names to show in table.
	 * 
	 * @return String array contains all column names.
	 */
	String[] getColumnNames();

	/**
	 * Gets the width of each column.
	 * 
	 * @return <code>int<code> array of all columns width.
	 */
	int[] getColumnWidths();

	/**
	 * Gets the hint text for the form.
	 * 
	 * @return The hint text.
	 */
	String getTitle();

	/**
	 * Deletes an item.
	 * 
	 * @param pos The item's current position
	 * @return True if success, otherwise false.
	 */
	boolean doDeleteItem(int pos) throws Exception;

	/**
	 * Adds an item.
	 * 
	 * @param pos The position to insert a new item
	 * 
	 * @return True if success, otherwise false.
	 */
	boolean doAddItem(int pos) throws Exception;

	/**
	 * Edits a given item.
	 * 
	 * @param pos The item's current position
	 * @return True if success, otherwise false.
	 */
	boolean doEditItem(int pos);

	/**
	 * Gets the label for the element under given index.
	 * 
	 * @param element     The data object.
	 * @param columnIndex The table column index.
	 * @return The label for the element under given index
	 */
	String getColumnText(Object element, int columnIndex);

	/**
	 * Gets the image path for the element under given index.
	 * 
	 * @param element     The data object.
	 * @param columnIndex The table column index.
	 * @return The image path for the element under given index
	 */
	String getImagePath(Object element, int columnIndex);

	/**
	 * Gets all elements of the given input.
	 * 
	 * @param inputElement The input object.
	 * @return Elements array.
	 */
	Object[] getElements(Object inputElement);

	/**
	 * Returns the value for the given property of the given element. Returns
	 * <code>null</code> if the element does not have the given property.
	 * 
	 * @param element  The data object
	 * @param property The column name
	 * @return The property value
	 */
	Object getValue(Object element, String property);

	/**
	 * Judges whether to refresh data.
	 * 
	 * @param event The DE notify event.
	 * @return True needs refresh, false not need.
	 */
	boolean needRefreshed(NotificationEvent event);

	public boolean isEditable();

	void setSortingColumnIndex(int index);

	void setSortDirection(int dir);

	int getOriginalIndex(int pos);

	int getShowIndex(int pos);

}
