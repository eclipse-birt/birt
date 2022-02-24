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

package org.eclipse.birt.report.designer.internal.ui.editors.wizards;

import org.eclipse.birt.report.designer.internal.ui.dialogs.BaseWizardDialog;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.widgets.Shell;

/**
 * A WizardDialog witch can return a select path.
 */

public class SaveReportAsWizardDialog extends BaseWizardDialog {

	private IPath saveAsPath;

	public SaveReportAsWizardDialog(Shell parentShell, IWizard newWizard) {
		super(parentShell, newWizard);
		setHelpAvailable(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.WizardDialog#finishPressed()
	 */
	protected void finishPressed() {
		super.finishPressed();
		IWizardPage page = getCurrentPage();
		IWizard wizard = page.getWizard();
		this.saveAsPath = ((SaveReportAsWizard) wizard).getSaveAsPath();
	}

	public IPath getResult() {
		return this.saveAsPath;
	}

}
