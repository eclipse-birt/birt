/*
 *************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */

package org.eclipse.birt.data.engine.api;

import org.eclipse.birt.data.engine.core.DataException;

/**
 * Describes the metadata of a parameter in an <code>IPreparedQuery</code>.
 * A parameter's metadata is defined based on a query's 
 * runtime meta-data (as described by underlying its data source driver),
 * merging with static input and output parameter hints specified 
 * in a data set design.
 */
public interface IParameterMetaData
{
	/**
	 * Returns whether this parameter is an input parameter.
     * A parameter can be of both input and output modes.
	 * @return	true if this parameter is an input parameter, 
	 * 			false if it is output only, or null
	 * 			if its input mode is unknown.
	 */
	public Boolean isInputMode();
	
	/**
	 * Returns whether this parameter is an output parameter.
     * A parameter can be of both input and output modes.
	 * @return	true if this parameter is an output parameter, 
	 * 			false if it is input only, or null
	 * 			if its output mode is unknown.
	 */
	public Boolean isOutputMode();

	/**
	 * Returns the name of this parameter.
	 * @return	the parameter name of this parameter, 
	 * 			or null if the name is non-specified or unknown.
	 */
	public String getName();
	
	/**
	 * Returns the 1-based parameter position of this parameter,
	 * as defined by the underlying data provider.
	 * Not all data source parameters are defined with a position value.
	 * @return 	the 1-based parameter position of this parameter, 
	 * 			or -1 if the position is non-specified or unknown.
	 */
	public int getPosition();
	
	/**
	 * Returns the data type of this parameter.
	 * @return		The data type of this parameter, as an integer 
     * 				defined in <code>org.eclipse.birt.core.data.DataType</code>.
	 * @throws DataException	
	 */
	public int getDataType() throws DataException;

	/**
	 * Returns the data type name of this parameter.
	 * @return		The data type name of this parameter.
	 * @throws DataException
	 */
	public String getDataTypeName() throws DataException;

	/**
	 * Returns whether this parameter is optional.
	 * @return	true if this parameter is optional, 
	 * 			false if this parameter is required,
	 * 			null if unknown.
	 */
	public Boolean isOptional();


	/**
     * Returns the default value of this input parameter.
     * @return	the default value if known, null if not specified
     * 			or if this is an output only parameter.
     */
	public String getDefaultInputValue();
	
	/**
	 * Returns the data provider specific data type name of this parameter.
	 * @return	the data type name as defined by the data provider.
	 */
	public String getNativeTypeName();
	
	/**
	 * Returns the maximum number of digits to the right of the
	 * decimal point of this parameter.
	 * @return	the scale of the parameter, or 
	 * 			-1 if the scale is not specified or unknown.
	 */
	public int getScale();
	
	/**
	 * Returns the maximum number of decimal digits of this parameter.
	 * @return	the precision of the parameter, or 
	 * 			-1 if the scale is not specified or unknown.
	 */
	public int getPrecision();
	
	/**
	 * Returns whether a null value is allowed for this parameter.
	 * @return	true if null is allowed for this parameter, 
	 * 			false if null is not allowed, or
	 * 			null if its nullability is not specified or unknown.
	 */
	public Boolean isNullable();
}
