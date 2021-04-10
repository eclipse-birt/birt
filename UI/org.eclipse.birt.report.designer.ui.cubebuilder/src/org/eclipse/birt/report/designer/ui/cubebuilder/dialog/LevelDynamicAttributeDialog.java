/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.cubebuilder.dialog;

import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.ui.cubebuilder.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.BaseDialog;
import org.eclipse.birt.report.designer.ui.util.UIUtil;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

/**
 * 
 */

public class LevelDynamicAttributeDialog extends BaseDialog {

	private Combo memberCombo;
	private String[] items;
	private String item;

	public LevelDynamicAttributeDialog(String title) {
		super(title);
	}

	public void setInput(String[] items) {
		this.items = items;
	}

	public void setInput(String[] items, String item) {
		this.items = items;
		this.item = item;
	}

	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);

		Composite container = new Composite(composite, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginWidth = layout.marginHeight = 0;
		container.setLayout(layout);

		Label nameLabel = new Label(container, SWT.WRAP);
		nameLabel.setText(Messages.getString("LevelDynamicAttributeDialog.Label.Member")); //$NON-NLS-1$
		nameLabel.setLayoutData(new GridData());
		nameLabel.setFont(parent.getFont());

		memberCombo = new Combo(container, SWT.BORDER | SWT.READ_ONLY);
		memberCombo.setVisibleItemCount(30);
		GridData gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL);
		gd.widthHint = 250;
		memberCombo.setLayoutData(gd);
		memberCombo.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				checkButtonStatus();
			}
		});

		applyDialogFont(composite);

		UIUtil.bindHelp(parent, IHelpContextIds.LEVEL_DYNAMIC_ATTRIBUTE_DIALOG);

		initDialog();

		return composite;
	}

	protected boolean initDialog() {
		if (items != null) {
			memberCombo.setItems(items);
		}
		if (item != null) {
			memberCombo.setText(item);
		}
		return super.initDialog();
	}

	protected Control createButtonBar(Composite parent) {
		Control bar = super.createButtonBar(parent);
		checkButtonStatus();
		return bar;
	}

	private void checkButtonStatus() {
		if (memberCombo.getText().trim().length() == 0) {
			if (getButton(IDialogConstants.OK_ID) != null) {
				getButton(IDialogConstants.OK_ID).setEnabled(false);
			}
		} else {
			if (getButton(IDialogConstants.OK_ID) != null) {
				getButton(IDialogConstants.OK_ID).setEnabled(true);
			}
		}
	}

	protected void okPressed() {
		setResult(memberCombo.getText());
		super.okPressed();
	}
}
