
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
package org.eclipse.birt.data.engine.olap.impl.query;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBaseQueryResults;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.DataEngineSession;
import org.eclipse.birt.data.engine.olap.api.ICubeQueryResults;
import org.eclipse.birt.data.engine.olap.api.IPreparedCubeQuery;
import org.eclipse.birt.data.engine.olap.api.query.IBaseCubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.mozilla.javascript.Scriptable;


/**
 * 
 */

public class PreparedCubeQuery implements IPreparedCubeQuery
{
	private ICubeQueryDefinition cubeQueryDefn;
	private DataEngineSession session;
	private DataEngineContext context;
	private Map appContext;
	
	/**
	 * 
	 * @param defn
	 * @param scope
	 */
	public PreparedCubeQuery( ICubeQueryDefinition defn, DataEngineSession session, DataEngineContext context, Map appContext ) throws DataException
	{
		this.cubeQueryDefn = defn;
		this.session = session;
		this.context = context;
		this.appContext = appContext;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.api.IPreparedCubeQuery#execute(org.mozilla.javascript.Scriptable)
	 */
	public ICubeQueryResults execute( Scriptable scope ) throws DataException
	{
		return this.execute( null, scope );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.api.IPreparedCubeQuery#execute(org.eclipse.birt.data.engine.api.IBaseQueryResults, org.mozilla.javascript.Scriptable)
	 */
	public ICubeQueryResults execute( IBaseQueryResults outerResults, Scriptable scope ) throws DataException
	{
		//Create a scope for each query execution.
		 Scriptable cubeScope = session.getEngineContext( )
				.getScriptContext( )
				.getContext( )
				.newObject( scope == null ? this.session.getSharedScope( )
						: scope );
		cubeScope.setParentScope( scope == null ? this.session.getSharedScope( )
				: scope );
		cubeScope.setPrototype( scope == null ? this.session.getSharedScope( )
				: scope );
		
		Object delegateObject = null;
		try
		{
			delegateObject = Thread.currentThread( )
					.getContextClassLoader( )
					.loadClass( "org.eclipse.birt.data.engine.olap.impl.query.PreparedCubeQueryDelegate" )
					.getConstructor( ICubeQueryDefinition.class,
							DataEngineSession.class,
							DataEngineContext.class,
							Map.class )
					.newInstance( cubeQueryDefn, session, context, appContext );
		}
		catch ( ClassNotFoundException e )
		{
		}
		catch ( InstantiationException e )
		{
		}
		catch ( IllegalAccessException e )
		{
		}
		catch ( SecurityException e )
		{
		}
		catch ( IllegalArgumentException e )
		{
		}
		catch ( InvocationTargetException e )
		{
		}
		catch ( NoSuchMethodException e )
		{
		}
		if( delegateObject != null )
		{
			try
			{
				Method method = delegateObject.getClass( )
						.getMethod( "execute", new Class[]{
								IBaseQueryResults.class, Scriptable.class
						} );
				return (ICubeQueryResults) method.invoke( delegateObject,
						new Object[]{
								outerResults, scope
						} );
			}
			catch ( SecurityException e )
			{
			}
			catch ( NoSuchMethodException e )
			{
			}
			catch ( IllegalArgumentException e )
			{
			}
			catch ( IllegalAccessException e )
			{
			}
			catch ( InvocationTargetException e )
			{
			}
		}
		return new CubeQueryResults( outerResults,
				this,
				this.session,
				cubeScope,
				this.context,
				appContext );
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.api.IPreparedCubeQuery#getCubeQueryDefinition()
	 */
	public IBaseCubeQueryDefinition getCubeQueryDefinition( )
	{
		return this.cubeQueryDefn;
	}
}
