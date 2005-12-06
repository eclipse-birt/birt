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
 * The interface for oda data set element to store the constants.
 */
public interface IOdaDataSetModel
{

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
	 * @deprecated
	 */

	public static final String PUBLIC_DRIVER_PROPERTIES_PROP = "publicDriverProperties"; //$NON-NLS-1$

	/**
	 * The property name of private driver properties.
	 */

	public static final String PRIVATE_DRIVER_PROPERTIES_PROP = "privateDriverProperties"; //$NON-NLS-1$

	/**
	 * The property name of cached row count.
	 */
	public static final String CACHED_ROW_COUNT_PROP = "cachedRowCount"; //$NON-NLS-1$


}
