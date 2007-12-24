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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.DataType;
import org.eclipse.birt.chart.model.attribute.GroupingUnitType;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.SeriesGrouping;
import org.eclipse.birt.chart.reportitem.i18n.Messages;
import org.eclipse.birt.core.data.ExpressionUtil;
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
import org.eclipse.birt.data.engine.api.querydefn.GroupDefinition;
import org.eclipse.birt.data.engine.api.querydefn.InputParameterBinding;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.SortDefinition;
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
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.emf.common.util.EList;

/**
 * Customized query implementation for Chart.
 */
public final class ChartReportItemQueryImpl extends ReportItemQueryBase
{

	private Chart cm = null;

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

	public IDataQueryDefinition[] createReportQueries(
			IDataQueryDefinition parent ) throws BirtException
	{
		logger.log( ILogger.INFORMATION,
				Messages.getString( "ChartReportItemQueryImpl.log.getReportQueries.start" ) ); //$NON-NLS-1$

		IDataQueryDefinition idqd = createQuery( eih, parent );
		logger.log( ILogger.INFORMATION,
				Messages.getString( "ChartReportItemQueryImpl.log.getReportQueries.end" ) ); //$NON-NLS-1$

		return new IDataQueryDefinition[]{
			idqd
		};
	}

	IDataQueryDefinition createQuery( ExtendedItemHandle handle,
			IDataQueryDefinition parent ) throws BirtException
	{
		if ( handle.getDataSet( ) != null
				|| parent instanceof IBaseQueryDefinition )
		{
			return createBaseQuery( handle, parent );
		}
		else if ( handle.getCube( ) != null
				|| parent instanceof ICubeQueryDefinition )
		{
			return new ChartCubeQueryHelper( handle, cm ).createCubeQuery( parent );
		}
		return null;
	}

