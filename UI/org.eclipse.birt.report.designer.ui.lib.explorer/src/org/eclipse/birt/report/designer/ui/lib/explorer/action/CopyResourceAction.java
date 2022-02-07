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
import java.util.Collection;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.lib.explorer.LibraryExplorerTreeViewPage;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;

/**
 * The action class for copying resources in resource explorer.
 */
public class CopyResourceAction extends ResourceAction {

	/** The clipboard for copying resource. */
	private Clipboard clipboard;

	/**
	 * Constructs an action for copying resource.
	 * 
	 * @param page      the resource explorer page
	 * @param clipboard the clipboard for copying resource
	 */
	public CopyResourceAction(LibraryExplorerTreeViewPage page, Clipboard clipboard) {
		super(Messages.getString("CopyLibraryAction.Text"), page); //$NON-NLS-1$
		this.clipboard = clipboard;
		setId(ActionFactory.COPY.getId());
		setAccelerator(SWT.CTRL | 'C');

		setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_COPY));

		setDisabledImageDescriptor(
				PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_COPY_DISABLED));
	}

	@Override
	public boolean isEnabled() {
		try {
			return !getSelectedFiles().isEmpty();
		} catch (IOException e) {
			return false;
		}
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

		// Get the file names and a string representation
		final int length = files.size();
		int actualLength = 0;
		String[] fileNames = new String[length];

		for (File file : files) {
			IPath location = new Path(file.getAbsolutePath());

			if (location != null) {
				fileNames[actualLength++] = location.toOSString();
			}
		}

		// was one or more of the locations null?
		if (actualLength < length) {
			String[] tempFileNames = fileNames;

			fileNames = new String[actualLength];
			for (int i = 0; i < actualLength; i++) {
				fileNames[i] = tempFileNames[i];
			}
		}
		setClipboard(fileNames);
	}

	/**
	 * Set the clipboard contents. Prompt to retry if clipboard is busy.
	 * 
	 * @param resources the resources to copy to the clipboard
	 * @param fileNames file names of the resources to copy to the clipboard
	 * @param names     string representation of all names
	 */
	private void setClipboard(String[] fileNames) {
		// set the clipboard contents
		if (fileNames.length > 0) {
			clipboard.setContents(new Object[] { fileNames }, new Transfer[] { FileTransfer.getInstance() });
		}
	}
}
