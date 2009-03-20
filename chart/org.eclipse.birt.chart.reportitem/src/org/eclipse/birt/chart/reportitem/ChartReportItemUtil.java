/*******************************************************************************
 * Copyright (c) 2007, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.reportitem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.eclipse.birt.chart.aggregate.IAggregateFunction;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.DataType;
import org.eclipse.birt.chart.model.attribute.GroupingUnitType;
import org.eclipse.birt.chart.model.attribute.SortOption;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.reportitem.i18n.Messages;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.birt.chart.util.SecurityUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.aggregation.api.IBuildInAggregation;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IGroupDefinition;
import org.eclipse.birt.data.engine.api.ISubqueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.extension.IQueryResultSet;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.ListGroupHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.api.MultiViewsHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.util.DimensionUtil;
import org.eclipse.emf.common.util.EList;

/**
 * Utility class for Chart integration as report item
 */

public class ChartReportItemUtil implements ChartReportItemConstants
{

	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.reportitem/trace" ); //$NON-NLS-1$

	/**
	 * Returns the element handle which can save binding columns the given
	 * element
	 * 
	 * @param handle
	 *            the handle of the element which needs binding columns
	 * @return the holder for the element,or itself if no holder available
	 */
	public static ReportItemHandle getBindingHolder( DesignElementHandle handle )
	{
		if ( handle instanceof ReportElementHandle )
		{
			
			if ( handle instanceof ReportItemHandle )
			{
				if ( ( (ReportItemHandle) handle ).getDataBindingReference( ) != null
						|| ( (ReportItemHandle) handle ).getCube( ) != null
						|| ( (ReportItemHandle) handle ).getDataSet( ) != null )
				{
					return (ReportItemHandle) handle;
				}
			}

			return getBindingHolder( handle.getContainer( ) );
		}
		return null;
	}

	/**
	 * Checks if shared scale is needed when computation
	 * 
	 * @param eih
	 *            handle
	 * @param cm
	 *            chart model
	 * @return shared binding needed or not
	 * @since 2.3
	 */
	public static boolean canScaleShared( ReportItemHandle eih, Chart cm )
	{
		return cm instanceof ChartWithAxes
				&& eih.getDataSet( ) == null
				&& getBindingHolder( eih ) != null
				&& ChartXTabUtil.isInXTabMeasureCell( eih );
	}

	/**
	 * @return Returns if current eclipse environment is RtL.
	 */
	public static boolean isRtl( )
	{
		// get -dir rtl option
		boolean rtl = false;
		String eclipseCommands = SecurityUtil.getSysProp( "eclipse.commands" ); //$NON-NLS-1$
		if ( eclipseCommands != null )
		{
			String[] options = eclipseCommands.split( "-" ); //$NON-NLS-1$
			String regex = "[\\s]*[dD][iI][rR][\\s]*[rR][tT][lL][\\s]*"; //$NON-NLS-1$
			Pattern pattern = Pattern.compile( regex );
			for ( int i = 0; i < options.length; i++ )
			{
				String option = options[i];
				if ( pattern.matcher( option ).matches( ) )
				{
					rtl = true;
					break;
				}
			}
		}
		return rtl;
	}

	/**
	 * Gets all column bindings from handle and its container
	 * 
	 * @param itemHandle
	 *            handle
	 * @return Iterator of all bindings
	 */
	public static Iterator<ComputedColumnHandle> getColumnDataBindings(
			ReportItemHandle itemHandle )
	{
		return getColumnDataBindings( itemHandle, false );
	}

	/**
	 * Gets all column bindings from handle and its container.
	 * 
	 * @param itemHandle
	 * @param unique
	 *            <code>true</code> will ignore the binding of container if it
	 *            is duplicate between handle and its container.
	 * @return ComputedColumnHandle iterator
	 * @since 2.3.2
	 */
	public static Iterator<ComputedColumnHandle> getColumnDataBindings(
			ReportItemHandle itemHandle, boolean unique )
	{
		if ( itemHandle.getDataSet( ) != null || itemHandle.getCube( ) != null )
		{
			return itemHandle.columnBindingsIterator( );
		}
		ReportItemHandle handle = getBindingHolder( itemHandle );
		if ( handle == null )
		{
			return null;
		}

		Map<String, ComputedColumnHandle> bindingMap = new LinkedHashMap<String, ComputedColumnHandle>( );
		ArrayList<ComputedColumnHandle> list = new ArrayList<ComputedColumnHandle>( );
		Iterator<ComputedColumnHandle> i = handle.columnBindingsIterator( );
		while ( i.hasNext( ) )
		{
			ComputedColumnHandle cch = i.next( );
			list.add( cch );
			bindingMap.put( cch.getName( ), cch );
		}
		if ( handle != itemHandle )
		{
			// Do not add same handle twice
			i = itemHandle.columnBindingsIterator( );
			while ( i.hasNext( ) )
			{
				ComputedColumnHandle cch = i.next( );
				list.add( cch );
				bindingMap.put( cch.getName( ), cch );
			}
		}
		if ( unique )
		{
			return bindingMap.values( ).iterator( );
		}
		else
			return list.iterator( );
		
	}

