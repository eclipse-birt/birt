
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
import org.eclipse.birt.core.script.functionservice.IScriptFunctionContext;
import org.eclipse.birt.core.script.functionservice.IScriptFunctionExecutor;

/**
 * 
 */
abstract class Function_temp implements IScriptFunctionExecutor
{
	protected int length;
	protected boolean isFixed;
	
	public Object execute( Object[] args, IScriptFunctionContext context  )
	{
		if ( args == null || ( isFixed? args.length != length: args.length > length) )
			throw new IllegalArgumentException( "The number of arguement is incorrect." );
		
		try
		{
			return getValue( args );
		}
		catch ( BirtException e )
		{
			throw new IllegalArgumentException( "The type of arguement is incorrect." );
		}
	}
	protected abstract Object getValue( Object[] args ) throws BirtException;
}