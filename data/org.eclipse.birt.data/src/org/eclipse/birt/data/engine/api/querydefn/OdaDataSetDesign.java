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

package org.eclipse.birt.data.engine.api.querydefn;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import org.eclipse.birt.data.engine.api.IOdaDataSetDesign;

/**
 * Default implementation of the IExtendedDataSetDesign interface.<p>
 * Describes the static design of a generic extended Data Set.
 */
public class OdaDataSetDesign extends BaseDataSetDesign 
		implements IOdaDataSetDesign
{
	private String	queryText;
	private String	queryScript;
	private String	dataSetType;
	private String	primaryResultSetName;
	private Map 	publicProps;
	private Map 	privateProps;
	
    public OdaDataSetDesign( String name )
    {
        super( name );
    }

    public OdaDataSetDesign( String name, String dataSourceName )
    {
        super( name, dataSourceName );
    }

	/* (non-Javadoc)
     * @see org.eclipse.birt.data.engine.api.IExtendedDataSetDesign#getQueryText()
     */
    public String getQueryText()
    {
        return queryText;
    }
 
    /**
     * Specifies the static query text. 
     * @param queryText	Static query text.
     */
	public void setQueryText( String queryText ) 
	{
	    this.queryText = queryText;
	}

	/* (non-Javadoc)
     * @see org.eclipse.birt.data.engine.api.IExtendedDataSetDesign#getQueryScript()
     */
    public String getQueryScript()
    {
        return queryScript;
    }

    /**
     * Specifies the script that dynamically generates a query text.
     * At runtime, a dynamically generated query, if exists and not null,
     * would get used, instead of the static query text.
     * @param queryScript
     */
	public void setQueryScript( String queryScript )
	{
	    this.queryScript = queryScript;
	}
 
	/* (non-Javadoc)
     * @see org.eclipse.birt.data.engine.api.IExtendedDataSetDesign#getDataSetType()
     */
    public String getDataSetType()
    {
        return dataSetType;
    }

    /**
     * Gets the type of query defined, as identified by
     * a data access driver.  The type name is required if the
     * data access driver supports more than one types of query.
     * @return	The type of query as referenced by a data access driver. 
	 * @deprecated by the {@link #getDataSetType()}
     */
    public String getQueryType()
    {
        return dataSetType;
    }
    
    /**
     * Specifies the type of data set query defined in the data set.
     * @param dataSetType	The type of data set query, as named by the data access driver.
     */
    public void setDataSetType( String dataSetType )
    {
        this.dataSetType = dataSetType;
    }
 
    /**
     * Specifies the type of query defined in the data set.
     * @param queryType	The type of query, as named by the data access driver.
	 * @deprecated by the {@link #setDataSetType()}
     */
    public void setQueryType( String queryType )
    {
        this.dataSetType = queryType;
    }
    
	/* (non-Javadoc)
     * @see org.eclipse.birt.data.engine.api.IExtendedDataSetDesign#getPrimaryResultSetName()
     */
    public String getPrimaryResultSetName()
    {
        return primaryResultSetName;
    }
 
    /**
     * Specifies the name of the primary result set.
     * @param resultSetName
     */
    public void setPrimaryResultSetName( String resultSetName )
    {
        primaryResultSetName = resultSetName;
    }

	/* (non-Javadoc)
     * @see org.eclipse.birt.data.engine.api.IExtendedDataSetDesign#getPublicProperties()
     */
	public Map getPublicProperties( ) 
	{
	    if ( publicProps == null )
	        publicProps = new HashMap();
		return publicProps;
	}

	/* (non-Javadoc)
     * @see org.eclipse.birt.data.engine.api.IExtendedDataSetDesign#getPrivateProperties()
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
	public void addPublicProperty( String name, String value )
	{
	    addProperty( getPublicProperties(), name, value );
	}
   
 	/**
	 * Adds a private connection property, in the form of a (Name, value) string pair.
	 * For a named property that is mapped to more than one values, 
	 * make multiple calls using the same property name will add additional value
	 * to a Set for the same property. 
	 */
    public void addPrivateProperty( String name, String value )
    {
	    addProperty( getPrivateProperties(), name, value );
    }

    /**
     * Add given value to the set of values for named property
     * in the given properties map.
     */
    protected void addProperty( Map properties, String name, String value )
    {
        Set values = (Set) properties.get( name );
        if ( values == null )
            values = new HashSet();
 
        // add given value to the set of values
        values.add( value );
        
        properties.put( name, values );
    }
    
}