	/**
	 * Convert group unit type from Chart's to DtE's.
	 * 
	 * @param dataType
	 * @param groupUnitType
	 * @param intervalRange
	 * @since BIRT 2.3
	 */
	public static int convertToDtEGroupUnit( DataType dataType,
			GroupingUnitType groupUnitType, double intervalRange )
	{
		if ( dataType == DataType.NUMERIC_LITERAL )
		{
			if ( intervalRange == 0 )
			{
				return IGroupDefinition.NO_INTERVAL;
			}

			return IGroupDefinition.NUMERIC_INTERVAL;
		}
		else if ( dataType == DataType.DATE_TIME_LITERAL )
		{
			switch ( groupUnitType.getValue( ) )
			{
				case GroupingUnitType.SECONDS :
					return IGroupDefinition.SECOND_INTERVAL;

				case GroupingUnitType.MINUTES :
					return IGroupDefinition.MINUTE_INTERVAL;

				case GroupingUnitType.HOURS :
					return IGroupDefinition.HOUR_INTERVAL;

				case GroupingUnitType.DAYS :
					return IGroupDefinition.DAY_INTERVAL;

				case GroupingUnitType.WEEKS :
					return IGroupDefinition.WEEK_INTERVAL;

				case GroupingUnitType.MONTHS :
					return IGroupDefinition.MONTH_INTERVAL;

				case GroupingUnitType.QUARTERS :
					return IGroupDefinition.QUARTER_INTERVAL;

				case GroupingUnitType.YEARS :
					return IGroupDefinition.YEAR_INTERVAL;
			}
		}
		else if ( dataType == DataType.TEXT_LITERAL )
		{
			switch ( groupUnitType.getValue( ) )
			{
				case GroupingUnitType.STRING_PREFIX :
					return IGroupDefinition.STRING_PREFIX_INTERVAL;
			}

			return IGroupDefinition.NO_INTERVAL;
		}

		return IGroupDefinition.NO_INTERVAL;
	}

	/**
	 * Convert interval range from Chart's to DtE's.
	 * 
	 * @param dataType
	 * @param groupUnitType
	 * @param intervalRange
	 * @since BIRT 2.3
	 */
	public static double convertToDtEIntervalRange( DataType dataType,
			GroupingUnitType groupUnitType, double intervalRange )
	{
		double range = intervalRange;
		if ( Double.isNaN( intervalRange ) )
		{
			range = 0;
		}

		if ( dataType == DataType.DATE_TIME_LITERAL && range <= 0 )
		{
			range = 1;
		}
		else if ( dataType == DataType.TEXT_LITERAL )
		{
			return (long) range;
		}

		return range;
	}

	/**
	 * Convert sort direction from Chart's to DtE's.
	 * 
	 * @param sortOption
	 * @since BIRT 2.3
	 */
	public static int convertToDtESortDirection( SortOption sortOption )
	{
		if ( sortOption == SortOption.ASCENDING_LITERAL )
		{
			return IGroupDefinition.SORT_ASC;
		}
		else if ( sortOption == SortOption.DESCENDING_LITERAL )
		{
			return IGroupDefinition.SORT_DESC;
		}
		return IGroupDefinition.NO_SORT;
	}

	/**
	 * Convert aggregation name from Chart's to DtE's.
	 * 
	 * @param agg
	 * @since BIRT 2.3
	 */
	public static String convertToDtEAggFunction( String agg )
	{
		if ( PluginSettings.DefaultAggregations.SUM.equals( agg ) )
		{
			return IBuildInAggregation.TOTAL_SUM_FUNC;

		}
		else if ( PluginSettings.DefaultAggregations.AVERAGE.equals( agg ) )
		{
			return IBuildInAggregation.TOTAL_AVE_FUNC;

		}
		else if ( PluginSettings.DefaultAggregations.COUNT.equals( agg ) )
		{
			return IBuildInAggregation.TOTAL_COUNT_FUNC;

		}
		else if ( PluginSettings.DefaultAggregations.DISTINCT_COUNT.equals( agg ) )
		{
			return IBuildInAggregation.TOTAL_COUNTDISTINCT_FUNC;

		}
		else if ( PluginSettings.DefaultAggregations.FIRST.equals( agg ) )
		{
			return IBuildInAggregation.TOTAL_FIRST_FUNC;

		}
		else if ( PluginSettings.DefaultAggregations.LAST.equals( agg ) )
		{
			return IBuildInAggregation.TOTAL_LAST_FUNC;

		}
		else if ( PluginSettings.DefaultAggregations.MIN.equals( agg ) )
		{
			return IBuildInAggregation.TOTAL_MIN_FUNC;

		}
		else if ( PluginSettings.DefaultAggregations.MAX.equals( agg ) )
		{
			return IBuildInAggregation.TOTAL_MAX_FUNC;
		}
		else if ( PluginSettings.DefaultAggregations.WEIGHTED_AVERAGE.equals( agg ) )
		{
			return IBuildInAggregation.TOTAL_WEIGHTEDAVE_FUNC;
		}
		else if ( PluginSettings.DefaultAggregations.MEDIAN.equals( agg ) )
		{
			return IBuildInAggregation.TOTAL_MEDIAN_FUNC;
		}
		else if ( PluginSettings.DefaultAggregations.MODE.equals( agg ) )
		{
			return IBuildInAggregation.TOTAL_MODE_FUNC;
		}
		else if ( PluginSettings.DefaultAggregations.STDDEV.equals( agg ) )
		{
			return IBuildInAggregation.TOTAL_STDDEV_FUNC;
		}
		else if ( PluginSettings.DefaultAggregations.VARIANCE.equals( agg ) )
		{
			return IBuildInAggregation.TOTAL_VARIANCE_FUNC;
		}
		else if ( PluginSettings.DefaultAggregations.IRR.equals( agg ) )
		{
			return IBuildInAggregation.TOTAL_IRR_FUNC;
		}
		else if ( PluginSettings.DefaultAggregations.MIRR.equals( agg ) )
		{
			return IBuildInAggregation.TOTAL_MIRR_FUNC;
		}
		else if ( PluginSettings.DefaultAggregations.NPV.equals( agg ) )
		{
			return IBuildInAggregation.TOTAL_NPV_FUNC;
		}
		else if ( PluginSettings.DefaultAggregations.PERCENTILE.equals( agg ) )
		{
			return IBuildInAggregation.TOTAL_PERCENTILE_FUNC;
		}
		else if ( PluginSettings.DefaultAggregations.QUARTILE.equals( agg ) )
		{
			return IBuildInAggregation.TOTAL_QUARTILE_FUNC;
		}
		else if ( PluginSettings.DefaultAggregations.MOVING_AVERAGE.equals( agg ) )
		{
			return IBuildInAggregation.TOTAL_MOVINGAVE_FUNC;
		}
		else if ( PluginSettings.DefaultAggregations.RUNNING_SUM.equals( agg ) )
		{
			return IBuildInAggregation.TOTAL_RUNNINGSUM_FUNC;
		}
		else if ( PluginSettings.DefaultAggregations.RUNNING_NPV.equals( agg ) )
		{
			return IBuildInAggregation.TOTAL_RUNNINGNPV_FUNC;
		}
		else if ( PluginSettings.DefaultAggregations.RANK.equals( agg ) )
		{
			return IBuildInAggregation.TOTAL_RANK_FUNC;
		}
		else if ( PluginSettings.DefaultAggregations.TOP.equals( agg ) )
		{
			return IBuildInAggregation.TOTAL_TOP_N_FUNC;
		}
		else if ( PluginSettings.DefaultAggregations.TOP_PERCENT.equals( agg ) )
		{
			return IBuildInAggregation.TOTAL_TOP_PERCENT_FUNC;
		}
		else if ( PluginSettings.DefaultAggregations.BOTTOM.equals( agg ) )
		{
			return IBuildInAggregation.TOTAL_BOTTOM_N_FUNC;
		}
		else if ( PluginSettings.DefaultAggregations.BOTTOM_PERCENT.equals( agg ) )
		{
			return IBuildInAggregation.TOTAL_BOTTOM_PERCENT_FUNC;
		}
		else if ( PluginSettings.DefaultAggregations.PERCENT_RANK.equals( agg ) )
		{
			return IBuildInAggregation.TOTAL_PERCENT_RANK_FUNC;
		}
		else if ( PluginSettings.DefaultAggregations.PERCENT_SUM.equals( agg ) )
		{
			return IBuildInAggregation.TOTAL_PERCENTSUM_FUNC;
		}
		else if ( PluginSettings.DefaultAggregations.RUNNING_COUNT.equals( agg ) )
		{
			return IBuildInAggregation.TOTAL_RUNNINGCOUNT_FUNC;
		}

		return null;
	}

