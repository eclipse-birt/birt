/*******************************************************************************
 * Copyright (c) 2012 Actuate Corporation.
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

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;

public class TableOptionWizard extends Wizard {

	private static final String MSG_INSERT_TABLE = Messages.getString("TableOptionDialog.title.InsertTable"); //$NON-NLS-1$

	private DataSetBindingSelectorPage dataSetBindingSelectorPage;
	private TableOptionPage tableOptionPage;

	public TableOptionWizard() {
		super();
		setWindowTitle(MSG_INSERT_TABLE); // $NON-NLS-1$
	}

	@Override
	public void addPages() {
		tableOptionPage = new TableOptionPage();
		addPage(tableOptionPage);
		dataSetBindingSelectorPage = new DataSetBindingSelectorPage();
		addPage(dataSetBindingSelectorPage);
	}

	@Override
	public boolean performFinish() {
		tableOptionPage.performFinish();
		dataSetBindingSelectorPage.performFinish();
		return true;
	}

	@Override
	public boolean performCancel() {
		return super.performCancel();
	}

	@Override
	public boolean canFinish() {
		IWizardPage[] pages = getPages();
		for (int i = 0; i < pages.length; i++) {
			IWizardPage currentPage = pages[i];
			if (!currentPage.isPageComplete()) {
				return false;
			}
			if (currentPage.getNextPage() == null) {
				return true;
			}
		}
		return true;
	}

	@Override
	public boolean needsPreviousAndNextButtons() {
		return true;
	}

	public Object[] getResult() {
		return new Object[] { tableOptionPage.getResult(), dataSetBindingSelectorPage.getResult() };
	}
}
