/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
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

package org.eclipse.birt.data.engine.api.aggregation;

/**
 *
 */
public interface IParameterDefn {

	/**
	 * get the native name of this parameter.
	 *
	 * @return
	 */
	String getName();

	/**
	 * to indicate whether this parameter is optional.
	 *
	 * @return
	 */
	boolean isOptional();

	/**
	 * to indicate whether this parameter reference to a column or binding of BIRT.
	 *
	 * @return
	 */
	boolean isDataField();

	/**
	 * get the display name of this parameter.
	 *
	 * @return
	 */
	String getDisplayName();

	/**
	 * get the description of this parameter.
	 *
	 * @return
	 */
	String getDescription();

	/**
	 * check whether this parameter support specified data type.
	 *
	 * @return
	 */
	boolean supportDataType(int dataType);
}
