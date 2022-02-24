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
import org.eclipse.birt.report.model.api.DimensionHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.simpleapi.IHideRule;
import org.eclipse.birt.report.model.api.simpleapi.IHighlightRule;
import org.eclipse.birt.report.model.api.simpleapi.IRow;
import org.eclipse.birt.report.model.elements.interfaces.ITableRowModel;

public class Row extends DesignElement implements IRow {

	public Row(RowHandle handle) {
		super(handle);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IRow#getHeight()
	 */

	public String getHeight() {
		DimensionHandle height = ((RowHandle) handle).getHeight();
		return (height == null ? null : height.getStringValue());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IRow#getBookmark()
	 */

	public String getBookmark() {
		return ((RowHandle) handle).getBookmark();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IRow#setBookmark(java
	 * .lang.String)
	 */

	public void setBookmark(String value) throws SemanticException {
		setProperty(ITableRowModel.BOOKMARK_PROP, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.engine.api.script.IHighlightRuleMethod#
	 * addHighlightRule
	 * (org.eclipse.birt.report.engine.api.script.element.IHighlightRule)
	 */

	public void addHighlightRule(IHighlightRule rule) throws SemanticException {
		if (rule == null)
			return;
		ActivityStack cmdStack = handle.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			HighlightRuleMethodUtil.addHighlightRule(handle, rule);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.engine.api.script.IHighlightRuleMethod#
	 * getHighlightRules()
	 */

	public IHighlightRule[] getHighlightRules() {
		return HighlightRuleMethodUtil.getHighlightRules(handle);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.engine.api.script.IHighlightRuleMethod#
	 * removeHighlightRules()
	 */

	public void removeHighlightRules() throws SemanticException {
		ActivityStack cmdStack = handle.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			HighlightRuleMethodUtil.removeHighlightRules(handle);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.engine.api.script.IHighlightRuleMethod#
	 * removeHighlightRule
	 * (org.eclipse.birt.report.engine.api.script.element.IHighlightRule)
	 */

	public void removeHighlightRule(IHighlightRule rule) throws SemanticException {
		if (rule == null)
			return;

		ActivityStack cmdStack = handle.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			HighlightRuleMethodUtil.removeHighlightRule(handle, rule);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IHideRuleStructure#
	 * addHideRule(org.eclipse.birt.report.engine.api.script.element.IHideRule)
	 */
	public void addHideRule(IHideRule rule) throws SemanticException {
		if (rule == null)
			return;

		ActivityStack cmdStack = handle.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			HideRuleMethodUtil.addHideRule(handle, rule);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IHideRuleStructure#
	 * getHideRules()
	 */

	public IHideRule[] getHideRules() {
		return HideRuleMethodUtil.getHideRules(handle);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IHideRuleStructure#
	 * removeHideRule (org.eclipse.birt.report.engine.api.script.element.IHideRule)
	 */

	public void removeHideRule(IHideRule rule) throws SemanticException {
		if (rule == null)
			return;

		ActivityStack cmdStack = handle.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			HideRuleMethodUtil.removeHideRule(handle, rule);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IHideRuleStructure#
	 * removeHideRules()
	 */

	public void removeHideRules() throws SemanticException {
		ActivityStack cmdStack = handle.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			HideRuleMethodUtil.removeHideRules(handle);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}
}
