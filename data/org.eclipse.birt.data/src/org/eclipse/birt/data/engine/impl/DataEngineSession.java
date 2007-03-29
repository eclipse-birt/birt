/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.impl;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.core.script.CoreJavaScriptInitializer;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.executor.DataSetCacheManager;
import org.eclipse.birt.data.engine.executor.cache.CacheUtil;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.Scriptable;

/**
 * Data Engine Session contains DataEngineImpl specific information. Each DataEngineSession
 * only has one DataEngineImpl instance accompany with, and verse visa. 
 */

public class DataEngineSession
{
	private Map context;
	private Scriptable scope;
	private DataSetCacheManager dataSetCacheManager;
	private CacheUtil cacheUtil;
	
	/**
	 * Constructor.
	 * @param engine
	 */
	public DataEngineSession( DataEngineContext context, DataEngine engine )
	{
		this.context = new HashMap();
		
		this.dataSetCacheManager = new DataSetCacheManager( context.getTmpdir( ), engine );
		this.cacheUtil = new CacheUtil( context.getTmpdir( ) );
		
		this.scope = context.getJavaScriptScope( );
		
		Context cx = Context.enter( );
		if ( this.scope == null )
		{
			this.scope = new ImporterTopLevel( cx );
		}
		new CoreJavaScriptInitializer( ).initialize( cx, scope );
		Context.exit( );
		
	}
	
	/**
	 * Get a context property according to given key.
	 * 
	 * @param key
	 * @return
	 */
	public Object get( String key )
	{
		if( key!= null )
			return this.context.get( key );
		return null;
	}
	
	/**
	 * Set a context property with given key.
	 * 
	 * @param key
	 * @param value
	 */
	public void set( String key, Object value )
	{
		this.context.put( key, value );
	}
	
	/**
	 * 
	 * @return
	 */
	public Scriptable getSharedScope( )
	{
		return this.scope;
	}
	
	/**
	 * 
	 * @return
	 */
	public DataSetCacheManager getDataSetCacheManager( )
	{
		return this.dataSetCacheManager;
	}
	
	/**
	 * 
	 * @return
	 */
	public CacheUtil getCacheUtil( )
	{
		return this.cacheUtil;
	}
}
