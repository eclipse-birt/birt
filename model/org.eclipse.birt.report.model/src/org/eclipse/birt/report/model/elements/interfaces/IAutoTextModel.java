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
 * The interface for autotext element to store the constants.
 */
public interface IAutoTextModel {

	/**
	 * Name of the autotext type property that supports page number and total page
	 */

	String AUTOTEXT_TYPE_PROP = "type"; //$NON-NLS-1$

	/**
	 * Name of the page variable property which refers to the page variable name.
	 */
	String PAGE_VARIABLE_PROP = "pageVariable"; //$NON-NLS-1$

}
