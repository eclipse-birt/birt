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
import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.script.IDataRow;
import org.eclipse.birt.data.engine.api.script.IDataSetEventHandler;
import org.eclipse.birt.data.engine.api.script.IDataSetInstance;
import org.mozilla.javascript.Scriptable;

/**
 * This class handles data set events by executing the Javascript
 * event code.
 * NOTE: functionality of this class will be moved to Engine. This class
 * is temporary 
 */
public class DataSetJSEventHandler implements IDataSetEventHandler
{
	protected IBaseDataSetDesign design;
	protected JSMethodRunner runner;
	
	public DataSetJSEventHandler( IBaseDataSetDesign dataSetDesign )
	{
		this.design = dataSetDesign;
	}

	protected IBaseDataSetDesign getBaseDesign()
	{
		return design;
	}

	protected JSMethodRunner getRunner( Scriptable scope )
	{
		if ( runner == null )
		{
			String scopeName = "DataSet[" + design.getName() + "]";
			runner = new JSMethodRunner( scope, scopeName );
		}
		return runner;
	}
	
	public void beforeOpen(IDataSetInstance dataSet) throws BirtException
	{
		String script = getBaseDesign().getBeforeOpenScript();
		if ( script != null && script.length() > 0 )
		{
			getRunner( dataSet.getScriptScope() ).runScript(
					"beforeOpen", script );
		}
	}

	public void beforeClose(IDataSetInstance dataSet) throws BirtException
	{
		String script = getBaseDesign().getBeforeCloseScript();
		if ( script != null && script.length() > 0 )
		{
			getRunner( dataSet.getScriptScope() ).runScript(
					"beforeClose", script );
		}
	}

	public void afterOpen(IDataSetInstance dataSet) throws BirtException
	{
		String script = getBaseDesign().getAfterOpenScript();
		if ( script != null && script.length() > 0 )
		{
			getRunner( dataSet.getScriptScope() ).runScript(
					"afterOpen", script );
		}
	}

	public void afterClose(IDataSetInstance dataSet) throws BirtException
	{
		String script = getBaseDesign().getAfterCloseScript();
		if ( script != null && script.length() > 0 )
		{
			getRunner( dataSet.getScriptScope() ).runScript(
					"afterClose", script );
		}
	}

	public void onFetch(IDataSetInstance dataSet, IDataRow row) throws BirtException
	{
		String script = getBaseDesign().getOnFetchScript();
		if ( script != null && script.length() > 0 )
		{
			getRunner( dataSet.getScriptScope() ).runScript(
					"onFetch", script );
		}
	}
}
