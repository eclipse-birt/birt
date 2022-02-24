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

package org.eclipse.birt.report.designer.internal.ui.dialogs;

import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.BaseDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * Template Report Item Properties Dialog.
 */

public class TemplateReportItemPropertiesDialog extends BaseDialog {

	private static final String DIALOG_TITLE = Messages.getString("TemplateReportItemPropertiesDialog.title"); //$NON-NLS-1$

	private static final String GROUP_TITLE = Messages.getString("TemplateReportItemPropertiesDialog.group"); //$NON-NLS-1$

	private static final String LABEL_OBJECT_TYPE = Messages.getString("TemplateReportItemPropertiesDialog.objectType"); //$NON-NLS-1$

	private static final String LABEL_PROMPT_TEXT = Messages.getString("TemplateReportItemPropertiesDialog.promptText"); //$NON-NLS-1$

	private static final String LABEL_NAME_TEXT = Messages.getString("TemplateReportItemPropertiesDialog.nameText");//$NON-NLS-1$
	// private static final String ERROR_TITLE =
	// Messages.getString("TemplateReportItemPropertiesDialog.errorTitle");
	// //$NON-NLS-1$
	//
	// private static final String ERROR_MESSAGE =
	// Messages.getString("TemplateReportItemPropertiesDialog.errorMessage");
	// //$NON-NLS-1$

	private String objectType;
	private String defaultPromptText;
	private Text promptText;
	private String defaultNameText;
	private Text nameText;
	private String name;

	public TemplateReportItemPropertiesDialog(String objectType, String defaultPromptText) {
		super(DIALOG_TITLE);
		this.objectType = objectType;
		this.defaultPromptText = defaultPromptText;
	}

	public TemplateReportItemPropertiesDialog(String objectType, String defaultNameText, String defaultPromptText) {
		this(objectType, defaultPromptText);
		this.defaultNameText = defaultNameText;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.
	 * Composite)
	 */
	protected Control createDialogArea(Composite parent) {
		UIUtil.bindHelp(parent, IHelpContextIds.CREATE_TEMPLATE_REPORT_ITEM_DIALOG_ID);
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginWidth = 10;
		// gridLayout.marginTop = 7;
		container.setLayout(gridLayout);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.widthHint = 400;
		container.setLayoutData(gridData);

		Group group = new Group(container, SWT.NONE);
		group.setLayout(new GridLayout());
		group.setLayoutData(new GridData(GridData.FILL_BOTH));
		group.setText(GROUP_TITLE);

		Composite groupContainer = new Composite(group, SWT.NONE);
		gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gridLayout.marginWidth = 7;
		gridLayout.marginHeight = 7;
		gridLayout.verticalSpacing = 10;
		groupContainer.setLayout(gridLayout);
		groupContainer.setLayoutData(new GridData(GridData.FILL_BOTH));

		new Label(groupContainer, SWT.NONE).setText(LABEL_OBJECT_TYPE);
		new Label(groupContainer, SWT.NONE).setText(objectType);

		Label nameLabel = new Label(groupContainer, SWT.NONE);
		nameLabel.setText(LABEL_NAME_TEXT);
		nameLabel.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));

		nameText = new Text(groupContainer, SWT.BORDER);
		nameText.setText(defaultNameText);
		nameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL));

		Label promptLable = new Label(groupContainer, SWT.NONE);
		promptLable.setText(LABEL_PROMPT_TEXT);
		promptLable.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));

		promptText = new Text(groupContainer, SWT.BORDER | SWT.MULTI);
		promptText.setText(defaultPromptText);

		gridData = new GridData(GridData.FILL_BOTH);

		gridData.heightHint = 60;
		promptText.setLayoutData(gridData);

		return container;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	protected void okPressed() {
		// if ( promptText.getText( ) == null
		// || promptText.getText( ).trim( ).length( ) == 0 )
		// {
		// ExceptionHandler.openErrorMessageBox(ERROR_TITLE,ERROR_MESSAGE);
		// promptText.forceFocus();
		// }
		// else
		// {
		setResult(promptText.getText());
		setName(nameText.getText());
		super.okPressed();
		// }

	}

	/**
	 * Set the name.
	 * 
	 * @param value
	 */
	final protected void setName(String value) {
		name = value;
	}

	/**
	 * Get the name.
	 * 
	 * @param value
	 */
	public String getName() {
		return name;
	}
}
