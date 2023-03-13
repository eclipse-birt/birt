/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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

package org.eclipse.birt.report.model.api.simpleapi;

/**
 *
 * Represents the design of an ResultSetColumn in the scripting environment
 *
 */

public interface IResultSetColumn {

	/**
	 * Gets column name.
	 *
	 * @return column name
	 */

	String getName();

	/**
	 * Gets native data type.
	 *
	 * @return native data type.
	 */

	Integer getNativeDataType();

	/**
	 * Gets position.
	 *
	 * @return position
	 */

	Integer getPosition();

	/**
	 * Gets column data type.
	 *
	 * @return column data type.
	 */

	String getColumnDataType();
}
