/*******************************************************************************
 * Copyright (c) 2004-2008 Actuate Corporation.
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

package org.eclipse.birt.report.designer.ui.lib.explorer.action;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.lib.explorer.LibraryExplorerTreeViewPage;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;

/**
 * The action class for removing resources in resource explorer.
 */
public class DeleteResourceAction extends ResourceAction {

	/**
	 * Constructs an action for removing resource.
	 *
	 * @param page the resource explorer page
	 */
	public DeleteResourceAction(LibraryExplorerTreeViewPage page) {
		super(Messages.getString("DeleteLibraryAction.Text"), page); //$NON-NLS-1$
		setId(ActionFactory.DELETE.getId());
		setAccelerator(SWT.DEL);

		setImageDescriptor(
				PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));

		setDisabledImageDescriptor(
				PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_DELETE_DISABLED));
	}

	@Override
	public boolean isEnabled() {
		return canModifySelectedResources();
	}

	@Override
	public void run() {
		if (!MessageDialog.openQuestion(getShell(), Messages.getString("DeleteResourceAction.Dialog.Title"), //$NON-NLS-1$
				Messages.getString("DeleteResourceAction.Dialog.Message"))) //$NON-NLS-1$
		{
			return;
		}

		try {
			new ProgressMonitorDialog(getShell()).run(true, true, createDeleteRunnable(getSelectedFiles()));
		} catch (InvocationTargetException | InterruptedException | IOException e) {
			ExceptionUtil.handle(e);
		}
	}
}
