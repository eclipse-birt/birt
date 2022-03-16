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
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.IDataGroupModel;

/**
 *
 */
public class DataGroupHandle extends ContentElementHandle implements IDataGroupModel {

	/**
	 * Constructs a data group handle with the given design and the element. The
	 * application generally does not create handles directly. Instead, it uses one
	 * of the navigation methods available on other element handles.
	 *
	 * @param module  the module
	 * @param element the model representation of the element
	 */

	public DataGroupHandle(Module module, DesignElement element) {
		super(module, element);
	}

	/**
	 * Gets the name of the group.
	 *
	 * @return the name of the group
	 */

	public String getGroupName() {
		return getStringProperty(GROUP_NAME_PROP);
	}

	/**
	 * Sets the group name.
	 *
	 * @param theName the group name to set
	 * @throws SemanticException
	 */

	public void setGroupName(String theName) throws SemanticException {

		// trim the name, have the same behavior as Name property.

		try {
			setProperty(GROUP_NAME_PROP, StringUtil.trimString(theName));
		} catch (NameException e) {
			throw e;
		} catch (SemanticException e) {
			assert false;
		}

	}

	/**
	 * Gets the expression that defines the group. This is normally simply a
	 * reference to a data set column.
	 *
	 * @return the expression as a string
	 *
	 * @see #setKeyExpr(String)
	 */

	public String getKeyExpr() {
		return getStringProperty(KEY_EXPR_PROP);
	}

	/**
	 * Sets the data group expression.
	 *
	 * @param expr the expression to set
	 * @throws SemanticException If the expression is invalid.
	 *
	 * @see #getKeyExpr()
	 */

	public void setKeyExpr(String expr) throws SemanticException {
		setProperty(KEY_EXPR_PROP, expr);
	}

	/**
	 * Returns the iterator for Sort list defined on the data group. The element in
	 * the iterator is the corresponding <code>StructureHandle</code>.
	 *
	 * @return the iterator for <code>SortKey</code> structure list defined on the
	 *         data group.
	 */

	public Iterator sortsIterator() {
		PropertyHandle propHandle = getPropertyHandle(SORT_PROP);
		assert propHandle != null;
		return propHandle.iterator();
	}

	/**
	 * Returns an iterator for the filter list defined on the data group. Each
	 * object returned is of type <code>StructureHandle</code>.
	 *
	 * @return the iterator for <code>FilterCond</code> structure list defined on
	 *         the data group.
	 */

	public Iterator filtersIterator() {
		PropertyHandle propHandle = getPropertyHandle(FILTER_PROP);
		assert propHandle != null;
		return propHandle.iterator();
	}

	/**
	 * Sets the base of the interval property of this data group.IntervalBase, in
	 * conjunction with Interval and IntervalRange, determines how data is divided
	 * into data groups.
	 *
	 * @param intervalBase interval base property value.
	 * @throws SemanticException if the property is locked.
	 */

	public void setIntervalBase(String intervalBase) throws SemanticException {
		setStringProperty(INTERVAL_BASE_PROP, intervalBase);
	}

	/**
	 * Return the interval base property value of this data group.
	 *
	 * @return interval baseF property value of this data group.
	 */

	public String getIntervalBase() {
		return getStringProperty(INTERVAL_BASE_PROP);
	}

	/**
	 * Returns the interval of this data group. The return value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 *
	 * <ul>
	 * <li><code>INTERVAL_NONE</code>
	 * <li><code>INTERVAL_PREFIX</code>
	 * <li><code>INTERVAL_YEAR</code>
	 * <li><code>INTERVAL_QUARTER</code>
	 * <li><code>INTERVAL_MONTH</code>
	 * <li><code>INTERVAL_WEEK</code>
	 * <li><code>INTERVAL_DAY</code>
	 * <li><code>INTERVAL_HOUR</code>
	 * <li><code>INTERVAL_MINUTE</code>
	 * <li><code>INTERVAL_SECOND</code>
	 * <li><code>INTERVAL_INTERVAL</code>
	 *
	 * </ul>
	 *
	 * @return the interval value as a string
	 */

	public String getInterval() {
		return getStringProperty(INTERVAL_PROP);
	}

	/**
	 * Returns the interval of this data group. The input value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 *
	 * <ul>
	 * <li><code>INTERVAL_NONE</code>
	 * <li><code>INTERVAL_PREFIX</code>
	 * <li><code>INTERVAL_YEAR</code>
	 * <li><code>INTERVAL_QUARTER</code>
	 * <li><code>INTERVAL_MONTH</code>
	 * <li><code>INTERVAL_WEEK</code>
	 * <li><code>INTERVAL_DAY</code>
	 * <li><code>INTERVAL_HOUR</code>
	 * <li><code>INTERVAL_MINUTE</code>
	 * <li><code>INTERVAL_SECOND</code>
	 * <li><code>INTERVAL_INTERVAL</code>
	 *
	 * </ul>
	 *
	 * @param interval the interval value as a string
	 * @throws SemanticException if the property is locked or the input value is not
	 *                           one of the above.
	 */

	public void setInterval(String interval) throws SemanticException {
		setStringProperty(INTERVAL_PROP, interval);
	}

	/**
	 * Returns the interval range of this data group.
	 *
	 * @return the interval range value as a double
	 */

	public double getIntervalRange() {
		return getFloatProperty(INTERVAL_RANGE_PROP);
	}

	/**
	 * Returns the interval range of this data group.
	 *
	 * @param intervalRange the interval range value as a double
	 * @throws SemanticException if the property is locked.
	 */

	public void setIntervalRange(double intervalRange) throws SemanticException {
		setFloatProperty(INTERVAL_RANGE_PROP, intervalRange);
	}

	/**
	 * Sets the interval range of data group.
	 *
	 * @param intervalRange the interval range value as a string.value is locale
	 *                      dependent.
	 * @throws SemanticException if the property is locked.
	 */

	public void setIntervalRange(String intervalRange) throws SemanticException {
		setStringProperty(INTERVAL_RANGE_PROP, intervalRange);
	}

	/**
	 * Returns the sort direction of this data group. The return value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 *
	 * <ul>
	 * <li><code>SORT_DIRECTION_ASC</code>
	 * <li><code>SORT_DIRECTION_DESC</code>
	 *
	 * </ul>
	 *
	 * @return the sort direction of this data group
	 */

	public String getSortDirection() {
		return getStringProperty(SORT_DIRECTION_PROP);
	}

	/**
	 * Sets the sort direction of this data group. The return value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 *
	 * <ul>
	 * <li><code>SORT_DIRECTION_ASC</code>
	 * <li><code>SORT_DIRECTION_DESC</code>
	 *
	 * </ul>
	 *
	 * @param direction the sort direction of this data group
	 * @throws SemanticException if the property is locked or the input value is not
	 *                           one of the above.
	 *
	 */

	public void setSortDirection(String direction) throws SemanticException {
		setStringProperty(SORT_DIRECTION_PROP, direction);
	}

	/**
	 * Sets the sort type, which indicates the way of sorting.
	 *
	 * @param sortType sort type.
	 * @throws SemanticException if the property is locked.
	 */

	public void setSortType(String sortType) throws SemanticException {
		setStringProperty(SORT_TYPE_PROP, sortType);
	}

	/**
	 * Return the sort type.
	 *
	 * @return the sort type.
	 */

	public String getSortType() {
		return getStringProperty(SORT_TYPE_PROP);
	}

}
