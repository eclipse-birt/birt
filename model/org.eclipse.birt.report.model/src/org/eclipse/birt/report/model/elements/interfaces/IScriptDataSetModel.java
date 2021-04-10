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
 * The interface for script data set element to store the constants.
 */
public interface IScriptDataSetModel {

	/**
	 * Name of script property for opening the data set.
	 */

	public static final String OPEN_METHOD = "open"; //$NON-NLS-1$

	/**
	 * Name of script property for describing the result set dynamically.
	 */

	public static final String DESCRIBE_METHOD = "describe"; //$NON-NLS-1$

	/**
	 * Name of script property for providing the data for the next row from the
	 * result set.
	 */

	public static final String FETCH_METHOD = "fetch"; //$NON-NLS-1$

	/**
	 * Name of script property for closing the data set.
	 */

	public static final String CLOSE_METHOD = "close"; //$NON-NLS-1$

}
