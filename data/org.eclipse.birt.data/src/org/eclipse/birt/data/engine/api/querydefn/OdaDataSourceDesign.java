/*
 *************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.data.engine.api.querydefn;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.api.IOdaDataSourceDesign;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;

/**
 * Default implementation of the IExtendedDataSourceDesign interface. <p>
 * Describes the static design of a non-scripted Data Source.
 */
public class OdaDataSourceDesign extends BaseDataSourceDesign 
		implements IOdaDataSourceDesign 
{
    private String	driverName;
    private Map 	publicProps;
    private Map 	privateProps;

    public OdaDataSourceDesign( String name ) 
    {
        super( name );
    }
    
    /**
     * Gets the name of the data source driver name.
     * The driver name is required in a data source design.
     * @return	The data source driver name
     */
 	public String getDriverName()
	{
	    return driverName;
	}

 	/**
 	 * Specifies the name of the data source driver.
 	 * @param name	Name of the data source driver.
 	 */
	public void setDriverName( String name )
	{
	    driverName = name;
	}

	/**
	 * Gets the public properties for the data source.
	 * @return public properties as a map. Null if no public property 
	 * 			is defined for the data source
	 */
	public Map getPublicProperties( ) 
	{
	    if ( publicProps == null )
	        publicProps = new HashMap();
		return publicProps;
	}

	/**
	 * Gets the private properties for the data source.
	 * @return private properties as a map. Null if no public property 
	 * 			is defined for the data source
	 */
	public Map getPrivateProperties( ) 
	{
	    if ( privateProps == null )
	        privateProps = new HashMap();
		return privateProps;
	}

 	/**
	 * Adds a public connection property, in the form of a (Name, value) string pair.
	 * For a named property that is mapped to more than one values, 
	 * make multiple calls using the same property name will add additional value
	 * to a Set for the same property. 
	 */
	public void addPublicProperty( String name, String value ) throws DataException
	{
	    addProperty( getPublicProperties(), name, value );
 	}
   
 	/**
	 * Adds a private connection property, in the form of a (Name, value) string pair.
	 * For a named property that is mapped to more than one values, 
	 * make multiple calls using the same property name will add additional value
	 * to a Set for the same property. 
	 */
    public void addPrivateProperty( String name, String value ) throws DataException
    {
	    addProperty( getPrivateProperties(), name, value );
    }

    /**
     * Add given value to the set of values for named property
     * in the given properties map.
    */
    protected void addProperty( Map properties, String name, String value ) throws DataException
    {
        if ( properties.containsKey( name ) )
            // TODO - externalize message text
            throw new DataException( ResourceConstants.DUPLICATE_PROPERTY_NAME,
					name );
        
        if ( value != null )
			properties.put( name, value );
    }
   
}
