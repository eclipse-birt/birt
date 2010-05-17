
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
package org.eclipse.birt.core.script.function.bre;


import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.function.i18n.Messages;
import org.eclipse.birt.core.script.functionservice.IScriptFunctionContext;
import org.eclipse.birt.core.script.functionservice.IScriptFunctionExecutor;

/**
 * 
 */
abstract class Function_temp implements IScriptFunctionExecutor
{
	protected int minParamCount;
	protected int maxParamCount;
	
	public Object execute( Object[] args, IScriptFunctionContext context  ) throws BirtException
	{
		if ( args == null )
			throw new BirtException( "org.eclipse.birt.core.script.function", "error.arguement.cannot.empty",
					Messages.RESOURCE_BUNDLE);
		if ( args.length < minParamCount || args.length > maxParamCount )
		{
			throw new BirtException( "org.eclipse.birt.core.script.function", "error.argument.number.outofValidRange", 
					new Object[]{ minParamCount, maxParamCount, args.length },
					Messages.RESOURCE_BUNDLE);
		}
		return getValue( args );

	}
	protected abstract Object getValue( Object[] args ) throws BirtException;
}