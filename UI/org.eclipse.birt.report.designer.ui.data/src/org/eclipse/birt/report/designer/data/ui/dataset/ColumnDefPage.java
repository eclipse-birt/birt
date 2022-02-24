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

package org.eclipse.birt.report.designer.data.ui.dataset;

import org.eclipse.birt.report.designer.data.ui.util.IHelpConstants;
import org.eclipse.birt.report.designer.data.ui.util.Utility;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * column define page for script data set
 */

public class ColumnDefPage extends WizardPage {

	protected ResultSetColumnPage page;

	// message displayed in the top side
	protected final static String Message = Messages.getString("OutputColumnDefnPage.description"); //$NON-NLS-1$

	/**
	 * @param pageName
	 */
	protected ColumnDefPage() {
		super(Messages.getString("ColumnDefPage.description")); //$NON-NLS-1$
		setTitle(Messages.getString("ColumnDefPage.title")); //$NON-NLS-1$
		setPageComplete(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.
	 * Composite)
	 */
	public void createControl(Composite parent) {
		page = new ResultSetColumnPage(parent, SWT.NONE);
		setControl(page);
		setMessage(Message);

		Utility.setSystemHelp(getControl(), IHelpConstants.CONEXT_ID_DATASET_SCRIPT);
	}

	public void saveResult(DataSetHandle handle) {
		page.saveResult(handle);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.DialogPage#setVisible(boolean)
	 */
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		getControl().setFocus();
	}
}
