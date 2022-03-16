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

	@Override
	public String getKeyExpr() {
		return ((GroupHandle) handle).getKeyExpr();
	}

	@Override
	public void setKeyExpr(String expr) throws SemanticException {
		setProperty(IGroupElementModel.KEY_EXPR_PROP, expr);

	}

	@Override
	public String getName() {
		return ((GroupHandle) handle).getName();
	}

	@Override
	public void setName(String name) throws SemanticException {
		setProperty(IGroupElementModel.GROUP_NAME_PROP, StringUtil.trimString(name));

	}

	@Override
	public String getIntervalBase() {
		return ((GroupHandle) handle).getIntervalBase();
	}

	@Override
	public void setIntervalBase(String intervalBase) throws SemanticException {

		setProperty(IGroupElementModel.INTERVAL_BASE_PROP, intervalBase);
	}

	@Override
	public String getInterval() {
		return ((GroupHandle) handle).getInterval();
	}

	@Override
	public void setInterval(String interval) throws SemanticException {
		setProperty(IGroupElementModel.INTERVAL_PROP, interval);

	}

	@Override
	public double getIntervalRange() {
		return ((GroupHandle) handle).getIntervalRange();
	}

	@Override
	public void setIntervalRange(double intervalRange) throws SemanticException {
		setProperty(IGroupElementModel.INTERVAL_RANGE_PROP, Double.valueOf(intervalRange));

	}

	@Override
	public String getSortDirection() {
		return ((GroupHandle) handle).getSortDirection();
	}

	@Override
	public void setSortDirection(String direction) throws SemanticException {

		setProperty(IGroupElementModel.SORT_DIRECTION_PROP, direction);

	}

	@Override
	public boolean hasHeader() {
		return ((GroupHandle) handle).hasHeader();
	}

	@Override
	public boolean hasFooter() {
		return ((GroupHandle) handle).hasFooter();
	}

	@Override
	public String getTocExpression() {
		return ((GroupHandle) handle).getTocExpression();
	}

	@Override
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

	@Override
	public String getSortType() {
		return ((GroupHandle) handle).getSortType();
	}

	@Override
	public void setSortType(String sortType) throws SemanticException {

		setProperty(IGroupElementModel.SORT_TYPE_PROP, sortType);
	}

	/**
	 * Returns hide detail.
	 *
	 * @return hide detail.
	 */

	@Override
	public boolean getHideDetail() {
		Boolean value = (Boolean) ((GroupHandle) handle).getProperty(GroupHandle.HIDE_DETAIL_PROP);
		if (value == null) {
			return false;
		}
		return value.booleanValue();
	}

	/**
	 * Sets hide detail
	 *
	 * @param hideDetail hide detail
	 * @throws SemanticException if the property is locked.
	 */

	@Override
	public void setHideDetail(boolean hideDetail) throws SemanticException {
		setProperty(IGroupElementModel.HIDE_DETAIL_PROP, Boolean.valueOf(hideDetail));

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.simpleapi.IGroup#getPageBreakBefore()
	 */
	@Override
	public String getPageBreakBefore() {
		return ((GroupHandle) handle).getPageBreakBefore();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.simpleapi.IGroup#setPageBreakBefore
	 * (java.lang.String)
	 */
	@Override
	public void setPageBreakBefore(String value) throws SemanticException {
		setProperty(IStyleModel.PAGE_BREAK_BEFORE_PROP, value);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.simpleapi.IGroup#getPageBreakAfter()
	 */
	@Override
	public String getPageBreakAfter() {
		return ((GroupHandle) handle).getPageBreakAfter();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.simpleapi.IGroup#setPageBreakAfter(
	 * java.lang.String)
	 */
	@Override
	public void setPageBreakAfter(String value) throws SemanticException {

		setProperty(IStyleModel.PAGE_BREAK_AFTER_PROP, value);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.simpleapi.IGroup#getPageBreakInside()
	 */
	@Override
	public String getPageBreakInside() {
		return ((GroupHandle) handle).getPageBreakInside();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.simpleapi.IGroup#setPageBreakInside
	 * (java.lang.String)
	 */

	@Override
	public void setPageBreakInside(String value) throws SemanticException {
		setProperty(IStyleModel.PAGE_BREAK_INSIDE_PROP, value);
	}

}