	IDataQueryDefinition createBaseQuery( ExtendedItemHandle handle,
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

			if ( ChartReportItemUtil.canScaleShared( handle, cm ) )
			{
				// Add min/max binding to parent query since it's global min/max
				addMinMaxBinding( ChartReportItemUtil.getBindingHolder( handle ),
						parentQuery );
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

		try
		{
			new ChartBaseQueryHelper( cm, handle, query ).generateGroupBindings( );
		}
		catch ( DataException e )
		{
			logger.log( e );
		}

		return query;
	}

	private void addMinMaxBinding( ReportItemHandle handle,
			BaseQueryDefinition query )
	{
		// Add min/max bindings for the query expression in first value
		// series, so share the scale later
		try
		{
			String queryExp = getExpressionOfValueSeries( );

			ComputedColumn ccMin = StructureFactory.newComputedColumn( handle,
					ChartReportItemUtil.QUERY_MIN );
			ccMin.setAggregateFunction( DesignChoiceConstants.AGGREGATION_FUNCTION_MIN );
			ccMin.setExpression( queryExp );
			addColumBinding( query, handle.addColumnBinding( ccMin, false ) );

			ComputedColumn ccMax = StructureFactory.newComputedColumn( handle,
					ChartReportItemUtil.QUERY_MAX );
			ccMax.setAggregateFunction( DesignChoiceConstants.AGGREGATION_FUNCTION_MAX );
			ccMax.setExpression( queryExp );
			addColumBinding( query, handle.addColumnBinding( ccMax, false ) );
		}
		catch ( SemanticException e )
		{
			logger.log( e );
		}
	}

	private String getExpressionOfValueSeries( )
	{
		SeriesDefinition ySd;
		if ( cm instanceof ChartWithAxes )
		{
			Axis yAxis = (Axis) ( (Axis) ( (ChartWithAxes) cm ).getAxes( )
					.get( 0 ) ).getAssociatedAxes( ).get( 0 );
			ySd = (SeriesDefinition) yAxis.getSeriesDefinitions( ).get( 0 );
		}
		else
		{
			ySd = (SeriesDefinition) ( (SeriesDefinition) ( (ChartWithoutAxes) cm ).getSeriesDefinitions( )
					.get( 0 ) ).getSeriesDefinitions( ).get( 0 );
		}
		Query query = (Query) ySd.getDesignTimeSeries( )
				.getDataDefinition( )
				.get( 0 );
		return query.getDefinition( );
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
	protected List createParamBindings( Iterator iter )
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

	/**
	 * Get valid sort expression from series definition.
	 * 
	 * @param sd
	 * @return
	 */
	private String getValidSortExpr( SeriesDefinition sd )
	{
		if ( !sd.isSetSorting( ) )
		{
			return null;
		}

		String sortExpr = null;
		if ( sd.getSortKey( ) != null
				&& sd.getSortKey( ).getDefinition( ) != null )
		{
			sortExpr = sd.getSortKey( ).getDefinition( );
		}
		else
		{
			sortExpr = ( (Query) sd.getDesignTimeSeries( )
					.getDataDefinition( )
					.get( 0 ) ).getDefinition( );
		}
		if ( "".equals( sortExpr ) ) //$NON-NLS-1$
		{
			sortExpr = null;
		}

		return sortExpr;
	}

	/**
	 * The class is responsible to add group bindings of chart on query
	 * definition.
	 * 
	 * @since BIRT 2.3
	 */
	class ChartBaseQueryHelper
	{

		/** The handle of report item handle. */
		private ExtendedItemHandle fReportItemHandle;

		/**
		 * The handle of <code>QueryDefinition</code> which is the container
		 * to contains created group bindings.
		 */
		private QueryDefinition fQueryDefinition;

		/** Current chart handle. */
		private Chart fChart;

		/**
		 * Constructor of the class.
		 * 
		 * @param chart
		 * @param handle
		 * @param query
		 */
		public ChartBaseQueryHelper( Chart chart, ExtendedItemHandle handle,
				QueryDefinition query )
		{
			fChart = chart;
			fReportItemHandle = handle;
			fQueryDefinition = query;
		}

		/**
		 * Generate grouping bindings and add into query definition.
		 * 
		 * @throws DataException
		 */
		private void generateGroupBindings( ) throws DataException
		{
			// 1. Get first base and orthogonal series definition to get
			// grouping definition.
			SeriesDefinition baseSD = null;
			SeriesDefinition orthSD = null;
			Object[] orthAxisArray = null;
			if ( fChart instanceof ChartWithAxes )
			{
				ChartWithAxes cwa = (ChartWithAxes) fChart;
				baseSD = (SeriesDefinition) cwa.getBaseAxes( )[0].getSeriesDefinitions( )
						.get( 0 );

				orthAxisArray = cwa.getOrthogonalAxes( cwa.getBaseAxes( )[0],
						true );
				orthSD = (SeriesDefinition) ( (Axis) orthAxisArray[0] ).getSeriesDefinitions( )
						.get( 0 );
			}
			else if ( fChart instanceof ChartWithoutAxes )
			{
				ChartWithoutAxes cwoa = (ChartWithoutAxes) fChart;
				baseSD = (SeriesDefinition) cwoa.getSeriesDefinitions( )
						.get( 0 );
				orthSD = (SeriesDefinition) baseSD.getSeriesDefinitions( )
						.get( 0 );
			}

			// 2. Add grouping.
			// 2.1 Add Y optional grouping.
			GroupDefinition yGroupingDefinition = createOrthogonalGroupingDefinition( orthSD );
			if ( yGroupingDefinition != null )
			{
				fQueryDefinition.addGroup( yGroupingDefinition );

				// If the SortKey of Y grouping isn't Y grouping expression, add
				// new
				// sort definition on the group.
				// If base grouping is set, the value series should be
				// aggregate.
				if ( ChartReportItemUtil.isBaseGroupingDefined( baseSD )
						&& orthSD.isSetSorting( )
						&& orthSD.getSortKey( ) != null )
				{
					String sortKey = orthSD.getSortKey( ).getDefinition( );
					String yGroupingExpr = orthSD.getQuery( ).getDefinition( );

					// Add additional sort on the grouping.
					if ( sortKey != null && !yGroupingExpr.equals( sortKey ) )
					{
						// If the SortKey does't equal Y grouping expression, we
						// must create new sort definition and calculate
						// aggregate on the grouping and sort by the SortKey.
						String name = StructureFactory.newComputedColumn( fReportItemHandle,
								sortKey.replaceAll( "\"", "" ) ) //$NON-NLS-1$ //$NON-NLS-2$
								.getName( );
						Binding binding = new Binding( name );
						fQueryDefinition.addBinding( binding );

						binding.setExpression( new ScriptExpression( sortKey ) );
						binding.setDataType( org.eclipse.birt.core.data.DataType.ANY_TYPE );
						binding.addAggregateOn( yGroupingDefinition.getName( ) );
						String aggFunc = getAggFunExpr( sortKey,
								baseSD,
								orthAxisArray );
						binding.setAggrFunction( ChartReportItemUtil.convertToDtEAggFunction( aggFunc ) );

						SortDefinition sortDefinition = new SortDefinition( );
						sortDefinition.setColumn( binding.getBindingName( ) );
						sortDefinition.setExpression( ExpressionUtil.createRowExpression( binding.getBindingName( ) ) );
						sortDefinition.setSortDirection( ChartReportItemUtil.convertToDtESortDirection( orthSD.getSorting( ) ) );
						yGroupingDefinition.addSort( sortDefinition );
					}
				}
			}

			// 2.2 Add base grouping.
			GroupDefinition baseGroupDefinition = createBaseGroupingDefinition( baseSD );
			if ( baseGroupDefinition != null )
			{
				fQueryDefinition.addGroup( baseGroupDefinition );
			}

			// 3. Add binding for value series aggregate.
			GroupDefinition innerGroupDef = null;
			if ( fQueryDefinition.getGroups( ) != null
					&& fQueryDefinition.getGroups( ).size( ) > 0 )
			{
				innerGroupDef = (GroupDefinition) fQueryDefinition.getGroups( )
						.get( fQueryDefinition.getGroups( ).size( ) - 1 );
			}

			Map valueExprMap = new HashMap( );
			// If it has base grouping, the value series should be aggregate.
			if ( ChartReportItemUtil.isBaseGroupingDefined( baseSD ) )
			{
				if ( fChart instanceof ChartWithAxes )
				{
					for ( int i = 0; i < orthAxisArray.length; i++ )
					{
						addValueSeriesAggregateBindingForGrouping( fReportItemHandle,
								fQueryDefinition,
								( (Axis) orthAxisArray[i] ).getSeriesDefinitions( ),
								innerGroupDef,
								valueExprMap,
								baseSD );
					}
				}
				else if ( fChart instanceof ChartWithoutAxes )
				{
					addValueSeriesAggregateBindingForGrouping( fReportItemHandle,
							fQueryDefinition,
							baseSD.getSeriesDefinitions( ),
							innerGroupDef,
							valueExprMap,
							baseSD );
				}
			}

			// 4. Binding sort on base series.
			String baseSortExpr = getValidSortExpr( baseSD );
			if ( baseSD.isSetSorting( ) && baseSortExpr != null )
			{
				if ( ChartReportItemUtil.isBaseGroupingDefined( baseSD ) )
				{
					// If base series set group, add sort on group definition.
					String baseExpr = ( (Query) baseSD.getDesignTimeSeries( )
							.getDataDefinition( )
							.get( 0 ) ).getDefinition( );
					if ( baseExpr.equals( getValidSortExpr( baseSD ) ) )
					{
						baseGroupDefinition.setSortDirection( ChartReportItemUtil.convertToDtESortDirection( baseSD.getSorting( ) ) );
					}
					else
					{
						SortDefinition sd = new SortDefinition( );
						sd.setSortDirection( ChartReportItemUtil.convertToDtESortDirection( baseSD.getSorting( ) ) );

						String newValueSeriesExpr = (String) valueExprMap.get( baseSortExpr );
						if ( newValueSeriesExpr != null )
						{
							// Use new expression instead of old.
							baseSD.getSortKey( )
									.setDefinition( newValueSeriesExpr );

							sd.setExpression( newValueSeriesExpr );
						}
						else
						{
							sd.setExpression( baseSortExpr );
						}

						baseGroupDefinition.addSort( sd );
					}
				}
				else
				{
					// If base series doesn't set group, directly add sort on
					// query definition.
					SortDefinition sd = new SortDefinition( );
					sd.setExpression( baseSortExpr );
					sd.setSortDirection( ChartReportItemUtil.convertToDtESortDirection( baseSD.getSorting( ) ) );
					fQueryDefinition.addSort( sd );
				}
			}
		}

		/**
		 * Create Y grouping definition.
		 * 
		 * @param orthSD
		 * @return
		 */
		private GroupDefinition createOrthogonalGroupingDefinition(
				SeriesDefinition orthSD )
		{

			if ( ChartReportItemUtil.isYGroupingDefined( orthSD ) )
			{
				DataType dataType = null;
				GroupingUnitType groupUnit = null;
				double groupIntervalRange = 0; // Default value is 0.

				String yGroupExpr = orthSD.getQuery( ).getDefinition( );

				if ( orthSD.getGrouping( ) != null
						&& orthSD.getGrouping( ).isEnabled( ) )
				{
					dataType = orthSD.getGrouping( ).getGroupType( );
					groupUnit = orthSD.getGrouping( ).getGroupingUnit( );
					groupIntervalRange = orthSD.getGrouping( )
							.getGroupingInterval( );
				}

				GroupDefinition yGroupDefinition = new GroupDefinition( yGroupExpr );

				yGroupDefinition.setKeyExpression( yGroupExpr );

				yGroupDefinition.setInterval( ChartReportItemUtil.convertToDtEGroupUnit( dataType,
						groupUnit,
						groupIntervalRange ) );
				yGroupDefinition.setIntervalRange( ChartReportItemUtil.convertToDtEIntervalRange( dataType,
						groupIntervalRange ) );
				if ( orthSD.isSetSorting( ) )
				{
					yGroupDefinition.setSortDirection( ChartReportItemUtil.convertToDtESortDirection( orthSD.getSorting( ) ) );
				}

				return yGroupDefinition;
			}

			return null;
		}

		/**
		 * Create base grouping definition.
		 * 
		 * @param baseSD
		 * @return
		 */
		private GroupDefinition createBaseGroupingDefinition(
				SeriesDefinition baseSD )
		{
			DataType dataType;
			GroupingUnitType groupUnit;
			double groupIntervalRange;
			if ( ChartReportItemUtil.isBaseGroupingDefined( baseSD ) )
			{
				dataType = baseSD.getGrouping( ).getGroupType( );
				groupUnit = baseSD.getGrouping( ).getGroupingUnit( );
				groupIntervalRange = baseSD.getGrouping( )
						.getGroupingInterval( );
				if ( groupIntervalRange < 0 )
				{
					groupIntervalRange = 0;
				}

				String baseExpr = ( (Query) baseSD.getDesignTimeSeries( )
						.getDataDefinition( )
						.get( 0 ) ).getDefinition( );

				GroupDefinition baseGroupDefinition = new GroupDefinition( baseExpr );

				baseGroupDefinition.setKeyExpression( baseExpr );
				baseGroupDefinition.setInterval( ChartReportItemUtil.convertToDtEGroupUnit( dataType,
						groupUnit,
						groupIntervalRange ) );
				baseGroupDefinition.setIntervalRange( ChartReportItemUtil.convertToDtEIntervalRange( dataType,
						groupIntervalRange ) );

				return baseGroupDefinition;
			}
			return null;
		}

		/**
		 * Add aggregate bindings of value series for grouping case.
		 * 
		 * @param handle
		 * @param query
		 * @param seriesDefinitions
		 * @param innerGroupDef
		 * @param valueExprMap
		 * @param baseSD
		 * @throws DataException
		 */
		private void addValueSeriesAggregateBindingForGrouping(
				ExtendedItemHandle handle, QueryDefinition query,
				EList seriesDefinitions, GroupDefinition innerGroupDef,
				Map valueExprMap, SeriesDefinition baseSD ) throws DataException
		{
			for ( Iterator iter = seriesDefinitions.iterator( ); iter.hasNext( ); )
			{
				SeriesDefinition orthSD = (SeriesDefinition) iter.next( );
				String expr = ( (Query) orthSD.getDesignTimeSeries( )
						.getDataDefinition( )
						.get( 0 ) ).getDefinition( );
				if ( expr != null && !"".equals( expr ) ) //$NON-NLS-1$
				{
					// Get a unique name.
					String name = StructureFactory.newComputedColumn( handle,
							expr.replaceAll( "\"", "" ) ) //$NON-NLS-1$ //$NON-NLS-2$
							.getName( );
					Binding colBinding = new Binding( name );

					colBinding.setDataType( org.eclipse.birt.core.data.DataType.ANY_TYPE );
					colBinding.setExpression( new ScriptExpression( expr ) );
					if ( innerGroupDef != null )
					{
						colBinding.addAggregateOn( innerGroupDef.getName( ) );
						colBinding.setAggrFunction( ChartReportItemUtil.convertToDtEAggFunction( getAggFunExpr( orthSD,
								baseSD ) ) );
					}

					String newExpr = ExpressionUtil.createJSRowExpression( name );
					( (Query) orthSD.getDesignTimeSeries( )
							.getDataDefinition( )
							.get( 0 ) ).setDefinition( newExpr );

					query.addBinding( colBinding );

					valueExprMap.put( expr, newExpr );
				}
			}
		}

		/**
		 * Returns aggregation function expression.
		 * 
		 * @param orthSD
		 * @param baseSD
		 * @return
		 */
		private String getAggFunExpr( SeriesDefinition orthSD, SeriesDefinition baseSD )
		{
			
			String strBaseAggExp = null;
			if ( baseSD.getGrouping( ) != null &&
					baseSD.getGrouping( ).isSetEnabled( ) &&
					baseSD.getGrouping( ).isEnabled( ) )
			{
				strBaseAggExp = baseSD.getGrouping( ).getAggregateExpression( );
			}
			
			return getAggFunExpr( orthSD, strBaseAggExp );
		}

		/**
		 * Returns aggregation function expression.
		 * 
		 * @param orthSD
		 * @param baseAggExp
		 * @return
		 */
		private String getAggFunExpr( SeriesDefinition orthSD,
				String baseAggExp )
		{
			String strOrthoAgg = null;
			SeriesGrouping grouping = orthSD.getGrouping( );
			// Only if base series has enabled grouping
			if ( baseAggExp != null )
			{
				if ( grouping != null &&
						grouping.isSetEnabled( ) &&
						grouping.isEnabled( ) )
				{
					// Set own group
					strOrthoAgg = grouping.getAggregateExpression( );
				}
				
				// Set base group
				if ( strOrthoAgg == null || "".equals( strOrthoAgg ) ) //$NON-NLS-1$
				{
					strOrthoAgg = baseAggExp;
				}
			}
			
			return strOrthoAgg;
		}
		
		/**
		 * Get aggregation function string of sort key related with value
		 * series.
		 * 
		 * @param sortKey
		 * @param baseSD
		 * @param orthAxisArray
		 * @return
		 */
		private String getAggFunExpr( String sortKey,
				SeriesDefinition baseSD, Object[] orthAxisArray )
		{
			String baseAggFunExpr = null;
			if ( baseSD.getGrouping( ) != null &&
					baseSD.getGrouping( ).isSetEnabled( ) &&
					baseSD.getGrouping( ).isEnabled( ) )
			{
				baseAggFunExpr = baseSD.getGrouping( ).getAggregateExpression( );
			}
			
			String aggFunction = null;

			if ( fChart instanceof ChartWithAxes )
			{
				for ( int i = 0; i < orthAxisArray.length; i++ )
				{
					EList sds = ( (Axis) orthAxisArray[i] ).getSeriesDefinitions( );
					for ( Iterator iter = sds.iterator( ); iter.hasNext( ); )
					{
						SeriesDefinition sd = (SeriesDefinition) iter.next( );
						if ( sd.getDesignTimeSeries( ).getDataDefinition( ) != null
								&& sd.getDesignTimeSeries( )
										.getDataDefinition( )
										.get( 0 ) != null )
						{
							Query q = (Query) sd.getDesignTimeSeries( )
									.getDataDefinition( )
									.get( 0 );
							if ( sortKey.equals( q.getDefinition( ) ) )
							{
								aggFunction = getAggFunExpr( sd,
										baseAggFunExpr );
								break;
							}
						}
					}
				}
			}
			else if ( fChart instanceof ChartWithoutAxes )
			{

				for ( Iterator iter = baseSD.getSeriesDefinitions( ).iterator( ); iter.hasNext( ); )
				{
					SeriesDefinition sd = (SeriesDefinition) iter.next( );
					if ( sd.getDesignTimeSeries( ).getDataDefinition( ) != null
							&& sd.getDesignTimeSeries( )
									.getDataDefinition( )
									.get( 0 ) != null )
					{
						Query q = (Query) sd.getDesignTimeSeries( )
								.getDataDefinition( )
								.get( 0 );
						if ( sortKey.equals( q.getDefinition( ) ) )
						{
							aggFunction = sd.getGrouping( )
									.getAggregateExpression( );
							break;
						}
					}
				}

			}
			
			if( aggFunction == null || "".equals( aggFunction )) { //$NON-NLS-1$
				return baseAggFunExpr;
			}
			
			return aggFunction;
		}
	} // End of class ChartGroupBindingManager.
}