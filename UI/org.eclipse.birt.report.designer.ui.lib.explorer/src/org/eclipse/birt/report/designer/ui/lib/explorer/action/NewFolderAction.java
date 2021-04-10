/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.lib.explorer.action;

import java.io.File;
import java.io.IOException;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.lib.explorer.LibraryExplorerTreeViewPage;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;

/**
 * The action class for creating a folder in resource explorer.
 */
public class NewFolderAction extends ResourceAction {

	/**
	 * Constructs an action for creating folder.
	 * 
	 * @param page the resource explorer page
	 */
	public NewFolderAction(LibraryExplorerTreeViewPage page) {
		super(Messages.getString("NewFolderAction.Text"), page); //$NON-NLS-1$
		setImageDescriptor(ReportPlatformUIImages.getImageDescriptor(IReportGraphicConstants.ICON_NEW_FOLDER));
	}

	@Override
	public boolean isEnabled() {
		try {
			return canInsertIntoSelectedContainer();
		} catch (IOException e) {
			return false;
		}
	}

	@Override
	public void run() {
		File container = null;

		try {
			container = getSelectedContainer();
		} catch (IOException e) {
			ExceptionUtil.handle(e);
			return;
		}

		if (container == null) {
			return;
		}

		String newName = queryNewResourceName(container);

		if (newName == null || newName.length() <= 0) {
			return;
		}

		File newFolder = new File(container, newName);

		if (newFolder.mkdir()) {
			fireResourceChanged(newFolder.getAbsolutePath());
		}
	}

	/**
	 * Returns the new name to be given to the target resource.
	 * 
	 * @param container the container to query status on
	 * @return the new name to be given to the target resource.
	 */
	private String queryNewResourceName(final File container) {
		final IWorkspace workspace = ResourcesPlugin.getWorkspace();

		IInputValidator validator = new IInputValidator() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.jface.dialogs.IInputValidator#isValid(java.lang.String)
			 */
			public String isValid(String string) {
				if (string == null || string.length() <= 0) {
					return Messages.getString("NewFolderAction.emptyName"); //$NON-NLS-1$
				}

				File newPath = new File(container, string);

				if (newPath.exists()) {
					return Messages.getString("NewFolderAction.nameExists"); //$NON-NLS-1$
				}

				IStatus status = workspace.validateName(newPath.getName(), IResource.FOLDER);

				if (!status.isOK()) {
					return status.getMessage();
				}
				return null;
			}
		};

		InputDialog dialog = new InputDialog(getShell(), Messages.getString("NewFolderAction.inputDialogTitle"), //$NON-NLS-1$
				Messages.getString("NewFolderAction.inputDialogMessage"), //$NON-NLS-1$
				"", //$NON-NLS-1$
				validator);

		dialog.setBlockOnOpen(true);
		int result = dialog.open();
		if (result == Window.OK) {
			return dialog.getValue();
		}
		return null;
	}
}
