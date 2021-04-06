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

package org.eclipse.birt.report.model.simpleapi;

import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.simpleapi.IGroup;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.elements.interfaces.IGroupElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;

public class Group extends DesignElement implements IGroup {

	public Group(GroupHandle handle) {
		super(handle);
	}

	public String getKeyExpr() {
		return ((GroupHandle) handle).getKeyExpr();
	}

	public void setKeyExpr(String expr) throws SemanticException {
		setProperty(IGroupElementModel.KEY_EXPR_PROP, expr);

	}

	public String getName() {
		return ((GroupHandle) handle).getName();
	}

	public void setName(String name) throws SemanticException {
		setProperty(IGroupElementModel.GROUP_NAME_PROP, StringUtil.trimString(name));

	}

	public String getIntervalBase() {
		return ((GroupHandle) handle).getIntervalBase();
	}

	public void setIntervalBase(String intervalBase) throws SemanticException {

		setProperty(IGroupElementModel.INTERVAL_BASE_PROP, intervalBase);
	}

	public String getInterval() {
		return ((GroupHandle) handle).getInterval();
	}

	public void setInterval(String interval) throws SemanticException {
		setProperty(IGroupElementModel.INTERVAL_PROP, interval);

	}

	public double getIntervalRange() {
		return ((GroupHandle) handle).getIntervalRange();
	}

	public void setIntervalRange(double intervalRange) throws SemanticException {
		setProperty(IGroupElementModel.INTERVAL_RANGE_PROP, Double.valueOf(intervalRange));

	}

	public String getSortDirection() {
		return ((GroupHandle) handle).getSortDirection();
	}

	public void setSortDirection(String direction) throws SemanticException {

		setProperty(IGroupElementModel.SORT_DIRECTION_PROP, direction);

	}

	public boolean hasHeader() {
		return ((GroupHandle) handle).hasHeader();
	}

	public boolean hasFooter() {
		return ((GroupHandle) handle).hasFooter();
	}

	public String getTocExpression() {
		return ((GroupHandle) handle).getTocExpression();
	}

	public void setTocExpression(String expression) throws SemanticException {
		ActivityStack cmdStack = handle.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			((GroupHandle) handle).setTocExpression(expression);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	public String getSortType() {
		return ((GroupHandle) handle).getSortType();
	}

	public void setSortType(String sortType) throws SemanticException {

		setProperty(IGroupElementModel.SORT_TYPE_PROP, sortType);
	}

	/**
	 * Returns hide detail.
	 * 
	 * @return hide detail.
	 */

	public boolean getHideDetail() {
		Boolean value = (Boolean) ((GroupHandle) handle).getProperty(GroupHandle.HIDE_DETAIL_PROP);
		if (value == null)
			return false;
		return value.booleanValue();
	}

	/**
	 * Sets hide detail
	 * 
	 * @param hideDetail hide detail
	 * @throws SemanticException if the property is locked.
	 */

	public void setHideDetail(boolean hideDetail) throws SemanticException {
		setProperty(IGroupElementModel.HIDE_DETAIL_PROP, Boolean.valueOf(hideDetail));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.simpleapi.IGroup#getPageBreakBefore()
	 */
	public String getPageBreakBefore() {
		return ((GroupHandle) handle).getPageBreakBefore();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.simpleapi.IGroup#setPageBreakBefore
	 * (java.lang.String)
	 */
	public void setPageBreakBefore(String value) throws SemanticException {
		setProperty(IStyleModel.PAGE_BREAK_BEFORE_PROP, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.simpleapi.IGroup#getPageBreakAfter()
	 */
	public String getPageBreakAfter() {
		return ((GroupHandle) handle).getPageBreakAfter();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.simpleapi.IGroup#setPageBreakAfter(
	 * java.lang.String)
	 */
	public void setPageBreakAfter(String value) throws SemanticException {

		setProperty(IStyleModel.PAGE_BREAK_AFTER_PROP, value);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.simpleapi.IGroup#getPageBreakInside()
	 */
	public String getPageBreakInside() {
		return ((GroupHandle) handle).getPageBreakInside();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.simpleapi.IGroup#setPageBreakInside
	 * (java.lang.String)
	 */

	public void setPageBreakInside(String value) throws SemanticException {
		setProperty(IStyleModel.PAGE_BREAK_INSIDE_PROP, value);
	}

}
