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
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.odi.IResultObject;
import org.eclipse.birt.data.engine.odi.IResultObjectEvent;
import org.eclipse.birt.data.engine.script.ScriptEvalUtil;
import org.mozilla.javascript.Context;

/**
 * Implementation of IFilter, which will do filtering on row data.
 */
public class FilterByRow implements IResultObjectEvent
{
	protected DataSetRuntime dataSet;
	protected List filters;
	
	protected static Logger logger = Logger.getLogger( FilterByRow.class.getName( ) );
	
	FilterByRow( List filters, DataSetRuntime dataSet ) throws DataException
	{
		assert filters!= null && dataSet != null;
		isLegal( filters );
		this.filters = filters;
		this.dataSet = dataSet;
		logger.log( Level.FINER, "FilterByRow starts up" );
	}
	
	/**
	 * whether the filter expression is valid. if not, throw the exception. 
	 * @param filters
	 * @throws DataException
	 */
	private void isLegal( List filters ) throws DataException
	{
		Iterator filterIt = filters.iterator( );
		while ( filterIt.hasNext( ) )
		{
			IFilterDefinition filter = (IFilterDefinition) filterIt.next( );
			IBaseExpression expr = filter.getExpression( );

			if ( expr instanceof IConditionalExpression )
			{
				String expr4Exception = ( (ConditionalExpression) expr ).getExpression( )
						.getText( );
				try
				{
					new FilterExpressionParser( null, null ).compileFilterExpression( expr4Exception );
				}
				catch ( DataException e )
				{
					throw new DataException( ResourceConstants.INVALID_EXPRESSION_IN_FILTER,
							new Object[]{
								expr4Exception
							} );
				}
			}
		}
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
			dataSet.setRowObject( row, false );
			dataSet.setCurrentRowIndex( rowIndex );
			while ( filterIt.hasNext( ) )
			{
				IFilterDefinition filter = (IFilterDefinition) filterIt.next( );
				IBaseExpression expr = filter.getExpression( );
	
				Object result = ScriptEvalUtil.evalExpr( expr, cx, 
							dataSet.getScriptScope(), "Filter", 0 );
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
	
	public List getFilterList()
	{
		return filters;
	}

}
