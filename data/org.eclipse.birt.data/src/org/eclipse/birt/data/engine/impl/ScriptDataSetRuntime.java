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
import org.eclipse.birt.data.engine.api.IScriptDataSetDesign;
import org.eclipse.birt.data.engine.api.script.IScriptDataSetEventHandler;
import org.eclipse.birt.data.engine.core.DataException;

/**
 * Encapulates the runtime definition of a scripted data set.
 */
public class ScriptDataSetRuntime extends DataSetRuntime
{
	private IScriptDataSetEventHandler scriptEventHandler;
	
    ScriptDataSetRuntime( IScriptDataSetDesign dataSet, PreparedQuery.Executor executor )
    {
        super( dataSet, executor);
    	if ( getEventHandler() instanceof IScriptDataSetEventHandler )
    		scriptEventHandler = (IScriptDataSetEventHandler) getEventHandler();
		logger.log(Level.FINER,"ScriptDataSetRuntime starts up");
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
	public void open() throws DataException
	{
		if ( scriptEventHandler != null )
		{
			try
			{
				scriptEventHandler.open( this );
			}
			catch (BirtException e)
			{
				throw DataException.wrap(e);
			}
		}
	}

	/** Executes the fetch script; returns the result */
	public boolean fetch() throws DataException
	{
		if ( scriptEventHandler != null )
		{
			try
			{
				return scriptEventHandler.fetch( this, this.getDataRow() );
			}
			catch (BirtException e)
			{
				throw DataException.wrap(e);
			}
		}
		return false;
	}
	
    
	/** Executes the close script*/
	public void close() throws DataException
	{
		if ( scriptEventHandler != null )
		{
			try
			{
				scriptEventHandler.close(this );
			}
			catch (BirtException e)
			{
				throw DataException.wrap(e);
			}
		}
		super.close();
	}

	/** Executes the describe script*/
	public Object runDescribeScript() throws DataException
	{
		if ( scriptEventHandler != null )
		{
			try
			{
				return scriptEventHandler.describe(this );
			}
			catch (BirtException e)
			{
				throw DataException.wrap(e);
			}
		}
		return null;
	}
	
	/**
	 * @see org.eclipse.birt.data.engine.api.script.IDataSetInstance#getExtensionID()
	 */
	public String getExtensionID()
	{
		// Not an ODA data set and has no extension. Use a fixed string
		return "SCRIPT";
	}

	/**
	 * @see org.eclipse.birt.data.engine.api.script.IDataSetInstance#getPublicProperties()
	 */
	public Map getPublicProperties()
	{
		// No public properties
		return new HashMap();
	}

	/**
	 * @see org.eclipse.birt.data.engine.api.script.IDataSetInstance#getQueryText()
	 */
	public String getQueryText() throws BirtException
	{
		return "";
	}

	/**
	 * @see org.eclipse.birt.data.engine.api.script.IDataSetInstance#setQueryText(java.lang.String)
	 */
	public void setQueryText(String queryText) throws BirtException
	{
		// Query text has no effect on script data set
	}
	
}
