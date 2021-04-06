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
import org.eclipse.birt.report.model.api.ColumnHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.simpleapi.IColumn;
import org.eclipse.birt.report.model.api.simpleapi.IHideRule;

/**
 * Column script. Implements of <code>IColumn</code>
 * 
 */

public class Column extends DesignElement implements IColumn {

	/**
	 * Constructor.
	 * 
	 * @param columnHandle
	 */

	public Column(ColumnHandle columnHandle) {
		super(columnHandle);
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
