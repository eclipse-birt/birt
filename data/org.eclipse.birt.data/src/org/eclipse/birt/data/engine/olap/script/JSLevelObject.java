
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
import javax.olap.cursor.DimensionCursor;

import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;


/**
 * 
 */

public class JSLevelObject extends ScriptableObject
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DimensionCursor cursor;
	private String levelName;
	
	JSLevelObject( DimensionCursor cursor, String levelName )
	{
		this.cursor = cursor;
		this.levelName = levelName;
	}
	
	public String getClassName( )
	{
		return "JSLevelObject";
	}

	public Object getKeyValue( )
	{
		try
		{
			return this.cursor.getObject( this.levelName );
		}
		catch ( OLAPException e )
		{
			return null;
		}
	}
	
	/*
	 * @see org.mozilla.javascript.ScriptableObject#get(java.lang.String,
	 *      org.mozilla.javascript.Scriptable)
	 */
	public Object get( String name, Scriptable start )
	{
		try
		{
			return this.cursor.getObject( name );
		}
		catch ( OLAPException e )
		{
			return null;
		}
	}
}
