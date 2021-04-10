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

package org.eclipse.birt.report.designer.internal.ui.dialogs.resource;

import java.util.ArrayList;

import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * 
 */

public class AddResourceFileFolderSelectionDialog extends ResourceFileFolderSelectionDialog {

	ArrayList existFilesList = new ArrayList();

	private final String[] FILENAME_PATTERN;
	private final String[] FILENAME_SUFFIX;

	private Status ErrorStatusExist = new Status(IStatus.ERROR, ReportPlugin.REPORT_UI, IStatus.ERROR,
			Messages.getString("AddJarResourceFileFolderSelectionDialog.ErrorMessage.Exist"), //$NON-NLS-1$
			null);

	/**
	 * @param parent
	 * @param labelProvider
	 * @param contentProvider
	 */
	public AddResourceFileFolderSelectionDialog(String[] pattern, String[] suffix) {
		super(true, pattern);
		this.setEmptyFolderShowStatus(IResourceContentProvider.ALWAYS_NOT_SHOW_EMPTYFOLDER);
		this.FILENAME_PATTERN = pattern;
		this.FILENAME_SUFFIX = suffix;
		setValidator(new ResourceSelectionValidator(true, false, FILENAME_SUFFIX) {

			public IStatus validate(Object[] selection) {
				int nSelected = selection.length;

				for (int i = 0; i < nSelected; i++) {
					String selectedName = getPath(i);
					if (selectedName != null) {
						int index = existFilesList.indexOf(selectedName);
						if (index >= 0) {
							return ErrorStatusExist;
						}
					}
				}
				return super.validate(selection);
			}

		});
		setAllowMultiple(true);

		// String fileSuf = "";
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < FILENAME_SUFFIX.length; i++) {
			if (i == FILENAME_SUFFIX.length - 1 && FILENAME_SUFFIX.length >= 2) {
//				fileSuf = fileSuf
//						+ " "
//						+ Messages.getString( "AddJarResourceFileFolderSelectionDialog.Message.Or" );
				buffer.append(" " + Messages.getString("AddJarResourceFileFolderSelectionDialog.Message.Or"));
			}
			if (i != 0) {
				// fileSuf = fileSuf + " ";
				buffer.append(" ");
			}
			// fileSuf = fileSuf + FILENAME_SUFFIX[i];
			buffer.append(FILENAME_SUFFIX[i]);
		}
		String fileSuf = buffer.toString();
		setTitle(Messages.getFormattedString("ModulePage.Resourcefile.Dialog.Title", new String[] { fileSuf })); //$NON-NLS-1$
		setMessage(Messages.getFormattedString("AddJarResourceFileFolderSelectionDialog.Message", //$NON-NLS-1$
				new String[] { fileSuf }));
	}

	public void setExistFiles(String[] existFiles) {
		for (int i = 0; i < existFiles.length; i++) {
			existFilesList.add(existFiles[i]);
		}
	}

	/*
	 * @see Dialog#createDialogArea(Composite)
	 */
	protected Control createDialogArea(Composite parent) {
		Control control = super.createDialogArea(parent);
		// UIUtil.bindHelp( parent, IHelpContextIds.ADD_JAR_FILES_DIALOG_ID );
		if (helpDialogId != null) {
			UIUtil.bindHelp(parent, helpDialogId);
		}
		return control;
	}

	private String helpDialogId = null;

	public void setHelpDialogId(String id) {
		this.helpDialogId = id;
	}

	public String getHelpDialogId() {
		return helpDialogId;
	}
}
