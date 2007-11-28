/***********************************************************************
 * Copyright (c) 2005, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.reportitem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.reportitem.i18n.Messages;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.IInputParameterBinding;
import org.eclipse.birt.data.engine.api.querydefn.BaseQueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.FilterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.InputParameterBinding;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.SubqueryDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.report.engine.adapter.ModelDteApiAdapter;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.extension.ReportItemQueryBase;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.model.api.AggregationArgumentHandle;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.FilterConditionHandle;
import org.eclipse.birt.report.model.api.ModuleUtil;
import org.eclipse.birt.report.model.api.ParamBindingHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.IReportItem;

/**
 * Customized query implementation for Chart.
 */
public final class ChartReportItemQueryImpl extends ReportItemQueryBase
{

	/**
	 * 
	 */
	private Chart cm = null;

	/**
	 * 
	 */
	private ExtendedItemHandle eih = null;

	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.reportitem/trace" ); //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.extension.IReportItemQuery#setModelObject(org.eclipse.birt.report.model.api.ExtendedItemHandle)
	 */
	public void setModelObject( ExtendedItemHandle eih )
	{
		IReportItem item;
		try
		{
			item = eih.getReportItem( );
			if ( item == null )
			{
				try
				{
					eih.loadExtendedElement( );
				}
				catch ( ExtendedElementException eeex )
				{
					logger.log( eeex );
				}
				item = eih.getReportItem( );
				if ( item == null )
				{
					logger.log( ILogger.ERROR,
							Messages.getString( "ChartReportItemQueryImpl.log.UnableToLocate" ) ); //$NON-NLS-1$
					return;
				}
			}
		}
		catch ( ExtendedElementException e )
		{
			logger.log( ILogger.ERROR,
					Messages.getString( "ChartReportItemQueryImpl.log.UnableToLocate" ) ); //$NON-NLS-1$
			return;
		}
		cm = (Chart) ( (ChartReportItemImpl) item ).getProperty( "chart.instance" ); //$NON-NLS-1$
		this.eih = eih;
	}

	// /*
	// * (non-Javadoc)
	// *
	// * @see
	// org.eclipse.birt.report.engine.extension.IReportItemQuery#getReportQueries(org.eclipse.birt.data.engine.api.IBaseQueryDefinition)
	// */
	// public IBaseQueryDefinition[] getReportQueries(
	// IBaseQueryDefinition ibqdParent ) throws BirtException
	// {
	// logger.log( ILogger.INFORMATION,
	// Messages.getString( "ChartReportItemQueryImpl.log.getReportQueries.start"
	// ) ); //$NON-NLS-1$
	//
	// // BUILD THE QUERY ASSOCIATED WITH THE CHART MODEL
	//
	// IBaseQueryDefinition ibqd = null;
	// try
	// {
	// ibqd = ( new QueryHelper( ) ).build( eih, ibqdParent, cm );
	// }
	// catch ( RuntimeException gex )
	// {
	// logger.log( gex );
	// logger.log( ILogger.INFORMATION,
	// Messages.getString(
	// "ChartReportItemQueryImpl.log.getReportQueries.exception" ) );
	// //$NON-NLS-1$
	// throw new ChartException( ChartReportItemPlugin.ID,
	// ChartException.GENERATION,
	// gex );
	// }
	// logger.log( ILogger.INFORMATION,
	// Messages.getString( "ChartReportItemQueryImpl.log.getReportQueries.end" )
	// ); //$NON-NLS-1$
	// return new IBaseQueryDefinition[]{
	// ibqd
	// };
	// }

	public IDataQueryDefinition[] createReportQueries(
			IDataQueryDefinition parent ) throws BirtException
	{
		logger.log( ILogger.INFORMATION,
				Messages.getString( "ChartReportItemQueryImpl.log.getReportQueries.start" ) ); //$NON-NLS-1$

		IDataQueryDefinition idqd = createQuery( eih, parent );
		logger.log( ILogger.INFORMATION,
				Messages.getString( "ChartReportItemQueryImpl.log.getReportQueries.end" ) ); //$NON-NLS-1$

		return new IBaseQueryDefinition[]{
			(IBaseQueryDefinition) idqd
		};
	}

	protected IDataQueryDefinition createQuery( ExtendedItemHandle handle,
			IDataQueryDefinition parent )
	{
		BaseQueryDefinition parentQuery = null;
		if ( parent instanceof BaseQueryDefinition )
		{
			parentQuery = (BaseQueryDefinition) parent;
		}

		DataSetHandle dsHandle = handle.getDataSet( );

		if ( dsHandle == null )
		{
			// dataset reference error
			String dsName = (String) handle.getProperty( ReportItemHandle.DATA_SET_PROP );
			if ( dsName != null && dsName.length( ) > 0 )
			{
				logger.log( new EngineException( MessageConstants.UNDEFINED_DATASET_ERROR,
						dsName ) );
			}
			// we has data set name defined, so test if we have column
			// binding here.

			if ( parent instanceof ICubeQueryDefinition )
			{
				return null;
				// return createSubQuery(item, null);
			}

			// we have column binding, create a sub query.
			return createSubQuery( handle, parentQuery );
		}
		// The report item has a data set definition, must create a query for
		// it.
		QueryDefinition query = new QueryDefinition( parentQuery );
		query.setDataSetName( dsHandle.getQualifiedName( ) );

		// bind the query with parameters
		query.getInputParamBindings( )
				.addAll( createParamBindings( handle.paramBindingsIterator( ) ) );

		Iterator iter = handle.columnBindingsIterator( );
		while ( iter.hasNext( ) )
		{
			ComputedColumnHandle binding = (ComputedColumnHandle) iter.next( );
			addColumBinding( query, binding );
		}

		addSortAndFilter( handle, query );

		return query;
	}

