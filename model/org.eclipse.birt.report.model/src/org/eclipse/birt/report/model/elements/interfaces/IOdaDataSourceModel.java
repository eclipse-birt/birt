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
 * The interface for oda data source element to store the constants.
 */
public interface IOdaDataSourceModel {

	/**
	 * The property name of the name of a driver.
	 * 
	 * @deprecated This property is removed.
	 */

	public static final String DRIVER_NAME_PROP = "driverName"; //$NON-NLS-1$

	/**
	 * The property name of private driver properties.
	 */

	public static final String PRIVATE_DRIVER_PROPERTIES_PROP = "privateDriverProperties"; //$NON-NLS-1$

	/**
	 * The property name of external connection name.
	 */

	public static final String EXTERNAL_CONNECTION_NAME = "externalConnectionName";

	/**
	 * The property name of public driver properties.
	 * 
	 * @deprecated
	 */

	public static final String PUBLIC_DRIVER_PROPERTIES_PROP = "publicDriverProperties"; //$NON-NLS-1$

	/**
	 * The property name of designer state. The property is used to save ODA state
	 * persistently.
	 */

	public static final String DESIGNER_STATE_PROP = "designerState"; //$NON-NLS-1$
}
