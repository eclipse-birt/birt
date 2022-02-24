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
 * The interface for oda data set element to store the constants.
 */
public interface IOdaDataSetModel {

	/**
	 * The property name of the query statement.
	 */

	public static final String QUERY_TEXT_PROP = "queryText"; //$NON-NLS-1$

	/**
	 * The property name of the result set name.
	 */

	public static final String RESULT_SET_NAME_PROP = "resultSetName"; //$NON-NLS-1$

	/**
	 * The property name of public driver properties.
	 * 
	 * @deprecated
	 */

	public static final String PUBLIC_DRIVER_PROPERTIES_PROP = "publicDriverProperties"; //$NON-NLS-1$

	/**
	 * The property name of private driver properties.
	 */

	public static final String PRIVATE_DRIVER_PROPERTIES_PROP = "privateDriverProperties"; //$NON-NLS-1$

	/**
	 * The property name of designer state. The property is used to save ODA state
	 * persistently.
	 */

	public static final String DESIGNER_STATE_PROP = "designerState"; //$NON-NLS-1$

	/**
	 * The property is used to save ODA persistent values.
	 */

	public static final String DESIGNER_VALUES_PROP = "designerValues"; //$NON-NLS-1$

	/**
	 * The property name of the result set number.
	 */
	public static final String RESULT_SET_NUMBER_PROP = "resultSetNumber"; //$NON-NLS-1$

}
