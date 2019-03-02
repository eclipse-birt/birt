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
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.expression.CompareHints;
import org.eclipse.birt.data.engine.expression.ExprEvaluator;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.DataSetRuntime.Mode;
import org.eclipse.birt.data.engine.odi.FilterUtil;
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.eclipse.birt.data.engine.odi.IResultObject;

/**
 * Implementation of IFilter, which will do filtering on row data.
 */
public class FilterByRow implements IFilterByRow
{

	//
	public static final int DATASET_FILTER = 1;
	public static final int QUERY_FILTER = 2;
	public static final int ALL_ROW_FILTER = 3;
	public static final int NO_FILTER = 4;
	public static final int GROUP_FILTER = 5;
	public static final int AGGR_FILTER = 6;
	public static final int DATASET_AGGR_FILTER = 7;
	public static final int NOUPDATE_ROW_FILTER = 8;

	//
	private FilterByRowHelper currentFilters;
	private FilterByRowHelper dataSetFilters;
	private FilterByRowHelper dataSetAggrFilters;
	private FilterByRowHelper queryFilters;
	private FilterByRowHelper groupFilters;
	private FilterByRowHelper allRowFilters;
	private FilterByRowHelper aggrFilters;
	private FilterByRowHelper noUpdateRowFilters;
	
	private final ExprEvaluator exprEvaluator;

	protected static Logger logger = Logger.getLogger( FilterByRow.class.getName( ) );

	FilterByRow ( )
	{ 
		exprEvaluator = new ExprEvaluator( );
	}
	
	/**
	 * 
	 * @param dataSetFilters
	 * @param queryFilters
	 * @param dataSet
	 * @throws DataException
	 */
	FilterByRow( List<IFilterDefinition> dataSetFilters, List<IFilterDefinition> queryFilters, List<IFilterDefinition> groupFilters,
			List<IFilterDefinition> aggrFilters, List<IFilterDefinition> dataSetAggrFilters, List<IFilterDefinition> noUpdateRowFilters, DataSetRuntime dataSet ) throws DataException
	{
		this( );
		Object[] params = {
				dataSetFilters, queryFilters, groupFilters, dataSet
		};
		logger.entering( FilterByRow.class.getName( ), "FilterByRow", params );

		if ( dataSetFilters != null && dataSetFilters.size( ) > 0 )
			this.dataSetFilters = new FilterByRowHelper( dataSet,
					Mode.DataSet,
					FilterUtil.sortFilters( dataSetFilters ) );
		if ( queryFilters != null && queryFilters.size( ) > 0 )
			this.queryFilters = new FilterByRowHelper( dataSet,
					Mode.Query,
					FilterUtil.sortFilters( queryFilters ) );
		if ( groupFilters != null && groupFilters.size( ) > 0 )
			this.groupFilters = new FilterByRowHelper( dataSet,
					Mode.Query,
					groupFilters );
		if ( this.dataSetFilters != null || this.queryFilters != null )
			this.allRowFilters = new FilterByRowHelper( dataSet,
					Mode.DataSet,
					getAllRowFilters( dataSetFilters, queryFilters ) );
		if ( aggrFilters != null && aggrFilters.size( ) > 0 )
			this.aggrFilters = new FilterByRowHelper( dataSet,
					Mode.Query,
					aggrFilters );
		if( dataSetAggrFilters!= null && dataSetAggrFilters.size( ) > 0 )
			this.dataSetAggrFilters = new FilterByRowHelper( dataSet,
					Mode.DataSet,
					dataSetAggrFilters );
		
		if( noUpdateRowFilters!=null && noUpdateRowFilters.size( ) > 0 )
			this.noUpdateRowFilters = new FilterByRowHelper( dataSet, 
					Mode.Query,
					noUpdateRowFilters);
		
		this.currentFilters = this.allRowFilters;

		logger.exiting( FilterByRow.class.getName( ), "FilterByRow" );
		logger.log( Level.FINER, "FilterByRow starts up" );
	}

