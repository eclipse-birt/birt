
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

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.expression.CompiledExpression;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * 
 */

public class OLAPExpressionHandler extends CompiledExpression
{
	private Script script;
	
	OLAPExpressionHandler( Script script )
	{
		assert script!= null;
		this.script = script;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.expression.CompiledExpression#evaluate(org.mozilla.javascript.Context, org.mozilla.javascript.Scriptable)
	 */
	public Object evaluate( Context context, Scriptable scope )
			throws DataException
	{
		Object temp = this.script.exec( context, scope );
		if ( temp instanceof ScriptableObject )
		{
			return ((ScriptableObject)temp).getDefaultValue( null );
		}
		return temp;
	}

	public int getType( )
	{
		return 0;
	}
	
}
