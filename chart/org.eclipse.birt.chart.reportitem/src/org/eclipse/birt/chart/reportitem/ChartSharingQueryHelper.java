/***********************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.data.IColumnBinding;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.BaseQueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.report.data.adapter.api.IModelAdapter;
import org.eclipse.birt.report.model.api.AggregationArgumentHandle;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.SlotHandle;

/**
 * The class is used to create query from referred report item handle, it
 * include share query and multi-view cases.
 * 
 * @since 2.3
 */
public class ChartSharingQueryHelper extends ChartBaseQueryHelper
{

	/**
	 * Constructor of the class.
	 * 
	 * @param handle
	 *            the referred report item handle contains actual
	 *            bindings/groupings/filters.
	 * @param chart
	 * @param modelAdapter
	 */
	public ChartSharingQueryHelper( ReportItemHandle handle, Chart cm, IModelAdapter modelAdapter )
	{
		super( handle, cm, modelAdapter );
	}

	/**
	 * Constructor of the class.
	 * 
	 * @param handle
	 *            the referred report item handle contains actual
	 *            bindings/groupings/filters.
	 * @param chart
	 * @param modelAdapter
	 * @param bCreateBindingForExpression
	 *            indicates if query definition should create a new binding for
	 *            the complex expression. If the expression is simply a binding
	 *            name, always do not add the new binding.
	 */
	public ChartSharingQueryHelper( ReportItemHandle handle, Chart cm,
			IModelAdapter modelAdapter, boolean bCreateBindingForExpression )
	{
		super( handle, cm, modelAdapter, bCreateBindingForExpression );
	}

	/**
	 * Create query definition by related report item handle.
	 * 
	 * @param parent
	 * @return query definition
	 * @throws BirtException
	 */
	public IDataQueryDefinition createQuery( IDataQueryDefinition parent )
			throws BirtException
	{
		BaseQueryDefinition query = createQueryDefinition( parent );
		if ( query == null )
		{
			return null;
		}

		// Handle groups.
		List<GroupHandle> groups = getGroups( );
		for ( Iterator<GroupHandle> iter = groups.iterator( ); iter.hasNext( ); )
		{
			handleGroup( iter.next( ), query, modelAdapter );
		}

		if ( ChartReportItemUtil.isChartInheritGroups( fReportItemHandle ) )
		{
			// Copy aggregations from table container to chart
			ListingHandle table = null;
			DesignElementHandle container = fReportItemHandle.getContainer( );
			while ( container != null )
			{
				if ( container instanceof ListingHandle )
				{
					table = (ListingHandle) container;
					break;
				}
				container = container.getContainer( );
			}
			if ( table != null )
			{
				Iterator<ComputedColumnHandle> iterator = table.columnBindingsIterator( );
				while ( iterator.hasNext( ) )
				{
					ComputedColumnHandle binding = iterator.next( );
					if ( binding.getAggregateFunction( ) != null )
					{
						addAggregateBindings( query, binding, table );
					}
				}
			}
		}
		return query;
	}

	private void addAggregateBindings( IBaseQueryDefinition qd,ComputedColumnHandle binding, ListingHandle table ) throws BirtException {
		addColumnBinding( qd, binding );
		Iterator arguments = binding.argumentsIterator( );
		if ( arguments != null )
		{
			while ( arguments.hasNext( ) )
			{
				AggregationArgumentHandle argumentHandle = (AggregationArgumentHandle) arguments.next( );
				String argument = argumentHandle.getValue( );
				if ( argument != null )
				{
					ScriptExpression se = ChartReportItemUtil.newExpression( modelAdapter,
							argumentHandle ); 
					addReferenceBindings( qd, table, se.getText( ) );
				}
				// The aggregation expression should be the first argument, so break here.
				break;
			}
		}
	}

	/**
	 * Adds reference bindings of aggregate binding into query.  
	 * 
	 * @param qd
	 * @param table
	 * @param expression
	 * @throws BirtException
	 * @throws ChartException
	 */
	private void addReferenceBindings( IBaseQueryDefinition qd,
			ListingHandle table, String expression ) throws BirtException,
			ChartException
	{
		List<IColumnBinding> exprs = ExpressionUtil.extractColumnExpressions( expression );
		for ( Iterator<IColumnBinding> iter = exprs.iterator( ); iter.hasNext( ); )
		{
			IColumnBinding cb = iter.next( );
			String cname = cb.getResultSetColumnName( );
			for ( Iterator<ComputedColumnHandle> cbIter =  table.columnBindingsIterator( ); cbIter.hasNext( ); )
			{
				ComputedColumnHandle cch = cbIter.next( );
				if ( cch.getName( ).equals( cname ) )
				{
					if ( cch.getAggregateFunction( ) == null && !qd.getBindings( ).containsKey( cname )) {
						addColumnBinding( qd, cch );
						ScriptExpression se = ChartReportItemUtil.newExpression( modelAdapter,
								cch ); 
						addReferenceBindings( qd, table, se.getText( ) );
					}
					break;
				}
			}
		}
	}
	
	/**
	 * Returns groups in shared binding.
	 * 
	 * @return
	 */
	private List<GroupHandle> getGroups( )
	{
		List<GroupHandle> groupList = new ArrayList<GroupHandle>( );
		ListingHandle table = null;
		if ( fReportItemHandle instanceof ListingHandle )
		{
			table = (ListingHandle) fReportItemHandle;
		}
		else if ( ChartReportItemUtil.isChartInheritGroups( fReportItemHandle ) )
		{
			DesignElementHandle container = fReportItemHandle.getContainer( );
			while ( container != null )
			{
				if ( container instanceof ListingHandle )
				{
					table = (ListingHandle) container;
					break;
				}
				container = container.getContainer( );
			}
		}
		if ( table != null )
		{
			SlotHandle groups = table.getGroups( );
			for ( Iterator<GroupHandle> iter = groups.iterator( ); iter.hasNext( ); )
			{
				groupList.add( iter.next( ) );
			}
		}
		return groupList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.reportitem.ChartBaseQueryHelper#addSortAndFilter
	 * (org.eclipse.birt.report.model.api.ReportItemHandle,
	 * org.eclipse.birt.data.engine.api.querydefn.BaseQueryDefinition)
	 */
	protected void addSortAndFilter( ReportItemHandle handle,
			BaseQueryDefinition query )
	{
		super.addSortAndFilter( handle, query );
		if ( handle instanceof ListingHandle )
		{
			query.getSorts( )
					.addAll( createSorts( ( (ListingHandle) handle ).sortsIterator( ),
							modelAdapter ) );
		}
	}
}
