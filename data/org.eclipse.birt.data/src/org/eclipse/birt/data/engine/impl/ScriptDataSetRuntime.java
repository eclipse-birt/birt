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

package org.eclipse.birt.data.engine.impl;

import org.eclipse.birt.data.engine.api.IScriptDataSetDesign;
import org.eclipse.birt.data.engine.core.DataException;

/**
 * Encapulates the runtime definition of a scripted data set.
 */
public class ScriptDataSetRuntime extends DataSetRuntime implements IScriptDataSetDesign
{
    ScriptDataSetRuntime( IScriptDataSetDesign dataSet )
    {
        super( dataSet );
    }

    public IScriptDataSetDesign getSubdesign()
	{
		return (IScriptDataSetDesign) getDesign();
	}

    public ScriptDataSourceRuntime getScriptDataSource()
    {
        assert getDataSource() instanceof ScriptDataSourceRuntime;
        return (ScriptDataSourceRuntime) getDataSource();
    }

    public String getOpenScript()
    {
        return getSubdesign().getOpenScript();
    }
    
	/** Executes the open script */
	public Object runOpenScript() throws DataException
	{
		return runScript( getOpenScript(), "open" );
	}

    public String getFetchScript()
    {
        return getSubdesign().getFetchScript();
    }

	/** Executes the fetch script; returns the result */
	public Object runFetchScript() throws DataException
	{
		return runScript( getFetchScript(), "fetch" );
	}
	
    public String getCloseScript()
    {
        return getSubdesign().getCloseScript();
    }
    
	/** Executes the close script*/
	public Object runCloseScript() throws DataException
	{
		return runScript( getCloseScript(), "close" );
	}

    public String getDescribeScript()
    {
        return getSubdesign().getDescribeScript();
    }

	/** Executes the describe script*/
	public Object runDescribeScript() throws DataException
	{
		return runScript( getDescribeScript(), "describe" );
	}
	
	/** Executes the close script to close the data set */
	public void close() throws DataException
	{
		runCloseScript();
	}
}
