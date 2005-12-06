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

package org.eclipse.birt.data.engine.impl;

import org.eclipse.birt.data.engine.api.IOdaDataSourceDesign;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * Encapulates the runtime definition of a generic extended data source.
 */
public class OdaDataSourceRuntime extends DataSourceRuntime
{
	private	Map			publicProperties;
	private String		extensionID;
	
    OdaDataSourceRuntime( IOdaDataSourceDesign dataSource, DataEngineImpl dataEngine )
    {
        super( dataSource, dataEngine);
        
    	// Copy updatable properties
        publicProperties = new HashMap();
        publicProperties.putAll( dataSource.getPublicProperties() );
        
        extensionID = dataSource.getExtensionID();
		logger.log(Level.FINER,"OdaDataSourceRuntime starts up");
   }

    public IOdaDataSourceDesign getSubdesign()
	{
		return (IOdaDataSourceDesign) getDesign();
	}

    public String getExtensionID()
	{
	    return extensionID;
	}
    
    /** Sets the extensionID */
    public void setExtensionID( String exensionID )
    {
    	this.extensionID = exensionID;
    }

    public Map getPublicProperties( ) 
	{
    	// Return runtime copy of public properties, which may have been updated
    	return this.publicProperties;
	}

	public Map getPrivateProperties( ) 
	{
	    return getSubdesign().getPrivateProperties();
	}
	
}
