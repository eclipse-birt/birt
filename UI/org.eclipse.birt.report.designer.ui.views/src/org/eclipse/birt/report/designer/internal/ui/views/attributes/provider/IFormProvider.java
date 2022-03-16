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

package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Table;

/**
 * FormPage assistant class, provides form type(Filter, Sorting, Groups and
 * Hight-lights) sensitive data and processes.
 *
 *
 */
public interface IFormProvider extends IDescriptorProvider {

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
	 * Gets the column editors for the form.
	 *
	 * @param table The table widget these editors reside.
	 * @return An array contains all editors.
	 */
	CellEditor[] getEditors(Table table);

	/**
	 * Moves one item from a position to another.
	 *
	 * @param oldPos The item's current position
	 * @param newPos The item's new position
	 * @return True if success, otherwise false.
	 */
	boolean doMoveItem(int oldPos, int newPos) throws Exception;

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
	 * Gets the image for the element under given index.
	 *
	 * @param element     The data object.
	 * @param columnIndex The table column index.
	 * @return The image for the element under given index
	 */
	Image getImage(Object element, int columnIndex);

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
	boolean canModify(Object element, String property);

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
	 * Modifies the value for the given property of the given element. Has no effect
	 * if the element does not have the given property, or if the property cannot be
	 * modified.
	 *
	 * @param data     The model element
	 * @param property The column name
	 * @param value    The new property value
	 * @return True if success, otherwise false.
	 */
	boolean modify(Object data, String property, Object value) throws Exception;

	/**
	 * Judges whether to refresh data.
	 *
	 * @param event The DE notify event.
	 * @return True needs refresh, false not need.
	 */
	boolean needRefreshed(NotificationEvent event);

	boolean needRebuilded(NotificationEvent event);

	boolean isEnable();

	boolean isEditable();

	boolean isAddEnable(Object selectedObject);

	boolean isEditEnable(Object selectedObject);

	boolean isDeleteEnable(Object selectedObject);

	boolean isUpEnable(Object selectedObject);

	boolean isDownEnable(Object selectedObject);

}
