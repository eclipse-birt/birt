/*
 *************************************************************************
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
 *  Actuate Corporation - initial API and implementation
 *
 *************************************************************************
 */
package org.eclipse.birt.data.engine.api;

/**
 * Describes the metadata of a data set parameter. The definition is used to
 * provide a parameter's metadata when such information cannot be dynamically
 * obtained from the underlying data source.
 */
public interface IParameterDefinition {
	/**
	 * Returns the parameter name.
	 *
	 * @return the name of the parameter. Null if parameter is identified by index.
	 */
	String getName();

	/**
	 * Returns the native name of the parameter as known to the underlying data
	 * source.
	 *
	 * @return the parameter native name, or null if the name is not available or
	 *         this parameter is not named.
	 */
	String getNativeName();

	/**
	 * Returns the parameter position. Parameter positions start from 1.
	 *
	 * @return the parameter position. -1 if parameter is identified by name.
	 */
	int getPosition();

	/**
	 * Returns the parameter data type. See the
	 * <code>org.eclipse.birt.core.data.DataType</code> class for return value
	 * constants.
	 *
	 * @return the parameter data type
	 */
	int getType();

	/**
	 * Returns the parameter's native data type as defined by the underlying data
	 * source. The native data type code value is implementation-specific. Default
	 * value is 0 for none or unknown value.
	 *
	 * @return the native data type code of this parameter
	 */
	int getNativeType();

	/**
	 * Returns whether this parameter is an input parameter. A parameter can be of
	 * both input and output modes.
	 *
	 * @return true if this parameter is of input mode, false otherwise.
	 */
	boolean isInputMode();

	/**
	 * Returns whether this parameter is an output parameter. A parameter can be of
	 * both input and output modes.
	 *
	 * @return true if this parameter is of output mode, false otherwise.
	 */
	boolean isOutputMode();

	/**
	 * Specifies whether this parameter is optional. Applies to the parameter only
	 * if it is of input mode.
	 *
	 * @return true if this parameter is optional, false if this parameter is
	 *         required.
	 */
	boolean isInputOptional();

	/**
	 * Returns the default input value of this parameter.
	 *
	 * @return the default value, or null if the default value is not specified or
	 *         if this is an output only parameter.
	 */
	String getDefaultInputValue();

	/**
	 * Specifies whether null values are allowed for this parameter.
	 *
	 * @return true if this parameter value can be null, false otherwise.
	 */
	boolean isNullable();

}
