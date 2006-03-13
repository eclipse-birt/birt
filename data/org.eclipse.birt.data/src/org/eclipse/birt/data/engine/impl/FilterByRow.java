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

import java.util.ArrayList;
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
import org.eclipse.birt.data.engine.odi.FilterUtil;
import org.eclipse.birt.data.engine.odi.IResultObject;
import org.eclipse.birt.data.engine.odi.IResultObjectEvent;
import org.eclipse.birt.data.engine.script.ScriptEvalUtil;
import org.mozilla.javascript.Context;

/**
 * Implementation of IFilter, which will do filtering on row data.
 */
public class FilterByRow implements IResultObjectEvent
{
	//
	public static final int DATASET_FILTER = 1;
	public static final int QUERY_FILTER = 2;
	public static final int ALL_FILTER = 3;
	public static final int NO_FILTER = 4;
	
	//
	private DataSetRuntime dataSet;
	private List currentFilters;
	private List dataSetFilters;
	private List queryFilters;
	private List allFilters;
	private int currentWorkingFilters;
	

	
	protected static Logger logger = Logger.getLogger( FilterByRow.class.getName( ) );
	
	/**
	 * 
	 * @param dataSetFilters
	 * @param queryFilters
	 * @param dataSet
	 * @throws DataException
	 */
	FilterByRow( List dataSetFilters, List queryFilters, DataSetRuntime dataSet ) throws DataException
	{
		isLegal( dataSetFilters );
		isLegal( queryFilters);
		this.dataSet = dataSet;
		
		
		this.dataSetFilters = FilterUtil.sortFilters(dataSetFilters);
		this.queryFilters = FilterUtil.sortFilters(queryFilters);
		
		this.allFilters = getAllFilters( dataSetFilters, queryFilters );
		this.currentWorkingFilters = ALL_FILTER;
		
		logger.log( Level.FINER, "FilterByRow starts up" );
	}

	/**
	 * @param dataSetFilters
	 * @param queryFilters
	 */
	private List getAllFilters( List dataSetFilters, List queryFilters )
	{
		//When the all filters need to be processed at same time,that is, no multi-pass filters exists,
		//the order of filters becomes not important.
		List temp = new ArrayList();
		temp.addAll( dataSetFilters );
		temp.addAll( queryFilters );
		return temp;
	}

	/**
	 * Set the working filter set. The working filter set might be one of followings:
	 * 1. ALL_FILTER
	 * 2. DATASET_FILTER
	 * 3. QUERY_FILTER
	 * 4. NO_FILTER
	 * 
	 * @param filterSetType
	 * @throws DataException
	 */
	public void setWorkingFilterSet( int filterSetType ) throws DataException
	{
		this.validateFilterType( filterSetType );
		this.currentWorkingFilters = filterSetType;
	}
	
	/**
	 * Reset the current working filter set to the default value.
	 *
	 */
	public void restoreWorkingFilterSet()
	{
		this.currentWorkingFilters = ALL_FILTER;
	}
	
	/**
	 * 
	 * @param filterSetType
	 * @return
	 * @throws DataException
	 */	
	public boolean isFilterSetExist( int filterSetType ) throws DataException
	{
		this.validateFilterType( filterSetType );
		if( DATASET_FILTER == filterSetType )
		{
			return this.dataSetFilters.size( ) > 0;
		}
		else if ( QUERY_FILTER == filterSetType )
		{
			return this.queryFilters.size( ) > 0;
		}
		else 
		{
			return this.dataSetFilters.size( )+this.queryFilters.size( ) > 0;
		}
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
			this.currentFilters = this.getFilterList( currentWorkingFilters );
			Iterator filterIt = currentFilters.iterator( );
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
	
	/**
	 * Get the current working filter list.
	 * 
	 * @return
	 * @throws DataException
	 */
	public List getFilterList( ) throws DataException
	{
		return this.getFilterList( this.currentWorkingFilters );
	}
	/**
	 * Get the filter list according to the given filter set type.
	 * 
	 * @param filterSetType
	 * @return
	 * @throws DataException
	 */
	public List getFilterList( int filterSetType ) throws DataException
	{
		validateFilterType( filterSetType );
		if( DATASET_FILTER == filterSetType )
		{
			return this.dataSetFilters;
		}
		else if ( QUERY_FILTER == filterSetType )
		{
			return this.queryFilters;
		}
		else if ( ALL_FILTER == filterSetType )
		{
			return this.allFilters;
		}else
		{
			return new ArrayList();
		}
	}
	
	private void validateFilterType( int filterSetType ) throws DataException
	{
		if( filterSetType != NO_FILTER &&
			filterSetType != DATASET_FILTER &&
			filterSetType != ALL_FILTER &&
			filterSetType != QUERY_FILTER)
		{
			throw new DataException( "shit");
		}
	}

}
