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
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.reportitem.api.ChartItemUtil;
import org.eclipse.birt.chart.reportitem.plugin.ChartReportItemPlugin;
import org.eclipse.birt.chart.util.ChartExpressionUtil;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.chart.util.ChartExpressionUtil.ExpressionCodec;
import org.eclipse.birt.chart.util.ChartExpressionUtil.ExpressionSet;
import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.BaseQueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.report.data.adapter.api.IModelAdapter;
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
	 * @deprecated to invoke {@link #createBaseQuery(IDataQueryDefinition)} instead.
	 */
	public IDataQueryDefinition createQuery( IDataQueryDefinition parent )
			throws BirtException
	{
		return createBaseQuery( parent );
	}
	
	@Override
	protected void generateExtraBindings( BaseQueryDefinition query )
			throws ChartException
	{
		List<SeriesDefinition> sdList = ChartUtil.getAllOrthogonalSeriesDefinitions( fChartModel );
		sdList.addAll( ChartUtil.getBaseSeriesDefinitions( fChartModel ) );

		// Iterate all data definitions in series and add binding for complex
		// expression
		ExpressionSet exprSet = new ExpressionSet( );
		for ( int i = 0; i < sdList.size( ); i++ )
		{
			SeriesDefinition sd = sdList.get( i );
			List<Query> queryList = sd.getDesignTimeSeries( )
					.getDataDefinition( );
			for ( int j = 0; j < queryList.size( ); j++ )
			{
				exprSet.add( queryList.get( j ).getDefinition( ) );
			}

			exprSet.add( sd.getQuery( ).getDefinition( ) );
		}
		for ( String expr : exprSet )
		{
			exprCodec.decode( expr );
			if ( !exprCodec.isRowBinding( false ) )
			{
				String bindingName = ChartExpressionUtil.escapeSpecialCharacters( exprCodec.getExpression( ) );
				Binding colBinding = new Binding( bindingName );
				colBinding.setDataType( DataType.ANY_TYPE );
				colBinding.setExpression( ChartReportItemUtil.adaptExpression( exprCodec,
						modelAdapter,
						false ) );

				try
				{
					query.addBinding( colBinding );
				}
				catch ( DataException e )
				{
					throw new ChartException( ChartReportItemPlugin.ID,
							ChartException.DATA_BINDING,
							e );
				}
			}
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
				// Copy aggregation bindings and row bindings from container.
				Iterator<ComputedColumnHandle> iterator = table.columnBindingsIterator( );
				while ( iterator.hasNext( ) )
				{
					ComputedColumnHandle binding = iterator.next( );
					ChartItemUtil.loadExpression( exprCodec, binding );
					// For table case, if it isn't javascript expression, it
					// indicates the expression isn't a basic DatasetRow
					// expression, it should be a binding expression.
					boolean isJavascriptExpr = ( ExpressionCodec.JAVASCRIPT.equals( exprCodec.getType( ) ) );
					if ( binding.getAggregateFunction( ) != null
							|| exprCodec.getRowBindingNameSet( binding.getExpression( ) )
									.size( ) > 0
									|| !isJavascriptExpr )
					{
						try
						{
							query.addBinding( modelAdapter.adaptBinding( binding ) );
						}
						catch ( BirtException e )
						{
							throw new ChartException( ChartReportItemPlugin.ID,
									ChartException.DATA_BINDING,
									e );
						}
					}
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
			for ( Iterator<DesignElementHandle> iter = groups.iterator( ); iter.hasNext( ); )
			{
				groupList.add( (GroupHandle) iter.next( ) );
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
