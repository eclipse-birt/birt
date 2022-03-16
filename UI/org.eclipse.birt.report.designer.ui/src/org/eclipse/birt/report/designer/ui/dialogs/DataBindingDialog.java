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

package org.eclipse.birt.report.designer.ui.dialogs;

import java.util.ArrayList;

import org.eclipse.birt.report.designer.internal.ui.dialogs.BindingPage;
import org.eclipse.birt.report.designer.internal.ui.dialogs.IBindingDialogHelper;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * DataBindingDialog
 */
public class DataBindingDialog extends BaseDialog {

	private ArrayList items = new ArrayList();

	/**
	 * @param parentShell
	 */
	public DataBindingDialog(Shell parentShell, DesignElementHandle model) {
		super(parentShell, Messages.getString("dataBinding.title")); //$NON-NLS-1$
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
		Composite composite = (Composite) super.createDialogArea(parent);
		Label label = new Label(composite, SWT.NONE);
		label.setText(Messages.getString("dataBinding.label.selectBinding")); //$NON-NLS-1$
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		label.setLayoutData(data);

		IBindingDialogHelper dialogHelper = (IBindingDialogHelper) ElementAdapterManager.getAdapter(items.get(0),
				IBindingDialogHelper.class);
		if (dialogHelper != null) {
			dialogHelper.setBindingHolder(DEUtil.getBindingHolder((DesignElementHandle) items.get(0)));
		}
		BindingPage page = new BindingPage(composite, SWT.NONE,
				dialogHelper == null ? false : dialogHelper.canProcessAggregation());
		page.setEnableAutoCommit(false);
		page.setInput(items);

		data = new GridData(GridData.FILL_BOTH);
		page.setLayoutData(data);
		UIUtil.bindHelp(parent, IHelpContextIds.DATA_BINDING_DIALOG_ID);
		return composite;
	}

	@Override
	protected boolean needRememberLastSize() {
		return true;
	}

}
