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

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * Base class for ComboSelectDialog
 *
 */
public class ComboBoxSelectionDialog extends Dialog {

	private String fSelection = null;

	private final String fShellTitle;

	private final String fLabelText;

	private final String[] fAllowedStrings;

	private final int fInitialSelectionIndex;

	/**
	 * The constructor.
	 *
	 * @param parentShell
	 * @param shellTitle
	 * @param labelText
	 * @param comboStrings
	 * @param initialSelectionIndex
	 */
	public ComboBoxSelectionDialog(Shell parentShell, String shellTitle, String labelText, String[] comboStrings,
			int initialSelectionIndex) {
		super(parentShell);
		fShellTitle = shellTitle;
		fLabelText = labelText;
		fAllowedStrings = comboStrings;
		fInitialSelectionIndex = initialSelectionIndex;
	}

	/**
	 * Returns selected string value.
	 *
	 * @return
	 */
	public String getSelectedString() {
		return fSelection;
	}

	/*
	 * @see
	 * org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.
	 * Composite)
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		getShell().setText(fShellTitle);

		Composite composite = (Composite) super.createDialogArea(parent);
		Composite innerComposite = new Composite(composite, SWT.NONE);
		innerComposite.setLayoutData(new GridData());
		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		innerComposite.setLayout(gl);

		Label label = new Label(innerComposite, SWT.NONE);
		label.setText(fLabelText);
		label.setLayoutData(new GridData());

		final Combo combo = new Combo(innerComposite, SWT.READ_ONLY);
		for (int i = 0; i < fAllowedStrings.length; i++) {
			combo.add(fAllowedStrings[i]);
		}
		if (fInitialSelectionIndex != -1) {
			combo.select(fInitialSelectionIndex);
			fSelection = combo.getItem(combo.getSelectionIndex());
		}
		GridData gd = new GridData();
		gd.widthHint = convertWidthInCharsToPixels(getMaxStringLength());
		combo.setLayoutData(gd);
		combo.setVisibleItemCount(30);
		combo.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				fSelection = combo.getItem(combo.getSelectionIndex());
			}
		});
		applyDialogFont(composite);
		return composite;
	}

	private int getMaxStringLength() {
		int max = 0;
		for (int i = 0; i < fAllowedStrings.length; i++) {
			max = Math.max(max, fAllowedStrings[i].length());
		}
		if (max < 20) {
			max = 20;
		}
		return max;
	}

}
