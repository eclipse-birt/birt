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
 * The interface for parameter group element to store the constants.
 */
public interface IParameterGroupModel {

	public static final String START_EXPANDED_PROP = "startExpanded"; //$NON-NLS-1$
	public static final String HELP_TEXT_PROP = "helpText"; //$NON-NLS-1$
	public static final String HELP_TEXT_KEY_PROP = "helpTextID"; //$NON-NLS-1$

	/**
	 * Name of the prompt text property. Give hints to the user when enter parameter
	 * values.
	 */

	public static final String PROMPT_TEXT_PROP = "promptText"; //$NON-NLS-1$

	/**
	 * Name of the prompt text key property. This property contains the message ID
	 * used to localize property prompt text key.
	 */

	public static final String PROMPT_TEXT_ID_PROP = "promptTextID"; //$NON-NLS-1$

	/**
	 * Constant for the Report Items slot within a container.
	 */

	public static final int PARAMETERS_SLOT = 0;
}
