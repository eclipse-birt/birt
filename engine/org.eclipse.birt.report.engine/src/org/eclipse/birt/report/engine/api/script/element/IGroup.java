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

package org.eclipse.birt.report.engine.api.script.element;

import org.eclipse.birt.report.engine.api.script.ScriptException;

/**
 * Script wrapper of GroupHandle
 * 
 */

public interface IGroup extends IDesignElement {

	/**
	 * Gets the expression that defines the group. This is normally simply a
	 * reference to a data set column.
	 * 
	 * @return the expression as a string
	 * 
	 * @see #setKeyExpr(String)
	 */

	String getKeyExpr();

	/**
	 * Sets the group expression.
	 * 
	 * @param expr the expression to set
	 * @throws ScriptException If the expression is invalid.
	 * 
	 * @see #getKeyExpr()
	 */

	void setKeyExpr(String expr) throws ScriptException;

	/**
	 * Gets the name of the group.
	 * 
	 * @return the name of the group
	 */

	String getName();

	/**
	 * Sets the group name.
	 * 
	 * @param name the group name to set
	 * @throws ScriptException if the name is duplicate or the property is locked.
	 */

	void setName(String name) throws ScriptException;

	/**
	 * Return the interval base property value of this group.
	 * 
	 * @return interval baseF property value of this group.
	 */

	String getIntervalBase();

	/**
	 * Sets the base of the interval property of this group.IntervalBase, in
	 * conjunction with Interval and IntervalRange, determines how data is divided
	 * into groups.
	 * 
	 * @param intervalBase interval base property value.
	 * @throws ScriptException if the property is locked.
	 */

	void setIntervalBase(String intervalBase) throws ScriptException;

	/**
	 * Returns the interval of this group. The return value is defined in
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

	String getInterval();

	/**
	 * Returns the interval of this group. The input value is defined in
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
	 * @throws ScriptException if the property is locked or the input value is not
	 *                         one of the above.
	 */

	void setInterval(String interval) throws ScriptException;

	/**
	 * Returns the interval range of this group.
	 * 
	 * @return the interval range value as a double
	 */

	double getIntervalRange();

	/**
	 * Returns the interval range of this group.
	 * 
	 * @param intervalRange the interval range value as a double
	 * @throws ScriptException if the property is locked.
	 */

	void setIntervalRange(double intervalRange) throws ScriptException;

	/**
	 * Returns the sort direction of this group. The return value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * 
	 * <ul>
	 * <li><code>SORT_DIRECTION_ASC</code>
	 * <li><code>SORT_DIRECTION_DESC</code>
	 * 
	 * </ul>
	 * 
	 * @return the sort direction of this group
	 */

	String getSortDirection();

	/**
	 * Sets the sort direction of this group. The return value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * 
	 * <ul>
	 * <li><code>SORT_DIRECTION_ASC</code>
	 * <li><code>SORT_DIRECTION_DESC</code>
	 * 
	 * </ul>
	 * 
	 * @param direction the sort direction of this group
	 * @throws ScriptException if the property is locked or the input value is not
	 *                         one of the above.
	 * 
	 */

	void setSortDirection(String direction) throws ScriptException;

	/**
	 * Checks whether the group header slot is empty.
	 * 
	 * @return true is the header slot is not empty, otherwise, return false.
	 * 
	 */

	boolean hasHeader();

	/**
	 * Checks whether the group footer slot is empty.
	 * 
	 * @return true is the footer slot is not empty, otherwise, return false.
	 * 
	 */

	boolean hasFooter();

	/**
	 * Returns the expression evalueated as a table of contents entry for this item.
	 * 
	 * @return the expression evaluated as a table of contents entry for this item
	 * @see #setTocExpression(String)
	 */

	String getTocExpression();

	/**
	 * Sets a table of contents entry for this item. The TOC property defines an
	 * expression that returns a string that is to appear in the Table of Contents
	 * for this item or its container.
	 * 
	 * @param expression the expression that returns a string
	 * @throws ScriptException if the TOC property is locked by the property mask.
	 * 
	 * @see #getTocExpression()
	 */

