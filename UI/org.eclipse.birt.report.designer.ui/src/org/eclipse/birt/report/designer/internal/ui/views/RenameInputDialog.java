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

package org.eclipse.birt.report.designer.internal.ui.views;

import java.util.Arrays;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.BaseDialog;
import org.eclipse.birt.report.designer.ui.util.UIUtil;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * The dialog used to rename action's input
 */
public class RenameInputDialog extends BaseDialog {

	/**
	 * The message of the dialog.
	 */
	private String message;

	/**
	 * The input value.
	 */
	private String value;

	/**
	 * The help context ID.
	 */
	private String helpContextID;

	/**
	 * Input text widget.
	 */
	private Text text;

	/**
	 * Error message label widget.
	 */
	private String errorMessage;

	/**
	 * Error message label widget.
	 */
	private Text errorMessageText;

	private String[] existedNames;

	/**
	 * @param parentShell
	 * @param dialogTitle
	 * @param dialogMessage
	 * @param initialValue
	 * @param helpContextID
	 */
	public RenameInputDialog(Shell parentShell, String dialogTitle, String dialogMessage, String initialValue,
			String helpContextID) {
		super(dialogTitle);
		this.message = dialogMessage;
		this.value = initialValue;
		this.helpContextID = helpContextID;
	}

	/**
	 * @param parentShell
	 * @param dialogTitle
	 * @param dialogMessage
	 * @param initialValue
	 * @param existedNames
	 * @param helpContextID
	 */
	public RenameInputDialog(Shell parentShell, String dialogTitle, String dialogMessage, String initialValue,
			String[] existedNames, String helpContextID) {
		this(parentShell, dialogTitle, dialogMessage, initialValue, helpContextID);
		this.existedNames = existedNames;
	}

	// /*
	// * (non-Javadoc)
	// *
	// * @see
	// * org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets
	// * .Shell)
	// */
	// protected void configureShell( Shell shell )
	// {
	// super.configureShell( shell );
	// if ( title != null )
	// {
	// shell.setText( title );
	// }
	// }

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);

		Composite container = new Composite(composite, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		layout.marginWidth = layout.marginHeight = 0;
		container.setLayout(layout);
		if (message != null) {
			Label label = new Label(container, SWT.WRAP);
			label.setText(message);
			label.setLayoutData(new GridData());
			label.setFont(parent.getFont());
		}

		text = new Text(container, SWT.BORDER | SWT.SINGLE);
		text.setText(value);
		text.selectAll();

		GridData gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL);
		gd.horizontalSpan = 2;
		gd.widthHint = 250;
		text.setLayoutData(gd);
		text.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				String textName = text.getText().trim();
				if (textName.length() == 0) {
					getButton(IDialogConstants.OK_ID).setEnabled(false);
					setErrorMessage(Messages.getString("RenameInputDialog.Message.BlankName")); //$NON-NLS-1$
				} else if (existedNames != null && Arrays.asList(existedNames).contains(textName)) {
					getButton(IDialogConstants.OK_ID).setEnabled(false);
					setErrorMessage(Messages.getString("RenameInputDialog.Message.DuplicateName")); //$NON-NLS-1$
				} else {
					getButton(IDialogConstants.OK_ID).setEnabled(true);
					setErrorMessage(null);
				}
			}
		});

		errorMessageText = new Text(container, SWT.READ_ONLY | SWT.WRAP);
		gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL);
		gd.horizontalSpan = 3;
		errorMessageText.setLayoutData(gd);
		errorMessageText.setBackground(errorMessageText.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));

		setErrorMessage(errorMessage);

		applyDialogFont(composite);

		UIUtil.bindHelp(parent, helpContextID);

		return composite;
	}

	/**
	 * @param errorMessage
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
		if (errorMessageText != null && !errorMessageText.isDisposed()) {
			errorMessageText.setText(errorMessage == null ? " \n " : errorMessage); //$NON-NLS-1$

			boolean hasError = errorMessage != null && (StringConverter.removeWhiteSpaces(errorMessage)).length() > 0;
			errorMessageText.setEnabled(hasError);
			errorMessageText.setVisible(hasError);
			errorMessageText.getParent().update();

			Control button = getButton(IDialogConstants.OK_ID);
			if (button != null) {
				button.setEnabled(errorMessage == null);
			}
		}
	}

	@Override
	protected void okPressed() {
		setResult(text.getText());
		super.okPressed();
	}

	@Override
	protected Control createContents(Composite parent) {
		Control composite = super.createContents(parent);
		if (text.getText().trim().length() == 0) {
			getButton(IDialogConstants.OK_ID).setEnabled(false);
		} else if (existedNames != null && Arrays.asList(existedNames).contains(text.getText().trim())) {
			getButton(IDialogConstants.OK_ID).setEnabled(false);
		}
		return composite;
	}

}
