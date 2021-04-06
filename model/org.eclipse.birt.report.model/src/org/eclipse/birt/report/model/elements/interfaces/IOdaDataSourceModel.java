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
