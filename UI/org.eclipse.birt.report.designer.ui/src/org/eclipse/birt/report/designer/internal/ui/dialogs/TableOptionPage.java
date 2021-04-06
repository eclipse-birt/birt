/*******************************************************************************
 * Copyright (c) 2012 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.dialogs;

import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class TableOptionPage extends WizardPage {

	private TableOptionDialog dialog;

	public TableOptionPage() {
		super(Messages.getString("TableOptionPage.Title")); //$NON-NLS-1$
		this.setTitle(Messages.getString("TableOptionPage.Title")); //$NON-NLS-1$
	}

	public void createControl(Composite parent) {
		Composite pageComp = new Composite(parent, SWT.NONE);
		GridLayout pageCompLayout = new GridLayout();
		pageCompLayout.marginWidth = 10;
		pageCompLayout.marginHeight = 10;
		pageComp.setLayout(pageCompLayout);
		pageComp.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL | GridData.GRAB_VERTICAL
				| GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));

		UIUtil.bindHelp(pageComp, IHelpContextIds.SELECT_DATASET_BINDING_COLUMN);

		pageComp.layout();

		dialog = new TableOptionDialog(UIUtil.getDefaultShell(), true);
		dialog.showDataSetOption(false);
		Control control = dialog.createDialogArea(pageComp);
		control.setLayoutData(new GridData(GridData.FILL_BOTH));
		setControl(pageComp);
	}

	public boolean isPageComplete() {
		return true;
	}

	public Object getResult() {
		if (dialog != null)
			return dialog.getResult();
		return null;
	}

	public void performFinish() {
		dialog.okPressed();
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) {
			getControl().forceFocus();
		}
	}
}
