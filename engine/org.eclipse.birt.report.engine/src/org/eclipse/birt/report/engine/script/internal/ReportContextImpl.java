/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.script.internal;

import java.io.Serializable;
import java.util.Map;
import java.util.HashMap;

import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.mozilla.javascript.ScriptableObject;

/**
 * Implementation of the IReportContext interface
 */
public class ReportContextImpl extends ScriptableObject implements
		IReportContext
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6310095484480782798L;

	private Map params;

	private Map config;

	private Map appContext;

	private Map runtimeMap;

	private Map persistantMap;

	public ReportContextImpl( Map params, Map config, Map appContext )
	{
		this.params = params;
		this.config = config;
		this.appContext = appContext;
		runtimeMap = new HashMap( );
		persistantMap = new HashMap( );
	}

	public Map getParams( )
	{
		return params;
	}

	public Map getConfig( )
	{
		return config;
	}

	public Map getAppContext( )
	{
		return appContext;
	}

	public void setAppContext( Map appContext )
	{
		this.appContext = appContext;
	}

	public void addToTask( String name, Object obj )
	{
		runtimeMap.put( name, obj );

	}

	public void removeFromTask( String name )
	{
		runtimeMap.remove( name );
	}

	public Object getFromTask( String name )
	{
		return runtimeMap.get( name );
	}

	public void addToDocument( String name, Serializable obj )
	{
		persistantMap.put( name, obj );
	}

	public void removeFromDocument( String name )
	{
		persistantMap.remove( name );
	}

	public Object getFromDocument( String name )
	{
		return persistantMap.get( name );
	}

	public Map getTransientObjects( )
	{
		return runtimeMap;
	}

	public Map getPersistantObjects( )
	{
		return persistantMap;
	}

	public void setRegisteredPersistantObjects( Map persistantMap )
	{
		this.persistantMap = persistantMap;
	}

	public String getClassName( )
	{
		return "ReportContext";
	}

}
