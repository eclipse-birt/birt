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
 * The interface for script data set element to store the constants.
 */
public interface IScriptDataSetModel {

	/**
	 * Name of script property for opening the data set.
	 */

	String OPEN_METHOD = "open"; //$NON-NLS-1$

	/**
	 * Name of script property for describing the result set dynamically.
	 */

	String DESCRIBE_METHOD = "describe"; //$NON-NLS-1$

	/**
	 * Name of script property for providing the data for the next row from the
	 * result set.
	 */

	String FETCH_METHOD = "fetch"; //$NON-NLS-1$

	/**
	 * Name of script property for closing the data set.
	 */

	String CLOSE_METHOD = "close"; //$NON-NLS-1$

}
