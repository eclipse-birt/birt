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

import org.eclipse.birt.data.engine.api.IScriptDataSourceDesign;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.odi.IDataSource;

/**
 * Encapulates the runtime definition of a scripted data source.
 */
public class ScriptDataSourceRuntime extends DataSourceRuntime implements IScriptDataSourceDesign
{
    ScriptDataSourceRuntime( IScriptDataSourceDesign dataSource, DataEngineImpl dataEngine )
    {
        super( dataSource, dataEngine );
    }

    public IScriptDataSourceDesign getSubdesign()
	{
		return (IScriptDataSourceDesign) getDesign();
	}
    
    public String getOpenScript()
    {
        return getSubdesign().getOpenScript();
    }
    
    /** Executes the open script; returns its result */
    public Object runOpenScript() throws DataException
    {
    	return runScript( getOpenScript(), "open");
    }
    
    public String getCloseScript()
    {
        return getSubdesign().getCloseScript();
    }
    
    /** Executes the close script; returns its result */
    public Object runCloseScript() throws DataException
	{
    	return runScript( getCloseScript(), "close");
	}

	/**
	 * @see org.eclipse.birt.data.engine.impl.DataSourceRuntime#openOdiDataSource(org.eclipse.birt.data.engine.odi.IDataSource)
	 */
	public void openOdiDataSource(IDataSource odiDataSource) throws DataException
	{
		// This is when we should run the Open script associated with the script data source
		runOpenScript();
		super.openOdiDataSource(odiDataSource);
	}
	
	
	/**
	 * @see org.eclipse.birt.data.engine.impl.DataSourceRuntime#closeOdiDataSource()
	 */
	public void closeOdiDataSource() throws DataException
	{
		// This is when we should run the Open script associated with the script data source
		runCloseScript();
		super.closeOdiDataSource();
	}
}
