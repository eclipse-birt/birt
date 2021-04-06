/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.elements.interfaces;

/**
 * The interface for parameter element to store the constants.
 */
public interface IParameterModel {

	/**
	 * Name of the help text property.
	 */

	public static final String HELP_TEXT_PROP = "helpText"; //$NON-NLS-1$

	/**
	 * Name of the help text key property.
	 */

	public static final String HELP_TEXT_KEY_PROP = "helpTextID"; //$NON-NLS-1$

	/**
	 * Name of the "hidden" property.
	 */

	public static final String HIDDEN_PROP = "hidden"; //$NON-NLS-1$

	/**
	 * Name of the validation property. It allows the user to input their validation
	 * code for the parameter to validate.
	 */

	public static final String VALIDATE_PROP = "validate"; //$NON-NLS-1$

	/**
	 * Name of the prompt text property
	 */
	public static final String PROMPT_TEXT_PROP = "promptText"; //$NON-NLS-1$

	/**
	 * Name of the prompt text ID property. This property contains the message ID
	 * used to localize property prompt text ID.
	 */

	public static final String PROMPT_TEXT_ID_PROP = "promptTextID"; //$NON-NLS-1$
}
