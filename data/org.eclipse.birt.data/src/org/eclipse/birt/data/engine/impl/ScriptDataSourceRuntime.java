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

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IScriptDataSourceDesign;
import org.eclipse.birt.data.engine.api.script.IScriptDataSourceEventHandler;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.odi.IDataSource;

/**
 * Encapulates the runtime definition of a scripted data source.
 */
public class ScriptDataSourceRuntime extends DataSourceRuntime
{
	private IScriptDataSourceEventHandler scriptEventHandler; 
    ScriptDataSourceRuntime( IScriptDataSourceDesign dataSource, DataEngineImpl dataEngine )
    {
        super( dataSource, dataEngine );
        if ( getEventHandler() instanceof IScriptDataSourceEventHandler )
        	scriptEventHandler = (IScriptDataSourceEventHandler)getEventHandler();
        
		logger.log(Level.FINER,"ScriptDataSourceRuntime starts up");
    }

    public IScriptDataSourceDesign getSubdesign()
	{
		return (IScriptDataSourceDesign) getDesign();
	}
    
    /** Executes the open script; returns its result */
    public void open() throws DataException
    {
		if ( scriptEventHandler != null )
		{
			try
			{
				scriptEventHandler.handleOpen( this );
			}
			catch (BirtException e)
			{
				throw DataException.wrap(e);
			}
		}
    }
    
    /** Executes the close script; returns its result */
    public void close() throws DataException
	{
		if ( scriptEventHandler != null )
		{
			try
			{
				scriptEventHandler.handleClose( this );
			}
			catch (BirtException e)
			{
				throw DataException.wrap(e);
			}
		}
	}

	/**
	 * @see org.eclipse.birt.data.engine.impl.DataSourceRuntime#openOdiDataSource(org.eclipse.birt.data.engine.odi.IDataSource)
	 */
	public void openOdiDataSource(IDataSource odiDataSource) throws DataException
	{
		// This is when we should run the Open script associated with the script data source
		open();
		super.openOdiDataSource(odiDataSource);
	}
	
	/**
	 * @see org.eclipse.birt.data.engine.impl.DataSourceRuntime#closeOdiDataSource()
	 */
	public void closeOdiDataSource() throws DataException
	{
		// This is when we should run the Open script associated with the script data source
		close();
		super.closeOdiDataSource();
	}

	/**
	 * @see org.eclipse.birt.data.engine.impl.DataSourceRuntime#getExtensionID()
	 */
	public String getExtensionID()
	{
		// This is not ODA data source; it has no extension ID
		// Return a fixed string
		return "SCRIPT";
	}

	/**
	 * @see org.eclipse.birt.data.engine.api.script.IDataSourceInstance#getPublicProperties()
	 */
	public Map getPublicProperties()
	{
		// No public properties for Script data source
		return new HashMap();
	}
	
	
}
