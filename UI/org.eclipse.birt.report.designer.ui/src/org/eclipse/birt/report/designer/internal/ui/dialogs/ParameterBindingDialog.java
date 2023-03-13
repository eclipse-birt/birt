/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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

import java.util.ArrayList;

import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.BaseDialog;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * Dialog used for parameter binding
 */
public class ParameterBindingDialog extends BaseDialog {

	ArrayList items = new ArrayList();

	/**
	 * @param parentShell
	 */
	public ParameterBindingDialog(Shell parentShell, DesignElementHandle model) {
		super(parentShell, null);
		items.add(model);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets
	 * .Composite)
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		UIUtil.bindHelp(parent, IHelpContextIds.PARAMETERBINDING_DIALOG_ID);
		Composite composite = (Composite) super.createDialogArea(parent);
		ParameterBindingPage page = new ParameterBindingPage(composite, SWT.NONE);
		page.setEnableAutoCommit(false);
		page.setInput(items);

		GridData data = new GridData(GridData.FILL_BOTH);
		data.heightHint = 300;
		page.setLayoutData(data);
		this.setTitle(Messages.getString("ParameterBindingDialog.title")); //$NON-NLS-1$
		return composite;
	}
}
