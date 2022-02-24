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
package org.eclipse.birt.report.engine.api.script.instance;

import java.util.Map;

import org.eclipse.birt.report.engine.api.script.IColumnMetaData;
import org.eclipse.birt.report.engine.api.script.ScriptException;

public interface IDataSetInstance {
	/**
	 * Gets the name of this data set
	 */
	String getName();

	/**
	 * @return The runtime data source associated with this data set
	 */
	IDataSourceInstance getDataSource();

	/**
	 * Gets the unique id that identifies the type of the data set, assigned by the
	 * extension providing the implementation of this data set.
	 * 
	 * @return The id fo the type of data set type as referenced by an ODA driver.
	 *         Null if none is defined.
	 */
	String getExtensionID();

	/**
	 * Gets the current data set's column metadata, if available.
	 * 
	 * @return column metadata, or null if no metadata is available (e.g., data set
	 *         is not yet open).
	 */
	IColumnMetaData getColumnMetaData() throws ScriptException;

	/**
	 * Gets the query text of the data set.
	 * 
	 * @return The static query text for execution.
	 */
	String getQueryText() throws ScriptException;

	/**
	 * Sets the query text of the data set.
	 * 
	 * @return The static query text for execution.
	 */
	void setQueryText(String queryText) throws ScriptException;

	/**
	 * Get the value of a data set extension property.
	 * 
	 * @param name Name of property; must not be null or empty
	 * @return Property value; null if property has not been defined
	 */
	String getExtensionProperty(String name);

	/**
	 * Set the value of an extension property
	 * 
	 * @param name  Name of property; must not be null or empty
	 * @param value Property value; may be null
	 */
	void setExtensionProperty(String name, String value);

	/**
	 * Gets the data set extension properties, in the form of a ( name [String],
	 * value [String] ) map.
	 * 
	 * @return Extension properties as a Map of String->String pairs. Null if no
	 *         extension property is defined
	 */
	Map getAllExtensionProperties();

	/**
	 * Gets the current value of the named data set input parameter.
	 * 
	 * @param paramName Name of data set input parameter
	 * @return Current value of named data set input parameter
	 * @throws ScriptException if named parameter does not exist
	 */
	Object getInputParameterValue(String paramName) throws ScriptException;

	/**
	 * Sets the value of the named data set input parameter. Setting the input
	 * parameter value has an effect on the data set only at the data set's
	 * beforeOpen event
	 * 
	 * @param paramName  name of data set input parameter
	 * @param paramValue value of data set input parameter
	 * @throws ScriptException If named parameter does not exist, or if paramValue
	 *                         has an incompatible data type with the declared
	 *                         parameter type
	 */
	void setInputParameterValue(String paramName, Object paramValue) throws ScriptException;

	/**
	 * Gets the names and values of all data set input parameters, as a read-only
	 * Name (String) -> Value (Object) map.
	 * 
	 * @return Name (String) -> Value (Object) map. This map is read-only
	 */
	Map getInputParameters();

	/**
	 * Gets the current value of the named data set output parameter. A data set
	 * output parameter value is normally available only after the data set has been
	 * opened. If this method is called before an output parameter value is
	 * available, a null value is returned.
	 * 
	 * @param paramName Name of data set output parameter
	 * @return Current value of named data set output parameter
	 * @throws ScriptException if named parameter does not exist
	 */
	Object getOutputParameterValue(String paramName) throws ScriptException;

	/**
	 * Sets the value of the named data set output parameter. It will override any
	 * data set output parameter value that may have been provided by the external
	 * data source.
	 * 
	 * @param paramName  name of data set output parameter
	 * @param paramValue value of data set output parameter
	 * @throws BirtException If named parameter does not exist, or if paramValue has
	 *                       an incompatible data type with the declared parameter
	 *                       type
	 */
	void setOutputParameterValue(String paramName, Object paramValue) throws ScriptException;

	/**
	 * Gets the names and values of all data set output parameters, as a read-only
	 * Name (String) -> Value (Object) map.
	 * 
	 * @return Name (String) -> Value (Object) map. This map is read-only
	 */
	Map getOutputParameters();

}
