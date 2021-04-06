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

import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.views.RenameInputDialog;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.lib.explorer.LibraryExplorerTreeViewPage;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;

/**
 * The action class for pasting resources in resource explorer.
 */
public class PasteResourceAction extends ResourceAction {

	/** The clipboard for pasting resource. */
	private final Clipboard clipboard;

	/**
	 * Constructs an action for pasting resource.
	 * 
	 * @param page      the resource explorer page
	 * @param clipboard the clipboard for pasting resource
	 */
	public PasteResourceAction(LibraryExplorerTreeViewPage page, Clipboard clipboard) {
		super(Messages.getString("PasteLibraryAction.Text"), page); //$NON-NLS-1$
		this.clipboard = clipboard;
		setId(ActionFactory.PASTE.getId());
		setAccelerator(SWT.CTRL | 'V');

		setImageDescriptor(
				PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_PASTE));

		setDisabledImageDescriptor(
				PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_PASTE_DISABLED));
	}

	@Override
	public boolean isEnabled() {
		FileTransfer fileTransfer = FileTransfer.getInstance();
		String[] fileData = (String[]) clipboard.getContents(fileTransfer);

		if (fileData != null && fileData.length > 0) {
			try {
				return canInsertIntoSelectedContainer();
			} catch (IOException e) {
				return false;
			}
		}
		return false;
	}

	@Override
	public void run() {
		// try a file transfer
		FileTransfer fileTransfer = FileTransfer.getInstance();
		String[] fileData = (String[]) clipboard.getContents(fileTransfer);

		if (fileData != null && fileData.length > 0) {
			File container;

			try {
				container = getSelectedContainer();
			} catch (IOException e) {
				ExceptionUtil.handle(e);
				return;
			}

			if (container == null) {
				return;
			}

			for (String filename : fileData) {
				final File srcFile = new File(filename);
				File targetFile = new File(container, srcFile.getName());

				if (targetFile.exists()) {
					String[] existedNames = container.list();
					RenameInputDialog inputDialog = new RenameInputDialog(getShell(),
							Messages.getString("PasteResourceAction.Dialog.Title"), //$NON-NLS-1$
							Messages.getString("PasteResourceAction.Dialog.Message"), //$NON-NLS-1$
							Messages.getString("PasteResourceAction.Dialog.FilenameSuffix.CopyOf") + " " //$NON-NLS-1$ //$NON-NLS-2$
									+ srcFile.getName(), existedNames, IHelpContextIds.RENAME_INPUT_DIALOG_ID);
					inputDialog.create();

					if (inputDialog.open() == Window.OK) {
						targetFile = new File(container, inputDialog.getResult().toString().trim());
					} else
						return;

					// No need to check the attribute of the target file for the
					// target file doesn't exist at this point.
					// if ( !targetFile.canWrite( ) )
					// {
					// MessageDialog.openError( getShell( ),
					// Messages.getString( "PasteResourceAction.ReadOnlyEncounter.Title" ),
					// //$NON-NLS-1$
					// Messages.getFormattedString( "PasteResourceAction.ReadOnlyEncounter.Message",
					// //$NON-NLS-1$
					// new Object[]{
					// targetFile.getAbsolutePath( )
					// } ) );
					// return;
					// }
				}
				doCopy(srcFile, targetFile);
			}
		}
	}

	/**
	 * Copies files in a monitor dialog.
	 * 
	 * @param srcFile    the source file
	 * @param targetFile the target file
	 */
	private void doCopy(final File srcFile, final File targetFile) {
		try {
			new ProgressMonitorDialog(getShell()).run(true, true, createCopyFileRunnable(srcFile, targetFile));
		} catch (InvocationTargetException e) {
			ExceptionUtil.handle(e);
		} catch (InterruptedException e) {
			ExceptionUtil.handle(e);
		}
	}
}