	/**
	 * @param dataSetFilters
	 * @param queryFilters
	 */
	private List getAllRowFilters( List dataSetFilters, List queryFilters )
	{
		// When the all filters need to be processed at same time,that is, no
		// multi-pass filters exists,
		// the order of filters becomes not important.
		List temp = new ArrayList( );
		temp.addAll( dataSetFilters );
		temp.addAll( queryFilters );
		return temp;
	}

	/**
	 * Set the working filter set. The working filter set might be one of
	 * followings: 1. ALL_FILTER 2. DATASET_FILTER 3. QUERY_FILTER 4. NO_FILTER
	 * 5. GROUP_FILTER
	 * 
	 * @param filterSetType
	 * @throws DataException
	 */
	public void setWorkingFilterSet( int filterSetType ) throws DataException
	{
		this.validateFilterType( filterSetType );
		switch ( filterSetType )
		{
			case DATASET_FILTER :
				this.currentFilters = this.dataSetFilters;
				break;
			case QUERY_FILTER :
				this.currentFilters = this.queryFilters;
				break;
			case ALL_ROW_FILTER :
				this.currentFilters = this.allRowFilters;
				break;
			case GROUP_FILTER :
				this.currentFilters = this.groupFilters;
				break;
			case AGGR_FILTER :
				this.currentFilters = this.aggrFilters;
				break;
			case DATASET_AGGR_FILTER :
				this.currentFilters = this.dataSetAggrFilters;
				break;
			case NOUPDATE_ROW_FILTER :
				this.currentFilters = this.noUpdateRowFilters;
				break;
			default :
				this.currentFilters = null;
		}
	}

	/**
	 * Reset the current working filter set to the default value.
	 * 
	 */
	public void restoreWorkingFilterSet( )
	{
		this.currentFilters = this.allRowFilters;
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
		if ( DATASET_FILTER == filterSetType )
		{
			return this.dataSetFilters != null;
		}
		else if ( QUERY_FILTER == filterSetType )
		{
			return this.queryFilters != null;
		}
		else if ( GROUP_FILTER == filterSetType )
		{
			return this.groupFilters != null;
		}
		else if ( AGGR_FILTER == filterSetType )
		{
			return this.aggrFilters != null;
		}
		else if ( DATASET_AGGR_FILTER == filterSetType )
		{
			return this.dataSetAggrFilters != null;
		}
		else if ( NOUPDATE_ROW_FILTER == filterSetType )
		{
			return this.noUpdateRowFilters != null;
		}
		else
		{
			return this.allRowFilters != null;
		}
	}

	/*
	 * @see org.eclipse.birt.data.engine.odi.IResultObjectEvent#process(org.eclipse.birt.data.engine.odi.IResultObject)
	 */
	public boolean process( IResultObject row, int rowIndex )
			throws DataException
	{
		if ( this.currentFilters != null )
			return this.currentFilters.process( row, rowIndex );
		return true;
	}

	/**
	 * Get the current working filter list.
	 * 
	 * @return
	 * @throws DataException
	 */
	public List getFilterList( ) throws DataException
	{
		if ( currentFilters != null )
			return this.currentFilters.getFilters( );
		return new ArrayList( );
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
		switch ( filterSetType )
		{
			case DATASET_FILTER :
				return this.dataSetFilters != null
						? this.dataSetFilters.getFilters( ) : new ArrayList( );
			case QUERY_FILTER :
				return this.queryFilters != null
						? this.queryFilters.getFilters( ) : new ArrayList( );
			case ALL_ROW_FILTER :
				return this.allRowFilters != null
						? this.allRowFilters.getFilters( ) : new ArrayList( );
			case GROUP_FILTER :
				return this.groupFilters != null
						? this.groupFilters.getFilters( ) : new ArrayList( );
			case AGGR_FILTER :
				return this.aggrFilters != null ? this.aggrFilters.getFilters( )
						: new ArrayList( );
			case DATASET_AGGR_FILTER :
				return this.dataSetAggrFilters != null ? this.dataSetAggrFilters.getFilters( )
					: new ArrayList( );
			case NOUPDATE_ROW_FILTER :
				return this.noUpdateRowFilters != null ? this.noUpdateRowFilters.getFilters( )
						: new ArrayList( );
		
			default :
				return new ArrayList( );
		}
	}

