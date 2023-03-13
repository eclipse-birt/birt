/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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

package org.eclipse.birt.data.engine.api;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.core.DataException;

/**
 *
 */

public interface IBaseParameterMetaData {

	/**
	 * Returns the name of this parameter.
	 *
	 * @return the parameter name of this parameter, or null if the name is
	 *         non-specified or unknown.
	 */
	String getName();

	/**
	 * Returns the 1-based parameter position of this parameter, as defined by the
	 * underlying data provider. Not all data source parameters are defined with a
	 * position value.
	 *
	 * @return the 1-based parameter position of this parameter, or -1 if the
	 *         position is non-specified or unknown.
	 */
	int getPosition();

	/**
	 * Returns the data type of this parameter.
	 *
	 * @return The data type of this parameter, as an integer defined in
	 *         <code>org.eclipse.birt.core.data.DataType</code>.
	 * @throws DataException
	 */
	int getDataType() throws BirtException;

	/**
	 * Returns whether this parameter is optional.
	 *
	 * @return true if this parameter is optional, false if this parameter is
	 *         required, null if unknown.
	 */
	Boolean isOptional();

	/**
	 * Returns whether a null value is allowed for this parameter.
	 *
	 * @return true if null is allowed for this parameter, false if null is not
	 *         allowed, or null if its nullability is not specified or unknown.
	 */
	Boolean isNullable();

}
