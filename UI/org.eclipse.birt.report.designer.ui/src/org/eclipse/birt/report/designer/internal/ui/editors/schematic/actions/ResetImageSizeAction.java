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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions;

import java.util.logging.Level;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.command.CommandUtils;
import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.ui.IWorkbenchPart;

/**
 * 
 */

public class ResetImageSizeAction extends ContextSelectionAction {

	public static final String ID = "org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.ResetImageSizeAction"; //$NON-NLS-1$
	public static final String LABEL = Messages.getString("ResetImageSizeAction.label"); //$NON-NLS-1$

	/**
	 * @param part
	 */
	public ResetImageSizeAction(IWorkbenchPart part) {
		super(part);
		setId(ID);
		setText(LABEL);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.actions.Action#isEnabled()
	 */
	public boolean isEnabled() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		if (Policy.TRACING_ACTIONS) {
			System.out.println("Reset image size action >> Run ..."); //$NON-NLS-1$
		}
		SessionHandleAdapter.getInstance().getCommandStack().startTrans(LABEL);
		try {
			CommandUtils.executeCommand("org.eclipse.birt.report.designer.ui.command.resetImageSizeCommand", null); //$NON-NLS-1$
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
		SessionHandleAdapter.getInstance().getCommandStack().commit();
	}

}
