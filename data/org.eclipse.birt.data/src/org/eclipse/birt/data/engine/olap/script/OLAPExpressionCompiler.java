
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

import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.mozilla.javascript.CompilerEnvirons;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Interpreter;
import org.mozilla.javascript.Parser;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.ScriptOrFnNode;

/**
 * 
 */

public class OLAPExpressionCompiler
{
	/**
	 * 
	 * @param cx
	 */
	public static void compile( Context cx, IBaseExpression expr )
	{
		if ( expr instanceof IConditionalExpression )
		{
			prepareScriptExpression( cx,
					( (IConditionalExpression) expr ).getExpression( ) );
			prepareScriptExpression( cx,
					( (IConditionalExpression) expr ).getOperand1( ) );
			prepareScriptExpression( cx,
					( (IConditionalExpression) expr ).getOperand2( ) );
		}
		else if ( expr instanceof IScriptExpression )
		{
			prepareScriptExpression( cx, (IScriptExpression) expr );
		}
	}

	/**
	 * 
	 * @param cx
	 * @param expr1
	 */
	private static void prepareScriptExpression( Context cx, IScriptExpression expr1 )
	{
		if ( expr1 == null )
			return;
		
		String exprText = expr1.getText( );

		CompilerEnvirons compilerEnv = new CompilerEnvirons( );
		compilerEnv.initFromContext( cx );
		Parser p = new Parser( compilerEnv, cx.getErrorReporter( ) );

		ScriptOrFnNode tree = p.parse( exprText, null, 0 );
		Interpreter compiler = new Interpreter( );
		Object compiledOb = compiler.compile( compilerEnv,
				tree,
				null,
				false );
		Script script = (Script) compiler.createScriptObject( compiledOb,
				null );
		expr1.setHandle( new OLAPExpressionHandler( script ));
	}

	/**
	 * 
	 * @param expression
	 */
	public static void compile( IBaseExpression expression )
	{
		try
		{
			Context cx = Context.enter( );
			compile( cx, expression );
		}
		finally
		{
			Context.exit( );
		}

	}
}
