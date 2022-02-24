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

package org.eclipse.birt.chart.integration.wtp.ui.internal.dialogs;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

/**
 * Create folder selection group.
 * <p>
 * This group contains: Lable, Text , Button
 */
public class FolderSelectionGroup {

	/**
	 * label of group
	 */
	protected Label label;

	/**
	 * text of group
	 */
	protected Text text;

	/**
	 * button of group
	 */
	protected Button button;

	/**
	 * dialog of group
	 */
	protected DirectoryDialog dialog;

	/**
	 * Text of label
	 */
	private String labelText = "Default Label"; //$NON-NLS-1$

	/**
	 * DefaultValue of text
	 */
	private String textValue = ""; //$NON-NLS-1$

	/**
	 * Text of button
	 */
	private String buttonText = "Select..."; //$NON-NLS-1$

	/**
	 * Title of DirectoryDialog
	 */
	private String dialogTitle = "Browse For Folder"; //$NON-NLS-1$

	/**
	 * Message of DirectoryDialog
	 */
	private String dialogMessage = "Please select the folder:"; //$NON-NLS-1$

	/**
	 * FilterPath of DirectoryDialog
	 */
	private String dialogFilterPath = ""; //$NON-NLS-1$

	/**
	 * DELIMITER Contants
	 */
	private final static String DELIMITER = File.pathSeparator;

	/**
	 * Default Constructor
	 * 
	 */
	public FolderSelectionGroup() {
	}

	/**
	 * Constructor
	 * 
	 * @param labelText
	 * @param buttonText
	 * @param dialogTitle
	 * @param dialogMessage
	 * @param dialogFilterPath
	 */
	public FolderSelectionGroup(String labelText, String buttonText, String dialogTitle, String dialogMessage,
			String dialogFilterPath) {
		this.labelText = labelText;
		this.buttonText = buttonText;
		this.dialogTitle = dialogTitle;
		this.dialogMessage = dialogMessage;
		this.dialogFilterPath = dialogFilterPath;
	}

	/**
	 * Create group
	 * 
	 * @param parent
	 */
	public void create(Composite parent) {
		// get font
		Font font = parent.getFont();

		// label control
		Label label = new Label(parent, SWT.LEFT);
		label.setFont(font);
		label.setText(this.labelText);

		Composite composite = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.numColumns = 2;
		composite.setLayout(layout);

		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		composite.setLayoutData(data);

		// text control
		text = new Text(composite, SWT.BORDER);
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		text.setFont(font);
		text.setText(this.textValue);
		text.addVerifyListener(new VerifyListener() {

			public void verifyText(VerifyEvent e) {
				e.doit = e.text.indexOf(DELIMITER) < 0;
			}
		});

		// directory selection button
		button = new Button(composite, SWT.PUSH);
		button.setFont(font);
		button.setText(this.buttonText);
		button.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent event) {
				dialog = new DirectoryDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell());

				dialog.setText(dialogTitle);
				dialog.setMessage(dialogMessage);
				dialog.setFilterPath(dialogFilterPath);
				String folderName = dialog.open();
				if (folderName == null) {
					return;
				}
				text.setText(folderName);
			}
		});
	}

	/**
	 * @return the buttonText
	 */
	public String getButtonText() {
		return buttonText;
	}

	/**
	 * @param buttonText the buttonText to set
	 */
	public void setButtonText(String buttonText) {
		this.buttonText = buttonText;
	}

	/**
	 * @return the dialogFilterPath
	 */
	public String getDialogFilterPath() {
		return dialogFilterPath;
	}

	/**
	 * @param dialogFilterPath the dialogFilterPath to set
	 */
	public void setDialogFilterPath(String dialogFilterPath) {
		this.dialogFilterPath = dialogFilterPath;
	}

	/**
	 * @return the dialogMessage
	 */
	public String getDialogMessage() {
		return dialogMessage;
	}

	/**
	 * @param dialogMessage the dialogMessage to set
	 */
	public void setDialogMessage(String dialogMessage) {
		this.dialogMessage = dialogMessage;
	}

	/**
	 * @return the dialogTitle
	 */
	public String getDialogTitle() {
		return dialogTitle;
	}

	/**
	 * @param dialogTitle the dialogTitle to set
	 */
	public void setDialogTitle(String dialogTitle) {
		this.dialogTitle = dialogTitle;
	}

	/**
	 * @return the labelText
	 */
	public String getLabelText() {
		return labelText;
	}

	/**
	 * @param labelText the labelText to set
	 */
	public void setLabelText(String labelText) {
		this.labelText = labelText;
	}

	/**
	 * @return the button
	 */
	public Button getButton() {
		return button;
	}

	/**
	 * @return the dialog
	 */
	public DirectoryDialog getDialog() {
		return dialog;
	}

	/**
	 * @return the label
	 */
	public Label getLabel() {
		return label;
	}

	/**
	 * @return the text
	 */
	public Text getText() {
		return text;
	}

	/**
	 * @return the textValue
	 */
	public String getTextValue() {
		return textValue;
	}

	/**
	 * @param textValue the textValue to set
	 */
	public void setTextValue(String textValue) {
		this.textValue = textValue;
	}
}
