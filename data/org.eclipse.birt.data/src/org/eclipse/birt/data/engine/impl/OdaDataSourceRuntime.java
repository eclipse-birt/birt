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

/**
 * Encapulates the runtime definition of a generic extended data source.
 */
public class OdaDataSourceRuntime extends DataSourceRuntime implements IOdaDataSourceDesign
{
	private	Map			publicProperties;
	private String		driverName;
	
    OdaDataSourceRuntime( IOdaDataSourceDesign dataSource, DataEngineImpl dataEngine )
    {
        super( dataSource, dataEngine);
        
    	// Copy updatable properties
        publicProperties = new HashMap();
        publicProperties.putAll( dataSource.getPublicProperties() );
        
        driverName = dataSource.getDriverName();
    }

    public IOdaDataSourceDesign getSubdesign()
	{
		return (IOdaDataSourceDesign) getDesign();
	}

    public String getDriverName()
	{
	    return driverName;
	}
    
    /** Sets the driverName */
    public void setDriverName( String driverName )
    {
    	this.driverName = driverName;
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
