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

package org.eclipse.birt.report.engine.script.internal.element;

import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.engine.api.script.element.IGroup;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;

public class Group extends DesignElement implements IGroup {

	public Group(GroupHandle handle) {
		super(handle);
	}

	@Override
	public String getKeyExpr() {
		return ((org.eclipse.birt.report.model.api.simpleapi.IGroup) designElementImpl).getKeyExpr();
	}

	@Override
	public void setKeyExpr(String expr) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IGroup) designElementImpl).setKeyExpr(expr);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	@Override
	public String getName() {
		return ((org.eclipse.birt.report.model.api.simpleapi.IGroup) designElementImpl).getName();
	}

	@Override
	public void setName(String name) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IGroup) designElementImpl).setName(name);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	@Override
	public String getIntervalBase() {
		return ((org.eclipse.birt.report.model.api.simpleapi.IGroup) designElementImpl).getIntervalBase();
	}

	@Override
	public void setIntervalBase(String intervalBase) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IGroup) designElementImpl).setIntervalBase(intervalBase);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	@Override
	public String getInterval() {
		return ((org.eclipse.birt.report.model.api.simpleapi.IGroup) designElementImpl).getInterval();
	}

	@Override
	public void setInterval(String interval) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IGroup) designElementImpl).setInterval(interval);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	@Override
	public double getIntervalRange() {
		return ((org.eclipse.birt.report.model.api.simpleapi.IGroup) designElementImpl).getIntervalRange();
	}

	@Override
	public void setIntervalRange(double intervalRange) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IGroup) designElementImpl).setIntervalRange(intervalRange);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	@Override
	public String getSortDirection() {
		return ((org.eclipse.birt.report.model.api.simpleapi.IGroup) designElementImpl).getSortDirection();
	}

	@Override
	public void setSortDirection(String direction) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IGroup) designElementImpl).setSortDirection(direction);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	@Override
	public boolean hasHeader() {
		return ((org.eclipse.birt.report.model.api.simpleapi.IGroup) designElementImpl).hasHeader();
	}

	@Override
	public boolean hasFooter() {
		return ((org.eclipse.birt.report.model.api.simpleapi.IGroup) designElementImpl).hasFooter();
	}

	@Override
	public String getTocExpression() {
		return ((org.eclipse.birt.report.model.api.simpleapi.IGroup) designElementImpl).getTocExpression();
	}

	@Override
	public void setTocExpression(String expression) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IGroup) designElementImpl).setTocExpression(expression);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	@Override
	public String getSortType() {
		return ((org.eclipse.birt.report.model.api.simpleapi.IGroup) designElementImpl).getSortType();
	}

	@Override
	public void setSortType(String sortType) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IGroup) designElementImpl).setSortType(sortType);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/**
	 * Returns hide detail.
	 *
	 * @return hide detail.
	 */

	@Override
	public boolean getHideDetail() {
		return ((org.eclipse.birt.report.model.api.simpleapi.IGroup) designElementImpl).getHideDetail();
	}

	/**
	 * Sets hide detail
	 *
	 * @param hideDetail hide detail
	 * @throws ScriptException if the property is locked.
	 */

	@Override
	public void setHideDetail(boolean hideDetail) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IGroup) designElementImpl).setHideDetail(hideDetail);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.IGroup#getPageBreakBefore()
	 */
	@Override
	public String getPageBreakBefore() {

		return ((org.eclipse.birt.report.model.api.simpleapi.IGroup) designElementImpl).getPageBreakBefore();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.IGroup#setPageBreakBefore(
	 * java.lang.String)
	 */
	@Override
	public void setPageBreakBefore(String value) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IGroup) designElementImpl).setPageBreakBefore(value);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.IGroup#getPageBreakAfter()
	 */
	@Override
	public String getPageBreakAfter() {
		return ((org.eclipse.birt.report.model.api.simpleapi.IGroup) designElementImpl).getPageBreakAfter();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.IGroup#setPageBreakAfter(
	 * java.lang.String)
	 */

	@Override
	public void setPageBreakAfter(String value) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IGroup) designElementImpl).setPageBreakAfter(value);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.IGroup#getPageBreakInside()
	 */
	@Override
	public String getPageBreakInside() {
		return ((org.eclipse.birt.report.model.api.simpleapi.IGroup) designElementImpl).getPageBreakInside();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.IGroup#setPageBreakInside(
	 * java.lang.String)
	 */
	@Override
	public void setPageBreakInside(String value) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IGroup) designElementImpl).setPageBreakInside(value);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

}
