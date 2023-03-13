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

import org.eclipse.birt.report.designer.internal.ui.dialogs.ParameterGroupGeneralPage;
import org.eclipse.birt.report.designer.internal.ui.dialogs.TabDialog;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * The dialog used to create or edit a parameter group
 */

public class ParameterGroupDialog extends TabDialog {

	private static final String GENERAL_PAGE_TITLE = Messages.getString("ParameterGroupDialog.Page.Title");//$NON-NLS-1$

	/**
	 * The constructor.
	 *
	 * @param parentShell
	 * @param title
	 */
	public ParameterGroupDialog(Shell parentShell, String title) {
		super(parentShell, title);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.dialogs.TabDialog#addTabPages ()
	 */
	@Override
	public void addTabPages() {
		addTabPage(new ParameterGroupGeneralPage(GENERAL_PAGE_TITLE));
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		UIUtil.bindHelp(parent, IHelpContextIds.PARAMETER_GROUP_DIALOG_ID);
		return super.createDialogArea(parent);
	}
}
