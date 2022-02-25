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

import java.util.logging.Level;

import org.eclipse.birt.report.designer.internal.ui.command.CommandUtils;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IEditorPart;

/**
 *
 */

public class RefreshModuleHandleAction extends AbstractViewAction {

	public static final String ID = "org.eclipse.birt.report.designer.internal.ui.views.actions.RefreshModuleHandleAction"; //$NON-NLS-1$
	public static final String ACTION_TEXT = Messages.getString("RefreshModuleHandleAction.Action.Text"); //$NON-NLS-1$

	/**
	 * @param selectedObject
	 */
	public RefreshModuleHandleAction(Object selectedObject) {
		super(selectedObject, ACTION_TEXT);
	}

	/**
	 * @param selectedObject
	 * @param text
	 */
	public RefreshModuleHandleAction(Object selectedObject, String text) {
		super(selectedObject, text);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see isEnabled()
	 */
	@Override
	public boolean isEnabled() {
		if (getSelection() instanceof ReportDesignHandle || getSelection() instanceof LibraryHandle) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		IEditorPart editor = UIUtil.getActiveEditor(true);
		if (editor != null && editor.isDirty()) {
			MessageDialog md = new MessageDialog(UIUtil.getDefaultShell(),
					Messages.getString("RefreshModuleHandleAction.MessageBox.Title"), //$NON-NLS-1$
					null, Messages.getString("RefreshModuleHandleAction.MessageBox.Text"), //$NON-NLS-1$
					MessageDialog.QUESTION_WITH_CANCEL,
					new String[] { Messages.getString("RefreshModuleHandleAction.MessageBox.SaveButton"), //$NON-NLS-1$
							// Messages.getString( "RefreshModuleHandleAction.MessageBox.DiscardButton" ),
							// //$NON-NLS-1$
							Messages.getString("RefreshModuleHandleAction.MessageBox.CancelButton") //$NON-NLS-1$
					}, 0);

			switch (md.open()) {
			case 0:
				try {
					editor.doSave(null);
					CommandUtils.executeCommand("org.eclipse.birt.report.designer.ui.command.refreshLibraryCommand", //$NON-NLS-1$
							null);
				} catch (Exception e) {
					logger.log(Level.SEVERE, e.getMessage(), e);
				}
				break;
			// case 1 :
			// try
			// {
			// CommandUtils.executeCommand(
			// "org.eclipse.birt.report.designer.ui.command.refreshLibraryCommand", null );
			// //$NON-NLS-1$
			// }
			// catch ( Exception e )
			// {
			// logger.log( Level.SEVERE, e.getMessage( ), e );
			// }
			// break;
			default:
			}
		} else {
			try {
				CommandUtils.executeCommand("org.eclipse.birt.report.designer.ui.command.refreshLibraryCommand", null); //$NON-NLS-1$
			} catch (Exception e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
			}
		}
	}
}
