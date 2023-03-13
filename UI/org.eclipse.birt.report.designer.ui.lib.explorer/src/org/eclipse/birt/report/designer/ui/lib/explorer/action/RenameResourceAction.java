/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.lib.explorer.LibraryExplorerTreeViewPage;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.actions.ActionFactory;

/**
 * The action class for renaming resource in resource explorer.
 */
public class RenameResourceAction extends ResourceAction {

	/**
	 * Constructs an action for renaming resource.
	 *
	 * @param page the resource explorer page
	 */
	public RenameResourceAction(LibraryExplorerTreeViewPage page) {
		super(Messages.getString("RenameLibraryAction.Text"), page); //$NON-NLS-1$
		setId(ActionFactory.RENAME.getId());
	}

	@Override
	public boolean isEnabled() {
		boolean enabled = canModifySelectedResources();
		if (enabled) {
			Collection<?> resources = getSelectedResources();
			if (resources.size() > 1) {
				enabled = false;
			}
		}
		return enabled;
	}

	@Override
	public void run() {
		Collection<File> files = null;

		try {
			files = getSelectedFiles();
		} catch (IOException e) {
			ExceptionUtil.handle(e);
			return;
		}

		if (files == null || files.size() != 1) {
			return;
		}

		File file = files.iterator().next();

		Object adapter = ElementAdapterManager.getAdapter(this, IRenameChecker.class);
		if (adapter != null) {
			boolean saveAndClose = ((IRenameChecker) adapter).renameCheck(file);

			if (!saveAndClose) {
				return;
			}
		}

		String newName = queryNewResourceName(file);

		if (newName == null || newName.length() <= 0) {
			return;
		}

		File newFile = new Path(file.getAbsolutePath()).removeLastSegments(1).append(newName).toFile();

		try {
			new ProgressMonitorDialog(getShell()).run(true, true, createRenameFileRunnable(file, newFile));
		} catch (InvocationTargetException | InterruptedException e) {
			ExceptionUtil.handle(e);
		}
	}

	/**
	 * Returns the new name to be given to the target resource.
	 *
	 * @param resource the resource to query status on
	 * @return the new name
	 */
	protected String queryNewResourceName(final File resource) {
		final IWorkspace workspace = ResourcesPlugin.getWorkspace();
		final IPath prefix = new Path(resource.getAbsolutePath()).removeLastSegments(1);

		IInputValidator validator = new IInputValidator() {

			/*
			 * (non-Javadoc)
			 *
			 * @see org.eclipse.jface.dialogs.IInputValidator#isValid(java.lang.String )
			 */
			@Override
			public String isValid(String string) {
				if (new Path(resource.getName()).removeFileExtension().toFile().getName().equals(string)) {
					return Messages.getString("RenameResourceAction.nameExists"); //$NON-NLS-1$
				}

				IPath newPath = new Path(string);

				IStatus status = workspace.validateName(newPath.toFile().getName(),
						resource.isFile() ? IResource.FILE : IResource.FOLDER);

				if (!status.isOK()) {
					return status.getMessage();
				}

				IPath fullPath = prefix.append(string);

				if (fullPath.toFile().exists()) {
					return Messages.getString("RenameResourceAction.nameExists"); //$NON-NLS-1$
				}
				return null;
			}
		};

		InputDialog dialog = new InputDialog(getShell(), Messages.getString("RenameResourceAction.inputDialogTitle"), //$NON-NLS-1$
				Messages.getString("RenameResourceAction.inputDialogMessage"), //$NON-NLS-1$
				new Path(resource.getName()).toFile().getName(), validator);

		dialog.setBlockOnOpen(true);
		int result = dialog.open();
		if (result == Window.OK) {
			IPath newPath = new Path(dialog.getValue());

			return newPath.toFile().getName();
		}
		return null;
	}
}
