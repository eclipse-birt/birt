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

import org.eclipse.birt.data.engine.api.IScriptDataSourceDesign;

/**
 * Default implementation of IScriptDataSourceDesign interface.<p>
 * Describes the static design of a scripted Data Source.
 */
public class ScriptDataSourceDesign extends BaseDataSourceDesign implements
        IScriptDataSourceDesign
{
	private String 	openScript;
	private String 	closeScript;

    public ScriptDataSourceDesign( String name )
    {
        super( name );
    }

    /* (non-Javadoc)
     * @see org.eclipse.birt.data.engine.api.IScriptDataSourceDesign#getOpenScript()
     */
    public String getOpenScript()
    {
        return openScript;
    }

    /**
     * Specifies the Open script for opening the data source (connection).
     * @param The Open script
     */
    public void setOpenScript( String script )
    {
        openScript = script;
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.birt.data.engine.api.IScriptDataSourceDesign#getCloseScript()
     */
    public String getCloseScript()
    {
        return closeScript;
    }

    /**
     * Specifies the Close script for closing the data source (connection).
     * @param The Close script
     */
    public void setCloseScript( String script )
    {
        closeScript = script;
    }

}
