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

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.BaseQueryDefinition;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.TableHandle;

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
	 */
	public ChartSharingQueryHelper( ReportItemHandle handle, Chart cm )
	{
		super( handle, cm );
	}

	/**
	 * Constructor of the class.
	 * 
	 * @param handle
	 *            the referred report item handle contains actual
	 *            bindings/groupings/filters.
	 * @param chart
	 * @param bCreateBindingForExpression
	 *            indicates if query definition should create a new binding for
	 *            the complex expression. If the expression is simply a binding
	 *            name, always do not add the new binding.
	 */
	public ChartSharingQueryHelper( ReportItemHandle handle, Chart cm,
			boolean bCreateBindingForExpression )
	{
		super( handle, cm, bCreateBindingForExpression );
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
			handleGroup( iter.next( ), query );
		}

		if ( ChartReportItemUtil.isChartInheritGroups( fReportItemHandle ) )
		{
			// Copy aggregations from table container to chart
			TableHandle table = null;
			DesignElementHandle container = fReportItemHandle.getContainer( );
			while ( container != null )
			{
				if ( container instanceof TableHandle )
				{
					table = (TableHandle) container;
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
						addColumBinding( query, binding );
					}
				}
			}
		}
		return query;
	}

	/**
	 * Returns groups in shared binding.
	 * 
	 * @return
	 */
	private List<GroupHandle> getGroups( )
	{
		List<GroupHandle> groupList = new ArrayList<GroupHandle>( );
		TableHandle table = null;
		if ( fReportItemHandle instanceof TableHandle )
		{
			table = (TableHandle) fReportItemHandle;
		}
		else if ( ChartReportItemUtil.isChartInheritGroups( fReportItemHandle ) )
		{
			DesignElementHandle container = fReportItemHandle.getContainer( );
			while ( container != null )
			{
				if ( container instanceof TableHandle )
				{
					table = (TableHandle) container;
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
		if ( handle instanceof TableHandle )
		{
			query.getSorts( )
					.addAll( createSorts( ( (TableHandle) handle ).sortsIterator( ) ) );
		}
	}
}
