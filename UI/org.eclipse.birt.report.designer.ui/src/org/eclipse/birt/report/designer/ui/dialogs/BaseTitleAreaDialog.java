/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
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

package org.eclipse.birt.report.designer.ui.dialogs;

import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * Provides the base dialog support with a title area
 * 
 * @since 2.5.1
 */
public class BaseTitleAreaDialog extends TitleAreaDialog {

	protected String title;

	private String okLabel = null;

	protected Object result;

	protected String errorMsg = null;

	public void setErrorMessage(String errMsg) {
		super.setErrorMessage(errMsg);
		this.errorMsg = errMsg;
	}

	public void setMessage(String msg, int newType) {
		super.setMessage(msg, newType);

		if (newType == IMessageProvider.ERROR) {
			this.errorMsg = msg;
		} else {
			this.errorMsg = null;
		}
	}

	public void setMessage(String msg) {
		this.setMessage(msg, IMessageProvider.NONE);
	}

	protected void updateButtons() {
		if ((getOkButton() != null) && (errorMsg != null && errorMsg.length() > 0)) {
			getOkButton().setEnabled(false);
		}
	}

	public BaseTitleAreaDialog(Shell parentShell) {
		super(parentShell);
		setHelpAvailable(false);
		setShellStyle(getShellStyle() | SWT.RESIZE);
	}

	/**
	 * Configures the given shell in preparation for opening this window in it.
	 * <p>
	 * The <code>BaseDialog</code> overrides this framework method sets in order to
	 * set the title of the dialog.
	 * </p>
	 * 
	 * @param shell the shell
	 */
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		if (title != null) {
			shell.setText(title);
		}
	}

	/**
	 * Sets the text for OK button.
	 * 
	 * @param label
	 */
	protected void setOkButtonText(String label) {
		okLabel = label;
	}

	/**
	 * Gets the dialog result.
	 * 
	 * @return the dialog result.
	 */
	public Object getResult() {
		return result;
	}

	/**
	 * Sets the dialog result.
	 * 
	 * @param value
	 */
	final protected void setResult(Object value) {
		result = value;
	}

	/**
	 * Gets the Ok button
	 * 
	 * @return Returns the OK button
	 */
	protected Button getOkButton() {
		return getButton(IDialogConstants.OK_ID);
	}

	protected Control createContents(Composite parent) {
		Control control = super.createContents(parent);
		if (title != null) {
			setTitle(title);
			getShell().setText(title);
		}
		return control;
	}

	/**
	 * Initialize the dialog after all controls have been created.The default
	 * implement of this framework method does nothing.Subclassed may override it.
	 * 
	 * @return Returns true if the dialog is initialized correctly, or false if
	 *         failed
	 */
	protected boolean initDialog() {
		// Doing nothing
		return true;
	}

	/**
	 * Opens this window, creating it first if it has not yet been created.
	 * <p>
	 * (<code>BaseDialog</code>) overrides this method to initialize the dialog
	 * after create it. If initializtion failed, the dialog will be treated as
	 * cancel button is pressed
	 * </p>
	 * 
	 * @return the return code
	 * 
	 * @see #create()
	 */
	public int open() {
		if (getShell() == null) {
			// create the window
			create();
		}
		if (initDialog()) {
			if (Policy.TRACING_DIALOGS) {
				String[] result = this.getClass().getName().split("\\."); //$NON-NLS-1$
				System.out.println("Dialog >> Open " //$NON-NLS-1$
						+ result[result.length - 1]);
			}
			return super.open();
		}

		return Dialog.CANCEL;
	}

	/**
	 * Creates a new button with the given id. Override this method to support
	 * custom label for OK button
	 * 
	 * 
	 * @param parent        the parent composite
	 * @param id            the id of the button (see
	 *                      <code>IDialogConstants.*_ID</code> constants for
	 *                      standard dialog button ids)
	 * @param label         the label from the button
	 * @param defaultButton <code>true</code> if the button is to be the default
	 *                      button, and <code>false</code> otherwise
	 * 
	 * @return the new button
	 * 
	 * @see #getCancelButton
	 * @see #getOKButton()
	 */
	protected Button createButton(Composite parent, int id, String label, boolean defaultButton) {
		if (IDialogConstants.OK_ID == id && okLabel != null) {
			return super.createButton(parent, id, okLabel, defaultButton);
		}
		return super.createButton(parent, id, label, defaultButton);
	}
}
