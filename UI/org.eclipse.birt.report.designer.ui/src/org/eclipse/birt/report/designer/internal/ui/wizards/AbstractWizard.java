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

package org.eclipse.birt.report.designer.internal.ui.wizards;

import org.eclipse.birt.report.designer.internal.ui.dialogs.WizardDialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

/**
 * Base class for all wizards and some dialog
 * 
 * 
 */

public abstract class AbstractWizard extends Wizard {

	private int style;

	private Object model = null;

	private AbstractWizard wizard;

	private String finishLabel = IDialogConstants.FINISH_LABEL;

	/**
	 * Creates a wizard to create or edit element
	 * 
	 * @param title the wizard title
	 */
	public AbstractWizard(String title, int style) {
		super();
		wizard = this;
		this.style = style;
		setWindowTitle(title);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	public boolean performFinish() {
		assert model != null;
		IWizardPage[] pages = getPages();
		for (int i = 0; i < pages.length; i++) {
			savePage(((ElementWizardPage) pages[i]));
		}
		return true;
	}

	/**
	 * Sets wizard content
	 */
	public void initPage(IWizardPage page) {
		assert model != null;
		assert page instanceof ElementWizardPage;
		((ElementWizardPage) page).setInput(model);
	}

	/**
	 * Sets wizard content input
	 * 
	 * @param model the model to set
	 */
	public void setInput(Object model) {
		assert model != null;
		this.model = model;
	}

	/**
	 * Saves the result of the page
	 * 
	 * @page the page to save
	 */
	public void savePage(ElementWizardPage page) {
		page.saveTo(model);
	}

	/**
	 * Opens the wizard and return the result
	 * 
	 * @return the result
	 */
	public Object open() {
		// initialize the shell
		Shell shell = new Shell(PlatformUI.getWorkbench().getDisplay().getActiveShell(),
				SWT.DIALOG_TRIM | SWT.PRIMARY_MODAL);

		shell.setLayout(new GridLayout());

		// initialize the composite
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		composite.setLayout(layout);

		// create wizard dialog
		WizardDialog dialog = new WizardDialog(shell, wizard);
		dialog.create();
		dialog.setFinishLabel(finishLabel);

		// initialize page
		initPage(wizard.getStartingPage());

		if (dialog.open() == WizardDialog.CANCEL) {// Cancel was pressed
			return null;
		}
		// Finish button was pressed
		return model;
	}

	/**
	 * @return Returns the type.
	 */
	public int getStyle() {
		return style;
	}

	/**
	 * Sets finish Label
	 * 
	 * @param newLabel the label to be set
	 */
	protected void setFinishLabel(String newLabel) {
		finishLabel = newLabel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.IWizard#isHelpAvailable()
	 */
	public boolean isHelpAvailable() {
		return true;
	}
}
