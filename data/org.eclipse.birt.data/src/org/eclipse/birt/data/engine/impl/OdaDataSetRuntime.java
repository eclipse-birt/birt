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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.data.engine.api.IOdaDataSetDesign;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.script.ScriptEvalUtil;
import org.mozilla.javascript.Context;

/**
 * Encapulates the runtime definition of a generic extended (ODA) data set.
 * Wraps the static design properties defined in the
 * org.eclipse.birt.data.engine.api.IExtendedDataSetDesign interface.
 */
public class OdaDataSetRuntime extends DataSetRuntime implements IOdaDataSetDesign
{
	private String 	queryText;
	private Map		publicProperties;
	
    OdaDataSetRuntime( IOdaDataSetDesign dataSet )
    {
        super( dataSet );
        
        // Copy from design all properties that may change at runtime
        queryText = dataSet.getQueryText();
        publicProperties = new HashMap();
        publicProperties.putAll( dataSet.getPublicProperties() );
    }

    public IOdaDataSetDesign getSubdesign()
	{
		return (IOdaDataSetDesign) getDesign();
	}

    public OdaDataSourceRuntime getExtendedDataSource()
    {
        assert getDataSource() instanceof OdaDataSourceRuntime;
        return (OdaDataSourceRuntime) getDataSource();
    }

    public String getQueryText()
    {
    	return queryText;
    }
    
    public void setQueryText( String queryText )
    {
    	this.queryText = queryText;
    }

    public String getQueryScript()
    {
    	return getSubdesign().getQueryScript();
    }
    
    public String getDataSetType()
    {
        return getSubdesign().getDataSetType();
    }

    /**
     * @deprecated use getDataSetType
     */
	public String getQueryType()
	{
        return getSubdesign().getDataSetType();
	}
	
    public String getPrimaryResultSetName()
    {
        return getSubdesign().getPrimaryResultSetName();
    }

	public Map getPublicProperties( ) 
	{
		return this.publicProperties;
	}

	public Map getPrivateProperties( ) 
	{
        return getSubdesign().getPrivateProperties();
	}
	
	/**
	 * Gets the effective queryText. If queryScript is defined and returns non-null, use its
	 * result as query text. Otherwise use queryText.
	 */
	public String getEffectiveQueryText() throws DataException
	{
		String query = null;
		
		String queryScript = getQueryScript();
		if ( queryScript != null && queryScript.length() > 0 )
		{
			Context cx = Context.enter();
			try
			{
				Object result = ScriptEvalUtil.evaluateJSExpr( cx, this.getScriptable(), queryScript, 
						"DataSet(" +getName() + ").QueryScript",
						1);
				query = result.toString();
			}
			finally
			{
				Context.exit();
			}
		}
		
		if ( query == null || query.length() == 0 )
			query = this.queryText;
		
		return query;
	}

}
