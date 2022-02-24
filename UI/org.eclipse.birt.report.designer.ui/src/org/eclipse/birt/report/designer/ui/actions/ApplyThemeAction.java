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
package org.eclipse.birt.report.designer.ui.actions;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.actions.MenuUpdateAction.DynamicItemAction;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.ThemeHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;

public class ApplyThemeAction extends DynamicItemAction {

	private static final String ID = "ApplyTheme"; //$NON-NLS-1$
	private static final String STACK_MSG_APPLY_STYLE = Messages.getString("ApplyThemeAction.stackMsg.applyStyle");//$NON-NLS-1$
	private ThemeHandle handle;

	public ApplyThemeAction(ThemeHandle theme) {
		this.handle = theme;
		setId(ID);
		if (handle == null) {
			setText(Messages.getString(Messages.getString("ApplyThemeAction.actionLabel.none"))); //$NON-NLS-1$
		} else {
			setText(DEUtil.getEscapedMenuItemText(DEUtil.getDisplayLabel(handle)));
		}
	}

	/**
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		if (Policy.TRACING_ACTIONS) {
			System.out.println("Apply style rule action >> Run ..."); //$NON-NLS-1$
		}
		CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();
		stack.startTrans(STACK_MSG_APPLY_STYLE);

		try {
			SessionHandleAdapter.getInstance().getReportDesignHandle().setTheme(handle);
			stack.commit();
		} catch (SemanticException e) {
			stack.rollbackAll();
			ExceptionHandler.handle(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#isEnabled()
	 */
	public boolean isEnabled() {
		return true;
	}
}
