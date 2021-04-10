/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;

/**
 * Handler for custom button in <code>WizardBase</code>. If users want to add
 * more buttons in <code>WizardBase</code>, need to implement this interface and
 * add it in by invoking <code>WizardBase.addCustomButton()</code>.
 */

public interface IButtonHandler {

	/**
	 * Returns ID for the custom button. ID must be unique in
	 * <code>WizardBase</code>.
	 * 
	 * @see IDialogConstants
	 * @return ID for the button
	 */
	int getId();

	/**
	 * Returns Label for the custom button.
	 * 
	 * @return Label for the custom button.
	 */
	String getLabel();

	/**
	 * Returns tool-tip text of this button.
	 * 
	 * @return tool-tip text of this button.
	 */
	String getTooltip();

	/**
	 * Returns button icon.
	 * 
	 * @return button icon.
	 */
	Image getIcon();

	/**
	 * Restores the button control for later use. This method will be invoked by
	 * <code>WizardBase</code>
	 * 
	 * @param button button control
	 */
	void setButton(Button button);

	/**
	 * Returns the button control.
	 * 
	 * @return button control
	 */
	Button getButton();

	/**
	 * Runs the operation user defined when pressing the button.
	 * 
	 */
	void run();
}
