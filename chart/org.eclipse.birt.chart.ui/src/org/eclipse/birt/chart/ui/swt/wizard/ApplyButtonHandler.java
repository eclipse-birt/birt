/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
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

package org.eclipse.birt.chart.ui.swt.wizard;

import org.eclipse.birt.chart.ui.i18n.Messages;
import org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.IButtonHandler;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;

/**
 * Apply button handler for chart builder. Method {@link #run()} must be
 * overriden for saving operation.
 */

public abstract class ApplyButtonHandler implements IButtonHandler {

	protected Button button;
	private ChartWizard wizard;

	public ApplyButtonHandler(ChartWizard wizard) {
		this.wizard = wizard;
	}

	public Button getButton() {
		return button;
	}

	public int getId() {
		return IDialogConstants.OK_ID;
	}

	public String getLabel() {
		return Messages.getString("ApplyButtonHandler.Label.Apply"); //$NON-NLS-1$
	}

	/**
	 * Returns tool-tip text of this button.
	 * 
	 * @return tool-tip text of this button.
	 */
	public String getTooltip() {
		return null;
	}

	/**
	 * Returns button icon.
	 * 
	 * @return button icon.
	 */
	public Image getIcon() {
		return null;
	}

	public void setButton(Button button) {
		this.button = button;
		((GridData) button.getLayoutData()).horizontalIndent = 10;

		// Default status is disabled
		button.setEnabled(false);
	}

	public void run() {
		// Update title and button status
		wizard.updateTitleAsEdit();
		getButton().setEnabled(false);
	}

}
