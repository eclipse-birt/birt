/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.expression;

import org.mozilla.javascript.Context;

/**
 * 
 */
public class ExpressionCompilerUtil
{
	private static ExpressionCompiler expressionCompiler = new ExpressionCompiler( );

	/**
	 * @param expr
	 * @param registry
	 * @param cx
	 * @return
	 */
	public static CompiledExpression compile( String expr, Context cx )
	{
		return expressionCompiler.compile( expr, null, cx );
	}

}
