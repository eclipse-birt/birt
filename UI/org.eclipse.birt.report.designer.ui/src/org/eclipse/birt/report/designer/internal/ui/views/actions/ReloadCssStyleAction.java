/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views.actions;

import java.util.logging.Level;

import org.eclipse.birt.report.designer.internal.ui.command.CommandUtils;
import org.eclipse.birt.report.designer.internal.ui.command.ICommandParameterNameContants;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.AbstractThemeHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.css.CssStyleSheetHandle;

/**
 * 
 */

public class ReloadCssStyleAction extends AbstractViewAction {

	public static final String ID = "org.eclipse.birt.report.designer.internal.ui.views.actions.ReloadCssStylesAction"; //$NON-NLS-1$
	public static final String ACTION_TEXT = Messages.getString("ReloadCssStylesAction.Action.Text"); //$NON-NLS-1$

	public ReloadCssStyleAction(Object selectedObject, String text) {
		super(selectedObject, text);
	}

	public ReloadCssStyleAction(Object selectedObject) {
		super(selectedObject, ACTION_TEXT);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see isEnabled()
	 */
	public boolean isEnabled() {
		Object selection = getSelection();
		if (selection == null) {
			return false;
		}
		if ((selection instanceof SlotHandle)
				&& (((SlotHandle) selection).getElementHandle() instanceof ReportDesignHandle)) {
			selection = ((SlotHandle) selection).getElementHandle();
		}

		if (selection instanceof CssStyleSheetHandle) {
			return true;
		}
		if (selection instanceof ReportDesignHandle) {
			return ((ReportDesignHandle) selection).getAllCssStyleSheets().size() > 0 ? true : false;
		} else if (selection instanceof AbstractThemeHandle) {
			return ((AbstractThemeHandle) selection).getAllCssStyleSheets().size() > 0 ? true : false;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		Object selection = getSelection();
		if (selection != null) {
			if ((selection instanceof SlotHandle)
					&& (((SlotHandle) selection).getElementHandle() instanceof ReportDesignHandle)) {
				selection = ((SlotHandle) selection).getElementHandle();
			}

			CommandUtils.setVariable(ICommandParameterNameContants.SELECTION, selection);
		}

		try {
			CommandUtils.executeCommand("org.eclipse.birt.report.designer.ui.command.reloadCssStyleCommand", null); //$NON-NLS-1$
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		} finally {
			CommandUtils.removeVariable(ICommandParameterNameContants.SELECTION);
		}
	}

}
