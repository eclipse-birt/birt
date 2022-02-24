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

package org.eclipse.birt.report.model.adapter.oda;

/**
 * Golbal constants.
 *
 */

public interface IConstants {

	/**
	 * Indicates an empty string.
	 */

	public final static String EMPTY_STRING = ""; //$NON-NLS-1$

	/**
	 * Character enconding in parsing and writing designer values.
	 */

	public final static String CHAR_ENCODING = "utf-8"; //$NON-NLS-1$

	/**
	 * The version number to set when serializes designer values.
	 */

	final static String DESINGER_VALUES_VERSION = "2.0"; //$NON-NLS-1$
}
