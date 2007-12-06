
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
package org.eclipse.birt.data.engine.olap.script;

import javax.olap.OLAPException;
import javax.olap.cursor.CubeCursor;

import org.eclipse.birt.data.engine.script.ScriptConstants;
import org.eclipse.birt.data.engine.script.ScriptEvalUtil;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;


/**
 * The scriptable object which bound with key word "row" in cube query.
 */

public class JSCubeBindingObject extends ScriptableObject
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8830069667117258594L;
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
			if( ScriptConstants.OUTER_RESULT_KEYWORD.equals( arg0 ))
				return cursor.getObject( ScriptEvalUtil.OUTER_RESULTS_SCRIPTABLE );
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
