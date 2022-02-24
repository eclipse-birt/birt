/*
 *************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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
 *  
 *************************************************************************
 */

package org.eclipse.birt.data.engine.odi;

/**
 * A parameter meta-data interface that defines the metadata of a
 * IPreparedDSQuery's parameter, which can be of either input only, output only,
 * or input and output modes.
 * <p>
 * The metadata is defined based on a query's runtime metadata (as described by
 * its underlying data source driver), merging with static input and output
 * parameter hints specified in a IDataSourceQuery.
 */
public interface IParameterMetaData {
	/**
	 * Returns whether this parameter is an input parameter. A parameter can be of
	 * both input and output modes.
	 * 
	 * @return true if this parameter is an input parameter, false if it is output
	 *         only, or null if its input mode is unknown.
	 */
	public Boolean isInputMode();

	/**
	 * Returns whether this parameter is an output parameter. A parameter can be of
	 * both input and output modes.
	 * 
	 * @return true if this parameter is an output parameter, false if it is input
	 *         only, or null if its output mode is unknown.
	 */
	public Boolean isOutputMode();

	/**
	 * Returns the name of this parameter.
	 * 
	 * @return the parameter name of this parameter, or null if the name is
	 *         non-specified or unknown.
	 */
	public String getName();

	/**
	 * Returns the native name of this parameter.
	 * 
	 * @return the parameter native name of this parameter.
	 */
	public String getNativeName();

	/**
	 * Returns the 1-based parameter position of this parameter, as defined by the
	 * underlying data provider. Not all data source parameters are defined with a
	 * position value.
	 * 
	 * @return the 1-based parameter position of this parameter, or -1 if the
	 *         position is non-specified or unknown.
	 */
	public int getPosition();

	/**
	 * Returns the class of the expected value of this parameter.
	 * 
	 * @return The class of the expected parameter value.
	 */
	public Class getValueClass();

	/**
	 * Returns whether this parameter is optional.
	 * 
	 * @return true if this parameter is optional, false if this parameter is
	 *         required, null if unknown.
	 */
	public Boolean isOptional();

	/**
	 * Returns the default value of this input parameter.
	 * 
	 * @return the default value if known, null if not specified or if this is an
	 *         output only parameter.
	 */
	public String getDefaultInputValue();

	/**
	 * Returns the data provider specific data type name of this parameter.
	 * 
	 * @return the data type name as defined by the data provider.
	 */
	public String getNativeTypeName();

	/**
	 * Returns the maximum number of digits to the right of the decimal point of
	 * this parameter.
	 * 
	 * @return the scale of the parameter, or -1 if the scale is not specified or
	 *         unknown.
	 */
	public int getScale();

	/**
	 * Returns the maximum number of decimal digits of this parameter.
	 * 
	 * @return the precision of the parameter, or -1 if the scale is not specified
	 *         or unknown.
	 */
	public int getPrecision();

	/**
	 * Returns whether a null value is allowed for this parameter.
	 * 
	 * @return true if null is allowed for this parameter, false if null is not
	 *         allowed, or null if its nullability is not specified or unknown.
	 */
	public Boolean isNullable();
}
