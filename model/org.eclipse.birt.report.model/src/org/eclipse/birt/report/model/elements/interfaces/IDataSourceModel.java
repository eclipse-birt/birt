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
 * The interface for DataSource to store the constants.
 */
public interface IDataSourceModel {

	/**
	 * The property name of the script called before opening this data source.
	 */

	public static final String BEFORE_OPEN_METHOD = "beforeOpen"; //$NON-NLS-1$

	/**
	 * The property name of the script called before closing this data source.
	 */

	public static final String BEFORE_CLOSE_METHOD = "beforeClose"; //$NON-NLS-1$

	/**
	 * The property name of the script called after opening this data source.
	 */

	public static final String AFTER_OPEN_METHOD = "afterOpen"; //$NON-NLS-1$

	/**
	 * The property name of the script called after closing this data source.
	 */

	public static final String AFTER_CLOSE_METHOD = "afterClose"; //$NON-NLS-1$

}
