/*
 *************************************************************************
 * Copyright (c) 2004-2005 Actuate Corporation.
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

import java.util.Map;

import org.eclipse.birt.data.engine.impl.DataSourceRuntime;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * Implements the JavaScript DataSource[] array, obtained from
 * "report.dataSources"
 */
public class JSDataSources extends ScriptableObject
{
	private Map			dataSources;
	
	/**
	 * Constructor.
	 * @param dataSourceMap A map of data source name (String) to DataSourceRuntime objects
	 */
	public JSDataSources( Map dataSourceMap )
	{
		assert dataSourceMap != null;
		this.dataSources = dataSourceMap;
		
		// This object is not modifiable
		sealObject();
	}
	
	/**
	 * @see org.mozilla.javascript.Scriptable#getClassName()
	 */
	public String getClassName()
	{
		return "DataSources";
	}

	/**
	 * @see org.mozilla.javascript.Scriptable#get(java.lang.String, org.mozilla.javascript.Scriptable)
	 */
	public Object get(String name, Scriptable start)
	{
		DataSourceRuntime ds = (DataSourceRuntime)dataSources.get( name ); 
		if ( ds != null )
		{
			return ds.getScriptable();
		}
		else
		{
			return super.get( name, start);
		}
	}
	
	/**
	 * @see org.mozilla.javascript.Scriptable#getIds()
	 */
	public Object[] getIds()
	{
		// Returns all data source names
		return dataSources.keySet().toArray( new String[0] );
	}
	
	/**
	 * @see org.mozilla.javascript.Scriptable#has(java.lang.String, org.mozilla.javascript.Scriptable)
	 */
	public boolean has(String name, Scriptable start)
	{
		if ( dataSources.containsKey( name ) )
			return true;
		else
			return super.has( name, start);
	}
}
