
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
	protected int length;
	protected boolean isFixed;
	
	public Object execute( Object[] args, IScriptFunctionContext context  )
	{
		if ( args == null )
			throw new IllegalArgumentException( Messages.getString( "error.arguement.cannot.empty" ) );
		if ( isFixed )
		{
			if ( args.length != length )
				throw new IllegalArgumentException( Messages.getFormattedString( "error.incorrect.number.function.fixedArgument",
						new Object[]{
								length, args.length
						} ) );
		}
		else
		{
			if ( args.length > length )
				throw new IllegalArgumentException( Messages.getFormattedString( "error.incorrect.number.function.variableArgument",
						new Object[]{
								length, args.length
						} ) );
		}

		try
		{
			return getValue( args );
		}
		catch ( BirtException e )
		{
			throw new IllegalArgumentException( Messages.getString( "error.incorrect.type.function.argument" ) );
		}
	}
	protected abstract Object getValue( Object[] args ) throws BirtException;
}