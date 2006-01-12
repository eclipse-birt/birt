/*
 *************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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
package org.eclipse.birt.data.engine.api.script;

import java.util.Map;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IResultMetaData;

/**
 * A runtime instance of a data set. Java scripts associated with a data source
 * can use this interface to get/set data source properties. 
 */

public interface IDataSetInstanceHandle extends IJavascriptContext
{
	/**
	 * Gets the name of this data set
	 */
	String getName( );
	
	/**
	 * @return The runtime data source associated with this data set
	 */
	IDataSourceInstanceHandle getDataSource();
	
    /**
     * Gets the unique id that identifies the type of the data set, assigned by the
     * extension providing the implementation of this data set.
     * @return	The id fo the type of data set type as referenced by an ODA driver.
     * 			Null if none is defined. 
     */
    String getExtensionID();

    /**
     * Gets the current data set's column metadata, if available.
     * @return column metadata, or null if no metadata is available 
     * (e.g., data set is not yet open).
     */
    IResultMetaData getResultMetaData() throws BirtException;

	/**
	 * Gets the query text of the data set.
	 * 
	 * @return The static query text for execution.
	 */
	String getQueryText( ) throws BirtException;

	/**
	 * Sets the query text of the data set.
	 * 
	 * @return The static query text for execution.
	 */
	void setQueryText( String queryText ) throws BirtException;

	/**
	 * Get the value of a data set extension property. 
	 * 
	 * @param name Name of property; must not be null or empty
	 * @return Property value; null if property has not been defined 
	 */
	String getExtensionProperty( String name );

	/**
	 * Set the value of an extension property 
	 * 
	 * @param name Name of property; must not be null or empty
	 * @param value Property value; may be null
	 */
	void setExtensionProperty( String name, String value );
	
	/**
	 * Gets the data set extension properties, in the form of a ( name [String], value [String] )
	 * map. 
	 * 
	 * @return Extension properties as a Map of String->String pairs. Null if no extension
	 * property is defined
	 */
	Map getExtensionProperties( );
    
}