	/**
	 * Checks if result set is empty
	 * 
	 * @param set
	 *            result set
	 * @throws BirtException
	 * @since 2.3
	 */
	public static boolean isEmpty( IBaseResultSet set ) throws BirtException
	{
		if ( set instanceof IQueryResultSet )
		{
			return ( (IQueryResultSet) set ).isEmpty( );
		}
		// TODO add code to check empty for ICubeResultSet
		return false;
	}

	/**
	 * Check if Y grouping is defined.
	 * 
	 * @param orthSeriesDefinition
	 * @since BIRT 2.3
	 */
	public static boolean isYGroupingDefined(
			SeriesDefinition orthSeriesDefinition )
	{
		if ( orthSeriesDefinition == null )
		{
			return false;
		}
		String yGroupExpr = null;
		if ( orthSeriesDefinition.getQuery( ) != null )
		{
			yGroupExpr = orthSeriesDefinition.getQuery( ).getDefinition( );
		}

		return yGroupExpr != null && !"".equals( yGroupExpr ); //$NON-NLS-1$
	}

	/**
	 * Check if base series grouping is defined.
	 * 
	 * @param baseSD
	 * @since BIRT 2.3
	 */
	public static boolean isBaseGroupingDefined( SeriesDefinition baseSD )
	{
		if ( baseSD != null && !baseSD.getDesignTimeSeries( ).getDataDefinition( ).isEmpty( )
				&& baseSD.getGrouping( ) != null
				&& baseSD.getGrouping( ).isEnabled( ) )
		{
			return true;
		}

		return false;
	}

	/**
	 * Check if current chart has defined grouping.
	 * 
	 * @param cm
	 * @since BIRT 2.3
	 */
	public static boolean isGroupingDefined( Chart cm )
	{
		SeriesDefinition baseSD = null;
		SeriesDefinition orthSD = null;
		Object[] orthAxisArray = null;
		if ( cm instanceof ChartWithAxes )
		{
			ChartWithAxes cwa = (ChartWithAxes) cm;
			baseSD = cwa.getBaseAxes( )[0].getSeriesDefinitions( )
					.get( 0 );

			orthAxisArray = cwa.getOrthogonalAxes( cwa.getBaseAxes( )[0], true );
			orthSD = ( (Axis) orthAxisArray[0] ).getSeriesDefinitions( )
					.get( 0 );
		}
		else if ( cm instanceof ChartWithoutAxes )
		{
			ChartWithoutAxes cwoa = (ChartWithoutAxes) cm;
			baseSD = cwoa.getSeriesDefinitions( ).get( 0 );
			orthSD = baseSD.getSeriesDefinitions( ).get( 0 );
		}

		if ( isBaseGroupingDefined( baseSD ) || isYGroupingDefined( orthSD ) )
		{
			return true;
		}

		return false;
	}

