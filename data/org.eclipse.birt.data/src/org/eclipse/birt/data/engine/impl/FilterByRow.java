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

package org.eclipse.birt.data.engine.impl;

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IFilterDefn;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.odi.IFilter;
import org.eclipse.birt.data.engine.odi.IResultObject;
import org.eclipse.birt.data.engine.script.JSRowObject;
import org.eclipse.birt.data.engine.script.ScriptEvalUtil;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

/**
 * Implementation of IFilter, which will do filtering on row data.
 */
class FilterByRow implements IFilter
{

	protected List filters;
	protected Scriptable scope;
	protected JSRowObject scriptObj;
	
	FilterByRow( List filters, Scriptable scope, JSRowObject scriptObj )
	{
		this.filters = filters;
		this.scope = scope;
		this.scriptObj = scriptObj;
	}
	
	public boolean accept( IResultObject row ) throws DataException
	{
		Context cx = Context.enter();
		try
		{
			boolean isAccepted = true;
			Iterator filterIt = filters.iterator( );
			scriptObj.setRowObject( row, false );
			while ( filterIt.hasNext( ) )
			{
				IFilterDefn filter = (IFilterDefn) filterIt.next( );
				IBaseExpression expr = filter.getExpression( );
	
				Object result = ScriptEvalUtil.evalExpr( expr, cx, scope, "Filter", 0 );
				// filter in
				if ( DataTypeUtil.toBoolean( result ).booleanValue( ) == false )
				{
					isAccepted = false;
					break;
				}
			}
			return isAccepted;
		}
		finally
		{
			Context.exit();
		}
	}

}
