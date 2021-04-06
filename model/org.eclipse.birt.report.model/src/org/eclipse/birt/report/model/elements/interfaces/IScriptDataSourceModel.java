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
 * The interface for script data source element to store the constants.
 */
public interface IScriptDataSourceModel {

	/**
	 * The property name of the script that connects to the data source.
	 */

	public static final String OPEN_METHOD = "open"; //$NON-NLS-1$

	/**
	 * The property name of the script that close the data source.
	 */

	public static final String CLOSE_METHOD = "close"; //$NON-NLS-1$

}
