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

package org.eclipse.birt.report.designer.ui.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.VariableElementHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

/**
 * SelectVariableDialog
 */
public class SelectVariableDialog extends BaseDialog {

	private ReportDesignHandle designHandle;

	private Combo variablesCombo;

	public SelectVariableDialog(ReportDesignHandle designHandle) {
		super(Messages.getString("SelectVariableDialog.Title")); //$NON-NLS-1$
		this.designHandle = designHandle;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite content = new Composite(parent, SWT.NONE);
		content.setLayoutData(GridDataFactory.swtDefaults().hint(300, SWT.DEFAULT).create());
		content.setLayout(GridLayoutFactory.swtDefaults().numColumns(2).margins(15, 15).create());
		new Label(content, SWT.NONE).setText(Messages.getString("SelectVariableDialog.AvailableVariables")); //$NON-NLS-1$
		variablesCombo = new Combo(content, SWT.READ_ONLY);
		variablesCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		variablesCombo.setVisibleItemCount(30);
		variablesCombo.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				validate();
			}
		});
		UIUtil.bindHelp(parent, IHelpContextIds.SELECT_VARIABLE_DIALOG_ID);
		return content;
	}

	@Override
	protected boolean initDialog() {
		List<VariableElementHandle> variables = this.designHandle.getPageVariables();
		List<String> items = new ArrayList<String>();
		items.add(Messages.getString("SelectVariableDialog.ReportSeperator")); //$NON-NLS-1$
		for (VariableElementHandle variable : variables) {
			if (variable.getType() == null || variable.getType().equals(DesignChoiceConstants.VARIABLE_TYPE_REPORT))
				items.add(variable.getName());
		}
		items.add(Messages.getString("SelectVariableDialog.PageSeperator")); //$NON-NLS-1$
		for (VariableElementHandle variable : variables) {
			if (variable.getType() != null && variable.getType().equals(DesignChoiceConstants.VARIABLE_TYPE_PAGE))
				items.add(variable.getName());
		}
		variablesCombo.setItems(items.toArray(new String[items.size()]));
		variablesCombo.select(0);
		validate();
		return true;
	}

	@Override
	protected void okPressed() {
		setResult(variablesCombo.getText());
		super.okPressed();
	}

	private void validate() {
		String value = variablesCombo.getText();
		if (value == null || value.equals("")) //$NON-NLS-1$
			getOkButton().setEnabled(false);
		else if (value.equals(Messages.getString("SelectVariableDialog.ReportSeperator")) //$NON-NLS-1$
				|| value.equals(Messages.getString("SelectVariableDialog.PageSeperator"))) //$NON-NLS-1$
		{
			getOkButton().setEnabled(false);
		} else {
			getOkButton().setEnabled(true);
		}
	}
}
