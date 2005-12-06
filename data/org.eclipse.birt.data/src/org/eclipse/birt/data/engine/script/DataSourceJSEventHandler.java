/*
 *************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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
package org.eclipse.birt.data.engine.script;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseDataSourceDesign;
import org.eclipse.birt.data.engine.api.script.IDataSourceEventHandler;
import org.eclipse.birt.data.engine.api.script.IDataSourceInstance;
import org.eclipse.birt.data.engine.core.DataException;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class DataSourceJSEventHandler implements IDataSourceEventHandler
{
	private IBaseDataSourceDesign design;
	private JSMethodRunner runner;
	
	public DataSourceJSEventHandler( IBaseDataSourceDesign dataSourceDesign )
	{
		assert dataSourceDesign != null;
		this.design = dataSourceDesign;
	}
	
	protected IBaseDataSourceDesign getBaseDesign()
	{
		return design;
	}
	
	protected JSMethodRunner getRunner( Scriptable scope )
	{
		if ( runner == null )
		{
			String scopeName = "DataSource[" + design.getName() + "]";
			runner = new JSMethodRunner( scope, scopeName );
		}
		return runner;
	}
	
	public void beforeOpen(IDataSourceInstance dataSource) throws BirtException
	{
		String script = getBaseDesign().getBeforeOpenScript();
		if ( script != null && script.length() > 0 )
		{
			getRunner( dataSource.getScriptScope() ).runScript(
					"beforeOpen", script );
		}
	}

	public void beforeClose(IDataSourceInstance dataSource) throws BirtException
	{
		String script = getBaseDesign().getBeforeCloseScript();
		if ( script != null && script.length() > 0 )
		{
			getRunner( dataSource.getScriptScope() ).runScript(
					"beforeClose", script );
		}
	}

	public void afterOpen(IDataSourceInstance dataSource) throws BirtException
	{
		String script = getBaseDesign().getAfterOpenScript();
		if ( script != null && script.length() > 0 )
		{
			getRunner( dataSource.getScriptScope() ).runScript(
					"afterOpen", script );
		}
	}

	public void afterClose(IDataSourceInstance dataSource) throws BirtException
	{
		String script = getBaseDesign().getAfterCloseScript();
		if ( script != null && script.length() > 0 )
		{
			getRunner( dataSource.getScriptScope() ).runScript(
					"afterClose", script );
		}
	}
	
}
