/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.engine.api.script.instance;

import java.util.Map;
import java.util.Set;

import org.eclipse.birt.report.engine.api.script.ScriptException;

public interface IDataSetInstance
{
	/**
	 * Gets the name of this data set
	 */
	String getName( );

	/**
	 * @return The runtime data source associated with this data set
	 */
	IDataSourceInstance getDataSource( );

	/**
	 * Gets the unique id that identifies the type of the data set, assigned by
	 * the extension providing the implementation of this data set.
	 * 
	 * @return The id fo the type of data set type as referenced by an ODA
	 *         driver. Null if none is defined.
	 */
	String getExtensionID( );

	/**
	 * Gets the query text of the data set.
	 * 
	 * @return The static query text for execution.
	 */
	String getQueryText( ) throws ScriptException;

	/**
	 * Sets the query text of the data set.
	 * 
	 * @return The static query text for execution.
	 */
	void setQueryText( String queryText ) throws ScriptException;

	/**
	 * Get the extension property value(s)
	 * 
	 * @param name
	 * @return a Set of extention properties
	 */
	Set getExtensionProperty( String name );

	/**
	 * Set the extension property value(s)
	 * 
	 * @param name
	 * @param values
	 */
	void setExtensionProperty( String name, Set values );

	/**
	 * Set en extension property. This will add to the set of extension
	 * properties with the provided name.
	 * 
	 * @param name
	 * @param value
	 */
	void setExtensionProperty( String name, String value );

	/**
	 * Gets the data set extension properties, in the form of a (name, set)
	 * pair. A named property can be mapped to more than one values. The
	 * property name is of String type. The property value is a Set interface of
	 * string values.
	 * 
	 * @return Public properties as a Map of name-set pairs. Null if none is
	 *         defined.
	 */
	Map getExtensionProperties( );

}