	public void deleteFilter( List filter )
	{
		this.deleteFilter( this.aggrFilters, filter );
		this.deleteFilter( this.allRowFilters, filter );
		this.deleteFilter( this.currentFilters, filter );
		this.deleteFilter( this.dataSetAggrFilters, filter );
		this.deleteFilter( this.dataSetFilters, filter );
		this.deleteFilter( this.groupFilters, filter );
		this.deleteFilter( this.noUpdateRowFilters, filter );
		this.deleteFilter( this.queryFilters, filter );
	}
	
	private void deleteFilter( FilterByRowHelper helper, List filter )
	{
		if( helper!= null )
			helper.getFilters( ).removeAll( filter );
	}
	/**
	 * 
	 * @param filterSetType
	 */
	private void validateFilterType( int filterSetType )
	{
		if ( filterSetType != NO_FILTER
				&& filterSetType != DATASET_FILTER
				&& filterSetType != ALL_ROW_FILTER
				&& filterSetType != QUERY_FILTER
				&& filterSetType != GROUP_FILTER
				&& filterSetType != AGGR_FILTER
				&& filterSetType != DATASET_AGGR_FILTER 
				&& filterSetType != NOUPDATE_ROW_FILTER )
		{
			assert false;
		}
	}
	
	public void close( )
	{
		this.exprEvaluator.close( );
	}

	private class FilterByRowHelper
	{

		private DataSetRuntime dataSet;
		private List currentFilters;
		private Mode mode;
		private CompareHints compareHints;

		FilterByRowHelper( DataSetRuntime dataSet, Mode mode, List filters )
		{
			this.dataSet = dataSet;
			this.currentFilters = filters;
			this.mode = mode;
			this.compareHints = new CompareHints( dataSet.getCompareLocator( ), dataSet.getNullest( ) );
		}

		public List getFilters( )
		{
			return this.currentFilters;
		}

		@SuppressWarnings("unchecked")
		public boolean process( IResultObject row, int rowIndex )
				throws DataException
		{
			if( currentFilters.size( ) == 0 )
				return true;
			logger.entering( FilterByRow.class.getName( ), "process" );
			boolean isAccepted = true;
			Iterator filterIt = currentFilters.iterator( );
			IResultIterator cachedIterator = dataSet.getResultSet( );
			dataSet.setRowObject( row, false );
			dataSet.setCurrentRowIndex( rowIndex );
			Mode temp = dataSet.getMode( );
			dataSet.setMode( this.mode );
			try
			{
				while ( filterIt.hasNext( ) )
				{
					IFilterDefinition filter = (IFilterDefinition) filterIt.next( );
					IBaseExpression expr = filter.getExpression( );

					Object result = null;
					try
					{
						/*
						 * if ( helper!= null) result = helper.evaluate( expr );
						 * else result = ScriptEvalUtil.evalExpr( expr,
						 * cx,dataSet.getScriptScope(), "Filter", 0 );
						 */
						if ( expr instanceof IConditionalExpression )
							result = exprEvaluator.evaluateConditionExpression( (IConditionalExpression) expr,
									dataSet.getScriptScope( ),
									true,
									dataSet.getSession( )
											.getEngineContext( )
											.getScriptContext( ),
									compareHints,
									dataSet );
						else
							result = exprEvaluator.evaluateRawExpression2( expr,
									dataSet.getScriptScope( ),
									dataSet.getSession( )
											.getEngineContext( )
											.getScriptContext( ),
									dataSet);
					}
					catch ( BirtException e2 )
					{
						DataException dataEx = DataException.wrap( e2 );
						throw dataEx;
					}

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
						DataException e1 = new DataException( ResourceConstants.DATATYPEUTIL_ERROR,
								e );
						logger.logp( Level.FINE,
								FilterByRow.class.getName( ),
								"process",
								"An error is thrown by DataTypeUtil.",
								e1 );
						throw e1;
					}
				}
				if( cachedIterator!= null )
					this.dataSet.setResultSet( cachedIterator, false );
				return isAccepted;
			}
			finally
			{
				dataSet.setMode( temp );
			}
		}

	}
}
