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

package org.eclipse.birt.report.data.adapter.api;

import javax.olap.OLAPException;
import javax.olap.cursor.CubeCursor;

import org.eclipse.birt.data.engine.api.IResultIterator;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * This class implement some utility methods that can be used by the consumer of Data Engine.
 */

public class DataAdapterUtil
{
	/**
	 * This method is used to register the Java Script Objects which are defined in the scope of
	 * source ResultSet ( might be IResultSet or CubeCursor ) to target scope. One possible client
	 * of this method is Report Engine. A classic use case is that instead of register its own "row" object 
	 * the Report Engine can simply call this method with proper argument so that the "row" object
	 * registered in IResultIterator's scope, that is, JSResultSetRow, can be accessed by engine using
	 * engine scope. 
	 *   
	 * @param targetScope
	 * @param source
	 */
	public static void registerJSObject( Scriptable targetScope, Object source )
	{
		if ( source instanceof IResultIterator )
		{
			Scriptable scope = ( (IResultIterator) source ).getScope( );
			targetScope.put( "row", targetScope, scope.get( "row", scope ) );

		}
		else if ( source instanceof CubeCursor )
		{
			targetScope.put( "row", targetScope, new JSCubeBindingObject( (CubeCursor)source ) );
		}
	}
	
	/**
	 * The scriptable object which bound with key word "row" in cube query.
	 */

	private static class JSCubeBindingObject extends ScriptableObject
	{
		private CubeCursor cursor;
		
		public JSCubeBindingObject( CubeCursor cursor )
		{
			this.cursor = cursor;
		}
		
		/*
		 * (non-Javadoc)
		 * @see org.mozilla.javascript.ScriptableObject#get(java.lang.String, org.mozilla.javascript.Scriptable)
		 */
		public Object get( String arg0, Scriptable scope )
		{
			try
			{
				return cursor.getObject( arg0 );
			}
			catch ( OLAPException e )
			{
				return null;
			}
		}
		
		/*
		 * (non-Javadoc)
		 * @see org.mozilla.javascript.ScriptableObject#getClassName()
		 */
		public String getClassName( )
		{
			return "JSCubeBindingObject";
		}

	}
}
