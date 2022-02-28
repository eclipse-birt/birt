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

package org.eclipse.birt.report.designer.internal.ui.editors.wizards;

import java.io.File;

import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.editors.IReportEditorContants;
import org.eclipse.birt.report.designer.ui.util.UIUtil;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPathEditorInput;

/**
 *
 */

public class WizardSaveAsPage extends WizardPage {

	private NewReportPageSupport support;

	private static final String MSG_EMPTY_FILE_LOCATION_DIRECTORY = Messages
			.getString("WizardNewReportCreationPage.msg.empty.file.locationDirectory"); //$NON-NLS-1$
	private static final String MSG_EMPTY_FILE_NAME = Messages
			.getString("WizardNewReportCreationPage.msg.empty.file.name"); //$NON-NLS-1$

	private Listener locationModifyListener = new Listener() {

		@Override
		public void handleEvent(Event e) {
			validatePage();
			getContainer().updateButtons();
		}
	};

	public WizardSaveAsPage(String pageName) {
		super(pageName);
		support = new NewReportPageSupport();
	}

	@Override
	public void createControl(Composite parent) {

		Composite composite = support.createComposite(parent);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		composite.setFont(parent.getFont());

		support.getFileNameField().setFocus();
		support.getFileNameField().addListener(SWT.Modify, locationModifyListener);
		support.getLocationPathField().addListener(SWT.Modify, locationModifyListener);

		setControl(composite);
		UIUtil.bindHelp(getControl(), IHelpContextIds.SAVE_AS_WIZARD_ID);
	}

	public void setOriginalFile(IEditorInput input) {
		String container = ((IPathEditorInput) input).getPath().removeLastSegments(1).toOSString();
		support.setInitialFileLocation(container);
		support.setInitialFileName(input.getName());
	}

	public boolean validatePage() {
		if (support.getFileName().equals(""))//$NON-NLS-1$
		{
			setErrorMessage(null);
			setMessage(MSG_EMPTY_FILE_NAME);
			return false;
		}

		String location = support.getFileLocationFullPath().toOSString();

		if (location.equals("")) //$NON-NLS-1$
		{
			setErrorMessage(null);
			setMessage(MSG_EMPTY_FILE_LOCATION_DIRECTORY);
			return false;
		}

		setErrorMessage(null);
		return true;
	}

	public IPath getResult() {

		IPath path = support.getFileLocationFullPath().append(support.getFileName());

		// If the user does not supply a file extension and if the save
		// as dialog was provided a default file name append the extension
		// of the default filename to the new name
		if (ReportPlugin.getDefault().isReportDesignFile(support.getInitialFileName())
				&& !ReportPlugin.getDefault().isReportDesignFile(path.toOSString())) {
			String[] parts = support.getInitialFileName().split("\\."); //$NON-NLS-1$
			path = path.addFileExtension(parts[parts.length - 1]);
		} else if (support.getInitialFileName().endsWith(IReportEditorContants.TEMPLATE_FILE_EXTENTION)
				&& !path.toOSString().endsWith(IReportEditorContants.TEMPLATE_FILE_EXTENTION)) {
			path = path.addFileExtension("rpttemplate"); //$NON-NLS-1$
		}
		// If the path already exists then confirm overwrite.
		File file = path.toFile();
		if (file.exists()) {
			String[] buttons = { IDialogConstants.YES_LABEL, IDialogConstants.NO_LABEL, IDialogConstants.CANCEL_LABEL };

			String question = Messages.getFormattedString("SaveAsDialog.overwriteQuestion", //$NON-NLS-1$
					new Object[] { path.toOSString() });
			MessageDialog d = new MessageDialog(getShell(), Messages.getString("SaveAsDialog.Question"), //$NON-NLS-1$
					null, question, MessageDialog.QUESTION, buttons, 0);
			int overwrite = d.open();
			switch (overwrite) {
			case 0: // Yes
				break;
			case 1: // No
				return null;
			case 2: // Cancel
			default:
				return Path.EMPTY;
			}
		}

		return path;
	}
}
