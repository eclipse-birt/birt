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
 * The interface for Label element to store the constants.
 */
public interface ILabelModel {

	/**
	 * Name of the text property. This property contains the non-localized text for
	 * the label.
	 */

	public static final String TEXT_PROP = "text"; //$NON-NLS-1$

	/**
	 * Name of the message ID property. This property contains the message ID used
	 * to localize the text of the label.
	 */

	public static final String TEXT_ID_PROP = "textID"; //$NON-NLS-1$

	/**
	 * Name of the help text property.
	 */

	public static final String HELP_TEXT_PROP = "helpText"; //$NON-NLS-1$

	/**
	 * Name of the help text id property.
	 */

	public static final String HELP_TEXT_ID_PROP = "helpTextID"; //$NON-NLS-1$

	/**
	 * Name of the action property.
	 */

	public static final String ACTION_PROP = "action"; //$NON-NLS-1$

}
