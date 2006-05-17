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

import org.eclipse.birt.core.script.bre.NativeBirtComp;
import org.eclipse.birt.core.script.bre.NativeBirtDateTime;
import org.eclipse.birt.core.script.bre.NativeBirtMath;
import org.eclipse.birt.core.script.bre.NativeBirtStr;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public class CoreJavaScriptInitializer implements IJavascriptInitializer
{

	public void initialize( Context cx, Scriptable scope )
	{
		try
		{
			ScriptableObject.defineClass( scope, NativeFinance.class );
			ScriptableObject.defineClass( scope, NativeDateTimeSpan.class );
			ScriptableObject.defineClass( scope, NativeBirtDateTime.class );
			ScriptableObject.defineClass( scope, NativeBirtMath.class );
			ScriptableObject.defineClass( scope, NativeBirtStr.class );
			ScriptableObject.defineClass( scope, NativeBirtComp.class );
		}
		catch ( Exception ex )
		{
			assert false;
		}
	}

}
