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
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;

import org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceEntry;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.lib.explorer.LibraryExplorerTreeViewPage;
import org.eclipse.birt.report.designer.ui.lib.explorer.dialog.MoveResourceDialog;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.dialogs.SelectionDialog;

/**
 * The action class for moving resources in resource explorer.
 */
public class MoveResourceAction extends ResourceAction {

	/**
	 * Constructs an action for moving resource.
	 * 
	 * @param page the resource explorer page
	 */
	public MoveResourceAction(LibraryExplorerTreeViewPage page) {
		super(Messages.getString("MoveLibraryAction.Text"), page); //$NON-NLS-1$
		setId(ActionFactory.MOVE.getId());
	}

	@Override
	public boolean isEnabled() {
		return canModifySelectedResources();
	}

	@Override
	public void run() {
		Collection<File> files = null;

		try {
			files = getSelectedFiles();
		} catch (IOException e) {
			ExceptionUtil.handle(e);
		}

		if (files == null || files.isEmpty()) {
			return;
		}

		SelectionDialog dialog = new MoveResourceDialog(files);

		if (dialog.open() == Window.OK) {
			Object[] selected = dialog.getResult();

			if (selected != null && selected.length == 1) {
				try {
					ResourceEntry entry = (ResourceEntry) selected[0];
					IPath targetPath = new Path(convertToFile(entry.getURL()).getAbsolutePath());

					for (File file : files) {
						moveFile(file, targetPath.append(file.getName()).toFile());
					}
				} catch (IOException e) {
					ExceptionUtil.handle(e);
				} catch (InvocationTargetException e) {
					ExceptionUtil.handle(e);
				} catch (InterruptedException e) {
					ExceptionUtil.handle(e);
				}
			}
		}
	}

	/**
	 * Moves the specified source file to the specified target file.
	 * 
	 * @param srcFile    the source file.
	 * @param targetFile the target file
	 * @exception InvocationTargetException if the run method must propagate a
	 *                                      checked exception, it should wrap it
	 *                                      inside an
	 *                                      <code>InvocationTargetException</code>;
	 *                                      runtime exceptions and errors are
	 *                                      automatically wrapped in an
	 *                                      <code>InvocationTargetException</code>
	 *                                      by this method
	 * @exception InterruptedException      if the operation detects a request to
	 *                                      cancel, using
	 *                                      <code>IProgressMonitor.isCanceled()</code>,
	 *                                      it should exit by throwing
	 *                                      <code>InterruptedException</code>; this
	 *                                      method propagates the exception
	 */
	private void moveFile(File srcFile, File targetFile) throws InvocationTargetException, InterruptedException {
		if (targetFile.exists()) {
			if (!MessageDialog.openQuestion(getShell(), Messages.getString("MoveResourceAction.Dialog.Title"), //$NON-NLS-1$
					Messages.getString("MoveResourceAction.Dialog.Message"))) //$NON-NLS-1$
			{
				return;
			}

			new ProgressMonitorDialog(getShell()).run(true, true,
					createDeleteRunnable(Arrays.asList(new File[] { targetFile })));
		}

		new ProgressMonitorDialog(getShell()).run(true, true, createRenameFileRunnable(srcFile, targetFile));
	}
}
