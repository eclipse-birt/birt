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

package org.eclipse.birt.report.designer.internal.ui.views.actions;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;

/**
 * 
 */

public class RemoveLibraryAction extends AbstractElementAction {

	private static final String ACTION_TEXT = Messages.getString("RemoveLibraryAction.Text"); //$NON-NLS-1$
	private static final String CONFIRM_LIBRARY_REMOVE_TITLE = Messages.getString("RemoveLibraryAction.config.Title"); //$NON-NLS-1$
	private static final String CONFIRM_LIBRARY_REMOVE_MESSAGE = Messages
			.getString("RemoveLibraryAction.config.Message"); //$NON-NLS-1$

	public RemoveLibraryAction(Object selectedObject) {
		super(selectedObject, ACTION_TEXT);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.actions.
	 * AbstractElementAction#doAction()
	 */
	protected boolean doAction() throws Exception {
		if (MessageDialog.openQuestion(PlatformUI.getWorkbench().getDisplay().getActiveShell(),
				CONFIRM_LIBRARY_REMOVE_TITLE, CONFIRM_LIBRARY_REMOVE_MESSAGE)) {
			SessionHandleAdapter.getInstance().getReportDesignHandle().dropLibrary((LibraryHandle) getSelection());
			return true;
		}
		return false;
	}

}
