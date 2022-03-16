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

package org.eclipse.birt.report.model.elements.interfaces;

/**
 * The interface for parameter element to store the constants.
 */
public interface IParameterModel {

	/**
	 * Name of the help text property.
	 */

	String HELP_TEXT_PROP = "helpText"; //$NON-NLS-1$

	/**
	 * Name of the help text key property.
	 */

	String HELP_TEXT_KEY_PROP = "helpTextID"; //$NON-NLS-1$

	/**
	 * Name of the "hidden" property.
	 */

	String HIDDEN_PROP = "hidden"; //$NON-NLS-1$

	/**
	 * Name of the validation property. It allows the user to input their validation
	 * code for the parameter to validate.
	 */

	String VALIDATE_PROP = "validate"; //$NON-NLS-1$

	/**
	 * Name of the prompt text property
	 */
	String PROMPT_TEXT_PROP = "promptText"; //$NON-NLS-1$

	/**
	 * Name of the prompt text ID property. This property contains the message ID
	 * used to localize property prompt text ID.
	 */

	String PROMPT_TEXT_ID_PROP = "promptTextID"; //$NON-NLS-1$
}
