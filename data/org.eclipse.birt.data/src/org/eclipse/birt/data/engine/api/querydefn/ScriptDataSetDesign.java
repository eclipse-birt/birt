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

import org.eclipse.birt.data.engine.api.IScriptDataSetDesign;

/**
 * Default implementation of IScriptDataSetDesign interface.<p>
 * Describes the static design of a scripted Data Set.
 */
public class ScriptDataSetDesign extends BaseDataSetDesign implements
        IScriptDataSetDesign
{
	private String 	openScript;
	private String 	fetchScript;
	private String 	closeScript;
	private String	describeScript;

    public ScriptDataSetDesign( String name )
    {
        super( name );
    }

    public ScriptDataSetDesign( String name, String dataSourceName )
    {
        super( name, dataSourceName );
    }

    /* (non-Javadoc)
     * @see org.eclipse.birt.data.engine.api.IScriptDataSetDesign#getOpenScript()
     */
    public String getOpenScript()
    {
        return openScript;
    }

    /**
     * Specifies the Open script for opening the data set.
     * @param The Open script
     */
    public void setOpenScript( String script )
    {
        openScript = script;
    }

    /* (non-Javadoc)
     * @see org.eclipse.birt.data.engine.api.IScriptDataSetDesign#getFetchScript()
     */
    public String getFetchScript()
    {
        return fetchScript;
    }

    /**
     * Specifies the Fetch script for fetching each data row.
     * @param The Fetch script
     */
    public void setFetchScript( String script )
    {
        fetchScript = script;
    }

    /* (non-Javadoc)
     * @see org.eclipse.birt.data.engine.api.IScriptDataSetDesign#getCloseScript()
     */
    public String getCloseScript()
    {
        return closeScript;
    }

    /**
     * Specifies the Close script for closing the data set.
     * @param The Close script
     */
    public void setCloseScript( String script )
    {
        closeScript = script;
    }
    
    /*
     *  (non-Javadoc)
     * @see org.eclipse.birt.data.engine.api.IScriptDataSetDesign#getDescribeScript()
     */
    public String getDescribeScript()
    {
        return describeScript;
    }

    /**
     * Specifies the Describe script for closing the data set.
     * @param The Describe script
     */
    public void setDescribeScript( String script )
    {
        describeScript = script;
    }

}
