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

import java.util.Map;

/**
 * Describes the static design of a generic ODA Extended Data Set.
 * The data set is to be retrieved by a query and/or properties 
 * defined in this specialized interface.
 */
public interface IOdaDataSetDesign extends IBaseDataSetDesign 
{
    /**
     * Gets the static query text of the data set.
     * A data set might not have any query defined at all.
     * If a query text is defined, it could be either static
     * or dynamic. 
     * @return	The static query text for execution.  
     * 			Null if no static query is defined.
     */
    public abstract String getQueryText();
      
    /**
     * Gets the user-defined script, if defined, that dynamically generates a query text.
     * @return	The script that generates a query text.  
     * 			Null if no query script is defined.
     */
    public abstract String getQueryScript();

    /**
     * Gets the type of data set query defined, as identified by
     * an ODA data access driver.  The type name is required if the
     * data access driver supports more than one types of data set query.
     * @return	The type of data set query as referenced by a data access driver.
     * 			Null if none is defined. 
     */
    public abstract String getDataSetType();
      
   
    /**
     * Gets the name of the primary result set retrieved by the query.
     * This is required for a query that returns
     * multiple result sets, each of which can be identified by name.
     * @return	The name of the primary result set.
     * 			Null if none is defined.
     */
    public abstract String getPrimaryResultSetName();
      
    /**
	 * Gets the public data set property, in the form of a (name, set) pair.
	 * A named property can be mapped to more than one values. 
	 * The property name is of String type.
	 * The property value is a Set interface of string values.
	 * @return	Public properties as a Map of name-set pairs.
	 * 			Null if none is defined.
	 */
	public abstract Map getPublicProperties( );   

    /**
	 * Gets the private data set property, in the form of a (name, set) pair.
	 * A named property can be mapped to more than one values. 
	 * The property name is of String type.
	 * The property value is a Set interface of string values.
	 * @return	Private properties as a Map of name-set pairs.
	 * 			Null if none is defined.
	 */
	public abstract Map getPrivateProperties( );   

}
