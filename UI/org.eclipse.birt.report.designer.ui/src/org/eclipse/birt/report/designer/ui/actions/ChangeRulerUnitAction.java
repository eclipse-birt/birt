/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation .
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

package org.eclipse.birt.report.designer.ui.actions;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.jface.action.Action;

/**
 * Changes rule unit
 * 
 */
public class ChangeRulerUnitAction extends Action {

	private String value = ""; //$NON-NLS-1$

	/**
	 * Constructor.
	 * 
	 * @param value
	 * @param displayName
	 */
	public ChangeRulerUnitAction(String value, String displayName) {
		super();
		this.value = value;
		setText(displayName);
		setChecked(isCheckValue());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		if (Policy.TRACING_ACTIONS) {
			System.out.println("Change ruler unit action >> Run ..."); //$NON-NLS-1$
		}
		try {
			getReportDesignHandle().setDefaultUnits(value);
		} catch (SemanticException e) {
			ExceptionHandler.handle(e);
		}
	}

	private boolean isCheckValue() {
		if (value == null) {
			return false;
		}
		return value.equals(getReportDesignHandle().getDefaultUnits());
	}

	private ModuleHandle getReportDesignHandle() {
		return SessionHandleAdapter.getInstance().getReportDesignHandle();
	}
}