	public static boolean isBaseGroupingDefined( Chart cm )
	{
		SeriesDefinition baseSD = null;
		if ( cm instanceof ChartWithAxes )
		{
			ChartWithAxes cwa = (ChartWithAxes) cm;
			baseSD = cwa.getBaseAxes( )[0].getSeriesDefinitions( )
					.get( 0 );
		}
		else if ( cm instanceof ChartWithoutAxes )
		{
			ChartWithoutAxes cwoa = (ChartWithoutAxes) cm;
			baseSD = cwoa.getSeriesDefinitions( ).get( 0 );
		}

		if ( isBaseGroupingDefined( baseSD ) )
		{
			return true;
		}

		return false;
	}
	/**
	 * Check if running aggregates are set on chart.
	 * 
	 * @param cm
	 * @return set or not
	 * @throws ChartException
     * @since 2.3.1
	 */
	public static boolean isSetRunningAggregation( Chart cm )
			throws ChartException
	{
		SeriesDefinition baseSD = ChartUtil.getBaseSeriesDefinitions( cm )
				.get( 0 );
		for ( SeriesDefinition orthoSD : ChartUtil.getAllOrthogonalSeriesDefinitions( cm ) )
		{
			for ( Query query : orthoSD.getDesignTimeSeries( )
					.getDataDefinition( ) )
			{
				String aggrFunc = ChartUtil.getAggregateFuncExpr( orthoSD,
						baseSD,
						query );
				if ( aggrFunc == null )
				{
					continue;
				}

				IAggregateFunction aFunc = PluginSettings.instance( )
						.getAggregateFunction( aggrFunc );
				if ( aFunc != null
						&& aFunc.getType( ) == IAggregateFunction.RUNNING_AGGR )
				{
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Check if summary aggregates are set on chart.
	 * 
	 * @param cm
	 * @return set or not
	 * @throws ChartException
	 */
	public static boolean isSetSummaryAggregation( Chart cm )
			throws ChartException
	{
		SeriesDefinition baseSD = ChartUtil.getBaseSeriesDefinitions( cm )
				.get( 0 );
		for ( SeriesDefinition orthoSD : ChartUtil.getAllOrthogonalSeriesDefinitions( cm ) )
		{
			for ( Query query : orthoSD.getDesignTimeSeries( )
					.getDataDefinition( ) )
			{
				String aggrFunc = ChartUtil.getAggregateFuncExpr( orthoSD,
						baseSD,
						query );
				if ( aggrFunc == null )
				{
					continue;
				}
				IAggregateFunction aFunc = PluginSettings.instance( )
						.getAggregateFunction( aggrFunc );
				if ( aFunc != null
						&& aFunc.getType( ) == IAggregateFunction.SUMMARY_AGGR )
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Check if chart has aggregation.
	 * 
	 * @param cm
	 */
	public static boolean hasAggregation( Chart cm )
	{
		SeriesDefinition baseSD = null;
		if ( cm instanceof ChartWithAxes )
		{
			ChartWithAxes cwa = (ChartWithAxes) cm;
			baseSD = cwa.getBaseAxes( )[0].getSeriesDefinitions( )
					.get( 0 );
		}
		else if ( cm instanceof ChartWithoutAxes )
		{
			ChartWithoutAxes cwoa = (ChartWithoutAxes) cm;
			baseSD = cwoa.getSeriesDefinitions( ).get( 0 );
		}

		// Check base is set aggregation.
		if ( isBaseGroupingDefined( baseSD )
				&& !ChartUtil.isEmpty( baseSD.getGrouping( )
						.getAggregateExpression( ) ) )
		{
			return true;
		}

		// Check if aggregation is just set on value series.
		try
		{
			if ( cm instanceof ChartWithAxes )
			{
				EList<Axis> axisList = ( (ChartWithAxes) cm ).getAxes( )
						.get( 0 )
						.getAssociatedAxes( );
				for ( Axis a : axisList )
				{
					for ( SeriesDefinition orthSD : a.getSeriesDefinitions( ) )
					{
						for ( Query query : orthSD.getDesignTimeSeries( )
								.getDataDefinition( ) )
						{
							if ( ChartUtil.getAggregateFuncExpr( orthSD,
									baseSD,
									query ) != null )
							{
								return true;
							}
						}
					}
				}
			}
			else if ( cm instanceof ChartWithoutAxes )
			{
				for ( SeriesDefinition orthSD : ( (ChartWithoutAxes) cm ).getSeriesDefinitions( )
						.get( 0 )
						.getSeriesDefinitions( ) )
				{
					for ( Query query : orthSD.getDesignTimeSeries( )
							.getDataDefinition( ) )
					{
						if ( ChartUtil.getAggregateFuncExpr( orthSD,
								baseSD,
								query ) != null )
						{
							return true;
						}
					}
				}
			}
		}
		catch ( ChartException e )
		{
			logger.log( e );
		}

		return false;
	}

	/**
	 * Finds chart report item from handle
	 * 
	 * @param eih
	 *            extended item handle with chart
	 * @since 2.3
	 */
	public static ChartReportItemImpl getChartReportItemFromHandle(
			ExtendedItemHandle eih )
	{
		ChartReportItemImpl item = null;
		if ( !isChartHandle( eih ) )
		{
			return null;
		}
		try
		{
			item = (ChartReportItemImpl) eih.getReportItem( );
		}
		catch ( ExtendedElementException e )
		{
			logger.log( e );
		}
		if ( item == null )
		{
			try
			{
				eih.loadExtendedElement( );
				item = (ChartReportItemImpl) eih.getReportItem( );
			}
			catch ( ExtendedElementException eeex )
			{
				logger.log( eeex );
			}
			if ( item == null )
			{
				logger.log( ILogger.ERROR,
						Messages.getString( "ChartReportItemPresentationImpl.log.UnableToLocateWrapper" ) ); //$NON-NLS-1$
			}
		}
		return item;
	}

	/**
	 * Checks if the object is handle with Chart model
	 * 
	 * @param content
	 *            the object to check
	 * @since 2.3
	 */
	public static boolean isChartHandle( Object content )
	{
		return content instanceof ExtendedItemHandle
				&& CHART_EXTENSION_NAME.equals( ( (ExtendedItemHandle) content ).getExtensionName( ) );
	}

	/**
	 * Finds Chart model from handle
	 * 
	 * @param handle
	 *            the handle with chart
	 * @since 2.3
	 */
	public static Chart getChartFromHandle( ExtendedItemHandle handle )
	{
		ChartReportItemImpl item = getChartReportItemFromHandle( handle );
		if ( item == null )
		{
			return null;
		}
		return (Chart) ( item ).getProperty( PROPERTY_CHART );
	}

	/**
	 * Gets all column bindings. If the handle's contain has column bindings,
	 * will combine the bindings with the handle's.
	 * 
	 * @param itemHandle
	 *            handle
	 * @return the iterator of all column bindings
	 * @since 2.3
	 */
	public static Iterator<ComputedColumnHandle> getAllColumnBindingsIterator(
			ReportItemHandle itemHandle )
	{
		ReportItemHandle container = ChartReportItemUtil.getBindingHolder( itemHandle );
		if ( container != null && container != itemHandle )
		{
			// Add all bindings to an iterator
			List<ComputedColumnHandle> allBindings = new ArrayList<ComputedColumnHandle>( );
			for ( Iterator<ComputedColumnHandle> ownBindings = itemHandle.columnBindingsIterator( ); ownBindings.hasNext( ); )
			{
				allBindings.add( ownBindings.next( ) );
			}
			for ( Iterator<ComputedColumnHandle> containerBindings = container.columnBindingsIterator( ); containerBindings.hasNext( ); )
			{
				allBindings.add( containerBindings.next( ) );
			}
			return allBindings.iterator( );
		}
		return itemHandle.columnBindingsIterator( );
	}

	/**
	 * Transforms dimension value to points.
	 * 
	 * @param handle
	 * @param dpi
	 *            to convert px unit
	 * 
	 * @return the dimension value with measure of points
	 * @since 2.3
	 */
	public static double convertToPoints(
			org.eclipse.birt.report.model.api.DimensionHandle handle, int dpi )
	{
		double retValue = 0.0;

		if ( handle.getMeasure( ) > 0
				&& handle.getUnits( ).trim( ).length( ) > 0 )
		{
			if ( handle.getUnits( ) == DesignChoiceConstants.UNITS_PT )
			{
				retValue = handle.getMeasure( );
			}
			else if ( handle.getUnits( ) == DesignChoiceConstants.UNITS_PX )
			{
				retValue = ( handle.getMeasure( ) * 72d ) / dpi;
			}
			else
			{
				retValue = DimensionUtil.convertTo( handle.getMeasure( ),
						handle.getUnits( ),
						DesignChoiceConstants.UNITS_PT ).getMeasure( );
			}
		}
		return retValue;
	}

	/**
	 * Creates the default bounds for chart model.
	 * 
	 * @param eih
	 *            chart handle
	 * @param cm
	 *            chart model
	 * @return default bounds
	 * @since 2.3
	 */
	public static Bounds createDefaultChartBounds( ExtendedItemHandle eih,
			Chart cm )
	{
		// Axis chart case
		if ( ChartXTabUtil.isAxisChart( eih ) )
		{
			// Axis chart must be ChartWithAxes
			ChartWithAxes cmWA = (ChartWithAxes) cm;
			if ( cmWA.isTransposed( ) )
			{
				return BoundsImpl.create( 0,
						0,
						DEFAULT_CHART_BLOCK_WIDTH,
						DEFAULT_AXIS_CHART_BLOCK_SIZE );
			}
			else
			{
				return BoundsImpl.create( 0,
						0,
						DEFAULT_AXIS_CHART_BLOCK_SIZE,
						DEFAULT_CHART_BLOCK_HEIGHT );
			}
		}
		// Plot or ordinary chart case
		else
		{
			return BoundsImpl.create( 0,
					0,
					DEFAULT_CHART_BLOCK_WIDTH,
					DEFAULT_CHART_BLOCK_HEIGHT );
		}
	}

	/**
	 * Convert model/engine aggregate expression to chart.
	 * 
	 * @param agg
	 * @since 2.3
	 */
	public static String convertToChartAggExpression( String agg )
	{

		if ( DesignChoiceConstants.AGGREGATION_FUNCTION_SUM.equalsIgnoreCase( agg ) )
		{
			return PluginSettings.DefaultAggregations.SUM;

		}
		else if ( DesignChoiceConstants.AGGREGATION_FUNCTION_AVERAGE.equalsIgnoreCase( agg ) )
		{
			return PluginSettings.DefaultAggregations.AVERAGE;

		}
		else if ( DesignChoiceConstants.AGGREGATION_FUNCTION_COUNT.equalsIgnoreCase( agg ) )
		{
			return PluginSettings.DefaultAggregations.COUNT;

		}
		else if ( DesignChoiceConstants.AGGREGATION_FUNCTION_COUNTDISTINCT.equalsIgnoreCase( agg ) )
		{
			return PluginSettings.DefaultAggregations.DISTINCT_COUNT;

		}
		else if ( DesignChoiceConstants.AGGREGATION_FUNCTION_FIRST.equalsIgnoreCase( agg ) )
		{
			return PluginSettings.DefaultAggregations.FIRST;

		}
		else if ( DesignChoiceConstants.AGGREGATION_FUNCTION_LAST.equalsIgnoreCase( agg ) )
		{
			return PluginSettings.DefaultAggregations.LAST;

		}
		else if ( DesignChoiceConstants.AGGREGATION_FUNCTION_MIN.equalsIgnoreCase( agg ) )
		{
			return PluginSettings.DefaultAggregations.MIN;

		}
		else if ( DesignChoiceConstants.AGGREGATION_FUNCTION_MAX.equalsIgnoreCase( agg ) )
		{
			return PluginSettings.DefaultAggregations.MAX;
		}
		else if ( DesignChoiceConstants.AGGREGATION_FUNCTION_WEIGHTEDAVG.equalsIgnoreCase( agg ) )
		{
			return PluginSettings.DefaultAggregations.WEIGHTED_AVERAGE;
		}
		else if ( DesignChoiceConstants.AGGREGATION_FUNCTION_MEDIAN.equalsIgnoreCase( agg ) )
		{
			return PluginSettings.DefaultAggregations.MEDIAN;
		}
		else if ( DesignChoiceConstants.AGGREGATION_FUNCTION_MODE.equalsIgnoreCase( agg ) )
		{
			return PluginSettings.DefaultAggregations.MODE;
		}
		else if ( DesignChoiceConstants.AGGREGATION_FUNCTION_STDDEV.equalsIgnoreCase( agg ) )
		{
			return PluginSettings.DefaultAggregations.STDDEV;
		}
		else if ( DesignChoiceConstants.AGGREGATION_FUNCTION_VARIANCE.equalsIgnoreCase( agg ) )
		{
			return PluginSettings.DefaultAggregations.VARIANCE;
		}
		else if ( DesignChoiceConstants.AGGREGATION_FUNCTION_IRR.equalsIgnoreCase( agg ) )
		{
			return PluginSettings.DefaultAggregations.IRR;
		}
		else if ( DesignChoiceConstants.AGGREGATION_FUNCTION_MIRR.equalsIgnoreCase( agg ) )
		{
			return PluginSettings.DefaultAggregations.MIRR;
		}
		else if ( DesignChoiceConstants.AGGREGATION_FUNCTION_NPV.equalsIgnoreCase( agg ) )
		{
			return PluginSettings.DefaultAggregations.NPV;
		}
		else if ( DesignChoiceConstants.AGGREGATION_FUNCTION_PERCENTILE.equalsIgnoreCase( agg ) )
		{
			return PluginSettings.DefaultAggregations.PERCENTILE;
		}
		else if ( DesignChoiceConstants.AGGREGATION_FUNCTION_TOP_QUARTILE.equalsIgnoreCase( agg ) )
		{
			return PluginSettings.DefaultAggregations.QUARTILE;
		}
		else if ( DesignChoiceConstants.AGGREGATION_FUNCTION_MOVINGAVE.equalsIgnoreCase( agg ) )
		{
			return PluginSettings.DefaultAggregations.MOVING_AVERAGE;
		}
		else if ( DesignChoiceConstants.AGGREGATION_FUNCTION_RUNNINGSUM.equalsIgnoreCase( agg ) )
		{
			return PluginSettings.DefaultAggregations.RUNNING_SUM;
		}
		else if ( DesignChoiceConstants.AGGREGATION_FUNCTION_RUNNINGNPV.equalsIgnoreCase( agg ) )
		{
			return PluginSettings.DefaultAggregations.RUNNING_NPV;
		}
		else if ( DesignChoiceConstants.AGGREGATION_FUNCTION_RANK.equalsIgnoreCase( agg ) )
		{
			return PluginSettings.DefaultAggregations.RANK;
		}
		else if ( DesignChoiceConstants.AGGREGATION_FUNCTION_IS_TOP_N.equalsIgnoreCase( agg ) )
		{
			return PluginSettings.DefaultAggregations.TOP;
		}
		else if ( DesignChoiceConstants.AGGREGATION_FUNCTION_IS_TOP_N_PERCENT.equalsIgnoreCase( agg ) )
		{
			return PluginSettings.DefaultAggregations.TOP_PERCENT;
		}
		else if ( DesignChoiceConstants.AGGREGATION_FUNCTION_IS_BOTTOM_N.equalsIgnoreCase( agg ) )
		{
			return PluginSettings.DefaultAggregations.BOTTOM;
		}
		else if ( DesignChoiceConstants.AGGREGATION_FUNCTION_IS_BOTTOM_N_PERCENT.equalsIgnoreCase( agg ) )
		{
			return PluginSettings.DefaultAggregations.BOTTOM_PERCENT;
		}
		else if ( DesignChoiceConstants.AGGREGATION_FUNCTION_PERCENT_RANK.equalsIgnoreCase( agg ) )
		{
			return PluginSettings.DefaultAggregations.PERCENT_RANK;
		}
		else if ( DesignChoiceConstants.AGGREGATION_FUNCTION_PERCENT_SUM.equalsIgnoreCase( agg ) )
		{
			return PluginSettings.DefaultAggregations.PERCENT_SUM;
		}
		else if ( DesignChoiceConstants.AGGREGATION_FUNCTION_RUNNINGCOUNT.equalsIgnoreCase( agg ) )
		{
			return PluginSettings.DefaultAggregations.RUNNING_COUNT;
		}

		return null;
	}

	/**
	 * Check if chart is child of multi-views handle.
	 * 
	 * @param handle
	 * @since 2.3
	 */
	public static boolean isChildOfMultiViewsHandle( DesignElementHandle handle )
	{
		if ( handle != null
				&& handle.getContainer( ) instanceof MultiViewsHandle )
		{
			return true;
		}
		return false;
	}

	/**
	 * Checks if the expression contains string. e.g.
	 * data["year"]+"Q"+data["quarter"]
	 * 
	 * @param expression
	 * @return true if contains
	 */
	public static boolean checkStringInExpression( String expression )
	{
		boolean haveString = false;

		for ( int i = 0; i < expression.length( ); i++ )
		{
			if ( expression.charAt( i ) == '"' )
			{
				if ( i == 0 || i == expression.length( ) - 1 )
				{
					haveString = true;
					break;
				}
				else
				{
					if ( expression.charAt( i - 1 ) != '['
							&& expression.charAt( i + 1 ) != ']' )
					{
						haveString = true;
						break;
					}
				}

			}
		}
		return haveString;
	}

	/**
	 * Compare version number, the format of version number should be X.X.X
	 * style.
	 * 
	 * @param va
	 *            version number 1.
	 * @param vb
	 *            version number 2.
	 * @since 2.3
	 */
	public static int compareVersion( String va, String vb )
	{
		return ChartUtil.compareVersion( va, vb );
	}

	/**
	 * Check if specified expression is a grouping expression of shared report
	 * item.
	 * 
	 * @param expression
	 * @param handle
	 * @return
	 */
	private static boolean isSharedGroupExpression( String expression,
			ReportItemHandle handle )
	{
		ReportItemHandle itemHandle = getReportItemReference( handle );
		if ( itemHandle instanceof ListingHandle )
		{
			List<GroupHandle> groupList = new ArrayList<GroupHandle>( );
			SlotHandle groups = ( (ListingHandle) itemHandle ).getGroups( );
			for ( Iterator<GroupHandle> iter = groups.iterator( ); iter.hasNext( ); )
			{
				groupList.add( iter.next( ) );
			}
			
			if ( groupList.size( ) == 0 )
			{
				return false;
			}
			for ( GroupHandle gh : groupList )
			{
				if ( expression.equals( gh.getKeyExpr( ) ) )
				{
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Checks if chart should use internal grouping or DTE grouping.
	 * 
	 * @param chartHandle
	 *            handle with version
	 * @return true means old report using internal grouping
	 * @since 2.3.1
	 */
	public static boolean isOldChartUsingInternalGroup(
			ReportItemHandle chartHandle, Chart cm )
	{
		if ( compareVersion( cm.getVersion( ), "2.5.0" ) >= 0 ) //$NON-NLS-1$
		{
			return false;
		}
		
		String reportVer = chartHandle.getModuleHandle( ).getVersion( );
		if ( reportVer == null || compareVersion( reportVer, "3.2.16" ) < 0 ) //$NON-NLS-1$
		{
			return true;
		}

		// Since if the chart is serilized into a document, the version number
		// will always be
		// the newest, so we can only detect an old chart using internal group
		// with following facts:
		// 1. the chart has an gouping on base seriesDefination
		// 2. shared binding is used.
		// 3. the shared binding is not grouped.
		if ( chartHandle.getDataBindingReference( ) != null
				&& ChartReportItemUtil.isBaseGroupingDefined( cm )
				&& !isSharedGroupExpression( ChartUtil.getCategoryExpressions( cm )[0],
						chartHandle ) )
		{
			return true;
		}
		return false;
	}

	/**
	 * Checks if string is a single query expression
	 * 
	 * @param expr
	 */
	public static boolean isSimpleExpression( String expr )
	{
		String rowPattern = "row\\[.*\\]"; //$NON-NLS-1$
		String dataPattern = "data\\[.*\\]"; //$NON-NLS-1$
		boolean matches = expr.matches( rowPattern )
				|| expr.matches( dataPattern );
		boolean isSingle = expr.indexOf( "]" ) == expr.length( ) - 1; //$NON-NLS-1$
		return matches && isSingle;
	}

	/**
	 * Returns report item reference of specified item handle.
	 * 
	 * @param itemHandle
	 * @since 2.3
	 */
	public static ReportItemHandle getReportItemReference(
			ReportItemHandle itemHandle )
	{
		return getReportItemReferenceImpl( itemHandle, itemHandle );
	}

	/**
	 * Returns item handle reference.
	 * 
	 * @param currentItemHandle
	 * @param itemHandle
	 * @return
	 * @since 2.3
	 */
	private static ReportItemHandle getReportItemReferenceImpl(
			final ReportItemHandle currentItemHandle,
			final ReportItemHandle itemHandle )
	{
		ReportItemHandle handle = currentItemHandle.getDataBindingReference( );
		if ( handle == null )
		{
			if ( currentItemHandle.getContainer( ) instanceof MultiViewsHandle )
			{
				return getReportItemReferenceImpl( (ReportItemHandle) currentItemHandle.getContainer( )
						.getContainer( ),
						itemHandle );
			}
			else if ( currentItemHandle == itemHandle )
			{
				return null;
			}

			return currentItemHandle;
		}

		return getReportItemReferenceImpl( handle, itemHandle );
	}

	/**
	 * Check if specified report item handle is related to chart.
	 * 
	 * @param handle
	 * @since 2.3
	 */
	public static boolean isChartReportItemHandle( ReportItemHandle handle )
	{
		if ( handle instanceof ExtendedItemHandle
				&& getChartFromHandle( (ExtendedItemHandle) handle ) != null )
		{
			return true;
		}
		return false;
	}

	/**
	 * Return the binding name of row["binding"]
	 * 
	 * @param expr
	 *            expression
	 * @param hasOperation
	 *            indicates if operation can be allowed in expression
	 */
	public static String getRowBindingName( String expr, boolean hasOperation )
	{
		if ( !isRowBinding( expr, hasOperation ) )
			return null;
		if ( hasOperation )
		{
			return expr.replaceFirst( ".*\\Qrow[\"\\E", "" ) //$NON-NLS-1$ //$NON-NLS-2$
					.replaceFirst( "\\Q\"]\\E.*", "" ); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return expr.replaceFirst( "\\Qrow[\"\\E", "" ) //$NON-NLS-1$ //$NON-NLS-2$
				.replaceFirst( "\\Q\"]\\E", "" ); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Checks if the expression references a binding name
	 * 
	 * @param expr
	 *            expression
	 * @param hasOperation
	 *            indicates if operation can be allowed in expression
	 */
	public static boolean isRowBinding( String expr, boolean hasOperation )
	{
		if ( expr == null )
		{
			return false;
		}
		
		String regExp = hasOperation ? ".*\\Qrow[\"\\E.*\\Q\"]\\E.*" //$NON-NLS-1$
				: "\\Qrow[\"\\E.*\\Q\"]\\E"; //$NON-NLS-1$
		
		boolean result = expr.matches( regExp );
		if ( hasOperation )
		{
			return result;
		}
		else if ( result
				&& containsOnce( expr, "row[\"" ) && containsOnce( expr, "\"]" ) )//$NON-NLS-1$ //$NON-NLS-2$
		{
			// It is a regular row binding.
			return true;
		}

		return false;
	}

	/**
	 * Check if expression only contains specified substring once.
	 * 
	 * @param expr
	 * @param substring
	 * @return
	 */
	private static boolean containsOnce( String expr, String substring )
	{
		String text = expr;
		int index = text.indexOf( substring );
		if ( index < 0 )
		{
			return false;
		}

		text = text.substring( index + substring.length( ) - 1 );
		if ( text.indexOf( substring ) >= 0 )
		{
			return false;
		}

		return true;
	}
	
	public static String createBindingNameForRowExpression( String expr )
	{
		if ( isRowBinding( expr, false ) )
		{
			return getRowBindingName( expr, false );
		}
		if ( isRowBinding( expr, true ) )
		{
			return ChartUtil.removeInvalidSymbols( expr );
		}
		return expr; // The specified expression might be a binding name,
						// directly return.
	}
	
	/**
	 * In some cases, if the expression in subquery is a simple binding, and
	 * this binding is from parent query, should copy the binding from parent
	 * and insert into subquery.
	 * 
	 * @param query
	 *            subquery
	 * @param expr
	 *            expression
	 * @throws DataException
	 * @since 2.3.1 and 2.4.0
	 */
	public static void copyAndInsertBindingFromContainer(
			ISubqueryDefinition query, String expr ) throws DataException
	{
		String bindingName = getRowBindingName( expr, false );
		if ( bindingName != null
				&& !query.getBindings( ).containsKey( bindingName )
				&& query.getParentQuery( )
						.getBindings( )
						.containsKey( bindingName ) )
		{
			// Copy the binding from container and insert it into
			// subquery
			IBinding parentBinding = (IBinding) query.getParentQuery( )
					.getBindings( )
					.get( bindingName );
			IBinding binding = new Binding( bindingName,
					parentBinding.getExpression( ) );
			binding.setAggrFunction( parentBinding.getAggrFunction( ) );
			binding.setDataType( parentBinding.getDataType( ) );
			binding.setDisplayName( parentBinding.getDisplayName( ) );
			binding.setFilter( parentBinding.getFilter( ) );
			// Exportable is true for new subquery bindings
			query.addBinding( binding );
		}
	}

	/**
	 * The field indicates it will revise chart model under reference report
	 * item case.
	 */
	public static final int REVISE_REFERENCE_REPORT_ITEM = 1;

	/**
	 * Revise chart model.
	 * 
	 * @param reviseType
	 * @param cm
	 * @param itemHandle
	 */
	public static void reviseChartModel( int reviseType, Chart cm,
			ReportItemHandle itemHandle )
	{
		switch ( reviseType )
		{
			case REVISE_REFERENCE_REPORT_ITEM :
				if ( itemHandle.getDataBindingReference( ) != null
						&& ChartReportItemUtil.isBaseGroupingDefined( cm )
						&& !isSharedGroupExpression( ChartUtil.getCategoryExpressions( cm )[0],
								itemHandle ) )
				{
					// In older version of chart, it is allowed to set grouping
					// on category series when sharing report item, but now it
					// isn't allowed, so this calls will revise chart model to
					// remove category series grouping flag for the case.
					SeriesDefinition baseSD = null;
					if ( cm instanceof ChartWithAxes )
					{
						ChartWithAxes cwa = (ChartWithAxes) cm;
						baseSD = cwa.getBaseAxes( )[0].getSeriesDefinitions( )
								.get( 0 );
					}
					else if ( cm instanceof ChartWithoutAxes )
					{
						ChartWithoutAxes cwoa = (ChartWithoutAxes) cm;
						baseSD = cwoa.getSeriesDefinitions( ).get( 0 );
					}
					if ( baseSD != null && baseSD.getGrouping( ) != null )
					{
						baseSD.getGrouping( ).unsetEnabled( );
					}
				}
				break;
		}
	}
	
	/**
	 * Checks if chart inherits groupings and aggregations from container
	 * 
	 * @param handle
	 *            chart handle
	 * @return inherits groupings or not
	 * @since 2.5
	 */
	public static boolean isChartInheritGroups( ReportItemHandle handle )
	{
		return handle.getDataSet( ) == null
				&& ( handle.getContainer( ) instanceof CellHandle
						|| handle.getContainer( ) instanceof ListHandle || handle.getContainer( ) instanceof ListGroupHandle )
				&& !handle.getBooleanProperty( ChartReportItemConstants.PROPERTY_INHERIT_COLUMNS );
	}
}
