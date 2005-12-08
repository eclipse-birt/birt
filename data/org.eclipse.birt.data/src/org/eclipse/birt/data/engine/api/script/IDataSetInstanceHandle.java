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
	public abstract String getName();
	
	/**
	 * @return The runtime data source associated with this data set
	 */
	public abstract IDataSourceInstanceHandle getDataSource();
	
    /**
     * Gets the unique id that identifies the type of the data set, assigned by the
     * extension providing the implementation of this data set.
     * @return	The id fo the type of data set type as referenced by an ODA driver.
     * 			Null if none is defined. 
     */
    public abstract String getExtensionID();
    
    /**
	 * Gets the public data set property, in the form of a (name, set) pair.
	 * A named property can be mapped to more than one values. 
	 * The property name is of String type.
	 * The property value is a Set interface of string values.
	 * The returned Map is modifiable by the caller. The effect of such modification 
	 * depends on the event handler that make it.
	 * @return	Public properties as a Map of name-set pairs.
	 * 			Null if none is defined.
	 */
	public abstract Map getPublicProperties( );   

    /**
     * Gets the query text of the data set.
     * @return	The static query text for execution.  
     */
    public abstract String getQueryText() throws BirtException;
	
    /**
     * Sets the query text of the data set.
     * @return	The static query text for execution.  
     */
    public abstract void setQueryText( String queryText ) throws BirtException;
    
    /**
     * Gets the current data set's column metadata, if available.
     * @return column metadata, or null if no metadata is available 
     * (e.g., data set is not yet open).
     */
    public abstract IResultMetaData getResultMetaData() throws BirtException;
    
}
