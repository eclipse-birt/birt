/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.ui.dialogs;

import java.util.logging.Logger;

import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * Base class for dialogs in BIRT, the dialog extends JFace's TrayDialog
 *
 * @since 2.5
 */
public abstract class BaseDialog extends TrayDialog {

	private static final String DIALOG_HEIGHT = "DIALOG_HEIGHT"; //$NON-NLS-1$

	private static final String DIALOG_WIDTH = "DIALOG_WIDTH"; //$NON-NLS-1$

	protected Logger logger = Logger.getLogger(BaseDialog.class.getName());

	private String title;

	private String okLabel = null;

	protected Object result;

	private IDialogSettings dialogSettings;

	/**
	 *
	 * Creates a dialog under the parent shell with the given title and a help
	 * button. This constructor is equivalent to calling
	 * <code>BaseDialog( Shell parentShell, String title, true )</code>.
	 *
	 * @param title the title of the dialog
	 */

	protected BaseDialog(String title) {
		this(UIUtil.getDefaultShell(), title);
	}

	protected BaseDialog(Shell parentShell) {
		this(parentShell, null);
	}

	/**
	 * Creates a dialog under the parent shell with the given title
	 *
	 * @param parentShell the parent shell
	 * @param title       the title of the dialog
	 */

	protected BaseDialog(Shell parentShell, String title) {
		super(parentShell);
		setHelpAvailable(false);
		setShellStyle(getShellStyle() | SWT.RESIZE);
		this.title = title;
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
	@Override
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
	 * Configures the given shell in preparation for opening this window in it.
	 * <p>
	 * The <code>BaseDialog</code> overrides this framework method sets in order to
	 * set the title of the dialog.
	 * </p>
	 *
	 * @param shell the shell
	 */
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		if (title != null) {
			shell.setText(title);
		}
	}

	/**
	 * Sets the title of the dialog
	 */

	public void setTitle(String newTitle) {
		title = newTitle;
		if (getShell() != null) {
			getShell().setText(newTitle);
		}
	}

	/**
	 * Gets the title of the dialog
	 *
	 * @return Returns the title.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Gets the Ok button
	 *
	 * @return Returns the OK button
	 */
	protected Button getOkButton() {
		return getButton(IDialogConstants.OK_ID);
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
	@Override
	protected Button createButton(Composite parent, int id, String label, boolean defaultButton) {
		if (IDialogConstants.OK_ID == id && okLabel != null) {
			return super.createButton(parent, id, okLabel, defaultButton);
		}
		return super.createButton(parent, id, label, defaultButton);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.dialogs.Dialog#getDialogBoundsSettings()
	 */
	@Override
	protected IDialogSettings getDialogBoundsSettings() {
		if (dialogSettings == null) {
			dialogSettings = loadDialogSettings();
		}
		return dialogSettings;
	}

	/**
	 * @return
	 */
	private IDialogSettings loadDialogSettings() {
		if (!needRememberLastSize()) {
			return null;
		}
		IDialogSettings dialogSettings = ReportPlugin.getDefault().getDialogSettings();
		StringBuilder buf = new StringBuilder();
		Shell curShell = getShell();
		while (curShell != null) {
			buf.append(curShell.toString() + '/');
			Composite parent = curShell.getParent();
			if (parent != null) {
				curShell = parent.getShell();
			} else {
				curShell = null;
			}
		}
		if (buf.length() > 0) {
			buf.deleteCharAt(buf.length() - 1);
			String sectionName = buf.toString();
			IDialogSettings setting = dialogSettings.getSection(sectionName);
			if (setting == null) {
				setting = dialogSettings.addNewSection(sectionName);
			}
			return setting;
		} else {
			return dialogSettings;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.window.Window#getInitialSize()
	 */
	@Override
	protected Point getInitialSize() {
		try {
			IDialogSettings setting = getDialogBoundsSettings();
			if (setting != null) {
				int width = setting.getInt(DIALOG_WIDTH);
				int height = setting.getInt(DIALOG_HEIGHT);
				return new Point(width, height);
			}
		} catch (NumberFormatException e) {
		}
		return getDefaultSize();
	}

	/**
	 * Override this method to get the default size of current dialog.
	 *
	 * @return a Point object which encapsulate the width and height of the dialog.
	 */
	protected Point getDefaultSize() {
		return super.getInitialSize();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.dialogs.Dialog#initializeBounds()
	 */
	@Override
	protected void initializeBounds() {
		Shell shell = getShell();
		if (shell != null) {
			if (shell.getDisplay().getDismissalAlignment() == SWT.RIGHT) {
				// make the default button the right-most button
				Button defaultButton = shell.getDefaultButton();
				if (defaultButton != null && isContained(buttonBar, defaultButton)) {
					defaultButton.moveBelow(null);
					((Composite) buttonBar).layout();
				}
			}
		}
		Point size;
		if (!needRememberLastSize()) {
			size = getShell().computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
		} else {
			size = getInitialSize();
		}
		Point location = getInitialLocation(size);
		getShell().setBounds(getConstrainedShellBounds(new Rectangle(location.x, location.y, size.x, size.y)));
	}

	private boolean isContained(Control container, Control control) {
		Composite parent;
		while ((parent = control.getParent()) != null) {
			if (parent == container) {
				return true;
			}
			control = parent;
		}
		return false;
	}

	// if an instance of this dialog needn't remember last size and location,
	// then return false
	protected boolean needRememberLastSize() {
		return false;
	}

}
