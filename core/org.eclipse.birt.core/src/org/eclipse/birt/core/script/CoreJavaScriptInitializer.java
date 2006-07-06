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

import org.eclipse.birt.core.script.bre.BirtComp;
import org.eclipse.birt.core.script.bre.BirtDateTime;
import org.eclipse.birt.core.script.bre.BirtMath;
import org.eclipse.birt.core.script.bre.BirtStr;
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
			ScriptableObject birtMath = new BirtMath();
			ScriptableObject birtComp = new BirtComp();
			ScriptableObject birtDateTime = new BirtDateTime();
			ScriptableObject birtStr = new BirtStr();
			ScriptableObject.putProperty( scope, birtMath.getClassName( ), birtMath );
			ScriptableObject.putProperty( scope, birtComp.getClassName(), birtComp );
			ScriptableObject.putProperty( scope, birtDateTime.getClassName( ), birtDateTime );
			ScriptableObject.putProperty( scope, birtStr.getClassName( ), new BirtStr() );
		}
		catch ( Exception ex )
		{
			assert false;
		}
	}

}
