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

package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import org.eclipse.birt.report.model.api.activity.NotificationEvent;

/**
 * FormPage assistant class, provides form type(Filter, Sorting, Groups and
 * Hight-lights) sensitive data and processes.
 * 
 * 
 */
public interface ISortingFormProvider extends IDescriptorProvider {

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
	 * Checks whether the given property of the given element can be modified.
	 * 
	 * @param element  The data object
	 * @param property The column name
	 * @return <code>true</code> if the property can be modified, and
	 *         <code>false</code> if it is not modifiable
	 */
	Object getValue(Object element, String property);

	/**
	 * Modifies the value for the given property of the given element. Has no effect
	 * if the element does not have the given property, or if the property cannot be
	 * modified.
	 * 
	 * @param data     The model element
	 * @param property The column name
	 * @param value    The new property value
	 * @return True if success, otherwise false.
	 */
	boolean needRefreshed(NotificationEvent event);

	boolean needRebuilded(NotificationEvent event);

	boolean isEnable();

	boolean isEditable();

	boolean isAddEnable();

	boolean isEditEnable();

	boolean isDeleteEnable();

	void setSortingColumnIndex(int index);

	void setSortDirection(int dir);

	int getOriginalIndex(int pos);

	int getShowIndex(int pos);
}