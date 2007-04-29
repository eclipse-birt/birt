
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
package org.eclipse.birt.data.engine.olap.util;

import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.mozilla.javascript.CompilerEnvirons;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Node;
import org.mozilla.javascript.Parser;
import org.mozilla.javascript.ScriptOrFnNode;

/**
 * 
 */

public class OlapExpressionCompiler
{
	public static String getReferencedScriptObject( IBaseExpression expr, String objectName )
	{
		if ( expr instanceof IScriptExpression )
		{
			return getReferencedScriptObject( ((IScriptExpression)expr), objectName );
		}
		else if ( expr instanceof IConditionalExpression )
		{
			String dimName = null;
			IScriptExpression expr1 = ((IConditionalExpression)expr).getExpression( );
			dimName = getReferencedScriptObject( expr1, objectName );
			if ( dimName!= null )
				return dimName;
			IScriptExpression op1 = ((IConditionalExpression)expr).getOperand1( );
			dimName = getReferencedScriptObject( op1, objectName );
			if ( dimName!= null )
				return dimName;
			
			IScriptExpression op2 = ((IConditionalExpression)expr).getOperand2( );
			dimName = getReferencedScriptObject( op2, objectName );
			return dimName;
		}
		
		return null;
	}
	
	private static String getReferencedScriptObject( IScriptExpression expr, String objectName )
	{
		if ( expr == null )
			return null;
		else 
			return getReferencedScriptObject( expr.getText( ), objectName);
	}
	
	public static String getReferencedScriptObject( String expr, String objectName )
	{
		Context cx = Context.enter();
		CompilerEnvirons ce = new CompilerEnvirons();
		Parser p = new Parser( ce, cx.getErrorReporter( ) );
		ScriptOrFnNode tree = p.parse( expr, null, 0 );
		
		return getScriptObjectName( tree, objectName );
	}
	
	private static String getScriptObjectName( Node n , String objectName )
	{
		if ( n == null )
			return null;
		String result = null;
		if ( n.getType( ) == 38)
		{
			if( objectName.equals( n.getString( ) ))
			{
				Node dimNameNode = n.getNext( );
				if ( dimNameNode == null || dimNameNode.getType( )!= 40 )
					return null;
				
				return dimNameNode.getString( );
			}
		}
	
		result = getScriptObjectName( n.getFirstChild( ), objectName );
		if ( result == null )
			result = getScriptObjectName( n.getLastChild( ), objectName );
				
		return result;
	}
}
