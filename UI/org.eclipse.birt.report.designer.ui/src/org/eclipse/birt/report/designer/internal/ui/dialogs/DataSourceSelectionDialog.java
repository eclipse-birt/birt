/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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
import org.eclipse.birt.report.designer.ui.dialogs.BaseDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * When data set miss its data source, this dialog allow user to link with its
 * available data source.
 * 
 */
public class DataSourceSelectionDialog extends BaseDialog {

	private String[] dataSourceNames;
	private Combo combo;

	public DataSourceSelectionDialog(Shell parentShell, String title, String[] names) {
		super(parentShell, title);
		this.dataSourceNames = names;
	}

	/*
	 * @see
	 * org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets
	 * .Composite)
	 */
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		new Label(composite, SWT.NONE).setText(Messages.getString("dataset.editor.label.selectDataSource")); //$NON-NLS-1$
		combo = new Combo(composite, SWT.BORDER | SWT.READ_ONLY);
		combo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		combo.setVisibleItemCount(30);
		combo.setItems(dataSourceNames);

		UIUtil.bindHelp(parent, IHelpContextIds.ADD_DATA_SOURCE_SELECTION_DIALOG_ID);
		return composite;
	}

	/*
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.dialogs.BaseDialog#initDialog ()
	 */
	protected boolean initDialog() {
		if (this.dataSourceNames == null || this.dataSourceNames.length == 0) {
			this.getOkButton().setEnabled(false);
		} else {
			combo.select(0);
		}
		return true;
	}

	/*
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	protected void okPressed() {
		setResult(combo.getItem(combo.getSelectionIndex()));
		super.okPressed();
	}

}
