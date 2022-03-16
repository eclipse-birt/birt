/*******************************************************************************
 * Copyright (c) 2004, 2014 Actuate Corporation and others.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.internal.rcp.dialogs;

import java.io.File;

import org.eclipse.birt.report.designer.internal.ui.editors.ReportEditorInput;
import org.eclipse.birt.report.designer.internal.ui.editors.wizards.NewReportPageSupport;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.dialogs.BaseTitleAreaDialog;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;

/**
 * SaveAs dialogue for report designer
 */

public class SaveAsDialog extends BaseTitleAreaDialog {

	private static final String DEFAULT_MESSAGE = Messages.getString("SaveAsDialog.message"); //$NON-NLS-1$
	private NewReportPageSupport support;
	private Control okButton;

	private static final String MSG_EMPTY_FILE_LOCATION_DIRECTORY = Messages
			.getString("WizardNewReportCreationPage.msg.empty.file.locationDirectory"); //$NON-NLS-1$
	private static final String MSG_EMPTY_FILE_NAME = Messages
			.getString("WizardNewReportCreationPage.msg.empty.file.name"); //$NON-NLS-1$

	private Listener locationModifyListener = new Listener() {

		@Override
		public void handleEvent(Event e) {
			setDialogComplete(validatePage());
		}
	};
	private IPath result;

	public SaveAsDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.RESIZE);
		support = new NewReportPageSupport();
	}

	/**
	 * Sets the original file to use.
	 *
	 * @param input the original editorInput
	 */
	public void setOriginalFile(IEditorInput input) {
		String container = ((ReportEditorInput) input).getPath().removeLastSegments(1).toOSString();
		support.setInitialFileLocation(container);
		support.setInitialFileName(((ReportEditorInput) input).getName());
	}

	/**
	 * Set the original file name to use. Used instead of
	 * <code>setOriginalFile</code> when the original resource is not an IFile. Must
	 * be called before <code>create</code>.
	 *
	 * @param originalName default file name
	 */
	public void setOriginalName(String originalName) {
		support.setInitialFileName(originalName);
	}

	/**
	 * Returns the full path entered by the user.
	 * <p>
	 * Note that the file and container might not exist and would need to be
	 * created. See the <code>IFile.create</code> method and the
	 * <code>ContainerGenerator</code> class.
	 * </p>
	 *
	 * @return the path, or <code>null</code> if Cancel was pressed
	 */
	@Override
	public IPath getResult() {
		return result;
	}

	/*
	 * (non-Javadoc) Method declared in Window.
	 */
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(Messages.getString("SaveAsDialog.text")); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc) Method declared in Window.
	 */
	@Override
	protected Control createContents(Composite parent) {
		Control contents = super.createContents(parent);
		setTitle(Messages.getString("SaveAsDialog.title")); //$NON-NLS-1$
		Image dlgTitleImage = ReportPlugin.getImage("/icons/wizban/create_report_wizard.gif"); //$NON-NLS-1$
		setTitleImage(dlgTitleImage);
		setMessage(DEFAULT_MESSAGE);
		setDialogComplete(validatePage());
		return contents;
	}

	/**
	 * The <code>SaveAsDialog</code> implementation of this <code>Window</code>
	 * method disposes of the banner image when the dialog is closed.
	 */
	@Override
	public boolean close() {
		support = null;
		return super.close();
	}

	/*
	 * (non-Javadoc) Method declared on Dialog.
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		okButton = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	/*
	 * (non-Javadoc) Method declared on Dialog.
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		// top level composite
		Composite parentComposite = (Composite) super.createDialogArea(parent);

		Composite composite = support.createComposite(parent);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		composite.setFont(parentComposite.getFont());

		support.getFileNameField().setFocus();
		support.getFileNameField().addListener(SWT.Modify, locationModifyListener);
		support.getLocationPathField().addListener(SWT.Modify, locationModifyListener);
		return parentComposite;
	}

	private boolean validatePage() {
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

		setMessage(DEFAULT_MESSAGE);
		setErrorMessage(null);
		return true;
	}

	/**
	 * Sets the completion state of this dialog and adjusts the enable state of the
	 * OK button accordingly.
	 *
	 * @param value <code>true</code> if this dialog is complete, and
	 *              <code>false</code> otherwise
	 */
	protected void setDialogComplete(boolean value) {
		okButton.setEnabled(value);
	}

	/*
	 * (non-Javadoc) Method declared on Dialog.
	 */
	@Override
	protected void okPressed() {
		// Get new path.
		IPath path = support.getFileLocationFullPath().append(support.getFileName());

		// If the user does not supply a file extension and the save
		// as dialog was provided a default file name, then append the extension
		// of the default filename to the new name
		if (!ReportPlugin.getDefault().isReportDesignFile(path.toOSString())) {
			String[] parts = support.getInitialFileName().split("\\."); //$NON-NLS-1$
			path = path.addFileExtension(parts[parts.length - 1]);
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
				return;
			case 2: // Cancel
			default:
				cancelPressed();
				return;
			}
		}

		// Store path and close.
		result = path;
		close();
	}

}
