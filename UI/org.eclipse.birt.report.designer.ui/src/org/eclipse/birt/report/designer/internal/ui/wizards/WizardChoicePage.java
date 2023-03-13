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

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 * A wizard page to choose how to create the report.
 */
public class WizardChoicePage extends WizardPage {

	private static final String MESSAGE_CHOOSE_CUSTOM = Messages.getString("WizardChoicePage.radio.createFromCustom"); //$NON-NLS-1$
	private static final String MESSAGE_CHOOSE_TEMPLATE = Messages
			.getString("WizardChoicePage.radio.createFromTemplate"); //$NON-NLS-1$
	private static final String MESSAGE_CHOOSE_BLANK = Messages.getString("WizardChoicePage.radio.createBlank"); //$NON-NLS-1$

	private Button customChoice;
	private Button predefChoice;
	private Button blankChoice;

	private SelectionListener listener = new SelectionAdapter() {

		@Override
		public void widgetSelected(SelectionEvent e) {
			getContainer().updateButtons();
		}
	};

	/**
	 * The constructor.
	 *
	 * @param pageName
	 */
	public WizardChoicePage(String pageName) {
		super(pageName);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.
	 * Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.marginHeight = 10;
		gridLayout.marginWidth = 10;
		gridLayout.horizontalSpacing = 10;
		gridLayout.verticalSpacing = 10;
		composite.setLayout(gridLayout);

		predefChoice = new Button(composite, SWT.RADIO);
		predefChoice.setText(MESSAGE_CHOOSE_TEMPLATE);
		predefChoice.addSelectionListener(listener);

		customChoice = new Button(composite, SWT.RADIO);
		customChoice.setText(MESSAGE_CHOOSE_CUSTOM);
		customChoice.addSelectionListener(listener);

		blankChoice = new Button(composite, SWT.RADIO);
		blankChoice.setText(MESSAGE_CHOOSE_BLANK);
		blankChoice.addSelectionListener(listener);

		predefChoice.setSelection(true);
		setControl(composite);
	}

	/**
	 * Returns if current selection is custom.
	 */
	public boolean isCustom() {
		return customChoice.getSelection();
	}

	/**
	 * Returns if current selection is blank.
	 */
	public boolean isBlank() {
		return blankChoice.getSelection();
	}

}
