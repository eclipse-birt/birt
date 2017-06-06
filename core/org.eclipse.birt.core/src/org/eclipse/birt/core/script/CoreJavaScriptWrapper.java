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
package org.eclipse.birt.core.script;

import java.util.LinkedHashMap;
import java.util.List;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;


public class CoreJavaScriptWrapper implements IJavascriptWrapper
{

	public Object wrap( Context cx, Scriptable scope, Object javaObject,
			Class staticType )
	{
		if ( javaObject instanceof LinkedHashMap )
		{
			return new NativeJavaLinkedHashMap( scope, javaObject, staticType );
		}
		if ( javaObject instanceof BirtHashMap )
		{
			return new NativeJavaMap( scope, javaObject, staticType );
		}
		if ( javaObject instanceof List )
		{
			// latest change in Rhino implements List interface
			if ( !( javaObject instanceof org.mozilla.javascript.NativeArray ) )
			{
				return new NativeJavaList( scope, javaObject, staticType );
			}
		}

		return javaObject;
	}

}
