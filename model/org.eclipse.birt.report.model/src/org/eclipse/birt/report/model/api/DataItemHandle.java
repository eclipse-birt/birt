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

package org.eclipse.birt.report.model.api;

import java.util.Iterator;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.Action;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.IDataItemModel;
import org.eclipse.birt.report.model.util.impl.ActionHelper;

/**
 * Represents a data item element. A data item has an action, value expression
 * and help text.
 * 
 * @see org.eclipse.birt.report.model.elements.DataItem
 */

public class DataItemHandle extends ReportItemHandle implements IDataItemModel {

	/**
	 * Constructs a handle of the data item with the given design and a data item.
	 * The application generally does not create handles directly. Instead, it uses
	 * one of the navigation methods available on other element handles.
	 * 
	 * @param module  the module
	 * @param element the model representation of the element
	 */

	public DataItemHandle(Module module, DesignElement element) {
		super(module, element);
	}

	/**
	 * Returns a handle to work with the action property, action is a structure that
	 * defines a hyperlink.
	 * 
	 * @return a handle to the action property, return <code>null</code> if the
	 *         action has not been set on the data item.
	 * @see ActionHandle
	 */

	public ActionHandle getActionHandle() {
		return new ActionHelper(this, ACTION_PROP).getActionHandle();
	}

	/**
	 * Set an action on the image.
	 * 
	 * @param action new action to be set on the image, it represents a bookmark
	 *               link, hyperlink, and drill through etc.
	 * @return a handle to the action property, return <code>null</code> if the
	 *         action has not been set on the image.
	 * 
	 * @throws SemanticException if member of the action is not valid.
	 */

	public ActionHandle setAction(Action action) throws SemanticException {
		return new ActionHelper(this, ACTION_PROP).setAction(action);
	}

	/**
	 * Returns the iterator for action defined on this data item.
	 * 
	 * @return the iterator for <code>Action</code> structure list defined on this
	 *         data item
	 */

	public Iterator<ActionHandle> actionsIterator() {
		return new ActionHelper(this, ACTION_PROP).actionsIterator();
	}

	/**
	 * Returns the value of the distinct property.
	 * 
	 * @return the distinct value as a string
	 * 
	 * @deprecated by the drop function of Cell element.
	 */

	public String getDistinct() {
		return ""; //$NON-NLS-1$
	}

	/**
	 * Sets the value of the distinct property. The input value is defined in
	 * <code>DesignChoiceConstants</code> and is one of these:
	 * 
	 * <ul>
	 * <li>DISTINCT_ALL</li>
	 * <li>DISTINCT_REPEAT</li>
	 * <li>DISTINCT_REPEAT_ON_PAGE</li>
	 * </ul>
	 * 
	 * @param distinct the distinct value as a string
	 * 
	 * @throws SemanticException If the property is locked or the value is not one
	 *                           of the above.
	 * 
	 * @deprecated by the drop function of Cell element.
	 */

	public void setDistinct(String distinct) throws SemanticException {
	}

	/**
	 * Returns the value of the distinct-reset property.
	 * 
	 * @return the distinct-set value as a string
	 * 
	 * @deprecated by the drop function of Cell element.
	 */

	public String getDistinctReset() {
		return ""; //$NON-NLS-1$
	}

	/**
	 * Returns the value of the distinct-reset property.
	 * 
	 * @param value the distinct-set value as a string
	 * @throws SemanticException If the property is locked.
	 * 
	 * @deprecated by the drop function of Cell element.
	 */

	public void setDistinctReset(String value) throws SemanticException {
	}

	/**
	 * Returns the expression that gives the value that the data item displays.
	 * 
	 * @return the value expression
	 * 
	 * @deprecated As of BIRT version 2.1.0, replaced by getResultSetColumn( )
	 * @see {@link #getResultSetExpression()} for the shortcut function to get the
	 *      value expression
	 */

	public String getValueExpr() {
		return null;
	}

	/**
	 * Sets the expression for the value that the data item is to display. This
	 * method still can be used. However, if the user uses both this method and
	 * setResultSetColumn(String columnName), the result is unexpectable. It is
	 * strongly recommended to use ONLY one of two methods.
	 * 
	 * 
	 * @param expr the expression to set
	 * @throws SemanticException If the property is locked.
	 * @deprecated As of BIRT version 2.1.0, replaced by setResultSetColumn(String
	 *             columnName)
	 * @throws SemanticException
	 */

	public void setValueExpr(String expr) throws SemanticException {
		setStringProperty(IDataItemModel.RESULT_SET_COLUMN_PROP, expr);
	}

	/**
	 * Returns the help text of this data item.
	 * 
	 * @return the help text
	 */

	public String getHelpText() {
		return getStringProperty(IDataItemModel.HELP_TEXT_PROP);
	}

	/**
	 * Sets the help text of this data item.
	 * 
	 * @param value the help text
	 * 
	 * @throws SemanticException if the property is locked.
	 */

	public void setHelpText(String value) throws SemanticException {
		setStringProperty(IDataItemModel.HELP_TEXT_PROP, value);
	}

	/**
	 * Returns the help text resource key of this data item.
	 * 
	 * @return the help text key
	 */

	public String getHelpTextKey() {
		return getStringProperty(IDataItemModel.HELP_TEXT_KEY_PROP);
	}

	/**
	 * Sets the resource key of the help text of this data item.
	 * 
	 * @param value the resource key of the help text
	 * 
	 * @throws SemanticException if the property is locked.
	 */

	public void setHelpTextKey(String value) throws SemanticException {
		setStringProperty(IDataItemModel.HELP_TEXT_KEY_PROP, value);
	}

	/**
	 * Looks the column name from the data binding element that is nearest to this
	 * data item. Iterate the column name expression list to see if there is a
	 * column name is equals with the value of the
	 * DataItemHandle.DATA_COLUMN_NAME_PROP on this data item. If yes, return the
	 * expression value.
	 * 
	 * @return the expression value.
	 * @throws SemanticException
	 */

	public String getResultSetExpression() {
		String columnName = getResultSetColumn();
		if (columnName == null)
			return null;

		Iterator columnBindings = columnBindingsIterator();

		while (columnBindings.hasNext()) {
			ComputedColumnHandle column = (ComputedColumnHandle) columnBindings.next();
			if (columnName.equals(column.getName()))
				return column.getExpression();
		}
		return null;
	}

	/**
	 * Gets the value of the result set column name property on this data item.
	 * 
	 * @return the value of the property.
	 */

	public String getResultSetColumn() {
		return getStringProperty(IDataItemModel.RESULT_SET_COLUMN_PROP);
	}

	/**
	 * Sets the value of the column name property.
	 * 
	 * @param columnName the value to set.
	 * @throws SemanticException
	 */

	public void setResultSetColumn(String columnName) throws SemanticException {
		setStringProperty(IDataItemModel.RESULT_SET_COLUMN_PROP, columnName);
	}
}