	void setTocExpression(String expression) throws ScriptException;

	/**
	 * Return the sort type.
	 * 
	 * @return the sort type.
	 */

	String getSortType();

	/**
	 * Sets the sort type, which indicates the way of sorting
	 * 
	 * @param sortType sort type.
	 * @throws ScriptException if the property is locked.
	 */

	void setSortType(String sortType) throws ScriptException;

	/**
	 * Returns hide detail.
	 * 
	 * @return hide detail.
	 */

	boolean getHideDetail();

	/**
	 * Sets hide detail
	 * 
	 * @param hideDetail hide detail
	 * @throws ScriptException if the property is locked.
	 */

	void setHideDetail(boolean hideDetail) throws ScriptException;

	/**
	 * Returns the value for break before property. The return value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * 
	 * <ul>
	 * <li><code>PAGE_BREAK_BEFORE_AUTO</code>
	 * <li><code>PAGE_BREAK_BEFORE_ALWAYS</code>
	 * <li><code>PAGE_BREAK_BEFORE_AVOID</code>
	 * <li><code>PAGE_BREAK_BEFORE_ALWAYS_EXCLUDING_FIRST</code>
	 * </ul>
	 * 
	 * 
	 * @return the value in string
	 */

	public String getPageBreakBefore();

	/**
	 * Sets the value for break before property. The input value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * 
	 * <ul>
	 * <li><code>PAGE_BREAK_BEFORE_AUTO</code>
	 * <li><code>PAGE_BREAK_BEFORE_ALWAYS</code>
	 * <li><code>PAGE_BREAK_BEFORE_AVOID</code>
	 * <li><code>PAGE_BREAK_BEFORE_ALWAYS_EXCLUDING_FIRST</code>
	 * </ul>
	 * 
	 * 
	 * @param value the page break before value
	 * @throws ScriptException if the value is not one of above choices.
	 */

	public void setPageBreakBefore(String value) throws ScriptException;

	/**
	 * Returns the value for break after property. The return value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * 
	 * <ul>
	 * <li><code>PAGE_BREAK_AFTER_AUTO</code>
	 * <li><code>PAGE_BREAK_AFTER_ALWAYS</code>
	 * <li><code>PAGE_BREAK_AFTER_AVOID</code>
	 * <li><code>PAGE_BREAK_AFTER_ALWAYS_EXCLUDING_LAST</code>
	 * </ul>
	 * 
	 * @return the value in string
	 * 
	 */

	public String getPageBreakAfter();

	/**
	 * Sets the value for break after property. The return value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * 
	 * <ul>
	 * <li><code>PAGE_BREAK_AFTER_AUTO</code>
	 * <li><code>PAGE_BREAK_AFTER_ALWAYS</code>
	 * <li><code>PAGE_BREAK_AFTER_AVOID</code>
	 * <li><code>PAGE_BREAK_AFTER_ALWAYS_EXCLUDING_LAST</code>
	 * </ul>
	 * 
	 * @param value the value to set
	 * @throws ScriptException if the value is not one of above choices.
	 */

	public void setPageBreakAfter(String value) throws ScriptException;

	/**
	 * Gets the value for break inside property. The return value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * 
	 * <ul>
	 * <li><code>PAGE_BREAK_INSIDE_AVOID</code>
	 * <li><code>PAGE_BREAK_INSIDE_AUTO</code>
	 * </ul>
	 * 
	 * @return the value in string
	 */

	public String getPageBreakInside();

	/**
	 * Sets the value for the page-break-inside. The return value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * 
	 * <ul>
	 * <li><code>PAGE_BREAK_INSIDE_AVOID</code>
	 * <li><code>PAGE_BREAK_INSIDE_AUTO</code>
	 * </ul>
	 * 
	 * 
	 * @param value the value to set
	 * @throws ScriptException
	 * 
	 */
	public void setPageBreakInside(String value) throws ScriptException;
}