	protected BaseQueryDefinition createSubQuery( ExtendedItemHandle handle,
			BaseQueryDefinition parentQuery )
	{
		BaseQueryDefinition query = null;
		// sub query must be defined in a transform
		if ( parentQuery == null )
		{
			// no parent query exits, so create a empty query for it.
			query = new QueryDefinition( null );
		}
		else
		{
			// create a sub query
			query = new SubqueryDefinition( "chart_subquery", parentQuery ); //$NON-NLS-1$
			parentQuery.getSubqueries( ).add( query );
		}

		Iterator iter = handle.columnBindingsIterator( );
		while ( iter.hasNext( ) )
		{
			ComputedColumnHandle binding = (ComputedColumnHandle) iter.next( );
			addColumBinding( query, binding );
		}

		addSortAndFilter( handle, query );

		return query;
	}

	private void addSortAndFilter( ExtendedItemHandle handle,
			BaseQueryDefinition query )
	{
		query.getFilters( ).addAll( createFilters( handle.filtersIterator( ) ) );
	}

	/**
	 * create a filter array given a filter condition handle iterator
	 * 
	 * @param iter
	 *            the iterator
	 * @return filter array
	 */
	private ArrayList createFilters( Iterator iter )
	{
		ArrayList filters = new ArrayList( );
		if ( iter != null )
		{

			while ( iter.hasNext( ) )
			{
				FilterConditionHandle filterHandle = (FilterConditionHandle) iter.next( );
				IFilterDefinition filter = createFilter( filterHandle );
				filters.add( filter );
			}
		}
		return filters;
	}

	/**
	 * create one Filter given a filter condition handle
	 * 
	 * @param handle
	 *            a filter condition handle
	 * @return the filter
	 */
	private IFilterDefinition createFilter( FilterConditionHandle handle )
	{
		String filterExpr = handle.getExpr( );
		if ( filterExpr == null || filterExpr.length( ) == 0 )
			return null; // no filter defined

		// converts to DtE exprFilter if there is no operator
		String filterOpr = handle.getOperator( );
		if ( filterOpr == null || filterOpr.length( ) == 0 )
			return new FilterDefinition( new ScriptExpression( filterExpr ) );

		/*
		 * has operator defined, try to convert filter condition to
		 * operator/operand style column filter with 0 to 2 operands
		 */

		String column = filterExpr;
		int dteOpr = ModelDteApiAdapter.toDteFilterOperator( filterOpr );
		if ( ModuleUtil.isListFilterValue( handle ) )
		{
			List operand1List = handle.getValue1List( );
			return new FilterDefinition( new ConditionalExpression( column,
					dteOpr,
					operand1List ) );
		}
		String operand1 = handle.getValue1( );
		String operand2 = handle.getValue2( );
		return new FilterDefinition( new ConditionalExpression( column,
				dteOpr,
				operand1,
				operand2 ) );
	}

	protected void addColumBinding( IBaseQueryDefinition transfer,
			ComputedColumnHandle columnBinding )
	{
		String name = columnBinding.getName( );
		String expr = columnBinding.getExpression( );
		String type = columnBinding.getDataType( );
		int dbType = ModelDteApiAdapter.toDteDataType( type );
		IBaseExpression dbExpr = new ScriptExpression( expr, dbType );
		if ( columnBinding.getAggregateOn( ) != null )
		{
			dbExpr.setGroupName( columnBinding.getAggregateOn( ) );
		}
		IBinding binding = new Binding( name, dbExpr );
		try
		{
			if ( columnBinding.getAggregateOn( ) != null )
				binding.addAggregateOn( columnBinding.getAggregateOn( ) );
			if ( columnBinding.getAggregateFunction( ) != null )
			{
				binding.setAggrFunction( columnBinding.getAggregateFunction( ) );
			}
			String filter = columnBinding.getFilterExpression( );
			if ( filter != null )
			{
				binding.setFilter( new ScriptExpression( filter ) );
			}
			Iterator arguments = columnBinding.argumentsIterator( );
			if ( arguments != null )
			{
				while ( arguments.hasNext( ) )
				{
					AggregationArgumentHandle argumentHandle = (AggregationArgumentHandle) arguments.next( );
					String argument = argumentHandle.getValue( );
					if ( argument != null )
					{
						binding.addArgument( new ScriptExpression( argument ) );
					}
				}
			}
			transfer.addBinding( binding );
		}
		catch ( DataException ex )
		{
			logger.log( ex );
		}
	}

	/**
	 * create input parameter bindings
	 * 
	 * @param iter
	 *            parameter bindings iterator
	 * @return a list of input parameter bindings
	 */
	protected ArrayList createParamBindings( Iterator iter )
	{
		ArrayList list = new ArrayList( );
		if ( iter != null )
		{
			while ( iter.hasNext( ) )
			{
				ParamBindingHandle modelParamBinding = (ParamBindingHandle) iter.next( );
				IInputParameterBinding binding = createParamBinding( modelParamBinding );
				if ( binding != null )
				{
					list.add( binding );
				}
			}
		}
		return list;
	}

	/**
	 * create input parameter binding
	 * 
	 * @param handle
	 * @return
	 */
	protected IInputParameterBinding createParamBinding(
			ParamBindingHandle handle )
	{
		if ( handle.getExpression( ) == null )
			return null; // no expression is bound
		ScriptExpression expr = new ScriptExpression( handle.getExpression( ) );
		// model provides binding by name only
		return new InputParameterBinding( handle.getParamName( ), expr );

	}

}