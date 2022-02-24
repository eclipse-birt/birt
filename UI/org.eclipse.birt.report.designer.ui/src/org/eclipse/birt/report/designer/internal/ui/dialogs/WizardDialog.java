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

package org.eclipse.birt.report.designer.internal.ui.dialogs;

import org.eclipse.birt.report.designer.internal.ui.wizards.AbstractWizard;
import org.eclipse.birt.report.designer.internal.ui.wizards.ElementWizardPage;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * A dialog to show a wizard.
 */
public class WizardDialog extends org.eclipse.jface.wizard.WizardDialog {

	/**
	 * Creates a new wizard dialog for the given wizard.
	 * 
	 * @param parentShell the parent shell
	 * @param newWizard   the wizard this dialog is working on
	 */
	public WizardDialog(Shell parentShell, AbstractWizard newWizard) {
		super(parentShell, newWizard);
		setHelpAvailable(false);
		setBlockOnOpen(true);
		setShellStyle(getShellStyle() ^ SWT.RESIZE);
	}

	/**
	 * Sets the label of Finish button
	 * 
	 * @param text the new label of the Finish button
	 */
	public void setFinishLabel(String text) {
		getButton(IDialogConstants.FINISH_ID).setText(text);
	}

	/**
	 * Creates and returns the contents of this dialog's button bar.
	 * <p>
	 * The <code>WizardDialog</code> implementation of this framework method
	 * prevents the buttons from aligning with the same direction in order to make
	 * Help button split with other buttons.
	 * </p>
	 * 
	 * @param parent the parent composite to contain the button bar
	 * @return the button bar control
	 */
	protected Control createButtonBar(Composite parent) {
		Composite composite = (Composite) super.createButtonBar(parent);
		composite.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_CENTER));
		return composite;
	}

	/**
	 * Sets the layout data of the button to a GridData with appropriate heights and
	 * widths.
	 * <p>
	 * The <code>WizardDialog</code> override the method in order to make Help
	 * button split with other buttons.
	 * 
	 * @param button the button to be set layout data to
	 */
	protected void setButtonLayoutData(Button button) {
		GridData data;
		if (button.getText().equals(IDialogConstants.HELP_LABEL)) {
			data = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.VERTICAL_ALIGN_CENTER);
			data.grabExcessHorizontalSpace = true;
		} else {
			data = new GridData(GridData.HORIZONTAL_ALIGN_END | GridData.VERTICAL_ALIGN_CENTER);
		}
		data.heightHint = convertVerticalDLUsToPixels(IDialogConstants.BUTTON_HEIGHT);
		int widthHint = convertHorizontalDLUsToPixels(IDialogConstants.BUTTON_WIDTH);
		data.widthHint = Math.max(widthHint, button.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);
		button.setLayoutData(data);
	}

	/**
	 * The Next button has been pressed.
	 * 
	 * The <code>WizardDialog</code> override the method in order to save current
	 * result and when pages are switched
	 * 
	 * 
	 */
	protected void nextPressed() {
		ElementWizardPage page = (ElementWizardPage) getCurrentPage();
		((AbstractWizard) getWizard()).savePage(page);
		ElementWizardPage nextPage = (ElementWizardPage) page.getNextPage();
		if (nextPage != null) {
			((AbstractWizard) getWizard()).initPage(nextPage);
			showPage(nextPage);
		}
	}
}
