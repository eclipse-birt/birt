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
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.odi.IResultObject;
import org.eclipse.birt.data.engine.odi.IResultObjectEvent;
import org.eclipse.birt.data.engine.script.JSRowObject;
import org.eclipse.birt.data.engine.script.ScriptEvalUtil;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

/**
 * Implementation of IFilter, which will do filtering on row data.
 */
class FilterByRow implements IResultObjectEvent
{

	protected List filters;
	protected Scriptable scope;
	protected JSRowObject scriptObj;
	
	protected static Logger logger = Logger.getLogger( FilterByRow.class.getName( ) );
	
	FilterByRow( List filters, Scriptable scope, JSRowObject scriptObj )
	{
		this.filters = filters;
		this.scope = scope;
		this.scriptObj = scriptObj;
		logger.log( Level.FINER, "FilterByRow starts up" );
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.odi.IResultObjectEvent#process(org.eclipse.birt.data.engine.odi.IResultObject)
	 */
	public boolean process( IResultObject row , int rowIndex) throws DataException
	{
		logger.entering( FilterByRow.class.getName( ), "process" );
		Context cx = Context.enter();
		try
		{
			boolean isAccepted = true;
			Iterator filterIt = filters.iterator( );
			scriptObj.setRowObject( row, false );
			scriptObj.setCurrentRowIndex( rowIndex );
			while ( filterIt.hasNext( ) )
			{
				IFilterDefinition filter = (IFilterDefinition) filterIt.next( );
				IBaseExpression expr = filter.getExpression( );
	
				Object result = ScriptEvalUtil.evalExpr( expr, cx, scope, "Filter", 0 );
				if ( result == null )
				{
					Object info = null;
					if ( expr instanceof IScriptExpression )
						info = ( (IScriptExpression) expr ).getText( );
					else
						info = expr;
					throw new DataException( ResourceConstants.INVALID_EXPRESSION_IN_FILTER,
							info );
				}
				
				try
				{
					// filter in
					if ( DataTypeUtil.toBoolean( result ).booleanValue( ) == false )
					{
						isAccepted = false;
						break;
					}
				}
				catch ( BirtException e )
				{
			    	DataException e1 = new DataException( ResourceConstants.DATATYPEUTIL_ERROR, e );
					logger.logp( Level.FINE,
							FilterByRow.class.getName( ),
							"process",
							"An error is thrown by DataTypeUtil.",
							e1 );
					throw e1;
				}
			}
			return isAccepted;
		}
		finally
		{
			Context.exit();
			logger.exiting( FilterByRow.class.getName( ), "process" );
		}
	}

}
